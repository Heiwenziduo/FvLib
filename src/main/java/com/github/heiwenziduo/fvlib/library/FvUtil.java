package com.github.heiwenziduo.fvlib.library;

import com.github.heiwenziduo.fvlib.api.mixin.LivingEntityMixinAPI;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static com.github.heiwenziduo.fvlib.data.FvLibDamageType.PURE;

/**
 * <p>*welcum to TimeZone*</p>
 * <p><span style="color:f28383">Waring:&nbsp;</span>every class NOT in library folder will be changing destructively. Make sure you are using the right dependencies</p>
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

    /// Damage Type: &nbsp;<span style="color: 47afff;">MAGIC</span>
    public static boolean isGenericMagic(DamageSource damage) {
        // every source not PURE and have BYPASSES_ARMOR tag will be defined as magic
        return !damage.is(PURE) && !damage.is(DamageTypeTags.BYPASSES_ARMOR);
    }
}
