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
 * Created by lukas on 16-03-15.
 */
public abstract class BaseJsonRequest<T> extends JsonRequest<T> {

    private static final String TAG = BaseJsonRequest.class.getSimpleName();

    private static final String USER_AGENT_DEFAULT = UserAgentUtils.getUserAgent(BuildConfig
            .APPLICATION_ID, BuildConfig.VERSION_NAME);

    private Map<String, String> mHeaders;
    private MagnetErrorListener mMagnetErrorListener;

    /**
     * BaseJsonRequest constructor that uses the usual Volley {@link com.android.volley.Response
     * .ErrorListener} for error handling.
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

    public void setUserAgent(String userAgent) {
        addHeader(HTTP.USER_AGENT, userAgent);
    }

    protected void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    protected void addHeaders(Map<String, String> headers) {
        if (headers != null) {
            mHeaders.putAll(headers);
        }
    }
}