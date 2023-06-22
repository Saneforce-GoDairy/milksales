package com.saneforce.milksales.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.milksales.R;

public class PrivacyPolicy extends AppCompatActivity {
    WebView privacyWebView;
    CheckBox privacyCheck;
    Button privacySubmit, privacyDisable;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";

    String PrivacyScreen = "";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_privacy_policy);
            sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
            /*webView*/
            privacyWebView = (WebView) findViewById(R.id.privacy_webview);
            privacyWebView.loadUrl("https://hap.sanfmcg.com/Privacy.html");

            if (sharedpreferences.contains(Name)) {
                PrivacyScreen = sharedpreferences.getString(Name, "");
                Log.e("Privacypolicy", "Checking" + PrivacyScreen);
                if (PrivacyScreen.equals("One")) {
                    startActivity(new Intent(PrivacyPolicy.this, Login.class));
                }
            }

            privacyCheck = findViewById(R.id.privacy_check_box);
            privacySubmit = findViewById(R.id.submit_privacy);
            privacySubmit.setText("Submit");
            privacySubmit.setTextColor(Color.parseColor("#ffffff"));
            privacyDisable = findViewById(R.id.submit_disable_privacy);
            privacyDisable.setText("Submit");
            privacyDisable.setTextColor(Color.parseColor("#ffffff"));

            privacyCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();

                    if (checked) {
                        privacySubmit.setVisibility(View.VISIBLE);
                        privacyDisable.setVisibility(View.GONE);
                    } else {

                        privacySubmit.setVisibility(View.GONE);
                        privacyDisable.setVisibility(View.VISIBLE);
                    }
                }
            });

            privacySubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Save();
                }
            });
        } catch (Exception e) {

        }
    }

    public void Save() {
        String n = "One";
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name, n);
        editor.commit();
        startActivity(new Intent(PrivacyPolicy.this, Login.class));
    }

}