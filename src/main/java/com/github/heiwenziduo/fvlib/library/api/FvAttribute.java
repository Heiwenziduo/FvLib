package com.github.heiwenziduo.fvlib.library.api;

import com.github.heiwenziduo.fvlib.initializer.FvAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;

/// fvlib注册的新属性
public class FvAttribute {
    public static final Attribute PASSIVE_REGEN = FvAttributes.PASSIVE_REGEN.get();

    public static final Attribute MAGIC_RESISTANCE = FvAttributes.MAGIC_RESISTANCE.get();

    public static final Attribute STATUS_RESISTANCE = FvAttributes.STATUS_RESISTANCE.get();
}
