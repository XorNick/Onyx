package support.plugin.onyx.factions.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.commands.handler.ICommand;
import support.plugin.onyx.commands.handler.Info;
import support.plugin.onyx.config.Configuration;
import support.plugin.onyx.factions.Faction;
import support.plugin.onyx.factions.FactionManager;

/*
Copyright (c) 2017 PluginManager LTD. All rights reserved.
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge and/or publish copies of the Software,
and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
Any copies of the Software shall stay private and cannot be resold.
Credit to PluginManager LTD shall be expressed in all forms of advertisement and/or endorsement.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

/**
 * Handles the sub-command for inviting players
 */
@Info(subCommand = "invite", description = "Invite a player to your faction", usage = "<player>", permission = "onyx.factions.disband", aliases = {"inv", "i"})
public class FactionInviteCommand implements ICommand {

    private Onyx instance;

    public FactionInviteCommand(Onyx instance) {
        this.instance = instance;
    }

    @Override
    public void execute(Player player, String[] args) {

        Configuration locale = instance.getLocale();
        FactionManager factionManager = instance.getFactionManager();

        if (factionManager.getFactionByMember(player.getUniqueId()) == null) {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.general.not_in_faction")));
            return;

        }

        if (args.length == 0) {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("general.invalid_arguments") + " &c/faction invite <player>"));
            return;

        }

        Faction faction = factionManager.getFactionByMember(player.getUniqueId());

        if (faction.getRole(player.getUniqueId()).getRank() < 2) {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.roles.officer_required")));
            return;

        }

        if (instance.getServer().getPlayer(args[0]) == null) {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("general.player_offline")));
            return;

        }

        Player target = instance.getServer().getPlayer(args[0]);

        if (factionManager.getFactionByMember(target.getUniqueId()) != null) {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.invite.already_in_faction")));
            return;

        }

        if (faction.getInvitedPlayers().contains(target.getUniqueId())) {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.invite.already_invited")));
            return;

        }

        faction.getInvitedPlayers().add(target.getUniqueId());

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.invite.success").replace("{player}", target.getName())));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.invite.invited_message").replace("{faction}", faction.getFactionName())));

        for (Player onlineMember : faction.getOnlinePlayers()) {

            if (onlineMember == player) {
                continue;
            }

            onlineMember.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.invite.faction_broadcast").replace("{player}", target.getName())));

        }

    }
}
