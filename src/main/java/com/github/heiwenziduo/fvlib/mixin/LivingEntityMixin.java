package com.github.heiwenziduo.fvlib.mixin;

import com.github.heiwenziduo.fvlib.api.event.FvEventHooks;
import com.github.heiwenziduo.fvlib.api.manager.BKBEffectManager;
import com.github.heiwenziduo.fvlib.api.manager.TimeLockManager;
import com.github.heiwenziduo.fvlib.api.mixin.LivingEntityMixinAPI;
import com.github.heiwenziduo.fvlib.library.effect.FvHookedEffect;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.heiwenziduo.fvlib.library.registry.FvAttribute.*;
import static com.github.heiwenziduo.fvlib.library.registry.FvDamageType.PURE;


@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityMixinAPI {
    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    public int hurtTime;
    @Shadow
    protected int lastHurtByPlayerTime;
    @Shadow
    protected Player lastHurtByPlayer;
    @Shadow
    private LivingEntity lastHurtMob;
    @Shadow
    private LivingEntity lastHurtByMob;
    @Shadow
    private int lastHurtByMobTimestamp;
    @Shadow @Final private AttributeMap attributes;

    @Shadow
    public abstract boolean isDeadOrDying();
    @Shadow
    protected abstract void tickDeath();
    @Shadow
    public abstract void setLastHurtByMob(@Nullable LivingEntity pLivingEntity);
    @Shadow
    public abstract float getAbsorptionAmount();
    @Shadow
    public abstract void setAbsorptionAmount(float pAbsorptionAmount);
    @Shadow
    public abstract CombatTracker getCombatTracker();
    @Shadow
    public abstract float getHealth();
    @Shadow
    public abstract void setHealth(float pAbsorptionAmount);
    @Shadow public abstract boolean hasEffect(MobEffect pEffect);
    @Shadow public abstract void heal(float pHealAmount);
    @Shadow @javax.annotation.Nullable public abstract MobEffectInstance getEffect(MobEffect pEffect);

    @Unique
    private static final EntityDataAccessor<Integer> DATA_TIME_LOCK = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> DATA_BKB_EFFECT = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    @Unique
    protected TimeLockManager FvLib$timeLockManager = new TimeLockManager();

    @Unique
    protected BKBEffectManager FvLib$BKBEffectManager = new BKBEffectManager();


    @Unique
    @Override
    public TimeLockManager FvLib$getTimeLockManager() {
        return FvLib$timeLockManager;
    }

    @Unique
    @Override
    public BKBEffectManager FvLib$getBKBEffectManager() {
        return FvLib$BKBEffectManager;
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"), cancellable = true)
    private static void moreLivingAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue().add(MAGIC_RESISTANCE).add(STATUS_RESISTANCE).add(PASSIVE_REGEN).add(EVASION).add(LIFESTEAL));
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    public void defineSynchedData(CallbackInfo ci) {
        entityData.define(DATA_TIME_LOCK, 0);
        entityData.define(DATA_BKB_EFFECT, false);
    }

    /// 被时间锁定的活物不能行动
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void livingTickMixin(CallbackInfo ci) {
        if (!level().isClientSide) {
            // 服务端发起同步
            entityData.set(DATA_TIME_LOCK, FvLib$timeLockManager.getTimeLock());
            entityData.set(DATA_BKB_EFFECT, FvLib$BKBEffectManager.hasBKB());
        } else {
            // 客户端接收同步的数据
            int timeLockSync = entityData.get(DATA_TIME_LOCK);
            boolean bkbSync = entityData.get(DATA_BKB_EFFECT);
            FvLib$timeLockManager.setTimeLock(timeLockSync);
            FvLib$BKBEffectManager.setBKB(bkbSync);
        }


        // 似乎也会插入所有子类的开头 ?
        FvLib$handleTimeLock(ci);
        FvLib$handleLifeRegen();
        FvLib$handleStun(ci);
        FvLib$handleHex();

        FvLib$BKBEffectManager.setBKB(false); /// 执行位置在{@link LivingEntity#tickEffects}之前
    }

    /// 纯粹伤害不会被减免
    @Inject(method = "actuallyHurt", at = @At("HEAD"), cancellable = true)
    public void onTakePureDamage(DamageSource pDamageSource, float pDamageAmount, CallbackInfo ci) {
        if (pDamageSource != null && pDamageSource.is(PURE) && !isInvulnerableTo(pDamageSource)) {
            Entity entity = pDamageSource.getEntity();
            if (entity instanceof LivingEntity living) {
                // 抛出造成纯粹伤害事件
                pDamageAmount = FvEventHooks.onLivingDealPure(living, pDamageSource, pDamageAmount);
            }
            // 类型转换 [(LivingEntity) (Object)](https://www.reddit.com/r/fabricmc/comments/nw3rs8/how_can_i_access_the_this_in_a_mixin_for_a_class/?tl=zh-hans)
            pDamageAmount = Math.max(net.minecraftforge.common.ForgeHooks.onLivingHurt((LivingEntity) (Object) this, pDamageSource, pDamageAmount), pDamageAmount);

            float f1 = Math.max(pDamageAmount - getAbsorptionAmount(), 0.0F);
            setAbsorptionAmount(getAbsorptionAmount() - (pDamageAmount - f1));
            float f = pDamageAmount - f1;
            if (f > 0.0F && f < 3.4028235E37F) {
                if (entity instanceof ServerPlayer) {
                    ServerPlayer serverplayer = (ServerPlayer)entity;
                    serverplayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(f * 10.0F));
                }
            }

            f1 = Math.max(net.minecraftforge.common.ForgeHooks.onLivingDamage((LivingEntity) (Object) this, pDamageSource, f1), f1);
            // 实体受到纯粹伤害
            FvEventHooks.onLivingTakePure((LivingEntity) (Object) this, pDamageSource, f1);
            if (f1 != 0.0F) {
                getCombatTracker().recordDamage(pDamageSource, f1);
                setHealth(getHealth() - f1);
                setAbsorptionAmount(getAbsorptionAmount() - f1);
                gameEvent(GameEvent.ENTITY_DAMAGE);
            }

            ci.cancel();
        }
    }

    /// 技能免疫状态, 普通负面效果不会生效 (这样逻辑更简洁)
    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    public void affectEffectWhenBKB(MobEffectInstance pEffectInstance, CallbackInfoReturnable<Boolean> cir) {
        MobEffect toApply = pEffectInstance.getEffect();
        if (toApply.getCategory() != MobEffectCategory.HARMFUL) return;
        //System.out.println("affectEffectWhenBKBClient: " + level().isClientSide + "   bkb? " + entityData.get(DATA_BKB_EFFECT)); // 有BKB时都是服务端, 无BKB时两端都有 ?

        if (FvLib$BKBEffectManager.hasBKB()) {
            // 默认所有传统效果都是不无视魔免的
            if (toApply instanceof FvHookedEffect hooked) {
                if (hooked.isPierceImmunity()) return; // 无视魔免的效果仍可施加
                cir.setReturnValue(false);
            } else cir.setReturnValue(false);
        }
    }







    ///
    @Unique
    private void FvLib$doHurtTick() {
        if (hurtTime > 0) {
            --hurtTime;
        }
        // ServerPlayer 的 invulnerable-- 在 tick() 中, 暂时不管它
//                if (invulnerableTime > 0 && !(this instanceof ServerPlayer)) {
//                    --invulnerableTime;
//                }
        if (invulnerableTime > 0) {
            --invulnerableTime;
        }
        if (isDeadOrDying() && level().shouldTickDeath(this)) {
            tickDeath();
        }
        if (lastHurtByPlayerTime > 0) {
            --lastHurtByPlayerTime;
        } else {
            lastHurtByPlayer = null;
        }
        if (lastHurtMob != null && !lastHurtMob.isAlive()) {
            lastHurtMob = null;
        }
        if (lastHurtByMob != null) {
            if (!lastHurtByMob.isAlive()) {
                setLastHurtByMob((LivingEntity) null);
            } else if (tickCount - lastHurtByMobTimestamp > 100) {
                setLastHurtByMob((LivingEntity) null);
            }
        }
    }

    ///
    @Unique
    private void FvLib$handleTimeLock(CallbackInfo ci) {
        if (FvLib$getTimeLockManager().isTimeLocked()) {
            FvLib$timeLockManager.timeLockDecrement();
            // if is locked, apply some base tick logic, like reducing invulnerable time.
            // from LivingEntity#baseTick
            FvLib$doHurtTick();

            if(!level().isClientSide) {
                if (tickCount % 10 == 0) {
                    for (int i = 0; i < 5; i++) {
                        ((ServerLevel) level()).sendParticles(ParticleTypes.DRAGON_BREATH, getX() + random.nextDouble(), getY() + random.nextDouble(), getZ() + random.nextDouble(), 1, 0, .5, 0, .5);
                    }
                }

                ci.cancel();

            } else {
                // todo: 给时停实体一个紫色滤镜(render)
                // bug: 1. 客户端tick停止后实体肢体可能会抽搐, 2.渲染动画没有停下, 例如岩浆怪, 在空中不会保持"展开"的状态
            }
        }
    }

    /// {@link LivingEntity#heal(float)}
    @Unique
    private void FvLib$handleLifeRegen() {
        if (!level().isClientSide && tickCount % 5 == 0){
            double regen = attributes.getValue(PASSIVE_REGEN);
            // heal方法会调用回血事件, PASSIVE_REGEN受治疗增强影响
            heal((float) (regen / 4));
        }
    }

    ///
    @Unique
    private void FvLib$handleStun(CallbackInfo ci) {
        // 能运行到这里说明不在时停中

    }

    /// todo
    @Unique
    private void FvLib$handleHex() {

    }
    // ==================== test ======================
    /// 09/21 可以用这种方法把映射前的类名调出来, 位于打包后的 refmap
//    @Inject(method = "onEffectRemoved", at = @At("HEAD"))
//    protected abstract void onEffectRemoved(MobEffectInstance pEffectInstance, CallbackInfo ci);
    // ==================== test-end ==================
}
