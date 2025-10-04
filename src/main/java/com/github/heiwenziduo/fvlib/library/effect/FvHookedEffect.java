package com.github.heiwenziduo.fvlib.library.effect;

import com.github.heiwenziduo.fvlib.library.api.DispelType;
import com.github.heiwenziduo.fvlib.library.effect.hook.EffectDispelledHook;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.MobEffectEvent;

import java.util.ArrayList;
import java.util.List;

/// {@link net.minecraft.world.effect.MobEffect}
/// 用于给Effect效果添加触发钩子的抽象层, 需设定效果驱散类型{@link DispelType}和是否无视BKB
public class FvHookedEffect extends MobEffect implements EffectDispelledHook {
    private final boolean isPierceImmunity;
    private final DispelType canDispel;

    /// 默认增益效果无视技能免疫
    public FvHookedEffect(MobEffectCategory pCategory, int pColor, DispelType canDispel) {
        this(pCategory, pColor, canDispel, pCategory == MobEffectCategory.BENEFICIAL);
    }
    public FvHookedEffect(MobEffectCategory pCategory, int pColor, DispelType canDispel, boolean isPierceImmunity) {
        super(pCategory, pColor);
        this.canDispel = canDispel;
        this.isPierceImmunity = isPierceImmunity;
    }

    /// effect do this tick
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

    }

    /// @return whether effect will do #applyEffectTick this tick
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        ArrayList<ItemStack> cure = new ArrayList<>();
        // Dispellable effects can be cured by milk. For stun, let it override this method
        if (isDispellable()) cure.add(new ItemStack(Items.MILK_BUCKET));
        return cure;
    }

    public final boolean isDispellable() {
        return canDispel != DispelType.IMMUNE;
    }
    public final boolean isDispellable(DispelType type) {
        return isDispellable() && !(type == DispelType.BASIC && canDispel == DispelType.STRONG);
    }

    /// 将驱散类型应用到当前效果上, 返回驱散结果 <br/>
    /// 调用了 living#removeEffect, 会抛出 MobEffectEvent.Remove 事件 <br/>
    /// Do NOT call this during iterate, call {@link isDispellable(type)} instead
    public final boolean applyDispel(LivingEntity living, DispelType type) {
        if (isDispellable(type)) {
            living.removeEffect(this);
            return true;
        } else {
            return false;
        }
    }

    public final boolean isPierceImmunity() {
        return isPierceImmunity;
    }

    @Override
    public void onEffectDispelled(MobEffectEvent.Remove event) {
        //System.out.println("onEffectDispelled-----------" + event.getEffect());
        if (!isDispellable()) event.setCanceled(true);
    }
}
