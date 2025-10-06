package net.trique.gemforged.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.item.gear.*;

public class GemforgedItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gemforged.MODID);

    public static final DeferredItem<Item> SHADOWSTEP_DAGGER = ITEMS.register("shadowstep_dagger",
            () -> new ShadowstepDaggerItem(new Item.Properties().rarity(Rarity.EPIC).durability(240)));

    public static final DeferredItem<Item> BATTLE_CHARM = ITEMS.register("battle_charm",
            () -> new BattleCharmItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> SANDBURST_STAFF = ITEMS.register("sandburst_staff",
            () -> new SandburstStaffItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> VENOMFANG_BLADE = ITEMS.register("venomfang_blade",
            () -> new VenomfangBladeItem(new Item.Properties().rarity(Rarity.EPIC).durability(240)));

    public static final DeferredItem<Item> PHOENIX_RELIC = ITEMS.register("phoenix_relic",
            () -> new PhoenixRelicItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> THUNDER_PRISM = ITEMS.register("thunder_prism",
            () -> new ThunderPrismItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));

    public static final DeferredItem<Item> SHIMMER_POWDER = ITEMS.register("shimmer_powder",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final DeferredItem<Item> AMETHYST_CRYSTAL_POWDER = ITEMS.register("amethyst_crystal_powder",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CITRINE_CRYSTAL_POWDER = ITEMS.register("citrine_crystal_powder",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CITRINE_SHARD = ITEMS.register("citrine_shard",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIAMOND_CRYSTAL_POWDER = ITEMS.register("diamond_crystal_powder",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ELECTRUM_ALLOY_DUST = ITEMS.register("electrum_alloy_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ELECTRUM_INGOT = ITEMS.register("electrum_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EMERALD_CRYSTAL_POWDER = ITEMS.register("emerald_crystal_powder",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NYXITE_FUSION_CRYSTAL = ITEMS.register("nyxite_fusion_crystal",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OBSIDIAN_CRYSTAL_POWDER = ITEMS.register("obsidian_crystal_powder",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PHOENIXITE_FUSION_CRYSTAL = ITEMS.register("phoenixite_fusion_crystal",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PRISMYTE_FUSION_CRYSTAL = ITEMS.register("prismyte_fusion_crystal",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> QUARTZ_CRYSTAL_POWDER = ITEMS.register("quartz_crystal_powder",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SCARLET_FUSION_CRYSTAL = ITEMS.register("scarlet_fusion_crystal",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SOLARIUM_FUSION_CRYSTAL = ITEMS.register("solarium_fusion_crystal",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STEEL_ALLOY_DUST = ITEMS.register("steel_alloy_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STEEL_INGOT = ITEMS.register("steel_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VENOMYTE_FUSION_CRYSTAL = ITEMS.register("venomyte_fusion_crystal",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YASHARIUM_ALLOY_DUST = ITEMS.register("yasharium_alloy_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YASHARIUM_INGOT = ITEMS.register("yasharium_ingot",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}