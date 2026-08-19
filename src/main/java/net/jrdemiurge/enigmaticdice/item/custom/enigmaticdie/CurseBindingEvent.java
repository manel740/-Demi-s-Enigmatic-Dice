package net.jrdemiurge.enigmaticdice.item.custom.enigmaticdie;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

public class CurseBindingEvent extends RandomEvent {
    public CurseBindingEvent(int rarity) { this.rarity = rarity; }

    @Override
    public boolean execute(Level pLevel, Player pPlayer, boolean guaranteed) {
        if (!guaranteed && !rollChance(pLevel, pPlayer, rarity)) return false;

        ItemStack itemStack = pPlayer.getMainHandItem();
        if (!itemStack.isEmpty()) {
            ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("minecraft:binding_curse"));
            pLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(key).ifPresent(holder -> {
                ItemEnchantments current = itemStack.get(DataComponents.ENCHANTMENTS);
                if (current == null || current.getLevel(holder) == 0) {
                    ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(current != null ? current : ItemEnchantments.EMPTY);
                    mutable.set(holder, 1);
                    itemStack.set(DataComponents.ENCHANTMENTS, mutable.toImmutable());
                }
            });
        }
        return true;
    }
}