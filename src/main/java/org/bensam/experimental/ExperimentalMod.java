package org.bensam.experimental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bensam.experimental.block.ModBlocks;
import org.bensam.experimental.capability.teleportation.CommandTeleportation;
import org.bensam.experimental.capability.teleportation.TeleportationHandlerCapabilityProvider;
import org.bensam.experimental.entity.EntityTeleportationSplashPotion;
import org.bensam.experimental.entity.EntityTeleportationTippedArrow;
import org.bensam.experimental.item.ModItems;
import org.bensam.experimental.material.ModMaterials;
import org.bensam.experimental.network.PacketRequestUpdatePedestal;
import org.bensam.experimental.network.PacketRequestUpdateTeleportBeacon;
import org.bensam.experimental.network.PacketUpdatePedestal;
import org.bensam.experimental.network.PacketUpdateTeleportBeacon;
import org.bensam.experimental.potion.ModPotions;
import org.bensam.experimental.proxy.IProxy;
import org.bensam.experimental.recipe.ModRecipes;
import org.bensam.experimental.util.ModConfig;
import org.bensam.experimental.util.ModGuiHandler;
import org.bensam.experimental.util.ModUtil;
import org.bensam.experimental.world.ModWorldGenerator;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author WilliHay
 * 
 */
@Mod(
        modid = ExperimentalMod.MODID, 
        name = ExperimentalMod.NAME, 
        version = ExperimentalMod.VERSION,
        acceptedMinecraftVersions = ExperimentalMod.ACCEPTED_MINECRAFT_VERSIONS,
        dependencies = ExperimentalMod.DEPENDENCIES)
