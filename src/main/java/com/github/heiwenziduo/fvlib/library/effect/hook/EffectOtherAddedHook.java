package com.github.heiwenziduo.fvlib.library.effect.hook;

import net.minecraftforge.event.entity.living.MobEffectEvent;

public interface EffectOtherAddedHook {
    /// 当此效果存在, 又被施加了新效果时. 事件中是新施加的效果<br/>
    /// 先不启用
    void onEffectOtherAdded(MobEffectEvent.Added event);
}
