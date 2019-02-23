package org.bensam.experimental.handler;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.item.ModItems;
import org.bensam.experimental.potion.PotionTeleportation;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = ExperimentalMod.MODID)
public class ColorHandler
{

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerItemColorHandlers(ColorHandlerEvent.Item event)
    {
        event.getItemColors().registerItemColorHandler(new IItemColor()
        {
            public int colorMultiplier(ItemStack stack, int tintIndex)
            {
                return tintIndex > 0 ? -1 : new PotionTeleportation().getLiquidColor();
            }
        }, ModItems.teleportationSplashPotion, ModItems.teleportationTippedArrow);
    }
}
