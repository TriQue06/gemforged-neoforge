package net.trique.gemforged.event;

import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.trique.gemforged.item.GemforgedItems;
import net.trique.gemforged.potion.GemforgedPotions;

public class GemforgedEvents {

    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(Potions.AWKWARD, GemforgedItems.SHIMMER_POWDER.get(), GemforgedPotions.SHIMMER);
    }
}