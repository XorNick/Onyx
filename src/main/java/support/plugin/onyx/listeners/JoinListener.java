package support.plugin.onyx.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.factions.Faction;
import support.plugin.onyx.profiles.GameProfile;
import support.plugin.onyx.profiles.dto.ChatMode;
import support.plugin.onyx.timer.TimerType;
import support.plugin.onyx.timer.timers.Timer;

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
public class JoinListener implements Listener {

    private Onyx instance;

    public JoinListener(Onyx instance) {

        this.instance = instance;

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        GameProfile profile;

        if (instance.getProfileManager().getUser(player.getUniqueId()) == null) {

            profile = new GameProfile(player.getUniqueId());
            profile.setChatMode(ChatMode.PUBLIC);
            profile.setDeathban(0L);

            instance.getProfileManager().createUser(profile);

        } else {

            profile = instance.getProfileManager().getUser(player.getUniqueId());

        }

        Location spawn = new Location(player.getWorld(), 0, player.getWorld().getHighestBlockYAt(0, 0), 0);

        if (!player.hasPlayedBefore()) {

            if (instance.getSettings().getBoolean("map.kitmap")) {

                player.teleport(spawn);
                return;

            }

            if (!instance.getTimerManager().isSotwActive()) {

                Timer timer = new Timer(player, TimerType.INVINCIBILITY, (60000 * 30) + System.currentTimeMillis());
                instance.getTimerManager().giveTimer(player, timer);
                player.sendMessage(ChatColor.GREEN + "Your invincibility timer is now activated.");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        timer.setFrozen(true);
                    }
                }.runTaskLaterAsynchronously(Onyx.getInstance(), 4L);

            }

            player.teleport(spawn);
            player.getInventory().clear();
            player.updateInventory();
            profile.setBalance(Onyx.getInstance().getSettings().getDouble("map.player_start.balance"));
            player.sendMessage(ChatColor.GREEN + "Your balance has been set to $" + profile.getBalance());


        }

        if (instance.getFactionManager().getFactionByMember(player.getUniqueId()) != null) {

            Faction faction = instance.getFactionManager().getFactionByMember(player.getUniqueId());

            faction.sendMessage(ChatColor.GREEN + "Member online: " + ChatColor.BOLD + faction.getRole(player.getUniqueId()).getPrefix() + player.getName() + ChatColor.GREEN + " (" + faction.getOnlinePlayers().size() + "/" + faction.getFactionMembers().size() + ")");

        }

    }

}
