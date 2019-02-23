package org.bensam.experimental.item;

import java.util.List;

import javax.annotation.Nullable;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.entity.EntityTeleportationTippedArrow;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTeleportationTippedArrow extends ItemArrow
{
    protected final String name = "teleportation_tipped_arrow";
    
    public ItemTeleportationTippedArrow()
    {
        setTranslationKey(name); // used for translating the name of the item into the currently active language
        setRegistryName(name); // used when registering our item with Forge

        setCreativeTab(ExperimentalMod.creativeTab);
    }

    public void registerItemModel()
    {
        ExperimentalMod.proxy.registerItemRenderer(this, 0, name);
    }

    /**
     * Add custom lines of information to the mouseover description.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        tooltip.add(TextFormatting.DARK_GREEN + I18n.format("effect.teleportation_potion"));
    }

    @Override
    public EntityArrow createArrow(World world, ItemStack stack, EntityLivingBase shooter)
    {
        EntityTeleportationTippedArrow entityArrow = new EntityTeleportationTippedArrow(world, shooter);
        
        return entityArrow;
    }
}
