/**
 * 
 */
package org.bensam.experimental.block.mobdetector;

import java.util.List;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * @author Will
 *
 */
public class TileEntityMobDetector extends TileEntity implements ITickable
{
    private static final int INITIAL_DELAY = 10;
    
    // Calculated on the client so no need for readFromNBT or writeToNBT.
    private boolean lit = false;

    private int updateDelayCounter = INITIAL_DELAY;
    private int lastMobCount = 0;
    
    public boolean isLit()
    {
        return lit;
    }

    @Override
    public void update()
    {
        if (world.isRemote) // running on client
        {
            boolean wasLit = lit;
            updateMobCount();
            lit = lastMobCount > 0;
            if (lit != wasLit)
            {
                world.markBlockRangeForRenderUpdate(getPos(), getPos());
            }
        }
    }

    private void updateMobCount()
    {
        // Count the entities every #INITIAL_DELAY ticks (usu. there are 20 ticks/second).
        updateDelayCounter--;
        if (updateDelayCounter <= 0)
        {
            List<EntityMob> list = world.getEntitiesWithinAABB(EntityMob.class,
                    new AxisAlignedBB(getPos().add(-20, -10, -20), getPos().add(20, 10, 20)));
            lastMobCount = list.size();
            updateDelayCounter = INITIAL_DELAY;
        }
    }
}
