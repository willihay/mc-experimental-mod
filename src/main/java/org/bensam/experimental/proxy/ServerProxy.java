package org.bensam.experimental.proxy;

import org.bensam.experimental.ExperimentalMod;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author WilliHay
 *
 */
public class ServerProxy implements IProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        ExperimentalMod.MOD_LOGGER.info("** ServerProxy pre-initialization event **");
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        ExperimentalMod.MOD_LOGGER.info("** ServerProxy initialization event **");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
