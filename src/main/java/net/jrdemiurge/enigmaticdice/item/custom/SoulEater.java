package net.jrdemiurge.enigmaticdice.item.custom;

import net.jrdemiurge.enigmaticdice.Config;
import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.effect.ModEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.jrdemiurge.enigmaticdice.EnigmaticDice;

import java.util.List;

public class SoulEater extends SwordItem {

    private static final ResourceLocation SOUL_EATER_HEALTH_BUFF_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "soul_eater_health_buff");

    public SoulEater(Tier pTier, Properties pProperties) {
        super(pTier, pProperties.attributes(
                ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                                Item.BASE_ATTACK_DAMAGE_ID,
                                Config.SoulEaterAttackDamage - 1.0, // 10.0 - 1.0 = 9.0 (Total: 1.0 + 9.0 = 10.0)
                                AttributeModifier.Operation.ADD_VALUE
                        ), EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(
                                Item.BASE_ATTACK_SPEED_ID,
                                Config.SoulEaterAttackSpeed - 4.0, // 2.0 - 4.0 = -2.0 (Total: 4.0 + -2.0 = 2.0)
                                AttributeModifier.Operation.ADD_VALUE
                        ), EquipmentSlotGroup.MAINHAND)
                        .build()
        ));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide && attacker instanceof Player player) {
            if (player.getAttackStrengthScale(0.5F) > 0.9F) {
                float currentHealth = player.getHealth();
                float maxHealth = player.getMaxHealth();

                if (currentHealth > 1.0F) {
                    float spentHealth = Math.min(currentHealth - 1.0F, maxHealth * 0.5F);
                    player.setHealth(currentHealth - spentHealth);

                    AttributeInstance attr = player.getAttribute(Attributes.MAX_HEALTH);
                    if (attr != null) {
                        attr.removeModifier(SOUL_EATER_HEALTH_BUFF_ID);

                        float healthBoost = spentHealth * (float) Config.SoulEaterMaxHealthStealPercent;
                        AttributeModifier healthMod = new AttributeModifier(
                                SOUL_EATER_HEALTH_BUFF_ID,
                                healthBoost,
                                AttributeModifier.Operation.ADD_VALUE
                        );
                        attr.addTransientModifier(healthMod);

                        player.setHealth(Math.min(player.getHealth() + healthBoost, player.getMaxHealth()));
                        player.addEffect(new MobEffectInstance(ModEffects.SOUL_EATER_HEALTH_BOOST, 20 * Config.SoulEaterMaxHealthBuffDuration, 0));
                    }
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        // CORRECCIÓN: El tercer parámetro debe ser Entity, no LivingEntity
        if (!player.level().isClientSide && entity instanceof LivingEntity livingEntity) {
            player.removeEffect(ModEffects.SOUL_EATER_CHARGED_HIT);
            float spentHealthSum = 0;

            AttributeInstance attr = player.getAttribute(Attributes.MAX_HEALTH);
            if (attr != null && attr.getModifier(SOUL_EATER_HEALTH_BUFF_ID) != null) {
                spentHealthSum = (float) attr.getModifier(SOUL_EATER_HEALTH_BUFF_ID).amount();
                attr.removeModifier(SOUL_EATER_HEALTH_BUFF_ID);
                player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));
            }

            if (spentHealthSum > 0) {
                livingEntity.addEffect(new MobEffectInstance(ModEffects.SOUL_EATER_CHARGED_HIT, 20 * Config.SoulEaterChargeDuration, (int) (spentHealthSum - 1)));
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.soul_eater_1"));
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.soul_eater_2").withStyle(ChatFormatting.GOLD));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.holdShift"));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}