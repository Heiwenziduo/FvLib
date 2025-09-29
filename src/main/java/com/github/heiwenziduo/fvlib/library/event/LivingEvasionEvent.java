package com.github.heiwenziduo.fvlib.library.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

/// 在生物闪避了一次攻击后抛出, 主要用于激活渲染器<br>
/// 不可取消, 没有结果
public class LivingEvasionEvent extends LivingEvent {

    public LivingEvasionEvent(LivingEntity entity) {
        super(entity);
    }
}
