package com.github.heiwenziduo.fvlib.api.mixin;

import com.github.heiwenziduo.fvlib.api.manager.TimeLockManager;

public interface LivingEntityMixinAPI {
    TimeLockManager FvLib$getTimeLockManager();
    void FvLib$doHurtTick();
}
