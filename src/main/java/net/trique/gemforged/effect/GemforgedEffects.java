package net.trique.gemforged.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trique.gemforged.Gemforged;

public class GemforgedEffects {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, Gemforged.MODID);

    public static final ResourceKey<MobEffect> RAGE_KEY =
            ResourceKey.create(Registries.MOB_EFFECT,
                    ResourceLocation.fromNamespaceAndPath(Gemforged.MODID, "rage"));

    public static final DeferredHolder<MobEffect, MobEffect> RAGE =
            EFFECTS.register(RAGE_KEY.location().getPath(), RageEffect::new);

    public static final ResourceKey<MobEffect> PHOENIX_KEY =
            ResourceKey.create(Registries.MOB_EFFECT,
                    ResourceLocation.fromNamespaceAndPath(Gemforged.MODID, "phoenix"));
    public static final DeferredHolder<MobEffect, MobEffect> PHOENIX =
            EFFECTS.register(PHOENIX_KEY.location().getPath(), PhoenixEffect::new);

    public static final ResourceKey<MobEffect> GLACIAL_FIST_KEY =
            ResourceKey.create(Registries.MOB_EFFECT,
                    ResourceLocation.fromNamespaceAndPath(Gemforged.MODID, "glacial_fist"));
    public static final DeferredHolder<MobEffect, MobEffect> GLACIAL_FIST =
            EFFECTS.register(GLACIAL_FIST_KEY.location().getPath(), GlacialFistEffect::new);
}