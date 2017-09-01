package support.plugin.onyx.profiles;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;
import org.bukkit.Bukkit;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.profiles.dto.ChatMode;
import support.plugin.onyx.profiles.dto.Death;
import support.plugin.onyx.profiles.dto.Kill;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by eric on 01/09/2017.
 */
@Builder
public class GameProfile {

    @Getter
    private UUID uuid;

    @Getter
    @Setter
    private Set<Kill> kills;

    @Getter
    @Setter
    private Set<Death> deaths;

    @Getter
    @Setter
    private int soulboundLives;

    @Getter
    @Setter
    private int friendLives;

    @Getter
    @Setter
    private long deathban;

    @Getter
    @Setter
    private ChatMode chatMode;

    @Getter
    @Setter
    private boolean foundDiamonds;

    @Getter
    @Setter
    private boolean playersInSpawn;

    @Getter
    @Setter
    private double balance;

    public GameProfile(UUID uuid) {
        this.uuid = uuid;

        kills = new HashSet<>();
        deaths = new HashSet<>();

        soulboundLives = 0;
        friendLives = 0;
        deathban = 0L;

        chatMode = ChatMode.PUBLIC;

        foundDiamonds = false;

        playersInSpawn = false;

        balance = 0.00;

        runTask();
    }

    public boolean useSoulboundLife() {

        deathban = 0;
        soulboundLives--;
        return true;

    }

    public void runTask() {

        // ew.

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Onyx.getInstance(), new Runnable() {
            @Override
            public void run() {

                if (deathban < 0) {
                    deathban = 0;
                }

                if (deathban > 0) {
                    deathban = deathban - 1000;
                }

            }
        }, 20L, 20L);

    }

}
