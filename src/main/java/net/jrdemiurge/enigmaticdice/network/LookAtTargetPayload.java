package net.jrdemiurge.enigmaticdice.network;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.item.custom.crucibleofrile.ClientLookController;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import java.util.UUID;

public record LookAtTargetPayload(UUID targetUuid, int durationTicks) implements CustomPacketPayload {
    public static final Type<LookAtTargetPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "look_at_target"));
    public static final StreamCodec<RegistryFriendlyByteBuf, LookAtTargetPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString), LookAtTargetPayload::targetUuid,
            ByteBufCodecs.VAR_INT, LookAtTargetPayload::durationTicks,
            LookAtTargetPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(LookAtTargetPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> ClientLookController.startOrExtend(payload.targetUuid(), payload.durationTicks()));
    }
}