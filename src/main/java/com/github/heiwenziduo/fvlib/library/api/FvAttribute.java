package com.github.heiwenziduo.fvlib.library.api;

import com.github.heiwenziduo.fvlib.initializer.FvAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;

/// fvlib注册的新属性
public class FvAttribute {
    /// 被动回复
    public static final Attribute PASSIVE_REGEN = FvAttributes.PASSIVE_REGEN.get();

    /// 通用魔法抗性, 只有[ADDITION]运算符会生效
    public static final Attribute MAGIC_RESISTANCE = FvAttributes.MAGIC_RESISTANCE.get();

    /// 通用(负面)状态抗性, 只有[ADDITION]运算符会生效
    public static final Attribute STATUS_RESISTANCE = FvAttributes.STATUS_RESISTANCE.get();

    /// 通用物理闪避, 只有[ADDITION]运算符会生效
    public static final Attribute EVASION = FvAttributes.EVASION.get();
}
