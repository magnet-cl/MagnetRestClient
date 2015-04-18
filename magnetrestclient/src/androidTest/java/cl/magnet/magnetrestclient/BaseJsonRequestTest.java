package cl.magnet.magnetrestclient;

import android.os.Build;
import android.test.AndroidTestCase;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;

import cl.magnet.magnetrestclient.requests.BaseJsonRequest;

/**
 * Created by lukas on 06-04-15.
 */
public class BaseJsonRequestTest extends AndroidTestCase {

    private static final String TAG = BaseJsonRequestTest.class.getSimpleName();

    private MockWebServer mMockWebServer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMockWebServer = new MockWebServer();
    }

    public void testUserAgent() throws Exception {
        mMockWebServer.enqueue(new MockResponse().setBody("Testing USER AGENT"));
        mMockWebServer.start();

        BaseJsonRequest<String> request = new BaseJsonRequestImpl(Request.Method.GET,
                mMockWebServer.getUrl("/").toString(), null, null, null);
        VolleyManager.getInstance(mContext).addToRequestQueue(request);

        RecordedRequest request1 = mMockWebServer.takeRequest();
        String userAgent = cl.magnet.magnetrestclient.test.BuildConfig.APPLICATION_ID
                + "/"
                + cl.magnet.magnetrestclient.test.BuildConfig.VERSION_NAME
                + " (Android "
                + Build.VERSION.RELEASE
                + "; "
                + Build.MODEL
                + ")";
        assertEquals(userAgent, request1.getHeader(HTTP.USER_AGENT));

        mMockWebServer.shutdown();
    }

    private static class BaseJsonRequestImpl extends BaseJsonRequest<String> {

        public BaseJsonRequestImpl(int method, String url, String requestBody,
                                   Response.Listener<String> listener,
                                   Response.ErrorListener errorListener) {
            super(method, url, requestBody, listener, errorListener);
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
            String parsed;
            try {
                parsed = new String(networkResponse.data, HttpHeaderParser.parseCharset
                        (networkResponse.headers));
            } catch (UnsupportedEncodingException e) {
                parsed = new String(networkResponse.data);
            }
            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(networkResponse));
        }
    }

}
