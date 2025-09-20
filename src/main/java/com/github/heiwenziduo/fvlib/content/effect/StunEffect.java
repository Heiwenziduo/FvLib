package com.github.heiwenziduo.fvlib.content.effect;

import com.github.heiwenziduo.fvlib.library.effect.FvHookedEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/** "Stun is a status effect that completely locks down affected units, disabling almost all of its capabilities." */
public class StunEffect extends FvHookedEffect {
    protected StunEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

    }
}
