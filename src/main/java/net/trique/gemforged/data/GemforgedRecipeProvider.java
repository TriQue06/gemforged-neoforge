package net.trique.gemforged.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
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
    }
}