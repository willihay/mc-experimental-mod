package org.bensam.experimental.block.counter;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * @author WilliHay
 *
 */
public class TileEntityCounter extends TileEntity
{
    private int count;

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        count = compound.getInteger("count");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("count", count);
        return super.writeToNBT(compound);
    }

    public int getCount()
    {
        return count;
    }

    public void incrementCount()
    {
        count++;
        markDirty();
    }

    public void decrementCount()
    {
        count--;
        markDirty();
    }
}
