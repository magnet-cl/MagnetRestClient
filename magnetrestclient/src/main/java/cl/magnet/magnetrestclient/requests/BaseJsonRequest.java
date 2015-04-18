package cl.magnet.magnetrestclient.requests;

import android.os.Build;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

import java.util.HashMap;
import java.util.Map;

import cl.magnet.magnetrestclient.BuildConfig;
import cl.magnet.magnetrestclient.MagnetErrorListener;
import cl.magnet.magnetrestclient.VolleyManager;
import cl.magnet.magnetrestclient.utils.UserAgentUtils;


/**
 * A request for retrieving a T type response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 *
 * <p>
 * This class can handle {@link cl.magnet.magnetrestclient.MagnetErrorListener} different errors,
 * but it also works with common  Volley {@link com.android.volley.Response.ErrorListener}.
 *
 * {@inheritDoc}
 *
 * <p>
 * Created by lukas on 16-03-15.
 */
public abstract class BaseJsonRequest<T> extends JsonRequest<T> {

    private static final String TAG = BaseJsonRequest.class.getSimpleName();

    private static final String USER_AGENT_DEFAULT = UserAgentUtils.getUserAgent(BuildConfig
            .APPLICATION_ID, BuildConfig.VERSION_NAME);

    private Map<String, String> mHeaders;
    private MagnetErrorListener mMagnetErrorListener;


    /**
     * {@inheritDoc}
     *
     * @param method the http request method. See {@link com.android.volley.Request.Method} for
     *               supported methods.
     * @param url the request url
     * @param requestBody the request body
     * @param listener callback for delivering parse responses
     * @param errorListener callback for devilering errors. It can be a
     *  {@link cl.magnet.magnetrestclient.MagnetErrorListener} or
     *  {@link com.android.volley.Response.ErrorListener}
     */
    public BaseJsonRequest(int method, String url, String requestBody,
                           Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);

        // Check if the error listener is an instance of MagnetErrorListener
        if (errorListener instanceof MagnetErrorListener) {
            mMagnetErrorListener = (MagnetErrorListener) errorListener;
        } else {
            mMagnetErrorListener = null;
        }

        mHeaders = new HashMap<>();

        // we add the default user-agent header
        addHeader(HTTP.USER_AGENT, USER_AGENT_DEFAULT);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    @Override
    public void deliverError(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.statusCode == HttpStatus
                .SC_UNAUTHORIZED && mMagnetErrorListener != null) {
            Log.d(TAG, "Unauthorized request!");
            mMagnetErrorListener.onUnauthorizedError(error, this);
        } else {
            super.deliverError(error);
        }
    }

    /**
     * Sets the user agent http header.
     *
     * @param userAgent the user agent
     */
    public void setUserAgent(String userAgent) {
        addHeader(HTTP.USER_AGENT, userAgent);
    }

    /**
     * Adds a header to the request.
     *
     * @param key the header key, e.g: Content-Type
     * @param value the header value, e.g: application/json
     */
    protected void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    /**
     * Adds a map of headers to the request.
     *
     * @param headers the headers map.
     */
    protected void addHeaders(Map<String, String> headers) {
        if (headers != null) {
            mHeaders.putAll(headers);
        }
    }
}