package net.trique.gemforged.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.block.GemforgedBlocks;

import java.util.List;

public class GemforgedConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> RANDOM_GEM_VEIN_KEY = registerKey("random_gem_vein");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> randomGemTargets = List.of(
                OreConfiguration.target(stoneReplaceables, GemforgedBlocks.RANDOM_GEM_VEIN.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_RANDOM_GEM_VEIN.get().defaultBlockState()));
        register(context, RANDOM_GEM_VEIN_KEY, Feature.ORE, new OreConfiguration(randomGemTargets, 5));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(Gemforged.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}