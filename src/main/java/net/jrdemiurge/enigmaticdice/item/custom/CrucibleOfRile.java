package net.jrdemiurge.enigmaticdice.item.custom;

import net.jrdemiurge.enigmaticdice.Config;
import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.effect.ModEffects;
import net.jrdemiurge.enigmaticdice.network.LookAtTargetPayload;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.jrdemiurge.enigmaticdice.EnigmaticDice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrucibleOfRile extends SwordItem implements IItemExtension {

    private static final ResourceLocation ARMOR_BOOST_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "armor_boost");
    private static final ResourceLocation TOUGHNESS_BOOST_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "toughness_boost");
    private static final String TAG_UNIQUE_KILLS = "UniqueKills";

    public CrucibleOfRile(Tier pTier, Properties pProperties) {
        super(pTier, pProperties.attributes(
                ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                                Item.BASE_ATTACK_DAMAGE_ID,
                                Config.CrucibleOfRileAttackDamage - 1.0, // 16.0 - 1.0 = 15.0 (Total: 16.0)
                                AttributeModifier.Operation.ADD_VALUE
                        ), EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(
                                Item.BASE_ATTACK_SPEED_ID,
                                Config.CrucibleOfRileAttackSpeed - 4.0, // 1.4 - 4.0 = -2.6 (Total: 1.4)
                                AttributeModifier.Operation.ADD_VALUE
                        ), EquipmentSlotGroup.MAINHAND)
                        .build()
        ));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (pLevel.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        if (pPlayer instanceof ServerPlayer sp) {
            int durationTicks = Config.CrucibleOfRileAggroDuration;
            LookAtTargetPayload payload = new LookAtTargetPayload(pPlayer.getUUID(), durationTicks);
            // En 1.21.1, se envía el payload directamente, sin envolverlo en ClientboundCustomPayloadPacket
            sp.connection.send(payload);
        }

        applyArmorBuff(pPlayer);
        stack.hurtAndBreak(1, pPlayer, pPlayer.getEquipmentSlotForItem(stack));
        return InteractionResultHolder.consume(stack);
    }

    private void applyArmorBuff(Player player) {
        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        AttributeInstance toughness = player.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (armor == null || toughness == null) return;

        double armorAmount = player.getArmorValue() * Config.CrucibleOfRileArmorBuffValue;
        // En 1.21.1 no existe getArmorToughness() directo en Player, se obtiene del atributo
        double toughnessAmount = toughness.getValue() * Config.CrucibleOfRileArmorBuffValue;

        AttributeModifier armorBoost = new AttributeModifier(ARMOR_BOOST_ID, armorAmount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        AttributeModifier toughnessBoost = new AttributeModifier(TOUGHNESS_BOOST_ID, toughnessAmount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        if (!armor.hasModifier(ARMOR_BOOST_ID)) armor.addTransientModifier(armorBoost);
        if (!toughness.hasModifier(TOUGHNESS_BOOST_ID)) toughness.addTransientModifier(toughnessBoost);

        int buffDuration = Config.CrucibleOfRileArmorBuffDuration;
        player.addEffect(new net.minecraft.world.effect.MobEffectInstance(ModEffects.CRUCIBLE_OF_RILE_ARMOR_BOOST, buffDuration, 0));
    }

    public void removeArmorBuff(Player player) {
        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        AttributeInstance toughness = player.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (armor != null) armor.removeModifier(ARMOR_BOOST_ID);
        if (toughness != null) toughness.removeModifier(TOUGHNESS_BOOST_ID);
    }

    // --- MÉTODOS ESTÁTICOS NECESARIOS PARA OTROS HANDLERS ---

    public static boolean isHeldMainHand(LivingEntity entity) {
        return entity.getMainHandItem().getItem() instanceof CrucibleOfRile;
    }

    public static void handleOnOwnerAttacked(LivingEntity attacker) {
        // Aquí va la lógica original de contraataque si la tenías.
        // Si no, déjalo vacío para que compile.
    }

    public static Set<String> getUniqueKills(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        ListTag list = tag.getList(TAG_UNIQUE_KILLS, 8); // 8 = StringTag
        Set<String> kills = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            kills.add(list.getString(i));
        }
        return kills;
    }

    public static void setUniqueKills(ItemStack stack, Set<String> kills) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        ListTag list = new ListTag();
        for (String kill : kills) {
            list.add(StringTag.valueOf(kill));
        }
        tag.put(TAG_UNIQUE_KILLS, list);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    // --- FIN MÉTODOS ESTÁTICOS ---

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}