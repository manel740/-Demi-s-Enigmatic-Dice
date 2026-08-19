package net.jrdemiurge.enigmaticdice.item.custom.enigmaticdie;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GiveRandomPotionEvent extends RandomEvent {
    private final boolean onlyVanillaEffects;
    private final Random random = new Random();

    public GiveRandomPotionEvent(int rarity, boolean onlyVanillaEffects) {
        this.rarity = rarity;
        this.onlyVanillaEffects = onlyVanillaEffects;
    }

    @Override
    public boolean execute(Level pLevel, Player pPlayer, boolean guaranteed) {
        if (!guaranteed) {
            if (!rollChance(pLevel, pPlayer, rarity)) return false;
        }

        ItemStack potion = new ItemStack(Items.SPLASH_POTION);
        int effectCount = 3 + random.nextInt(4);

        List<Holder<MobEffect>> availableEffects = new ArrayList<>();

        // Corrección del bucle para 1.21.1
        for (Holder<MobEffect> holder : BuiltInRegistries.MOB_EFFECT.holders().toList()) {
            if (onlyVanillaEffects) {
                ResourceLocation key = BuiltInRegistries.MOB_EFFECT.getKey(holder.value());
                if (key != null && "minecraft".equals(key.getNamespace())) {
                    availableEffects.add(holder);
                }
            } else {
                availableEffects.add(holder);
            }
        }

        Collections.shuffle(availableEffects, random);
        List<MobEffectInstance> selectedEffects = new ArrayList<>();

        for (int i = 0; i < Math.min(effectCount, availableEffects.size()); i++) {
            Holder<MobEffect> holder = availableEffects.get(i);
            MobEffect effect = holder.value();

            int amplifier;
            int duration;

            if (effect.isInstantenous()) { // Typo de Mojang en 1.21.1
                duration = 1;
                amplifier = random.nextInt(5);
            } else {
                int seconds = 10 + random.nextInt((15 * 60) - 10);
                duration = seconds * 20;
                amplifier = random.nextInt(3);
            }

            selectedEffects.add(new MobEffectInstance(holder, duration, amplifier));
        }

        potion.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.empty(), selectedEffects));

        CompoundTag tag = potion.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        ListTag enchantments = new ListTag();
        CompoundTag fakeEnchant = new CompoundTag();
        fakeEnchant.putString("id", "minecraft:unbreaking");
        fakeEnchant.putShort("lvl", (short) 1);
        enchantments.add(fakeEnchant);
        tag.put("Enchantments", enchantments);
        potion.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        Component customName = onlyVanillaEffects
                ? Component.translatable("item.enigmaticdice.vanilla_random_splash_potion").withStyle(style -> style.withItalic(false))
                : Component.translatable("item.enigmaticdice.modded_random_splash_potion").withStyle(style -> style.withItalic(false));

        potion.set(DataComponents.CUSTOM_NAME, customName);

        ItemEntity entity = new ItemEntity(pLevel, pPlayer.getX(), pPlayer.getY() + 1, pPlayer.getZ(), potion);
        pLevel.addFreshEntity(entity);

        MutableComponent message = onlyVanillaEffects
                ? Component.translatable("enigmaticdice.event.vanilla_random_potion")
                : Component.translatable("enigmaticdice.event.modded_random_potion");

        pPlayer.displayClientMessage(message, false);
        return true;
    }
}