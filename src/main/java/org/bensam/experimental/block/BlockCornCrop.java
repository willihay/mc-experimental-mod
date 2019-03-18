package org.bensam.experimental.block;

import javax.annotation.Nonnull;

import org.bensam.experimental.item.ModItems;
import org.bensam.experimental.util.ModSetup;

import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;

/**
 * @author WilliHay
 *
 */
public class BlockCornCrop extends BlockCrops
{
    
    public BlockCornCrop(@Nonnull String name)
    {
        ModSetup.setRegistryNames(this, name);
    }

    @Override
    protected Item getSeed()
    {
        return ModItems.CORN_SEED;
    }

    @Override
    protected Item getCrop()
    {
        return ModItems.CORN;
    }
}
