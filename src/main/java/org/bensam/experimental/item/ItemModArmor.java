package org.bensam.experimental.item;

import javax.annotation.Nonnull;

import org.bensam.experimental.util.ModSetup;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

/**
 * @author WilliHay
 *
 */
public class ItemModArmor extends ItemArmor
{

    public ItemModArmor(@Nonnull String name, ArmorMaterial material, EntityEquipmentSlot equipmentSlot)
    {
        super(material, 0, equipmentSlot);

        ModSetup.setRegistryNames(this, name);
        ModSetup.setCreativeTab(this, CreativeTabs.COMBAT);
    }
}
