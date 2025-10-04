package com.github.heiwenziduo.fvlib.api.event;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.api.capability.FvCapabilitiesProvider;
import com.github.heiwenziduo.fvlib.test.TestCapacityProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.heiwenziduo.fvlib.FvLib.fvResource;
import static com.github.heiwenziduo.fvlib.api.capability.FvCapabilitiesProvider.FV_CAPA;
import static com.github.heiwenziduo.fvlib.test.TestCapacityProvider.TEST_CAPA;

@Mod.EventBusSubscriber(modid = FvLib.ModId, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapacityAttach {

    public static final ResourceLocation TEST_CAPA_RESOURCE = fvResource("test_capa_resource");
    public static final ResourceLocation FV_CAPA_RESOURCE = fvResource("fv_capa_resource");

    @SubscribeEvent
    static void onAttachCapacity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity living) {
            // 在两端触发
            System.out.println("onAttachCapacity/////////////////////////////////" + living.getClass());
            System.out.println("client?  " + living.level().isClientSide);
            System.out.println("isRegistered?    " + FV_CAPA.isRegistered());

            if (!living.getCapability(FV_CAPA).isPresent()) {
                event.addCapability(FV_CAPA_RESOURCE, new FvCapabilitiesProvider(living));
            }
        }
    }
}
