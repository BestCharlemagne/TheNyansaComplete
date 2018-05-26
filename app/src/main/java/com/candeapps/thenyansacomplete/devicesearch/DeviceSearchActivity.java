package com.candeapps.thenyansacomplete.devicesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.candeapps.thenyansacomplete.Login;
import com.candeapps.thenyansacomplete.R;
import com.candeapps.thenyansacomplete.utils.CookieParcelable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by mchan on 12/03/17.
 */
public class DeviceSearchActivity extends Activity {
    private com.candeapps.thenyansacomplete.devicesearch.DeviceSearchFragment deviceSearchFragment;

    private static final String DEVICE_SEARCH_FRAGMENT = "DEVICE_SEARCH_FRAGMENT";


    public static String[] radioInfo = {"numDevices"};
    public static String[] connectedClientsInfo = {"uuid"};
    public static String[] apInfo = {"apName", "uuid", "apGroup", "ipAddress", "controllerModel", "controllerVersion", "numDevices"};
    public static String[] clientInfo = {"userName","bssid","browser","createdAt","model","osAndVersion","macAddress","ipAddress","uuid"};
    public static String[] radioStrings = {"2.4GHz","5GHz"};
    public static String apiToken;
    public static String apiUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_search);
        apiToken = readFile(Login.tokenFileName);
        apiUrl = readFile(Login.urlFileName);

        // Create the device search fragment.
        deviceSearchFragment = new com.candeapps.thenyansacomplete.devicesearch.DeviceSearchFragment();

        // Display the device search fragment.
        getFragmentManager().beginTransaction()
                .replace(R.id.device_search_container, deviceSearchFragment, DEVICE_SEARCH_FRAGMENT)
                .commit();

//        EditText editText = findViewById(R.id.editTextMacAddress);
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//
//                if(i == EditorInfo.IME_ACTION_DONE){
//                    searchDeviceClick(getCurrentFocus());
//
//                }
//                return false;
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings_change:
                Intent intent = new Intent(DeviceSearchActivity.this, Login.class);
                this.startActivity(intent);
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private String readFile(String fileName){
        String contents = "";

        try {
            InputStream inputStream = this.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                contents = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return contents;
    }


    // Search button click
    public void searchDeviceClick(View view) {
        deviceSearchFragment.searchDeviceClick(view);
    }

}