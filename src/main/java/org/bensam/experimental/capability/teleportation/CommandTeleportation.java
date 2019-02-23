package org.bensam.experimental.capability.teleportation;

import java.util.Collections;
import java.util.List;

import org.bensam.experimental.ExperimentalMod;
import org.bensam.experimental.capability.teleportation.TeleportDestination.DestinationType;
import org.bensam.experimental.network.PacketUpdateTeleportBeacon;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandTeleportation extends CommandBase
{
    
    /**
     * Gets the name of the command.
     */
    @Override
    public String getName()
    {
        return "td";
    }

    /**
     * Gets the usage string for the command.
     */
    @Override
    public String getUsage(ICommandSender sender)
    {
        return "commands.td.usage";
    }

    /**
     * Callback for when the command is executed.
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length <= 0)
        {
            throw new WrongUsageException("commands.td.usage", new Object[0]);
        }
        
        String cmd = args[0];
        int destinationIndex = -1;
        
        // Get the target destination array index, if any.
        if (args.length >= 2)
        {
            destinationIndex = parseInt(args[1], 1) - 1;
        }
        
        if (cmd.equalsIgnoreCase("list"))
        {
            executeListCommand(sender, destinationIndex);
        }
        else if (cmd.equalsIgnoreCase("delete"))
        {
            if (args.length < 2)
            {
                throw new CommandException("commands.td.destination.missing");
            }
            executeDeleteCommand(sender, destinationIndex);
        }
        else if (cmd.equalsIgnoreCase("next"))
        {
            executeNextCommand(sender);
        }
        else
        {
            throw new WrongUsageException("commands.td.usage", new Object[0]);
        }
    }

    public void executeListCommand(ICommandSender sender, int destinationIndex) throws CommandException
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        ITeleportationHandler teleportationHandler = player.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);

        if (teleportationHandler != null)
        {
            int destinationCount = teleportationHandler.getDestinationCount();
            if (destinationCount == 0)
            {
                player.sendMessage(new TextComponentTranslation("commands.td.destination.none"));
                return;
            }
            
            if (destinationIndex < 0)
            {
                // List all the teleport destinations in this player's network.
                for (int i = 0; i < destinationCount; i++)
                {
                    TeleportDestination destination = teleportationHandler.getDestinationFromIndex(i);
                    player.sendMessage(new TextComponentString((i + 1) + ") " + teleportationHandler.getLongFormattedName(player, destination)));
                }
            }
            else
            {
                // Display the name of the selected teleport destination.
                TeleportDestination destination = teleportationHandler.getDestinationFromIndex(destinationIndex);
                if (destination == null)
                {
                    throw new CommandException("commands.td.destination.notFound", new Object[] {(destinationIndex + 1)});
                }
                
                player.sendMessage(new TextComponentString(teleportationHandler.getLongFormattedName(player, destination)));
            }
        }
    }

    public void executeDeleteCommand(ICommandSender sender, int destinationIndex) throws CommandException
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);        
        ITeleportationHandler teleportationHandler = player.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);
        
        if (teleportationHandler != null)
        {
            int destinationCount = teleportationHandler.getDestinationCount();
            if (destinationCount == 0)
            {
                player.sendMessage(new TextComponentTranslation("commands.td.destination.none"));
                return;
            }
            
            // Get the TeleportDestination.
            TeleportDestination destination = teleportationHandler.getDestinationFromIndex(destinationIndex);
            if (destination == null)
            {
                throw new CommandException("commands.td.destination.notFound", new Object[] {(destinationIndex + 1)});
            }
            
            // Check destination - we don't want to remove an Overworld spawn bed from the network because this is supposed to be a fixed destination.
            if (destination.destinationType == DestinationType.SPAWNBED && destination.dimension == 0)
            {
                throw new CommandException("commands.td.delete.spawnBed.invalid");
            }
            
            // Notify the player what's getting removed.
            String name = destination.friendlyName;
            player.sendMessage(new TextComponentString("Teleport: " + TextFormatting.DARK_GREEN + name + TextFormatting.RESET + " REMOVED from your network"));

            // Only need to send a packet update to the client if we can still find the destination in the world.
            if (teleportationHandler.validateDestination(player, destination))
            {
                ExperimentalMod.network.sendTo(new PacketUpdateTeleportBeacon(destination.position, false), player);
            }

            // Finally, remove the destination from the player's network.
            teleportationHandler.removeDestination(destinationIndex);
        }
    }

    private void executeNextCommand(ICommandSender sender) throws CommandException
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        ITeleportationHandler teleportationHandler = player.getCapability(TeleportationHandlerCapabilityProvider.TELEPORTATION_CAPABILITY, null);

        if (teleportationHandler != null)
        {
            int destinationCount = teleportationHandler.getDestinationCount();
            if (destinationCount == 0)
            {
                player.sendMessage(new TextComponentTranslation("commands.td.destination.none"));
                return;
            }

            TeleportDestination destination = teleportationHandler.getNextActiveDestination();
            player.sendMessage(new TextComponentString("Active Teleport: " + teleportationHandler.getShortFormattedName(player, destination)));
        }
    }
    
    /**
     * Check if the given ICommandSender has permission to execute this command.
     */
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    /**
     * Get a list of options for when the user presses the TAB key
     */
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                          BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, new String[] {"delete", "list", "next"});
        }
        else if (args.length == 2 && !args[0].equalsIgnoreCase("next"))
        {
            return Lists.newArrayList("2");
        }

        return Collections.<String>emptyList();
    }

}
