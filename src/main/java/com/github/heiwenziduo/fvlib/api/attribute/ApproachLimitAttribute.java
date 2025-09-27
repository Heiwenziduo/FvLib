package com.github.heiwenziduo.fvlib.api.attribute;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

/// 用于在DeferredRegister<Attribute>中注册"逼近式"的属性, 此属性只支持ADDITION运算符<br/>
/// 例: 0 + 70% -> 70%; ~ += 70% -> 91%; ~ += 70% -> 97.3% 以此类推<br/>
/// 默认的区间是 \[-100, 1] <br/>
/// 参考: MAGIC_RESISTANCE, STATUS_RESISTANCE, EVASION
public class ApproachLimitAttribute extends RangedAttribute {
    public static final double MaxValue = 1;

    public ApproachLimitAttribute(String pDescriptionId, double defaultValue, double min) {
        super(pDescriptionId, defaultValue, min, MaxValue);
    }

    @Override
    public double sanitizeValue(double pValue) {
        return Double.isNaN(pValue) ? getDefaultValue() : Mth.clamp(pValue, getMinValue(), MaxValue);
    }
}
