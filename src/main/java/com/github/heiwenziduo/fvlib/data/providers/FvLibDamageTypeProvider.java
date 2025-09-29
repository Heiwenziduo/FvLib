package com.github.heiwenziduo.fvlib.data.providers;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

import static com.github.heiwenziduo.fvlib.FvLib.ModId;
import static com.github.heiwenziduo.fvlib.library.registry.FvDamageType.PURE;

public class FvLibDamageTypeProvider implements RegistrySetBuilder.RegistryBootstrap<DamageType> {

    /** Registers this provider with the registry set builder */
    public static void register(RegistrySetBuilder builder) {
        builder.add(Registries.DAMAGE_TYPE, new FvLibDamageTypeProvider());
    }

    @Override
    public void run(BootstapContext<DamageType> context) {
        String id = ModId + ".";
        context.register(PURE, new DamageType(id + PURE.location().getPath(), DamageScaling.NEVER, 0.1f));

    }
}
