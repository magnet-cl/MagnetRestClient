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
import android.content.res.Resources;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

/**
 * Helper volley error class
 *
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
     * @param error The VolleyError received
     * @param context The context of the application
     * @return A string with the error feedback.
     */
    public static String getMessage(VolleyError error, Context context) {
        Resources res = context.getResources();

        if (isNetworkProblem(error)) {
            // Handles network error
            return res.getString(R.string.no_internet_error);
        } else if (isAuthError(error)) {
            // Handles authentication errors
            return res.getString(R.string.auth_failure_error);
        }

        // Any other error codes, return a message with the code.
        if (error.networkResponse != null) {
            return String.format(res.getString(R.string.unhandled_error),
                    error.networkResponse.statusCode);
        }

        // if there is no networkResponse, then the connection couldn't established, so there is an
        // internet error
        return res.getString(R.string.no_internet_error);
    }


    /**
     * Check if the error is an authentication error.
     *
     * @param error The VolleyError received.
     * @return True if the error is an authentication error.
     */
    public static boolean isAuthError(VolleyError error) {
        return error.networkResponse != null && error.networkResponse.statusCode == 401;
    }

    /**
     * Determines whether the error is related to a network problem.
     *
     * @param error The VolleyError returned
     * @return True if the error is a network problem
     */
    public static boolean isNetworkProblem(VolleyError error) {
        return (error instanceof NoConnectionError) || (error instanceof NetworkError);
    }
}
