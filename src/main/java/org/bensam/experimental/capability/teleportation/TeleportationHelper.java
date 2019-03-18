package org.bensam.experimental.capability.teleportation;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.block.ModBlocks;
import org.bensam.experimental.block.teleportbeacon.BlockTeleportBeacon;
import org.bensam.experimental.block.teleportbeacon.TileEntityTeleportBeacon;
import org.bensam.experimental.network.PacketUpdateTeleportBeacon;
import org.bensam.experimental.util.ModUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * @author Will
 *
 */
public class TeleportationHelper
{

//    @Nullable
//    public static BlockPos findSafeTeleportPosAroundDestination(World world, BlockPos teleportDestination,
//                                                                @Nullable EnumFacing facingHint)
//    {
//        if (teleportDestination.equals(BlockPos.ORIGIN))
//            return null;
//        
//        BlockPos safePos = teleportDestination;
//        if (facingHint != null)
//        {
//            if (facingHint == EnumFacing.DOWN)
//            {
//                safePos = safePos.offset(facingHint, 2);
//            }
//            else
//            {
//                safePos = safePos.offset(facingHint);
//            }
//        }
//
//        IBlockState state = world.getBlockState(safePos);
//        IBlockState stateAbove = world.getBlockState(safePos.up());
//        if (!state.getMaterial().isSolid() && !stateAbove.getMaterial().isSolid())
//            return safePos;
//
//        // TODO: try other blocks adjacent to teleportDestination - can we use logic from BlockBed.getSafeExitLocation() or something similar?
//
//        // Can't find a safe spawn position around intended destination!
//        return null;
//    }

    @Nullable
    public static BlockPos findSafeTeleportPosNearBed(int dimension, BlockPos bedPos)
    {
        if (bedPos.equals(BlockPos.ORIGIN))
            return null;
        
        World world = ModUtil.getWorldServerForDimension(dimension);
        IBlockState blockState = world.getBlockState(bedPos);
        Block block = blockState.getBlock();
        
        if (block != Blocks.BED)
            return null; // not a bed
        
        return BlockBed.getSafeExitLocation(world, bedPos, 0);
    }

//    @Nullable
//    public static BlockPos findSafeTeleportPosToBed(World world, EntityPlayer player)
//    {
//        BlockPos bedPos = player.getBedLocation(world.provider.getDimension());
//        if (bedPos != null)
//            return EntityPlayer.getBedSpawnLocation(world, bedPos, false);
//
//        // Can't find bed spawn position!
//        return null;
//    }

    @Nullable
    public static BlockPos findTeleportBeacon(World world, UUID beaconUUID)
    {
        if (world == null)
            return null;
        
        List<TileEntity> tileEntityList = world.loadedTileEntityList;

        for (TileEntity te : tileEntityList)
        {
            if (te instanceof TileEntityTeleportBeacon
                    && ((TileEntityTeleportBeacon) te).getUniqueID().equals(beaconUUID))
            {
                return te.getPos();
            }
        }
        
        return null;
    }

    public static void remountRider(Entity rider, Entity entityRidden)
    {
        if (!rider.isRiding() 
                && rider.dimension == entityRidden.dimension 
                && (rider.getPosition().distanceSqToCenter(entityRidden.posX, entityRidden.posY, entityRidden.posZ) < 4.0D))
        {
            rider.startRiding(entityRidden, true);
            if (rider instanceof EntityPlayerMP)
            {
                // Send an explicit vehicle move packet to update ridden entity with latest position info.
                // (If not done, then the normal move packet can override the teleport in some conditions (e.g. when they're close to the teleport destination) and send both player and vehicle back to their pre-teleport position!) 
                ((EntityPlayerMP) rider).connection.netManager.sendPacket(new SPacketMoveVehicle(entityRidden));
            }
        }
    }

    public static Entity teleportOther(Entity entityToTeleport, TileEntity tileEntityInitiatingTeleport)
    {
        ITeleportationHandler teleportationHandler = tileEntityInitiatingTeleport.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);
        if (teleportationHandler != null)
        {
            TeleportDestination activeTeleportDestination = teleportationHandler.getActiveDestination();
            if (activeTeleportDestination != null)
            {
                if (teleportationHandler.validateDestination((Entity) null, activeTeleportDestination))
                {
                    return teleport(entityToTeleport, activeTeleportDestination);
                }
            }
        }
        
