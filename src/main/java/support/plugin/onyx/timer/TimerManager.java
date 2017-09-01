package support.plugin.onyx.timer;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import support.plugin.onyx.Onyx;
import support.plugin.onyx.timer.dao.TimerDao;

import java.util.ArrayList;
import java.util.Set;
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
    private ConcurrentHashMap<UUID, Set<ITimer>> activeTimers;

    /*

    Probably not the best way to handle a SOTW time, but it's easier...

     */
    @Getter @Setter
    private boolean sotwActive;

    @Getter @Setter
    private long sotwTime;

    @Getter
    private TimerDao timerDao;


    public TimerManager(Onyx instance){

        this.instance = instance;

        timerDao = new TimerDao(
                instance.getSettings().getString("database.hostname"),
                instance.getSettings().getString("database.port"),
                instance.getSettings().getString("database.auth_key")
        );

        activeTimers = timerDao.getAll(); // Restoring timers from database

    }

    public boolean hasTimer(Player player){

        if(activeTimers.contains(player.getUniqueId())){
            return true;
        }

        return false;

    }

    public Set<ITimer> getTimers(Player player){

        if(hasTimer(player)){

            return activeTimers.get(player.getUniqueId());

        }

        return null;

    }

    public void removeTimer(Player player, ITimer timer){

        if(hasTimer(player, timer.getType())){

            Set<ITimer> timers = getTimers(player);

            timers.remove(timer);

            activeTimers.remove(player.getUniqueId());
            activeTimers.put(player.getUniqueId(), timers);

        }else{

            return;

        }

    }

    public void giveTimer(Player player, ITimer timer){

        if(hasTimer(player)){

            //Currently has a timer, add to current ones
            Set<ITimer> timers = getTimers(player);

            timers.add(timer);

            activeTimers.remove(player.getUniqueId());
            activeTimers.put(player.getUniqueId(), timers);

        }else{

            Set<ITimer> timers = getTimers(player);

            timers.add(timer);

            activeTimers.put(player.getUniqueId(), timers);

        }

    }

    public boolean hasTimer(Player player, TimerType timerType){

        if(hasTimer(player)){

            if(getTimers(player).contains(timerType)){
                return true;
            }

        }

        return false;

    }

    public ITimer getTimer(Player player, TimerType timerType){

        if(hasTimer(player)){

            for(ITimer timer : getTimers(player)){

                if(timer.getType() == timerType){
                    return timer;
                }

            }

        }

        return null;

    }

}
