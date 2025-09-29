package com.github.heiwenziduo.fvlib.initializer;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.api.attribute.ApproachLimitAttribute;
import com.github.heiwenziduo.fvlib.library.registry.FvAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/// for outside usage, pls see {@link FvAttribute}
public class FvAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, FvLib.ModId);
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final RegistryObject<Attribute> PASSIVE_REGEN = ATTRIBUTES.register("passive_regen",
            () -> new RangedAttribute("attribute.fvlib.passive_regen", 0, 0, 299792458).setSyncable(true));

    public static final RegistryObject<Attribute> MAGIC_RESISTANCE = ATTRIBUTES.register("magic_resistance",
            () -> new ApproachLimitAttribute("attribute.fvlib.magic_resistance", 0, -100).setSyncable(true));

    public static final RegistryObject<Attribute> STATUS_RESISTANCE = ATTRIBUTES.register("status_resistance",
            () -> new ApproachLimitAttribute("attribute.fvlib.status_resistance", 0, -10).setSyncable(true));

    public static final RegistryObject<Attribute> EVASION = ATTRIBUTES.register("evasion",
            () -> new ApproachLimitAttribute("attribute.fvlib.evasion", 0, 0).setSyncable(true));

    public static final RegistryObject<Attribute> ITEM_COOLDOWN_REDUCTION = ATTRIBUTES.register("item_cooldown_reduction",
            () -> new ApproachLimitAttribute("attribute.fvlib.item_cooldown_reduction", 0, -10).setSyncable(true));

    public static final RegistryObject<Attribute> LIFESTEAL = ATTRIBUTES.register("lifesteal",
            () -> new RangedAttribute("attribute.fvlib.lifesteal", 0, 0, 299792458).setSyncable(true));
}
