package com.github.heiwenziduo.fvlib.network.packet;

import com.github.heiwenziduo.fvlib.client.manager.TimelockEffectManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.github.heiwenziduo.fvlib.api.capability.FvCapabilitiesProvider.FV_CAPA;

public class ClientboundCapabilityPacket implements BoundedNetworkPacket {
    private final int id;
    private final short timelock;
    private final byte bkbNumber;
    private final byte stunNumber;

    public ClientboundCapabilityPacket(int entityId, short timelock, byte bkbNumber, byte stunNumber) {
        id = entityId;
        this.timelock = timelock;
        this.bkbNumber = bkbNumber;
        this.stunNumber = stunNumber;
    }

    public ClientboundCapabilityPacket(FriendlyByteBuf buf) {
        id = buf.readInt();
        timelock = buf.readShort();
        bkbNumber = buf.readByte();
        stunNumber = buf.readByte();
    }


    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeShort(timelock);
        buf.writeByte(bkbNumber);
        buf.writeByte(stunNumber);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            var level = Minecraft.getInstance().level;
            if (level != null) {
                var entity = level.getEntity(id);
                if (entity != null) {
                    entity.getCapability(FV_CAPA).ifPresent(capa -> {
                        capa.onReceivePacket();
                        capa.setEverything(timelock, bkbNumber, stunNumber);
                    });
                }
            }
        });
        return true;
    }
}
