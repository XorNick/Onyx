package support.plugin.onyx.utils;

/**
 * Created by eric on 08/09/2017.
 */
public class DoubleUtils {

    public static boolean tryParse(String str) {

        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

}
