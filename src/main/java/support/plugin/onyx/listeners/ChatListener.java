package support.plugin.onyx.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.factions.Faction;
import support.plugin.onyx.profiles.GameProfile;
import support.plugin.onyx.profiles.dto.ChatMode;

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
 * Handles the various different chat channels
 */
public class ChatListener implements Listener {

    /*

    This class is gross, I'll update this and fix it up when I've got time...

    TODO: Clean this up ffs

     */

    private Onyx instance;

    public ChatListener(Onyx instance) {

        this.instance = instance;

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {

        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        GameProfile gameProfile = instance.getProfileManager().getUser(player.getUniqueId());

        Faction faction = instance.getFactionManager().getFactionByMember(player.getUniqueId());

        ChatMode chatMode = gameProfile.getChatMode();

        e.setCancelled(true);

        switch (chatMode) {

            case PUBLIC:

                String format = ChatColor.translateAlternateColorCodes('&', instance.getSettings().getString("chat_format.public"));

                for (Player recipient : e.getRecipients()) {

                    if (faction == null) {

                        // No tag
                        String tag = instance.getSettings().getString("chat_tags.no_faction");

                        format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                        format = format.replace("{name}", player.getName());

                        if (player.hasPermission("chat.coloured")) {
                            format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                        } else {
                            format = format.replace("{message}", e.getMessage());
                        }

                        recipient.sendMessage(format);

                    } else {

                        Faction recipientFaction = instance.getFactionManager().getFactionByMember(recipient.getUniqueId());

                        //Now check if they're an ally, in their faction or have no relation
                        if (faction == recipientFaction) {

                            //In faction
                            String tag = instance.getSettings().getString("chat_tags.friendly");

                            format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                            format = format.replace("{name}", player.getName());

                            if (player.hasPermission("chat.coloured")) {
                                format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                            } else {
                                format = format.replace("{message}", e.getMessage());
                            }

                            recipient.sendMessage(format);

                        } else if (faction.isAllied(recipientFaction)) {

                            //Allied
                            String tag = instance.getSettings().getString("chat_tags.allied");

                            format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                            format = format.replace("{name}", player.getName());

                            if (player.hasPermission("chat.coloured")) {
                                format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                            } else {
                                format = format.replace("{message}", e.getMessage());
                            }

                            recipient.sendMessage(format);

                        } else {

                            //No relation
                            String tag = instance.getSettings().getString("chat_tags.enemy");

                            format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                            format = format.replace("{name}", player.getName());

                            if (player.hasPermission("chat.coloured")) {
                                format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                            } else {
                                format = format.replace("{message}", e.getMessage());
                            }

                            recipient.sendMessage(format);


                        }

                    }

                }

                if (!e.getRecipients().contains(player)) {

                    if (faction == null) {

                        // No tag
                        String tag = instance.getSettings().getString("chat_tags.no_faction");

                        format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                        format = format.replace("{name}", player.getName());

                        if (player.hasPermission("chat.coloured")) {
                            format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                        } else {
                            format = format.replace("{message}", e.getMessage());
                        }

                        player.sendMessage(format);

                    } else {

                        Faction recipientFaction = instance.getFactionManager().getFactionByMember(player.getUniqueId());

                        //Now check if they're an ally, in their faction or have no relation
                        if (faction == recipientFaction) {

                            //In faction
                            String tag = instance.getSettings().getString("chat_tags.friendly");

                            format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                            format = format.replace("{name}", player.getName());

                            if (player.hasPermission("chat.coloured")) {
                                format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                            } else {
                                format = format.replace("{message}", e.getMessage());
                            }

                            player.sendMessage(format);

                        } else if (faction.isAllied(recipientFaction)) {

                            //Allied
                            String tag = instance.getSettings().getString("chat_tags.allied");

                            format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                            format = format.replace("{name}", player.getName());

                            if (player.hasPermission("chat.coloured")) {
                                format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                            } else {
                                format = format.replace("{message}", e.getMessage());
                            }

                            player.sendMessage(format);

                        } else {

                            //No relation
                            String tag = instance.getSettings().getString("chat_tags.enemy");

                            format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                            format = format.replace("{name}", player.getName());

                            if (player.hasPermission("chat.coloured")) {
                                format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                            } else {
                                format = format.replace("{message}", e.getMessage());
                            }

                            player.sendMessage(format);


                        }

                    }

                }
                break;

            case ALLY:

                if (faction == null) {
                    player.sendMessage(ChatColor.RED + "You aren't in a faction! Switching chatmode back to public.");
                    gameProfile.setChatMode(ChatMode.PUBLIC);
                    return;
                }

                String allyformat = ChatColor.translateAlternateColorCodes('&', instance.getSettings().getString("chat_format.ally"));

                allyformat = allyformat.replace("{name}", player.getName());
                allyformat = allyformat.replace("{message}", e.getMessage());

                //"&5[Ally] {name}&7: {message}"

                for (Player online : faction.getOnlinePlayers()) {
                    online.sendMessage(allyformat);
                }

                for (Player online : faction.getOnlineAllies()) {
                    online.sendMessage(allyformat);
                }
                break;

            case FACTION:

                if (faction == null) {
                    player.sendMessage(ChatColor.RED + "You aren't in a faction! Switching chatmode back to public.");
                    gameProfile.setChatMode(ChatMode.PUBLIC);
                    return;
                }

                String factionformat = ChatColor.translateAlternateColorCodes('&', instance.getSettings().getString("chat_format.faction"));

                factionformat = factionformat.replace("{role_prefix}", faction.getRole(player.getUniqueId()).getPrefix());
                factionformat = factionformat.replace("{name}", player.getName());
                factionformat = factionformat.replace("{message}", e.getMessage());

                for (Player online : faction.getOnlinePlayers()) {
                    online.sendMessage(factionformat);
                }
                break;

            case OFFICER:

                if (faction == null) {
                    player.sendMessage(ChatColor.RED + "You aren't in a faction! Switching chatmode back to public.");
                    gameProfile.setChatMode(ChatMode.PUBLIC);
                    return;
                }

                String officerformat = ChatColor.translateAlternateColorCodes('&', instance.getSettings().getString("chat_format.officer"));

                officerformat = officerformat.replace("{role_prefix}", faction.getRole(player.getUniqueId()).getPrefix());
                officerformat = officerformat.replace("{name}", player.getName());
                officerformat = officerformat.replace("{message}", e.getMessage());

                for (Player online : faction.getOnlineOfficers()) {
                    online.sendMessage(officerformat);
                }
                break;

        }

    }


}
