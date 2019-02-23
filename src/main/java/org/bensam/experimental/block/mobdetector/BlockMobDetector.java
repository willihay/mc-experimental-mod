/**
 * 
 */
package org.bensam.experimental.block.mobdetector;

import java.util.List;

import org.bensam.experimental.block.BlockTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author Will
 *
 */
public class BlockMobDetector extends BlockTileEntity<TileEntityMobDetector>
{
    public static final PropertyBool LIT = PropertyBool.create("lit");

    public BlockMobDetector()
    {
        super(Material.GLASS, "mob_detector");
        setLightLevel(1.0F);
    }

    //    @SideOnly(Side.CLIENT)
    //    @Override
    //    public BlockRenderLayer getRenderLayer()
    //    {
    //        return BlockRenderLayer.TRANSLUCENT;
    //    }
    //
    //    @Override
    //    @Deprecated
    //    public boolean isOpaqueCube(IBlockState state)
    //    {
    //        return false;
    //    }

    @Override
    public Class<TileEntityMobDetector> getTileEntityClass()
    {
        return TileEntityMobDetector.class;
    }

    @Override
    public TileEntityMobDetector createTileEntity(World world, IBlockState state)
    {
        return new TileEntityMobDetector();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.withProperty(LIT, getTileEntity(world, pos).isLit());
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LIT);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote) // i.e. running on the server thread
        {
            TileEntityMobDetector tile = getTileEntity(world, pos);
            List<EntityMob> list = world.getEntitiesWithinAABB(EntityMob.class,
                    new AxisAlignedBB(tile.getPos().add(-20, -10, -20), tile.getPos().add(20, 10, 20)));

            player.sendMessage(new TextComponentString("Nearby mobs: " + list.size()));
            for (EntityMob mob : list)
            {
                player.sendMessage(
                        new TextComponentString(mob.getName() + " " + (mob.getPosition().subtract(tile.getPos()))));
            }
        }

        return true;
    }

}
