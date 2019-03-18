/**
 * CreativeTab - provides a new tab in Creative mode
 */
package org.bensam.experimental;

import org.bensam.experimental.item.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author WilliHay
 *
 */
public class CreativeTab extends CreativeTabs
{

    public CreativeTab()
    {
        super(ExperimentalMod.MODID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack createIcon()
    {
        return new ItemStack(ModItems.COPPER_INGOT);
    }
}
