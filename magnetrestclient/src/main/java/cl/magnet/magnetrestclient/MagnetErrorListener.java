package cl.magnet.magnetrestclient;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Abstract class that divides error responses in three:
 * <ul>
 *     <li>Unauthorized error: response status code 401</li>
 *     <li>Upgrade required error: response status code 426</li>
 *     <li>Other errors</li>
 * </ul>
 *
 * Created by lukas on 18-03-15.
 */
public abstract class MagnetErrorListener implements Response.ErrorListener {

    private static final String TAG = MagnetErrorListener.class.getSimpleName();

    private static final int HTTP_UPGRADE_REQUIRED = 426;

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error.networkResponse != null) {
            if (error.networkResponse.statusCode == HTTP_UPGRADE_REQUIRED) {
                onUpgradeRequiredError(error);
            } else {
                onUnhandledError(error);
            }
        } else {
            onUnhandledError(error);
        }
    }

    /**
     * Method called when an unhandled error has been ocurred. This happens when the server
     * responds with a 4xx or 5xx status code, with the exception of 401 and 426 codes,
     * that are handled by {@link #onUnauthorizedError(com.android.volley.VolleyError, com.android.volley.Request)}
     * and {@link #onUpgradeRequiredError(com.android.volley.VolleyError)} respectively.
     *
     * @param volleyError The error with the provided error code.
     */
    public abstract void onUnhandledError(VolleyError volleyError);

    /**
     * Method called when an Unauthorized error has been ocurred. This happens when the
     * server responds with an 401 status code.
     *
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.2">401 Unauthorized</a>
     *
     * @param volleyError The error with the provided error code.
     * @param request The request that triggered the error.
     * @param <T> Parsed type of the request.
     */
    public abstract <T> void onUnauthorizedError(VolleyError volleyError, final Request<T> request);

    /**
     * Method called when an Upgrade Required error has been ocurred. This happens when the
     * server responds with a 426 status code.
     *
     * @param volleyError The error with the provided error code.
     */
    public abstract void onUpgradeRequiredError(VolleyError volleyError);
}
