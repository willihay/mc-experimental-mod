package org.bensam.experimental.item;

import javax.annotation.Nonnull;

import org.bensam.experimental.block.ModBlocks;
import org.bensam.experimental.util.ModSetup;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;

/**
 * @author WilliHay
 *
 */
public class ItemCornSeed extends ItemSeeds
{
    public ItemCornSeed(@Nonnull String name)
    {
        super(ModBlocks.CORN_CROP, Blocks.FARMLAND); // ItemSeeds takes a crop block and a soil block

        ModSetup.setRegistryNames(this, name);
        ModSetup.setCreativeTab(this);
    }
}
