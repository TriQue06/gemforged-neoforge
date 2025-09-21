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

    public static final DeferredItem<Item> ONYX = ITEMS.register("onyx",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GARNET = ITEMS.register("garnet",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CITRINE = ITEMS.register("citrine",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MALACHITE = ITEMS.register("malachite",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CARNELIAN = ITEMS.register("carnelian",
            () -> new Item(new Item.Properties()));

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

    public static final DeferredItem<Item> ZIRCON_PRISM = ITEMS.register("zircon_prism",
            () -> new ZirconPrismItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> SHIMMER_POWDER = ITEMS.register("shimmer_powder",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}