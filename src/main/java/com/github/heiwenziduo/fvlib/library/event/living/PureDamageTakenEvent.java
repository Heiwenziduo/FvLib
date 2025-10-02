package com.github.heiwenziduo.fvlib.library.event.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

/// 受到纯粹伤害时抛出的事件, 不能取消, 没有结果
public class PureDamageTakenEvent extends LivingEvent {
    private final DamageSource source;
    private final float amount;

    public PureDamageTakenEvent(LivingEntity entity, DamageSource pSource, float damage) {
        super(entity);
        source = pSource;
        amount = damage;
    }

    public DamageSource getSource() { return source; }

    public float getAmount() { return amount; }
}