        return entityToTeleport;
    }
    
    public static Entity teleportOther(Entity entityToTeleport, Entity entityInitiatingTeleport)
    {
        ITeleportationHandler teleportationHandler = entityInitiatingTeleport.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);
        if (teleportationHandler != null)
        {
            TeleportDestination activeTeleportDestination = teleportationHandler.getActiveDestination();
            if (activeTeleportDestination != null)
            {
                if (teleportationHandler.validateDestination(entityInitiatingTeleport, activeTeleportDestination))
                {
                    return teleport(entityToTeleport, activeTeleportDestination);
                }
            }
        }
        
        return entityToTeleport;
    }
    
    public static Entity teleport(Entity entityToTeleport, TeleportDestination destination)
    {
        World currentWorld = entityToTeleport.world;
        int teleportDimension = destination.dimension;
        World teleportWorld = ModUtil.getWorldServerForDimension(teleportDimension);
        BlockPos safePos = null;
        float rotationYaw = entityToTeleport.rotationYaw;

        switch (destination.destinationType)
        {
        case SPAWNBED:
            safePos = findSafeTeleportPosNearBed(teleportDimension, destination.position);
            break;
        case BEACON:
            if (!teleportWorld.getBlockState(destination.position.up()).getMaterial().isSolid())
            {
                safePos = destination.position;
            }
            break;
        case BLOCKPOS:
            IBlockState state = teleportWorld.getBlockState(destination.position);
            Block block = state.getBlock();
            if (block.canSpawnInBlock())
            {
                safePos = destination.position;
            }
            break;
        default:
            break;
        }
        
        if (safePos == null)
            return entityToTeleport;
        
        return teleport(currentWorld, entityToTeleport, teleportDimension, safePos, rotationYaw);
    }
    
    public static Entity teleport(World currentWorld, Entity entityToTeleport, int teleportDimension,
                                BlockPos teleportPos, float playerRotationYaw)
    {
        if (currentWorld.isRemote) // running on client
            return entityToTeleport;

        int entityCurrentDimension = entityToTeleport.dimension;
        WorldServer teleportWorld = ModUtil.getWorldServerForDimension(teleportDimension);

        // Dismount teleporting entity or passengers riding this entity, if applicable.
        if (entityToTeleport.isRiding())
        {
            entityToTeleport.dismountRidingEntity();
        }
        if (entityToTeleport.isBeingRidden())
        {
            entityToTeleport.removePassengers();
        }

        BlockPos preTeleportPosition = entityToTeleport.getPosition();

        // Set entity facing direction (yaw - N/S/E/W).
        entityToTeleport.setPositionAndRotation(entityToTeleport.posX, entityToTeleport.posY, entityToTeleport.posZ, playerRotationYaw, entityToTeleport.rotationPitch);

        if (entityCurrentDimension != teleportDimension)
        {
            // Transfer teleporting entity to teleport position in different dimension.
            if (entityToTeleport instanceof EntityPlayerMP)
            {
                ExperimentalMod.MOD_LOGGER.info("Using CustomTeleporter to teleport " + entityToTeleport.getDisplayName().getFormattedText() + " to dimension " + teleportDimension);
                teleportWorld.getMinecraftServer().getPlayerList().transferPlayerToDimension(
                        (EntityPlayerMP) entityToTeleport, teleportDimension, new CustomTeleporter(teleportWorld, teleportPos));
            }
            else if (entityToTeleport instanceof EntityLivingBase)
            {
                ExperimentalMod.MOD_LOGGER.info("Using CustomTeleporter to teleport " + entityToTeleport.getDisplayName().getFormattedText() + " to dimension " + teleportDimension);
                entityToTeleport = entityToTeleport.changeDimension(teleportDimension, new CustomTeleporter(teleportWorld, teleportPos));
            }
        }
        else
        {
            // Teleport entity to destination.

            // Try attemptTeleport first because it has some extra, interesting render effects.
            // Note that the Y-coordinate is specified one block higher because of how the attemptTeleport function
            //   starts looking for safe teleport positions one block BELOW the specified Y-coordinate.
            if (entityToTeleport instanceof EntityLivingBase && ((EntityLivingBase) entityToTeleport)
                    .attemptTeleport(teleportPos.getX() + 0.5D, teleportPos.up().getY() + 0.25D, teleportPos.getZ() + 0.5D))
            {
                ExperimentalMod.MOD_LOGGER.info("attemptTeleport succeeded");
            }
            else
            {
                // If we can't do it the "pretty way", just force it! Hopefully they survive teh magiks. :P
                ExperimentalMod.MOD_LOGGER.info("Calling setPositionAndUpdate...");
                entityToTeleport.setPositionAndUpdate(teleportPos.getX() + 0.5D, teleportPos.getY() + 0.25D,
                        teleportPos.getZ() + 0.5D);
            }
        }

        // TODO: This doesn't seem to correct the lighting when traveling to the nether...
//        if (entityToTeleport instanceof EntityPlayerMP)
//        {
//            TileEntity te = teleportWorld.getTileEntity(teleportPos);
//            if (te instanceof TileEntityTeleportBeacon)
//            {
//                // Send block update to correct lighting in some scenarios (e.g. teleporting across dimensions).
//                ExperimentalMod.network.sendTo(new PacketUpdateTeleportBeacon(te.getPos(), true), (EntityPlayerMP) entityToTeleport);
//            }
//        }
        
        // Play teleport sound at the starting position and final position of the teleporting entity.
        currentWorld.playSound((EntityPlayer) null, preTeleportPosition, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
                SoundCategory.PLAYERS, 1.0F, 1.0F);
        teleportWorld.playSound((EntityPlayer) null, teleportPos, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
                SoundCategory.PLAYERS, 1.0F, 1.0F);
        
        return entityToTeleport;
    }
}
