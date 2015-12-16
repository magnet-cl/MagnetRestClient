package cl.magnet.magnetrestclient;

import android.test.AndroidTestCase;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.util.HashMap;

import cl.magnet.magnetrestclient.requests.StringJsonRequest;

/**
 * Created by lukas on 07-04-15.
 */
public class MagnetErrorListenerTest extends AndroidTestCase {

    private MagnetErrorListenerImpl mMagnetErrorListener;
    private StringJsonRequest mRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMagnetErrorListener = new MagnetErrorListenerImpl();
        mRequest = new StringJsonRequest(Request.Method.GET,
                "http://blabla.com/", null, null, mMagnetErrorListener);
    }

    public void testUnhandledError() throws Exception {
        String body = "ERROR!";
        NetworkResponse networkResponse = new NetworkResponse(404, body.getBytes(),
                new HashMap<String, String>(), true);
        VolleyError error = new VolleyError(networkResponse);
        mRequest.deliverError(error);
        assertEquals(MagnetErrorListenerImpl.UNHANDLED_ERROR, mMagnetErrorListener.getError());
    }

    public void testUnauthorizedError() throws Exception {
        String body = "ERROR!";
        NetworkResponse networkResponse = new NetworkResponse(401, body.getBytes(),
                new HashMap<String, String>(), true);
        VolleyError error = new VolleyError(networkResponse);
        mRequest.deliverError(error);
        assertEquals(MagnetErrorListenerImpl.ON_UNAUTHORIZED_ERROR, mMagnetErrorListener.getError());
    }

    public void testUpgradeRequiredError() throws Exception {
        String body = "ERROR!";
        NetworkResponse networkResponse = new NetworkResponse(426, body.getBytes(),
                new HashMap<String, String>(), true);
        VolleyError error = new VolleyError(networkResponse);
        mRequest.deliverError(error);
        assertEquals(MagnetErrorListenerImpl.ON_UPGRADE_REQUIRED_ERROR,
                mMagnetErrorListener.getError());
    }

    private class MagnetErrorListenerImpl extends MagnetErrorListener {

        static final int NO_ERROR = 0;
        static final int UNHANDLED_ERROR = 1;
        static final int ON_UNAUTHORIZED_ERROR = 2;
        static final int ON_UPGRADE_REQUIRED_ERROR = 3;

        private int mError;

        public MagnetErrorListenerImpl() {
            super(null);
            mError = NO_ERROR;
        }

        public int getError() {
            return mError;
        }

        @Override
        public void onUnhandledError(VolleyError volleyError) {
            mError = UNHANDLED_ERROR;
        }

        @Override
        public <T> void onUnauthorizedError(VolleyError volleyError) {
            mError = ON_UNAUTHORIZED_ERROR;
        }

        @Override
        public void onUpgradeRequiredError(VolleyError volleyError) {
            mError = ON_UPGRADE_REQUIRED_ERROR;
        }
    }
}
