package net.trique.gemforged.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.item.GemforgedItems;

public class GemforgedItemModelProvider extends ItemModelProvider {

    public GemforgedItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Gemforged.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        handheldItem(GemforgedItems.SHADOWSTEP_DAGGER);
        handheldItem(GemforgedItems.BATTLE_CHARM);
        handheldItem(GemforgedItems.SANDBURST_STAFF);
        handheldItem(GemforgedItems.VENOMFANG_BLADE);
        handheldItem(GemforgedItems.PHOENIX_RELIC);
        handheldItem(GemforgedItems.THUNDER_PRISM);

        generatedItem(GemforgedItems.SHIMMER_POWDER);
        generatedItem(GemforgedItems.AMETHYST_CRYSTAL_POWDER);
        generatedItem(GemforgedItems.CITRINE_CRYSTAL_POWDER);
        generatedItem(GemforgedItems.CITRINE_SHARD);
        generatedItem(GemforgedItems.DIAMOND_CRYSTAL_POWDER);
        generatedItem(GemforgedItems.ELECTRUM_ALLOY_DUST);
        generatedItem(GemforgedItems.ELECTRUM_INGOT);
        generatedItem(GemforgedItems.EMERALD_CRYSTAL_POWDER);
        generatedItem(GemforgedItems.NYXITE_FUSION_CRYSTAL);
        generatedItem(GemforgedItems.OBSIDIAN_CRYSTAL_POWDER);
        generatedItem(GemforgedItems.PHOENIXITE_FUSION_CRYSTAL);
        generatedItem(GemforgedItems.PRISMYTE_FUSION_CRYSTAL);
        generatedItem(GemforgedItems.QUARTZ_CRYSTAL_POWDER);
        generatedItem(GemforgedItems.SCARLET_FUSION_CRYSTAL);
        generatedItem(GemforgedItems.SOLARIUM_FUSION_CRYSTAL);
        generatedItem(GemforgedItems.STEEL_ALLOY_DUST);
        generatedItem(GemforgedItems.STEEL_INGOT);
        generatedItem(GemforgedItems.VENOMYTE_FUSION_CRYSTAL);
        generatedItem(GemforgedItems.YASHARIUM_ALLOY_DUST);
        generatedItem(GemforgedItems.YASHARIUM_INGOT);
    }

    private ItemModelBuilder generatedItem(DeferredItem<?> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated"))
                .texture("layer0", modLoc("item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(DeferredItem<?> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld"))
                .texture("layer0", modLoc("item/" + item.getId().getPath()));
    }
}
