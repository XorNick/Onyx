package support.plugin.onyx.factions;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.factions.claim.Claim;
import support.plugin.onyx.factions.enums.FactionRole;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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
public class Faction {

    @Getter
    @Setter
    private UUID factionId;

    @Getter
    @Setter
    private String factionName;

    @Getter
    @Setter
    private UUID factionOwner;

    @Getter
    @Setter
    private Location factionHome;

    @Getter
    @Setter
    private HashMap<UUID, FactionRole> factionMembers;

    @Getter
    @Setter
    private Set<Claim> factionClaims;

    @Getter
    @Setter
    private double balance;

    @Getter
    @Setter
    private int lives;

    @Getter
    @Setter
    private double dtr;

    @Getter
    private Set<Faction> allies;

    @Getter
    private Set<Faction> invitedAllies;

    @Getter
    private Set<UUID> invitedPlayers;

    @Getter
    @Setter
    private int[] freezeTime;

    @Getter
    @Setter
    private boolean systemFaction;

    public Faction(UUID factionOwner, String factionName) {
        this.factionOwner = factionOwner;

        factionMembers = new HashMap<>();
        factionClaims = new HashSet<>();
        allies = new HashSet<>();
        invitedAllies = new HashSet<>();
        invitedPlayers = new HashSet<>();

        factionMembers.put(factionOwner, FactionRole.OWNER);

        factionHome = null;
        balance = 0;
        lives = 0;
        dtr = Onyx.getInstance().getSettings().getDouble("dtr.starting");

        systemFaction = false;

        runTasks();
    }

    public double getMaxDtr() {
        if (systemFaction) {
            return 0.0;
        }

        return Onyx.getInstance().getSettings().getDouble("dtr.starting") + (Onyx.getInstance().getSettings().getDouble("dtr.per_player") * factionMembers.size());
    }

    public Set<Player> getOnlinePlayers() {

        Set<Player> online = new HashSet<>();

        for (UUID uuid : factionMembers.keySet()) {

            if (Bukkit.getPlayer(uuid) != null) {
                online.add(Bukkit.getPlayer(uuid));
            }

        }

        return online;

    }

    public Set<Player> getOnlineOfficers() {

        Set<Player> online = new HashSet<>();

        for (UUID uuid : factionMembers.keySet()) {

            if (Bukkit.getPlayer(uuid) != null) {
                if (factionMembers.get(uuid).getRank() >= 2) {
                    online.add(Bukkit.getPlayer(uuid));
                }
            }

        }

        return online;

    }

    public Set<Player> getOnlineAllies() {

        Set<Player> players = new HashSet<>();

        for (Faction ally : allies) {

            for (Player allied : ally.getOnlinePlayers()) {
                players.add(allied);
            }

        }

        return players;

    }


    public boolean contains(UUID uuid) {

        return factionMembers.containsKey(uuid);

    }

    public FactionRole getRole(UUID uuid) {

        return factionMembers.get(uuid);

    }

    public boolean isAllied(Faction faction) {

        return allies.contains(faction);

    }

    public boolean isFrozen() {
        return freezeTime != null;
    }

    public void freeze(int duration) {
        freezeTime = new int[]{duration, (int) (System.currentTimeMillis() / 1000)};
    }

    public void sendMessage(String message) {

        for (Player player : getOnlinePlayers()) {

            player.sendMessage(message);

        }

    }

    private synchronized void runTasks() {

        if (systemFaction) {
            return;
        }

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Onyx.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (isFrozen()) {
                    if (System.currentTimeMillis() / 1000 - freezeTime[1] >= freezeTime[0]) {
                        setFreezeTime(null);
                    }
                }
            }
        }, 20L, 20L);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Onyx.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (isFrozen()) {
                    if (!(isFrozen()) && dtr < getMaxDtr()) {
                        setDtr(getDtr() + Onyx.getInstance().getSettings().getInt("dtr.regeneration.addition"));
                    }
                }
            }
        }, Onyx.getInstance().getSettings().getInt("dtr.regeneration.interval") * 20L, Onyx.getInstance().getSettings().getInt("dtr.regeneration.interval") * 20L);
    }

    /*public List<String> showInfo(){

        return Arrays.asList(
                ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"),
                ChatColor.translateAlternateColorCodes('&', "&9"+getFactionName()+" &7["+getOnlinePlayers().size()+"/"+getFactionMembers().size()+"] &3- &eHome:&f "+factionHome.getX()+", "+factionHome.getZ()),
                ChatColor.translateAlternateColorCodes('&', "&eLeader: "),
                ChatColor.translateAlternateColorCodes('&', "&eCo-Leaders: "),
                ChatColor.translateAlternateColorCodes('&', "&eOfficers: "),
                ChatColor.translateAlternateColorCodes('&', "&eMembers: "),
                ChatColor.translateAlternateColorCodes('&', "&eAllies: "),
                ChatColor.translateAlternateColorCodes('&', "&eBalance: "),
                ChatColor.translateAlternateColorCodes('&', "&eDTR: "),
                ChatColor.translateAlternateColorCodes('&', "&eRegen: ")
        );

    }*/

}
