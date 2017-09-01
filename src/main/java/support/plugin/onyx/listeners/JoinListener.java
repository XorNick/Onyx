package support.plugin.onyx.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.utils.ItemBuilder;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by eric on 01/09/2017.
 */
public class JoinListener implements Listener {

    private Onyx instance;

    public JoinListener(Onyx instance){

        this.instance = instance;

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();

        if(!player.hasPlayedBefore()){

            if(instance.getSettings().getBoolean("map.kitmap")){

                return;

            }

        }

    }

}
