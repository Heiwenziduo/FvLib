package com.github.heiwenziduo.fvlib.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
}
