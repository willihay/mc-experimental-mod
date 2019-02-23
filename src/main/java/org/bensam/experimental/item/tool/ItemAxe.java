/**
 * 
 */
package org.bensam.experimental.item.tool;

import org.bensam.experimental.ExperimentalMod;

/**
 * @author Will
 *
 */
public class ItemAxe extends net.minecraft.item.ItemAxe
{
    private String name;

    public ItemAxe(ToolMaterial material, String name)
    {
        super(material, 8f, -3.1f);

        setRegistryName(name);
        setTranslationKey(name);
        this.name = name;
    }

    public void registerItemModel()
    {
        ExperimentalMod.proxy.registerItemRenderer(this, 0, name);
    }
}
