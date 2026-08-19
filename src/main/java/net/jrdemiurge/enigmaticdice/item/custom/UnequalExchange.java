package net.jrdemiurge.enigmaticdice.item.custom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.jrdemiurge.enigmaticdice.Config;
import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.item.ModItems;
import net.jrdemiurge.enigmaticdice.item.custom.unequalexchange.UnequalExchangeData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class UnequalExchange extends SwordItem {

    private static final ResourceLocation HEALTH_DEBUFF_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "unequal_exchange_health_debuff");
    private static final ResourceLocation ATTACK_SPEED_DEBUFF_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "unequal_exchange_attack_speed_debuff");
    private static final ResourceLocation ARMOR_DEBUFF_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "unequal_exchange_armor_debuff");
    private static final ResourceLocation ARMOR_TOUGHNESS_DEBUFF_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "unequal_exchange_armor_toughness_debuff");
    private static final ResourceLocation SPEED_DEBUFF_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "unequal_exchange_speed_debuff");

    public UnequalExchange(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    // Nota: Sin @Override porque la firma en 1.21.1 cambió, lo tratamos como método helper o de interfaz custom
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "unequal_exchange_damage"),
                Config.UnequalExchangeAttackDamage - 1, AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "unequal_exchange_speed"),
                Config.UnequalExchangeAttackSpeed - 4, AttributeModifier.Operation.ADD_VALUE));
        return modifiers;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide && attacker instanceof Player player) {
            if (player.getAttackStrengthScale(0.5F) > 0.9F) {
                removeModifierIfExists(player, Attributes.MAX_HEALTH, HEALTH_DEBUFF_ID);
                removeModifierIfExists(player, Attributes.ATTACK_SPEED, ATTACK_SPEED_DEBUFF_ID);
                removeModifierIfExists(player, Attributes.ARMOR, ARMOR_DEBUFF_ID);
                removeModifierIfExists(player, Attributes.ARMOR_TOUGHNESS, ARMOR_TOUGHNESS_DEBUFF_ID);
                removeModifierIfExists(player, Attributes.MOVEMENT_SPEED, SPEED_DEBUFF_ID);

                player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(new AttributeModifier(
                        HEALTH_DEBUFF_ID, -Config.UnequalExchangePlayerHealthReduction, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

                player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(
                        ATTACK_SPEED_DEBUFF_ID, -Config.UnequalExchangeStatDebuff, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

                player.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(
                        ARMOR_DEBUFF_ID, -Config.UnequalExchangeStatDebuff, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

                player.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(new AttributeModifier(
                        ARMOR_TOUGHNESS_DEBUFF_ID, -Config.UnequalExchangeStatDebuff, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(
                        SPEED_DEBUFF_ID, -Config.UnequalExchangeStatDebuff, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    public static void updateModifiers(Player player, UnequalExchangeData data) {
        if (data.getHitCount() <= 0) return;

        double healthReduction = -Config.UnequalExchangePlayerHealthReduction;
        double statDebuff = -Config.UnequalExchangeStatDebuff;

        player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(new AttributeModifier(
                HEALTH_DEBUFF_ID, healthReduction, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(
                ATTACK_SPEED_DEBUFF_ID, statDebuff, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        player.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(
                ARMOR_DEBUFF_ID, statDebuff, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        player.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(new AttributeModifier(
                ARMOR_TOUGHNESS_DEBUFF_ID, statDebuff, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(
                SPEED_DEBUFF_ID, statDebuff, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    private static void removeModifierIfExists(Player player, Holder<Attribute> attribute, ResourceLocation id) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(id);
        }
    }

    // MÉTODO AÑADIDO: Para resolver el error en LivingHurtLowestHandler
    public static boolean isHeldMainHand(Player player) {
        return player.getMainHandItem().is(ModItems.UNEQUAL_EXCHANGE.get());
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, java.util.List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.unequal_exchange_1"));
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.unequal_exchange_2")
                    .withStyle(ChatFormatting.GOLD));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.holdShift"));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}