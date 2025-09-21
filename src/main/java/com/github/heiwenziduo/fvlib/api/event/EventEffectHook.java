package com.github.heiwenziduo.fvlib.api.event;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.library.effect.hook.DamageTakenHook;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectAddedHook;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectDispelledHook;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectExpiredHook;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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

    @SubscribeEvent
    static void effectsAdd(MobEffectEvent.Added event) {
        MobEffect effect = event.getEffectInstance().getEffect();
        if (effect instanceof EffectAddedHook){
            ((EffectAddedHook) effect).onEffectAdded(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    static void effectsDispel(MobEffectEvent.Remove event) {
        // 确保被触发驱散事件的效果在目标实体身上
        MobEffectInstance instance = event.getEffectInstance();
        if (instance == null) return;
        MobEffect effect = event.getEffect();
        if (effect instanceof EffectDispelledHook){
            ((EffectDispelledHook) effect).onEffectDispelled(event);
        }
    }

    @SubscribeEvent
    static void effectsExpire(MobEffectEvent.Expired event) {
        MobEffectInstance instance = event.getEffectInstance();
        if (instance == null) return;
        MobEffect effect = instance.getEffect();
        if (effect instanceof EffectExpiredHook){
            ((EffectExpiredHook) effect).onEffectExpired(event);
        }
    }
}
