package org.bensam.experimental.block.teleportbeacon;

import java.util.UUID;

import javax.annotation.Nullable;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.block.BlockTileEntity;
import org.bensam.experimental.capability.teleportation.ITeleportationHandler;
import org.bensam.experimental.capability.teleportation.TeleportationHandlerCapabilityProvider;
import org.bensam.experimental.item.ModItems;
import org.bensam.experimental.network.PacketUpdateTeleportBeacon;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTeleportBeacon extends BlockTileEntity<TileEntityTeleportBeacon>
{

    public BlockTeleportBeacon()
    {
        super(Material.ROCK, "teleport_beacon");
        setHardness(5.0F); // enchantment table = 5.0F
        setResistance(2000.F); // enchantment table = 2000.F
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public Class<TileEntityTeleportBeacon> getTileEntityClass()
    {
        return TileEntityTeleportBeacon.class;
    }

    @Nullable
    @Override
    public TileEntityTeleportBeacon createTileEntity(World world, IBlockState state)
    {
        return new TileEntityTeleportBeacon();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote) // running on server
        {
            TileEntityTeleportBeacon te = getTileEntity(world, pos);
            UUID uuid = te.getUniqueID();
            String name = te.getBeaconName();

            if (uuid == null || uuid.equals(new UUID(0, 0)) || name == null || name.isEmpty())
            {
                ExperimentalMod.logger.warn("Something went wrong! Teleport Beacon block activated with invalid UUID or name fields. Setting to defaults...");
                te.setDefaultUUID();
                te.setBeaconName(null);
                
                uuid = te.getUniqueID();
                name = te.getBeaconName();
            }
            
            // Send the name of the beacon to the player if they're not using a teleport wand.
            if (player.getHeldItem(hand).getItem() != ModItems.teleportationWand)
            {
                player.sendMessage(new TextComponentString("Teleport: " + TextFormatting.DARK_GREEN + name));
            }
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (!world.isRemote) // running on server
        {
            TileEntityTeleportBeacon te = getTileEntity(world, pos);
            if (stack.hasDisplayName())
            {
                // Make sure beacon name is updated with any changes in the item stack (e.g. was renamed in anvil).
                te.setBeaconName(stack.getDisplayName());
            }

            String name = te.getBeaconName();
            UUID uuid = te.getUniqueID();
            if (uuid == null || uuid.equals(new UUID(0, 0)))
            {
                te.setDefaultUUID();
                if (name == null || name.isEmpty())
                {
                    te.setBeaconName(null); // Set beacon name to a default-generated name.
                    ExperimentalMod.logger.info("New Teleport Beacon placed: name = {}", te.getBeaconName());
                }
            }
            else
            {
                ExperimentalMod.logger.info("Teleport Beacon placed: name = {}", name);
                ITeleportationHandler teleportationHandler = placer.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);
                if (teleportationHandler != null)
                {
                    if (teleportationHandler.hasDestination(uuid))
                    {
                        teleportationHandler.setDestinationAsPlaced(uuid, null, world.provider.getDimension(), pos, null);
                        if (placer instanceof EntityPlayerMP)
                        {
                            ExperimentalMod.network.sendTo(new PacketUpdateTeleportBeacon(pos, true), (EntityPlayerMP) placer);
                        }
                        placer.sendMessage(new TextComponentString("Teleport Beacon " + TextFormatting.DARK_GREEN + name + TextFormatting.RESET + " FOUND in your network"));
                    }
                }
            }
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
                                   boolean willHarvest)
    {
        if (!world.isRemote) // running on server
        {
            TileEntityTeleportBeacon te = getTileEntity(world, pos);
            UUID uuid = te.getUniqueID();
            ITeleportationHandler teleportationHandler = player.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);
            if (teleportationHandler != null)
            {
                teleportationHandler.setDestinationAsRemoved(uuid);
            }
        }

        if (willHarvest)
            return true; // If it will harvest, delay deletion of the block until after getDrops.

        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
                             ItemStack stack)
    {
        super.harvestBlock(world, player, pos, state, te, stack);
        world.setBlockToAir(pos); // getDrops will take care of spawning the item, which will include this block's tile entity data.
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        TileEntityTeleportBeacon te = getTileEntity(world, pos);
        ItemStack itemStack = new ItemStack(Item.getItemFromBlock(this));
        
        // Preserve the custom name in the item stack containing this TE block.
        itemStack.setStackDisplayName(te.getBeaconName());
        
        // Set the BlockEntityTag tag so that Forge will write the TE data when the block is placed in the world again.
        itemStack.setTagInfo("BlockEntityTag", te.serializeNBT());
        drops.add(itemStack);
    }
}
