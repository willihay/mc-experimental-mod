/**
 * BlockOre - convenience class for adding ores
 */
package org.bensam.experimental.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author Will
 *
 */
public class BlockOre extends BlockBase
{
    private String oreName;
    
    public BlockOre(String name, String oreName)
    {
        super(Material.ROCK, name);
        
        this.oreName = oreName;
        
        setHardness(3f);
        setResistance(5f);
    }

    public void initOreDictionary()
    {
        OreDictionary.registerOre(oreName, this);
    }

    @Override
    public BlockOre setCreativeTab(CreativeTabs tab)
    {
        super.setCreativeTab(tab);
        return this;
    }

}
