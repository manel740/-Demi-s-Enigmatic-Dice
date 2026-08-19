package net.jrdemiurge.enigmaticdice.item;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.item.custom.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, EnigmaticDice.MOD_ID);

    public static final DeferredHolder<Item, Item> ENIGMATIC_DIE = ITEMS.register("enigmatic_die", () -> new EnigmaticDie(new Item.Properties()));
    public static final DeferredHolder<Item, Item> UNEQUAL_EXCHANGE = ITEMS.register("unequal_exchange", () -> new UnequalExchange(Tiers.NETHERITE, new Item.Properties()));
    public static final DeferredHolder<Item, Item> SOUL_EATER = ITEMS.register("soul_eater", () -> new SoulEater(Tiers.NETHERITE, new Item.Properties()));
    public static final DeferredHolder<Item, Item> ANTIMATTER = ITEMS.register("antimatter", () -> new Antimatter(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FOUR_LEAF_CLOVER = ITEMS.register("four_leaf_clover", () -> new FourLeafClover(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GIANTS_RING = ITEMS.register("giants_ring", () -> new GiantsRing(new Item.Properties()));
    public static final DeferredHolder<Item, Item> MOON_SHARD = ITEMS.register("moon_shard", () -> new MoonShard(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GRAVITY_CORE = ITEMS.register("gravity_core", () -> new GravityCore(new Item.Properties()));
    public static final DeferredHolder<Item, Item> MOON = ITEMS.register("moon", () -> new Moon(new Item.Properties()));
    public static final DeferredHolder<Item, Item> RING_OF_AGILITY = ITEMS.register("ring_of_agility", () -> new RingOfAgility(new Item.Properties()));
    public static final DeferredHolder<Item, Item> DIVINE_SHIELD = ITEMS.register("divine_shield", () -> new DivineShield(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PERMAFROST = ITEMS.register("permafrost", () -> new Permafrost(Tiers.NETHERITE, new Item.Properties()));
    //public static final DeferredHolder<Item, Item> KYOMU = ITEMS.register("kyomu", () -> new Kyomu(new Item.Properties()));
    //public static final DeferredHolder<Item, Item> PHOENIX = ITEMS.register("phoenix", () -> new Phoenix(new Item.Properties())); // Asegúrate de que la clase Phoenix exista
    public static final DeferredHolder<Item, Item> CRUCIBLE_OF_RILE = ITEMS.register("crucible_of_rile", () -> new CrucibleOfRile(Tiers.NETHERITE, new Item.Properties()));
    public static final DeferredHolder<Item, Item> MOAI_CHARM = ITEMS.register("moai_charm", () -> new MoaiCharm(new Item.Properties()));
    public static final DeferredHolder<Item, Item> UNFINISHED_WEAPON = ITEMS.register("unfinished_weapon", () -> new UnfinishedWeapon(Tiers.IRON, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}