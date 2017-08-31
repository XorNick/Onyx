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

/*

Copyright (c) 2017 PluginManager LTD

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

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
