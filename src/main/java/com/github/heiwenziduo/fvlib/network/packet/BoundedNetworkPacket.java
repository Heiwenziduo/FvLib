package com.github.heiwenziduo.fvlib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface BoundedNetworkPacket {
    /// encoder
    void toBytes(FriendlyByteBuf buf);

    /// consumerMainThread
    boolean handle(Supplier<NetworkEvent.Context> supplier);
}
