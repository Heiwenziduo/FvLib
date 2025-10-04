package com.github.heiwenziduo.fvlib.api.capability;

import com.github.heiwenziduo.fvlib.FvLib;
import com.github.heiwenziduo.fvlib.api.mixin.LivingEntityMixinAPI;
import com.github.heiwenziduo.fvlib.network.FvPacketHandler;
import com.github.heiwenziduo.fvlib.network.packet.ClientboundCapabilityPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.heiwenziduo.fvlib.api.capability.FvCapabilitiesProvider.FV_CAPA;

@AutoRegisterCapability
@Mod.EventBusSubscriber(modid = FvLib.ModId, bus = Mod.EventBusSubscriber.Bus.FORGE)
/// both in client & server
public class FvCapabilities {
    final LivingEntity living;
    final boolean clientSide;
    short timelock = 0; // at most 32767t, 1638sec
    byte bkbNumber = 0;
    byte stunNumber = 0;

    public FvCapabilities(LivingEntity living) {
        this.living = living;
        clientSide = living.level().isClientSide;
    }

    public boolean isTimelocked() {
        return timelock > 0;
    }

    public short getTimelock() {
        return timelock;
    }

    public void setTimelock(short timelock) {
        this.timelock = (short) Math.max(this.timelock, timelock);
        sendPacket();
    }

    // bkb=======================================================================================
    public boolean haveBKB() {
        return bkbNumber > 0;
    }
    public byte getBKBEffect() {
        return ++bkbNumber;
    }
    public byte loseBKBEffect() {
        return bkbNumber > 0 ? --bkbNumber : 0;
    }
    public byte getBkbNumber() {
        return bkbNumber;
    }
    public void setBkbNumber(byte bkbNumber) {
        this.bkbNumber = (byte) Math.max(bkbNumber, 0);
    }

    // stun======================================================================================
    public boolean isStunned() {
        return stunNumber > 0;
    }
    public byte getStunEffect() {
        //fixme: 多个同种stun加数累计
        return ++stunNumber;
    }
    public byte loseStunEffect() {
        return stunNumber > 0 ? --stunNumber : 0;
    }
    public byte getStunNumber() {
        return stunNumber;
    }
    public void setStunNumber(byte stunNumber) {
        this.stunNumber = (byte) Math.max(stunNumber, 0);
    }

    // ======================================================================================
    public void tick() {
        timelock = timelock > 0 ? --timelock : 0;
    }

    public void saveNBTData(CompoundTag nbt) {
        // 因为载入存档时不触发Effect Add钩子, 这里存一下效果计数是必要的
        nbt.putShort("timelock", timelock);
        nbt.putByte("bkb_num", bkbNumber);
        nbt.putByte("stun_num", stunNumber);
    }

    public void loadNBTData(CompoundTag nbt) {
        timelock = nbt.getShort("timelock");
        bkbNumber = nbt.getByte("bkb_num");
        stunNumber = nbt.getByte("stun_num");
    }

    /// {@link LivingEntity#tick}
    @SubscribeEvent
    static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity living = event.getEntity();
        living.getCapability(FV_CAPA).ifPresent(capa -> {
            capa.tick();

            // Living有两大子类: Mob 和 Player
            if (living instanceof Mob mob) {
                if (capa.isTimelocked()) {
                    ((LivingEntityMixinAPI) mob).FvLib$doHurtTick();

                    mob.tickCount--;

                    event.setCanceled(true);
                }

                if (capa.isStunned()) {
                    //mob.setNoAi(true); // noAi also canceled physic effects...
                    if (mob.tickCount % 20 == 0)
                        System.out.println(mob.tickCount + "   client: " + mob.level().isClientSide);
                } else {
                    //mob.setNoAi(false);
                }
            }

            // player: local & server
            if (living instanceof Player player) {
                if (capa.isTimelocked()) {

                }
            }
        });
    }

    // ========================================================================================== SYNC
    public void sendPacket() {
        if (clientSide) return;
        FvPacketHandler.sendToPlayersTrackingEntity(new ClientboundCapabilityPacket(living.getId(), this.timelock, bkbNumber, stunNumber), living, true);
    }

    public void onReceivePacket() {
        System.out.println("synced.   client: " + clientSide);
    }

    public void setEverything(short timelock, byte bkbNumber, byte stunNumber) {
        this.timelock = timelock;
        this.bkbNumber = bkbNumber;
        this.stunNumber = stunNumber;
    }
}
