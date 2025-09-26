package com.github.heiwenziduo.fvlib.library.effect.hook;

import net.minecraftforge.event.entity.living.MobEffectEvent;

public interface EffectApplicableHook {
    /// 判断效果[自身]是否可被施加
    void onEffectApplyCheck(MobEffectEvent.Applicable event);
}