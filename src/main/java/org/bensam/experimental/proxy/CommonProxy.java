/**
 * CommonProxy - code common to both client and server sides
 * Don't register things like textures server-side because the server doesn't render anything.
 */
package org.bensam.experimental.proxy;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.ModConfig;
import org.bensam.experimental.entity.ModEntities;

import net.minecraft.item.Item;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author Will
 *
 */
public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        ModConfig.initConfig(event.getModConfigurationDirectory());
        ModConfig.readConfig();
        // TODO: change copper sword attack damage based on config value

        ModEntities.preInit();
    }

    public void init(FMLInitializationEvent event)
    {
        ExperimentalMod.logger.info("** CommonProxy initialization event **");
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        if (ModConfig.config.hasChanged())
        {
            ModConfig.config.save();
        }
    }

//    public String localize(String unlocalized, Object... args)
//    {
//        return I18n.translateToLocalFormatted(unlocalized, args);
//    }

    public void registerItemRenderer(Item item, int meta, String id)
    {}

    public void registerRenderers()
    {}
}
