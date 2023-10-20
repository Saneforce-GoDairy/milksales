package com.milksales.godairy.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.ApiClient;
import com.milksales.godairy.databinding.ActivityLoginHomeBinding;

public class LoginHome extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    private Shared_Common_Pref shared_common_pref;
    private ActivityLoginHomeBinding binding;
    private final Context context = this;
    private SharedPreferences SHARED_PREF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SHARED_PREF = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        shared_common_pref = new Shared_Common_Pref(this);

        if (!shared_common_pref.getvalue("base_url").isEmpty()) {
            ApiClient.ChangeBaseURL(shared_common_pref.getvalue("base_url"));
        }
        onClick();
        sessionCheck();
    }

    private void onClick() {
        binding.signIn.setOnClickListener(v -> {
            Intent intent = new Intent(context, Login.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        });
    }

    private void sessionCheck() {
        boolean Login = SHARED_PREF.getBoolean("Login", false);
        if (Login){
            loadHome();
        }
    }

    private void loadHome() {
        Intent intent = new Intent(context, Login.class);
        startActivity(intent);
        finish();
    }
}