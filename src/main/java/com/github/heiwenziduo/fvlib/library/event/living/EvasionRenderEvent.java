package com.github.heiwenziduo.fvlib.library.event.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

/// 在生物闪避了一次攻击后抛出, 主要用于激活渲染器<br>
/// 不可取消, 没有结果
public class EvasionRenderEvent extends LivingEvent {

    public EvasionRenderEvent(LivingEntity entity) {
        super(entity);
    }
}
