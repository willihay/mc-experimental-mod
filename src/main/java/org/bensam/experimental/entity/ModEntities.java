/**
 * 
 */
package org.bensam.experimental.entity;

import org.bensam.experimental.ExperimentalMod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author Will
 *
 */
public class ModEntities
{
    private static int networkID = 1; // unique ID for entities from this mod
    
    // @formatter:off
    public static void preInit()
    {
//        EntityRegistry.registerModEntity(new ResourceLocation(ExperimentalMod.MODID, "health_zombie"), EntityHealthZombie.class, "health_zombie", networkID++, ExperimentalMod.instance,
//                /* trackingRange */ 64,
//                /* updateFrequency */ 3,
//                /* sendsVelocityUpdates */ true,
//                /* primaryEgg */ 0x996600,
//                /* secondaryEgg */ 0x00ff00);
        
//        EntityRegistry.addSpawn(EntityHealthZombie.class, 
//                /* weightedProb */ 100, 
//                /* min */ 3, 
//                /* max */ 5, 
//                /* typeOfCreature */ EnumCreatureType.MONSTER, 
//                /* biomes */ Biomes.PLAINS, Biomes.ICE_PLAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU);
        
//        LootTableList.register(EntityHealthZombie.LOOT);
    }
    // @formatter:on

    public static void registerEntities(IForgeRegistry<EntityEntry> registry)
    {
        EntityEntry entryTeleportationSplashPotion = EntityEntryBuilder.create()
                .id(new ResourceLocation(ExperimentalMod.MODID, "teleportation_splash_potion"), networkID++)
                .name(ExperimentalMod.MODID + ":teleportation_splash_potion")
                .entity(EntityTeleportationSplashPotion.class)
                .tracker(64, 3, true)
                .build();

        registry.register(entryTeleportationSplashPotion);

        EntityEntry entryTeleportationTippedArrow = EntityEntryBuilder.create()
                .id(new ResourceLocation(ExperimentalMod.MODID, "teleportation_tipped_arrow"), networkID++)
                .name(ExperimentalMod.MODID + ":teleportation_tipped_arrow")
                .entity(EntityTeleportationTippedArrow.class)
                .tracker(64, 3, true)
                .build();

        registry.register(entryTeleportationTippedArrow);
    }

    public static void registerRenderer()
    {
//        RenderingRegistry.registerEntityRenderingHandler(EntityHealthZombie.class, RenderHealthZombie.RENDER_FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityTeleportationSplashPotion.class, RenderTeleportationSplashPotion.RENDER_FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityTeleportationTippedArrow.class, RenderTeleportationTippedArrow.RENDER_FACTORY);
    }
}
