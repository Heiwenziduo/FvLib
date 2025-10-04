package com.github.heiwenziduo.fvlib.test;

import com.github.heiwenziduo.fvlib.library.FvUtil;
import com.github.heiwenziduo.fvlib.library.effect.StunEffect;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import static com.github.heiwenziduo.fvlib.api.capability.FvCapabilitiesProvider.FV_CAPA;
import static com.github.heiwenziduo.fvlib.initializer.FvEffects.CLASSIC_STUN;
import static com.github.heiwenziduo.fvlib.test.TestCapacityProvider.TEST_CAPA;

public class TestStick extends Item {
    public TestStick(Properties pProperties) {
        super(pProperties);
    }

    //only in server
    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        //FvUtil.setTimeLock(pTarget, 100);
//        System.out.println("set someone in time-lock");

        //pTarget.addEffect(new MobEffectInstance(new StunEffect())); // can not use effect does not register
        //pTarget.addEffect(new MobEffectInstance(CLASSIC_STUN.get(), 20, 0));

        System.out.println("testCapa:=============================");
        pTarget.getCapability(FV_CAPA).ifPresent(capa -> {
            capa.setTimelock((short) 100);
            System.out.println("bkb  " + capa.getBkbNumber());
            System.out.println("stun " + capa.getStunNumber());
        });
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
