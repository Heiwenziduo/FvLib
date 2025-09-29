package com.github.heiwenziduo.fvlib.client;

import com.github.heiwenziduo.fvlib.FvLib;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FvLib.ModId, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FvClientSetUp {
    //gecko
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

    }

    //vanilla
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
}