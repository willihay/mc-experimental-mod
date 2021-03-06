package org.bensam.experimental.handler;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.capability.teleportation.ITeleportationHandler;
import org.bensam.experimental.capability.teleportation.TeleportationHandlerCapabilityProvider;
import org.bensam.experimental.capability.teleportation.TeleportationHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@Mod.EventBusSubscriber(modid = ExperimentalMod.MODID)
public class PlayerEventHandler
{

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event)
    {
        if (event.isWasDeath()) // true when respawning after death
        {
            EntityPlayer oldPlayer = event.getOriginal();
            ITeleportationHandler oldTeleportationHandler = oldPlayer.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);
            if (oldTeleportationHandler == null)
                return;

            EntityPlayer clonePlayer = event.getEntityPlayer();
            TeleportationHandler cloneTeleportationHandler = (TeleportationHandler) (clonePlayer.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null));
            
            // Copy the TeleportationHandler data from the old player to the cloned player object.
            cloneTeleportationHandler.deserializeNBT(((TeleportationHandler) oldTeleportationHandler).serializeNBT());
        }
    }
    
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;

        // Validate the data in this player's TeleportationHandler.
        ITeleportationHandler teleportationHandler = player.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);
        if (teleportationHandler == null)
            return;
        
        teleportationHandler.updateSpawnBed(player, 0); // make sure there is a SpawnBed destination in the Overworld and update its position
        teleportationHandler.validateAllDestinations(player);
    }
    
    @SubscribeEvent
    public static void onPlayerSetSpawn(PlayerSetSpawnEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        
        if (player.world.isRemote)
            return; // ignore what the client thinks, we only trust the server on this
        
        int dimension = player.dimension;
        BlockPos newSpawnPos = event.getNewSpawn();
        boolean isForced = event.isForced();
        ExperimentalMod.MOD_LOGGER.info("New spawn pos for " + player.getDisplayNameString() + ": " + newSpawnPos + "; isForced = " + isForced);
        
        // Ignore a spawn getting set in a non-Overworld dimension if it is forced. Those do not come from awaking from a bed.
        // If the spawn is being set in the Overworld, however, that's a TeleportDestination we always track, so that can be updated.
        if (dimension != 0 && isForced)
            return;
        
        // Get the player's TeleportationHandler and make sure they have a SpawnBed TeleportDestination with this new spawn position.
        ITeleportationHandler teleportationHandler = player.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);
        if (teleportationHandler == null)
            return;
        
        if (dimension == 0 && isForced)
        {
            // We do not allow teleportation to non-bed positions in the Overworld. 
            teleportationHandler.updateSpawnBed(BlockPos.ORIGIN, dimension);
        }
        else
        {
            teleportationHandler.updateSpawnBed(newSpawnPos, dimension);
        }
    }
}
