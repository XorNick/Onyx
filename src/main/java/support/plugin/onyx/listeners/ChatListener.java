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

        if(!instance.getSettings().getBoolean("chat_format")){
            return;
        }

        switch (chatMode){

            case PUBLIC:

                for(Player recipient : e.getRecipients()){

                    Faction recipientFaction = instance.getFactionManager().getFactionByMember(recipient.getUniqueId());

                    if(recipientFaction == null){

                        if(faction == null){

                            // Standard public chat
                            String no_tag = instance.getSettings().getString("chat_tags.no_faction");



                        }else{

                            // Enemy tag
                            String enemy = instance.getSettings().getString("chat_tags.enemy");

                            

                        }

                    }else{

                        if(faction.isAllied(recipientFaction)){

                            //Allied public format
                            String allied = instance.getSettings().getString("chat_tags.allied");



                        }else if(faction.contains(recipient.getUniqueId())){

                            //Friendly public format
                            String friendly = instance.getSettings().getString("chat_tags.friendly");



                        }

                    }

                }
            case ALLY:

                if(faction == null){
                    player.sendMessage(ChatColor.RED + "You aren't in a faction! Switching chatmode back to public.");
                    gameProfile.setChatMode(ChatMode.PUBLIC);
                    return;
                }

            case FACTION:

                if(faction == null){
                    player.sendMessage(ChatColor.RED + "You aren't in a faction! Switching chatmode back to public.");
                    gameProfile.setChatMode(ChatMode.PUBLIC);
                    return;
                }

            case OFFICER:

                if(faction == null){
                    player.sendMessage(ChatColor.RED + "You aren't in a faction! Switching chatmode back to public.");
                    gameProfile.setChatMode(ChatMode.PUBLIC);
                    return;
                }

        }

    }

}
