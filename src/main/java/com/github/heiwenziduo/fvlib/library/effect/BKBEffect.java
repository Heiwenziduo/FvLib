package com.github.heiwenziduo.fvlib.library.effect;

import com.github.heiwenziduo.fvlib.library.api.DispelType;
import com.github.heiwenziduo.fvlib.library.effect.hook.*;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.MobEffectEvent;

import java.util.UUID;

import static com.github.heiwenziduo.fvlib.library.FvUtil.dispel;
import static com.github.heiwenziduo.fvlib.library.FvUtil.setBKB;
import static com.github.heiwenziduo.fvlib.library.api.FvAttribute.MAGIC_RESISTANCE;

/**
 * *godlike appearance, knockback resistance, magic resistance, dispel once enact, and spell immunity<br/><br/>
 * 生效期间阻止负面效果见{@link com.github.heiwenziduo.fvlib.mixin.LivingEntityMixin#affectEffectWhenBKB}
 */
public class BKBEffect extends FvHookedEffect implements EffectAddedHook {
    private final float knockbackResistance = 1f;
    private final float magicResistance = 0.5f;

    public BKBEffect(int pColor, double magicResistance) {
        super(MobEffectCategory.BENEFICIAL, pColor, DispelType.IMMUNE);
        addMagicResistance(magicResistance);
    }
    public BKBEffect(int pColor) {
        this(pColor, 0.5);
    }

    @Override
    public void onEffectAdded(MobEffectEvent.Added event) {
        // 激活时施加一次弱驱散
        LivingEntity living = event.getEntity();
        dispel(living, DispelType.BASIC);
        setBKB(living, true);
    }

    /// effect do this tick <br/> 其他带有魔免的效果应继承这一方法
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        setBKB(pLivingEntity, true);
    }

    /// @return whether effect will do #applyEffectTick this tick
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    public void addMagicResistance(double value) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString("6a74ae0b-ebe4-49a4-9d4b-95bd43350fcd"), this::getDescriptionId, value, AttributeModifier.Operation.ADDITION);
        getAttributeModifiers().put(MAGIC_RESISTANCE, attributemodifier);
    }
}
