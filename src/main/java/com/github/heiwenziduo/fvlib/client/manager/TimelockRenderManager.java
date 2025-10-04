package com.github.heiwenziduo.fvlib.client.manager;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Optional;

/// OnlyIn: <span color="f44">CLIENT</span><br>
/// 客户端单例管理动画状态
@OnlyIn(Dist.CLIENT)
public class TimelockRenderManager {
    public static final TimelockRenderManager INSTANCE = new TimelockRenderManager();
    public static TimelockRenderManager getInstance() {
        return INSTANCE;
    }

    public TimelockRenderManager() {}

    private final HashMap<Integer, TimelockSnapshot> timelockedList = new HashMap<>();

    ///
    public void makeSnapshot(int entityId, float partialTick) {
        TimelockSnapshot snapshot = timelockedList.get(entityId);
        long current = Minecraft.getInstance().level.getGameTime();
        if (snapshot == null) {
            timelockedList.put(entityId, new TimelockSnapshot(current, partialTick));
        }
    }

    public Optional<TimelockSnapshot> getSnapshot(int entityId) {
        return Optional.ofNullable(timelockedList.get(entityId));
    }

    public record TimelockSnapshot(long startTime, float partialTick) {

    }
}
