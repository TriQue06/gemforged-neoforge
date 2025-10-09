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
    public static final ResourceKey<ConfiguredFeature<?, ?>> NYXITE_ORE_KEY = registerKey("nyxite_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLOODSTONE_ORE_KEY = registerKey("bloodstone_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SOLARIUM_ORE_KEY = registerKey("solarium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VENOMYTE_ORE_KEY = registerKey("venomyte_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PHOENIXTONE_ORE_KEY = registerKey("phoenixtone_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PRISMYTE_ORE_KEY = registerKey("prismyte_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRAVITIUM_ORE_KEY = registerKey("gravitium_ore");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        // Nyxite
        List<OreConfiguration.TargetBlockState> nyxiteTargets = List.of(
                OreConfiguration.target(stoneReplaceables, GemforgedBlocks.NYXITE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_NYXITE_ORE.get().defaultBlockState()));
        register(context, NYXITE_ORE_KEY, Feature.ORE, new OreConfiguration(nyxiteTargets, 5));

        // Bloodstone
        List<OreConfiguration.TargetBlockState> bloodstoneTargets = List.of(
                OreConfiguration.target(stoneReplaceables, GemforgedBlocks.BLOODSTONE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_BLOODSTONE_ORE.get().defaultBlockState()));
        register(context, BLOODSTONE_ORE_KEY, Feature.ORE, new OreConfiguration(bloodstoneTargets, 5));

        // Solarium
        List<OreConfiguration.TargetBlockState> solariumTargets = List.of(
                OreConfiguration.target(stoneReplaceables, GemforgedBlocks.SOLARIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_SOLARIUM_ORE.get().defaultBlockState()));
        register(context, SOLARIUM_ORE_KEY, Feature.ORE, new OreConfiguration(solariumTargets, 5));

        // Venomyte
        List<OreConfiguration.TargetBlockState> venomyteTargets = List.of(
                OreConfiguration.target(stoneReplaceables, GemforgedBlocks.VENOMYTE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_VENOMYTE_ORE.get().defaultBlockState()));
        register(context, VENOMYTE_ORE_KEY, Feature.ORE, new OreConfiguration(venomyteTargets, 5));

        // Phoenixtone
        List<OreConfiguration.TargetBlockState> phoenixtoneTargets = List.of(
                OreConfiguration.target(stoneReplaceables, GemforgedBlocks.PHOENIXTONE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_PHOENIXTONE_ORE.get().defaultBlockState()));
        register(context, PHOENIXTONE_ORE_KEY, Feature.ORE, new OreConfiguration(phoenixtoneTargets, 5));

        // Prismyte
        List<OreConfiguration.TargetBlockState> prismyteTargets = List.of(
                OreConfiguration.target(stoneReplaceables, GemforgedBlocks.PRISMYTE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_PRISMYTE_ORE.get().defaultBlockState()));
        register(context, PRISMYTE_ORE_KEY, Feature.ORE, new OreConfiguration(prismyteTargets, 5));

        // Gravitium
        List<OreConfiguration.TargetBlockState> gravitiumTargets = List.of(
                OreConfiguration.target(stoneReplaceables, GemforgedBlocks.GRAVITIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, GemforgedBlocks.DEEPSLATE_GRAVITIUM_ORE.get().defaultBlockState()));
        register(context, GRAVITIUM_ORE_KEY, Feature.ORE, new OreConfiguration(gravitiumTargets, 5));
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
