package support.plugin.onyx.factions.claim;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import support.plugin.onyx.factions.Faction;
import support.plugin.onyx.utils.LocationUtils;

/**
 * Created by eric on 31/08/2017.
 */
@Builder
public class Claim {

    @Getter @Setter
    private Location cornerA, cornerB;

    @Getter @Setter
    private Faction owner;

    public Claim(){}

    public boolean insideClaim(Location location){

        return LocationUtils.betweenPoints(location, cornerA, cornerB);

    }

    public boolean insideClaim(Player player){

        return LocationUtils.betweenPoints(player.getLocation(), cornerA, cornerB);

    }

}
