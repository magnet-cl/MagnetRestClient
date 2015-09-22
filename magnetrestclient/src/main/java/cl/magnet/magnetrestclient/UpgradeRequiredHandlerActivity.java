package cl.magnet.magnetrestclient;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ignacio on 02-08-15 for the LayoutGenerator Library.
 */
public abstract class UpgradeRequiredHandlerActivity extends AppCompatActivity
        implements UpgradeRequiredHandler {

    public void handleUpgradeRequired(){
        Intent i = new Intent(getApplicationContext(), UpgradeRequiredActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
