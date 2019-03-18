package org.bensam.experimental.item;

import java.util.Arrays;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.item.tool.ItemModAxe;
import org.bensam.experimental.item.tool.ItemModHoe;
import org.bensam.experimental.item.tool.ItemModPickaxe;
import org.bensam.experimental.item.tool.ItemModShovel;
import org.bensam.experimental.item.tool.ItemModSword;
import org.bensam.experimental.material.ModMaterials;

import com.google.common.base.Preconditions;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author WilliHay
 * Thanks to Cadiboo for the registration code examples!
 *
 */
@ObjectHolder(ExperimentalMod.MODID)
public class ModItems
{
    // Ingots
    public static final ItemIngot COPPER_INGOT = null;

    // Tools
    public static final ItemModAxe COPPER_AXE = null;
    public static final ItemModHoe COPPER_HOE = null;
    public static final ItemModPickaxe COPPER_PICKAXE = null;
    public static final ItemModShovel COPPER_SHOVEL = null;
    public static final ItemModSword COPPER_SWORD = null;

    // Armor
    public static final ItemModArmor COPPER_BOOTS = null;
    public static final ItemModArmor COPPER_CHESTPLATE = null;
    public static final ItemModArmor COPPER_HELMET = null;
    public static final ItemModArmor COPPER_LEGGINGS = null;

    // Food & Seeds
    public static final ItemModFood CORN = null;
    public static final ItemModFood CORN_BREAD = null;
    public static final ItemCornSeed CORN_SEED = null;
    public static final ItemModFood CREAMED_CORN_SOUP = null;

    // Teleportation Capability
    public static final ItemGeneric ENDER_EYE_SHARD = null;
    public static final ItemGeneric ENDER_EYE_TRANSLUCENT = null;
    public static final ItemTeleportationWand TELEPORTATION_WAND = null;
    public static final ItemTeleportationSplashPotion TELEPORTATION_SPLASH_POTION = null;
    public static final ItemTeleportationTippedArrow TELEPORTATION_TIPPED_ARROW = null;
    
    // @formatter:off
    public static void register(IForgeRegistry<Item> registry)
    {
        // Ingots
        registry.register(new ItemIngot("copper_ingot"));
        
        // Tools
        registry.register(new ItemModAxe("copper_axe", ModMaterials.COPPER_TOOL_MATERIAL));
        registry.register(new ItemModHoe("copper_hoe", ModMaterials.COPPER_TOOL_MATERIAL));
        registry.register(new ItemModPickaxe("copper_pickaxe", ModMaterials.COPPER_TOOL_MATERIAL));
        registry.register(new ItemModShovel("copper_shovel", ModMaterials.COPPER_TOOL_MATERIAL));
        registry.register(new ItemModSword("copper_sword", ModMaterials.COPPER_TOOL_MATERIAL));
        
        // Armor
        registry.register(new ItemModArmor("copper_boots", ModMaterials.COPPER_ARMOR_MATERIAL, EntityEquipmentSlot.FEET));
        registry.register(new ItemModArmor("copper_chestplate", ModMaterials.COPPER_ARMOR_MATERIAL, EntityEquipmentSlot.CHEST));
        registry.register(new ItemModArmor("copper_helmet", ModMaterials.COPPER_ARMOR_MATERIAL, EntityEquipmentSlot.HEAD));
        registry.register(new ItemModArmor("copper_leggings", ModMaterials.COPPER_ARMOR_MATERIAL, EntityEquipmentSlot.LEGS));
        
        // Food & Seeds
        registry.register(new ItemModFood("corn", 3, 0.6f, true));
        registry.register(new ItemModFood("corn_bread", 5, 0.7f, false));
        registry.register(new ItemCornSeed("corn_seed"));
        registry.register(new ItemModFood("creamed_corn_soup", 6, 0.7f, false));

        // Teleportation Capability
        registry.register(new ItemGeneric("ender_eye_shard", true));
        registry.register(new ItemGeneric("ender_eye_translucent", false));
        registry.register(new ItemTeleportationWand("teleportation_wand"));
        registry.register(new ItemTeleportationSplashPotion("teleportation_splash_potion"));
        registry.register(new ItemTeleportationTippedArrow("teleportation_tipped_arrow"));
    }

    public static void registerItemModels()
    {
        Arrays.stream(new Item[]
                {
                    COPPER_INGOT, // ingots
                    COPPER_AXE, // tools
                    COPPER_HOE,
                    COPPER_PICKAXE,
                    COPPER_SHOVEL,
                    COPPER_SWORD,
                    COPPER_BOOTS, // armor
                    COPPER_CHESTPLATE,
                    COPPER_HELMET,
                    COPPER_LEGGINGS,
                    CORN, // food & seeds
                    CORN_BREAD,
                    CORN_SEED,
                    CREAMED_CORN_SOUP,
                    ENDER_EYE_SHARD, // teleportation capability
                    ENDER_EYE_TRANSLUCENT,
                    TELEPORTATION_WAND,
                    TELEPORTATION_SPLASH_POTION,
                    TELEPORTATION_TIPPED_ARROW
                }).forEach(item -> 
                {
                    Preconditions.checkNotNull(item, "Item cannot be null!");
                    ModelLoader.setCustomModelResourceLocation(item, 0, 
                            new ModelResourceLocation(item.getRegistryName(), "inventory"));
                });
    }
    // @formatter:on
    
    public static void registerOreDictionaryEntries()
    {
        OreDictionary.registerOre("ingotCopper", COPPER_INGOT);
        OreDictionary.registerOre("cropCorn", CORN);
    }
}
