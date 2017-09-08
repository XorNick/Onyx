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

/**
 * Handles the faction system
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

    /**
     * Saves data to the keystore through the DAO
     */
    public void save() {

        factionsDao.saveAll(factions);

    }

    /**
     * Gets the faction by the faction's ID
     *
     * @param uuid the faction's uuid
     * @return the faction from the uuid
     */
    public Faction getFactionById(UUID uuid) {

        for (Faction faction : factions) {

            if (faction.getFactionId() == uuid) {
                return faction;
            }

        }

        return null;

    }

    /**
     * Gets a faction by a member's UUID
     * @param uuid the member's uuid
     * @return the faction from the member
     */
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

    /**
     * Gets a faction by a members's name
     * @param playerName the player's name
     * @return the faction from the player
     */
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

    /**
     * Gets a faction by it's name
     * @param name the faction's name
     * @return the faction
     */
    public Faction getFactionByName(String name) {

        for (Faction faction : factions) {

            if (faction.getFactionName().equalsIgnoreCase(name)) {
                return faction;
            }

        }

        return null;
    }

    /**
     * Get a faction by it's claim
     * @param location the location in question
     * @return the faction from the claim
     */
    public Faction getFactionByClaimLocation(Location location) {

        for (Faction faction : factions) {

            for (Claim claim : faction.getFactionClaims()) {

                if (claim.insideClaim(location)) {
                    return faction;
                }

            }

        }

        return null;

    }

    /**
     * Gets a claim by it's location
     * @param location the location of the claim
     * @return the claim
     */
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

    /**
     * Gets all faction officers as UUIDs
     * @param faction the faction in question
     * @return the officers
     */
    public Set<UUID> getFactionOfficers(Faction faction) {

        Set<UUID> officers = new HashSet<>();

        for (UUID factionMember : faction.getFactionMembers().keySet()) {

            if (faction.getFactionMembers().get(factionMember).getRank() >= 2) {
                officers.add(factionMember);
            }

        }

        return officers;

    }

    /**
     * Checks if the player is an officer or higher
     * @param player the player in question
     * @return whether the player is an officer+ or not
     */
    public boolean isOfficerOrHigher(Player player) {

        Faction faction = getFactionByMember(player.getUniqueId());

        return faction.getFactionMembers().get(player.getUniqueId()).getRank() >= 2;

    }

    /**
     * Checks if the faction is raidable
     * @param faction the faction in question
     * @return whether the faction is raidable or not
     */
    public boolean isRaidable(Faction faction) {

        return faction.getDtr() < 0;

    }

    /**
     * Creates a faction
     * @param faction the faction in question
     */
    public void createFaction(Faction faction) {

        factions.add(faction);
        factionsDao.insert(faction);

    }

    /**
     * Disbands a faction
     * @param faction the faction in question
     */
    public void disbandFaction(Faction faction) {

        factions.remove(faction);
        factionsDao.delete(faction);

    }

    /**
     * Runs a task for every second, handles DTR regen
     */
    private synchronized void runTasks() {

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Onyx.getInstance(), new Runnable() {
            @Override
            public void run() {

                for (Faction faction : factions) {

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

                for (Faction faction : factions) {

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

}
