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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import cl.magnet.magnetrestclient.requests.BaseJsonRequest;
import cl.magnet.magnetrestclient.utils.LruBitmapCache;
import cl.magnet.magnetrestclient.utils.PersistentCookieStore;
import cl.magnet.magnetrestclient.utils.UserAgentUtils;

/**
 * Singleton class that encapsulates {@link com.android.volley.RequestQueue} and other volley
 * functionality, and uses <a href="http://square.github.io/okhttp/">OkHttp</a> as its transport
 * layer.
 * <p/>
 * To every {@link cl.magnet.magnetrestclient.requests.BaseJsonRequest}
 * sent, it set's the user agent to:
 * <blockquote>{@code applicationId/versionName (androidVersion; model)}</blockquote>
 * <p/>
 * For example:
 * <blockquote>{@code com.example.app/1.0 (Android 4.4.4; XT1032)}</blockquote>
 * <p/>
 * Created by lukas on 02-11-14.
 */
public final class VolleyManager {

    public static final String TAG = VolleyManager.class.getSimpleName();

    private static VolleyManager sInstance; // singleton instance of Volley Manager

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mContext;
    private String mUserAgent; // user agent known at runtime

    /**
     * Private constructor to prevent that VolleyManager is instanciated outside this class. Uses
     * the {@link cl.magnet.magnetrestclient.utils.PersistentCookieStore} for reading and writing cookies.
     *
     * @param context The context of the controller
     */
    private VolleyManager(Context context) {
        mContext = context;
        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(mContext),
                CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(cookieManager);
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(LruBitmapCache
                .getCacheSize(context)));

        // set USER AGENT
        mUserAgent = UserAgentUtils.getUserAgent(mContext);
    }

    /**
     * Returns the singleton instance of VolleyManager. If there is no instance,
     * then it creates a new one, else it returns the existing one.
     * <p/>
     * A key concept is that context <b>must</b> be the Application context,
     * <b>not</b> an Activity context. This  ensures that the RequestQueue will last for the
     * lifetime of your app, instead of being recreated every time the activity is recreated (for
     * example, when the user rotates the device).
     *
     * @param context The context where the method is called. This context <b>MUST</b> be
     *                Application context.
     * @return The instance of VolleyManager
     */
    public static synchronized VolleyManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new VolleyManager(context);
        }

        return sInstance;
    }

    /**
     * Returns the singleton instance of RequestQueue that last the lifetime of the app. If there
     * is no instance of RequestQueue, then a new one is created.
     * <p/>
     * The created RequestQueue uses {@link cl.magnet.magnetrestclient.OkHttpStack OkHttpStack}
     * for networking.
     *
     * @return RequestQueue instance
     */
    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() keeps from leaking Activity or BroadcastReceiver if
            // someone pass one in
            OkHttpClient client = new OkHttpClient();
            client.setCookieHandler(CookieHandler.getDefault());
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext(),
                    new OkHttpStack(client));
        }

        return mRequestQueue;
    }

    /**
     * @return The image loader
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


    /**
     * Adds a request to the RequestQueue with the default tag.
     *
     * @param request The request that will be added to the queue.
     * @param <T>     The type of the request
     */
    public <T> void addToRequestQueue(Request<T> request) {
        addToRequestQueue(request, TAG);
    }

    /**
     * Adds a request to the RequestQueue with a specific tag.
     *
     * @param request The request that will be added to the queue.
     * @param tag     The tag to be added to the request
     * @param <T>     The type of the request
     */
    public <T> void addToRequestQueue(Request<T> request, Object tag) {
        if (request instanceof BaseJsonRequest) {
            ((BaseJsonRequest) request).setUserAgent(mUserAgent);
        }

        request.setTag(tag == null ? TAG : tag);
        getRequestQueue().add(request);
    }

    /**
     * Cancels all pending requests by the specified tag. It is important to specify a tag so
     * that pending/ongoing requests can be cancelled.
     *
     * @param tag The tag of the requests that are going to be cancelled
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
