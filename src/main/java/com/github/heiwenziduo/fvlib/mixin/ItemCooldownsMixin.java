package com.github.heiwenziduo.fvlib.mixin;

import com.github.heiwenziduo.fvlib.api.mixin.ItemCooldownsMixinAPI;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.github.heiwenziduo.fvlib.library.registry.FvAttribute.ITEM_COOLDOWN_REDUCTION;

@Mixin(value = ItemCooldowns.class)
public abstract class ItemCooldownsMixin implements ItemCooldownsMixinAPI {
    @Unique
    Player fvLib$playerReference;

    @Unique
    public ItemCooldowns setFvLib$playerReference(Player player) {
        fvLib$playerReference = player;
        // 下面这行会导致游戏崩溃: 载入世界时 "玩家实体初始化时gameProfile为null, 导致在调用getName()方法时出现空指针异常"
        // System.out.println("setFvLib$playerReference:    " + player);
        return (ItemCooldowns) (Object) this;
    }

    @ModifyVariable(method = "addCooldown", at = @At(value = "HEAD"), argsOnly = true)
    // on ordinal specified, make it auto-detect arg pos, nor crash
    public int whenAddCooldown(int pTicks) {
        if (fvLib$playerReference != null) {
            var cooldownReduction = fvLib$playerReference.getAttribute(ITEM_COOLDOWN_REDUCTION);
            if (cooldownReduction != null)
                return (int) (pTicks * (1 - cooldownReduction.getValue()));
        }
        return pTicks;
    }

}
