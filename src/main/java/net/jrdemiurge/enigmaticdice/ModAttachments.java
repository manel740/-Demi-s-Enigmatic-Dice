package net.jrdemiurge.enigmaticdice;

import net.jrdemiurge.enigmaticdice.item.custom.souleater.SoulEaterData;
import net.jrdemiurge.enigmaticdice.item.custom.unequalexchange.UnequalExchangeData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, EnigmaticDice.MOD_ID);

    public static final Supplier<AttachmentType<SoulEaterData>> SOUL_EATER_DATA = ATTACHMENTS.register(
            "soul_eater_data",
            () -> AttachmentType.builder(SoulEaterData::new)
                    .serialize(SoulEaterData.CODEC)
                    .build()
    );

    public static final Supplier<AttachmentType<UnequalExchangeData>> UNEQUAL_EXCHANGE_DATA = ATTACHMENTS.register(
            "unequal_exchange_data",
            () -> AttachmentType.builder(UnequalExchangeData::new)
                    .serialize(UnequalExchangeData.CODEC)
                    .build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
    }
}