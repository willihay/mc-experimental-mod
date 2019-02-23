package org.bensam.experimental.network;

import org.bensam.experimental.block.teleportbeacon.TileEntityTeleportBeacon;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * PacketUpdateTeleportBeacon - sent from server to client to update the active status stored in the teleport beacon on the client,
 * used whenever the status changes on the server
 */
public class PacketUpdateTeleportBeacon implements IMessage
{
    private BlockPos pos;
    private boolean isActive;
    
    public PacketUpdateTeleportBeacon(BlockPos pos, boolean isActive)
    {
        this.pos = pos;
        this.isActive = isActive;
    }
    
    /*
     * Constructor for Forge to call via reflection, which will then call fromBytes() to initialize fields
     */
    public PacketUpdateTeleportBeacon()
    {}
    
    public static class Handler implements IMessageHandler<PacketUpdateTeleportBeacon, IMessage>
    {
        @Override
        public IMessage onMessage(PacketUpdateTeleportBeacon message, MessageContext ctx)
        {
            // Update the teleport beacon in the client, scheduling this execution on the main thread instead of the Netty networking thread.
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if (te instanceof TileEntityTeleportBeacon)
                {
                    ((TileEntityTeleportBeacon) te).isActive = message.isActive;
                }
            });
            
            return null;
        }
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
        isActive = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        buf.writeBoolean(isActive);
    }

}