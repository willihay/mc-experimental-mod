package org.bensam.experimental.item;

import javax.annotation.Nonnull;

import org.bensam.experimental.util.ModSetup;

import net.minecraft.item.Item;

/**
 * @author WilliHay
 *
 */
public class ItemIngot extends Item
{

    public ItemIngot(@Nonnull String name)
    {
        ModSetup.setRegistryNames(this, name);
        ModSetup.setCreativeTab(this);
    }
}
