package support.plugin.onyx.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Created by eric on 01/09/2017.
 */
public class TntListener implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent e){

        e.blockList().clear();
        e.setCancelled(true);

    }

}
