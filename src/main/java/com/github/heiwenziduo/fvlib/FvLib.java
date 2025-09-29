package com.github.heiwenziduo.fvlib;

import com.github.heiwenziduo.fvlib.initializer.FvAttributes;
import com.github.heiwenziduo.fvlib.initializer.FvEffects;
import com.github.heiwenziduo.fvlib.test.TestThings;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FvLib.ModId)
public class FvLib {
    public static final String ModId = "fvlib";

    public FvLib(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();

        FvAttributes.register(eventBus);
        FvEffects.register(eventBus);

        TestThings.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    // static

    /** {@link ResourceLocation#fromNamespaceAndPath} */
    public static ResourceLocation fvResource(String name) {
        return ResourceLocation.fromNamespaceAndPath(ModId, name);
    }
}
