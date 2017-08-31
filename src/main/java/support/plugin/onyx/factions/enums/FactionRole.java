package support.plugin.onyx.factions.enums;

import lombok.Getter;

/**
 * Created by eric on 31/08/2017.
 */
public enum FactionRole {

    OWNER(4),
    CO_OWNER(3),
    OFFICER(2),
    MEMBER(1);

    @Getter
    int rank;

    FactionRole(int rank){
        this.rank = rank;
    }

}
