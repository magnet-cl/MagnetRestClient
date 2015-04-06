package cl.magnet.magnetrestclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * Created by lukas on 17-11-14.
 */
public class PersistentCookieStore implements CookieStore {

    /**
     * The default preferences string.
     */
    private final static String PREF_DEFAULT_STRING = "";

    /**
     * The preferences name.
     */
    private final static String PREFS_NAME = PersistentCookieStore.class.getName();

    /**
     * The preferences session cookie key.
     */
    private final static String PREF_SESSION_COOKIE = "session_cookie";

    private CookieStore mStore;
    private Context mContext;
    private Gson mGson;

    public PersistentCookieStore(Context context) {
        // prevent context leaking by getting the application context
        mContext = context.getApplicationContext();
        mGson = new Gson();

        // get the default in memory store and if there is a cookie stored in shared preferences,
        // we added it to the cookie store
        mStore = new CookieManager().getCookieStore();
        String jsonSessionCookie = getJsonSessionCookieString();
        if (!jsonSessionCookie.equals(PREF_DEFAULT_STRING)) {
            HttpCookie cookie = mGson.fromJson(jsonSessionCookie, HttpCookie.class);
            mStore.add(URI.create(cookie.getDomain()), cookie);
        }
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        if (cookie.getName().equals("sessionid")) {
            // if the cookie that the cookie store attempt to add is a session cookie,
            // we remove the older cookie and save the new one in shared preferences
            remove(URI.create(cookie.getDomain()), cookie);
            saveSessionCookie(cookie);
        }

        mStore.add(URI.create(cookie.getDomain()), cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return mStore.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return mStore.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return mStore.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return mStore.remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return mStore.removeAll();
    }

    private String getJsonSessionCookieString() {
        return getPrefs().getString(PREF_SESSION_COOKIE, PREF_DEFAULT_STRING);
    }

    /**
     * Saves the HttpCookie to SharedPreferences as a json string.
     *
     * @param cookie The cookie to save in SharedPreferences.
     */
    private void saveSessionCookie(HttpCookie cookie) {
        String jsonSessionCookieString = mGson.toJson(cookie);
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(PREF_SESSION_COOKIE, jsonSessionCookieString);
        editor.apply();
    }

    private SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}
