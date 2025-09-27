package com.github.heiwenziduo.fvlib.api.event;

import com.github.heiwenziduo.fvlib.FvLib;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.heiwenziduo.fvlib.library.FvUtil.isGenericMagic;
import static com.github.heiwenziduo.fvlib.library.api.FvAttribute.MAGIC_RESISTANCE;

@Mod.EventBusSubscriber(modid = FvLib.ModId, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventAttribute {
    @SubscribeEvent(priority = EventPriority.HIGH)
    static void evasion(LivingAttackEvent event) {
        // 闪避物理伤害

    }

    @SubscribeEvent
    static void magicResistanceWholePass(LivingAttackEvent event) {
        // 如果魔抗到了100%则整个跳过伤害
        if (event.getEntity().getAttributeValue(MAGIC_RESISTANCE) == 1 && isGenericMagic(event.getSource())){
            event.setCanceled(true);

            // 或许可以加点指示性特效...
        }
    }

    @SubscribeEvent
    static void magicResistance(LivingHurtEvent event) {
        var mag = event.getEntity().getAttributeValue(MAGIC_RESISTANCE);
        if (isGenericMagic(event.getSource())){
            event.setAmount((float) (event.getAmount() * (1 - mag)));
        }
    }
}
