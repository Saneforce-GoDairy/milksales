package com.saneforce.godairy.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.databinding.ActivityMainBinding;
import com.saneforce.godairy.procurement.FarmerCreationActivity;
import com.saneforce.godairy.procurement.MilkCollEntryActivity;
import com.saneforce.godairy.procurement.ProcurementHome;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final Context context = this;
    public static final String mypreference = "mypref";
    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);

            shared_common_pref = new Shared_Common_Pref(this);
            new Handler().postDelayed(() -> {
                SharedPreferences sharedpreferences;
                sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                if (sharedpreferences.getString("nameKey", "").equals("")) {
                    startActivity(new Intent(context, PrivacyPolicy.class));
                } else {
                    startActivity(new Intent(context, LoginHome.class));
                }
                finish();
            }, 3000);
    }
}