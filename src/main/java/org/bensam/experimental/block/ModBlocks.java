/**
 * ModBlocks - contains instances of all our blocks. Blocks are singletons.
 */
package org.bensam.experimental.block;

import org.bensam.experimental.block.counter.BlockCounter;
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
    public static BlockOre copperOre;
    public static BlockCropCorn cornCrop;
    public static BlockPedestal pedestal;
    public static BlockCounter counter = new BlockCounter();
    
    public static void preInit()
    {
        copperOre = new BlockOre("ore_copper", "oreCopper");
        cornCrop = new BlockCropCorn();
        pedestal = new BlockPedestal();
    }
    
    public static void register(IForgeRegistry<Block> registry)
    {
        registry.registerAll(
                copperOre, 
                cornCrop, 
                pedestal,
                counter);
        
        GameRegistry.registerTileEntity(counter.getTileEntityClass(), counter.getRegistryName().toString());
        GameRegistry.registerTileEntity(pedestal.getTileEntityClass(), pedestal.getRegistryName().toString());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry)
    {
        registry.registerAll(
                copperOre.createItemBlock(),
                pedestal.createItemBlock(),
                counter.createItemBlock());
    }
    
    public static void registerModels()
    {
        copperOre.registerItemModel(Item.getItemFromBlock(copperOre));
        pedestal.registerItemModel(Item.getItemFromBlock(pedestal));
        counter.registerItemModel(Item.getItemFromBlock(counter));
    }
    
}
