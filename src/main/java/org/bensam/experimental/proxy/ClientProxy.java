/**
 * ClientProxy - client-specific code
 */
package org.bensam.experimental.proxy;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.block.pedestal.RendererPedestal;
import org.bensam.experimental.block.pedestal.TileEntityPedestal;
import org.bensam.experimental.entity.ModEntities;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author Will
 *
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        ExperimentalMod.logger.info("** ClientProxy initialization event **");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }

    @Override
    public String localize(String unlocalized, Object... args)
    {
        return I18n.format(unlocalized, args);
    }
    
    @Override
    public void registerItemRenderer(Item item, int meta, String id)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(ExperimentalMod.MODID + ":" + id, "inventory"));
    }

    @Override
    public void registerRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPedestal.class, new RendererPedestal());
        ModEntities.registerRenderer();
    }

}
