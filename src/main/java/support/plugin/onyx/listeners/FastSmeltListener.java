package support.plugin.onyx.listeners;

import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import support.plugin.onyx.Onyx;

import java.util.LinkedList;

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
public class FastSmeltListener implements Listener {

    private LinkedList<Furnace> furnaces = new LinkedList<>();
    private LinkedList<BrewingStand> stands = new LinkedList<>();

    public FastSmeltListener() {
        new BukkitRunnable() {
            public void run() {
                for (Furnace furnace : furnaces) {
                    if (furnace.getInventory().getItem(0) != null) {
                        furnace.setCookTime((short) (furnace.getCookTime() + 20));
                        furnace.setBurnTime((short) (furnace.getBurnTime() + 5));
                    } else {
                        furnace.setCookTime((short) 0);
                        furnace.setBurnTime((short) 0);
                    }
                }
                for (BrewingStand stand : stands) {
                    if ((stand.getLocation().getChunk().isLoaded()) && (stand.getBrewingTime() > 1))
                        stand.setBrewingTime(Math.max(1, stand.getBrewingTime() - 10));
                }
            }
        }.runTaskTimer(Onyx.getInstance(), 2L, 2L);
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent e) {
        Furnace furnace = (Furnace) e.getFurnace().getState();
        if (!this.furnaces.contains(furnace))
            this.furnaces.add(furnace);
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent e) {
        Furnace furnace = (Furnace) e.getFurnace().getState();
        if (!this.furnaces.contains(furnace))
            this.furnaces.add(furnace);
    }

    @EventHandler
    public void onBrew(BrewEvent e) {
        BrewingStand brewingStand = (BrewingStand) e.getBlock().getState();
        if (!this.stands.contains(brewingStand))
            this.stands.add(brewingStand);
    }

    @EventHandler
    public void onClickBlock(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.BREWING_STAND) {
                BrewingStand brewingStand = (BrewingStand) e.getClickedBlock().getState();
                if (!this.stands.contains(brewingStand))
                    this.stands.add(brewingStand);
            }
            if ((e.getClickedBlock().getType() == Material.FURNACE) || (e.getClickedBlock().getType() == Material.BURNING_FURNACE)) {
                Furnace furnace = (Furnace) e.getClickedBlock().getState();
                if (!this.furnaces.contains(furnace))
                    this.furnaces.add(furnace);
            }
        }
    }

}
