package com.github.heiwenziduo.fvlib.library.effect;

import net.minecraft.world.effect.MobEffectCategory;

public abstract class AbstractBKBEffect extends FvHookedEffect {
    protected AbstractBKBEffect(int pColor) {
        super(MobEffectCategory.BENEFICIAL, pColor, false);
    }
}
