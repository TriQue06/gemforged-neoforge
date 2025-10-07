package net.trique.gemforged.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
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

    public static final DeferredItem<Item> PHOENIX_CHARM = ITEMS.register("phoenix_charm",
            () -> new PhoenixCharmItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> THUNDER_PRISM = ITEMS.register("thunder_prism",
            () -> new ThunderPrismItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));

    public static final DeferredItem<Item> GRAVITY_HORN = ITEMS.register("gravity_horn",
            () -> new GravityHornItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> NYXITE = ITEMS.register("nyxite",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> BLOODSTONE = ITEMS.register("bloodstone",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> SOLARIUM = ITEMS.register("solarium",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> VENOMYTE = ITEMS.register("venomyte",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> PHOENIXTONE = ITEMS.register("phoenixtone",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> PRISMYTE = ITEMS.register("prismyte",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> GRAVITIUM = ITEMS.register("gravitium",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> SHIMMER_POWDER = ITEMS.register("shimmer_powder",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final DeferredItem<Item> DAGGER_TEMPLATE = ITEMS.register("dagger_template",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredItem<Item> CHARM_TEMPLATE = ITEMS.register("charm_template",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredItem<Item> STAFF_TEMPLATE = ITEMS.register("staff_template",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredItem<Item> PRISM_TEMPLATE = ITEMS.register("prism_template",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredItem<Item> BLADE_TEMPLATE = ITEMS.register("blade_template",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final DeferredItem<Item> HORN_TEMPLATE = ITEMS.register("horn_template",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}