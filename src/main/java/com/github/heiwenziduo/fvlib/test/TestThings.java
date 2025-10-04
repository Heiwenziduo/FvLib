package com.github.heiwenziduo.fvlib.test;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.library.effect.FvHookedEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TestThings {
    public static final DeferredRegister<Item> ITEMS_T = DeferredRegister.create(ForgeRegistries.ITEMS, FvLib.ModId);
    public static final DeferredRegister<MobEffect> EFFECTS_T = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FvLib.ModId);
    public static void register(IEventBus eventBus) {
        ITEMS_T.register(eventBus);
        EFFECTS_T.register(eventBus);
    }

    public static final RegistryObject<Item> TEST_STICK = ITEMS_T.register("test_stick",
            () -> new TestStick(new Item.Properties()));
    public static final RegistryObject<FvHookedEffect> TEST_EFFECT = EFFECTS_T.register("test_effect", TestEffect::new);
}
