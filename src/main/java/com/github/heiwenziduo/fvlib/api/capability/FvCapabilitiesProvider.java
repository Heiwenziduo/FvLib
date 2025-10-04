package com.github.heiwenziduo.fvlib.api.capability;

import com.github.heiwenziduo.fvlib.test.TestCapacity;
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

public class FvCapabilitiesProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<FvCapabilities> FV_CAPA = CapabilityManager.get(new CapabilityToken<>() {});

    private final LivingEntity living;
    private FvCapabilities fvCapa = null;
    private final LazyOptional<FvCapabilities> lazy = LazyOptional.of(this::createFvCapabilities);

    public FvCapabilitiesProvider(LivingEntity living) {
        this.living = living;
    }

    private FvCapabilities createFvCapabilities() {
        if (fvCapa == null) {
            fvCapa = new FvCapabilities(living);
        }
        return fvCapa;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == FV_CAPA) {
            return lazy.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createFvCapabilities().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createFvCapabilities().loadNBTData(nbt);
    }
}
