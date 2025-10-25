package net.trique.gemforged.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.trique.gemforged.block.GemforgedBlocks;
import net.trique.gemforged.item.GemforgedItems;

import java.util.concurrent.CompletableFuture;

public class GemforgedRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public GemforgedRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput out) {
        makeStorageRecipes(out, GemforgedItems.NYXITE.get(), GemforgedBlocks.NYXITE_BLOCK.get(), "nyxite");
        makeStorageRecipes(out, GemforgedItems.BLOODSTONE.get(), GemforgedBlocks.BLOODSTONE_BLOCK.get(), "bloodstone");
        makeStorageRecipes(out, GemforgedItems.SOLARIUM.get(), GemforgedBlocks.SOLARIUM_BLOCK.get(), "solarium");
        makeStorageRecipes(out, GemforgedItems.VENOMYTE.get(), GemforgedBlocks.VENOMYTE_BLOCK.get(), "venomyte");
        makeStorageRecipes(out, GemforgedItems.PHOENIXTONE.get(), GemforgedBlocks.PHOENIXTONE_BLOCK.get(), "phoenixtone");
        makeStorageRecipes(out, GemforgedItems.PRISMYTE.get(), GemforgedBlocks.PRISMYTE_BLOCK.get(), "prismyte");
        makeStorageRecipes(out, GemforgedItems.GRAVITIUM.get(), GemforgedBlocks.GRAVITIUM_BLOCK.get(), "gravitium");
        makeStorageRecipes(out, GemforgedItems.VERDANTITE.get(), GemforgedBlocks.VERDANTITE_BLOCK.get(), "verdantite");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, GemforgedItems.SHADOWSTEP_DAGGER.get())
                .requires(GemforgedItems.DAGGER_TEMPLATE.get())
                .requires(GemforgedItems.NYXITE.get())
                .unlockedBy("has_dagger_template", has(GemforgedItems.DAGGER_TEMPLATE.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", "shadowstep_dagger_crafting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, GemforgedItems.BATTLE_CHARM.get())
                .requires(GemforgedItems.CHARM_TEMPLATE.get())
                .requires(GemforgedItems.BLOODSTONE.get())
                .unlockedBy("has_charm_template", has(GemforgedItems.CHARM_TEMPLATE.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", "battle_charm_crafting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, GemforgedItems.SANDBURST_STAFF.get())
                .requires(GemforgedItems.STAFF_TEMPLATE.get())
                .requires(GemforgedItems.SOLARIUM.get())
                .unlockedBy("has_staff_template", has(GemforgedItems.STAFF_TEMPLATE.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", "sandburst_staff_crafting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, GemforgedItems.VENOMFANG_BLADE.get())
                .requires(GemforgedItems.BLADE_TEMPLATE.get())
                .requires(GemforgedItems.VENOMYTE.get())
                .unlockedBy("has_blade_template", has(GemforgedItems.BLADE_TEMPLATE.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", "venomfang_blade_crafting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, GemforgedItems.PHOENIX_CHARM.get())
                .requires(GemforgedItems.CHARM_TEMPLATE.get())
                .requires(GemforgedItems.PHOENIXTONE.get())
                .unlockedBy("has_charm_template", has(GemforgedItems.CHARM_TEMPLATE.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", "phoenix_charm_crafting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, GemforgedItems.THUNDER_PRISM.get())
                .requires(GemforgedItems.PRISM_TEMPLATE.get())
                .requires(GemforgedItems.PRISMYTE.get())
                .unlockedBy("has_prism_template", has(GemforgedItems.PRISM_TEMPLATE.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", "thunder_prism_crafting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, GemforgedItems.GRAVITY_HORN.get())
                .requires(GemforgedItems.HORN_TEMPLATE.get())
                .requires(GemforgedItems.GRAVITIUM.get())
                .unlockedBy("has_horn_template", has(GemforgedItems.HORN_TEMPLATE.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", "gravity_horn_crafting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, GemforgedItems.VERDANT_TOTEM.get())
                .requires(GemforgedItems.TOTEM_TEMPLATE.get())
                .requires(GemforgedItems.VERDANTITE.get())
                .unlockedBy("has_totem_template", has(GemforgedItems.TOTEM_TEMPLATE.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", "verdant_totem_crafting"));
    }

    private void makeStorageRecipes(RecipeOutput out, Item gem, net.minecraft.world.level.block.Block block, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, block)
                .define('#', gem)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_" + name, has(gem))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", name + "_block_from_gems"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, gem, 9)
                .requires(block)
                .unlockedBy("has_" + name + "_block", has(block))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", name + "_from_block"));
    }
}
