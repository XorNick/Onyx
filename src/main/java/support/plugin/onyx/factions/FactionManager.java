package support.plugin.onyx.factions;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.factions.claim.Claim;
import support.plugin.onyx.factions.dao.FactionsDao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by eric on 31/08/2017.
 */
public class FactionManager {

    private Onyx instance;

    private FactionsDao factionsDao;

    private List<Faction> factions;

    public FactionManager(Onyx instance){
        this.instance = instance;

        factionsDao = new FactionsDao(
                instance.getSettings().getString("database.hostname"),
                instance.getSettings().getString("database.port"),
                instance.getSettings().getString("database.auth_key")
        );

        factions = factionsDao.getAll(); // Loading all factions...
    }

    public Faction getFactionById(UUID uuid){

        for(Faction faction : factions){

            if(faction.getFactionId() == uuid){
                return faction;
            }

        }

        return null;

    }

    public Faction getFactionByMember(UUID uuid){

        for(Faction faction : factions){

            for(UUID memberUUID : faction.getFactionMembers().keySet()){

                if(memberUUID == uuid){
                    return faction;
                }

            }

        }

        return null;

    }

    public Faction getFactionByClaim(Location location){

        for(Faction faction : factions){

            for(Claim claim : faction.getFactionClaims()){

                if(claim.insideClaim(location)){

                    return faction;

                }

            }

        }

        return null;

    }

    public Set<UUID> getFactionOfficers(Faction faction){

        Set<UUID> officers = new HashSet<>();

        for(UUID factionMember : faction.getFactionMembers().keySet()){

            if(faction.getFactionMembers().get(factionMember).getRank() >= 2){
                officers.add(factionMember);
            }

        }

        return officers;

    }

    public boolean isOfficerOrHigher(Player player){

        Faction faction = getFactionByMember(player.getUniqueId());

        if(faction.getFactionMembers().get(player.getUniqueId()).getRank() >= 2){
            return true;
        }

        return false;

    }

    public boolean isRaidable(Faction faction){

        if(faction.getDtr() < 0){
            return true;
        }

        return false;

    }

    public void createFaction(Faction faction){

        factions.add(faction);
        factionsDao.insert(faction);

    }

    public void disbandFaction(Faction faction){

        factions.remove(faction);
        factionsDao.delete(faction);

    }

}
