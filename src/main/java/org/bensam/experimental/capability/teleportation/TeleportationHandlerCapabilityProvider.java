package org.bensam.experimental.capability.teleportation;

import org.bensam.experimental.ExperimentalMod;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/*
 * A class to provide the teleportation handler capability
 */
public class TeleportationHandlerCapabilityProvider implements ICapabilitySerializable<NBTBase>
{
    @CapabilityInject(ITeleportationHandler.class)
    public static Capability<ITeleportationHandler> TELEPORTATION_CAPABILITY = null;

    private final ITeleportationHandler instance = TELEPORTATION_CAPABILITY.getDefaultInstance();

    public static void registerCapability()
    {
        CapabilityManager.INSTANCE.register(ITeleportationHandler.class, new Capability.IStorage<ITeleportationHandler>()
        {
            @Override
            public NBTBase writeNBT(Capability<ITeleportationHandler> capability,
                                    ITeleportationHandler instance, EnumFacing side)
            {
                ExperimentalMod.logger.info("TeleportationHandlerCapabilityProvider storage writeNBT called");

                TeleportationHandler teleportationHandler = (TeleportationHandler)instance;
                return teleportationHandler.serializeNBT();
            }

            @Override
            public void readNBT(Capability<ITeleportationHandler> capability,
                                ITeleportationHandler instance, EnumFacing side, NBTBase nbt)
            {
                ExperimentalMod.logger.info("TeleportationHandlerCapabilityProvider storage readNBT called");
                if (!(instance instanceof TeleportationHandler))
                    throw new RuntimeException("Cannot deserialize instance of ITeleportationHandler to the default TeleportationHandler implementation");
                
                TeleportationHandler teleportationHandler = (TeleportationHandler)instance;
                
                if (nbt instanceof NBTTagList) // TODO: older save format - remove when all old saves are gone
                {
                    NBTTagList nbtTagList = (NBTTagList)nbt;
                    
                    for (int i = 0; i < nbtTagList.tagCount(); ++i)
                    {
                        NBTTagCompound destinationTag = nbtTagList.getCompoundTagAt(i);
                        teleportationHandler.addOrReplaceDestination(new TeleportDestination(destinationTag));
                    }
                }
                else
                {
                    teleportationHandler.deserializeNBT((NBTTagCompound) nbt);
                }
            }
        }, TeleportationHandler::new);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == TELEPORTATION_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return capability == TELEPORTATION_CAPABILITY ? TELEPORTATION_CAPABILITY.cast(instance) : null;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return TELEPORTATION_CAPABILITY.getStorage().writeNBT(TELEPORTATION_CAPABILITY, instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        TELEPORTATION_CAPABILITY.getStorage().readNBT(TELEPORTATION_CAPABILITY, instance, null, nbt);
    }

}
