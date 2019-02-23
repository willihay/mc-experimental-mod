package org.bensam.experimental.block.teleportbeacon;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.ModHelper;
import org.bensam.experimental.block.ModBlocks;
import org.bensam.experimental.network.PacketRequestUpdateTeleportBeacon;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;

public class TileEntityTeleportBeacon extends TileEntity implements IWorldNameable
{
    public static final ItemStack TOPPER_ITEM_WHEN_ACTIVE = new ItemStack(Items.ENDER_EYE);
    public boolean isActive = false;

    private String beaconName = "";
    private UUID uniqueID = new UUID(0, 0);

    @Override
    public void onLoad()
    {
        if (world.isRemote) // running on client
        {
            ExperimentalMod.network.sendToServer(new PacketRequestUpdateTeleportBeacon(this));
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        ExperimentalMod.logger.info("TileEntityTeleportBeacon.readFromNBT called");

        if (compound.hasKey("beaconName"))
        {
            beaconName = compound.getString("beaconName");
            if (beaconName == null || beaconName.isEmpty())
            {
                ExperimentalMod.logger.info("TileEntityTeleportBeacon.readFromNBT: beaconName null or empty");
            }
            else
            {
                ExperimentalMod.logger.info("TileEntityTeleportBeacon.readFromNBT: beaconName = " + beaconName);
            }
        }

        if (compound.hasUniqueId("uniqueID"))
        {
            uniqueID = compound.getUniqueId("uniqueID");
            if (uniqueID == null || uniqueID.equals(new UUID(0, 0)))
            {
                ExperimentalMod.logger.info("TileEntityTeleportBeacon.readFromNBT: uniqueID empty");
            }
            else
            {
                ExperimentalMod.logger.info("TileEntityTeleportBeacon.readFromNBT: uniqueID = " + uniqueID);
            }
        }

        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        ExperimentalMod.logger.info("TileEntityTeleportBeacon.writeToNBT called");

        if (beaconName.isEmpty())
        {
            ExperimentalMod.logger.info("TileEntityTeleportBeacon.writeToNBT: beaconName empty");
        }
        else
        {
            ExperimentalMod.logger.info("Writing NBT for " + beaconName);
            compound.setString("beaconName", beaconName);
        }

        if (uniqueID.equals(new UUID(0, 0)))
        {
            ExperimentalMod.logger.info("TileEntityTeleportBeacon.writeToNBT: uniqueID empty");
        }
        else
        {
            compound.setUniqueId("uniqueID", uniqueID);
        }

        return super.writeToNBT(compound);
    }

    public UUID getUniqueID()
    {
        return uniqueID;
    }

    public void setDefaultUUID()
    {
        uniqueID = UUID.randomUUID();
        markDirty();
    }

    public String getBeaconName()
    {
        return beaconName;
    }

    /*
     * Pass in null or empty string to set to default name format, based on time and date.
     */
    public void setBeaconName(@Nullable String name)
    {
        if (name == null || name.isEmpty())
        {
            beaconName = "Beacon " + ModHelper.getRandomLetter() + (new SimpleDateFormat("ss").format(new Date()));
        }
        else
        {
            beaconName = name;
        }
        markDirty();
    }

    @Override
    public String getName()
    {
        return hasCustomName() ? beaconName : ModBlocks.teleportBeacon.getTranslationKey();
    }

    @Override
    public boolean hasCustomName()
    {
        return !(beaconName.isEmpty());
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }
}