public class ExperimentalMod
{
    public static final String MODID = "experimental";
    public static final String NAME = "Experimental Mod";
    public static final String VERSION = "@VERSION@";
    public static final String ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2]";
    public static final String DEPENDENCIES = "" +
            "required-after:minecraft;" +
            "required-after:forge@[14.23.5.2768,);" +
            "";
    
    @SidedProxy(clientSide = "org.bensam.experimental.proxy.ClientProxy", serverSide = "org.bensam.experimental.proxy.ServerProxy")
    public static IProxy proxy; // proxies help run code on the right side (client or server)

    @Instance(MODID)
    public static ExperimentalMod instance; // needed for GUIs and entities; set by FML 

    public static final CreativeTab CREATIVE_TAB = new CreativeTab();
    public static final Logger MOD_LOGGER = LogManager.getLogger(MODID);
    public static SimpleNetworkWrapper network;
    private static int networkPacketID;

    /**
     * Read your config and register anything else that doesn't have its own FML event (e.g. world gen, networking, loot tables).
     */
    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
        
        ModConfig.initConfig(event.getModConfigurationDirectory());
        ModConfig.readConfig();
        // TODO: change copper sword attack damage based on config value

        // Second parameter to registerWorldGenerator() is the 'weight' to assign to this generator.
        // Heavy weights tend to sink to the bottom of the list of world generators (i.e. they run later).
        // If you’re experiencing issues with other mods interfering with your world generation, you may want to change this.
        GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 3);

        // Register our GUI handler with Forge.
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());

        // Register the teleportation capability, to make it available for injection.
        TeleportationHandlerCapabilityProvider.registerCapability();

        // Setup network channel and register our messages with the side on which it is received.
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(new PacketUpdatePedestal.Handler(), PacketUpdatePedestal.class, networkPacketID++, Side.CLIENT);
        network.registerMessage(new PacketRequestUpdatePedestal.Handler(), PacketRequestUpdatePedestal.class, networkPacketID++,
                Side.SERVER);
        network.registerMessage(new PacketUpdateTeleportBeacon.Handler(), PacketUpdateTeleportBeacon.class, networkPacketID++, Side.CLIENT);
        network.registerMessage(new PacketRequestUpdateTeleportBeacon.Handler(), PacketRequestUpdateTeleportBeacon.class, networkPacketID++,
                Side.SERVER);

        // Register miscellaneous loot tables.
        LootTableList.register(new ResourceLocation(MODID, "chests/spawn_bonus_chest"));
    }

    /**
     * Register recipes, things that depend on preInit from other mods (e.g. recipes, advancements), send FMLInterModComms messages to other mods.
     */
    @EventHandler
    public void onInit(FMLInitializationEvent event)
    {
        proxy.init(event);

        // Register ore dictionaries.
        ModBlocks.registerOreDictionaryEntries();
        ModItems.registerOreDictionaryEntries();

        // Register miscellaneous crafting recipes and smelting recipes.
        ModRecipes.register();
        
        // Register dispenser behaviors.
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.TELEPORTATION_SPLASH_POTION, new IBehaviorDispenseItem()
        {
            /**
             * Dispenses the specified ItemStack from a dispenser.
             */
            public ItemStack dispense(IBlockSource source, final ItemStack stack)
            {
                return (new BehaviorProjectileDispense()
                {
                    /**
                     * Return the projectile entity spawned by this dispense behavior.
                     */
                    protected IProjectile getProjectileEntity(World world, IPosition position, ItemStack stack)
                    {
                        return new EntityTeleportationSplashPotion(world, position.getX(), position.getY(), position.getZ(), source);
                    }
                    protected float getProjectileInaccuracy()
                    {
                        return super.getProjectileInaccuracy() * 0.5F;
                    }
                    protected float getProjectileVelocity()
                    {
                        return super.getProjectileVelocity() * 1.25F;
                    }
                }).dispense(source, stack);
            }
        });
        
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.TELEPORTATION_TIPPED_ARROW, new IBehaviorDispenseItem()
        {
            /**
             * Dispenses the specified ItemStack from a dispenser.
             */
            public ItemStack dispense(IBlockSource source, final ItemStack stack)
            {
                return (new BehaviorProjectileDispense()
                {
                    /**
                     * Return the projectile entity spawned by this dispense behavior.
                     */
                    protected IProjectile getProjectileEntity(World world, IPosition position, ItemStack stack)
                    {
                        EntityTeleportationTippedArrow entityArrow = new EntityTeleportationTippedArrow(world, position.getX(), position.getY(), position.getZ(), source);
                        entityArrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                        return entityArrow;
                    }
                }).dispense(source, stack);
            }
        });

        // Miscellaneous debug output...
        // [STDOUT]: [org.bensam.experimental.ExperimentalMod:init:121]: APPLE ITEM >> minecraft:apple
        System.out.println("APPLE ITEM >> " + Items.APPLE.getRegistryName());

        // [experimental]: STONE BLOCK >> minecraft:stone
        MOD_LOGGER.info("STONE BLOCK >> {}", Blocks.STONE.getRegistryName());

        // [experimental]: COUNTER TILEENTITY >> experimental:counter
        MOD_LOGGER.info("COUNTER TILEENTITY >> {}", ModBlocks.COUNTER.getRegistryName());
        MOD_LOGGER.info("TELEPORTATION_POTION POTION ITEM >> {}", ModPotions.TELEPORTATION_POTION.getRegistryName());
        MOD_LOGGER.info("Teleport Beacon translation key: {}", ModBlocks.TELEPORT_BEACON.getTranslationKey());
        MOD_LOGGER.info("Random letters: {} {} {}", ModUtil.getRandomLetter(), ModUtil.getRandomLetter(), ModUtil.getRandomLetter());
        
        MOD_LOGGER.info("IRON SWORD attack damage: {}",
                ((net.minecraft.item.ItemSword) Items.IRON_SWORD).getAttackDamage());
        MOD_LOGGER.info("COPPER SWORD attack damage: {}", ModItems.COPPER_SWORD.getAttackDamage());
        MOD_LOGGER.info("COPPER MATERIAL attack damage: {}", ModMaterials.COPPER_TOOL_MATERIAL.getAttackDamage());
        MOD_LOGGER.info("copperSwordAttackDamage: {}", ModConfig.copperSwordAttackDamage);

        MOD_LOGGER.info("Carrot Heal: {}; Saturation: {}", ((ItemFood) Items.CARROT).getHealAmount(null),
                ((ItemFood) Items.CARROT).getSaturationModifier(null));
        MOD_LOGGER.info("Rotten Flesh Heal: {}; Saturation: {}", ((ItemFood) Items.ROTTEN_FLESH).getHealAmount(null),
                ((ItemFood) Items.ROTTEN_FLESH).getSaturationModifier(null));
        MOD_LOGGER.info("Steak Heal: {}; Saturation: {}", ((ItemFood) Items.COOKED_BEEF).getHealAmount(null),
                ((ItemFood) Items.COOKED_BEEF).getSaturationModifier(null));
        MOD_LOGGER.info("Corn Heal: {}; Saturation: {}", ModItems.CORN.getHealAmount(null),
                ModItems.CORN.getSaturationModifier(null));
        MOD_LOGGER.info("Corn Bread Heal: {}; Saturation: {}", ModItems.CORN_BREAD.getHealAmount(null),
                ModItems.CORN_BREAD.getSaturationModifier(null));
    }

    /**
     * Handle interaction with other mods. You can check which ones are loaded here.
     */
    @EventHandler
    public void onPostInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
        
        if (ModConfig.config.hasChanged())
        {
            ModConfig.config.save();
        }
    }
    
    /**
     * Called after FMLServerAboutToStartEvent and before FMLServerStartedEvent.
     * This event allows for customizations of the server, such as loading custom commands, perhaps customizing recipes or other activities.
     */
    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandTeleportation());
    }
}
