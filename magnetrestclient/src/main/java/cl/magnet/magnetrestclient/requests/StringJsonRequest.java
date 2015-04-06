package cl.magnet.magnetrestclient.requests;


import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

import cl.magnet.magnetrestclient.MagnetErrorListener;


/**
 * Created by lukas on 18-08-14.
 */
public class StringJsonRequest extends BaseJsonRequest<String> {

    public StringJsonRequest(int method, String url, String requestBody,
                             Response.Listener<String> listener,
                             MagnetErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

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
