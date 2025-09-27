package com.github.heiwenziduo.fvlib.test;

import com.github.heiwenziduo.fvlib.library.effect.BKBEffect;
import com.github.heiwenziduo.fvlib.library.effect.hook.DamageTakenHook;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

/// 测试用
public class TestEffect extends BKBEffect implements DamageTakenHook {
    public static final float MagicResistance2 = 0.5f;

    public TestEffect() {
        super(0xFF0000, MagicResistance2);
    }

    //=============================================test
    @Override
    public void onDamageTaken(LivingAttackEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity){


            LivingEntity living = event.getEntity();
            living.level();

            System.out.println("TestEffect: onDamageTaken " + event.getAmount());
            System.out.println("source: " + event.getSource());
            System.out.println("Client: " + living.level().isClientSide);   // always server
        }
    }

}
