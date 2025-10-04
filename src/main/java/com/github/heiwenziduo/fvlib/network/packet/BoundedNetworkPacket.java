package com.github.heiwenziduo.fvlib.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface BoundedNetworkPacket {
    /// encoder
    public void toBytes(FriendlyByteBuf buf);

    /// consumerMainThread
    public boolean handle(Supplier<NetworkEvent.Context> supplier);
}
