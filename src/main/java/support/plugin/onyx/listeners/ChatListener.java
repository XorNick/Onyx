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

/**
 * Created by eric on 01/09/2017.
 */
public class ChatListener implements Listener {

    private Onyx instance;

    public ChatListener(Onyx instance){

        this.instance = instance;

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e){

        if(e.isCancelled()){ return; }

        Player player = e.getPlayer();
        GameProfile gameProfile = instance.getProfileManager().getUser(player.getUniqueId());

        Faction faction = instance.getFactionManager().getFactionByMember(player.getUniqueId());

        ChatMode chatMode = gameProfile.getChatMode();

        e.setCancelled(true);

        switch (chatMode){

            case PUBLIC:

                String format = ChatColor.translateAlternateColorCodes('&', instance.getSettings().getString("chat_format.public"));

                for(Player recipient : e.getRecipients()){

                    if(faction == null){

                        // No tag
                        String tag = instance.getSettings().getString("chat_tags.no_faction");

                        format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                        format = format.replace("{name}", player.getName());

                        if(player.hasPermission("chat.coloured")){
                            format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&',e.getMessage()));
                        }else{
                            format = format.replace("{message}", e.getMessage());
                        }

                        recipient.sendMessage(format);

                    }else{

                        Faction recipientFaction = instance.getFactionManager().getFactionByMember(recipient.getUniqueId());

                        //Now check if they're an ally, in their faction or have no relation
                        if(faction == recipientFaction){

                            //In faction
                            String tag = instance.getSettings().getString("chat_tags.friendly");

                            format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                            format = format.replace("{name}", player.getName());

                            if(player.hasPermission("chat.coloured")){
                                format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&',e.getMessage()));
                            }else{
                                format = format.replace("{message}", e.getMessage());
                            }

                            recipient.sendMessage(format);

                        }else if(faction.isAllied(recipientFaction)){

                            //Allied
                            String tag = instance.getSettings().getString("chat_tags.allied");

                            format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                            format = format.replace("{name}", player.getName());

                            if(player.hasPermission("chat.coloured")){
                                format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&',e.getMessage()));
                            }else{
                                format = format.replace("{message}", e.getMessage());
                            }

                            recipient.sendMessage(format);

                        }else{

                            //No relation
                            String tag = instance.getSettings().getString("chat_tags.enemy");

                            format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                            format = format.replace("{name}", player.getName());

                            if(player.hasPermission("chat.coloured")){
                                format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&',e.getMessage()));
                            }else{
                                format = format.replace("{message}", e.getMessage());
                            }

                            recipient.sendMessage(format);


                        }

                    }

                }

                if(!e.getRecipients().contains(player)){

                    if(faction == null){

                        // No tag
                        String tag = instance.getSettings().getString("chat_tags.no_faction");

                        format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                        format = format.replace("{name}", player.getName());

                        if(player.hasPermission("chat.coloured")){
                            format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&',e.getMessage()));
                        }else{
                            format = format.replace("{message}", e.getMessage());
                        }

                        player.sendMessage(format);

                    }else{

                        Faction recipientFaction = instance.getFactionManager().getFactionByMember(player.getUniqueId());

                        //Now check if they're an ally, in their faction or have no relation
                        if(faction == recipientFaction){

                            //In faction
                            String tag = instance.getSettings().getString("chat_tags.friendly");

                            format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                            format = format.replace("{name}", player.getName());

                            if(player.hasPermission("chat.coloured")){
                                format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&',e.getMessage()));
                            }else{
                                format = format.replace("{message}", e.getMessage());
                            }

                            player.sendMessage(format);

                        }else if(faction.isAllied(recipientFaction)){

                            //Allied
                            String tag = instance.getSettings().getString("chat_tags.allied");

                            format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                            format = format.replace("{name}", player.getName());

                            if(player.hasPermission("chat.coloured")){
                                format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&',e.getMessage()));
                            }else{
                                format = format.replace("{message}", e.getMessage());
                            }

                            player.sendMessage(format);

                        }else{

                            //No relation
                            String tag = instance.getSettings().getString("chat_tags.enemy");

                            format = format.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag));
                            format = format.replace("{name}", player.getName());

                            if(player.hasPermission("chat.coloured")){
                                format = format.replace("{message}", ChatColor.translateAlternateColorCodes('&',e.getMessage()));
                            }else{
                                format = format.replace("{message}", e.getMessage());
                            }

                            player.sendMessage(format);


                        }

                    }

                }

            case ALLY:

                if(faction == null){
                    player.sendMessage(ChatColor.RED + "You aren't in a faction! Switching chatmode back to public.");
                    gameProfile.setChatMode(ChatMode.PUBLIC);
                    return;
                }

                String allyformat = ChatColor.translateAlternateColorCodes('&', instance.getSettings().getString("chat_format.ally"));

                allyformat = allyformat.replace("{name}", player.getName());
                allyformat = allyformat.replace("{message}", e.getMessage());

                //"&5[Ally] {name}&7: {message}"

                for(Player online : faction.getOnlinePlayers()){
                    online.sendMessage(allyformat);
                }

                for(Player online : faction.getOnlineAllies()){
                    online.sendMessage(allyformat);
                }

            case FACTION:

                if(faction == null){
                    player.sendMessage(ChatColor.RED + "You aren't in a faction! Switching chatmode back to public.");
                    gameProfile.setChatMode(ChatMode.PUBLIC);
                    return;
                }

                String factionformat = ChatColor.translateAlternateColorCodes('&', instance.getSettings().getString("chat_format.faction"));

                factionformat = factionformat.replace("{role_prefix}",faction.getRole(player.getUniqueId()).getPrefix());
                factionformat = factionformat.replace("{name}", player.getName());
                factionformat = factionformat.replace("{message}", e.getMessage());

                for(Player online : faction.getOnlinePlayers()){
                    online.sendMessage(factionformat);
                }

            case OFFICER:

                if(faction == null){
                    player.sendMessage(ChatColor.RED + "You aren't in a faction! Switching chatmode back to public.");
                    gameProfile.setChatMode(ChatMode.PUBLIC);
                    return;
                }

                String officerformat = ChatColor.translateAlternateColorCodes('&', instance.getSettings().getString("chat_format.officer"));

                officerformat = officerformat.replace("{role_prefix}",faction.getRole(player.getUniqueId()).getPrefix());
                officerformat = officerformat.replace("{name}", player.getName());
                officerformat = officerformat.replace("{message}", e.getMessage());

                for(Player online : faction.getOnlineOfficers()){
                    online.sendMessage(officerformat);
                }

        }

    }


}
