package org.bensam.experimental;

import org.bensam.experimental.block.ModBlocks;
import org.bensam.experimental.entity.ModEntities;
import org.bensam.experimental.item.ModItems;
import org.bensam.experimental.potion.ModPotions;
import org.bensam.experimental.sound.ModSounds;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

/**
 * Signals to Forge that we want to listen to the main event bus, which allows mods to register/subscribe
 * handler methods to run when certain events occur. This class is concerned with common (i.e. non-sided) registry events.
 */
@Mod.EventBusSubscriber(modid = ExperimentalMod.MODID)
public final class CommonEventSubscriber
{

    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
    {
        ExperimentalMod.MOD_LOGGER.info("** Block RegistryEvent **");
        
        ModBlocks.register(event.getRegistry());
        ExperimentalMod.MOD_LOGGER.info("ModBlocks registered");
    }
    
    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event)
    {
        ExperimentalMod.MOD_LOGGER.info("** Item RegistryEvent **");
        
        ModItems.register(event.getRegistry());
        ExperimentalMod.MOD_LOGGER.info("ModItems registered");
        
        ModBlocks.registerItemBlocks(event.getRegistry());
        ExperimentalMod.MOD_LOGGER.info("ModBlocks ItemBlocks registered");
    }

    @SubscribeEvent
    public static void onRegisterEntities(RegistryEvent.Register<EntityEntry> event)
    {
        ExperimentalMod.MOD_LOGGER.info("** EntityEntry RegistryEvent **");
        
        ModEntities.register(event.getRegistry());
        ExperimentalMod.MOD_LOGGER.info("ModEntities registered");
    }
    
    @SubscribeEvent
    public static void onRegisterPotions(RegistryEvent.Register<Potion> event)
    {
        ExperimentalMod.MOD_LOGGER.info("** Potion RegistryEvent **");
        
        ModPotions.register(event.getRegistry());
        ExperimentalMod.MOD_LOGGER.info("ModPotions registered");
    }
    
    @SubscribeEvent
    public static void onRegisterSounds(RegistryEvent.Register<SoundEvent> event)
    {
        ExperimentalMod.MOD_LOGGER.info("** SoundEvent RegistryEvent **");
        
        ModSounds.register(event.getRegistry());
        ExperimentalMod.MOD_LOGGER.info("ModSounds registered");
    }
}
