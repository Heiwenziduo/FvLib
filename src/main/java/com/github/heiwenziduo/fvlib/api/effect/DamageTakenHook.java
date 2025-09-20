package com.github.heiwenziduo.fvlib.api.effect;

import net.minecraftforge.event.entity.living.LivingAttackEvent;

/// 收到伤害时
public interface DamageTakenHook {
    void onDamageTaken(LivingAttackEvent event);
}
