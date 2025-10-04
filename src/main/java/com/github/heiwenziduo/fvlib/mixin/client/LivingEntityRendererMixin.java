package com.github.heiwenziduo.fvlib.mixin.client;

import com.github.heiwenziduo.fvlib.client.manager.TimelockRenderManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.heiwenziduo.fvlib.api.capability.FvCapabilitiesProvider.FV_CAPA;

@Mixin(value = LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    // 10/01 只是为了获取混淆前的方法名

    @Inject(method = "setupRotations", at = @At("HEAD"))
    public void setupRotations(T pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks, CallbackInfo ci) {

    }

    @Inject(method = "scale", at = @At("HEAD"))
    public void scale(T pLivingEntity, PoseStack pPoseStack, float pPartialTickTime, CallbackInfo ci) {

    }

    @Inject(method = "getBob", at = @At("HEAD"))
    public void getBob(T pLivingBase, float pPartialTick, CallbackInfoReturnable<Float> cir) {

    }

    /// 拦截partialTick, 可以把大多数动画锁定
    @ModifyVariable(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD"), ordinal = 1, argsOnly = true)
    public float render(float partialTick, T living) {
        AtomicBoolean isLocked = new AtomicBoolean(false);
        living.getCapability(FV_CAPA).ifPresent(capa -> {
            if (capa.isTimelocked()) isLocked.set(true);
        });

        AtomicReference<Float> partial = new AtomicReference<>(partialTick);
        if (isLocked.get()) {
            TimelockRenderManager.getInstance().getSnapshot(living.getId()).ifPresentOrElse(snap -> {
                partial.set(snap.partialTick());
            }, () -> {
                TimelockRenderManager.getInstance().makeSnapshot(living.getId(), partialTick);
            });
        }
        return partial.get();
    }
}
