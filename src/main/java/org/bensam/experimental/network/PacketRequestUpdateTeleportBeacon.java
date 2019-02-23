package org.bensam.experimental.network;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.block.teleportbeacon.TileEntityTeleportBeacon;
import org.bensam.experimental.capability.teleportation.ITeleportationHandler;
import org.bensam.experimental.capability.teleportation.TeleportationHandlerCapabilityProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * PacketRequestUpdateTeleportBeacon - sent from client to server when client loads the TileEntity and needs to get its data
 * from the server
 */
public class PacketRequestUpdateTeleportBeacon implements IMessage
{
    private BlockPos pos;
    private int dimension;
    
    public PacketRequestUpdateTeleportBeacon(BlockPos pos, int dimension)
    {
        this.pos = pos;
        this.dimension = dimension;
    }
    
    public PacketRequestUpdateTeleportBeacon(TileEntityTeleportBeacon te)
    {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }
    
    public PacketRequestUpdateTeleportBeacon()
    {}

    public static class Handler implements IMessageHandler<PacketRequestUpdateTeleportBeacon, PacketUpdateTeleportBeacon>
    {
        @Override
        public PacketUpdateTeleportBeacon onMessage(PacketRequestUpdateTeleportBeacon message, MessageContext ctx)
        {
            boolean isBeaconActive = false;
            EntityPlayer player = ctx.getServerHandler().player;
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(message.dimension);
            TileEntity te = world.getTileEntity(message.pos);
            
            if (te instanceof TileEntityTeleportBeacon)
            {
                ITeleportationHandler teleportationHandler = player.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);

                if (teleportationHandler != null)
                {
                    isBeaconActive = teleportationHandler.hasDestination(((TileEntityTeleportBeacon) te).getUniqueID());
                }

                return new PacketUpdateTeleportBeacon(message.pos, isBeaconActive);
            }

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
