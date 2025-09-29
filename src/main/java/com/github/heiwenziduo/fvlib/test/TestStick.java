package com.github.heiwenziduo.fvlib.test;

import com.github.heiwenziduo.fvlib.library.FvUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
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

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level pLevel, LivingEntity living, int pTimeLeft) {
        if (living instanceof Player player) {
            System.out.println("releaseUsing ======= " + player.level().isClientSide);

            if (player.getCooldowns().isOnCooldown(stack.getItem())) return;
            System.out.println("client?: " + player.level().isClientSide);

            player.getCooldowns().addCooldown(stack.getItem(), 100);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);

        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 20;
    }

}
