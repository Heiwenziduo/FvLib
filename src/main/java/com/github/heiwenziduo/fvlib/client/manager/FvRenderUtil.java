package com.github.heiwenziduo.fvlib.client.manager;

import com.github.heiwenziduo.fvlib.mixin.accessor.LivingEntityRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import org.jetbrains.annotations.NotNull;

public class FvRenderUtil {
    /// 用于在Renderer#render以外进行生物模型渲染时应用一些基本逻辑, 应当放在压入帧的最后, renderToBuffer之前 <br>参<br>
    /// {@link LivingEntityRenderer}
    public static void livingRenderOutsideRenderer(RenderLivingEvent.Post<LivingEntity, ?> event, float partialTicks, boolean renderLayer, float layerAlpha) {
        LivingEntity living = event.getEntity();
        LivingEntityRenderer<LivingEntity, ?> renderer = event.getRenderer();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = event.getMultiBufferSource();
        int packedLight = event.getPackedLight();

        float f = Mth.rotLerp(partialTicks, living.yBodyRotO, living.yBodyRot);
        float f7 = renderer.getBob(living, partialTicks);
        renderer.setupRotations(living, poseStack, f7, f, partialTicks);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        renderer.scale(living, poseStack, partialTicks);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        // 渲染一个模型相关的所有layer层
        if (renderLayer) {
            // # 这里忽略了实体在 睡觉, 骑乘, 倒置("Dinnerbone") 状态下的模型变换
            float f1 = Mth.rotLerp(partialTicks, living.yHeadRotO, living.yHeadRot);
            float f6 = Mth.lerp(partialTicks, living.xRotO, living.getXRot());
            float f2 = f1 - f;

            float f8 = 0.0F, f5 = 0.0F;
            boolean shouldSit = living.isPassenger() && (living.getVehicle() != null && living.getVehicle().shouldRiderSit());
            if (!shouldSit && living.isAlive()) {
                f8 = living.walkAnimation.speed(partialTicks);
                f5 = living.walkAnimation.position(partialTicks);
                if (living.isBaby()) {
                    f5 *= 3.0F;
                }

                f8 = Math.min(f8, 1.0f);
            }

            for(var renderlayer : ((LivingEntityRendererAccessor) renderer).getLayers()) {
                // todo: layer层调为透明
                //renderlayer.render(poseStack, new TranslucentBufferSourceWrapper(bufferSource, layerAlpha), packedLight, living, f5, f8, partialTicks, f7, f2, f6);
                renderlayer.render(poseStack, bufferSource, packedLight, living, f5, f8, partialTicks, f7, f2, f6);
            }
        }
    }

    /// 用于在Renderer#render以外进行生物模型渲染时应用一些基本逻辑, 应当放在压入帧的最后, renderToBuffer之前 <br>参<br>
    /// {@link LivingEntityRenderer}
    public static void livingRenderOutsideRenderer(RenderLivingEvent.Post<LivingEntity, ?> event, float partialTicks) {
        livingRenderOutsideRenderer(event, partialTicks, false, 1);
    }





    /// layer强转透明
    public static class TranslucentBufferSourceWrapper implements MultiBufferSource {
        private final MultiBufferSource delegate;
        private final float alpha;
        public TranslucentBufferSourceWrapper(MultiBufferSource buffer, float pAlpha) {
            delegate = buffer;
            alpha = pAlpha;
        }

        @Override
        public @NotNull VertexConsumer getBuffer(RenderType pRenderType) {
            //RenderType translucentType = RenderType.entityTranslucent(pRenderType.textureState().texture.get());
            RenderType translucentType = RenderType.entityTranslucent(ResourceLocation.parse(""));
            return new AlphaModifyingVertexConsumer(delegate.getBuffer(translucentType), alpha);
        }
    }

    ///
    public static class AlphaModifyingVertexConsumer implements VertexConsumer {
        private final VertexConsumer delegate;
        private final float alpha;

        public AlphaModifyingVertexConsumer(VertexConsumer consumer, float pAlpha) {
            delegate = consumer;
            alpha = pAlpha;
        }

        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            return delegate.vertex(x, y, z);
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int pAlpha) {
            // 强制使用传入的alpha
            return delegate.color(red, green, blue, (int) (alpha * 255));
        }

        @Override
        public VertexConsumer uv(float u, float v) {
            return delegate.uv(u, v);
        }

        @Override
        public VertexConsumer overlayCoords(int u, int v) {
            return delegate.overlayCoords(u, v);
        }

        @Override
        public VertexConsumer uv2(int u, int v) {
            return delegate.uv2(u, v);
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            return delegate.normal(x, y, z);
        }

        @Override
        public void endVertex() {
            delegate.endVertex();
        }

        @Override
        public void defaultColor(int pDefaultR, int pDefaultG, int pDefaultB, int pDefaultA) {

        }

        @Override
        public void unsetDefaultColor() {

        }
    }
}
