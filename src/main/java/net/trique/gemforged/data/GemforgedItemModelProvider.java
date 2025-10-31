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
        handheldItem(GemforgedItems.PHOENIX_CHARM);
        handheldItem(GemforgedItems.THUNDER_PRISM);
        handheldItem(GemforgedItems.GRAVITY_HORN);
        generatedItem(GemforgedItems.VERDANT_TOTEM);
        handheldItem(GemforgedItems.PHOENIXFIRE_STAFF);
        handheldItem(GemforgedItems.GLACIAL_CHARM);

        generatedItem(GemforgedItems.NYXITE);
        generatedItem(GemforgedItems.BLOODSTONE);
        generatedItem(GemforgedItems.SOLARIUM);
        generatedItem(GemforgedItems.VENOMYTE);
        generatedItem(GemforgedItems.PHOENIXTONE);
        generatedItem(GemforgedItems.PRISMYTE);
        generatedItem(GemforgedItems.GRAVITIUM);
        generatedItem(GemforgedItems.VERDANTITE);

        handheldItem(GemforgedItems.DAGGER_TEMPLATE);
        handheldItem(GemforgedItems.CHARM_TEMPLATE);
        handheldItem(GemforgedItems.STAFF_TEMPLATE);
        handheldItem(GemforgedItems.PRISM_TEMPLATE);
        handheldItem(GemforgedItems.BLADE_TEMPLATE);
        handheldItem(GemforgedItems.HORN_TEMPLATE);
        handheldItem(GemforgedItems.TOTEM_TEMPLATE);
        handheldItem(GemforgedItems.BOW_TEMPLATE);
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
