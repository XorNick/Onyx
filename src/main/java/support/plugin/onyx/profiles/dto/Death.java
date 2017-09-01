package support.plugin.onyx.profiles.dto;

import lombok.Getter;
import lombok.experimental.Builder;
import org.bukkit.inventory.ItemStack;

import javax.xml.stream.Location;
import java.util.UUID;

/**
 * Created by eric on 01/09/2017.
 */
@Builder
public class Death {

    @Getter
    private UUID uuid;

    @Getter
    private Location deathLocation;

    @Getter
    private UUID killer;

    @Getter
    private ItemStack[] inventoryContents;

    public Death(UUID uuid, UUID killer) {
        this.uuid = uuid;
        this.killer = killer;
    }

}
