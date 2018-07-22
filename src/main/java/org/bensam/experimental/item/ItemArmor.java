/**
 * 
 */
package org.bensam.experimental.item;

import org.bensam.experimental.ExperimentalMod;

import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * @author Will
 *
 */
public class ItemArmor extends net.minecraft.item.ItemArmor
{
    private String name;
    
    public ItemArmor(ArmorMaterial material, EntityEquipmentSlot equipmentSlot, String name)
    {
        super(material, 0, equipmentSlot);

        setRegistryName(name);
        setUnlocalizedName(name);
        this.name = name;
    }

    public void registerItemModel()
    {
        ExperimentalMod.proxy.registerItemRenderer(this, 0, name);
    }
}
