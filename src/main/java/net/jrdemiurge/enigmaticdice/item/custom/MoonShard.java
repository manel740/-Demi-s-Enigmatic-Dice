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
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.WeakHashMap;

public class MoonShard extends Item {
    private static final ResourceLocation GRAVITY_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "moon_shard_gravity");
    private static final WeakHashMap<Player, Boolean> gravityDisable = new WeakHashMap<>();

    public MoonShard(Properties pProperties) { super(pProperties); }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof Player player) {
            if (pSlotId >= 0 && pSlotId <= 8) {
                boolean isSneaking = player.isShiftKeyDown();
                AttributeInstance gravityAttr = player.getAttribute(Attributes.GRAVITY);

                if (gravityAttr != null) {
                    AttributeModifier existingModifier = gravityAttr.getModifier(GRAVITY_MODIFIER_ID);
                    if (player.getMainHandItem().is(ModItems.MOON.get()) || player.getOffhandItem().is(ModItems.MOON.get())) {
                        if (existingModifier != null) gravityAttr.removeModifier(GRAVITY_MODIFIER_ID);
                        return;
                    }
                    if (isWearingGravityCore(player)) {
                        boolean gravityDisabled = gravityDisable.getOrDefault(player, false);
                        if (isSneaking && !player.onGround() && !gravityDisabled) {
                            gravityDisable.put(player, true);
                            if (existingModifier != null) gravityAttr.removeModifier(GRAVITY_MODIFIER_ID);
                        }
                        if (player.onGround()) {
                            gravityDisable.put(player, false);
                            if (existingModifier == null) {
                                gravityAttr.addTransientModifier(new AttributeModifier(GRAVITY_MODIFIER_ID, Config.MoonShardGravityReduction, AttributeModifier.Operation.ADD_VALUE));
                            }
                        }
                    } else {
                        if (!isSneaking) {
                            if (existingModifier == null) {
                                gravityAttr.addTransientModifier(new AttributeModifier(GRAVITY_MODIFIER_ID, Config.MoonShardGravityReduction, AttributeModifier.Operation.ADD_VALUE));
                            }
                        } else {
                            if (existingModifier != null) gravityAttr.removeModifier(GRAVITY_MODIFIER_ID);
                        }
                    }
                }
                Scheduler.schedule(() -> {
                    boolean found = false;
                    for (int i = 0; i <= 8; i++) {
                        if (player.getInventory().getItem(i).is(ModItems.MOON_SHARD.get())) { found = true; break; }
                    }
                    AttributeInstance laterAttr = player.getAttribute(Attributes.GRAVITY);
                    if (!found && laterAttr != null) {
                        AttributeModifier modifier = laterAttr.getModifier(GRAVITY_MODIFIER_ID);
                        if (modifier != null) laterAttr.removeModifier(GRAVITY_MODIFIER_ID);
                    }
                }, 10);
            }
        }
    }

    private static boolean isWearingGravityCore(Player player) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.GRAVITY_CORE.get(), player).isPresent();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.while_hotbar"));
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.moon_shard_1"));
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.moon_shard_2"));
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.moon_shard_3"));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.enigmaticdice.holdShift"));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}