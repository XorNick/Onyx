package support.plugin.onyx.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.timer.TimerManager;
import support.plugin.onyx.timer.TimerType;

import java.util.HashMap;
import java.util.UUID;

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
public class StuckListener implements Listener {

    private Onyx instance;

    private HashMap<UUID, Location> stuckLocations;

    public StuckListener(Onyx instance) {

        this.instance = instance;

        stuckLocations = new HashMap<>();

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        TimerManager timerManager = instance.getTimerManager();

        if (timerManager.hasTimer(e.getPlayer(), TimerType.STUCK)) {

            if (stuckLocations.containsKey(e.getPlayer().getUniqueId())) {

                Location startLocation = stuckLocations.get(e.getPlayer().getUniqueId());

                double distance = startLocation.distance(e.getPlayer().getLocation());

                if (distance > instance.getSettings().getDouble("timers.stuck.movement")) {

                    e.getPlayer().sendMessage(ChatColor.RED + "You moved more than 5 blocks, your stuck timer has been cancelled.");
                    instance.getTimerManager().removeTimer(e.getPlayer(), instance.getTimerManager().getTimer(e.getPlayer(), TimerType.STUCK));

                }

            }

        }

    }

}
