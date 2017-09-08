package support.plugin.onyx.economy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.config.Configuration;
import support.plugin.onyx.profiles.GameProfile;
import support.plugin.onyx.utils.DoubleUtils;

/**
 * Created by eric on 08/09/2017.
 */
public class PayCommand implements CommandExecutor {

    private Onyx instance;

    public PayCommand(Onyx instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("pay")) {

            Configuration locale = instance.getLocale();

            if (!(sender instanceof Player)) {

                sender.sendMessage(locale.translateString("commands.console_sender"));
                return true;

            }

            Player player = (Player) sender;

            // Get both game profiles and make checks...
            if (args.length < 2) {

                player.sendMessage(locale.translateString("general.invalid_arguments") + "/pay <player> <amount>");
                return true;

            }

            Player target = instance.getServer().getPlayer(args[0]);

            if (target == null) {

                player.sendMessage(locale.translateString("general.player_offline"));
                return true;

            }

            if (!DoubleUtils.tryParse(args[1])) {

                player.sendMessage(locale.translateString("general.not_number"));
                return true;

            }

            double amount = Double.parseDouble(args[1]);

            GameProfile senderProfile = instance.getProfileManager().getUser(player.getUniqueId());

            GameProfile receiverProfile = instance.getProfileManager().getUser(target.getUniqueId());

            if (senderProfile.getBalance() <= 0) {


            }

        }

        return true;
    }
}
