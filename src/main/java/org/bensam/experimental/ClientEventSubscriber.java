package org.bensam.experimental;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;

import org.bensam.experimental.block.ModBlocks;
import org.bensam.experimental.item.ModItems;
import org.bensam.experimental.potion.ModPotions;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;

/**
 * @author WilliHay
 * 
 * Signals to Forge that we want to listen to the main event bus, which allows mods to register/subscribe
 * handler methods to run when certain events occur. This class is concerned with client-side registry events.
 */
@Mod.EventBusSubscriber(modid = ExperimentalMod.MODID, value = CLIENT)
public final class ClientEventSubscriber
{

    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent event)
    {
        ExperimentalMod.MOD_LOGGER.info("** ModelRegistryEvent **");
        
        ModBlocks.registerItemBlockModels();
        ExperimentalMod.MOD_LOGGER.info("ModBlocks ItemBlock models registered");
        
        ModItems.registerItemModels();
        ExperimentalMod.MOD_LOGGER.info("ModItems Item models registered");
    }

    @SubscribeEvent
    public static void onRegisterItemColorHandlers(ColorHandlerEvent.Item event)
    {
        ExperimentalMod.MOD_LOGGER.info("** ColorHandlerEvent **");

        event.getItemColors().registerItemColorHandler(new IItemColor()
        {
            public int colorMultiplier(ItemStack stack, int tintIndex)
            {
                return tintIndex > 0 ? -1 : ModPotions.TELEPORTATION_POTION.getLiquidColor();
            }
        }, ModItems.TELEPORTATION_SPLASH_POTION, ModItems.TELEPORTATION_TIPPED_ARROW);
        
        ExperimentalMod.MOD_LOGGER.info("Teleportation Potion color registered");
    }
}
