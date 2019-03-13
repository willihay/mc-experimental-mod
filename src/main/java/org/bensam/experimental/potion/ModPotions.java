package org.bensam.experimental.potion;

import org.bensam.experimental.item.ModItems;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * @author Will
 *
 */
public class ModPotions
{
    public static final Potion TELEPORTATION = new PotionTeleportation();

    public static void registerPotions()
    {
        ForgeRegistries.POTIONS.register(TELEPORTATION);
        
        ItemStack inputPotion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD);
        ItemStack ingredient = new ItemStack(ModItems.enderEyeShard);
        ItemStack outputPotion = PotionUtils.addPotionToItemStack(new ItemStack(ModItems.teleportationSplashPotion), PotionTypes.EMPTY);
        BrewingRecipeRegistry.addRecipe(new CustomBrewingRecipe(inputPotion, ingredient, outputPotion));
    }
}

class CustomBrewingRecipe extends BrewingRecipe
{
    public CustomBrewingRecipe(ItemStack input, ItemStack ingredient, ItemStack output)
    {
        super(input, ingredient, output);
    }

    @Override
    public boolean isInput(ItemStack stack)
    {
        // We only want to match input potions that have the PotionType specified in the brewing recipe. For potions, that means comparing NBT data.
        return ItemStack.areItemStacksEqualUsingNBTShareTag(stack, getInput());
    }
}
