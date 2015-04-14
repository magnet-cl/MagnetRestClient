package cl.magnet.magnetrestclient;

import android.os.Build;
import android.test.AndroidTestCase;

import cl.magnet.magnetrestclient.utils.UserAgentUtils;

/**
 * Created by lukas on 07-04-15.
 */
public class UserAgentUtilsTest extends AndroidTestCase {

    public void testGetUserAgent() {
        String userAgent = cl.magnet.magnetrestclient.test.BuildConfig.APPLICATION_ID
                + "/"
                + cl.magnet.magnetrestclient.test.BuildConfig.VERSION_NAME
                + " (Android "
                + Build.VERSION.RELEASE
                + "; "
                + Build.MODEL
                + ")";
        assertEquals(userAgent, UserAgentUtils.getUserAgent(mContext));
    }
}
