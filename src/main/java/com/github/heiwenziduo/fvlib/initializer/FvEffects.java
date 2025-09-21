package com.github.heiwenziduo.fvlib.initializer;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.library.effect.ClassicBKBEffect;
import com.github.heiwenziduo.fvlib.library.effect.StunEffect;
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

    public static final RegistryObject<StunEffect> CLASSIC_STUN = EFFECTS.register("stun_classic", StunEffect::new);
    public static final RegistryObject<ClassicBKBEffect> CLASSIC_BKB = EFFECTS.register("bkb_classic", () -> new ClassicBKBEffect(0xf4c22c));

    // example of reuse Stun effect
    // public static final RegistryObject<StunEffect> BASHER_STUN = EFFECTS.register("stun_basher", () -> new StunEffect(true));
}
