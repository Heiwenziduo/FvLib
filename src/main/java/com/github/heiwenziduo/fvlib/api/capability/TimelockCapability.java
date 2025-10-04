//package com.github.heiwenziduo.fvlib.api.capability;
//
//import com.github.heiwenziduo.fvlib.FvLib;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraftforge.event.entity.living.LivingEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = FvLib.ModId, bus = Mod.EventBusSubscriber.Bus.FORGE)
//public class TimelockCapability {
//    private boolean frozen = false;
//
//    // --- 动画快照字段 ---
//    private boolean hasSnapshot = false;
//    private float limbSwing, limbSwingAmount, oLimbSwingAmount; // o...代表上一tick的值
//    private float yBodyRot, oYBodyRot;
//    private float yHeadRot, oYHeadRot;
//    private float xRot, oXRot;
//    private int attackTime;
//    private int hurtTime;
//    private int tickCount; // 很多动画都依赖于tickCount
//
//    public boolean hasSnapshot() {
//        return hasSnapshot;
//    }
//
//    // 在客户端调用，当收到时停包后，下一帧渲染前执行
//    public void captureSnapshot(LivingEntity entity) {
//        limbSwing = entity.limbSwing;
//        limbSwingAmount = entity.limbSwingAmount;
//        oLimbSwingAmount = entity.oBob; // 注意：limbSwingAmount的上一tick值是oBob
//
//        yBodyRot = entity.yBodyRot;
//        oYBodyRot = entity.yBodyRotO;
//
//        yHeadRot = entity.yHeadRot;
//        oYHeadRot = entity.yHeadRotO;
//
//        xRot = entity.getXRot();
//        oXRot = entity.xRotO;
//
//        attackTime = entity.attackTime;
//        hurtTime = entity.hurtTime;
//
//        tickCount = entity.tickCount;
//
//        hasSnapshot = true;
//    }
//
//    // 在客户端的RenderLivingEvent.Pre中，每一帧都调用
//    public void applySnapshot(LivingEntity entity) {
//        if (!hasSnapshot) return;
//
//        // --- 强制恢复所有变量 ---
//        entity.limbSwing = limbSwing;
//        entity.limbSwingAmount = limbSwingAmount;
//        entity.oBob = oLimbSwingAmount; // 恢复上一tick的值
//
//        entity.yBodyRot = yBodyRot;
//        entity.yBodyRotO = oYBodyRot; // 恢复上一tick的值
//
//        entity.yHeadRot = yHeadRot;
//        entity.yHeadRotO = oYHeadRot; // 恢复上一tick的值
//
//        entity.setXRot(xRot);
//        entity.xRotO = oXRot; // 恢复上一tick的值
//
//        entity.attackTime = attackTime;
//        entity.hurtTime = hurtTime;
//
//        // 覆盖tickCount可以让依赖时间的粒子、颜色变化等动画也停止
//        entity.tickCount = tickCount;
//
//        // 冻结位置插值
//        entity.xo = entity.getX();
//        entity.yo = entity.getY();
//        entity.zo = entity.getZ();
//    }
//
//    // 当解冻时，需要重置hasSnapshot
//    public void releaseSnapshot() {
//        hasSnapshot = false;
//    }
//
//    ///
//    @SubscribeEvent
//    static void onLivingTick(LivingEvent.LivingTickEvent event) {
//        LivingEntity living = event.getEntity();
//        // todo: if capacity_isLocked, cancel event (but do hurt tick
//    }
//}
