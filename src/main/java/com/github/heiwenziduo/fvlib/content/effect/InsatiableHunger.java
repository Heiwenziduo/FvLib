package com.github.heiwenziduo.fvlib.content.effect;

import com.github.heiwenziduo.fvlib.api.effect.DamageTakenHook;
import com.github.heiwenziduo.fvlib.library.effect.FvHookedEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

/// 极度饥渴, 后面迁移到spell中
public class InsatiableHunger extends FvHookedEffect implements DamageTakenHook {
    public InsatiableHunger() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000);
    }

    //=============================================test
    @Override
    public void onDamageTaken(LivingAttackEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity){


            LivingEntity living = event.getEntity();
            living.level();

            System.out.println("InsatiableHunger: onDamageTaken " + event.getAmount());
            System.out.println("source: " + event.getSource());
            System.out.println("Client: " + living.level().isClientSide);   // always server
        }
    }

}
