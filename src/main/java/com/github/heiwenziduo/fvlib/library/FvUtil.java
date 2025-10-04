package com.github.heiwenziduo.fvlib.library;

import com.github.heiwenziduo.fvlib.api.event.FvEventHooks;
import com.github.heiwenziduo.fvlib.library.api.DispelType;
import com.github.heiwenziduo.fvlib.library.effect.FvHookedEffect;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.heiwenziduo.fvlib.api.capability.FvCapabilitiesProvider.FV_CAPA;
import static com.github.heiwenziduo.fvlib.library.registry.FvDamageType.PURE;

/**
 * <p>*welcum to TimeZone*</p>
 * <p><span style="color:f28383">Waring:&nbsp;</span>every class NOT in library folder will be changing destructively. Make sure you are using the right dependencies</p>
 */
public class FvUtil {

    /// get time-lock ticks remain on a living
    public static int getTimeLockTick(@NotNull LivingEntity living) {
        AtomicReference<Short> tick = new AtomicReference<>((short) 0);
        living.getCapability(FV_CAPA).ifPresent(capa -> {
            tick.set(capa.getTimelock());
        });
        return tick.get();
    }

    /// whether the living is timelocked
    public static boolean isTimeLocked(@NotNull LivingEntity living) {
        AtomicBoolean isLocked = new AtomicBoolean(false);
        living.getCapability(FV_CAPA).ifPresent(capa -> {
            isLocked.set(capa.isTimelocked());
        });
        return isLocked.get();
    }

    /// main entry to set living timelock <br>
    /// note that timelock tick is "short" inside, so any number higher than 32767 will be set to short.MAXVALUE <br><br>
    public static void setTimeLock(@NotNull LivingEntity living, int tick) {
        if (living.level().isClientSide) return;

        short tickS = (short) Math.min(tick, Short.MAX_VALUE);
        FvEventHooks.onLivingTimelock(living, tickS);
        living.getCapability(FV_CAPA).ifPresent(capa -> {
            capa.setTimelock(tickS);
        });
    }

    /// check if a living has any BKB effects
    public static boolean hasBKB(@NotNull LivingEntity living) {
        AtomicBoolean haveBKB = new AtomicBoolean(false);
        living.getCapability(FV_CAPA).ifPresent(capa -> {
            haveBKB.set(capa.haveBKB());
        });
        return haveBKB.get();
    }

    /// Damage Type: &nbsp;<span style="color: ff2556;">PHYSIC</span>
    public static boolean isGenericPhysic(@NotNull DamageSource damage) {
        // as long as it does not bypassArmor...
        return !damage.is(DamageTypeTags.BYPASSES_ARMOR) && !damage.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    /// Damage Type: &nbsp;<span style="color: 47afff;">MAGIC</span>
    public static boolean isGenericMagic(@NotNull DamageSource damage) {
        // every source not PURE(generic pure XD) and have BYPASSES_ARMOR tag will be defined as magic
        return damage.is(DamageTypeTags.BYPASSES_ARMOR) && !damage.is(PURE) && !damage.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damage.is(DamageTypeTags.BYPASSES_EFFECTS);
    }

    /// just define if an effect can be cured, it's a dispellable effect
    public static boolean isGenericDispellable(@NotNull MobEffect effect) {
        return !effect.getCurativeItems().isEmpty();
    }

    /// Dispel a living. iterator see: {@link LivingEntity#tickEffects()}
    /// @return dispelled effects number
    public static int dispel(@NotNull LivingEntity target, LivingEntity from, DispelType type) {
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
