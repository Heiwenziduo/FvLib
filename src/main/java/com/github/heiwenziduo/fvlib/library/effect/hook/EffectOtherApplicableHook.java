package com.github.heiwenziduo.fvlib.library.effect.hook;

import net.minecraftforge.event.entity.living.MobEffectEvent;

public interface EffectOtherApplicableHook {
    /// 判断新效果是否可以被施加<br/>
    /// 先不启用
    void onEffectOtherApplyCheck(MobEffectEvent.Applicable event);
}