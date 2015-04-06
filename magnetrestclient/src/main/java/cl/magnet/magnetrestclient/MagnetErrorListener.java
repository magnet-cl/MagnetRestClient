package cl.magnet.magnetrestclient;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Abstract class that divides error responses in three:
 * - Unauthorized errors: 401
 * - upgrade required error: 426
 * - other errors
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

    public abstract void onUnhandledError(VolleyError volleyError);

    public abstract <T> void onUnauthorizedError(VolleyError volleyError, final Request<T> request);

    public abstract void onUpgradeRequiredError(VolleyError volleyError);
}
