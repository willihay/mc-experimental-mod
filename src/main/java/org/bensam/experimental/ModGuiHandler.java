/**
 * 
 */
package org.bensam.experimental;

import org.bensam.experimental.block.pedestal.ContainerPedestal;
import org.bensam.experimental.block.pedestal.GuiPedestal;
import org.bensam.experimental.block.pedestal.TileEntityPedestal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * @author Will
 *
 */
public class ModGuiHandler implements IGuiHandler
{
    public static final int PEDESTAL = 0; // pedestal's GUI ID
    
    /* (non-Javadoc)
     * @see net.minecraftforge.fml.common.network.IGuiHandler#getServerGuiElement(int, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
     */
    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
        case PEDESTAL:
            return new ContainerPedestal(player.inventory, (TileEntityPedestal)world.getTileEntity(new BlockPos(x, y, z)));
        default:
            return null;
        }
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.fml.common.network.IGuiHandler#getClientGuiElement(int, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int)
     */
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch(ID)
        {
        case PEDESTAL:
            return new GuiPedestal(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
        default:
            return null;
        }
    }

}
