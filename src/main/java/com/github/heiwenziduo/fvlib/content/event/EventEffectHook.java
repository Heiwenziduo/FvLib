package com.github.heiwenziduo.fvlib.content.event;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.api.effect.DamageTakenHook;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = FvLib.ModId, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventEffectHook {

//    @SubscribeEvent
//    public static void onMeleeHit() {
//
//    }

    @SubscribeEvent(priority = EventPriority.LOW)
    static void livingAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();

        // client side always returns false, so this should be fine?
        if (entity.level().isClientSide() || entity.isDeadOrDying()) {
            return;
        }
        // I cannot think of a reason to run when invulnerable
        DamageSource source = event.getSource();
        if (entity.isInvulnerableTo(source)) {
            return;
        }

        for (MobEffectInstance instance : entity.getActiveEffects()){
            MobEffect effect = instance.getEffect();
            if (effect instanceof DamageTakenHook) {
                ((DamageTakenHook) effect).onDamageTaken(event);
            }
        }

    }
}
