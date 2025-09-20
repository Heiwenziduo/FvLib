package com.github.heiwenziduo.fvlib.content.initializer;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.content.effect.InsatiableHunger;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FvEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FvLib.ModId);
    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

    public static final RegistryObject<InsatiableHunger> INSATIABLE_HUNGER = EFFECTS.register("insatiable_hunger", InsatiableHunger::new);
}
