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

package cl.magnet.magnetrestclient;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Abstract class that divides error responses in three:
 * <ul>
 * <li>Unauthorized error: response status code 401</li>
 * <li>Upgrade required error: response status code 426</li>
 * <li>Other errors</li>
 * </ul>
 * <p/>
 * MagnetErrorListener only works when using with requests that inherit from
 * {@link cl.magnet.magnetrestclient.requests.BaseJsonRequest}
 * <p/>
 * Created by lukas on 18-03-15.
 */
public abstract class MagnetErrorListener implements Response.ErrorListener {

    private static final String TAG = MagnetErrorListener.class.getSimpleName();

    private static final int HTTP_UPGRADE_REQUIRED = 426;

    private UpgradeRequiredHandler handler;

    public MagnetErrorListener(UpgradeRequiredHandler handler){
        this.handler = handler;
    }

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
     * responds with a 4xx or 5xx status code, with the exception of 401 and
     * {@value #HTTP_UPGRADE_REQUIRED} codes,
     * that are handled by
     * {@link #onUnauthorizedError(com.android.volley.VolleyError, com.android.volley.Request)}
     * and {@link #onUpgradeRequiredError(com.android.volley.VolleyError)} respectively.
     *
     * @param volleyError The error with the provided error code.
     */
    public abstract void onUnhandledError(VolleyError volleyError);

    /**
     * Method called when an Unauthorized error has been ocurred. This happens when the
     * server responds with an 401 status code.
     *
     * @param volleyError The error with the provided error code.
     * @param request     The request that triggered the error.
     * @param <T>         Parsed type of the request.
     * @see <a href="http://tools.ietf.org/html/rfc2616#section-10.4.2">401 Unauthorized</a>
     */
    public abstract <T> void onUnauthorizedError(VolleyError volleyError, final Request<T> request);

    /**
     * Method called when an Upgrade Required error has been ocurred. This happens when the
     * server responds with a {@value #HTTP_UPGRADE_REQUIRED} status code.
     *
     * @param volleyError The error with the provided error code.
     */
    public void onUpgradeRequiredError(VolleyError volleyError){
        handler.handleUpgradeRequired();
    }
}
