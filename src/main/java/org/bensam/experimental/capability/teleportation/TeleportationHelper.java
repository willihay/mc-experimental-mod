package org.bensam.experimental.capability.teleportation;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.ModHelper;
import org.bensam.experimental.block.ModBlocks;
import org.bensam.experimental.block.teleportbeacon.TileEntityTeleportBeacon;

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

    @Nullable
    public static BlockPos findSafeTeleportPosAroundDestination(World world, BlockPos teleportDestination,
                                                                @Nullable EnumFacing facingHint)
    {
        if (teleportDestination.equals(BlockPos.ORIGIN))
            return null;
        
        BlockPos safePos = teleportDestination;
        if (facingHint != null)
        {
            if (facingHint == EnumFacing.DOWN)
            {
                safePos = safePos.offset(facingHint, 2);
            }
            else
            {
                safePos = safePos.offset(facingHint);
            }
        }

        IBlockState state = world.getBlockState(safePos);
        IBlockState stateAbove = world.getBlockState(safePos.up());
        if (!state.getMaterial().isSolid() && !stateAbove.getMaterial().isSolid())
            return safePos;

        // TODO: try other blocks adjacent to teleportDestination - can we use logic from BlockBed.getSafeExitLocation() or something similar?

        // Can't find a safe spawn position around intended destination!
        return null;
    }

    @Nullable
    public static BlockPos findSafeTeleportPosNearBed(int dimension, BlockPos bedPos)
    {
        if (bedPos.equals(BlockPos.ORIGIN))
            return null;
        
        World world = ModHelper.getWorldServerForDimension(dimension);
        IBlockState blockState = world.getBlockState(bedPos);
        Block block = blockState.getBlock();
        
        if (block != Blocks.BED)
            return null; // not a bed
        
        // TODO: replace the BlockBed method with an improved method that returns the first safe location around a position
        return BlockBed.getSafeExitLocation(world, bedPos, 0);
    }

    @Nullable
    public static BlockPos findSafeTeleportPosToBed(World world, EntityPlayer player)
    {
        BlockPos bedPos = player.getBedLocation(world.provider.getDimension());
        if (bedPos != null)
            return EntityPlayer.getBedSpawnLocation(world, bedPos, false);

        // Can't find bed spawn position!
        return null;
    }

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

    public static Entity teleportOther(Entity entityToTeleport, TileEntity tileEntityInitiatingTeleport, boolean rotateToFaceIfBeacon)
    {
        ITeleportationHandler teleportationHandler = tileEntityInitiatingTeleport.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);
        if (teleportationHandler != null)
        {
            TeleportDestination activeTeleportDestination = teleportationHandler.getActiveDestination();
            if (activeTeleportDestination != null)
            {
                if (teleportationHandler.validateDestination((Entity) null, activeTeleportDestination))
                {
                    return teleport(entityToTeleport, activeTeleportDestination, rotateToFaceIfBeacon);
                }
            }
        }
        
        return entityToTeleport;
    }
    
    public static Entity teleportOther(Entity entityToTeleport, Entity entityInitiatingTeleport, boolean rotateToFaceIfBeacon)
    {
        ITeleportationHandler teleportationHandler = entityInitiatingTeleport.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);
        if (teleportationHandler != null)
        {
            TeleportDestination activeTeleportDestination = teleportationHandler.getActiveDestination();
            if (activeTeleportDestination != null)
            {
                if (teleportationHandler.validateDestination(entityInitiatingTeleport, activeTeleportDestination))
                {
                    return teleport(entityToTeleport, activeTeleportDestination, rotateToFaceIfBeacon);
                }
            }
        }
        
        return entityToTeleport;
    }
    
    public static Entity teleport(Entity entityToTeleport, TeleportDestination destination, boolean rotateToFaceIfBeacon)
    {
        World currentWorld = entityToTeleport.world;
        int teleportDimension = destination.dimension;
        World teleportWorld = ModHelper.getWorldServerForDimension(teleportDimension);
        BlockPos safePos = null;
        float rotationYaw = entityToTeleport.rotationYaw;

        switch (destination.destinationType)
        {
        case SPAWNBED:
            // TODO: Instead of doing safe checks here, should we just run validate on the destination here? or assume that validation has been run?
            safePos = findSafeTeleportPosNearBed(teleportDimension, destination.position);
            break;
        case BEACON:
            safePos = findSafeTeleportPosAroundDestination(teleportWorld, destination.position, destination.preferredTeleportFace);
            if (rotateToFaceIfBeacon)
            {
                rotationYaw = ModHelper.getRotationFromFacing(destination.preferredTeleportFace.getOpposite());
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
        WorldServer teleportWorld = ModHelper.getWorldServerForDimension(teleportDimension);

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
                ExperimentalMod.logger.info("Using CustomTeleporter to teleport " + entityToTeleport.getDisplayName().getFormattedText() + " to dimension " + teleportDimension);
                teleportWorld.getMinecraftServer().getPlayerList().transferPlayerToDimension(
                        (EntityPlayerMP) entityToTeleport, teleportDimension, new CustomTeleporter(teleportWorld, teleportPos));
            }
            else if (entityToTeleport instanceof EntityLivingBase)
            {
                ExperimentalMod.logger.info("Using CustomTeleporter to teleport " + entityToTeleport.getDisplayName().getFormattedText() + " to dimension " + teleportDimension);
                entityToTeleport = entityToTeleport.changeDimension(teleportDimension, new CustomTeleporter(teleportWorld, teleportPos));
            }
        }
        else
        {
            // Teleport entity to destination.
            if (entityToTeleport instanceof EntityLivingBase && ((EntityLivingBase) entityToTeleport)
                    .attemptTeleport(teleportPos.getX() + 0.5D, teleportPos.getY(), teleportPos.getZ() + 0.5D))
            {
                ExperimentalMod.logger.info("attemptTeleport succeeded");
            }
            else
            {
                // If we can't do it the "pretty way", just force it! Hopefully they survive teh magiks. :P
                ExperimentalMod.logger.info("Calling setPositionAndUpdate...");
                entityToTeleport.setPositionAndUpdate(teleportPos.getX() + 0.5D, teleportPos.getY(),
                        teleportPos.getZ() + 0.5D);
            }
        }

        // Play teleport sound at the starting position and final position of the teleporting entity.
        currentWorld.playSound((EntityPlayer) null, preTeleportPosition, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
                SoundCategory.PLAYERS, 1.0F, 1.0F);
        teleportWorld.playSound((EntityPlayer) null, teleportPos, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT,
                SoundCategory.PLAYERS, 1.0F, 1.0F);
        
        return entityToTeleport;
    }
}
