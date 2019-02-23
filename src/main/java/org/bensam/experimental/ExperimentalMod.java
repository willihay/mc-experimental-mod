package org.bensam.experimental;

import org.apache.logging.log4j.Logger;
import org.bensam.experimental.block.ModBlocks;
import org.bensam.experimental.capability.teleportation.CommandTeleportation;
import org.bensam.experimental.capability.teleportation.TeleportationHandlerCapabilityProvider;
import org.bensam.experimental.client.CreativeTab;
import org.bensam.experimental.entity.EntityTeleportationSplashPotion;
import org.bensam.experimental.entity.EntityTeleportationTippedArrow;
import org.bensam.experimental.entity.ModEntities;
import org.bensam.experimental.item.ModItems;
import org.bensam.experimental.network.PacketRequestUpdatePedestal;
import org.bensam.experimental.network.PacketRequestUpdateTeleportBeacon;
import org.bensam.experimental.network.PacketUpdatePedestal;
import org.bensam.experimental.network.PacketUpdateTeleportBeacon;
import org.bensam.experimental.potion.ModPotions;
import org.bensam.experimental.proxy.CommonProxy;
import org.bensam.experimental.recipe.ModRecipes;
import org.bensam.experimental.world.ModWorldGenerator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Note: requires Forge version 14.23.3.2694+ for use of ResourceLocation version of GameRegistry.registerTileEntity in ModBlocks.
 * @author Will
 * 
 */
@Mod(modid = ExperimentalMod.MODID, name = ExperimentalMod.NAME, version = ExperimentalMod.VERSION)
public class ExperimentalMod
{
    public static final String MODID = "experimental";
    public static final String NAME = "Experimental Mod";
    public static final String VERSION = "0.6.4";

    // TODO: Create an IProxy that both proxy classes implement, change CommonProxy to ServerProxy, then bring all truly common code into this class. (See CubiCoder tutorial for an example.)
    @SidedProxy(clientSide = "org.bensam.experimental.proxy.ClientProxy", serverSide = "org.bensam.experimental.proxy.CommonProxy")
    public static CommonProxy proxy; // proxies help run code on the right side (server or client)

    @Mod.Instance(MODID)
    public static ExperimentalMod instance; // needed for GUIs and entities; set by FML 

    public static Logger logger; // for printing debug messages to console

    public static final Item.ToolMaterial copperToolMaterial = EnumHelper.addToolMaterial("COPPER", 2, 500, 6.0f, 2.0f, 14);
    public static final ItemArmor.ArmorMaterial copperArmorMaterial = EnumHelper.addArmorMaterial("COPPER",
            MODID + ":copper", 15, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0f);

    public static CreativeTab creativeTab = new CreativeTab();

    public static SimpleNetworkWrapper network;
    private static int networkPacketID;

