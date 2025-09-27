package com.github.heiwenziduo.fvlib.util;

import com.github.heiwenziduo.fvlib.api.mixin.EffectInstanceMixinAPI;
import com.github.heiwenziduo.fvlib.api.mixin.LivingEntityMixinAPI;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

/// this util is only for internal usage
public class FvUtilInternal {
    /// set a living's BKB status
    public static void setBKB(LivingEntity living, boolean bkb) {
        ((LivingEntityMixinAPI) living).FvLib$getBKBEffectManager().setBKB(bkb);
    }

    /// set an effectInstance's duration
    public static void setEffectDuration(MobEffectInstance instance, int tick) {
        ((EffectInstanceMixinAPI) instance).fvLib$setEffectDuration(tick);
    }
}
