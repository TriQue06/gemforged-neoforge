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
        generatedItem(GemforgedItems.ONYX);
        handheldItem(GemforgedItems.SHADOWSTEP_DAGGER);
        generatedItem(GemforgedItems.GARNET);
        handheldItem(GemforgedItems.BATTLE_CHARM);
        generatedItem(GemforgedItems.CITRINE);
        handheldItem(GemforgedItems.SANDBURST_STAFF);
        generatedItem(GemforgedItems.MALACHITE);
        handheldItem(GemforgedItems.VENOMFANG_BLADE);
        generatedItem(GemforgedItems.CARNELIAN);
        handheldItem(GemforgedItems.PHOENIX_RELIC);
        generatedItem(GemforgedItems.ZIRCON);
        handheldItem(GemforgedItems.THUNDER_PRISM);
        generatedItem(GemforgedItems.SHIMMER_POWDER);
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