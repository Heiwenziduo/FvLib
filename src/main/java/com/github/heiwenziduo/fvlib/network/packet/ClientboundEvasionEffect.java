package com.github.heiwenziduo.fvlib.network.packet;

import com.github.heiwenziduo.fvlib.client.manager.EvasionEffectManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static java.lang.Math.PI;
import static java.lang.Math.random;

public class ClientboundEvasionEffect implements BoundedNetworkPacket {
    private final int id;
    private final Vec3 direct;

    public ClientboundEvasionEffect(int entityId, Vec3 direction) {
        id = entityId;
        direct = direction;
    }

    public ClientboundEvasionEffect(FriendlyByteBuf buf) {
        id = buf.readInt();
        direct = readVec3(buf);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(id);
        writeVec3(direct, buf);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            EvasionEffectManager.getInstance().startEffect(id, direct);
        });
        return true;
    }

    // ===================================================================================
    public static Vec3 readVec3(FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new Vec3(x, y, z);
    }

    public static void writeVec3(Vec3 vec3, FriendlyByteBuf buf) {
        buf.writeDouble(vec3.x);
        buf.writeDouble(vec3.y);
        buf.writeDouble(vec3.z);
    }

    /// 返回单位化了的闪避方向, 闪避方向始终垂直于y轴
    public static Vec3 evasionDirect(@NotNull LivingEntity living, DamageSource source) {
        Entity cause = source.getDirectEntity();
        if (cause == null) return new Vec3(random(), 0, random()).normalize();
        int r = random() < .5 ? 1 : -1;
        float yRadius = (float) ((random() - 1) * PI / 3); // 随机偏移
        return Vec3.directionFromRotation(new Vec2(0, cause.getYRot()))
                .cross(new Vec3(0, 1, 0))
                .scale(r)
                .yRot(yRadius)
                .normalize();
    }
}
