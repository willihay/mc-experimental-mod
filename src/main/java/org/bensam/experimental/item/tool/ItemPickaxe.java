/**
 * 
 */
package org.bensam.experimental.item.tool;

import org.bensam.experimental.ExperimentalMod;

/**
 * @author Will
 *
 */
public class ItemPickaxe extends net.minecraft.item.ItemPickaxe
{
    private String name;
    
    public ItemPickaxe(ToolMaterial material, String name)
    {
        super(material);
        
        setRegistryName(name);
        setUnlocalizedName(name);
        this.name = name;
    }

    public void registerItemModel()
    {
        ExperimentalMod.proxy.registerItemRenderer(this, 0, name);
    }
}
