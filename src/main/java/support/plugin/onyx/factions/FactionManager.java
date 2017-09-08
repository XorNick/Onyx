package support.plugin.onyx.factions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.factions.claim.Claim;
import support.plugin.onyx.factions.dao.FactionsDao;

import java.util.HashSet;
import java.util.List;
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
public class FactionManager {

    private Onyx instance;

    private FactionsDao factionsDao;

    private List<Faction> factions;

    public FactionManager(Onyx instance) {
        this.instance = instance;

        factionsDao = new FactionsDao(
                instance.getSettings().getString("database.hostname"),
                instance.getSettings().getString("database.port"),
                (instance.getSettings().getString("database.auth_key") == "" ? null : instance.getSettings().getString("database.auth_key"))
        );

        factions = factionsDao.getAll(); // Loading all factions...
        runTasks();
    }

    private synchronized void runTasks() {



            Bukkit.getScheduler().scheduleAsyncRepeatingTask(Onyx.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for(Faction faction : factions) {
                        if (faction.isSystemFaction()) {
                            return;
                        }

                        if (faction.isFrozen()) {
                            if (System.currentTimeMillis() / 1000 - faction.getFreezeTime()[1] >= faction.getFreezeTime()[0]) {
                                faction.setFreezeTime(null);
                            }
                        }
                    }
                }
            }, 20L, 20L);
            Bukkit.getScheduler().scheduleAsyncRepeatingTask(Onyx.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for(Faction faction : factions) {
                        if (faction.isSystemFaction()) {
                            return;
                        }

                        if (faction.isFrozen()) {
                            if (!(faction.isFrozen()) && faction.getDtr() < faction.getMaxDtr()) {
                                faction.setDtr(faction.getDtr() + Onyx.getInstance().getSettings().getInt("dtr.regeneration.addition"));
                            }
                        }
                    }
                }
            }, Onyx.getInstance().getSettings().getInt("dtr.regeneration.interval") * 20L, Onyx.getInstance().getSettings().getInt("dtr.regeneration.interval") * 20L);



    }

    public void save() {

        factionsDao.saveAll(factions);

    }

    public Faction getFactionById(UUID uuid) {

        for (Faction faction : factions) {

            if (faction.getFactionId() == uuid) {
                return faction;
            }

        }

        return null;

    }

    public Faction getFactionByMember(UUID uuid) {

        for (Faction faction : factions) {

            for (UUID memberUUID : faction.getFactionMembers().keySet()) {

                if (memberUUID == uuid) {
                    return faction;
                }

            }

        }

        return null;

    }

    public Faction getFactionByPlayerName(String playerName) {

        for (Faction faction : factions) {

            for (Player player : faction.getOnlinePlayers()) {

                if (player.getName().equalsIgnoreCase(playerName)) {
                    return faction;
                }

            }

        }

        return null;

    }

    public Faction getFactionByName(String name) {

        for (Faction faction : factions) {

            if (faction.getFactionName().equalsIgnoreCase(name)) {
                return faction;
            }

        }

        return null;
    }

    public Faction getFactionByClaim(Location location) {

        for (Faction faction : factions) {

            for (Claim claim : faction.getFactionClaims()) {

                if (claim.insideClaim(location)) {

                    return faction;

                }

            }

        }

        return null;

    }

    public Claim getClaimByLocation(Location location) {

        for (Faction faction : factions) {

            for (Claim claim : faction.getFactionClaims()) {

                if (claim.insideClaim(location)) {

                    return claim;

                }

            }

        }

        return null;

    }

    public Set<UUID> getFactionOfficers(Faction faction) {

        Set<UUID> officers = new HashSet<>();

        for (UUID factionMember : faction.getFactionMembers().keySet()) {

            if (faction.getFactionMembers().get(factionMember).getRank() >= 2) {
                officers.add(factionMember);
            }

        }

        return officers;

    }

    public boolean isOfficerOrHigher(Player player) {

        Faction faction = getFactionByMember(player.getUniqueId());

        return faction.getFactionMembers().get(player.getUniqueId()).getRank() >= 2;

    }

    public boolean isRaidable(Faction faction) {

        return faction.getDtr() < 0;

    }

    public void createFaction(Faction faction) {

        factions.add(faction);
        factionsDao.insert(faction);

    }

    public void disbandFaction(Faction faction) {

        factions.remove(faction);
        factionsDao.delete(faction);

    }

}
