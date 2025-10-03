package com.github.heiwenziduo.fvlib.library.event.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

/// 有生物被时停时触发, living: 被时停的实体<br>
public class TimelockEvent extends LivingEvent {
    private final int time;

    public TimelockEvent(LivingEntity entity, int tick) {
        super(entity);
        time = tick;
    }

    public float getTime() { return time; }
}
