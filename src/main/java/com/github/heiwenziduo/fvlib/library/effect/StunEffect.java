package com.github.heiwenziduo.fvlib.library.effect;

import com.github.heiwenziduo.fvlib.library.api.DispelType;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/** "Stun is a status effect that completely locks down affected units, disabling almost all of its capabilities." */
public class StunEffect extends FvHookedEffect {
    public StunEffect() {
        super(MobEffectCategory.HARMFUL, 0xf1ed81, DispelType.STRONG);
    }
    public StunEffect(boolean pierceImmunity) {
        super(MobEffectCategory.HARMFUL, 0xf1ed81, DispelType.STRONG, pierceImmunity);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        //todo: stop living's ai
    }
}
