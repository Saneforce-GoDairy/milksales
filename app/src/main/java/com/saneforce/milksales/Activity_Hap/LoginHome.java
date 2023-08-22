package com.saneforce.milksales.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.milksales.SFA_Activity.MapDirectionActivity;
import com.saneforce.milksales.databinding.ActivityLoginHomeBinding;

public class LoginHome extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    private ActivityLoginHomeBinding binding;
    private final Context context = this;
    SharedPreferences SHARED_PREF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SHARED_PREF = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

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
        Intent intent = new Intent(context, CheckInActivity2.class);
        startActivity(intent);
        finish();
    }
}