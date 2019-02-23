/**
 * 
 */
package org.bensam.experimental.item.tool;

import org.bensam.experimental.ExperimentalMod;

/**
 * @author Will
 *
 */
public class ItemHoe extends net.minecraft.item.ItemHoe
{
    private String name;

    public ItemHoe(ToolMaterial material, String name)
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
