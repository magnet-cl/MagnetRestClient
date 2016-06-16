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

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * A request for retreving a Java object from the response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 * The request uses {com.google.gson.Gson} to parse the json response into a Java object.
 * The default {com.google.gson.Gson} date format is {@code yyyy-MM-dd'T'HH:mm:ss}. For
 * customizing {com.google.gson.Gson} settings, {@link #getGson()} method can be overriden
 * in order to build Gson with various configuration settings.
 * Created by lukas on 26-07-14.
 * {@inheritDoc}
 */
public class GsonRequest<T> extends BaseJsonRequest<T> {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private final Gson mGson;
    private final Class<T> mClassType;
    private final Response.Listener<T> mListener;

    /**
     * {@inheritDoc}
     *
     * @param method        the http request method. See {com.android.volley.Request.Method} for
     *                      supported methods.
     * @param url           The request url
     * @param classType     Relevant class object, for Gson's reflection.
     * @param jsonRequest   The json that will be sent in the request body
     * @param listener      Callback for delivering parse responses
     * @param errorListener Callback for devilering errors. It can be a
     *                      {@link cl.magnet.magnetrestclient.MagnetErrorListener} or
     *                      {com.android.volley.Response.ErrorListener}
     */
    public GsonRequest(int method, String url, Class<T> classType, JSONObject jsonRequest,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, url, classType, null, jsonRequest, listener, errorListener);
    }

    /**
     * {@inheritDoc}
     *
     * @param method        the http request method. See {com.android.volley.Request.Method} for
     *                      supported methods.
     * @param url           The request url
     * @param classType     Relevant class object, for Gson's reflection.
     * @param headers       Map of request headers
     * @param jsonRequest   The json that will be sent in the request body
     * @param listener      Callback for delivering parse responses
     * @param errorListener Callback for devilering errors. It can be a
     *                      {@link cl.magnet.magnetrestclient.MagnetErrorListener} or
     *                      {com.android.volley.Response.ErrorListener}
     */
    public GsonRequest(int method, String url, Class<T> classType, Map<String, String> headers,
                       JSONObject jsonRequest, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
        mClassType = classType;
        mListener = listener;
        mGson = getGson();

        // add the headers
        addHeaders(headers);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset
                    (networkResponse.headers));
            return Response.success(mGson.fromJson(json, mClassType),
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    /**
     * This method is used to obtain the {com.google.gson.Gson} object that will parse the
     * response. It can be overriden in order to customize {com.google.gson.Gson}
     * with various configuration settings.
     *
     * @return The gson object that will parse the response
     */
    protected Gson getGson() {
        return new GsonBuilder().setDateFormat(DATE_FORMAT).create();
    }
}