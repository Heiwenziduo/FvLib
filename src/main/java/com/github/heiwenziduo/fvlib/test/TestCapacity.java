package com.github.heiwenziduo.fvlib.test;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

//@AutoRegisterCapability
public class TestCapacity {
    public TestCapacity(LivingEntity entity){}

    int testValue = 0;

    public int getTestValue() {
        return testValue;
    }

    public void setTestValue(int testValue) {
        this.testValue = testValue;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("test_value", testValue);
    }

    public void loadNBTData(CompoundTag nbt) {
        testValue = nbt.getInt("test_value");
    }
}
