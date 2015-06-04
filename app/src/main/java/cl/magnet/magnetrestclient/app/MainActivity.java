package cl.magnet.magnetrestclient.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;

import cl.magnet.magnetrestclient.VolleyErrorHelper;
import cl.magnet.magnetrestclient.VolleyManager;
import cl.magnet.magnetrestclient.app.R;
import cl.magnet.magnetrestclient.utils.UserAgentUtils;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mUserAgentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserAgentTv = (TextView) findViewById(R.id.activity_main_tv_user_agent);

        String userAgent = UserAgentUtils.getUserAgent(getApplicationContext());
        mUserAgentTv.setText(userAgent);

        StringRequest stringRequest = new StringRequest("www.google.com", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // should show no connection error, because the manifest has no internet permission
                Toast.makeText(getApplicationContext(), VolleyErrorHelper.getMessage(error,
                        getApplicationContext()), Toast.LENGTH_LONG).show();
            }
        });
        VolleyManager.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
