package com.github.heiwenziduo.fvlib.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public class FvCapabilities {
    final LivingEntity living;
    boolean isTimelocked = false;
    byte bkbNumber = 0;
    byte stunNumber = 0;

    public FvCapabilities(LivingEntity living) {
        this.living = living;
    }

    public boolean isTimelocked() {
        return isTimelocked;
    }

    // bkb=======================================
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

    // stun======================================
    public boolean isStunned() {
        return stunNumber > 0;
    }
    public byte getStunEffect() {
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


    public void saveNBTData(CompoundTag nbt) {
        // 因为载入存档时不触发Effect Add钩子, 这里存一下效果计数是必要的
        nbt.putByte("bkb_num", bkbNumber);
        nbt.putByte("stun_num", stunNumber);
    }

    public void loadNBTData(CompoundTag nbt) {
        bkbNumber = nbt.getByte("bkb_num");
        stunNumber = nbt.getByte("stun_num");
    }
}
