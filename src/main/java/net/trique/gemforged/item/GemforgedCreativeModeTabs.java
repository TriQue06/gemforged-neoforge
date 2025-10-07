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
                        output.accept(GemforgedItems.PHOENIX_CHARM);
                        output.accept(GemforgedItems.THUNDER_PRISM);
                        output.accept(GemforgedItems.GRAVITY_HORN);

                        output.accept(GemforgedItems.NYXITE);
                        output.accept(GemforgedItems.BLOODSTONE);
                        output.accept(GemforgedItems.SOLARIUM);
                        output.accept(GemforgedItems.VENOMYTE);
                        output.accept(GemforgedItems.PHOENIXTONE);
                        output.accept(GemforgedItems.PRISMYTE);
                        output.accept(GemforgedItems.GRAVITIUM);

                        output.accept(GemforgedItems.SHIMMER_POWDER);
                        output.accept(PotionContents.createItemStack(Items.POTION, GemforgedPotions.SHIMMER));
                        output.accept(PotionContents.createItemStack(Items.SPLASH_POTION, GemforgedPotions.SHIMMER));
                        output.accept(PotionContents.createItemStack(Items.LINGERING_POTION, GemforgedPotions.SHIMMER));
                        output.accept(PotionContents.createItemStack(Items.TIPPED_ARROW, GemforgedPotions.SHIMMER));

                        output.accept(GemforgedItems.DAGGER_TEMPLATE);
                        output.accept(GemforgedItems.CHARM_TEMPLATE);
                        output.accept(GemforgedItems.STAFF_TEMPLATE);
                        output.accept(GemforgedItems.PRISM_TEMPLATE);
                        output.accept(GemforgedItems.BLADE_TEMPLATE);
                        output.accept(GemforgedItems.HORN_TEMPLATE);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
