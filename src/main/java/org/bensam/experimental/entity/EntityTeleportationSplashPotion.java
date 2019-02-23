package org.bensam.experimental.entity;

import java.util.List;

import org.bensam.experimental.potion.PotionTeleportation;

import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTeleportationSplashPotion extends EntityThrowable
{
    private TileEntity sourceTileEntity;
    private boolean setDeadNextUpdate; // avoids "index out of bounds" exception when this splash potion causes another entity to be removed immediately from a World entity list
    
    public EntityTeleportationSplashPotion(World world)
    {
        super(world);
    }

    public EntityTeleportationSplashPotion(World world, double x, double y, double z, IBlockSource source)
    {
        super(world, x, y, z);
        sourceTileEntity = source.getBlockTileEntity();
    }

    public EntityTeleportationSplashPotion(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
    }

    public TileEntity getSourceTileEntity()
    {
        return sourceTileEntity;
    }
    
    /**
     * Gets the amount of gravity to apply to the thrown entity with each tick.
     */
    @Override
    protected float getGravityVelocity()
    {
        return 0.05F; // same as EntityPotion
    }

    @Override
    public void onUpdate()
    {
        if (setDeadNextUpdate)
        {
            this.setDead();
        }
        
        super.onUpdate();
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote && !setDeadNextUpdate)
        {
            PotionTeleportation potion = new PotionTeleportation();
            this.applySplash(result, potion);
            this.world.playEvent(2007, new BlockPos(this), potion.getLiquidColor()); // 2007 == potion instant effect
        }
    }

    private void applySplash(RayTraceResult result, PotionTeleportation potion)
    {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

        if (list.isEmpty())
        {
            this.setDead();
        }
        else 
        {
            for (EntityLivingBase entityLivingBase : list)
            {
                if (entityLivingBase.canBeHitWithPotion())
                {
                    double d0 = this.getDistanceSq(entityLivingBase);

                    if (d0 < 16.0D)
                    {
                        EntityLivingBase thrower = this.getThrower();
                        if (thrower != null)
                        {
                            potion.affectEntity(this, thrower, entityLivingBase, 0, 0.0D);
                        }
                        else if (sourceTileEntity != null)
                        {
                            potion.affectEntity(this, sourceTileEntity, entityLivingBase);
                        }
                    }
                }
            }
            
            setDeadNextUpdate = true;
        }
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("SetDeadNextUpdate", setDeadNextUpdate);
        
        if (sourceTileEntity != null)
        {
            compound.setLong("SourceTileEntityPos", sourceTileEntity.getPos().toLong());
        }
        
        super.writeEntityToNBT(compound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        setDeadNextUpdate = compound.getBoolean("SetDeadNextUpdate");
        
        if (compound.hasKey("SourceTileEntityPos"))
        {
            BlockPos sourceTileEntityPos = BlockPos.fromLong(compound.getLong("SourceTileEntityPos"));
            sourceTileEntity = this.world.getTileEntity(sourceTileEntityPos);
        }
        
        super.readEntityFromNBT(compound);
    }
}
