package org.bensam.experimental;

import org.apache.logging.log4j.Logger;
import org.bensam.experimental.block.ModBlocks;
import org.bensam.experimental.client.CreativeTab;
import org.bensam.experimental.item.ModItems;
import org.bensam.experimental.network.PacketRequestUpdatePedestal;
import org.bensam.experimental.network.PacketUpdatePedestal;
import org.bensam.experimental.proxy.CommonProxy;
import org.bensam.experimental.recipe.ModRecipes;
import org.bensam.experimental.world.ModWorldGenerator;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Will
 *
 */
@Mod(modid = ExperimentalMod.MODID, name = ExperimentalMod.NAME, version = ExperimentalMod.VERSION)
public class ExperimentalMod
{
    public static final String MODID = "experimental";
    public static final String NAME = "Experimental Mod";
    public static final String VERSION = "0.5";
    
    private static Logger logger;
    
    public static CreativeTab creativeTab;
    
    public static SimpleNetworkWrapper network;
    
    @SidedProxy(clientSide = "org.bensam.experimental.proxy.ClientProxy", serverSide = "org.bensam.experimental.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    @Mod.Instance(MODID)
    public static ExperimentalMod instance; // needed for GUIs and entities 
    
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
        creativeTab = new CreativeTab();
        
        proxy.preInit(event);
        ModBlocks.preInit();
        ModItems.preInit();
        
        // Second parameter to registerWorldGenerator() is the 'weight' to assign to this generator.
        // Heavy weights tend to sink to the bottom of the list of world generators (i.e. they run later).
        // If you�re experiencing issues with other mods interfering with your world generation, you may want to change this.
        GameRegistry.registerWorldGenerator(new ModWorldGenerator(), 3);
        
        // Register our GUI handler with Forge.
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
        
        // Setup network channel and register our messages with the side on which it is received.
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(new PacketUpdatePedestal.Handler(), PacketUpdatePedestal.class, 0, Side.CLIENT);
        network.registerMessage(new PacketRequestUpdatePedestal.Handler(), PacketRequestUpdatePedestal.class, 1, Side.SERVER);
        
        // Register our special renderers.
        proxy.registerRenderers();
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
        logger.info("Carrot Heal: {}; Saturation: {}", 
                ((ItemFood)Items.CARROT).getHealAmount(null),
                ((ItemFood)Items.CARROT).getSaturationModifier(null));
        logger.info("Rotten Flesh Heal: {}; Saturation: {}", 
                ((ItemFood)Items.ROTTEN_FLESH).getHealAmount(null),
                ((ItemFood)Items.ROTTEN_FLESH).getSaturationModifier(null));
        logger.info("Steak Heal: {}; Saturation: {}", 
                ((ItemFood)Items.COOKED_BEEF).getHealAmount(null),
                ((ItemFood)Items.COOKED_BEEF).getSaturationModifier(null));
        logger.info("Corn Heal: {}; Saturation: {}", 
                ModItems.corn.getHealAmount(null),
                ModItems.corn.getSaturationModifier(null));
        logger.info("Corn Bread Heal: {}; Saturation: {}", 
                ModItems.cornBread.getHealAmount(null),
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
}