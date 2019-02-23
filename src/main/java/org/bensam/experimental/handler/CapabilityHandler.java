package org.bensam.experimental.handler;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.capability.teleportation.TeleportationHandlerCapabilityProvider;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ExperimentalMod.MODID)
public class CapabilityHandler
{
    public static final ResourceLocation TELEPORTATION_CAPABILITY_ID = new ResourceLocation(ExperimentalMod.MODID, "teleportation_capability");
    
    @SubscribeEvent
    public static void attachCapabilityToEntity(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        {
            event.addCapability(TELEPORTATION_CAPABILITY_ID, new TeleportationHandlerCapabilityProvider());
        }
    }
    
    @SubscribeEvent
    public static void attachCapabilityToTileEntity(AttachCapabilitiesEvent<TileEntity> event)
    {
        if (event.getObject() instanceof TileEntityDispenser)
        {
            event.addCapability(TELEPORTATION_CAPABILITY_ID, new TeleportationHandlerCapabilityProvider());
        }
    }
}
