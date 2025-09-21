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
                        output.accept(GemforgedItems.ONYX);
                        output.accept(GemforgedItems.SHADOWSTEP_DAGGER);
                        output.accept(GemforgedItems.GARNET);
                        output.accept(GemforgedItems.BATTLE_CHARM);
                        output.accept(GemforgedItems.CITRINE);
                        output.accept(GemforgedItems.SANDBURST_STAFF);
                        output.accept(GemforgedItems.MALACHITE);
                        output.accept(GemforgedItems.VENOMFANG_BLADE);
                        output.accept(GemforgedItems.CARNELIAN);
                        output.accept(GemforgedItems.PHOENIX_RELIC);
                        output.accept(GemforgedItems.ZIRCON_PRISM);
                        output.accept(GemforgedItems.SHIMMER_POWDER);
                        output.accept(PotionContents.createItemStack(Items.POTION, GemforgedPotions.SHIMMER));
                        output.accept(PotionContents.createItemStack(Items.SPLASH_POTION, GemforgedPotions.SHIMMER));
                        output.accept(PotionContents.createItemStack(Items.LINGERING_POTION, GemforgedPotions.SHIMMER));
                        output.accept(PotionContents.createItemStack(Items.TIPPED_ARROW, GemforgedPotions.SHIMMER));
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}