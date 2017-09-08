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
 * Handles the sub-command for joining factions
 */
@Info(subCommand = "join", description = "Join a faction", usage = "<faction> (--force)", permission = "onyx.factions.join", aliases = {"join", "j"})
public class FactionJoinCommand implements ICommand {

    private Onyx instance;

    public FactionJoinCommand(Onyx instance) {
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

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("general.invalid_arguments") + " &c/faction join <faction/player>"));
            return;

        }

        Faction faction = factionManager.getFactionByMember(player.getUniqueId());

        if (faction != null) {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.general.already_in_faction")));
            return;

        }

        String attemptedFactionName = args[0];

        if (factionManager.getFactionByName(attemptedFactionName) == null) {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.general.non_existent").replace("{faction}", args[0])));
            return;

        }

        Faction attemptedFaction = factionManager.getFactionByName(args[0]);

        if (!attemptedFaction.isOpen()) {
            if (!attemptedFaction.getInvitedPlayers().contains(player.getUniqueId())) {

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.join.not_invited").replace("{faction}", attemptedFaction.getFactionName())));
                faction.sendOfficerMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.join.attempted_join")));
                return;

            }
        }

        if (faction.getFactionMembers().size() >= instance.getSettings().getInt("faction.max_players")) {


        }

        // If they have been invited....


    }

}
