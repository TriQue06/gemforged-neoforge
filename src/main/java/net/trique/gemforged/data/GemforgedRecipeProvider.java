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
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GemforgedItems.SHIMMER_POWDER.get(), 1)
                .requires(Items.PITCHER_PLANT)
                .requires(Items.AMETHYST_SHARD)
                .requires(Items.BLAZE_POWDER)
                .unlockedBy("has_pitcher_plant", has(Items.PITCHER_PLANT))
                .unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD))
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(out);

        makeStorageRecipes(out, GemforgedItems.NYXITE.get(), GemforgedBlocks.NYXITE_BLOCK.get(), "nyxite");
        makeStorageRecipes(out, GemforgedItems.BLOODSTONE.get(), GemforgedBlocks.BLOODSTONE_BLOCK.get(), "bloodstone");
        makeStorageRecipes(out, GemforgedItems.SOLARIUM.get(), GemforgedBlocks.SOLARIUM_BLOCK.get(), "solarium");
        makeStorageRecipes(out, GemforgedItems.VENOMYTE.get(), GemforgedBlocks.VENOMYTE_BLOCK.get(), "venomyte");
        makeStorageRecipes(out, GemforgedItems.PHOENIXTONE.get(), GemforgedBlocks.PHOENIXTONE_BLOCK.get(), "phoenixtone");
        makeStorageRecipes(out, GemforgedItems.PRISMYTE.get(), GemforgedBlocks.PRISMYTE_BLOCK.get(), "prismyte");
        makeStorageRecipes(out, GemforgedItems.GRAVITIUM.get(), GemforgedBlocks.GRAVITIUM_BLOCK.get(), "gravitium");

        makeOreCookingRecipes(out, GemforgedBlocks.NYXITE_ORE.get(), GemforgedItems.NYXITE.get(), "nyxite");
        makeOreCookingRecipes(out, GemforgedBlocks.DEEPSLATE_NYXITE_ORE.get(), GemforgedItems.NYXITE.get(), "deepslate_nyxite");

        makeOreCookingRecipes(out, GemforgedBlocks.BLOODSTONE_ORE.get(), GemforgedItems.BLOODSTONE.get(), "bloodstone");
        makeOreCookingRecipes(out, GemforgedBlocks.DEEPSLATE_BLOODSTONE_ORE.get(), GemforgedItems.BLOODSTONE.get(), "deepslate_bloodstone");

        makeOreCookingRecipes(out, GemforgedBlocks.SOLARIUM_ORE.get(), GemforgedItems.SOLARIUM.get(), "solarium");
        makeOreCookingRecipes(out, GemforgedBlocks.DEEPSLATE_SOLARIUM_ORE.get(), GemforgedItems.SOLARIUM.get(), "deepslate_solarium");

        makeOreCookingRecipes(out, GemforgedBlocks.VENOMYTE_ORE.get(), GemforgedItems.VENOMYTE.get(), "venomyte");
        makeOreCookingRecipes(out, GemforgedBlocks.DEEPSLATE_VENOMYTE_ORE.get(), GemforgedItems.VENOMYTE.get(), "deepslate_venomyte");

        makeOreCookingRecipes(out, GemforgedBlocks.PHOENIXTONE_ORE.get(), GemforgedItems.PHOENIXTONE.get(), "phoenixtone");
        makeOreCookingRecipes(out, GemforgedBlocks.DEEPSLATE_PHOENIXTONE_ORE.get(), GemforgedItems.PHOENIXTONE.get(), "deepslate_phoenixtone");

        makeOreCookingRecipes(out, GemforgedBlocks.PRISMYTE_ORE.get(), GemforgedItems.PRISMYTE.get(), "prismyte");
        makeOreCookingRecipes(out, GemforgedBlocks.DEEPSLATE_PRISMYTE_ORE.get(), GemforgedItems.PRISMYTE.get(), "deepslate_prismyte");

        makeOreCookingRecipes(out, GemforgedBlocks.GRAVITIUM_ORE.get(), GemforgedItems.GRAVITIUM.get(), "gravitium");
        makeOreCookingRecipes(out, GemforgedBlocks.DEEPSLATE_GRAVITIUM_ORE.get(), GemforgedItems.GRAVITIUM.get(), "deepslate_gravitium");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, GemforgedItems.SHADOWSTEP_DAGGER.get())
                .requires(GemforgedItems.DAGGER_TEMPLATE.get())
                .requires(GemforgedItems.NYXITE.get())
                .unlockedBy("has_dagger_template", has(GemforgedItems.DAGGER_TEMPLATE.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", "shadowstep_dagger_crafting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GemforgedItems.BATTLE_CHARM.get())
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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GemforgedItems.PHOENIX_CHARM.get())
                .requires(GemforgedItems.CHARM_TEMPLATE.get())
                .requires(GemforgedItems.PHOENIXTONE.get())
                .unlockedBy("has_charm_template", has(GemforgedItems.CHARM_TEMPLATE.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", "phoenix_charm_crafting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GemforgedItems.THUNDER_PRISM.get())
                .requires(GemforgedItems.PRISM_TEMPLATE.get())
                .requires(GemforgedItems.PRISMYTE.get())
                .unlockedBy("has_prism_template", has(GemforgedItems.PRISM_TEMPLATE.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", "thunder_prism_crafting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, GemforgedItems.GRAVITY_HORN.get())
                .requires(GemforgedItems.HORN_TEMPLATE.get())
                .requires(GemforgedItems.GRAVITIUM.get())
                .unlockedBy("has_horn_template", has(GemforgedItems.HORN_TEMPLATE.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", "gravity_horn_crafting"));
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

    private void makeOreCookingRecipes(RecipeOutput out, net.minecraft.world.level.block.Block ore, Item result, String name) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ore), RecipeCategory.MISC, result, 1.0f, 200)
                .unlockedBy("has_" + name + "_ore", has(ore))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", name + "_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ore), RecipeCategory.MISC, result, 1.0f, 100)
                .unlockedBy("has_" + name + "_ore", has(ore))
                .save(out, ResourceLocation.fromNamespaceAndPath("gemforged", name + "_blasting"));
    }
}
