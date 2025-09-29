package com.github.heiwenziduo.fvlib.api.event;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.network.FvPacketHandler;
import com.github.heiwenziduo.fvlib.network.packet.ClientboundEvasionEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.heiwenziduo.fvlib.library.FvUtil.isGenericMagic;
import static com.github.heiwenziduo.fvlib.library.FvUtil.isGenericPhysic;
import static com.github.heiwenziduo.fvlib.library.registry.FvAttribute.*;
import static com.github.heiwenziduo.fvlib.util.FvUtilInternal.setEffectDuration;
import static java.lang.Math.random;

@Mod.EventBusSubscriber(modid = FvLib.ModId, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventAttributeHook {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void lifesteal(LivingDamageEvent event) {
        double damage = event.getAmount();
        if (damage <= 0 || event.isCanceled()) return; // 只有实际造成了伤害才会触发吸血
        if (isGenericPhysic(event.getSource())){
            if (event.getSource().getEntity() instanceof LivingEntity living){
                float toHeal = (float) (living.getAttributeValue(LIFESTEAL) * damage);
                toHeal = FvEventHooks.onLivingLifesteal(living, event.getSource(), toHeal);
                living.heal(toHeal);
                if (toHeal > 0) {
                    // 加点特效...

                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    static void evasion(LivingAttackEvent event) {
        LivingEntity living = event.getEntity();
        // this event emits on both side
        if (living.level().isClientSide) return;

        // 闪避物理伤害
        if (isGenericPhysic(event.getSource())){
            boolean result1 = random() < living.getAttributeValue(EVASION);
            // 抛出一个躲避事件, 判断必中效果
            boolean result2 = result1 && FvEventHooks.onLivingEvasionCheck(living, event.getSource());
            if (result2) {
                event.setCanceled(true);
                FvEventHooks.onLivingEvasion(living);
                FvPacketHandler.sendToPlayersTrackingEntity(new ClientboundEvasionEffect("evasion: " + event.getAmount()), living, true);
            }
        }
    }

    @SubscribeEvent
    static void magicResistancePass(LivingAttackEvent event) {
        // 如果魔抗到了100%则整个跳过伤害
        if (event.getEntity().getAttributeValue(MAGIC_RESISTANCE) == 1 && isGenericMagic(event.getSource())){
            event.setCanceled(true);

            // 或许可以加点指示性特效...
        }
    }

    @SubscribeEvent
    static void magicResistance(LivingHurtEvent event) {
        var mag = event.getEntity().getAttributeValue(MAGIC_RESISTANCE);
        if (isGenericMagic(event.getSource())){
            event.setAmount((float) (event.getAmount() * (1 - mag)));
        }
    }

    @SubscribeEvent
    static void statusResistance(MobEffectEvent.Added event) {
        MobEffect effect = event.getEffectInstance().getEffect();
        if (effect.getCategory() != MobEffectCategory.HARMFUL) return; // 只缩短负面效果持续时间

        double resistance = event.getEntity().getAttributeValue(STATUS_RESISTANCE);
        MobEffectInstance instance = event.getEffectInstance();
        setEffectDuration(instance, (int) (instance.getDuration() * (1 - resistance)));
    }
}
