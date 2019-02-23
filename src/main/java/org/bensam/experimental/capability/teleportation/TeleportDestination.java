package org.bensam.experimental.capability.teleportation;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.bensam.experimental.ExperimentalMod;

import com.google.common.base.MoreObjects;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class TeleportDestination implements INBTSerializable<NBTTagCompound>
{
    public enum DestinationType
    {
        BLOCKPOS(0, "Block"),
        SPAWNBED(1, "Spawn Bed"),
        BEACON(2, "Beacon");
        
        private final int destinationValue;
        private final String valueName;
        
        private DestinationType(int destinationValue, String name)
        {
            this.destinationValue = destinationValue;
            this.valueName = name;
        }
        
        @Override
        public String toString()
        {
            return this.valueName;
        }

        int getDestinationValue()
        {
            return destinationValue;
        }
    }

    public DestinationType destinationType;
    public int dimension;
    public String friendlyName;
    public BlockPos position;
    public EnumFacing preferredTeleportFace;
    private UUID uuid;
    
    public TeleportDestination(NBTTagCompound nbt)
    {
        deserializeNBT(nbt);
    }
    
    public TeleportDestination(@Nonnull String friendlyName, DestinationType destinationType, int dimension, @Nonnull BlockPos position)
    {
        this(UUID.randomUUID(), friendlyName, destinationType, dimension, position, EnumFacing.UP);
    }
    
    public TeleportDestination(@Nonnull UUID uuid, @Nonnull String friendlyName, DestinationType destinationType, int dimension, @Nonnull BlockPos position, EnumFacing preferredTeleportFace)
    {
        this.uuid = uuid;
        this.friendlyName = friendlyName;
        this.destinationType = destinationType;
        this.dimension = dimension;
        this.position = position;
        this.preferredTeleportFace = preferredTeleportFace;
        
        if (this.uuid == null)
        {
            this.uuid = new UUID(0, 0);
        }
        
        if (this.friendlyName == null)
        {
            this.friendlyName = "<Invalid Destination Name>";
        }
        
        if (this.position == null)
        {
            this.position = BlockPos.ORIGIN;
        }
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof TeleportDestination))
        {
            return false;
        }
        else
        {
            return this.uuid != null && this.uuid.equals(((TeleportDestination)obj).getUUID());
        }
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("Type", destinationType)
                .add("UUID", uuid)
                .add("Name", friendlyName)
                .add("Dimension", dimension)
                .add("Position", position)
                .add("Facing", preferredTeleportFace)
                .toString();
    }

    public UUID getUUID()
    {
        return uuid;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        ExperimentalMod.logger.info("TeleportDestination.serializeNBT called for " + friendlyName);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("DestinationType", destinationType.getDestinationValue());
        nbt.setInteger("Dimension", dimension);
        nbt.setString("FriendlyName", friendlyName);
        nbt.setLong("Position", position.toLong());
        nbt.setInteger("TeleportFace", preferredTeleportFace.getIndex());
        nbt.setUniqueId("UUID", uuid);
        
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        ExperimentalMod.logger.info("TeleportDestination.deserializeNBT called for " + nbt.getString("FriendlyName"));
        destinationType = DestinationType.values()[nbt.getInteger("DestinationType")];
        dimension = nbt.getInteger("Dimension");
        friendlyName = nbt.getString("FriendlyName");
        position = BlockPos.fromLong(nbt.getLong("Position"));
        preferredTeleportFace = EnumFacing.byIndex(nbt.getInteger("TeleportFace"));
        uuid = nbt.getUniqueId("UUID");
    }
}
