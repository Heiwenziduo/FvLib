package com.github.heiwenziduo.fvlib.library.event.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

/// 造成纯粹伤害时抛出的事件, 可以修改数值, 不能取消, 没有结果
public class PureDamageDealEvent extends LivingEvent {
    private final DamageSource source;
    private float amount;

    public PureDamageDealEvent(LivingEntity entity, DamageSource pSource, float damage) {
        super(entity);
        source = pSource;
        amount = damage;
    }

    public DamageSource getSource() { return source; }

    public float getAmount() { return amount; }

    public void setAmount(float amount) { this.amount = amount; }
}
