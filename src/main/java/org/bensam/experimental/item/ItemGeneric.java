package org.bensam.experimental.item;

import javax.annotation.Nonnull;

import org.bensam.experimental.util.ModSetup;

import net.minecraft.item.Item;

/**
 * @author WilliHay
 *
 */
public class ItemGeneric extends Item
{

    public ItemGeneric(@Nonnull String name, boolean includeInCreativeTab)
    {
        ModSetup.setRegistryNames(this, name);
        
        if (includeInCreativeTab)
        {
            ModSetup.setCreativeTab(this);
        }
    }
}
