/**
 * 
 */
package org.bensam.experimental.block;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.item.ModItems;

import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;

/**
 * @author Will
 *
 */
public class BlockCropCorn extends BlockCrops
{
    public BlockCropCorn()
    {
        setUnlocalizedName("crop_corn");
        setRegistryName("crop_corn");
        
        setCreativeTab(ExperimentalMod.creativeTab);
    }

    @Override
    protected Item getSeed()
    {
        return ModItems.cornSeed;
    }

    @Override
    protected Item getCrop()
    {
        return ModItems.corn;
    }
    
}
