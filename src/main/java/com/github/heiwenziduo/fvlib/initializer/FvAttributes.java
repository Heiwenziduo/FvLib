package com.github.heiwenziduo.fvlib.initializer;

import com.github.heiwenziduo.fvlib.FvLib;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/// for outside usage, pls see {@link com.github.heiwenziduo.fvlib.library.api.FvAttribute}
public class FvAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, FvLib.ModId);
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final RegistryObject<Attribute> PASSIVE_REGEN = ATTRIBUTES.register("passive_regen",
            () -> new RangedAttribute("attribute.fvlib.passive_regen", 0, 0, 299792458));

    public static final RegistryObject<Attribute> MAGIC_RESISTANCE = ATTRIBUTES.register("magic_resistance",
            () -> new RangedAttribute("attribute.fvlib.magic_resistance", 0, -299792458, 1));

    public static final RegistryObject<Attribute> STATUS_RESISTANCE = ATTRIBUTES.register("status_resistance",
            () -> new RangedAttribute("attribute.fvlib.status_resistance", 0, -299792458, 1));
}
