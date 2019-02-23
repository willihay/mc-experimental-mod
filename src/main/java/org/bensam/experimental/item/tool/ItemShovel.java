/**
 * 
 */
package org.bensam.experimental.item.tool;

import org.bensam.experimental.ExperimentalMod;

import net.minecraft.item.ItemSpade;

/**
 * @author Will
 *
 */
public class ItemShovel extends ItemSpade
{
    private String name;

    public ItemShovel(ToolMaterial material, String name)
    {
        super(material);

        setRegistryName(name);
        setTranslationKey(name);
        this.name = name;
    }

    public void registerItemModel()
    {
        ExperimentalMod.proxy.registerItemRenderer(this, 0, name);
    }
}
