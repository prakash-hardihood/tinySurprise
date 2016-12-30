/*
package com.kratav.tinySurprise.notification;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.kratav.tinySurprise.R;


*/
/**
 * Created by rahul on 29/7/15.
 *//*

public class SplashActivity extends Activity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashActivity";


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Dialog alertDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        if (getIntent().hasExtra("NotificationMessage")) {
            ((TextView) findViewById(R.id.ashu)).setText(getIntent().getStringExtra("NotificationMessage"));
        }else {
            new RegisterAsync(this, "Ashu", "ashu@gmail.com", AppSharedPreference.getInstance(SplashActivity.this).getGCMToken()).execute();

        }

        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        //Let me send this token to my Sql Server so that it can broadcast messages as per the requirement

       */
/* new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String url = "tinysurprise.com/test_mode/mobi-app/gcm_register.php="+AppSharedPreference.getInstance(SplashActivity.this).getGCMToken();
                return null;
            }
        }.execute();
        *//*


    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}*/
