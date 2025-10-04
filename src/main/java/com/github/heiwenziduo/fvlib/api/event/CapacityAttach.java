package com.github.heiwenziduo.fvlib.api.event;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.test.TestCapacityProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.heiwenziduo.fvlib.FvLib.fvResource;
import static com.github.heiwenziduo.fvlib.test.TestCapacityProvider.TEST_CAPA;

@Mod.EventBusSubscriber(modid = FvLib.ModId, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapacityAttach {

    public static final ResourceLocation TEST_CAPA_RESOURCE = fvResource("test_capa_resource");

    @SubscribeEvent
    static void onAttachCapacity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity living) {
            // 在两端触发 (?)
            System.out.println("onAttachCapacity/////////////////////////////////" + living.getClass());
            System.out.println(TEST_CAPA.isRegistered());
            if (!living.getCapability(TEST_CAPA).isPresent()) {
                System.out.println("attach capa");
                event.addCapability(TEST_CAPA_RESOURCE, new TestCapacityProvider(living));
            }
        }
    }
}
