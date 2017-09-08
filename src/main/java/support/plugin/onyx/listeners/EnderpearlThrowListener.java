package support.plugin.onyx.listeners;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.InventoryHolder;
import support.plugin.onyx.Onyx;
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
 * Handles what happens when an enderpearl is thrown.
 */
public class EnderpearlThrowListener implements Listener {

    /*

    Credits to Seth for this class :)

     */

    private final ImmutableSet blockedPearlTypes;

    public EnderpearlThrowListener() {
        this.blockedPearlTypes = Sets.immutableEnumSet(Material.THIN_GLASS, Material.IRON_FENCE, Material.FENCE, Material.NETHER_FENCE, Material.FENCE_GATE, Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getPlayer().getItemInHand().getType().equals(Material.ENDER_PEARL)) {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {

                if (!Onyx.getInstance().getSettings().getBoolean("timers.enderpearl.enabled")) {
                    return;
                }

                if (player.getGameMode() == GameMode.CREATIVE) {
                    return;
                }

                TimerManager timerManager = Onyx.getInstance().getTimerManager();
                if (timerManager.getTimer(player, TimerType.ENDERPEARL) != null && timerManager.getTimer(player, TimerType.ENDERPEARL).getTime() > 0) {
                    long millisLeft = timerManager.getTimer(player, TimerType.ENDERPEARL).getTime();
                    double value = millisLeft / 1000.0D;
                    double sec = Math.round(10.0D * value) / 10.0D;

                    event.setCancelled(true);
                    event.getPlayer().updateInventory();
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou still have an enderpearl time for &c&l" + sec + "&c second(s)."));
                    return;
                }
                timerManager.giveTimer(player, new Timer(player, TimerType.ENDERPEARL, (Onyx.getInstance().getSettings().getInt("timers.enderpearl.time") * 1000) + System.currentTimeMillis()));

            }
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerInteract1(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.hasItem()) && (event.getItem().getType() == Material.ENDER_PEARL)) {
            Block block = event.getClickedBlock();
            if ((block.getType().isSolid()) && (!(block.getState() instanceof InventoryHolder))) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                player.setItemInHand(event.getItem());
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPearlClip(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Location to = event.getTo();
            if (this.blockedPearlTypes.contains(to.getBlock().getType())) {
                event.setCancelled(true);
                return;
            }
            to.setX(to.getBlockX() + 0.5D);
            to.setZ(to.getBlockZ() + 0.5D);
            event.setTo(to);
        }
    }

    @EventHandler
    public void onEnderpearlLand(PlayerTeleportEvent e) {

        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {

            Player player = e.getPlayer();
            TimerManager timerManager = Onyx.getInstance().getTimerManager();

            if (timerManager.hasTimer(player, TimerType.COMBAT)) {

                if (!Onyx.getInstance().getFactionManager().getClaimByLocation(e.getTo()).isDeathban()) {

                    player.sendMessage(ChatColor.RED + "You cannot enderpearl into safezone while in combat.");
                    e.setCancelled(true);

                }

            }

        }

    }

}
