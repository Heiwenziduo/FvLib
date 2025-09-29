package com.github.heiwenziduo.fvlib.library.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;

public class FvEventHooks {
    /// false if not evasion
    public static boolean onLivingEvasionCheck(LivingEntity entity, DamageSource source) {
        return !MinecraftForge.EVENT_BUS.post(new LivingEvasionCheckEvent(entity, source));
    }

    public static void onLivingEvasion(LivingEntity entity) {
        MinecraftForge.EVENT_BUS.post(new LivingEvasionEvent(entity));
    }
}
