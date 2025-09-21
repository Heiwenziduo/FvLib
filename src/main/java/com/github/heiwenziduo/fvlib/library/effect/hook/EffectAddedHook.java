package com.github.heiwenziduo.fvlib.library.effect.hook;

import net.minecraftforge.event.entity.living.MobEffectEvent;

public interface EffectAddedHook {
    /// 状态效果被添加时
    void onEffectAdded(MobEffectEvent.Added event);
}
