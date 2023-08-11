package com.saneforce.milksales.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final Context context = this;
    public static final String mypreference = "mypref";
    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);

            shared_common_pref = new Shared_Common_Pref(this);

            new Handler().postDelayed(() -> {

                SharedPreferences sharedpreferences;
                sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                if (sharedpreferences.getString("nameKey", "").equals("")) {
                    Intent intent = new Intent(context, PrivacyPolicy.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, LoginHome.class);
                    startActivity(intent);
                }
                finish();
            }, 3000);
        } catch (Exception e) {
            Log.v("MainActivity:", Objects.requireNonNull(e.getMessage()));
        }
    }
}