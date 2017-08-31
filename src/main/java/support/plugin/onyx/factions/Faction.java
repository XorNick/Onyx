package support.plugin.onyx.factions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.factions.claim.Claim;
import support.plugin.onyx.factions.enums.FactionRole;

import java.util.*;

/**
 * Created by eric on 31/08/2017.
 */
@Builder
public class Faction {

    @Getter @Setter
    private UUID factionId;

    @Getter @Setter
    private String factionName;

    @Getter @Setter
    private UUID factionOwner;

    @Getter @Setter
    private HashMap<UUID, FactionRole> factionMembers;

    @Getter @Setter
    private Set<Claim> factionClaims;

    @Getter @Setter
    private double balance;

    @Getter @Setter
    private int lives;

    @Getter @Setter
    private double dtr;

    @Getter
    private Set<Faction> allies;

    @Getter
    private Set<Faction> invitedAllies;

    @Getter
    private Set<UUID> invitedPlayers;

    @Getter
    private Date freezeTime;

    public Faction(UUID factionOwner){
        this.factionOwner = factionOwner;

        this.dtr = Onyx.getInstance().getSettings().getDouble("dtr.starting");

        factionMembers = new HashMap<>();
        factionClaims = new HashSet<>();
        allies = new HashSet<>();
        invitedPlayers = new HashSet<>();
    }

}
