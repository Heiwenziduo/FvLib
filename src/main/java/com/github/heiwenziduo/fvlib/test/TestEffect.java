package com.github.heiwenziduo.fvlib.test;

import com.github.heiwenziduo.fvlib.library.effect.BKBEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
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
    public static void onDamageTaken(LivingAttackEvent event) {
        // always server
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity){
            LivingEntity living = event.getEntity();
            living.level();

        }
        //System.out.println("TestEffect: onDamageTaken " + event.getAmount());
        //System.out.println("source: " + event.getSource());
    }

}
