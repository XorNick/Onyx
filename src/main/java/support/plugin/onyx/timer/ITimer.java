package support.plugin.onyx.timer;

import java.util.UUID;

/**
 * Created by eric on 31/08/2017.
 */
public interface ITimer {

    Long getTime();

    TimerType getType();

    UUID getPlayer();

    void toggleFreeze();

    boolean isFrozen();

}
