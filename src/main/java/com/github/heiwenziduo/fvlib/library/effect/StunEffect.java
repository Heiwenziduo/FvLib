package com.github.heiwenziduo.fvlib.library.effect;

import com.github.heiwenziduo.fvlib.api.capability.FvCapabilities;
import com.github.heiwenziduo.fvlib.library.api.DispelType;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectAddedHook;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectDispelledHook;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectExpiredHook;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;

import static com.github.heiwenziduo.fvlib.api.capability.FvCapabilitiesProvider.FV_CAPA;

/** "Stun is a status effect that completely locks down affected units, disabling almost all of its capabilities." */
public class StunEffect extends FvHookedEffect implements EffectAddedHook, EffectDispelledHook, EffectExpiredHook {
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

    @Override
    public void onEffectAdded(MobEffectEvent.Added event) {
        event.getEntity().getCapability(FV_CAPA).ifPresent(FvCapabilities::getStunEffect);
    }

    @Override
    public void onEffectDispelled(MobEffectEvent.Remove event) {
        super.onEffectDispelled(event);
        if (!event.isCanceled())
            event.getEntity().getCapability(FV_CAPA).ifPresent(FvCapabilities::loseStunEffect);
    }

    @Override
    public void onEffectExpired(MobEffectEvent.Expired event) {
        event.getEntity().getCapability(FV_CAPA).ifPresent(FvCapabilities::loseStunEffect);
    }
}
