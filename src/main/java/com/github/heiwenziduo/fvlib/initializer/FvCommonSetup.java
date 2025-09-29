package com.github.heiwenziduo.fvlib.initializer;

import com.github.heiwenziduo.fvlib.network.FvPacketHandler;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class FvCommonSetup {
    public static void onFMLSetup(FMLCommonSetupEvent event) {
        FvPacketHandler.register();
    }
}
