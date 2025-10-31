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
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(GemforgedItems.PHOENIX_CHARM.get()))
                    .title(Component.translatable("gemforged.tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(GemforgedBlocks.RANDOM_GEM_VEIN);
                        output.accept(GemforgedBlocks.DEEPSLATE_RANDOM_GEM_VEIN);
                        output.accept(GemforgedBlocks.BLOODSTONE_BLOCK);
                        output.accept(GemforgedBlocks.PHOENIXTONE_BLOCK);
                        output.accept(GemforgedBlocks.SOLARIUM_BLOCK);
                        output.accept(GemforgedBlocks.VERDANTITE_BLOCK);
                        output.accept(GemforgedBlocks.VENOMYTE_BLOCK);
                        output.accept(GemforgedBlocks.PRISMYTE_BLOCK);
                        output.accept(GemforgedBlocks.NYXITE_BLOCK);
                        output.accept(GemforgedBlocks.GRAVITIUM_BLOCK);

                        output.accept(GemforgedItems.BATTLE_CHARM);
                        output.accept(GemforgedItems.PHOENIX_CHARM);
                        output.accept(GemforgedItems.PHOENIXFIRE_STAFF);
                        output.accept(GemforgedItems.SANDBURST_STAFF);
                        output.accept(GemforgedItems.VERDANT_TOTEM);
                        output.accept(GemforgedItems.VENOMFANG_BLADE);
                        output.accept(GemforgedItems.GLACIAL_CHARM);
                        output.accept(GemforgedItems.THUNDER_PRISM);
                        output.accept(GemforgedItems.GHOST_BOW);
                        output.accept(GemforgedItems.SHADOWSTEP_DAGGER);
                        output.accept(GemforgedItems.GRAVITY_HORN);

                        output.accept(GemforgedItems.BLOODSTONE);
                        output.accept(GemforgedItems.PHOENIXTONE);
                        output.accept(GemforgedItems.SOLARIUM);
                        output.accept(GemforgedItems.VERDANTITE);
                        output.accept(GemforgedItems.VENOMYTE);
                        output.accept(GemforgedItems.PRISMYTE);
                        output.accept(GemforgedItems.NYXITE);
                        output.accept(GemforgedItems.GRAVITIUM);

                        output.accept(GemforgedItems.CHARM_TEMPLATE);
                        output.accept(GemforgedItems.STAFF_TEMPLATE);
                        output.accept(GemforgedItems.TOTEM_TEMPLATE);
                        output.accept(GemforgedItems.BLADE_TEMPLATE);
                        output.accept(GemforgedItems.PRISM_TEMPLATE);
                        output.accept(GemforgedItems.BOW_TEMPLATE);
                        output.accept(GemforgedItems.DAGGER_TEMPLATE);
                        output.accept(GemforgedItems.HORN_TEMPLATE);
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}