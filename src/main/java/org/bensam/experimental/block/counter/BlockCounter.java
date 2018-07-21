/**
 * 
 */
package org.bensam.experimental.block.counter;

import javax.annotation.Nullable;

import org.bensam.experimental.block.BlockTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * @author Will
 *
 */
public class BlockCounter extends BlockTileEntity<TileEntityCounter>
{
    public BlockCounter()
    {
        super(Material.ROCK, "counter");
    }

    /* (non-Javadoc)
     * @see org.bensam.experimental.block.BlockTileEntity#getTileEntityClass()
     */
    @Override
    public Class<TileEntityCounter> getTileEntityClass()
    {
        return TileEntityCounter.class;
    }

    /* (non-Javadoc)
     * @see org.bensam.experimental.block.BlockTileEntity#createTileEntity(net.minecraft.world.World, net.minecraft.block.state.IBlockState)
     */
    @Nullable
    @Override
    public TileEntityCounter createTileEntity(World world, IBlockState state)
    {
        return new TileEntityCounter();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
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
