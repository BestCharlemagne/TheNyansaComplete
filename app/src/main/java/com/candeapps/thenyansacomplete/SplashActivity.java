package com.candeapps.thenyansacomplete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import com.candeapps.thenyansacomplete.devicesearch.DeviceSearchActivity;

import java.io.File;

public class SplashActivity extends Activity {
    private static String TAG = SplashActivity.class.getName();
    private static long SLEEP_TIME = 3;  // Sleep time in seconds.
    private String tokenFileName;
    private String urlFileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        tokenFileName = Login.tokenFileName;
        urlFileName = Login.urlFileName;
        // Start timer and launch main activity
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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

    private class IntentLauncher extends Thread {
        @Override
        public void run() {
            try {
                // Sleeping
                Thread.sleep(SLEEP_TIME * 1000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            File tokenFile = new File(getApplicationContext().getFilesDir(),tokenFileName);
            File urlFile = new File(getApplicationContext().getFilesDir(),urlFileName);
            if(tokenFile.exists() && urlFile.exists()){
                // Skip login activity
                Intent intent = new Intent(SplashActivity.this, DeviceSearchActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
            else{
                // Start login activity
                Intent intent = new Intent(SplashActivity.this, Login.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }
    }
}
