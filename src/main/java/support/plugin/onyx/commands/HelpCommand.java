package support.plugin.onyx.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import support.plugin.onyx.Onyx;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        for(String helpLine : Onyx.getInstance().getLocale().getStringList("general.help_message")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', helpLine));
        }

        return true;
    }

}
