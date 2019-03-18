package org.bensam.experimental.item.tool;

import javax.annotation.Nonnull;

import org.bensam.experimental.util.ModSetup;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemPickaxe;

/**
 * @author WilliHay
 *
 */
public class ItemModPickaxe extends ItemPickaxe
{

    public ItemModPickaxe(@Nonnull String name, ToolMaterial material)
    {
        super(material);

        ModSetup.setRegistryNames(this, name);
        ModSetup.setCreativeTab(this, CreativeTabs.TOOLS);
    }
}
