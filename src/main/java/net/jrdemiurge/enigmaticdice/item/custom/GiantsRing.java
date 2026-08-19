package net.jrdemiurge.enigmaticdice.item.custom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.jrdemiurge.enigmaticdice.Config;
import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.attribute.ModAttributes;
import net.jrdemiurge.enigmaticdice.item.ModItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Team;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class GiantsRing extends Item implements ICurioItem {

    private static final ResourceLocation STEP_HEIGHT_BONUS_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "giants_ring_step_height");
    private static final ResourceLocation SIZE_SCALE_MULTIPLIER_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "giants_ring_size_scale");
    private static final ResourceLocation HEALTH_BONUS_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "giants_ring_health_bonus");
    private static final ResourceLocation SPEED_BONUS_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "giants_ring_speed_bonus");
    private static final ResourceLocation DAMAGE_BONUS_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "giants_ring_damage_bonus");

    private final Map<LivingEntity, Integer> stompCooldowns = new WeakHashMap<>();

    public GiantsRing(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canEquipFromUse(SlotContext context, ItemStack stack) {
        return true;
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity().level().isClientSide || !(slotContext.entity() instanceof Player player))
            return;

        AttributeInstance stepHeight = player.getAttribute(Attributes.STEP_HEIGHT);
        if (stepHeight != null && stepHeight.hasModifier(STEP_HEIGHT_BONUS_ID)) {
            stepHeight.removeModifier(STEP_HEIGHT_BONUS_ID);
        }

        AttributeInstance sizeScale = player.getAttribute(ModAttributes.SIZE_SCALE);
        if (sizeScale != null && sizeScale.hasModifier(SIZE_SCALE_MULTIPLIER_ID)) {
            sizeScale.removeModifier(SIZE_SCALE_MULTIPLIER_ID);
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (slotContext.entity().level().isClientSide || !(slotContext.entity() instanceof Player player))
            return;

        AttributeInstance stepHeight = player.getAttribute(Attributes.STEP_HEIGHT);
        if (stepHeight != null && !stepHeight.hasModifier(STEP_HEIGHT_BONUS_ID)) {
            stepHeight.addTransientModifier(new AttributeModifier(STEP_HEIGHT_BONUS_ID, 1.0, AttributeModifier.Operation.ADD_VALUE));
        }

        AttributeInstance sizeScale = player.getAttribute(ModAttributes.SIZE_SCALE);
        if (sizeScale != null && !sizeScale.hasModifier(SIZE_SCALE_MULTIPLIER_ID)) {
            sizeScale.addTransientModifier(new AttributeModifier(SIZE_SCALE_MULTIPLIER_ID, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().level().isClientSide || !(slotContext.entity() instanceof Player player))
            return;

        if (!player.isSprinting() || player.isSwimming()) return;

        int currentTick = player.tickCount;
        double totalDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);

        ItemStack weapon = player.getMainHandItem();
        // CORRECCIÓN: En 1.21.1 se usa ItemAttributeModifiers
        ItemAttributeModifiers weaponModifiers = weapon.getAttributeModifiers();

        for (var entry : weaponModifiers.modifiers()) {
            if (entry.attribute().is(Attributes.ATTACK_DAMAGE)) {
                if (entry.modifier().operation() == AttributeModifier.Operation.ADD_VALUE) {
                    totalDamage -= entry.modifier().amount();
                }
            }
        }

        float radius = 1f;
        AABB playerBox = player.getBoundingBox();
        AABB stompBox = new AABB(
                playerBox.minX - radius, player.getY() - 0.1, playerBox.minZ - radius,
                playerBox.maxX + radius, player.getY() + 0.4, playerBox.maxZ + radius
        );

        List<LivingEntity> victims = player.level().getEntitiesOfClass(
                LivingEntity.class, stompBox,
                e -> e != player && e.isAlive() && e.isPickable()
        );

        for (LivingEntity victim : victims) {
            Integer nextAvailableTick = stompCooldowns.getOrDefault(victim, 0);

            if (currentTick >= nextAvailableTick) {
                double playerVolume = player.getBoundingBox().getXsize() * player.getBoundingBox().getYsize() * player.getBoundingBox().getZsize();
                double victimVolume = victim.getBoundingBox().getXsize() * victim.getBoundingBox().getYsize() * victim.getBoundingBox().getZsize();

                if (playerVolume > victimVolume && checkFriendlyFire(victim, player)) {
                    victim.hurt(player.damageSources().playerAttack(player), (float) totalDamage);
                    stompCooldowns.put(victim, currentTick + 20);
                }
            }
        }
    }

    public static boolean checkFriendlyFire(LivingEntity target, LivingEntity attacker) {
        Team attackerTeam = attacker.getTeam();
        Team entityTeam = target.getTeam();
        if (entityTeam != null && attackerTeam == entityTeam && !attackerTeam.isAllowFriendlyFire()) {
            return false;
        } else {
            if (target instanceof OwnableEntity tameable && tameable.getOwner() != null) {
                LivingEntity owner = tameable.getOwner();
                if (owner == attacker) {
                    return false;
                }
                Team ownerTeam = owner.getTeam();
                if (ownerTeam != null && attackerTeam == ownerTeam && !attackerTeam.isAllowFriendlyFire()) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean isWearingGiantRing(LivingEntity livingEntity) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.GIANTS_RING.get(), livingEntity).isPresent();
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attributes = HashMultimap.create();
        attributes.put(Attributes.MAX_HEALTH, new AttributeModifier(HEALTH_BONUS_ID, Config.GiantsRingMaxHealth, AttributeModifier.Operation.ADD_VALUE));
        attributes.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(SPEED_BONUS_ID, Config.GiantsRingSpeedBonus, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attributes.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(DAMAGE_BONUS_ID, Config.GiantsRingAttackDamage, AttributeModifier.Operation.ADD_VALUE));
        return attributes;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.giants_ring_1"));
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.giants_ring_2"));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.giants_ring_0"));
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.holdShift"));
        }
    }
}