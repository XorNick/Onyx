package support.plugin.onyx.factions.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.commands.handler.SubCommand;
import support.plugin.onyx.config.Configuration;
import support.plugin.onyx.factions.Faction;
import support.plugin.onyx.factions.FactionManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by eric on 07/09/2017.
 */
public class FactionUninviteCommand extends SubCommand {

    private Onyx instance;

    public FactionUninviteCommand(Onyx instance){
        super(instance, "uninvite", Arrays.asList("uninv", "revoke"), "Invite a player to your faction", true);
        this.instance = instance;
    }

    @Override
    public void execute(Player player, String[] args) {

        Configuration locale = instance.getLocale();
        FactionManager factionManager = instance.getFactionManager();

        if(args.length == 0){

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.general.invalid_arguments") + "&c/f uninvite <player>"));
            return;

        }

        if(factionManager.getFactionByMember(player.getUniqueId()) == null){

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.general.not_in_faction")));
            return;

        }

        Faction faction = factionManager.getFactionByMember(player.getUniqueId());

        if(faction.getRole(player.getUniqueId()).getRank() < 2){

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.roles.officer_required")));
            return;

        }

        if(instance.getServer().getPlayer(args[0]) == null){

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("general.player_offline")));
            return;

        }

        Player target = instance.getServer().getPlayer(args[0]);

        if(!faction.getInvitedPlayers().contains(target.getUniqueId())){

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.uninvite.player_not_invited").replace("{player}", target.getName())));
            return;

        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.uninvite.success").replace("{player}", target.getName())));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.uninvite.revoke_message").replace("{faction}", faction.getFactionName())));
        faction.getInvitedPlayers().remove(target.getUniqueId());

    }
}
