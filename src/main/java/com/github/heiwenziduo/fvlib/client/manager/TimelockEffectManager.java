package com.github.heiwenziduo.fvlib.client.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Optional;

/// OnlyIn: <span color="f44">CLIENT</span><br>
/// 客户端单例管理动画状态
@OnlyIn(Dist.CLIENT)
public class TimelockEffectManager implements ClientEffectsManager {
    public static final TimelockEffectManager INSTANCE = new TimelockEffectManager();
    public static TimelockEffectManager getInstance() {
        return INSTANCE;
    }

    public TimelockEffectManager() {}

    private final HashMap<Integer, TimelockContext> timelockedList = new HashMap<>();

    public void startEffect(int entityId, short duration) {
        TimelockContext context = timelockedList.get(entityId);
        long current = Minecraft.getInstance().level.getGameTime();
        if (context == null) {
            timelockedList.put(entityId, new TimelockContext(current, duration));
        } else {
            long end0 = context.startTime + context.duration;
            end0 -= current;
            // 保留两次时停中较长者
            timelockedList.put(entityId, new TimelockContext(current, (short) Math.max(end0, duration)));
        }
    }

    public void tick(ClientLevel level) {
        // 每tick维护列表
        if (level == null) return;
        long currentTime = level.getGameTime();
        timelockedList.entrySet().removeIf(entry -> {
            Entity entity = level.getEntity(entry.getKey());
            return entity == null || !entity.isAlive() || currentTime > entry.getValue().startTime + entry.getValue().duration;
        });
    }

    public Optional<TimelockContext> getContext(int entityId) {
        return Optional.ofNullable(timelockedList.get(entityId));
    }

    public record TimelockContext(long startTime, short duration) {

    }
}
