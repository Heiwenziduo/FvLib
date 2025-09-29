package com.github.heiwenziduo.fvlib.library.event.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/// 触发了生命吸取后抛出, 可以取消, 取消后这次伤害不会吸血
@Cancelable
public class LifestealEvent extends LivingEvent {
    private final DamageSource source;
    private float amount;

    public LifestealEvent(LivingEntity entity, DamageSource pSource, float hp) {
        super(entity);
        source = pSource;
        amount = hp;
    }

    public DamageSource getSource() { return source; }

    public float getAmount() { return amount; }

    public void setAmount(float amount) { this.amount = amount; }
}
