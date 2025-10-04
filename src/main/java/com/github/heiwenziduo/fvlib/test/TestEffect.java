package com.github.heiwenziduo.fvlib.test;

import com.github.heiwenziduo.fvlib.library.api.DispelType;
import com.github.heiwenziduo.fvlib.library.effect.BKBEffect;
import com.github.heiwenziduo.fvlib.library.effect.FvHookedEffect;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectAddedHook;
import com.github.heiwenziduo.fvlib.library.event.living.EvasionCheckEvent;
import com.github.heiwenziduo.fvlib.library.event.living.PureDamageTakenEvent;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.heiwenziduo.fvlib.api.capability.FvCapabilitiesProvider.FV_CAPA;

/// 测试用
@Mod.EventBusSubscriber
public class TestEffect extends FvHookedEffect implements EffectAddedHook {
    public static final float MagicResistance2 = 0.5f;

    public TestEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFF0000, DispelType.IMMUNE);
    }

    //=============================================test

    /// static listener to registry a "DamageTakenHook"
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void testEvent(LivingAttackEvent event) {
        // always server
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity){
            LivingEntity living = event.getEntity();
            living.level();

        }
        //System.out.println("TestEffect: onDamageTaken " + event.getAmount());
        //System.out.println("source: " + event.getSource());
    }

    @SubscribeEvent
    public static void testEvent(EvasionCheckEvent event) {
        //client: false
//        System.out.println("onEvasion: " + event);
//        System.out.println("client?: " + event.getEntity().level().isClientSide);
//        if (random() < .5) {
//            event.setCanceled(true);
//            System.out.println("============================= no evasion");
//        }
    }

    @SubscribeEvent
    public static void testEvent(PureDamageTakenEvent event) {
        //System.out.println("PureDamageTakenEvent: " + event);
    }

    @Override
    public void onEffectAdded(MobEffectEvent.Added event) {
        // client: false
        System.out.println("test onEffectAdded client:    " + event.getEntity().level().isClientSide);
        event.getEntity().getCapability(FV_CAPA).ifPresent(capa -> {
            capa.setTimelock((short) 100);
        });
    }
}
