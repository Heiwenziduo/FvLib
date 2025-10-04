package com.github.heiwenziduo.fvlib.network.packet;

import com.github.heiwenziduo.fvlib.client.manager.TimelockEffectManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundTimelockEffect implements BoundedNetworkPacket {
    private final int id;
    private final short lockTick; // at most 32767t, 1638sec

    public ClientboundTimelockEffect(int entityId, short tick) {
        id = entityId;
        lockTick = tick;
    }

    public ClientboundTimelockEffect(FriendlyByteBuf buf) {
        id = buf.readInt();
        lockTick = buf.readShort();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeShort(lockTick);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            TimelockEffectManager.getInstance().startEffect(id, lockTick);
        });
        return true;
    }
}
