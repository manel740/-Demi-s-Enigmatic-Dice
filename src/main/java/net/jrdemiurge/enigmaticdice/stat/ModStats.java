package net.jrdemiurge.enigmaticdice.stat;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStats {
    // En 1.21.1, CUSTOM_STAT usa ResourceLocation como tipo de valor
    public static final DeferredRegister<ResourceLocation> CUSTOM_STATS =
            DeferredRegister.create(Registries.CUSTOM_STAT, EnigmaticDice.MOD_ID);

    // Registra tus estadísticas personalizadas aquí
    public static final DeferredHolder<ResourceLocation, ResourceLocation> OBTAINED_DICE_FROM_BLOCK =
            CUSTOM_STATS.register("obtained_dice_from_block", () -> ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "obtained_dice_from_block"));

    public static final DeferredHolder<ResourceLocation, ResourceLocation> OBTAINED_DICE_FROM_MOB =
            CUSTOM_STATS.register("obtained_dice_from_mob", () -> ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "obtained_dice_from_mob"));

    // Si tienes más estadísticas, añádelas aquí siguiendo el mismo patrón

    public static void register(IEventBus eventBus) {
        CUSTOM_STATS.register(eventBus);
    }
}