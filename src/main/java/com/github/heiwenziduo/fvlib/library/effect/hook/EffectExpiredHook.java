package com.github.heiwenziduo.fvlib.library.effect.hook;

import net.minecraftforge.event.entity.living.MobEffectEvent;

public interface EffectExpiredHook {
    /// 状态效果自然到期时
    void onEffectExpired(MobEffectEvent.Expired event);
}
