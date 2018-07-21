/**
 * 
 */
package org.bensam.experimental.item;

import org.bensam.experimental.ExperimentalMod;

import net.minecraft.item.ItemFood;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author Will
 *
 */
public class ItemFoodBase extends ItemFood
{
    private String name;
    private String oreName;
    
    public ItemFoodBase(String name, String oreName, int amount, float saturation, boolean isWolfFood)
    {
        super(amount, saturation, isWolfFood);
        
        this.name = name;
        setUnlocalizedName(name); // used for translating the name of the item into the currently active language
        setRegistryName(name); // used when registering our item with Forge
        
        this.oreName = oreName;
        setCreativeTab(ExperimentalMod.creativeTab);
    }
    
    public void registerItemModel()
    {
        ExperimentalMod.proxy.registerItemRenderer(this, 0, name);
    }

    public void initOreDictionary()
    {
        OreDictionary.registerOre(oreName, this);
    }

}
