package org.bensam.experimental.block.pedestal;

import javax.annotation.Nullable;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.network.PacketRequestUpdatePedestal;
import org.bensam.experimental.network.PacketUpdatePedestal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author WilliHay
 *
 */
public class TileEntityPedestal extends TileEntity
{
    public long lastChangeTime;

    // @formatter:off
    public ItemStackHandler inventory = new ItemStackHandler(1)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            if (!world.isRemote) // server only
            {
                // When the contents of the pedestal change, notify nearby players (within 64 meters).
                lastChangeTime = world.getTotalWorldTime();
                ExperimentalMod.network.sendToAllAround(
                        new PacketUpdatePedestal(TileEntityPedestal.this), 
                        new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
            }
        }
    };
    // @formatter:on

    @Override
    public void onLoad()
    {
        if (world.isRemote) // client only
        {
            // This TileEntity is saved on the server, so request an update from the server
            // so that the client can learn what data is stored in the pedestal.
            ExperimentalMod.network.sendToServer(new PacketRequestUpdatePedestal(this));
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        lastChangeTime = compound.getLong("lastChangeTime");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setLong("lastChangeTime", lastChangeTime);
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    // @formatter:off
    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
    }
    // @formatter:on
}
