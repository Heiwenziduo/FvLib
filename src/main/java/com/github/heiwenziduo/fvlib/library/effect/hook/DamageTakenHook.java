package com.github.heiwenziduo.fvlib.library.effect.hook;

import net.minecraftforge.event.entity.living.LivingAttackEvent;

/// 受到伤害时
public interface DamageTakenHook {
    void onDamageTaken(LivingAttackEvent event);
}
