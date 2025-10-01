package com.github.heiwenziduo.fvlib.client.manager;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/// OnlyIn: [CLIENT] <br>
/// 客户端单例管理动画状态
@OnlyIn(Dist.CLIENT)
public class EvasionEffectManager {
    public static final EvasionEffectManager INSTANCE = new EvasionEffectManager();

    public EvasionEffectManager() {

    }

    private final Map<Integer, EvasionAnimation> activeAnimations = new ConcurrentHashMap<>();

    // 动画总时长（ticks）
    private static final int ANIMATION_DURATION_TICKS = 10; // 0.5秒

    public static EvasionEffectManager getInstance() {
        return INSTANCE;
    }

    // 开始一个动画
    public void startEffect(int entityId, Vec3 slideDirect) {
        System.out.println("startEffect: " + slideDirect);
        activeAnimations.put(entityId, new EvasionAnimation(Minecraft.getInstance().level.getGameTime(), slideDirect));
    }

    // 在ClientTickEvent中调用, 用于更新和移除过期的动画
    public void tick(ClientLevel level) {
        if (level == null) return;
        long currentTime = level.getGameTime();
        activeAnimations.entrySet().removeIf(entry -> {
            Entity entity = level.getEntity(entry.getKey());
            // 如果实体不存在或动画已结束, 则移除
            return entity == null || !entity.isAlive() || currentTime > entry.getValue().startTime + ANIMATION_DURATION_TICKS;
        });
    }

    /// 供渲染器调用, 获取动画信息
    public Optional<EvasionAnimation> getAnimation(int entityId) {
        return Optional.ofNullable(activeAnimations.get(entityId));
    }

    /// 存储动画状态
    public record EvasionAnimation(long startTime, Vec3 slideDirect) {
        /// 获取动画进度 (0.0, 1.0)
        public float getProgress(float partialTicks) {
            long currentTime = Minecraft.getInstance().level.getGameTime();
            float ticksPassed = (currentTime - startTime) + partialTicks;
            return Mth.clamp(ticksPassed / (float) ANIMATION_DURATION_TICKS, 0.0f, 1.0f);
        }
    }

    /// render text: MISS
    public static void renderMissText(LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, EvasionAnimation animation, float partialTicks) {
        poseStack.pushPose();

        // 1. 移动到实体头顶
        poseStack.translate(0, entity.getBbHeight() + 0.7, 0);

        // 2. 使文字始终朝向玩家（Billboard effect）
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        poseStack.mulPose(camera.rotation());

        // 3. 缩放文字大小
        poseStack.scale(-0.025f, -0.025f, 0.025f);

        // 4. 获取字体渲染器并绘制
        Font font = Minecraft.getInstance().font;
        String text = "MISS";
        float textWidth = -font.width(text) / 2.0f;

        // 让文字有一个轻微的向上漂浮和淡出效果
        float progress = animation.getProgress(partialTicks);
        float yOffset = progress * 10.0f; // 向上漂浮
        int alpha = (int)((1.0f - progress) * 255.0f); // 淡出
        int color = (alpha << 24) | 0xFFFFFF; // 白色, 带有透明度

        Matrix4f matrix4f = poseStack.last().pose();
        font.drawInBatch(text, textWidth, yOffset, color, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);

        poseStack.popPose();
    }

    /// render evasion phantom
    public static void renderEvasionPhantom(RenderLivingEvent.Post<LivingEntity, ?> event, EvasionAnimation animation, float partialTicks) {
        LivingEntity entity = event.getEntity();
        LivingEntityRenderer<LivingEntity, ?> renderer = event.getRenderer();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = event.getMultiBufferSource();
        int packedLight = event.getPackedLight();

        poseStack.pushPose();

        // 1. 计算动画进度和偏移
        float progress = animation.getProgress(partialTicks);
        float slideDistance = 1.5f; // 残影滑动的总距离
        float currentOffset = Mth.lerp(progress, 0, slideDistance); // 从0插值到最大距离

        // 2. 计算侧向向量
        Vec3 direction = animation.slideDirect();

        // 3. 应用位移
        poseStack.translate(direction.x * currentOffset, 0, direction.z * currentOffset);

        // 4. 设置半透明效果
        // 我们可以通过一个特殊的RenderType来实现。或者一个更简单但效果稍差的方法是直接重载颜色
        float alpha = 1.0f - progress; // 残影随着动画进度逐渐消失
        int red = 255, green = 255, blue = 255;

        // 核心：使用一个封装的MultiBufferSource来修改顶点数据, 使其半透明
        // 这是一个高级技巧, 我们这里用一个更易于理解的方式：直接调用render方法, 但传入修改后的参数
        // 注意：这可能不适用于所有实体, 但对大多数标准实体有效
        // 更好的方法是找到半透明的RenderType并让模型使用它来渲染
        // RenderType.entityTranslucent(renderer.getTextureLocation(entity))

        // 一个简单有效的方法是获取实体的模型, 直接渲染它
        EntityModel<LivingEntity> model = renderer.getModel();
        // 准备渲染半透明模型
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityTranslucent(renderer.getTextureLocation(entity)));
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0f), 1.0f, 1.0f, 1.0f, alpha);


        poseStack.popPose();
    }
}
