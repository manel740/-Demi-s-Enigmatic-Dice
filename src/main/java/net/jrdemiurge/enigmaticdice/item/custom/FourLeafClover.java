package net.jrdemiurge.enigmaticdice.item.custom;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class FourLeafClover extends Item {
    // CAMBIO: Usar ResourceLocation en lugar de UUID
    private static final ResourceLocation LUCK_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "luck_modifier");

    public FourLeafClover(Properties pProperties) {
        super(pProperties);
    }

    public static void updateLuckModifier(Player player) {
        int cloverCount = getCloverCount(player);
        AttributeInstance luckAttribute = player.getAttribute(Attributes.LUCK);

        if (luckAttribute != null) {
            AttributeModifier existingModifier = luckAttribute.getModifier(LUCK_MODIFIER_ID);

            if (existingModifier == null) {
                luckAttribute.addTransientModifier(new AttributeModifier(LUCK_MODIFIER_ID, cloverCount, AttributeModifier.Operation.ADD_VALUE));
            } else if (existingModifier.amount() != (double) cloverCount) {
                luckAttribute.removeModifier(LUCK_MODIFIER_ID);
                luckAttribute.addTransientModifier(new AttributeModifier(LUCK_MODIFIER_ID, cloverCount, AttributeModifier.Operation.ADD_VALUE));
            }
        }
    }

    public static void removeLuckModifier(Player player) {
        AttributeInstance luckAttr = player.getAttribute(Attributes.LUCK);
        if (luckAttr != null) {
            AttributeModifier mod = luckAttr.getModifier(LUCK_MODIFIER_ID);
            if (mod != null) {
                luckAttr.removeModifier(LUCK_MODIFIER_ID);
            }
        }
    }

    private static int getCloverCount(Player player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof FourLeafClover) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}