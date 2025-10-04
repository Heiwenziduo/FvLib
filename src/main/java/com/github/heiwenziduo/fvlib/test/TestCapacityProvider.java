package com.github.heiwenziduo.fvlib.test;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 创建Capacity步骤<br><br>
 * {@link TestCapacity}定义具体属性, 并用@AutoRegisterCapability类注解注册<br><br>
 * {@link TestCapacityProvider#TEST_CAPA}Capability注册类, {@link TestCapacityProvider}负责创建和提供Capability的实例, 处理数据的序列化和反序列化,
 * 之后在{@link com.github.heiwenziduo.fvlib.api.event.CapacityAttach}登录capa到实体上
 */
public class TestCapacityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<TestCapacity> TEST_CAPA = CapabilityManager.get(new CapabilityToken<>() {});

    private final LivingEntity living;
    private TestCapacity testCapacityData = null;
    private final LazyOptional<TestCapacity> lazy = LazyOptional.of(this::createTestCapacity);

    public TestCapacityProvider(LivingEntity entity) {
        living = entity;
    }

    private TestCapacity createTestCapacity() {
        if (testCapacityData == null) {
            testCapacityData = new TestCapacity(living);
        }
        return testCapacityData;
    }

    /// "If only one capability is exposed on a given object, you can use Capability#orEmpty as an alternative to the if/else statement."
    /// ~~~return TEST_CAPA.orEmpty(cap, lazy);~~~
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == TEST_CAPA) {
            return lazy.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createTestCapacity().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createTestCapacity().loadNBTData(nbt);
    }
}
