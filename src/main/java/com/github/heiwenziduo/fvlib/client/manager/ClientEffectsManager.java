package com.github.heiwenziduo.fvlib.client.manager;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.event.TickEvent;

/// OnlyIn: <span color="f44">CLIENT</span><br>
/// 客户端单例管理动画状态
public interface ClientEffectsManager {
    /// this should be called during [TickEvent.ClientTickEvent]
    void tick(ClientLevel level);
}
