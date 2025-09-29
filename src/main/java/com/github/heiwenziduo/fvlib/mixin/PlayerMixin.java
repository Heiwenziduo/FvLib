package com.github.heiwenziduo.fvlib.mixin;

import com.github.heiwenziduo.fvlib.api.mixin.ItemCooldownsMixinAPI;
import com.github.heiwenziduo.fvlib.api.mixin.PlayerMixinAPI;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.heiwenziduo.fvlib.library.api.FvAttribute.ITEM_COOLDOWN_REDUCTION;

@Mixin(value = Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerMixinAPI {
    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void morePlayerAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue().add(ITEM_COOLDOWN_REDUCTION));
    }

    @Inject(method = "createItemCooldowns", at = @At("RETURN"), cancellable = true)
    public void createItemCooldowns(CallbackInfoReturnable<ItemCooldowns> cir) {
        cir.setReturnValue(((ItemCooldownsMixinAPI) cir.getReturnValue()).setFvLib$playerReference((Player) (Object) this));
    }
}
