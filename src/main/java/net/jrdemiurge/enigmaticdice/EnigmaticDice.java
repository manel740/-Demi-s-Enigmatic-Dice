package net.jrdemiurge.enigmaticdice;

import com.mojang.logging.LogUtils;
import net.jrdemiurge.enigmaticdice.attribute.ModAttributes;
import net.jrdemiurge.enigmaticdice.commands.EnigmaticDiceCommand;
import net.jrdemiurge.enigmaticdice.commands.EnigmaticDiceGetLuckCommand;
import net.jrdemiurge.enigmaticdice.commands.EnigmaticDiceSimulateCommand;
import net.jrdemiurge.enigmaticdice.effect.ModEffects;
import net.jrdemiurge.enigmaticdice.entity.ModEntities;
import net.jrdemiurge.enigmaticdice.entity.client.DragonclawHookRender;
import net.jrdemiurge.enigmaticdice.event.BlockBreakHandler;
import net.jrdemiurge.enigmaticdice.event.LootEventHandler;
import net.jrdemiurge.enigmaticdice.event.MobDropHandler;
import net.jrdemiurge.enigmaticdice.item.ModCreativeTabs;
import net.jrdemiurge.enigmaticdice.item.ModItems;
import net.jrdemiurge.enigmaticdice.item.custom.Antimatter;
import net.jrdemiurge.enigmaticdice.item.custom.enigmaticdie.RandomEventManager;
import net.jrdemiurge.enigmaticdice.network.NetworkHandler;
import net.jrdemiurge.enigmaticdice.scheduler.Scheduler;
import net.jrdemiurge.enigmaticdice.sound.ModSounds;
import net.jrdemiurge.enigmaticdice.stat.ModStats;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

@Mod(EnigmaticDice.MOD_ID)
public class EnigmaticDice {
    public static final String MOD_ID = "enigmaticdice";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static RandomEventManager eventManager;

    public EnigmaticDice(IEventBus modEventBus, net.neoforged.fml.ModContainer modContainer) {
        ModCreativeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEffects.register(modEventBus);
        ModEntities.register(modEventBus);
        ModAttributes.register(modEventBus);
        ModStats.register(modEventBus);

        ModAttachments.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(Antimatter::init);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.DRAGONCLAW_HOOK.get(), DragonclawHookRender::new);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(EnigmaticDiceCommand.create());
        event.getDispatcher().register(EnigmaticDiceSimulateCommand.create());
        event.getDispatcher().register(EnigmaticDiceGetLuckCommand.create());
    }
}