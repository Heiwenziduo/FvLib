package com.github.heiwenziduo.fvlib.test;

import com.github.heiwenziduo.fvlib.library.effect.BKBEffect;
import com.github.heiwenziduo.fvlib.library.event.living.EvasionCheckEvent;
import com.github.heiwenziduo.fvlib.library.event.living.PureDamageTakenEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/// 测试用
@Mod.EventBusSubscriber
public class TestEffect extends BKBEffect {
    public static final float MagicResistance2 = 0.5f;

    public TestEffect() {
        super(0xFF0000, MagicResistance2);
    }

    //=============================================test

    /// static listener to registry a "DamageTakenHook"
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void testEvent(LivingAttackEvent event) {
        // always server
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity){
            LivingEntity living = event.getEntity();
            living.level();

        }
        //System.out.println("TestEffect: onDamageTaken " + event.getAmount());
        //System.out.println("source: " + event.getSource());
    }

    @SubscribeEvent
    public static void testEvent(EvasionCheckEvent event) {
        //client: false
//        System.out.println("onEvasion: " + event);
//        System.out.println("client?: " + event.getEntity().level().isClientSide);
//        if (random() < .5) {
//            event.setCanceled(true);
//            System.out.println("============================= no evasion");
//        }
    }

    @SubscribeEvent
    public static void testEvent(PureDamageTakenEvent event) {
        //System.out.println("PureDamageTakenEvent: " + event);
    }

}
