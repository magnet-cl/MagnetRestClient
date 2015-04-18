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

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Helper volley error class
 * <p/>
 * Created by lukas on 02-11-14.
 */
public final class VolleyErrorHelper {

    // Prevent the instantiation of this class
    private VolleyErrorHelper() {
    }

    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @param context
     * @return
     */
    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.generic_server_down_error);
        } else if (isAuthFailureError(error)) {
            return context.getResources().getString(R.string.auth_failure_error);
        } else if (isServerProblem(error)) {
            return handleServerError(error, context);
        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.no_internet_error);
        } else {
            return context.getResources().getString(R.string.generic_networking_error);
        }
    }

    /**
     * Determines whether the error is related to network
     *
     * @param error
     * @return True if the error is a network problem
     */
    public static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    public static boolean isAuthFailureError(Object error) {
        return (error instanceof AuthFailureError);
    }

    /**
     * Determines whether the error is related to server, most likely 4xx or 5xx HTTP status code.
     *
     * @param error
     * @return True if the server responded with an error
     */
    public static boolean isServerProblem(Object error) {
        return (error instanceof ServerError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock message or to
     * show a message retrieved from the server.
     *
     * @param err
     * @param context
     * @return
     */
    public static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) switch (response.statusCode) {
            case 404:
                return context.getResources().getString(R.string.generic_networking_error);
            case 422:
                return context.getResources().getString(R.string.generic_networking_error);
            case 401:
                return error.getMessage();
            default:
                return context.getResources().getString(R.string.generic_server_down_error);
        }
        return context.getResources().getString(R.string.generic_networking_error);
    }

}
