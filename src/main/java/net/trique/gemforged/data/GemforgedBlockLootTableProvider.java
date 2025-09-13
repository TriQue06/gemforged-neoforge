package net.trique.gemforged.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.trique.gemforged.block.GemforgedBlocks;

import java.util.Set;

public class GemforgedBlockLootTableProvider extends BlockLootSubProvider {

    public GemforgedBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GemforgedBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}