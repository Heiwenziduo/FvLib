package com.github.heiwenziduo.fvlib.network.packet;

import com.github.heiwenziduo.fvlib.network.BoundedNetworkPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundEvasionEffect implements BoundedNetworkPacket {
    private final String msg;

    public ClientboundEvasionEffect(String str) {
        msg = str;
    }

    public ClientboundEvasionEffect(FriendlyByteBuf buf) {
        msg = buf.readUtf();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(msg);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            System.out.println("handle");
        });
        return true;
    }
}
