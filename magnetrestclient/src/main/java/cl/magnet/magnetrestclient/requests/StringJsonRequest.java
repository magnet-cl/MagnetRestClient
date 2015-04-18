package cl.magnet.magnetrestclient.requests;


import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;


/**
 * A request for retrieving a String type response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 * <p/>
 * Created by lukas on 18-08-14.
 */
public class StringJsonRequest extends BaseJsonRequest<String> {

    /**
     * {@inheritDoc}
     *
     * @param method        The http request method. See {@link com.android.volley.Request.Method} for
     *                      supported methods.
     * @param url           The request url
     * @param requestBody   The request body
     * @param listener      Callback for delivering parse responses
     * @param errorListener Callback for devilering errors. It can be a
     *                      {@link cl.magnet.magnetrestclient.MagnetErrorListener} or
     *                      {@link com.android.volley.Response.ErrorListener}
     */
    public StringJsonRequest(int method, String url, String requestBody,
                             Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        String parsed;
        try {
            parsed = new String(networkResponse.data, HttpHeaderParser.parseCharset
                    (networkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(networkResponse.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

}
