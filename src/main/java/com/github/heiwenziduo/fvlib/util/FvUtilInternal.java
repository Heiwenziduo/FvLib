package com.github.heiwenziduo.fvlib.util;

import com.github.heiwenziduo.fvlib.api.mixin.EffectInstanceMixinAPI;
import net.minecraft.world.effect.MobEffectInstance;

/// this util is only for internal usage
public class FvUtilInternal {

    /// set an effectInstance's duration
    public static void setEffectDuration(MobEffectInstance instance, int tick) {
        ((EffectInstanceMixinAPI) instance).fvLib$setEffectDuration(tick);
    }
}
