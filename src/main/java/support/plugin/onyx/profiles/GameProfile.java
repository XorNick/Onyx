package support.plugin.onyx.profiles;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.profiles.dto.ChatMode;
import support.plugin.onyx.profiles.dto.Death;
import support.plugin.onyx.profiles.dto.Kill;

import java.util.HashSet;
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
 * Object for GameProfiles, nothing too special here
 */
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

    /**
     * Uses a soulbound life
     *
     * @return
     */
    public boolean useSoulboundLife() {

        deathban = 0;
        soulboundLives--;
        return true;

    }

    /**
     * Runs the deathban task
     */
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
