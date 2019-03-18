package org.bensam.experimental.potion;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.capability.teleportation.TeleportationHelper;

import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author WilliHay
 *
 */
public class PotionTeleportation extends Potion
{
    public PotionTeleportation()
    {
        super(false, 3121697);
        setBeneficial();
    }

    public PotionTeleportation setup(String name)
    {
        setRegistryName(ExperimentalMod.MODID, name);
        setPotionName("effect." + name);
        return this;
    }

    @Override
    public boolean isInstant()
    {
        return true;
    }

    /*
     * Called when entity is affected by a Splash Potion of Teleportation or hit by an Arrow of Teleportation
     * thrown by a TileEntity (e.g. dispenser).
     */
    public Entity affectEntity(Entity source, TileEntity indirectSource, Entity entityAffected)
    {
        ExperimentalMod.MOD_LOGGER.info("PotionTeleportation.affectEntity {} from {} at {}", 
                entityAffected.getDisplayName().getFormattedText(),
                indirectSource.getDisplayName().getFormattedText(),
                indirectSource.getPos());
        
        World world = entityAffected.world;
        
        if (!world.isRemote) // running on server
        {
            return TeleportationHelper.teleportOther(entityAffected, indirectSource);
        }
        
        return entityAffected;
    }

    /*
     * Called when entity is affected by a Splash Potion of Teleportation or hit by an Arrow of Teleportation
     * thrown/shot by a living entity (e.g. player).
     */
    public Entity affectEntity(Entity source, Entity indirectSource, Entity entityAffected)
    {
        ExperimentalMod.MOD_LOGGER.info("PotionTeleportation.affectEntity {} from {} at {}", 
                entityAffected.getDisplayName().getFormattedText(),
                indirectSource.getDisplayName().getFormattedText(),
                indirectSource.getPosition());
        
        World world = entityAffected.world;
        
        if (!world.isRemote) // running on server
        {
            return TeleportationHelper.teleportOther(entityAffected, indirectSource);
        }
        
        return entityAffected;
    }
}
