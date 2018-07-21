/**
 * CommonProxy - code common to both client and server sides
 *     Don't register things like textures server-side because the server doesn't render anything.
 */
package org.bensam.experimental.proxy;

import org.apache.logging.log4j.Logger;

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
    protected static Logger logger;
    
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        
        // often useful: event.getModConfigurationDirectory();
    }
    
    public void init(FMLInitializationEvent event)
    {
        logger.info("** CommonProxy initialization event **");
    }
    
    public void postInit(FMLPostInitializationEvent event)
    {}

    public String localize(String unlocalized, Object... args)
    {
        return I18n.translateToLocalFormatted(unlocalized, args);
    }
    
    public void registerItemRenderer(Item item, int meta, String id)
    {}
    
    public void registerRenderers()
    {}
}
