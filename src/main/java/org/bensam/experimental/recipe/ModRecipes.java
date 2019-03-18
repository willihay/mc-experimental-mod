package org.bensam.experimental.recipe;

import org.bensam.experimental.block.ModBlocks;
import org.bensam.experimental.item.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author WilliHay
 *
 */
public class ModRecipes
{
    public static void register()
    {
        //        GameRegistry.addShapedRecipe(Items.APPLE.getRegistryName(), null, new ItemStack(Items.APPLE),
        //                "XXX",
        //                "XYX",
        //                "XXX",
        //                'X', Blocks.DIRT,
        //                'Y', Items.WHEAT_SEEDS);

        GameRegistry.addSmelting(ModBlocks.COPPER_ORE, new ItemStack(ModItems.COPPER_INGOT), 0.7f);
    }
}
