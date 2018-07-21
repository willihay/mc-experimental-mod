/**
 * 
 */
package org.bensam.experimental.recipe;

import org.bensam.experimental.block.ModBlocks;
import org.bensam.experimental.item.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author Will
 *
 */
public class ModRecipes
{
    public static void init()
    {
        // ore dictionary registrations
        ModBlocks.copperOre.initOreDictionary();
        ModItems.copperIngot.initOreDictionary();
        ModItems.corn.initOreDictionary();
        
        GameRegistry.addSmelting(ModBlocks.copperOre, new ItemStack(ModItems.copperIngot), 0.7f);
        
//        GameRegistry.addShapedRecipe(Items.APPLE.getRegistryName(), null, new ItemStack(Items.APPLE),
//                "XXX",
//                "XYX",
//                "XXX",
//                'X', Blocks.DIRT,
//                'Y', Items.WHEAT_SEEDS);

    }
}
