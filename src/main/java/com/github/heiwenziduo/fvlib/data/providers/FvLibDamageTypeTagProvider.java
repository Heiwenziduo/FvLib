package com.github.heiwenziduo.fvlib.data.providers;

import com.github.heiwenziduo.fvlib.FvLib;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.github.heiwenziduo.fvlib.library.registry.FvDamageType.PURE;
import static net.minecraft.tags.DamageTypeTags.*;


/// {@link DamageTypeTagsProvider}
public class FvLibDamageTypeTagProvider extends DamageTypeTagsProvider {

    public FvLibDamageTypeTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookup, FvLib.ModId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BYPASSES_SHIELD).add(PURE);
        tag(BYPASSES_EFFECTS).add(PURE);
        tag(BYPASSES_ARMOR).add(PURE);
        tag(AVOIDS_GUARDIAN_THORNS).add(PURE);
    }
}
