package org.bensam.experimental.block.counter;

import javax.annotation.Nonnull;

import org.bensam.experimental.util.ModSetup;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author WilliHay
 *
 */
public class BlockCounter extends Block
{
    public BlockCounter(@Nonnull String name)
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
    public TileEntityCounter createTileEntity(World world, IBlockState state)
    {
        return new TileEntityCounter();
    }
    
    public TileEntityCounter getTileEntity(@Nonnull IBlockAccess world, BlockPos pos)
    {
        return (TileEntityCounter) world.getTileEntity(pos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote) // i.e. running on the server thread
        {
            TileEntityCounter tile = getTileEntity(world, pos);
            if (facing == EnumFacing.DOWN)
            {
                tile.decrementCount();
            }
            else if (facing == EnumFacing.UP)
            {
                tile.incrementCount();
            }
            
            player.sendMessage(new TextComponentString("Count: " + tile.getCount()));
        }

        return true;
    }
}
