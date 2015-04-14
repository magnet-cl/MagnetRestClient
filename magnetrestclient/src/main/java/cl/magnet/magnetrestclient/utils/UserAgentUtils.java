package cl.magnet.magnetrestclient.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by lukas on 06-04-15.
 */
public final class UserAgentUtils {

    private UserAgentUtils() {
        // this class shouldn't be instantiated
    }

    public static String getUserAgent(Context context) {
        String applicationId = (String) getBuildConfigValue(context, "APPLICATION_ID");
        String versionName = (String) getBuildConfigValue(context, "VERSION_NAME");

        return getUserAgent(applicationId, versionName);
    }

    public static String getUserAgent(String applicationId, String versionName) {
        return applicationId
                + "/"
                + versionName
                + " (Android "
                + Build.VERSION.RELEASE
                + "; "
                + Build.MODEL
                + ")";
    }

    /**
     * Gets a field from the project's app BuildConfig.
     *
     * @param context       Used to find the correct file
     * @param fieldName     The name of the field-to-access
     * @return              The value of the field, or {@code null} if the field is not found.
     */
    private static Object getBuildConfigValue(Context context, String fieldName) {
        try {
            Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");
            Field field = clazz.getField(fieldName);
            return field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
