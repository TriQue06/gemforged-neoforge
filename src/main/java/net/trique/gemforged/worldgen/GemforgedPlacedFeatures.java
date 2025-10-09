package net.trique.gemforged.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.trique.gemforged.Gemforged;

import java.util.List;

public class GemforgedPlacedFeatures {
    public static final ResourceKey<PlacedFeature> NYXITE_ORE_PLACED_KEY = registerKey("nyxite_ore_placed");
    public static final ResourceKey<PlacedFeature> BLOODSTONE_ORE_PLACED_KEY = registerKey("bloodstone_ore_placed");
    public static final ResourceKey<PlacedFeature> SOLARIUM_ORE_PLACED_KEY = registerKey("solarium_ore_placed");
    public static final ResourceKey<PlacedFeature> VENOMYTE_ORE_PLACED_KEY = registerKey("venomyte_ore_placed");
    public static final ResourceKey<PlacedFeature> PHOENIXTONE_ORE_PLACED_KEY = registerKey("phoenixtone_ore_placed");
    public static final ResourceKey<PlacedFeature> PRISMYTE_ORE_PLACED_KEY = registerKey("prismyte_ore_placed");
    public static final ResourceKey<PlacedFeature> GRAVITIUM_ORE_PLACED_KEY = registerKey("gravitium_ore_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, NYXITE_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(GemforgedConfiguredFeatures.NYXITE_ORE_KEY),
                GemforgedOrePlacement.commonOrePlacement(3,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(16))));

        register(context, BLOODSTONE_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(GemforgedConfiguredFeatures.BLOODSTONE_ORE_KEY),
                GemforgedOrePlacement.commonOrePlacement(3,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(16))));

        register(context, SOLARIUM_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(GemforgedConfiguredFeatures.SOLARIUM_ORE_KEY),
                GemforgedOrePlacement.commonOrePlacement(3,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(16))));

        register(context, VENOMYTE_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(GemforgedConfiguredFeatures.VENOMYTE_ORE_KEY),
                GemforgedOrePlacement.commonOrePlacement(3,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(16))));

        register(context, PHOENIXTONE_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(GemforgedConfiguredFeatures.PHOENIXTONE_ORE_KEY),
                GemforgedOrePlacement.commonOrePlacement(3,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(16))));

        register(context, PRISMYTE_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(GemforgedConfiguredFeatures.PRISMYTE_ORE_KEY),
                GemforgedOrePlacement.commonOrePlacement(3,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(16))));

        register(context, GRAVITIUM_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(GemforgedConfiguredFeatures.GRAVITIUM_ORE_KEY),
                GemforgedOrePlacement.commonOrePlacement(3,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(16))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(Gemforged.MODID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context,
                                 ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
