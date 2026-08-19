package net.jrdemiurge.enigmaticdice.effect;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.effect.custom.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, EnigmaticDice.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> SOUL_EATER_CHARGED_HIT = MOB_EFFECTS.register("soul_eater_charged_hit",
            () -> new SoulEaterChargedHit(MobEffectCategory.BENEFICIAL, 0x00FF00));
    public static final DeferredHolder<MobEffect, MobEffect> SOUL_EATER_HEALTH_BOOST = MOB_EFFECTS.register("soul_eater_health_boost",
            () -> new SoulEaterHealthBoost(MobEffectCategory.BENEFICIAL, 0x00FF00));
    public static final DeferredHolder<MobEffect, MobEffect> UNEQUAL_EXCHANGE_DEBUFFS = MOB_EFFECTS.register("unequal_exchange_debuffs",
            () -> new UnequalExchangeDebuffs(MobEffectCategory.HARMFUL, 0xFF0000));
    public static final DeferredHolder<MobEffect, MobEffect> FROST_HIT = MOB_EFFECTS.register("frost_hit",
            () -> new FrostHit(MobEffectCategory.HARMFUL, 0x00FFFF));
    public static final DeferredHolder<MobEffect, MobEffect> FROST_AURA = MOB_EFFECTS.register("frost_aura",
            () -> new FrostAura(MobEffectCategory.HARMFUL, 0x00FFFF));
    public static final DeferredHolder<MobEffect, MobEffect> CRUCIBLE_OF_RILE_ARMOR_BOOST = MOB_EFFECTS.register("crucible_of_rile_armor_boost",
            () -> new CrucibleOfRileArmorBoost(MobEffectCategory.BENEFICIAL, 0xFFFF00));
    public static final DeferredHolder<MobEffect, MobEffect> DIVINE_SHIELD_INVULNERABILITY = MOB_EFFECTS.register("divine_shield_invulnerability",
            () -> new DivineShieldInvulnerability(MobEffectCategory.BENEFICIAL, 0xFFFFFF));
    public static final DeferredHolder<MobEffect, MobEffect> DAY_ON_THE_MOON = MOB_EFFECTS.register("day_on_the_moon",
            () -> new DayOnTheMoon(MobEffectCategory.NEUTRAL, 0xAAAAAA));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}