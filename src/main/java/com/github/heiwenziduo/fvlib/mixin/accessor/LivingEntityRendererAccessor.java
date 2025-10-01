package com.github.heiwenziduo.fvlib.mixin.accessor;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LivingEntityRenderer.class)
public interface LivingEntityRendererAccessor {
    /**
     * 通过此访问器获取 'layers' 字段
     */
    @Accessor("layers")
    List<RenderLayer<LivingEntity, EntityModel<LivingEntity>>> getLayers();
}
