package net.jrdemiurge.enigmaticdice.item;

import net.jrdemiurge.enigmaticdice.EnigmaticDice;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> MOD_CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EnigmaticDice.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ENIGMATIC_DICE_TAB = MOD_CREATIVE_TABS.register("enigmaticdice_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.enigmaticdice"))
                    .icon(() -> new ItemStack(ModItems.ENIGMATIC_DIE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.ENIGMATIC_DIE.get());
                        output.accept(ModItems.UNEQUAL_EXCHANGE.get());
                        output.accept(ModItems.SOUL_EATER.get());
                        output.accept(ModItems.ANTIMATTER.get());
                        output.accept(ModItems.FOUR_LEAF_CLOVER.get());
                        output.accept(ModItems.GIANTS_RING.get());
                        output.accept(ModItems.MOON_SHARD.get());
                        output.accept(ModItems.GRAVITY_CORE.get());
                        output.accept(ModItems.MOON.get());
                        output.accept(ModItems.RING_OF_AGILITY.get());
                        output.accept(ModItems.DIVINE_SHIELD.get());
                        output.accept(ModItems.PERMAFROST.get());
                        //output.accept(ModItems.PHOENIX.get());
                        output.accept(ModItems.CRUCIBLE_OF_RILE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        MOD_CREATIVE_TABS.register(eventBus);
    }
}