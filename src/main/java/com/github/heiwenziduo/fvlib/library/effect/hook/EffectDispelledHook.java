package com.github.heiwenziduo.fvlib.library.effect.hook;

import net.minecraftforge.event.entity.living.MobEffectEvent;

public interface EffectDispelledHook {
    /// 当状态被主动解除时, 不可驱散的效果应取消此处的事件
    void onEffectDispelled(MobEffectEvent.Remove event);
}
