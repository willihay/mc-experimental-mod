package org.bensam.experimental.block;

import java.util.Arrays;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.block.counter.BlockCounter;
import org.bensam.experimental.block.counter.TileEntityCounter;
import org.bensam.experimental.block.mobdetector.BlockMobDetector;
import org.bensam.experimental.block.mobdetector.TileEntityMobDetector;
import org.bensam.experimental.block.pedestal.BlockPedestal;
import org.bensam.experimental.block.pedestal.TileEntityPedestal;
import org.bensam.experimental.block.teleportbeacon.BlockTeleportBeacon;
import org.bensam.experimental.block.teleportbeacon.TileEntityTeleportBeacon;
import org.bensam.experimental.util.ModSetup;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author WilliHay
 * Thanks to Cadiboo for the registration code examples!
 *
 */
@ObjectHolder(ExperimentalMod.MODID)
public class ModBlocks
{
    public static final BlockOre COPPER_ORE = null;
    public static final BlockCornCrop CORN_CROP = null;
    public static final BlockCounter COUNTER = null;
    public static final BlockMobDetector MOB_DETECTOR = null;
    public static final BlockPedestal PEDESTAL = null;
    public static final BlockTeleportBeacon TELEPORT_BEACON = null;

    // @formatter:off
    public static void register(IForgeRegistry<Block> registry)
    {
        registry.register(new BlockOre("copper_ore"));
        registry.register(new BlockCornCrop("corn_crop"));
        registry.register(new BlockCounter("counter"));
        registry.register(new BlockMobDetector("mob_detector"));
        registry.register(new BlockPedestal("pedestal"));
        registry.register(new BlockTeleportBeacon("teleport_beacon"));
        
        GameRegistry.registerTileEntity(TileEntityCounter.class, new ResourceLocation(ExperimentalMod.MODID, "counter"));
        GameRegistry.registerTileEntity(TileEntityMobDetector.class, new ResourceLocation(ExperimentalMod.MODID, "mob_detector"));
        GameRegistry.registerTileEntity(TileEntityPedestal.class, new ResourceLocation(ExperimentalMod.MODID, "pedestal"));
        GameRegistry.registerTileEntity(TileEntityTeleportBeacon.class, new ResourceLocation(ExperimentalMod.MODID, "teleport_beacon"));
    }
    
    public static void registerItemBlocks(IForgeRegistry<Item> registry)
    {
        Arrays.stream(new Block[]
                {
                    COPPER_ORE,
                    COUNTER,
                    MOB_DETECTOR,
                    PEDESTAL,
                    TELEPORT_BEACON
                }).forEach(block -> 
                {
                    registry.register(
                            ModSetup.setCreativeTab(
                                    ModSetup.setRegistryNames(
                                            new ItemBlock(block),
                                            block.getRegistryName())));
                });
    }

    public static void registerItemBlockModels()
    {
        Arrays.stream(new Block[]
                {
                    COPPER_ORE,
                    COUNTER,
                    MOB_DETECTOR,
                    PEDESTAL,
                    TELEPORT_BEACON
                }).forEach(block -> 
                {
                    Preconditions.checkNotNull(block, "Block cannot be null!");
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, 
                            new ModelResourceLocation(block.getRegistryName(), "inventory"));
                });
    }
    // @formatter:on
    
    public static void registerOreDictionaryEntries()
    {
        OreDictionary.registerOre("oreCopper", COPPER_ORE);
    }
}
