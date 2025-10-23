package net.trique.gemforged.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.block.GemforgedBlocks;

import java.util.function.Supplier;

public class GemforgedCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Gemforged.MODID);

    public static final Supplier<CreativeModeTab> GEMFORGED_ITEMS_TAB = CREATIVE_MODE_TAB.register("gemforgeditemstab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(GemforgedItems.GRAVITY_HORN.get()))
                    .title(Component.translatable("gemforged.tab1"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(GemforgedBlocks.NYXITE_ORE);
                        output.accept(GemforgedBlocks.DEEPSLATE_NYXITE_ORE);
                        output.accept(GemforgedBlocks.NYXITE_BLOCK);

                        output.accept(GemforgedBlocks.BLOODSTONE_ORE);
                        output.accept(GemforgedBlocks.DEEPSLATE_BLOODSTONE_ORE);
                        output.accept(GemforgedBlocks.BLOODSTONE_BLOCK);

                        output.accept(GemforgedBlocks.SOLARIUM_ORE);
                        output.accept(GemforgedBlocks.DEEPSLATE_SOLARIUM_ORE);
                        output.accept(GemforgedBlocks.SOLARIUM_BLOCK);

                        output.accept(GemforgedBlocks.VENOMYTE_ORE);
                        output.accept(GemforgedBlocks.DEEPSLATE_VENOMYTE_ORE);
                        output.accept(GemforgedBlocks.VENOMYTE_BLOCK);

                        output.accept(GemforgedBlocks.PHOENIXTONE_ORE);
                        output.accept(GemforgedBlocks.DEEPSLATE_PHOENIXTONE_ORE);
                        output.accept(GemforgedBlocks.PHOENIXTONE_BLOCK);

                        output.accept(GemforgedBlocks.PRISMYTE_ORE);
                        output.accept(GemforgedBlocks.DEEPSLATE_PRISMYTE_ORE);
                        output.accept(GemforgedBlocks.PRISMYTE_BLOCK);

                        output.accept(GemforgedBlocks.GRAVITIUM_ORE);
                        output.accept(GemforgedBlocks.DEEPSLATE_GRAVITIUM_ORE);
                        output.accept(GemforgedBlocks.GRAVITIUM_BLOCK);

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
