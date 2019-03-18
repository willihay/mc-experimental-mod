package org.bensam.experimental.item.tool;

import javax.annotation.Nonnull;

import org.bensam.experimental.util.ModSetup;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSpade;

/**
 * @author WilliHay
 *
 */
public class ItemModShovel extends ItemSpade
{

    public ItemModShovel(@Nonnull String name, ToolMaterial material)
    {
        super(material);

        ModSetup.setRegistryNames(this, name);
        ModSetup.setCreativeTab(this, CreativeTabs.TOOLS);
    }
}
