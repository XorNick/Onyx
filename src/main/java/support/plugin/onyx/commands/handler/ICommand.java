package support.plugin.onyx.commands.handler;

import org.bukkit.entity.Player;

/**
 * Command interface for each command
 */
public interface ICommand {

    void execute(Player player, String[] args);

}
