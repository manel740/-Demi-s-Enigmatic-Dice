package net.jrdemiurge.enigmaticdice.network;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.event.ClientTimeHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TimeAccelStartPayload(int multiplier, int durationTicks) implements CustomPacketPayload {
    public static final Type<TimeAccelStartPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "time_accel"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TimeAccelStartPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, TimeAccelStartPayload::multiplier,
            ByteBufCodecs.VAR_INT, TimeAccelStartPayload::durationTicks,
            TimeAccelStartPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(TimeAccelStartPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> ClientTimeHandler.start(payload.multiplier(), payload.durationTicks()));
    }
}