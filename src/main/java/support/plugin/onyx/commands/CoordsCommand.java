package support.plugin.onyx.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import support.plugin.onyx.Onyx;

public class CoordsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        for(String coordsLine : Onyx.getInstance().getLocale().getStringList("general.coords_message")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', coordsLine));
        }

        return true;
    }

}