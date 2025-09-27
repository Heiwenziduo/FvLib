package com.github.heiwenziduo.fvlib.mixin;

import com.github.heiwenziduo.fvlib.api.attribute.ApproachLimitAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

import static com.github.heiwenziduo.fvlib.api.attribute.ApproachLimitAttribute.MaxValue;

@Mixin(value = AttributeInstance.class)
public abstract class AttributeInstanceMixin {
    @Shadow @Final private Attribute attribute;

    @Shadow public abstract double getBaseValue();

    @Shadow protected abstract Collection<AttributeModifier> getModifiersOrEmpty(AttributeModifier.Operation pOperation);

    @Inject(method = "calculateValue", at = @At("HEAD"), cancellable = true)
    public void calculateApproachAttr(CallbackInfoReturnable<Double> cir) {
        if (attribute instanceof ApproachLimitAttribute appro) {
            double d0 = getBaseValue();

            for(AttributeModifier modifier : getModifiersOrEmpty(AttributeModifier.Operation.ADDITION)) {
                if (d0 >= MaxValue || d0 <= appro.getMinValue()) break;
                // 应确保以不同顺序计算得到相同结果
                d0 += (MaxValue - d0) * modifier.getAmount();
            }

            cir.setReturnValue(appro.sanitizeValue(d0));
        }
    }
}
