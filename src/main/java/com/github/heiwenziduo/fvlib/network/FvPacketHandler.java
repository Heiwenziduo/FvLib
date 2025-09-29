package com.github.heiwenziduo.fvlib.network;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.network.packet.ClientboundEvasionEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;

public class FvPacketHandler {

    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(ResourceLocation.fromNamespaceAndPath(FvLib.ModId, "network"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();

    private static int PacketId = 0;

    private static int id() {
        return PacketId++;
    }

    public static void register() {
        CHANNEL.messageBuilder(ClientboundEvasionEffect.class, id(), PLAY_TO_CLIENT)
                .decoder(ClientboundEvasionEffect::new)
                .encoder(ClientboundEvasionEffect::toBytes)
                .consumerMainThread(ClientboundEvasionEffect::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);

    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity) {
        sendToPlayersTrackingEntity(message, entity, false);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity, boolean sendToSource) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
        if (sendToSource && entity instanceof ServerPlayer serverPlayer)
            sendToPlayer(message, serverPlayer);
    }
}