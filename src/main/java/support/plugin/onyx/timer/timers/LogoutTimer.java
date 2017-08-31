package support.plugin.onyx.timer.timers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import support.plugin.onyx.timer.ITimer;
import support.plugin.onyx.timer.TimerType;

import java.util.UUID;

/**
 * Created by eric on 31/08/2017.
 */
public class LogoutTimer implements ITimer {

    @Getter @Setter
    private UUID player;

    @Getter @Setter
    private TimerType type;

    @Setter
    private Long time;

    @Getter
    private boolean isFrozen;

    public LogoutTimer(Player player, TimerType timerType, Long time){

        this.player = player.getUniqueId();
        this.type = timerType;
        this.time = time;
        this.isFrozen = false;

    }

    public Long getTime(){
        return time - System.currentTimeMillis();
    }

    public void toggleFreeze(){

        if(isFrozen){
            isFrozen = false;
        }else{
            isFrozen = true;
        }

    }

}
