package com.github.heiwenziduo.fvlib.api.event;

import com.github.heiwenziduo.fvlib.library.event.living.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;

/// contains all events used by fvlib, only for internal
public class FvEventHooks {
    /// false if not evasion
    public static boolean onLivingEvasionCheck(LivingEntity entity, DamageSource source) {
        return !MinecraftForge.EVENT_BUS.post(new EvasionCheckEvent(entity, source));
    }

    public static void onLivingEvasion(LivingEntity entity) {
        MinecraftForge.EVENT_BUS.post(new EvasionRenderEvent(entity));
    }

    public static float onLivingLifesteal(LivingEntity entity, DamageSource pSource, float amount) {
        LifestealEvent event = new LifestealEvent(entity, pSource, amount);
        return (MinecraftForge.EVENT_BUS.post(event) ? 0 : event.getAmount());
    }

    public static float onLivingDealPure(LivingEntity entity, DamageSource pSource, float amount) {
        PureDamageDealEvent event = new PureDamageDealEvent(entity, pSource, amount);
        return (MinecraftForge.EVENT_BUS.post(event) ? 0 : event.getAmount());
    }

    public static void onLivingTakePure(LivingEntity entity, DamageSource pSource, float amount) {
        MinecraftForge.EVENT_BUS.post(new PureDamageTakenEvent(entity, pSource, amount));
    }
}
