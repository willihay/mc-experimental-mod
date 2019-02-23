package org.bensam.experimental.handler;

import org.bensam.experimental.ExperimentalMod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ExperimentalMod.MODID)
public class WorldEventHandler
{
    
    @SubscribeEvent
    public static void onEntityLivingDeath(LivingDeathEvent event)
    {
        EntityLivingBase entityLiving = event.getEntityLiving();
        if (entityLiving instanceof EntityPlayer && !entityLiving.world.isRemote)
        {
            BlockPos lastPos = entityLiving.getPosition();
            entityLiving.sendMessage(new TextComponentTranslation("message.death_coordinates", lastPos));
        }
    }
    
    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event)
    {
        if (event.getName().toString().equals("minecraft:chests/spawn_bonus_chest"))
        {
            ExperimentalMod.logger.info("Found spawn_bonus_chest");
            LootEntryTable entry = new LootEntryTable(
                    new ResourceLocation(ExperimentalMod.MODID, "chests/spawn_bonus_chest"), 1, 0, new LootCondition[0],
                    "experimental_inject_entry");
            LootPool pool = new LootPool(new LootEntry[] { entry }, new LootCondition[0], new RandomValueRange(1),
                    new RandomValueRange(0), "experimental_inject_pool");

            event.getTable().addPool(pool);
        }
    }
}
