package net.jrdemiurge.enigmaticdice.network;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.jrdemiurge.enigmaticdice.item.custom.GravityCore;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DoubleJumpPayload() implements CustomPacketPayload {
    public static final Type<DoubleJumpPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnigmaticDice.MOD_ID, "double_jump"));
    public static final StreamCodec<RegistryFriendlyByteBuf, DoubleJumpPayload> CODEC = StreamCodec.unit(new DoubleJumpPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(DoubleJumpPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                GravityCore.jump(player);
            }
        });
    }
}