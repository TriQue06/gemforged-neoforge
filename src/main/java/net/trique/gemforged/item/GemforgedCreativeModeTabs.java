package net.trique.gemforged.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.potion.GemforgedPotions;

import java.util.function.Supplier;

public class GemforgedCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Gemforged.MODID);

    public static final Supplier<CreativeModeTab> GEMFORGED_ITEMS_TAB = CREATIVE_MODE_TAB.register("gemforgeditemstab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(GemforgedItems.SHADOWSTEP_DAGGER.get()))
                    .title(Component.translatable("gemforged.tab1"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(GemforgedItems.SHADOWSTEP_DAGGER);
                        output.accept(GemforgedItems.BATTLE_CHARM);
                        output.accept(GemforgedItems.SANDBURST_STAFF);
                        output.accept(GemforgedItems.VENOMFANG_BLADE);
                        output.accept(GemforgedItems.PHOENIX_RELIC);
                        output.accept(GemforgedItems.THUNDER_PRISM);
                        output.accept(GemforgedItems.SHIMMER_POWDER);
                        output.accept(PotionContents.createItemStack(Items.POTION, GemforgedPotions.SHIMMER));
                        output.accept(PotionContents.createItemStack(Items.SPLASH_POTION, GemforgedPotions.SHIMMER));
                        output.accept(PotionContents.createItemStack(Items.LINGERING_POTION, GemforgedPotions.SHIMMER));
                        output.accept(PotionContents.createItemStack(Items.TIPPED_ARROW, GemforgedPotions.SHIMMER));

                        output.accept(GemforgedItems.AMETHYST_CRYSTAL_POWDER);
                        output.accept(GemforgedItems.CITRINE_CRYSTAL_POWDER);
                        output.accept(GemforgedItems.CITRINE_SHARD);
                        output.accept(GemforgedItems.DIAMOND_CRYSTAL_POWDER);
                        output.accept(GemforgedItems.ELECTRUM_ALLOY_DUST);
                        output.accept(GemforgedItems.ELECTRUM_INGOT);
                        output.accept(GemforgedItems.EMERALD_CRYSTAL_POWDER);
                        output.accept(GemforgedItems.NYXITE_FUSION_CRYSTAL);
                        output.accept(GemforgedItems.OBSIDIAN_CRYSTAL_POWDER);
                        output.accept(GemforgedItems.PHOENIXITE_FUSION_CRYSTAL);
                        output.accept(GemforgedItems.PRISMYTE_FUSION_CRYSTAL);
                        output.accept(GemforgedItems.QUARTZ_CRYSTAL_POWDER);
                        output.accept(GemforgedItems.SCARLET_FUSION_CRYSTAL);
                        output.accept(GemforgedItems.SOLARIUM_FUSION_CRYSTAL);
                        output.accept(GemforgedItems.STEEL_ALLOY_DUST);
                        output.accept(GemforgedItems.STEEL_INGOT);
                        output.accept(GemforgedItems.VENOMYTE_FUSION_CRYSTAL);
                        output.accept(GemforgedItems.YASHARIUM_ALLOY_DUST);
                        output.accept(GemforgedItems.YASHARIUM_INGOT);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
