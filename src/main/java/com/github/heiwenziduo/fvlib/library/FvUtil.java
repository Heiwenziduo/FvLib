package com.github.heiwenziduo.fvlib.library;

import com.github.heiwenziduo.fvlib.api.mixin.LivingEntityMixinAPI;
import com.github.heiwenziduo.fvlib.library.api.DispelType;
import com.github.heiwenziduo.fvlib.library.effect.FvHookedEffect;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

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
        // every source not PURE(generic pure XD) and have BYPASSES_ARMOR tag will be defined as magic
        return damage.is(DamageTypeTags.BYPASSES_ARMOR) && !damage.is(PURE) && !damage.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damage.is(DamageTypeTags.BYPASSES_EFFECTS);
    }

    /// just define if an effect can be cured, it's a dispellable effect
    public static boolean isGenericDispellable(MobEffect effect) {
        return !effect.getCurativeItems().isEmpty();
    }

    /// Dispel a living. iterator see: {@link LivingEntity#tickEffects()}
    /// @return dispelled effects number
    public static int dispel(LivingEntity target, LivingEntity from, DispelType type) {
        int i = 0;
        var dispelledType = target.isAlliedTo(from) ? MobEffectCategory.BENEFICIAL : MobEffectCategory.HARMFUL; //中性效果暂时不管
        Iterator<MobEffect> iterator = target.getActiveEffectsMap().keySet().iterator();
        // 遍历的同时修改原list: iterator
        try {
            while (iterator.hasNext()) {
                MobEffect effect = iterator.next();
                MobEffectInstance mobeffectinstance = target.getEffect(effect);
                if (mobeffectinstance == null) continue;
                // not right type we want to dispel
                if (effect.getCategory() != dispelledType) continue;

                if (effect instanceof FvHookedEffect fvEffect) {
                    if(fvEffect.isDispellable(type)) {
                        iterator.remove();
                        target.onEffectRemoved(mobeffectinstance);
                        i++;
                    }
                } else if (isGenericDispellable(effect)) {
                    iterator.remove();
                    target.onEffectRemoved(mobeffectinstance);
                    i++;
                }
            }
        } catch (ConcurrentModificationException ignored) {}
        return i;
    }
    /// Dispel a living
    /// @return dispelled effects number
    public static int dispel(LivingEntity target, DispelType type) {
        return dispel(target, target, type);
    }
}
