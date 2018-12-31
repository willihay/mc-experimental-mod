/**
 * ModBlocks - contains instances of all our blocks. Blocks are singletons.
 */
package org.bensam.experimental.block;

import org.bensam.experimental.block.counter.BlockCounter;
import org.bensam.experimental.block.mobdetector.BlockMobDetector;
import org.bensam.experimental.block.pedestal.BlockPedestal;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author Will
 *
 */
public class ModBlocks
{
    public static BlockOre copperOre = new BlockOre("ore_copper", "oreCopper");
    public static BlockCropCorn cornCrop = new BlockCropCorn();
    public static BlockCounter counter = new BlockCounter();
    public static BlockMobDetector mobDetector = new BlockMobDetector();
    public static BlockPedestal pedestal = new BlockPedestal();
    
    public static void register(IForgeRegistry<Block> registry)
    {
        registry.registerAll(
                copperOre, 
                cornCrop,
                counter,
                mobDetector,
                pedestal);
        
        GameRegistry.registerTileEntity(counter.getTileEntityClass(), counter.getRegistryName());
        GameRegistry.registerTileEntity(mobDetector.getTileEntityClass(), mobDetector.getRegistryName());
        GameRegistry.registerTileEntity(pedestal.getTileEntityClass(), pedestal.getRegistryName());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry)
    {
        registry.registerAll(
                copperOre.createItemBlock(),
                counter.createItemBlock(),
                mobDetector.createItemBlock(),
                pedestal.createItemBlock());
    }
    
    public static void registerModels()
    {
        copperOre.registerItemModel(Item.getItemFromBlock(copperOre));
        counter.registerItemModel(Item.getItemFromBlock(counter));
        mobDetector.registerItemModel(Item.getItemFromBlock(mobDetector));
        pedestal.registerItemModel(Item.getItemFromBlock(pedestal));
    }
    
}
