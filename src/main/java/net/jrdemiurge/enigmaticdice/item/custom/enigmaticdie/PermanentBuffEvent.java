package net.jrdemiurge.enigmaticdice.item.custom.enigmaticdie;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Map;

public class PermanentBuffEvent extends RandomEvent {

    private static final Map<net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect>, String> EFFECT_MESSAGES = Map.ofEntries(
            Map.entry(MobEffects.DAMAGE_BOOST, "enigmaticdice.effect.damage_boost"),
            Map.entry(MobEffects.MOVEMENT_SPEED, "enigmaticdice.effect.movement_speed"),
            Map.entry(MobEffects.DIG_SPEED, "enigmaticdice.effect.dig_speed"),
            Map.entry(MobEffects.HEALTH_BOOST, "enigmaticdice.effect.health_boost"),
            Map.entry(MobEffects.NIGHT_VISION, "enigmaticdice.effect.night_vision"),
            Map.entry(MobEffects.INVISIBILITY, "enigmaticdice.effect.invisibility"),
            Map.entry(MobEffects.FIRE_RESISTANCE, "enigmaticdice.effect.fire_resistance"),
            Map.entry(MobEffects.REGENERATION, "enigmaticdice.effect.regeneration")
    );

    private static final int MAX_LEVEL = 2;
    private static final int MAX_LEVEL_SINGLE = 1;

    public PermanentBuffEvent(int rarity) {
        this.rarity = rarity;
    }

    @Override
    public boolean execute(Level pLevel, Player pPlayer, boolean guaranteed) {
        if (!guaranteed) {
            if (!rollChance(pLevel, pPlayer, rarity)) return false;
        }

        Holder<MobEffect>[] effects = EFFECT_MESSAGES.keySet().toArray(new Holder[0]);

        for (int i = 0; i < effects.length; i++) {
            Holder<MobEffect> effectHolder = effects[pLevel.getRandom().nextInt(effects.length)];
            MobEffect effect = effectHolder.value();

            MobEffectInstance existingEffect = pPlayer.getEffect(effectHolder);
            int maxLevel = (effect == MobEffects.NIGHT_VISION || effect == MobEffects.FIRE_RESISTANCE || effect == MobEffects.INVISIBILITY) ? MAX_LEVEL_SINGLE : MAX_LEVEL;

            if (existingEffect == null) {
                pPlayer.addEffect(new MobEffectInstance(effectHolder, MobEffectInstance.INFINITE_DURATION, 0, false, false));
                pPlayer.sendSystemMessage(Component.translatable(EFFECT_MESSAGES.get(effectHolder)));
                return true;
            } else if (existingEffect.getAmplifier() < maxLevel - 1) {
                pPlayer.addEffect(new MobEffectInstance(effectHolder, MobEffectInstance.INFINITE_DURATION, existingEffect.getAmplifier() + 1, false, false));
                pPlayer.sendSystemMessage(Component.translatable(EFFECT_MESSAGES.get(effectHolder)));
                return true;
            }
        }
        return false;
    }
}