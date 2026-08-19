package net.jrdemiurge.enigmaticdice.event;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

@EventBusSubscriber(modid = EnigmaticDice.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class TooltipEventHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack.getItem() == ModItems.GIANTS_RING.get() ||
                stack.getItem() == ModItems.RING_OF_AGILITY.get() ||
                stack.getItem() == ModItems.DIVINE_SHIELD.get()) {

            List<Component> tooltip = event.getToolTip();
            if (tooltip.size() > 1) {
                tooltip.remove(1);
            }
        }
    }
}