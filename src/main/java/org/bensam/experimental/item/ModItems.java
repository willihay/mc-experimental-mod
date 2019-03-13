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
    public static ItemOre copperIngot = new ItemOre("ingot_copper", "ingotCopper");

    // Tools
    public static ItemAxe copperAxe = new ItemAxe(ExperimentalMod.copperToolMaterial, "copper_axe");
    public static ItemHoe copperHoe = new ItemHoe(ExperimentalMod.copperToolMaterial, "copper_hoe");
    public static ItemPickaxe copperPickaxe = new ItemPickaxe(ExperimentalMod.copperToolMaterial, "copper_pickaxe");
    public static ItemShovel copperShovel = new ItemShovel(ExperimentalMod.copperToolMaterial, "copper_shovel");
    public static ItemSword copperSword = new ItemSword(ExperimentalMod.copperToolMaterial, "copper_sword");

    // Armor
    public static ItemArmor copperBoots = new ItemArmor(ExperimentalMod.copperArmorMaterial, EntityEquipmentSlot.FEET,
            "copper_boots");
    public static ItemArmor copperChestplate = new ItemArmor(ExperimentalMod.copperArmorMaterial,
            EntityEquipmentSlot.CHEST, "copper_chestplate");
    public static ItemArmor copperHelmet = new ItemArmor(ExperimentalMod.copperArmorMaterial, EntityEquipmentSlot.HEAD,
            "copper_helmet");
    public static ItemArmor copperLeggings = new ItemArmor(ExperimentalMod.copperArmorMaterial,
            EntityEquipmentSlot.LEGS, "copper_leggings");

    // Food & Seeds
    public static ItemFoodBase corn = new ItemFoodBase("corn", "cropCorn", 3, 0.6f, false);
    public static ItemFoodBase cornBread = new ItemFoodBase("corn_bread", "", 5, 0.7f, false);
    public static ItemCornSeed cornSeed = new ItemCornSeed();
    public static ItemFoodBase creamedCornSoup = new ItemFoodBase("creamed_corn_soup", "", 6, 0.7f, false);

    // Teleportation Capability
    public static ItemBase enderEyeShard = new ItemBase("ender_eye_shard");
    public static ItemBase enderEyeTranslucent = new ItemBase("ender_eye_translucent");
    public static ItemTeleportationWand teleportationWand = new ItemTeleportationWand("teleportation_wand");
    public static ItemTeleportationSplashPotion teleportationSplashPotion = new ItemTeleportationSplashPotion("teleportation_splash_potion");
    public static ItemTeleportationTippedArrow teleportationTippedArrow = new ItemTeleportationTippedArrow();
    
    // @formatter:off
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
                creamedCornSoup,
                enderEyeShard, // teleportation capability
                enderEyeTranslucent,
                teleportationWand,
                teleportationSplashPotion,
                teleportationTippedArrow
                );
    }
    // @formatter:on

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

        // Teleportation Capability
        enderEyeShard.registerItemModel();
        enderEyeTranslucent.registerItemModel();
        teleportationWand.registerItemModel();
        teleportationSplashPotion.registerItemModel();
        teleportationTippedArrow.registerItemModel();
    }
}
