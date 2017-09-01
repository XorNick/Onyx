package support.plugin.onyx.profiles.dto;

import lombok.Getter;
import lombok.experimental.Builder;

import javax.xml.stream.Location;
import java.util.UUID;

/**
 * Created by eric on 01/09/2017.
 */
@Builder
public class Kill {

    @Getter
    private UUID uuid;

    @Getter
    private Location location;

    @Getter
    private UUID killed;

    public Kill(UUID uuid, UUID killed){
        this.uuid = uuid;
        this.killed = killed;
    }

}
