package net.jrdemiurge.enigmaticdice.item.custom.enigmaticdie;

import net.jrdemiurge.enigmaticdice.attribute.ModAttributes;
import net.jrdemiurge.enigmaticdice.scheduler.Scheduler;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class AntSizeShrinkEvent extends RandomEvent {

    // En 1.21.1, los modificadores usan ResourceLocation en lugar de UUID y String
    public static final ResourceLocation ANT_SIZE_SCALE_ID = ResourceLocation.fromNamespaceAndPath("enigmaticdice", "ant_size_scale");
    public static final AttributeModifier ANT_SIZE_SCALE_MULTIPLIER = new AttributeModifier(ANT_SIZE_SCALE_ID, -0.9, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public AntSizeShrinkEvent(int rarity) {
        this.rarity = rarity;
    }

    @Override
    public boolean execute(Level pLevel, Player pPlayer, boolean guaranteed) {
        if (!guaranteed) {
            if (!rollChance(pLevel, pPlayer, rarity)) return false;
        }

        int debuffDuration = 20 * 60 * 10;

        AttributeInstance sizeScaleAttribute = pPlayer.getAttribute(ModAttributes.SIZE_SCALE);
        if (sizeScaleAttribute == null) return false;

        // hasModifier ahora recibe el ResourceLocation
        if (sizeScaleAttribute.hasModifier(ANT_SIZE_SCALE_ID)) return false;

        sizeScaleAttribute.addTransientModifier(ANT_SIZE_SCALE_MULTIPLIER);

        Scheduler.schedule(() -> {
            // removeModifier ahora recibe el ResourceLocation
            sizeScaleAttribute.removeModifier(ANT_SIZE_SCALE_ID);
        }, debuffDuration);

        MutableComponent message = Component.translatable("enigmaticdice.event.ant_size_shrink");
        pPlayer.displayClientMessage(message, false);
        return true;
    }
}