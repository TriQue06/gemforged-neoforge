package net.trique.gemforged.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.trique.gemforged.block.GemforgedBlocks;
import net.trique.gemforged.item.GemforgedItems;

import java.util.Set;

public class GemforgedBlockLootTableProvider extends BlockLootSubProvider {

    public GemforgedBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.add(GemforgedBlocks.NYXITE_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.NYXITE.get()));
        this.add(GemforgedBlocks.DEEPSLATE_NYXITE_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.NYXITE.get()));
        this.dropSelf(GemforgedBlocks.NYXITE_BLOCK.get());

        this.add(GemforgedBlocks.BLOODSTONE_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.BLOODSTONE.get()));
        this.add(GemforgedBlocks.DEEPSLATE_BLOODSTONE_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.BLOODSTONE.get()));
        this.dropSelf(GemforgedBlocks.BLOODSTONE_BLOCK.get());

        this.add(GemforgedBlocks.SOLARIUM_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.SOLARIUM.get()));
        this.add(GemforgedBlocks.DEEPSLATE_SOLARIUM_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.SOLARIUM.get()));
        this.dropSelf(GemforgedBlocks.SOLARIUM_BLOCK.get());

        this.add(GemforgedBlocks.VENOMYTE_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.VENOMYTE.get()));
        this.add(GemforgedBlocks.DEEPSLATE_VENOMYTE_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.VENOMYTE.get()));
        this.dropSelf(GemforgedBlocks.VENOMYTE_BLOCK.get());

        this.add(GemforgedBlocks.PHOENIXTONE_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.PHOENIXTONE.get()));
        this.add(GemforgedBlocks.DEEPSLATE_PHOENIXTONE_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.PHOENIXTONE.get()));
        this.dropSelf(GemforgedBlocks.PHOENIXTONE_BLOCK.get());

        this.add(GemforgedBlocks.PRISMYTE_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.PRISMYTE.get()));
        this.add(GemforgedBlocks.DEEPSLATE_PRISMYTE_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.PRISMYTE.get()));
        this.dropSelf(GemforgedBlocks.PRISMYTE_BLOCK.get());

        this.add(GemforgedBlocks.GRAVITIUM_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.GRAVITIUM.get()));
        this.add(GemforgedBlocks.DEEPSLATE_GRAVITIUM_ORE.get(),
                block -> createOreDrop(block, GemforgedItems.GRAVITIUM.get()));
        this.dropSelf(GemforgedBlocks.GRAVITIUM_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GemforgedBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
