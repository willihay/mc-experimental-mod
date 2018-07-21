/**
 * CreativeTab - provides a new tab in Creative mode
 */
package org.bensam.experimental.client;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.item.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * @author Will
 *
 */
public class CreativeTab extends CreativeTabs
{

    public CreativeTab()
    {
        super(ExperimentalMod.MODID);
    }
    
    @Override
    public ItemStack getTabIconItem()
    {
        return new ItemStack(ModItems.copperIngot);
    }

}
