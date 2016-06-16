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

package cl.magnet.magnetrestclient.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;

import org.apache.http.protocol.HTTP;

import java.util.HashMap;
import java.util.Map;

import cl.magnet.magnetrestclient.BuildConfig;
import cl.magnet.magnetrestclient.utils.UserAgentUtils;


/**
 * A request for retrieving a T type response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 *
 * This class can handle {@link cl.magnet.magnetrestclient.MagnetErrorListener} different errors,
 * but it also works with common  Volley {com.android.volley.Response.ErrorListener}.
 *
 * {@inheritDoc}
 * Created by lukas on 16-03-15.
 */
public abstract class BaseJsonRequest<T> extends JsonRequest<T> {

    private static final String TAG = BaseJsonRequest.class.getSimpleName();

    private static final String USER_AGENT_DEFAULT = UserAgentUtils.getUserAgent(BuildConfig
            .APPLICATION_ID, BuildConfig.VERSION_NAME);

    private Map<String, String> mHeaders;


    /**
     * {@inheritDoc}
     *
     * @param method        The http request method. See {com.android.volley.Request.Method} for
     *                      supported methods.
     * @param url           The request url
     * @param requestBody   The request body
     * @param listener      Callback for delivering parse responses
     * @param errorListener Callback for devilering errors. It can be a
     *                      {@link cl.magnet.magnetrestclient.MagnetErrorListener} or
     *                      {com.android.volley.Response.ErrorListener}
     */
    public BaseJsonRequest(int method, String url, String requestBody,
                           Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);

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