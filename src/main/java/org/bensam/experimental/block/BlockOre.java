package org.bensam.experimental.block;

import javax.annotation.Nonnull;

import org.bensam.experimental.util.ModSetup;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @author WilliHay
 *
 */
public class BlockOre extends Block
{

    public BlockOre(@Nonnull String name)
    {
        super(Material.ROCK);
        
        ModSetup.setRegistryNames(this, name);
        setHardness(3f);
        setResistance(5f);
    }
}
