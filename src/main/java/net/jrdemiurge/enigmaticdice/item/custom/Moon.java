package net.jrdemiurge.enigmaticdice.item.custom;

import net.jrdemiurge.enigmaticdice.Config;
import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.item.ModItems;
import net.jrdemiurge.enigmaticdice.scheduler.Scheduler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class Moon extends Item {
    private static final ResourceLocation GRAVITY_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "moon_gravity");

    public Moon(Properties pProperties) { super(pProperties); }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof Player player) {
            boolean isSneaking = player.isShiftKeyDown();
            boolean isInMainHand = player.getMainHandItem().is(ModItems.MOON.get());
            boolean isInOffHand = player.getOffhandItem().is(ModItems.MOON.get());

            AttributeInstance gravityAttr = player.getAttribute(Attributes.GRAVITY);

            if (gravityAttr != null) {
                AttributeModifier existingModifier = gravityAttr.getModifier(GRAVITY_MODIFIER_ID);
                if (isInMainHand || isInOffHand) {
                    if (!isSneaking) {
                        if (existingModifier == null) {
                            gravityAttr.addTransientModifier(new AttributeModifier(GRAVITY_MODIFIER_ID, Config.MoonGravityReduction, AttributeModifier.Operation.ADD_VALUE));
                        }
                    } else {
                        if (existingModifier != null) {
                            gravityAttr.removeModifier(GRAVITY_MODIFIER_ID);
                        }
                    }
                }
            }

            Scheduler.schedule(() -> {
                boolean stillHolding = player.getMainHandItem().is(ModItems.MOON.get()) || player.getOffhandItem().is(ModItems.MOON.get());
                AttributeInstance laterAttr = player.getAttribute(Attributes.GRAVITY);
                if (!stillHolding && laterAttr != null) {
                    AttributeModifier modifier = laterAttr.getModifier(GRAVITY_MODIFIER_ID);
                    if (modifier != null) {
                        laterAttr.removeModifier(GRAVITY_MODIFIER_ID);
                    }
                }
            }, 4);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.while_held"));
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.moon_1"));
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.while_hotbar"));
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.moon_2"));
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.moon_3"));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.holdShift"));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}