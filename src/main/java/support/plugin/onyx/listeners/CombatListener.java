package support.plugin.onyx.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.factions.Faction;
import support.plugin.onyx.factions.FactionManager;
import support.plugin.onyx.timer.TimerManager;
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

/**
 * Handles the combat tagging and tracking (works with the TimerManager)
 */
public class CombatListener implements Listener {

    /*

    TODO: TEST

     */

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player) {

            Player player = (Player) e.getEntity();

            if (Onyx.getInstance().getTimerManager().isSotwActive()) {

                // Fully stop any SOTW damage
                e.setCancelled(true);

            } else if (Onyx.getInstance().getFactionManager().getFactionByClaimLocation(e.getEntity().getLocation()) != null && !Onyx.getInstance().getFactionManager().getClaimByLocation(e.getEntity().getLocation()).isDeathban()) {

                e.setCancelled(true);

            } else if (Onyx.getInstance().getTimerManager().hasActiveTimers(player) && Onyx.getInstance().getTimerManager().hasTimer(player, TimerType.INVINCIBILITY)) {

                e.setCancelled(true);

            }

        }

    }

    @EventHandler
    public void playerDamage(EntityDamageByEntityEvent e) {

        if (Onyx.getInstance().getTimerManager().isSotwActive()) {
            return;
        }

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {

            Player damager = (Player) e.getDamager();
            Player damaged = (Player) e.getEntity();

            if (Onyx.getInstance().getTimerManager().hasTimer(damager, TimerType.INVINCIBILITY)) {
                e.setCancelled(true);
            } else if (Onyx.getInstance().getTimerManager().hasTimer(damaged, TimerType.INVINCIBILITY)) {
                e.setCancelled(true);
            }

            FactionManager factionManager = Onyx.getInstance().getFactionManager();

            if (factionManager.getFactionByMember(damaged.getUniqueId()) != null && factionManager.getFactionByMember(damager.getUniqueId()) != null) {

                Faction damagedFaction = factionManager.getFactionByMember(damaged.getUniqueId());
                Faction damagerFaction = factionManager.getFactionByMember(damager.getUniqueId());

                if (damagedFaction.isAllied(damagerFaction)) {
                    if (Onyx.getInstance().getSettings().getBoolean("allies.damage")) {
                        damager.sendMessage(ChatColor.translateAlternateColorCodes('&', Onyx.getInstance().getLocale().getString("allies.damage_alert")));
                    } else {
                        damager.sendMessage(ChatColor.translateAlternateColorCodes('&', Onyx.getInstance().getLocale().getString("allies.no_damage")));
                        e.setDamage(0);
                        e.setCancelled(true);
                    }
                }

            }

            if (factionManager.getFactionByClaimLocation(damaged.getLocation()) != null) {
                if (!factionManager.getClaimByLocation(damaged.getLocation()).isDeathban()) {

                    e.setDamage(0);
                    e.setCancelled(true);
                    return;

                }
            }

            if (factionManager.getFactionByClaimLocation(damager.getLocation()) != null) {
                if (!factionManager.getClaimByLocation(damager.getLocation()).isDeathban()) {

                    e.setDamage(0);
                    e.setCancelled(true);
                    return;

                }
            }

            TimerManager timerManager = Onyx.getInstance().getTimerManager();

            if (timerManager.hasTimer(damaged, TimerType.ARCHER)) {

                e.setDamage(e.getDamage() * 1.25);

            }

            if (Onyx.getInstance().getSettings().getBoolean("timers.combat.enabled")) {

                if (timerManager.hasTimer(damaged, TimerType.COMBAT)) {

                    timerManager.getActiveTimers().get(damaged.getUniqueId()).remove(timerManager.getTimer(damaged, TimerType.COMBAT));

                } else {

                    damaged.sendMessage(ChatColor.RED + "You are now combat tagged for " + Onyx.getInstance().getSettings().getInt("timers.combat.time") + " seconds.");

                }

                if (timerManager.hasTimer(damager, TimerType.COMBAT)) {

                    timerManager.getActiveTimers().get(damager.getUniqueId()).remove(timerManager.getTimer(damager, TimerType.COMBAT));

                } else {

                    damager.sendMessage(ChatColor.RED + "You are now combat tagged for " + Onyx.getInstance().getSettings().getInt("timers.combat.time") + " seconds.");

                }

                timerManager.getActiveTimers().get(damaged.getUniqueId()).add(new Timer(damaged, TimerType.COMBAT, (Onyx.getInstance().getSettings().getInt("timers.combat.time") * 1000) + System.currentTimeMillis()));
                timerManager.getActiveTimers().get(damager.getUniqueId()).add(new Timer(damager, TimerType.COMBAT, (Onyx.getInstance().getSettings().getInt("timers.combat.time") * 1000) + System.currentTimeMillis()));

            }

            if (timerManager.hasTimer(damaged, TimerType.HOME)) {

                damaged.sendMessage(ChatColor.RED + "You took damage, therefor your teleportation timer was cancelled.");
                timerManager.getActiveTimers().get(damaged.getUniqueId()).remove(timerManager.getTimer(damaged, TimerType.HOME));

            } else if (timerManager.hasTimer(damaged, TimerType.STUCK)) {

                damaged.sendMessage(ChatColor.RED + "You took damage, therefor your teleportation timer was cancelled.");
                timerManager.getActiveTimers().get(damaged.getUniqueId()).remove(timerManager.getTimer(damaged, TimerType.STUCK));

            } else if (timerManager.hasTimer(damaged, TimerType.LOGOUT)) {

                damaged.sendMessage(ChatColor.RED + "You took damage, therefor your logout timer was cancelled.");
                timerManager.getActiveTimers().get(damaged.getUniqueId()).remove(timerManager.getTimer(damaged, TimerType.HOME));

            }

        }

    }

}
