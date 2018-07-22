/**
 * ModItems - contains instances of all our items. Items are singletons.
 */
package org.bensam.experimental.item;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.item.tool.*;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author Will
 *
 */
public class ModItems
{
    // Ingots
    public static ItemOre copperIngot;
    
    // Tools
    public static ItemAxe copperAxe = new ItemAxe(ExperimentalMod.copperToolMaterial, "copper_axe");
    public static ItemHoe copperHoe = new ItemHoe(ExperimentalMod.copperToolMaterial, "copper_hoe");
    public static ItemPickaxe copperPickaxe = new ItemPickaxe(ExperimentalMod.copperToolMaterial, "copper_pickaxe");
    public static ItemShovel copperShovel = new ItemShovel(ExperimentalMod.copperToolMaterial, "copper_shovel");
    public static ItemSword copperSword = new ItemSword(ExperimentalMod.copperToolMaterial, "copper_sword");
    
    // Armor
    public static ItemArmor copperBoots = new ItemArmor(ExperimentalMod.copperArmorMaterial, EntityEquipmentSlot.FEET, "copper_boots");
    public static ItemArmor copperChestplate = new ItemArmor(ExperimentalMod.copperArmorMaterial, EntityEquipmentSlot.CHEST, "copper_chestplate");
    public static ItemArmor copperHelmet = new ItemArmor(ExperimentalMod.copperArmorMaterial, EntityEquipmentSlot.HEAD, "copper_helmet");
    public static ItemArmor copperLeggings = new ItemArmor(ExperimentalMod.copperArmorMaterial, EntityEquipmentSlot.LEGS, "copper_leggings");
    
    // Food & Seeds
    public static ItemFoodBase corn;
    public static ItemFoodBase cornBread;
    public static ItemCornSeed cornSeed;
    public static ItemFoodBase creamedCornSoup;
    
    public static void preInit()
    {
        copperIngot = new ItemOre("ingot_copper", "ingotCopper");
        corn = new ItemFoodBase("corn", "cropCorn", 3, 0.6f, false);
        cornBread = new ItemFoodBase("corn_bread", "", 5, 0.7f, false);
        cornSeed = new ItemCornSeed();
        creamedCornSoup = new ItemFoodBase("creamed_corn_soup", "", 6, 0.7f, false);
    }
    
    public static void register(IForgeRegistry<Item> registry)
    {
        registry.registerAll(
                copperIngot, // ingots
                copperAxe, // tools
                copperHoe,
                copperPickaxe,
                copperShovel,
                copperSword,
                copperBoots, // armor
                copperChestplate,
                copperHelmet,
                copperLeggings,
                corn, // food
                cornBread, 
                cornSeed, 
                creamedCornSoup);
    }

    public static void registerModels()
    {
        // Ingots
        copperIngot.registerItemModel();
        
        // Tools
        copperAxe.registerItemModel();
        copperHoe.registerItemModel();
        copperPickaxe.registerItemModel();
        copperShovel.registerItemModel();
        copperSword.registerItemModel();
        
        // Armor
        copperBoots.registerItemModel();
        copperChestplate.registerItemModel();
        copperHelmet.registerItemModel();
        copperLeggings.registerItemModel();
        
        // Food & Seeds
        corn.registerItemModel();
        cornBread.registerItemModel();
        cornSeed.registerItemModel();
        creamedCornSoup.registerItemModel();
    }
}
