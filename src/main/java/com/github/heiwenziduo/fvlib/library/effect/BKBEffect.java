package com.github.heiwenziduo.fvlib.library.effect;

import com.github.heiwenziduo.fvlib.library.api.DispelType;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectAddedHook;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;

import static com.github.heiwenziduo.fvlib.library.FvUtil.dispel;

/**
 * godlike appearance, knockback resistance, magic resistance, dispel once enact, and spell immunity
 */
public class BKBEffect extends FvHookedEffect implements EffectAddedHook {
    private final float knockbackResistance = 1f;
    private final float magicResistance = 0.5f;

    public BKBEffect(int pColor) {
        super(MobEffectCategory.BENEFICIAL, pColor, DispelType.IMMUNE);
    }

    @Override
    public void onEffectAdded(MobEffectEvent.Added event) {
        // 激活时施加一次弱驱散
        LivingEntity living = event.getEntity();
        dispel(living, DispelType.BASIC);
    }
}
