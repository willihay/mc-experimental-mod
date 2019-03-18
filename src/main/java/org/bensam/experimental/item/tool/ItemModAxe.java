package org.bensam.experimental.item.tool;

import javax.annotation.Nonnull;

import org.bensam.experimental.util.ModSetup;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;

/**
 * @author WilliHay
 *
 */
public class ItemModAxe extends ItemAxe
{

    public ItemModAxe(@Nonnull String name, ToolMaterial material)
    {
        super(material, 8f, -3.1f);

        ModSetup.setRegistryNames(this, name);
        ModSetup.setCreativeTab(this, CreativeTabs.TOOLS);
    }
}
