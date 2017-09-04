package support.plugin.onyx.timer;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.timer.dao.TimerDao;
import support.plugin.onyx.timer.timers.Timer;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*

Copyright (c) 2017 PluginManager LTD

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

 */
public class TimerManager {

    @Getter
    private Onyx instance;

    @Getter
    private ConcurrentHashMap<UUID, List<Timer>> activeTimers;

    /*

    Probably not the best way to handle a SOTW time, but it's easier...

     */
    @Getter
    @Setter
    private boolean sotwActive;

    @Getter
    @Setter
    private long sotwTime;

    @Getter
    private TimerDao timerDao;


    public TimerManager(Onyx instance) {

        this.instance = instance;

        timerDao = new TimerDao(
                instance.getSettings().getString("database.hostname"),
                instance.getSettings().getString("database.port"),
                (instance.getSettings().getString("database.auth_key") == "" ? null : instance.getSettings().getString("database.auth_key"))
        );

        activeTimers = timerDao.getAll(); // Restoring timers from database

    }

    public void save() {

        this.timerDao.saveAll(activeTimers);

    }

    public void removeTimer(Player player, Timer timer) {

        if (hasTimer(player, timer.getType())) {

            List<Timer> timers = getActiveTimers().get(player.getUniqueId());

            timers.remove(timer);

            activeTimers.remove(player.getUniqueId());
            activeTimers.put(player.getUniqueId(), timers);

        }

    }

    public boolean hasTimer(Player player, TimerType timerType) {
        if (activeTimers.get(player.getUniqueId()) == null)
            return false;

        for (Timer timer : this.activeTimers.get(player.getUniqueId())) {
            if (timer.getType() == timerType) {
                if (timer.getTime() > 0L)
                    return true;
            }
        }
        return false;
    }

    public void giveTimer(Player player, Timer defaultTimer) {
        if (activeTimers.get(player.getUniqueId()) == null) {
            List<Timer> timersList = new LinkedList<>();
            timersList.add(defaultTimer);
            activeTimers.put(player.getUniqueId(), timersList);
            return;
        }
        List<Timer> timersList = this.activeTimers.get(player.getUniqueId());
        timersList.add(defaultTimer);
        activeTimers.put(player.getUniqueId(), timersList);
    }

    public boolean hasActiveTimers(Player player) {
        if (this.activeTimers.containsKey(player.getUniqueId()))
            return false;

        if (this.sotwActive)
            return true;

        return this.activeTimers.get(player.getUniqueId()).stream().anyMatch(timer -> timer.getTime() > 0);
    }

    public Timer getTimer(Player player, TimerType timerType) {
        if (activeTimers.get(player.getUniqueId()) == null)
            return null;

        for (Timer timer : this.activeTimers.get(player.getUniqueId())) {
            if (timer.getType() == timerType) {
                if (timer.getTime() > 0L)
                    return timer;
            }
        }

        return null;
    }

}
