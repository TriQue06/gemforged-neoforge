package net.trique.gemforged.potion;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.effect.GemforgedEffects;

public final class GemforgedPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, Gemforged.MODID);

    public static final Holder<Potion> SHIMMER = POTIONS.register("shimmer",
            () -> new Potion(new MobEffectInstance(GemforgedEffects.SHIMMER_RAGE, 1200, 0)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}