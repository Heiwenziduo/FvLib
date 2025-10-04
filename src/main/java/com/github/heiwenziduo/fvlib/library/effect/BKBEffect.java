package com.github.heiwenziduo.fvlib.library.effect;

import com.github.heiwenziduo.fvlib.api.capability.FvCapabilities;
import com.github.heiwenziduo.fvlib.library.api.DispelType;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectAddedHook;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectDispelledHook;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectExpiredHook;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;

import static com.github.heiwenziduo.fvlib.api.capability.FvCapabilitiesProvider.FV_CAPA;
import static com.github.heiwenziduo.fvlib.library.FvUtil.dispel;
import static com.github.heiwenziduo.fvlib.library.registry.FvAttribute.MAGIC_RESISTANCE;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE;

/**
 * *godlike appearance, knockback resistance, magic resistance, dispel once enact, and spell immunity<br/><br/>
 * 生效期间阻止负面效果见{@link com.github.heiwenziduo.fvlib.mixin.LivingEntityMixin#affectEffectWhenBKB}
 */
public class BKBEffect extends FvHookedEffect implements EffectAddedHook, EffectDispelledHook, EffectExpiredHook {
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
        event.getEntity().getCapability(FV_CAPA).ifPresent(FvCapabilities::getBKBEffect);
    }

    @Override
    public void onEffectDispelled(MobEffectEvent.Remove event) {
        super.onEffectDispelled(event);
        if (!event.isCanceled())
            event.getEntity().getCapability(FV_CAPA).ifPresent(FvCapabilities::loseBKBEffect);
    }

    @Override
    public void onEffectExpired(MobEffectEvent.Expired event) {
        event.getEntity().getCapability(FV_CAPA).ifPresent(FvCapabilities::loseBKBEffect);
    }
}
