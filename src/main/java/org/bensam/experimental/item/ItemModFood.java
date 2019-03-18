package org.bensam.experimental.item;

import javax.annotation.Nonnull;

import org.bensam.experimental.util.ModSetup;

import net.minecraft.item.ItemFood;

/**
 * @author WilliHay
 *
 */
public class ItemModFood extends ItemFood
{

    public ItemModFood(@Nonnull String name, int amount, float saturation, boolean isWolfFood)
    {
        super(amount, saturation, isWolfFood);

        ModSetup.setRegistryNames(this, name);
        ModSetup.setCreativeTab(this);
    }
}
