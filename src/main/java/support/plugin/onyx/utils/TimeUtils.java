package support.plugin.onyx.utils;

/**
 * Created by eric on 31/08/2017.
 */
public class TimeUtils {

    public static Long secondsFromMillis(Long millis){

        return (millis / 1000) % 60;

    }

    public static Long minutesFromMillis(Long millis){

        return (millis / (1000 * 60)) % 60;

    }

    public static Long hoursFromMillis(Long millis){
        return (millis / (1000 * 60 * 60)) % 24;
    }

}
