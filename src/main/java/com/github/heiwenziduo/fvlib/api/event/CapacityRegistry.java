package com.github.heiwenziduo.fvlib.api.event;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.test.TestCapacity;
import com.github.heiwenziduo.fvlib.test.TestCapacityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FvLib.ModId, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapacityRegistry {

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(TestCapacity.class);
    }
}
