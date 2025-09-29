package com.github.heiwenziduo.fvlib.mixin;

import com.github.heiwenziduo.fvlib.api.mixin.ItemCooldownsMixinAPI;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/// 也得混入双端, 不然不生效 (?)
@Mixin(value = ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Inject(method = "createItemCooldowns", at = @At("RETURN"), cancellable = true)
    public void createItemCooldowns(CallbackInfoReturnable<ItemCooldowns> cir) {
        cir.setReturnValue(((ItemCooldownsMixinAPI) cir.getReturnValue()).setFvLib$playerReference((ServerPlayer) (Object) this));
    }
}
