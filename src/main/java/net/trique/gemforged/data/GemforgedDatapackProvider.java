package net.trique.gemforged.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.worldgen.GemforgedBiomeModifiers;
import net.trique.gemforged.worldgen.GemforgedConfiguredFeatures;
import net.trique.gemforged.worldgen.GemforgedPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GemforgedDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, GemforgedConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, GemforgedPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, GemforgedBiomeModifiers::bootstrap);

    public GemforgedDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Gemforged.MODID));
    }
}
