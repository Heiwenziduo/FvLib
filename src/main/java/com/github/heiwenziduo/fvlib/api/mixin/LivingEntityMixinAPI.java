package com.github.heiwenziduo.fvlib.api.mixin;

import com.github.heiwenziduo.fvlib.api.manager.BKBEffectManager;
import com.github.heiwenziduo.fvlib.api.manager.TimeLockManager;

public interface LivingEntityMixinAPI {
    TimeLockManager FvLib$getTimeLockManager();
    BKBEffectManager FvLib$getBKBEffectManager();
    void FvLib$doHurtTick();
}
