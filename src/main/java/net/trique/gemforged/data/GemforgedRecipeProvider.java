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

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GemforgedItems.SHADOWSTEP_DAGGER.get())
                .pattern(" O ")
                .pattern(" O ")
                .pattern(" S ")
                .define('O', GemforgedItems.ONYX.get())
                .define('S', Items.STICK)
                .unlockedBy("has_onyx", has(GemforgedItems.ONYX.get()))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GemforgedItems.VENOMFANG_BLADE.get())
                .pattern(" M ")
                .pattern(" M ")
                .pattern(" S ")
                .define('M', GemforgedItems.MALACHITE.get())
                .define('S', Items.STICK)
                .unlockedBy("has_malachite", has(GemforgedItems.MALACHITE.get()))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GemforgedItems.BATTLE_CHARM.get())
                .pattern("GGG")
                .pattern("G G")
                .pattern("GGG")
                .define('G', GemforgedItems.GARNET.get())
                .unlockedBy("has_garnet", has(GemforgedItems.GARNET.get()))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GemforgedItems.PHOENIX_RELIC.get())
                .pattern("CCC")
                .pattern("C C")
                .pattern("CCC")
                .define('C', GemforgedItems.CARNELIAN.get())
                .unlockedBy("has_carnelian", has(GemforgedItems.CARNELIAN.get()))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, GemforgedItems.SANDBURST_STAFF.get())
                .pattern(" # ")
                .pattern(" ##")
                .pattern(" O ")
                .define('#', GemforgedItems.CITRINE.get())
                .define('O', Items.STICK)
                .unlockedBy("has_citrine", has(GemforgedItems.CITRINE.get()))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GemforgedItems.THUNDER_PRISM.get())
                .pattern(" A ")
                .pattern("AA ")
                .pattern("AAA")
                .define('A', GemforgedItems.ZIRCON.get())
                .unlockedBy("has_zircon", has(GemforgedItems.ZIRCON.get()))
                .save(out);
    }
}