package com.github.heiwenziduo.fvlib.api.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;

public interface ItemCooldownsMixinAPI {
    ItemCooldowns setFvLib$playerReference(Player player);
}
