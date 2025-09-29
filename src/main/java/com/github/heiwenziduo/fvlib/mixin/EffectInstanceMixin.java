package com.github.heiwenziduo.fvlib.mixin;

import com.github.heiwenziduo.fvlib.api.mixin.EffectInstanceMixinAPI;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/// just for test
@Mixin(value = MobEffectInstance.class)
public abstract class EffectInstanceMixin implements EffectInstanceMixinAPI {

    @Shadow private int duration;

    @Override
    public void fvLib$setEffectDuration(int tick) {
        if (tick > 0){
            duration = tick;
        }
    }
}
