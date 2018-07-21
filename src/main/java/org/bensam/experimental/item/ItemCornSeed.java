/**
 * 
 */
package org.bensam.experimental.item;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.block.ModBlocks;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;

/**
 * @author Will
 *
 */
public class ItemCornSeed extends ItemSeeds
{
    public ItemCornSeed()
    {
        super(ModBlocks.cornCrop, Blocks.FARMLAND); // ItemSeeds takes a crop block and a soil block
        setUnlocalizedName("corn_seed");
        setRegistryName("corn_seed");
        
        setCreativeTab(ExperimentalMod.creativeTab);
    }
    
    public void registerItemModel()
    {
        ExperimentalMod.proxy.registerItemRenderer(this, 0, "corn_seed");
    }
}
