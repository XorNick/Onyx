package support.plugin.onyx.factions.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.commands.handler.ICommand;
import support.plugin.onyx.commands.handler.Info;
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
 * Handles the sub-command for creating factions
 */
@Info(subCommand = "create", description = "Create a faction", usage = "<name> (--system)", permission = "onyx.factions.create", aliases = {"create"})
public class FactionCreateCommand implements ICommand {

    // Untested

    private Onyx instance;

    public FactionCreateCommand(Onyx instance) {
        this.instance = instance;
    }

    @Override
    public void execute(Player player, String[] args) {

        if (args.length == 0) {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getLocale().getString("faction.general.no_faction_name")));
            return;

        }

        boolean systemFaction = false;

        if (args.length > 2) {

            if (args[1].equalsIgnoreCase("--system")) {

                systemFaction = true;

            }

        }

        FactionManager factionManager = Onyx.getInstance().getFactionManager();

        if (factionManager.getFactionByMember(player.getUniqueId()) != null) {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getLocale().getString("faction.general.already_in_faction")));
            return;

        }

        String factionName = args[0];

        if (factionName.length() <= instance.getSettings().getInt("faction.name.min")) {

            String minimumFormat = ChatColor.translateAlternateColorCodes('&', instance.getLocale().getString("faction.creation.faction_min_name"));
            minimumFormat = minimumFormat.replace("{min}", instance.getSettings().getInt("faction.name.min") + "");

            player.sendMessage(minimumFormat);
            return;

        }

        if (factionName.length() >= instance.getSettings().getInt("faction.name.max")) {

            String minimumFormat = ChatColor.translateAlternateColorCodes('&', instance.getLocale().getString("faction.creation.faction_max_name"));
            minimumFormat = minimumFormat.replace("{max}", instance.getSettings().getInt("faction.name.max") + "");

            player.sendMessage(minimumFormat);
            return;

        }

        if (!factionName.matches("^[a-zA-Z0-9]*$")) { // Ensuring only alphanumerical names are allowed.

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getLocale().getString("faction.creation.alphanumerical_names")));
            return;

        }

        for (String bannedName : instance.getSettings().getStringList("faction.name.blocked")) {
            if (bannedName.equalsIgnoreCase(factionName)) {

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getLocale().getString("faction.creation.faction_name_blocked")));
                return;

            }
        }

        Faction faction = new Faction(player.getUniqueId(), factionName);
        faction.setSystemFaction(systemFaction);

        factionManager.createFaction(faction);

        String createdFormat = instance.getLocale().getString("faction.creation.faction_created");
        createdFormat = createdFormat.replace("{faction}", factionName);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', createdFormat));

        String createdBroadcast = instance.getLocale().getString("faction.creation.faction_created_broadcast");
        createdBroadcast = createdBroadcast.replace("{player}", player.getName());
        createdBroadcast = createdBroadcast.replace("{faction}", factionName);
        instance.getServer().broadcastMessage(createdBroadcast);

    }
}
