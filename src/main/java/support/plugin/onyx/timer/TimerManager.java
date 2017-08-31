package support.plugin.onyx.timer;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by eric on 31/08/2017.
 */
public class TimerManager {

    @Getter
    private ConcurrentHashMap<UUID, Set<ITimer>> activeTimers;

    /*

    Probably not the best way to handle a SOTW time, but it's easier...

     */
    @Getter @Setter
    private boolean sotwActive;

    @Getter @Setter
    private long sotwTime;

    public TimerManager(){}

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
