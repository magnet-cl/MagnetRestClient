/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Lukas Zorich, Magnet.cl
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package cl.magnet.magnetrestclient.utils;

import android.content.Context;
import android.os.Build;

import java.lang.reflect.Field;

/**
 * User agent utility class.
 *
 * Created by lukas on 06-04-15.
 */
public final class UserAgentUtils {

    private UserAgentUtils() {
        // this class shouldn't be instantiated
    }

    /**
     * Build a user agent string of the form:
     * <blockquote>{@code applicationId/versionName (androidVersion; model)}</blockquote>
     *
     * For example:
     * <blockquote>{@code com.example.app/1.0 (Android 4.4.4; XT1032)}</blockquote>
     *
     * @param context The application context
     * @return The user agent
     */
    public static String getUserAgent(Context context) {
        String applicationId = (String) getBuildConfigValue(context, "APPLICATION_ID");
        String versionName = (String) getBuildConfigValue(context, "VERSION_NAME");

        return getUserAgent(applicationId, versionName);
    }

    /**
     * Builds a user agent string of the form:
     * <blockquote>{@code applicationId/versionName (androidVersion; model)}</blockquote>
     *
     * For example:
     * <blockquote>{@code com.example.app/1.0 (Android 4.4.4; XT1032)}</blockquote>
     *
     * @param applicationId The application id, e.g: com.example.app
     * @param versionName   The application version name, e.g: 1.0
     * @return The user agent
     */
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
     * If the package name has a .debug suffix on it, it will be removed because the BuildConfig
     * file it is deployed in the original package.
     *
     * @param context   Used to find the correct file
     * @param fieldName The name of the field-to-access
     * @return The value of the field, or {@code null} if the field is not found.
     */
    private static Object getBuildConfigValue(Context context, String fieldName) {
        try {

            String packageName = context.getPackageName();

            //This remove the package name suffix, if was added by the Android Studio.
            //It is been assumed the suffix will be always .debug
            if (packageName.contains(".debug")) {
                packageName = packageName.replace(".debug", "");
            }

            Class<?> clazz = Class.forName(packageName + ".BuildConfig");
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
