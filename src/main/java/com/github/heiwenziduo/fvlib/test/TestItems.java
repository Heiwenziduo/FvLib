package com.github.heiwenziduo.fvlib.test;

import com.github.heiwenziduo.fvlib.FvLib;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TestItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FvLib.ModId);
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> TEST_STICK = ITEMS.register("test_stick",
            () -> new TestStick(new Item.Properties()));
}
