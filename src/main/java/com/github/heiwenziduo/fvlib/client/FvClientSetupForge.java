package com.github.heiwenziduo.fvlib.client;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.client.manager.EvasionEffectManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.heiwenziduo.fvlib.client.manager.EvasionEffectManager.renderEvasionPhantom;
import static com.github.heiwenziduo.fvlib.client.manager.EvasionEffectManager.renderMissText;

@Mod.EventBusSubscriber(modid = FvLib.ModId, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FvClientSetupForge {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                EvasionEffectManager.getInstance().tick(level);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
        LivingEntity entity = event.getEntity();
        int entityId = entity.getId();
        // handle evasion logic
        EvasionEffectManager.getInstance().getAnimation(entityId).ifPresent(animCollection -> {
            animCollection.forEach(animation -> {
                float partialTicks = event.getPartialTick();
                PoseStack poseStack = event.getPoseStack();
                renderEvasionPhantom(event, animation, partialTicks);
                renderMissText(entity, poseStack, event.getMultiBufferSource(), event.getPackedLight(), animation, partialTicks);
            });
        });
    }
}
