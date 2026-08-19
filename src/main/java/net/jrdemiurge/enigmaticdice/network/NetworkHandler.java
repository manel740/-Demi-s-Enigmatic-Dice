package net.jrdemiurge.enigmaticdice.network;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = EnigmaticDice.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0");
        registrar.playToServer(DoubleJumpPayload.TYPE, DoubleJumpPayload.CODEC, DoubleJumpPayload::handle);
        registrar.playToServer(TimeAccelStartPayload.TYPE, TimeAccelStartPayload.CODEC, TimeAccelStartPayload::handle);
        registrar.playToServer(LookAtTargetPayload.TYPE, LookAtTargetPayload.CODEC, LookAtTargetPayload::handle);
    }
}