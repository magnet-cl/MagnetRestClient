package cl.magnet.magnetrestclient.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;

import com.android.volley.toolbox.ImageLoader;

/**
 * Implementation of an in-memory lru cache.
 * Created by lukas on 25-07-14.
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    // 4 bytes per pixel
    private static final int BYTES_PER_PIXEL = 4;

    // three screens
    private static final int SCREENS_NUMB = 3;

    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    public LruBitmapCache(Context context) {
        this(getCacheSize(context));
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

    /**
     * Returns a cache size equal to approximately SCREENS_NUMB screens worth of images,
     * using BYTES_PER_PIXEL bytes per pixel.
     * @param context the context where the cache needs to be
     * @return the cache size
     */
    public static int getCacheSize(Context context) {
        final DisplayMetrics displayMetrics = context.getResources().
                getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;
        final int screenBytes = screenWidth * screenHeight * BYTES_PER_PIXEL;

        return screenBytes * SCREENS_NUMB;
    }
}
