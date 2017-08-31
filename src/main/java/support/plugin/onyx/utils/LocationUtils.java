package support.plugin.onyx.utils;

import org.bukkit.Location;

/**
 * Created by eric on 31/08/2017.
 */
public class LocationUtils {

    public static boolean betweenPoints(Location location, Location position1, Location position2){

        if((location.getX() > position1.getX())
                && (location.getY() > position1.getY())
                && (location.getZ() > position1.getZ())
                && (location.getX() < position2.getX())
                && (location.getY() < position2.getY())
                && (location.getZ() < position2.getZ())) { // Phat bit of code here...
            return true;
        }

        return false;

    }

}
