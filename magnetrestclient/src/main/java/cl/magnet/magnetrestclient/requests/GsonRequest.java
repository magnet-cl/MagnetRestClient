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

import cl.magnet.magnetrestclient.MagnetErrorListener;


/**
 * Volley adapter for JSON requests that will be parsed into Java objects by Gson.
 *
 * Created by lukas on 26-07-14.
 */
public class GsonRequest<T> extends BaseJsonRequest<T> {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private final Gson mGson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
    private final Class<T> mClassType;
    private final Response.Listener<T> mListener;

    public GsonRequest(int method, String url, Class<T> classType, JSONObject jsonRequest,
                       Response.Listener<T> listener, MagnetErrorListener errorListener) {
        this(method, url, classType, null, jsonRequest, listener, errorListener);
    }

    public GsonRequest(int method, String url, Class<T> classType, Map<String, String> headers,
                       JSONObject jsonRequest, Response.Listener<T> listener,
                       MagnetErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
        mClassType = classType;
        mListener = listener;

        // add the headers
        addHeaders(headers);
    }

    public GsonRequest(int method, String url, Class<T> classType, JSONObject jsonRequest,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, url, classType, null, jsonRequest, listener, errorListener);
    }

    public GsonRequest(int method, String url, Class<T> classType, Map<String, String> headers,
                       JSONObject jsonRequest, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
        mClassType = classType;
        mListener = listener;

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
}