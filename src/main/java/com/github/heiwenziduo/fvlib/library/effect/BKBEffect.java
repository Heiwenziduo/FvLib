package com.github.heiwenziduo.fvlib.library.effect;

import com.github.heiwenziduo.fvlib.library.api.DispelType;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectAddedHook;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;

import static com.github.heiwenziduo.fvlib.library.FvUtil.dispel;
import static com.github.heiwenziduo.fvlib.library.api.FvAttribute.MAGIC_RESISTANCE;
import static com.github.heiwenziduo.fvlib.util.FvUtilInternal.setBKB;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE;

/**
 * *godlike appearance, knockback resistance, magic resistance, dispel once enact, and spell immunity<br/><br/>
 * 生效期间阻止负面效果见{@link com.github.heiwenziduo.fvlib.mixin.LivingEntityMixin#affectEffectWhenBKB}
 */
public class BKBEffect extends FvHookedEffect implements EffectAddedHook {
    public static final float KnockbackResistance = 1f;
    public static final float MagicResistance = 0.5f;

    public BKBEffect(int pColor, double magicResistance) {
        super(MobEffectCategory.BENEFICIAL, pColor, DispelType.IMMUNE);
        addAttributeModifier(MAGIC_RESISTANCE, "6a74ae0b-ebe4-49a4-9d4b-95bd43350fcd", magicResistance, ADDITION);
        addAttributeModifier(KNOCKBACK_RESISTANCE, "5670362b-5d34-413f-989c-61bf64fd0ee6", KnockbackResistance, ADDITION);
    }
    public BKBEffect(int pColor) {
        this(pColor, MagicResistance);
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
}
