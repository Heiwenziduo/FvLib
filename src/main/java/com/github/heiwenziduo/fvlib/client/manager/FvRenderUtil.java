package com.github.heiwenziduo.fvlib.client.manager;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;

public class FvRenderUtil {
    /// 用于在Renderer#render以外进行生物模型渲染时应用一些基本逻辑, 应当放在压入帧的最后, renderToBuffer之前 <br>参<br>
    /// {@link LivingEntityRenderer}
    public static void livingRenderOutsideRenderer(RenderLivingEvent.Post<LivingEntity, ?> event, float partialTicks) {
        LivingEntity living = event.getEntity();
        LivingEntityRenderer<LivingEntity, ?> renderer = event.getRenderer();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = event.getMultiBufferSource();

        float f = Mth.rotLerp(partialTicks, living.yBodyRotO, living.yBodyRot);
        float f7 = renderer.getBob(living, partialTicks);
        renderer.setupRotations(living, poseStack, f7, f, partialTicks);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        renderer.scale(living, poseStack, partialTicks);
        poseStack.translate(0.0F, -1.501F, 0.0F);

    }
}
