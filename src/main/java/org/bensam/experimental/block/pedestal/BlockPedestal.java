package org.bensam.experimental.block.pedestal;

import javax.annotation.Nonnull;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.util.ModGuiHandler;
import org.bensam.experimental.util.ModSetup;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * @author WilliHay
 *
 */
public class BlockPedestal extends Block
{
    public BlockPedestal(@Nonnull String name)
    {
        super(Material.ROCK);
        
        ModSetup.setRegistryNames(this, name);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntityPedestal createTileEntity(World world, IBlockState state)
    {
        return new TileEntityPedestal();
    }
    
    public TileEntityPedestal getTileEntity(@Nonnull IBlockAccess world, BlockPos pos)
    {
        return (TileEntityPedestal) world.getTileEntity(pos);
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote) // i.e. running on the server thread
        {
            ItemStack heldItem = player.getHeldItem(hand);
            TileEntityPedestal tile = getTileEntity(world, pos);
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing); // object that stores the pedestal's inventory

            if (!player.isSneaking())
            {
                if (heldItem.isEmpty())
                {
                    player.setHeldItem(hand, itemHandler.extractItem(0, 64, false));
                }
                else
                {
                    player.setHeldItem(hand, itemHandler.insertItem(0, heldItem, false));
                }
                tile.markDirty();
            }
            else
            {
                player.openGui(ExperimentalMod.instance, ModGuiHandler.PEDESTAL, world, pos.getX(), pos.getY(),
                        pos.getZ());
            }
        }

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntityPedestal tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        ItemStack stack = itemHandler.getStackInSlot(0);

        if (!stack.isEmpty())
        {
            EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            world.spawnEntity(item);
        }

        super.breakBlock(world, pos, state);
    }
}
