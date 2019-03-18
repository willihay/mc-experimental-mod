package org.bensam.experimental.proxy;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.block.pedestal.RendererPedestal;
import org.bensam.experimental.block.pedestal.TileEntityPedestal;
import org.bensam.experimental.block.teleportbeacon.RendererTeleportBeacon;
import org.bensam.experimental.block.teleportbeacon.TileEntityTeleportBeacon;
import org.bensam.experimental.entity.ModEntities;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author WilliHay
 *
 */
public class ClientProxy implements IProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        ExperimentalMod.MOD_LOGGER.info("** ClientProxy pre-initialization event **");
        
        ModEntities.registerRenderer();
        ExperimentalMod.MOD_LOGGER.info("ModEntities renderers registered");
        
        registerTileEntityRenderers();
        ExperimentalMod.MOD_LOGGER.info("TileEntity renderers registered");
    }

    private void registerTileEntityRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPedestal.class, new RendererPedestal());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTeleportBeacon.class, new RendererTeleportBeacon());
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        ExperimentalMod.MOD_LOGGER.info("** ClientProxy initialization event **");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
    }

//    public String localize(String unlocalized, Object... args)
//    {
//        return I18n.format(unlocalized, args);
//    }
}
