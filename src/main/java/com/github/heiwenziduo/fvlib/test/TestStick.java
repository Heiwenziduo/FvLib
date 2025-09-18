package com.github.heiwenziduo.fvlib.test;

import com.github.heiwenziduo.fvlib.FvUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TestStick extends Item {
    public TestStick(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        FvUtil.setTimeLock(pTarget, 100);
        System.out.println("set someone in time-lock");
        return false;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
    }
}
