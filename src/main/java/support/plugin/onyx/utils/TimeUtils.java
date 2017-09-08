package support.plugin.onyx.utils;

import java.util.concurrent.TimeUnit;

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
 * Some utils for time formatting
 */
public class TimeUtils {

    /**
     * Converts milliseconds to seconds
     *
     * @param millis the milliseconds
     * @return the seconds
     */
    public static Long secondsFromMillis(Long millis) {

        return (millis / 1000) % 60;

    }

    /**
     * Converts milliseconds to minutes
     * @param millis the milliseconds
     * @return the minutes
     */
    public static Long minutesFromMillis(Long millis) {

        return (millis / (1000 * 60)) % 60;

    }

    /**
     * Converts milliseconds to hours
     * @param millis the milliseconds
     * @return the hours
     */
    public static Long hoursFromMillis(Long millis) {
        return (millis / (1000 * 60 * 60)) % 24;
    }

    /**
     * Parses a human time string (like 2h4s) to millis.
     * @param input the dirty human person time string
     * @return the clean computer milliseconds
     */
    public static long parseHumanTime(String input) {
        if (input == null || input.isEmpty()) {
            return -1L;
        }
        long result = 0L;
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                number.append(c);
            } else {
                String str;
                if (Character.isLetter(c) && !(str = number.toString()).isEmpty()) {
                    result += convert(Integer.parseInt(str), c);
                    number = new StringBuilder();
                }
            }
        }
        return result;
    }

    /**
     * Converts certain units to milliseconds
     * @param value the amount of time in a unit
     * @param unit the unit of time
     * @return a converted string from (ex. 24h) to whatever 24h is in milliseconds (work it out, lazy)
     */
    private static long convert(int value, char unit) {
        switch (unit) {
            case 'y': {
                return value * TimeUnit.DAYS.toMillis(365L);
            }
            case 'M': {
                return value * TimeUnit.DAYS.toMillis(30L);
            }
            case 'd': {
                return value * TimeUnit.DAYS.toMillis(1L);
            }
            case 'h': {
                return value * TimeUnit.HOURS.toMillis(1L);
            }
            case 'm': {
                return value * TimeUnit.MINUTES.toMillis(1L);
            }
            case 's': {
                return value * TimeUnit.SECONDS.toMillis(1L);
            }
            default: {
                return -1L;
            }
        }
    }

}
