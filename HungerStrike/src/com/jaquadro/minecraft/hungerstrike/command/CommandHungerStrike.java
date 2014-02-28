package com.jaquadro.minecraft.hungerstrike.command;

import com.jaquadro.minecraft.hungerstrike.ConfigManager;
import com.jaquadro.minecraft.hungerstrike.ExtendedPlayer;
import com.jaquadro.minecraft.hungerstrike.HungerStrike;
import com.jaquadro.minecraft.hungerstrike.PlayerHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class CommandHungerStrike extends CommandBase {

    @Override
    public String getCommandName () {
        return "hungerstrike";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage (ICommandSender sender) {
        return "commands.hungerstrike.usage";
    }

    @Override
    public void processCommand (ICommandSender sender, String[] args) {
        if (args.length >= 1) {
            if (args[0].equals("list")) {
                List<String> players = playersToNames(PlayerHandler.getStrikingPlayers());

                sender.addChatMessage(new ChatComponentTranslation("commands.hungerstrike.list",
                    Integer.valueOf(players.size()),
                    Integer.valueOf(MinecraftServer.getServer().getConfigurationManager().playerEntityList.size())));
                sender.addChatMessage(new ChatComponentText(joinNiceString(players.toArray(new String[players.size()]))));
                return;
            }

            if (args[0].equals("add")) {
                if (args.length < 2)
                    throw new WrongUsageException("commands.hungerstrike.add.usage");

                ExtendedPlayer player = ExtendedPlayer.get(getPlayer(sender, args[1]));
                player.enableHungerStrike(true);

                notifyAdmins(sender, "commands.hungerstrike.add.success", args[1]);
                return;
            }

            if (args[0].equals("remove")) {
                if (args.length < 2)
                    throw new WrongUsageException("commands.hungerstrike.remove.usage");

                ExtendedPlayer player = ExtendedPlayer.get(getPlayer(sender, args[1]));
                player.enableHungerStrike(false);

                notifyAdmins(sender, "commands.hungerstrike.remove.success", args[1]);
                return;
            }

            if (args[0].equals("mode")) {
                ConfigManager.Mode mode = HungerStrike.instance.config.getMode();

                if (mode == ConfigManager.Mode.NONE)
                    notifyAdmins(sender, "commands.hungerstrike.mode.none");
                else if (mode == ConfigManager.Mode.LIST)
                    notifyAdmins(sender, "commands.hungerstrike.mode.list");
                else if (mode == ConfigManager.Mode.ALL)
                    notifyAdmins(sender, "commands.hungerstrike.mode.all");
                return;
            }

            if (args[0].equals("setmode")) {
                if (args.length < 2)
                    throw new WrongUsageException("commands.hungerstrike.setmode.usage");

                ConfigManager.Mode mode = ConfigManager.Mode.fromValueIgnoreCase(args[1]);
                HungerStrike.instance.config.setMode(mode);

                if (mode == ConfigManager.Mode.NONE)
                    notifyAdmins(sender, "commands.hungerstrike.setmode.none");
                else if (mode == ConfigManager.Mode.LIST)
                    notifyAdmins(sender, "commands.hungerstrike.setmode.list");
                else if (mode == ConfigManager.Mode.ALL)
                    notifyAdmins(sender, "commands.hungerstrike.setmode.all");
                return;
            }
        }

        throw new WrongUsageException("commands.hungerstrike.usage");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "list", "add", "remove", "mode", "setmode");
        }
        else {
            if (args.length == 2) {
                if (args[0].equals("add")) {
                    List<String> players = playersToNames(PlayerHandler.getNonStrikingPlayers());
                    return getPartialMatches(args[args.length - 1], players);
                }

                if (args[0].equals("remove")) {
                    List<String> players = playersToNames(PlayerHandler.getStrikingPlayers());
                    return getPartialMatches(args[args.length - 1], players);
                }

                if (args[0].equals("setmode")) {
                    return getListOfStringsMatchingLastWord(args, "none", "list", "all");
                }
            }

            return null;
        }
    }

    private List<String> playersToNames (List<EntityPlayer> players) {
        List<String> playerNames = new ArrayList<String>(players.size());
        for (EntityPlayer player : players)
            playerNames.add(player.getCommandSenderName());

        return playerNames;
    }

    private List<String> getPartialMatches (String partialName, List<String> candidates) {
        List<String> suggestions = new ArrayList<String>();
        for (int i = 0; i < candidates.size(); i++) {
            String playerName = candidates.get(i);
            if (doesStringStartWith(partialName, playerName))
                suggestions.add(playerName);
        }

        return suggestions;
    }
}
