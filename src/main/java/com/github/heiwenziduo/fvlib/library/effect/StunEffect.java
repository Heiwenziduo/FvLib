package com.github.heiwenziduo.fvlib.library.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/** "Stun is a status effect that completely locks down affected units, disabling almost all of its capabilities." */
public class StunEffect extends FvHookedEffect {
    public StunEffect() {
        super(MobEffectCategory.HARMFUL, 0xf1ed81, false);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        //todo: stop living's ai
    }
}
