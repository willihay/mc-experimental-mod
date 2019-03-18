package org.bensam.experimental.material;

import org.bensam.experimental.ExperimentalMod;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

/**
 * @author WilliHay
 *
 */
public class ModMaterials
{
    // Item Materials
    public static final ResourceLocation COPPER_MATERIAL_NAME = new ResourceLocation(ExperimentalMod.MODID, "copper");
    public static final Item.ToolMaterial COPPER_TOOL_MATERIAL = EnumHelper.addToolMaterial(COPPER_MATERIAL_NAME.toString(), 2, 500, 6.0f, 2.0f, 14);
    public static final ItemArmor.ArmorMaterial COPPER_ARMOR_MATERIAL = EnumHelper.addArmorMaterial(COPPER_MATERIAL_NAME.toString(),
            COPPER_MATERIAL_NAME.toString(), 15, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0f);

}
