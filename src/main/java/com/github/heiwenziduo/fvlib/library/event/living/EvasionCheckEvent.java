package com.github.heiwenziduo.fvlib.library.event.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/// 有生物触发闪避时抛出的事件, 可以取消, 取消后此次伤害不会被闪避<br>
/// <br>
/// This event does not have a result. {@link HasResult}
@Cancelable
public class EvasionCheckEvent extends LivingEvent {
    private final DamageSource source;

    public EvasionCheckEvent(LivingEntity entity, DamageSource pSource) {
        super(entity);
        source = pSource;
    }

    public DamageSource getSource() { return source; }
}
