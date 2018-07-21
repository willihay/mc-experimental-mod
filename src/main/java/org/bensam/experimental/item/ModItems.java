/**
 * ModItems - contains instances of all our items. Items are singletons.
 */
package org.bensam.experimental.item;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author Will
 *
 */
public class ModItems
{
    public static ItemOre copperIngot;
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
        registry.registerAll(copperIngot, corn, cornBread, cornSeed, creamedCornSoup);
    }

    public static void registerModels()
    {
        copperIngot.registerItemModel();
        corn.registerItemModel();
        cornBread.registerItemModel();
        cornSeed.registerItemModel();
        creamedCornSoup.registerItemModel();
    }
}
