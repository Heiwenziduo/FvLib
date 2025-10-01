package com.github.heiwenziduo.fvlib.client.manager;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

    // 在ClientTickEvent中调用，用于更新和移除过期的动画
    public void tick(ClientLevel level) {
        if (level == null) return;
        long currentTime = level.getGameTime();
        activeAnimations.entrySet().removeIf(entry -> {
            Entity entity = level.getEntity(entry.getKey());
            // 如果实体不存在或动画已结束，则移除
            return entity == null || !entity.isAlive() || currentTime > entry.getValue().startTime + ANIMATION_DURATION_TICKS;
        });
    }

    // 供渲染器调用，获取动画信息
    public Optional<EvasionAnimation> getAnimation(int entityId) {
        return Optional.ofNullable(activeAnimations.get(entityId));
    }

    // 存储动画状态
    public record EvasionAnimation(long startTime, Vec3 slideDirect) {
    // 获取动画进度 (0.0 to 1.0)
        public float getProgress(float partialTicks) {
            long currentTime = Minecraft.getInstance().level.getGameTime();
            float ticksPassed = (currentTime - startTime) + partialTicks;
            return Mth.clamp(ticksPassed / (float) ANIMATION_DURATION_TICKS, 0.0f, 1.0f);
        }
    }

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
        int color = (alpha << 24) | 0xFFFFFF; // 白色，带有透明度

        Matrix4f matrix4f = poseStack.last().pose();
        font.drawInBatch(text, textWidth, yOffset, color, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);

        poseStack.popPose();
    }
}
