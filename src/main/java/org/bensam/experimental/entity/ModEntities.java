/**
 * 
 */
package org.bensam.experimental.entity;

import org.bensam.experimental.ExperimentalMod;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * @author Will
 *
 */
public class ModEntities
{
    public static void preInit()
    {
        int id = 1;
        EntityRegistry.registerModEntity(new ResourceLocation(ExperimentalMod.MODID, "health_zombie"), EntityHealthZombie.class, "health_zombie", id++, ExperimentalMod.instance,
                /* trackingRange */ 64,
                /* updateFrequency */ 3,
                /* sendsVelocityUpdates */ true,
                /* primaryEgg */ 0x996600,
                /* secondaryEgg */ 0x00ff00);
        
        EntityRegistry.addSpawn(EntityHealthZombie.class, 
                /* weightedProb */ 100, 
                /* min */ 3, 
                /* max */ 5, 
                /* typeOfCreature */ EnumCreatureType.MONSTER, 
                /* biomes */ Biomes.PLAINS, Biomes.ICE_PLAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU);
        
        LootTableList.register(EntityHealthZombie.LOOT);
    }
    
    public static void registerRenderer()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityHealthZombie.class, RenderHealthZombie.RENDER_FACTORY);
    }
}
