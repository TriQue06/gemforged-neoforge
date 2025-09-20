package net.trique.gemforged.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GemforgedEffects {
    public static final String MODID = "gemforged";

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, MODID);

    public static final ResourceKey<MobEffect> GARNET_RAGE_KEY =
            ResourceKey.create(Registries.MOB_EFFECT,
                    ResourceLocation.fromNamespaceAndPath(MODID, "garnet_rage"));
    public static final DeferredHolder<MobEffect, MobEffect> GARNET_RAGE =
            EFFECTS.register(GARNET_RAGE_KEY.location().getPath(), GarnetRageEffect::new);

    public static final ResourceKey<MobEffect> SHIMMER_RAGE_KEY =
            ResourceKey.create(Registries.MOB_EFFECT,
                    ResourceLocation.fromNamespaceAndPath(MODID, "shimmer_rage"));
    public static final DeferredHolder<MobEffect, MobEffect> SHIMMER_RAGE =
            EFFECTS.register(SHIMMER_RAGE_KEY.location().getPath(), ShimmerRageEffect::new);

    public static final ResourceKey<MobEffect> PHOENIX_KEY =
            ResourceKey.create(Registries.MOB_EFFECT,
                    ResourceLocation.fromNamespaceAndPath(MODID, "phoenix"));
    public static final DeferredHolder<MobEffect, MobEffect> PHOENIX =
            EFFECTS.register(PHOENIX_KEY.location().getPath(), PhoenixEffect::new);
}