package org.bensam.experimental.potion;

import org.bensam.experimental.item.ModItems;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
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
    public static final PotionType TELEPORTATION_POTION = new PotionType("teleportation_potion",
            new PotionEffect[] { new PotionEffect(TELEPORTATION) }).setRegistryName("teleportation_potion");

    public static void registerPotions()
    {
        // TODO: Now that we're not using the vanilla potions, can we remove the PotionType, registerPotion code, and the addMix call? (Document for future use, though.)
        registerPotion(TELEPORTATION_POTION, null, TELEPORTATION);

        registerPotionMixes();
        
        registerBrewingRecipes();
    }

    private static void registerPotion(PotionType defaultPotion, PotionType longPotion, Potion effect)
    {
        ForgeRegistries.POTIONS.register(effect);
        //ForgeRegistries.POTION_TYPES.register(defaultPotion);
        if (longPotion != null)
        {
            ForgeRegistries.POTION_TYPES.register(longPotion);
        }
    }

    private static void registerPotionMixes()
    {
        //PotionHelper.addMix(PotionTypes.AWKWARD, ModItems.enderEyeShard, TELEPORTATION_POTION);
    }
    
    private static void registerBrewingRecipes()
    {
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
