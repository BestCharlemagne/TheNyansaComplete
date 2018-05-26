package com.candeapps.thenyansacomplete;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.candeapps.thenyansacomplete.devicesearch.DeviceSearchActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Login extends Activity {
    public String apiToken;
    public String apiUrl;

    private EditText tokenText;
    private EditText urlText;
    private Button loginButton;

    private FileOutputStream tokenFile;
    public static String tokenFileName = "apiToken";
    private FileOutputStream urlFile;
    public static String urlFileName = "apiUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        tokenText = findViewById(R.id.token);
        urlText = findViewById(R.id.url);
        loginButton = findViewById(R.id.login_button);
        textWatchers(tokenText,urlText,loginButton);
        setIme();
    }

    private void setIme(){
        tokenText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(loginButton.isEnabled())
                        startLogin();
                    return true;
                }
                return false;
            }
        });
    }

    private void textWatchers(final EditText tokenEditText, final EditText urlEditText, final Button search) {
        tokenEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                search.setEnabled(true);

            }
        });

        urlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                search.setEnabled(true);
            }
        });

        if(tokenEditText.length() == 0 || urlEditText.length() == 0) {
            search.setEnabled(false);
        }
    }


    public void loginActivity(View view){
        startLogin();
    }

    private void startLogin(){
        apiToken = tokenText.getText().toString();
        apiUrl = urlText.getText().toString();

        String voyanceHostUrl = "";
        if(!apiUrl.contains("https://")){
            voyanceHostUrl = "https://";
        }
        voyanceHostUrl+=apiUrl;
        if(!apiUrl.contains(".nyansa.com")){
            voyanceHostUrl+=".nyansa.com";
        }
        if(!apiUrl.contains("/graphql")){
            voyanceHostUrl+="/graphql";
        }
        apiUrl = voyanceHostUrl;

        try{
            File check = new File(tokenFileName);
            File check2 = new File(urlFileName);


            if(check.exists()) {
                PrintWriter file = new PrintWriter(tokenFileName);
                file.close();
            }
            tokenFile = openFileOutput(tokenFileName, Context.MODE_PRIVATE);
            tokenFile.write(apiToken.getBytes());
            tokenFile.close();

            if(check2.exists()) {
                PrintWriter file2 = new PrintWriter(urlFileName);
                file2.close();
            }
            urlFile = openFileOutput(urlFileName, Context.MODE_PRIVATE);
            urlFile.write(apiUrl.getBytes());
            urlFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Login.this, DeviceSearchActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
