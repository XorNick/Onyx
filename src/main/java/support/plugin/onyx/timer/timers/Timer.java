package support.plugin.onyx.timer.timers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import support.plugin.onyx.timer.ITimer;
import support.plugin.onyx.timer.TimerType;

import java.util.UUID;

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

/**
 * Timer object which uses the timer interface
 */
public class Timer implements ITimer {

    @Getter
    @Setter
    private UUID player;

    @Getter
    @Setter
    private TimerType type;

    @Setter
    private Long time;

    @Getter
    @Setter
    private boolean isFrozen;

    public Timer(Player player, TimerType timerType, Long time) {

        this.player = player.getUniqueId();
        this.type = timerType;
        this.time = time;
        this.isFrozen = false;

    }

    /**
     * Gets the time left on the timer
     *
     * @return the amount of time left
     */
    public Long getTime() {
        return time - System.currentTimeMillis();
    }

    /**
     * Toggles the freeze status of the timer
     */
    public void toggleFreeze() {

        isFrozen = !isFrozen;

    }

    /**
     * Converts the current time to seconds (from millis)
     * @return the amount of time left in seconds
     */
    public double toSeconds() {

        return getTime() * 1000;

    }

}
