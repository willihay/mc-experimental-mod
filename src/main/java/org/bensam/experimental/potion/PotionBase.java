/**
 * 
 */
package org.bensam.experimental.potion;

import org.bensam.experimental.ExperimentalMod;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

/**
 * @author Will
 *
 */
public class PotionBase extends Potion
{
    private boolean isInstant;

    public PotionBase(String name, boolean isInstant, boolean isBadEffect, int liquidColor, int iconIndexX,
            int iconIndexY)
    {
        super(isBadEffect, liquidColor);
        this.isInstant = isInstant;
        setPotionName("effect." + name);
        setIconIndex(iconIndexX, iconIndexY);
        setRegistryName(new ResourceLocation(ExperimentalMod.MODID + ":" + name));
    }

    @Override
    public boolean hasStatusIcon()
    {
        if (isInstant)
            return false;
        else
        {
            Minecraft.getMinecraft().getTextureManager()
                    .bindTexture(new ResourceLocation(ExperimentalMod.MODID, "textures/gui/potion_effects.png"));
            return true;
        }
    }

    @Override
    public boolean isInstant()
    {
        return isInstant;
    }
}
