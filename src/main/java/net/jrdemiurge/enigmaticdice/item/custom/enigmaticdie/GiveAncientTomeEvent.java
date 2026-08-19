package net.jrdemiurge.enigmaticdice.item.custom.enigmaticdie;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

public class GiveAncientTomeEvent extends RandomEvent {
    private final Random random = new Random();

    public GiveAncientTomeEvent(int rarity) {
        this.rarity = rarity;
    }

    @Override
    public boolean execute(Level pLevel, Player pPlayer, boolean guaranteed) {
        if (!guaranteed) {
            if (!rollChance(pLevel, pPlayer, rarity)) return false;
        }

        ResourceLocation tomeId = ResourceLocation.parse("quark:ancient_tome");
        Item tomeItem = BuiltInRegistries.ITEM.get(tomeId);
        ItemStack tome = new ItemStack(tomeItem != null ? tomeItem : Items.ENCHANTED_BOOK);

        var enchantmentRegistry = pLevel.registryAccess().registryOrThrow(Registries.ENCHANTMENT);

        var allEnchantments = enchantmentRegistry.holders().toList();

        if (allEnchantments.isEmpty()) return false;

        var chosenHolder = allEnchantments.get(random.nextInt(allEnchantments.size()));
        Enchantment chosenEnchantment = chosenHolder.value();
        int level = 1 + random.nextInt(chosenEnchantment.getMaxLevel());

        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(chosenHolder, level);
        tome.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable());

        tome.set(DataComponents.CUSTOM_NAME, Component.literal("Ancient Tome").withStyle(net.minecraft.ChatFormatting.GOLD));

        pPlayer.getInventory().add(tome);

        Component message = Component.translatable("enigmaticdice.event.give_ancient_tome", chosenEnchantment.description().getString());
        pPlayer.displayClientMessage(message, false);

        return true;
    }
}