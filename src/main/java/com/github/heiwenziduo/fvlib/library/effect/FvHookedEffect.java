package com.github.heiwenziduo.fvlib.library.effect;

import com.github.heiwenziduo.fvlib.library.effect.hook.EffectDispelledHook;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.MobEffectEvent;

import java.util.ArrayList;
import java.util.List;

/// {@link net.minecraft.world.effect.MobEffect}
public class FvHookedEffect extends MobEffect implements EffectDispelledHook {
    private final boolean Dispellable;

    protected FvHookedEffect(MobEffectCategory pCategory, int pColor, boolean dispellable) {
        super(pCategory, pColor);
        Dispellable = dispellable;
    }

    /// effect do this tick
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

    }

    /// @return whether effect will do #applyEffectTick this tick
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        ArrayList<ItemStack> cure = new ArrayList<>();
        // Dispellable effects can be cured by milk. For stun, let it override this method
        if (Dispellable) cure.add(new ItemStack(Items.MILK_BUCKET));
        return cure;
    }

    public boolean isDispellable() {
        return Dispellable;
    }

    @Override
    public void onEffectDispelled(MobEffectEvent.Remove event) {
        System.out.println("onEffectDispelled" + event.getEffect());
        if (!Dispellable) event.setCanceled(true);
    }
}
