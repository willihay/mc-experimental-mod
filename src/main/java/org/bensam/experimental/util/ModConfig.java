/**
 * 
 */
package org.bensam.experimental.util;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.bensam.experimental.ExperimentalMod;

import net.minecraftforge.common.config.Configuration;

/**
 * @author Will
 *
 */
public class ModConfig
{
    public static Configuration config;

    private static final String CONFIG_FILENAME = "expmod.cfg";
    private static final String CONFIG_VERSION = "2";

    private static final String CATEGORY_GENERAL = "general";
    //private static final String CATEGORY_DIMENSIONS = "dimensions";

    // These values can be accessed throughout the mod:
    public static float copperSwordAttackDamage = 1.5f; // in game attack damage, add 4

    public static void initConfig(File configDir)
    {
        config = new Configuration(new File(configDir.getPath(), CONFIG_FILENAME), CONFIG_VERSION, false);
    }

    public static void readConfig()
    {
        try
        {
            config.load();
            initGeneralConfig();

            String configFileVersion = config.getLoadedConfigVersion();
            if (!configFileVersion.equals(CONFIG_VERSION))
            {
                if (configFileVersion.equals("1"))
                {
                    ExperimentalMod.MOD_LOGGER.info("Converting config file to version {}", CONFIG_VERSION);
                    // Save config to update config version and default copper sword attack damage.
                    config.save();
                }
            }
        }
        catch (Exception ex)
        {
            ExperimentalMod.MOD_LOGGER.log(Level.ERROR, "Problem loading config file!", ex);
        }
        finally
        {
            if (config.hasChanged())
            {
                config.save();
            }
        }
    }

    private static void initGeneralConfig()
    {
        config.addCustomCategoryComment(CATEGORY_GENERAL, "General Configuration");
        copperSwordAttackDamage = config.getFloat("copperSwordAttackDamage", CATEGORY_GENERAL, copperSwordAttackDamage,
                0f, 10f, "Set copper sword attack damage");
    }
}
