package net.jrdemiurge.enigmaticdice.event;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.item.custom.UnequalExchange;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = EnigmaticDice.MOD_ID)
public class LivingHurtLowestHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity target && event.getSource().getEntity() instanceof Player attacker) {
            if (UnequalExchange.isHeldMainHand(attacker)) {
                // Tu lógica original aquí si la tenías
            }
        }
    }
}