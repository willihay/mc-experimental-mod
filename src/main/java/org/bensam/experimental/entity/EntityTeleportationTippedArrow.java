package org.bensam.experimental.entity;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.capability.teleportation.TeleportationHelper;
import org.bensam.experimental.item.ModItems;
import org.bensam.experimental.potion.PotionTeleportation;

import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class EntityTeleportationTippedArrow extends EntityTippedArrow
{
    private TileEntity sourceTileEntity;
    
    public EntityTeleportationTippedArrow(World world)
    {
        super(world);
    }

    public EntityTeleportationTippedArrow(World world, EntityLivingBase shooter)
    {
        super(world, shooter);
    }

    public EntityTeleportationTippedArrow(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public EntityTeleportationTippedArrow(World world, double x, double y, double z, IBlockSource source)
    {
        super(world, x, y, z);
        sourceTileEntity = source.getBlockTileEntity();
    }

    public TileEntity getSourceTileEntity()
    {
        return sourceTileEntity;
    }

    @Override
    public int getColor()
    {
        PotionTeleportation potion = new PotionTeleportation();
        return potion.getLiquidColor();
    }

    @Override
    protected void arrowHit(EntityLivingBase entityHit)
    {
        World world = entityHit.world;
        
        if (!world.isRemote) // running on server
        {
            PotionTeleportation potion = new PotionTeleportation();
            Entity shooter = this.shootingEntity;

            if (shooter != null)
            {
                potion.affectEntity(this, shooter, entityHit);
            }
            else if (sourceTileEntity != null)
            {
                potion.affectEntity(this, sourceTileEntity, entityHit);
            }
        }
    }

    @Override
    protected ItemStack getArrowStack()
    {
        return new ItemStack(ModItems.teleportationTippedArrow);
    }

}
