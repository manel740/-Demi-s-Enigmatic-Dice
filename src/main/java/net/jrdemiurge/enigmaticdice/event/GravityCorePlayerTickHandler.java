package net.jrdemiurge.enigmaticdice.event;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.item.custom.GravityCore;
import net.jrdemiurge.enigmaticdice.network.DoubleJumpPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.WeakHashMap;

@EventBusSubscriber(modid = EnigmaticDice.MOD_ID, value = Dist.CLIENT)
public class GravityCorePlayerTickHandler {

    private static final int DOUBLE_JUMP_THRESHOLD_TICKS = 6;

    private static final WeakHashMap<Player, Long> lastJumpPressTick = new WeakHashMap<>();
    private static final WeakHashMap<Player, Boolean> wasJumping = new WeakHashMap<>();
    private static final WeakHashMap<Player, Boolean> lastTickIsJumping = new WeakHashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof LocalPlayer player)) return;

        if (!GravityCore.isWearingGravityCore(player)) return;
        if (player.isCreative() || player.isSpectator()) return;

        if (player.onGround()) {
            wasJumping.put(player, false);
        }

        if (!player.input.jumping) {
            lastTickIsJumping.put(player, false);
            return;
        }

        if (player.input.jumping
                && !lastTickIsJumping.getOrDefault(player, false)
                && !wasJumping.getOrDefault(player, false)) {

            long lastTick = lastJumpPressTick.getOrDefault(player, -100L);
            long currentTick = player.level().getGameTime();
            lastJumpPressTick.put(player, currentTick);
            lastTickIsJumping.put(player, true);

            if (currentTick - lastTick < DOUBLE_JUMP_THRESHOLD_TICKS) {
                // En 1.21.1, el cliente envía el payload directamente a través de la conexión
                player.connection.send(new DoubleJumpPayload());
                wasJumping.put(player, true);
                lastJumpPressTick.put(player, -100L);
            }
        }
    }
}