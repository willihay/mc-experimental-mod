package org.bensam.experimental.potion;

import javax.annotation.Nullable;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.capability.teleportation.TeleportationHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Will
 *
 */
public class PotionTeleportation extends PotionBase
{
    public PotionTeleportation()
    {
        super("teleportation_potion", true, false, 3121697, 0, 0);
        setBeneficial();
    }

    /*
     * Called when entity is affected by a Splash Potion of Teleportation or hit by an Arrow of Teleportation
     * thrown by a TileEntity (e.g. dispenser).
     */
    public Entity affectEntity(@Nullable Entity source, @Nullable TileEntity indirectSource, Entity entityAffected, boolean rotateToFaceIfBeacon)
    {
        ExperimentalMod.logger.info("PotionTeleportation.affectEntity " + entityAffected.getDisplayName().getFormattedText() + " from TileEntity");
        
        World world = entityAffected.world;
        
        if (!world.isRemote && indirectSource != null) // running on server
        {
            return TeleportationHelper.teleportOther(entityAffected, indirectSource, rotateToFaceIfBeacon);
        }
        
        return entityAffected;
    }

    /*
     * Called when entity is affected by a Splash Potion of Teleportation or hit by an Arrow of Teleportation
     * thrown/shot by a living entity (e.g. player).
     */
    public Entity affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, Entity entityAffected, boolean rotateToFaceIfBeacon)
    {
        ExperimentalMod.logger.info("PotionTeleportation.affectEntity " + entityAffected.getDisplayName().getFormattedText() + " from Entity");
        
        World world = entityAffected.world;
        
        if (!world.isRemote && indirectSource != null) // running on server
        {
            return TeleportationHelper.teleportOther(entityAffected, indirectSource, rotateToFaceIfBeacon);
        }
        
        return entityAffected;
    }

    /*
     * Called when entity is hit by an Arrow of Teleportation.
     */
//    @Override
//    public void performEffect(EntityLivingBase entityLivingBase, int amplifier)
//    {
//        ExperimentalMod.logger.info("PotionTeleportation.performEffect");
//        
//        World world = entityLivingBase.world;
//        EntityLivingBase indirectSource = entityLivingBase.getAttackingEntity();
//        
//        if (!world.isRemote && indirectSource != null) // running on server
//        {
//            TeleportationHelper.teleportOther(entityLivingBase, indirectSource);
//        }
//    }

    /*
     * Specifies when potion effect is ready. performEffect() won't be called until it's ready.
     * Must override to control when this happens.
     */
    @Override
    public boolean isReady(int duration, int amplifier)
    {
        return duration >= 1;
    }
}
