package org.bensam.experimental.sound;

import org.bensam.experimental.ExperimentalMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(ExperimentalMod.MODID)
public class ModSounds
{
    public static final SoundEvent ACTIVATE_TELEPORT_BEACON = null;
    public static final SoundEvent DEACTIVATE_TELEPORT_BEACON = null;
    
    public static void register(IForgeRegistry<SoundEvent> registry)
    {
        ResourceLocation location = new ResourceLocation(ExperimentalMod.MODID, "activate_teleport_beacon");
        registry.register(new SoundEvent(location).setRegistryName("activate_teleport_beacon"));

        location = new ResourceLocation(ExperimentalMod.MODID, "deactivate_teleport_beacon");
        registry.register(new SoundEvent(location).setRegistryName("deactivate_teleport_beacon"));
    }
}
