/**
 * 
 */
package org.bensam.experimental.entity;

import net.minecraft.entity.ai.EntityAIAttackMelee;

/**
 * @author Will
 *
 */
public class EntityAIHealthZombieAttack extends EntityAIAttackMelee
{
    private int raiseArmTicks;
    private EntityHealthZombie entityHealthZombie;

    public EntityAIHealthZombieAttack(EntityHealthZombie zombie, double speed, boolean useLongMemory)
    {
        super(zombie, speed, useLongMemory);
        this.entityHealthZombie = zombie;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        super.startExecuting();
        this.raiseArmTicks = 0;
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask()
    {
        super.resetTask();
        this.entityHealthZombie.setArmsRaised(false);
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
        super.updateTask();
        ++this.raiseArmTicks;

        if (this.raiseArmTicks >= 5 && this.attackTick < 10)
        {
            this.entityHealthZombie.setArmsRaised(true);
        }
        else
        {
            this.entityHealthZombie.setArmsRaised(false);
        }
    }
}
