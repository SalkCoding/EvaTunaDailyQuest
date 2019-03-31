package net.evatunadailyquest.salkcoding.command;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.GUI.GUICreator;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PluginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 2) {
            if (commandSender.isOp()) {
                if (strings[0].equalsIgnoreCase("uuid")) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(strings[1]);
                    if (player != null) {
                        commandSender.sendMessage(Constants.Info_Format + player.getName() + "'s player uuid : " + player.getUniqueId().toString());
                    } else {
                        commandSender.sendMessage(Constants.Warn_Format + strings[1] + " not exist player's name");
                    }
                } else if (strings[0].equalsIgnoreCase("info")) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(strings[1]);
                    if (player != null) {
                        List<Script> list = ScriptManager.getPlayerDailyQuests(player.getPlayer());
                        if (list != null) {
                            commandSender.sendMessage(Constants.Info_Format + player.getName() + "'s quests information");
                            for (Script script : list) {
                                commandSender.sendMessage("Script name : " + script.getQuestName());
                                commandSender.sendMessage("    Script Type : " + script.getClass().toString());
                                commandSender.sendMessage("    Progress : " + script.getQuestEvent().getProgress());
                                commandSender.sendMessage("    Condition : " + script.getQuestEvent().getCondition());
                            }
                        }
                    } else {
                        commandSender.sendMessage(Constants.Warn_Format + strings[1] + " is not online player or not exist player name");
                    }
                } else if (strings[0].equalsIgnoreCase("reset")) {
                    Player player = Bukkit.getPlayer(strings[1]);
                    if (player != null) {
                        ScriptManager.resetPlayerDailyQuests(player);
                        commandSender.sendMessage(Constants.Info_Format + player.getName() + " get new quests");
                    } else {
                        commandSender.sendMessage(Constants.Warn_Format + strings[1] + " is not online player or not exist player name");
                    }
                }
            } else {
                commandSender.sendMessage(Constants.Warn_Format + "You don't have the permission");
            }
        } else if (strings.length == 0) {
            if (commandSender instanceof Player) {
                GUICreator.createGUI((Player) commandSender);
                return true;
            }
            commandSender.sendMessage(Constants.Error_Format + "Console can't use this command");
        }
        return true;
    }
}
