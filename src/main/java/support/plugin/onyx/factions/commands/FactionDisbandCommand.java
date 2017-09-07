package support.plugin.onyx.factions.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.commands.handler.SubCommand;
import support.plugin.onyx.config.Configuration;
import support.plugin.onyx.factions.Faction;
import support.plugin.onyx.factions.FactionManager;
import support.plugin.onyx.factions.claim.Claim;
import support.plugin.onyx.factions.enums.FactionRole;
import support.plugin.onyx.profiles.GameProfile;

import java.util.List;

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
public class FactionDisbandCommand extends SubCommand {

    // Untested

    private Onyx instance;

    public FactionDisbandCommand(Onyx instance, String subCommand, List<String> aliases, String description, Boolean playerOnly) {
        super(instance, subCommand, aliases, description, playerOnly);
        this.instance = instance;
    }

    @Override
    public void execute(Player player, String[] args) {

        Configuration locale = instance.getLocale();
        FactionManager factionManager = instance.getFactionManager();

        if (factionManager.getFactionByMember(player.getUniqueId()) != null) {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.general.not_in_faction")));
            return;

        }

        Faction faction = factionManager.getFactionByMember(player.getUniqueId());

        if (faction.getRole(player.getUniqueId()) != FactionRole.OWNER) {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.roles.owner_required")));
            return;

        }

        double refundAmount = 0;

        for (Claim claim : faction.getFactionClaims()) {

            refundAmount += (claim.getPrice() / 2); // Adds half of the old claim worth

        }

        for (Faction alliedFaction : faction.getAllies()) {

            for (Player onlineAlly : alliedFaction.getOnlinePlayers()) {

                onlineAlly.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.disband.ally_message")));

            }

            alliedFaction.getAllies().remove(faction); // Making sure there are no null pointers ;)

        }

        faction.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.disband.member_message")));

        GameProfile profile = instance.getProfileManager().getUser(player.getUniqueId());

        profile.setBalance(profile.getBalance() + refundAmount);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.disband.success").replace("{refund}", refundAmount + "" /* lazy */)));
        instance.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', locale.getString("faction.disband.broadcast").replace("{faction}", faction.getFactionName()).replace("{player}", player.getName())));

        // Not sure if this is required, but I'll do it anyway.
        faction.getFactionClaims().clear();

        factionManager.disbandFaction(faction);

    }
}
