package org.bensam.experimental.item.tool;

import javax.annotation.Nonnull;

import org.bensam.experimental.util.ModSetup;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;

/**
 * @author WilliHay
 *
 */
public class ItemModSword extends ItemSword
{

    public ItemModSword(@Nonnull String name, ToolMaterial material)
    {
        super(material);

        ModSetup.setRegistryNames(this, name);
        ModSetup.setCreativeTab(this, CreativeTabs.COMBAT);
    }
}
