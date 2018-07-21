/**
 * ItemOre - convenience class for ore items that will be added to the Forge Ore Dictionary
 */
package org.bensam.experimental.item;

import net.minecraftforge.oredict.OreDictionary;

/**
 * @author Will
 *
 */
public class ItemOre extends ItemBase
{
    private String oreName;
    
    public ItemOre(String name, String oreName)
    {
        super(name);
        
        this.oreName = oreName;
    }

    public void initOreDictionary()
    {
        OreDictionary.registerOre(oreName, this);
    }

}
