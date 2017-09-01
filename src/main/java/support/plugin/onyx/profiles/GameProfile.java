package support.plugin.onyx.profiles;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.profiles.dto.Death;
import support.plugin.onyx.profiles.dto.Kill;

import java.util.List;
import java.util.UUID;

/**
 * Created by eric on 01/09/2017.
 */
public class GameProfile {

    @Getter
    private UUID uuid;

    @Getter @Setter
    private List<Kill> kills;

    @Getter @Setter
    private List<Death> deaths;

    @Getter @Setter
    private int soulboundLives;

    @Getter @Setter
    private int friendLives;

    @Getter @Setter
    private long deathban;

    public GameProfile(UUID uuid){
        this.uuid = uuid;

        runTask();
    }

    public boolean useSoulboundLife(){

        deathban = 0;
        soulboundLives--;
        return true;

    }

    public void runTask(){

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Onyx.getInstance(), new Runnable() {
            @Override
            public void run() {

                if(deathban < 0){
                    deathban = 0;
                }

                if(deathban > 0){
                    deathban = deathban-1000;
                }

            }
        },20L, 20L);

    }

}
