/**
 * BlockBase - convenience class for adding basic blocks
 */
package org.bensam.experimental.block;

import org.bensam.experimental.ExperimentalMod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

/**
 * @author Will
 *
 */
public class BlockBase extends Block
{
    protected String name;
    
    public BlockBase(Material material, String name)
    {
        super(material);
        
        this.name = name;
        setUnlocalizedName(name); // used for translating the name of the block into the currently active language
        setRegistryName(name); // used when registering our block with Forge
        
        setCreativeTab(ExperimentalMod.creativeTab);
    }
    
    public void registerItemModel(Item itemBlock)
    {
        ExperimentalMod.proxy.registerItemRenderer(itemBlock, 0, name);
    }
    
    public Item createItemBlock()
    {
        return new ItemBlock(this).setRegistryName(getRegistryName()); // ItemBlock is the form of a block in your inventory
    }

    @Override
    public BlockBase setCreativeTab(CreativeTabs tab)
    {
        super.setCreativeTab(tab);
        return this; // returns BlockBase instead of Block so we can use it in our register method without casting
    }
    
}