    /**
     * Signals to Forge that we want to listen to the main event bus, which allows mods to register/subscribe
     * handler methods to run when certain events occur. This class is concerned with registry events.
     */
    @Mod.EventBusSubscriber
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event)
        {
            ModBlocks.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerEntities(RegistryEvent.Register<EntityEntry> event)
        {
            ModEntities.registerEntities(event.getRegistry());
        }
        
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event)
        {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event)
        {
            ModItems.registerModels();
            ModBlocks.registerModels();
        }
    }

    /**
     * Run before anything else.
     * Typical uses: read your config, create blocks, items, etc, and register them with the GameRegistry.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        proxy.preInit(event);

        // Register the teleportation capability, to make it available for injection.
        TeleportationHandlerCapabilityProvider.registerCapability();
        
        // Second parameter to registerWorldGenerator() is the 'weight' to assign to this generator.
        // Heavy weights tend to sink to the bottom of the list of world generators (i.e. they run later).
        // If you’re experiencing issues with other mods interfering with your world generation, you may want to change this.
        GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 3);

        // Register our GUI handler with Forge.
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());

        // Setup network channel and register our messages with the side on which it is received.
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(new PacketUpdatePedestal.Handler(), PacketUpdatePedestal.class, networkPacketID++, Side.CLIENT);
        network.registerMessage(new PacketRequestUpdatePedestal.Handler(), PacketRequestUpdatePedestal.class, networkPacketID++,
                Side.SERVER);
        network.registerMessage(new PacketUpdateTeleportBeacon.Handler(), PacketUpdateTeleportBeacon.class, networkPacketID++, Side.CLIENT);
        network.registerMessage(new PacketRequestUpdateTeleportBeacon.Handler(), PacketRequestUpdateTeleportBeacon.class, networkPacketID++,
                Side.SERVER);

        // Register our special renderers.
        proxy.registerRenderers();

        // Init other registry handlers.
        ModPotions.registerPotions();

        // Register miscellaneous loot tables.
        LootTableList.register(new ResourceLocation(MODID, "chests/spawn_bonus_chest"));
        
        // Register dispenser behaviors.
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.teleportationSplashPotion, new IBehaviorDispenseItem()
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
        
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.teleportationTippedArrow, new IBehaviorDispenseItem()
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
    }

    /**
     * Performs mod setup.
     * Build whatever data structures you care about. Register recipes, send FMLInterModComms messages to other mods.
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        // [STDOUT]: [org.bensam.experimental.ExperimentalMod:init:121]: APPLE ITEM >> minecraft:apple
        System.out.println("APPLE ITEM >> " + Items.APPLE.getRegistryName());

        // [experimental]: STONE BLOCK >> minecraft:stone
        logger.info("STONE BLOCK >> {}", Blocks.STONE.getRegistryName());

        // [experimental]: COUNTER TILEENTITY >> experimental:counter
        logger.info("COUNTER TILEENTITY >> {}", ModBlocks.counter.getRegistryName());
        logger.info("TELEPORTATION POTION ITEM >> {}", ModPotions.TELEPORTATION.getRegistryName());
        logger.info("Teleport Beacon translation key: {}", ModBlocks.teleportBeacon.getTranslationKey());
        logger.info("Random letters: {} {} {}", ModHelper.getRandomLetter(), ModHelper.getRandomLetter(), ModHelper.getRandomLetter());
        
        logger.info("IRON SWORD attack damage: {}",
                ((net.minecraft.item.ItemSword) Items.IRON_SWORD).getAttackDamage());
        logger.info("COPPER SWORD attack damage: {}", ModItems.copperSword.getAttackDamage());
        logger.info("COPPER MATERIAL attack damage: {}", copperToolMaterial.getAttackDamage());
        logger.info("copperSwordAttackDamage: {}", ModConfig.copperSwordAttackDamage);

        logger.info("Carrot Heal: {}; Saturation: {}", ((ItemFood) Items.CARROT).getHealAmount(null),
                ((ItemFood) Items.CARROT).getSaturationModifier(null));
        logger.info("Rotten Flesh Heal: {}; Saturation: {}", ((ItemFood) Items.ROTTEN_FLESH).getHealAmount(null),
                ((ItemFood) Items.ROTTEN_FLESH).getSaturationModifier(null));
        logger.info("Steak Heal: {}; Saturation: {}", ((ItemFood) Items.COOKED_BEEF).getHealAmount(null),
                ((ItemFood) Items.COOKED_BEEF).getSaturationModifier(null));
        logger.info("Corn Heal: {}; Saturation: {}", ModItems.corn.getHealAmount(null),
                ModItems.corn.getSaturationModifier(null));
        logger.info("Corn Bread Heal: {}; Saturation: {}", ModItems.cornBread.getHealAmount(null),
                ModItems.cornBread.getSaturationModifier(null));

        proxy.init(event);

        ModRecipes.init();
    }

    /**
     * Handle interaction with other mods, complete your setup based on this.
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
    
    /**
     * Called after FMLServerAboutToStartEvent and before FMLServerStartedEvent.
     * This event allows for customizations of the server, such as loading custom commands, perhaps customizing recipes or other activities.
     */
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandTeleportation());
    }
}
