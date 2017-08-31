package support.plugin.onyx.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.timer.ITimer;
import support.plugin.onyx.timer.TimerManager;
import support.plugin.onyx.timer.TimerType;
import support.plugin.onyx.utils.TimeUtils;

/**
 * Created by eric on 31/08/2017.
 */
public class EnderpearlThrowListener implements Listener {

    @EventHandler
    public void onEnderpearlThrow(ProjectileLaunchEvent e){

        if(e.getEntity().getType() == EntityType.ENDER_PEARL){

            if(e.getEntity().getShooter() instanceof Player){
                Player player = (Player) e.getEntity().getShooter();

                TimerManager timerManager = Onyx.getInstance().getTimerManager();

                if(timerManager.hasTimer(player, TimerType.ENDERPEARL)){
                    player.sendMessage(ChatColor.RED + "You still have an enderpearl cooldown active for "+ TimeUtils.secondsFromMillis(timerManager.getTimer(player, TimerType.ENDERPEARL).getTime()));
                }

            }

        }

    }

}
