/**
 * PacketRequestUpdatePedestal - sent from client to server when client loads the TileEntity and needs to
 * get its data from the server
 */
package org.bensam.experimental.network;

import org.bensam.experimental.block.pedestal.TileEntityPedestal;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Will
 *
 */
public class PacketRequestUpdatePedestal implements IMessage
{
    private BlockPos pos;
    private int dimension;
    
    public PacketRequestUpdatePedestal(BlockPos pos, int dimension)
    {
        this.pos = pos;
        this.dimension = dimension;
    }

    public PacketRequestUpdatePedestal(TileEntityPedestal te)
    {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }
    
    public PacketRequestUpdatePedestal()
    {}
    
    public static class Handler implements IMessageHandler<PacketRequestUpdatePedestal, PacketUpdatePedestal>
    {
        /* (non-Javadoc)
         * @see net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler#onMessage(net.minecraftforge.fml.common.network.simpleimpl.IMessage, net.minecraftforge.fml.common.network.simpleimpl.MessageContext)
         */
        @Override
        public PacketUpdatePedestal onMessage(PacketRequestUpdatePedestal message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityPedestal te = (TileEntityPedestal)world.getTileEntity(message.pos);
            if (te != null)
            {
                return new PacketUpdatePedestal(te);
            }
            else
            {
                return null;
            }
        }
        
    }
    /* (non-Javadoc)
     * @see net.minecraftforge.fml.common.network.simpleimpl.IMessage#fromBytes(io.netty.buffer.ByteBuf)
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
        dimension = buf.readInt();
    }

    /* (non-Javadoc)
     * @see net.minecraftforge.fml.common.network.simpleimpl.IMessage#toBytes(io.netty.buffer.ByteBuf)
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);      
    }

}
