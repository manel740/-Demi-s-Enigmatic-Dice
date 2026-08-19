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

public class EternalBindingEvent extends RandomEvent {
    private static final ResourceLocation ETERNAL_BINDING_ID = ResourceLocation.fromNamespaceAndPath("enigmaticlegacy", "eternal_binding");

    public EternalBindingEvent(int rarity) {
        this.rarity = rarity;
    }

    @Override
    public boolean execute(Level pLevel, Player pPlayer, boolean guaranteed) {
        if (!guaranteed) {
            if (!rollChance(pLevel, pPlayer, rarity)) return false;
        }

        ItemStack itemStack = pPlayer.getMainHandItem();
        if (!itemStack.isEmpty()) {
            // CORRECCIÓN CRÍTICA: Convertir ResourceLocation a ResourceKey para el registro de encantamientos
            ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, ETERNAL_BINDING_ID);

            pLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(key).ifPresent(holder -> {
                ItemEnchantments currentEnchantments = itemStack.get(DataComponents.ENCHANTMENTS);
                if (currentEnchantments == null || currentEnchantments.getLevel(holder) == 0) {
                    ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(currentEnchantments != null ? currentEnchantments : ItemEnchantments.EMPTY);
                    mutable.set(holder, 1);
                    itemStack.set(DataComponents.ENCHANTMENTS, mutable.toImmutable());
                }
            });
        }
        return true;
    }
}