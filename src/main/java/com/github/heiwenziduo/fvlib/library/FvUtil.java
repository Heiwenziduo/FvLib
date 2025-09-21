package com.github.heiwenziduo.fvlib.library;

import com.github.heiwenziduo.fvlib.api.mixin.LivingEntityMixinAPI;
import net.minecraft.world.entity.LivingEntity;

/**
 * <p>*welcum to TimeZone*</p>
 * <p><span style="color:#F28383">Waring:&nbsp;</span>1</p>
 */
public class FvUtil {
    /// get time-lock ticks remain on an entity
    public static int getTimeLock(LivingEntity living) {
        return ((LivingEntityMixinAPI) living).FvLib$getTimeLockManager().getTimeLock();
    }

    /// set ticks to time-lock an entity
    public static void setTimeLock(LivingEntity living, int tick) {
        ((LivingEntityMixinAPI) living).FvLib$getTimeLockManager().setTimeLock(tick);
    }
}
