package support.plugin.onyx.factions.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.commands.handler.SubCommand;
import support.plugin.onyx.config.Configuration;
import support.plugin.onyx.factions.Faction;
import support.plugin.onyx.factions.FactionManager;
import support.plugin.onyx.profiles.GameProfile;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by eric on 07/09/2017.
 */
public class FactionInviteCommand extends SubCommand {

    private Onyx instance;

    public FactionInviteCommand(Onyx instance) {
        super(instance, "invite", Arrays.asList("inv"), "Invite a player to your faction", true);
        this.instance = instance;
    }

    @Override
    public void execute(Player player, String[] args) {

        Configuration locale = instance.getLocale();
        FactionManager factionManager = instance.getFactionManager();

        if(factionManager.getFactionByMember(player.getUniqueId())==null){

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.general.not_in_faction")));
            return;

        }

        if(args.length == 0){

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("general.invalid_arguments") + " &c/faction invite <player>"));
            return;

        }

        Faction faction = factionManager.getFactionByMember(player.getUniqueId());

        if(faction.getRole(player.getUniqueId()).getRank() >= 2){

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.roles.officer_required")));
            return;

        }

        if(instance.getServer().getPlayer(args[0]) == null){

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("general.player_offline")));
            return;

        }

        Player target = instance.getServer().getPlayer(args[0]);

        if(factionManager.getFactionByMember(target.getUniqueId()) != null){

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.invite.already_in_faction")));
            return;

        }

        if(faction.getInvitedPlayers().contains(target.getUniqueId())){

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.invite.already_invited")));
            return;

        }

        faction.getInvitedPlayers().add(target.getUniqueId());

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.invite.success").replace("{player}", target.getName())));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.invite.invited_message").replace("{faction}", faction.getFactionName())));

        for(Player onlineMember : faction.getOnlinePlayers()){

            if(onlineMember == player){
                continue;
            }

            onlineMember.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.invite.faction_broadcast").replace("{player}", target.getName())));

        }

    }
}
