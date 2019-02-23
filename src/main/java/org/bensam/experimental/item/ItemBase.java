/**
 * ItemBase - convenience class for adding basic items
 */
package org.bensam.experimental.item;

import org.bensam.experimental.ExperimentalMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author Will
 *
 */
public class ItemBase extends Item
{
    protected String name;

    public ItemBase(String name)
    {
        this.name = name;
        setTranslationKey(name); // used for translating the name of the item into the currently active language
        setRegistryName(name); // used when registering our item with Forge

        setCreativeTab(ExperimentalMod.creativeTab);
    }

    public void registerItemModel()
    {
        ExperimentalMod.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab)
    {
        super.setCreativeTab(tab);
        return this; // returns ItemBase instead of Item so we can use it in our register method without casting
    }

}
