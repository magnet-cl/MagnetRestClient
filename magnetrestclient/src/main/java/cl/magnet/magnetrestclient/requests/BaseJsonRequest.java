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


/**
 * Created by lukas on 16-03-15.
 */
public abstract class BaseJsonRequest<T> extends JsonRequest<T> {

    private static final String TAG = BaseJsonRequest.class.getSimpleName();

    private static final String USER_AGENT = BuildConfig.APPLICATION_ID
            + "/"
            + BuildConfig.VERSION_NAME
            + " (Android "
            + Build.VERSION.RELEASE
            + "; "
            + Build.MODEL
            + ")";

    private Map<String, String> mHeaders;
    private MagnetErrorListener mMagnetErrorListener;

    public BaseJsonRequest(int method, String url, String requestBody,
                           Response.Listener<T> listener, MagnetErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);

        mHeaders = new HashMap<String, String>();
        mMagnetErrorListener = errorListener;

        // we add the user-agent header
        addHeader(HTTP.USER_AGENT, USER_AGENT);
    }

    /**
     * BaseJsonRequest constructor that uses the usual Volley {@link com.android.volley.Response
     * .ErrorListener} for error handling.
     */
    public BaseJsonRequest(int method, String url, String requestBody,
                           Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        mMagnetErrorListener = null; // because we are using standard volley error listener
        mHeaders = new HashMap<String, String>();

        // we add the user-agent header
        addHeader(HTTP.USER_AGENT, USER_AGENT);
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

    protected void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    protected void addHeaders(Map<String, String> headers) {
        if (headers != null) {
            mHeaders.putAll(headers);
        }
    }
}