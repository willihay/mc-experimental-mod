package org.bensam.experimental.network;

import org.bensam.experimental.block.pedestal.TileEntityPedestal;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * PacketUpdatePedestal - sent from server to client to update the item stored in the pedestal on the client, used
 * whenever the item changes on the server
 */
public class PacketUpdatePedestal implements IMessage
{
    private BlockPos pos;
    private ItemStack itemStack;
    private long lastChangeTime;

    public PacketUpdatePedestal(BlockPos pos, ItemStack itemStack, long lastChangeTime)
    {
        this.pos = pos;
        this.itemStack = itemStack;
        this.lastChangeTime = lastChangeTime;
    }

    public PacketUpdatePedestal(TileEntityPedestal te)
    {
        this(te.getPos(), te.inventory.getStackInSlot(0), te.lastChangeTime);
    }

    /*
     * Constructor for Forge to call via reflection, which will then call fromBytes() to initialize fields
     */
    public PacketUpdatePedestal()
    {}

    public static class Handler implements IMessageHandler<PacketUpdatePedestal, IMessage>
    {
        @Override
        public IMessage onMessage(PacketUpdatePedestal message, MessageContext ctx)
        {
            // Update the pedestal in the client, scheduling this execution on the main thread instead of the Netty networking thread.
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                TileEntityPedestal te = (TileEntityPedestal) Minecraft.getMinecraft().world.getTileEntity(message.pos);
                te.inventory.setStackInSlot(0, message.itemStack);
                te.lastChangeTime = message.lastChangeTime;
            });
            
            return null;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
        itemStack = ByteBufUtils.readItemStack(buf);
        lastChangeTime = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeItemStack(buf, itemStack);
        buf.writeLong(lastChangeTime);
    }

}
