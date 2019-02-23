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
 * PacketRequestUpdatePedestal - sent from client to server when client loads the TileEntity and needs to get its data
 * from the server
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
        @Override
        public PacketUpdatePedestal onMessage(PacketRequestUpdatePedestal message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntityPedestal te = (TileEntityPedestal) world.getTileEntity(message.pos);
            if (te != null)
                return new PacketUpdatePedestal(te);
            else
                return null;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
        dimension = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        buf.writeInt(dimension);
    }
}
