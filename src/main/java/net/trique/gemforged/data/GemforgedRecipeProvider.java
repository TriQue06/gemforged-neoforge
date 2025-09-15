package net.trique.gemforged.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
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

        // Shimmer Powder: Pitcher Plant + Amethyst Shard -> 1x Shimmer Powder
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GemforgedItems.SHIMMER_POWDER.get(), 1)
                .requires(Items.PITCHER_PLANT)
                .requires(Items.AMETHYST_SHARD)
                .unlockedBy("has_pitcher_plant", has(Items.PITCHER_PLANT))
                .unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD))
                .save(out);

        // Shimmer Bottle: Glass Bottle + Shimmer Powder + Blaze Powder -> 1x Shimmer Bottle
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GemforgedItems.SHIMMER_BOTTLE.get(), 1)
                .requires(Items.GLASS_BOTTLE)
                .requires(GemforgedItems.SHIMMER_POWDER.get())
                .requires(Items.BLAZE_POWDER)
                .unlockedBy("has_shimmer_powder", has(GemforgedItems.SHIMMER_POWDER.get()))
                .unlockedBy("has_glass_bottle", has(Items.GLASS_BOTTLE))
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(out);
    }
}
