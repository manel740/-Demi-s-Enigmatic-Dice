package net.jrdemiurge.enigmaticdice.event;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

@EventBusSubscriber(modid = EnigmaticDice.MOD_ID)
public class AdvancementHandler {

    @SubscribeEvent
    public static void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {
        AdvancementHolder holder = event.getAdvancement();
        if (holder == null) return;

        Advancement advancement = holder.value();

        // En 1.21.1, display() devuelve un Optional<DisplayInfo>
        advancement.display().ifPresent(display -> {
            // FrameType ahora se llama AdvancementType
            AdvancementType frame = display.getType();

            if (frame == AdvancementType.GOAL || frame == AdvancementType.CHALLENGE) {
                Level level = event.getEntity().level();
                ItemStack drop = new ItemStack(ModItems.ENIGMATIC_DIE.get());

                ItemEntity entity = new ItemEntity(
                        level,
                        event.getEntity().getX(),
                        event.getEntity().getY(),
                        event.getEntity().getZ(),
                        drop
                );

                level.addFreshEntity(entity);
            }
        });
    }
}