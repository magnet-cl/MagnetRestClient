package cl.magnet.magnetrestclient.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cl.magnet.magnetrestclient.BuildConfig;
import cl.magnet.magnetrestclient.utils.UserAgentUtils;

/**
 * Created by ignacio on 02-08-15 for the LayoutGenerator Library.
 */
public class MagnetJsonObjectRequest extends JsonObjectRequest {

    private static final String TAG = BaseJsonRequest.class.getSimpleName();

    private static final String USER_AGENT_DEFAULT = UserAgentUtils.getUserAgent(BuildConfig
            .APPLICATION_ID, BuildConfig.VERSION_NAME);

    private Map<String, String> mHeaders;


    /**
     * {@inheritDoc}
     *
     * @param method        The http request method. See {@link com.android.volley.Request.Method} for
     *                      supported methods.
     * @param url           The request url
     * @param requestBody   The request body
     * @param listener      Callback for delivering parse responses
     * @param errorListener Callback for devilering errors. It can be a
     *                      {@link cl.magnet.magnetrestclient.MagnetErrorListener} or
     *                      {@link com.android.volley.Response.ErrorListener}
     */
    public MagnetJsonObjectRequest(int method, String url, JSONObject requestBody,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {

        super(method, url, requestBody, listener, errorListener);

        mHeaders = new HashMap<>();

        // we add the default user-agent header
        addHeader(HTTP.USER_AGENT, USER_AGENT_DEFAULT);
    }

    /**
     * {@inheritDoc}
     *
     * @param method        The http request method. See {@link com.android.volley.Request.Method} for
     *                      supported methods.
     * @param url           The request url
     * @param listener      Callback for delivering parse responses
     * @param errorListener Callback for devilering errors. It can be a
     *                      {@link cl.magnet.magnetrestclient.MagnetErrorListener} or
     *                      {@link com.android.volley.Response.ErrorListener}
     */
    public MagnetJsonObjectRequest(int method, String url, Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {

        super(method, url, listener, errorListener);

        mHeaders = new HashMap<>();

        // we add the default user-agent header
        addHeader(HTTP.USER_AGENT, USER_AGENT_DEFAULT);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    /**
     * Sets the user agent http header.
     *
     * @param userAgent The user agent
     */
    public void setUserAgent(String userAgent) {
        addHeader(HTTP.USER_AGENT, userAgent);
    }

    /**
     * Adds a header to the request.
     *
     * @param key   The header key, e.g: Content-Type
     * @param value The header value, e.g: application/json
     */
    protected void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    /**
     * Adds a map of headers to the request.
     *
     * @param headers The headers map.
     */
    protected void addHeaders(Map<String, String> headers) {
        if (headers != null) {
            mHeaders.putAll(headers);
        }
    }
}
