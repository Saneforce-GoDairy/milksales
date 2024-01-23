package com.saneforce.godairy.procurement.reports;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityProcReportsHomeBinding;
import com.saneforce.godairy.databinding.ActivityProcurementHomeBinding;

public class ProcReportsHomeActivity extends AppCompatActivity {
    private ActivityProcReportsHomeBinding binding;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProcReportsHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClick();
    }

    private void onClick() {
       binding.agronomist.setOnClickListener(v -> {
           Intent intent = new Intent(context, AgronomistReportActivity.class);
           startActivity(intent);
       });

       binding.ait.setOnClickListener(v -> {
           Intent intent = new Intent(context, AITReportActivity.class);
           startActivity(intent);
       });

        binding.veterinary.setOnClickListener(v -> {
            Intent intent = new Intent(context, VeterinaryReportActivity.class);
            startActivity(intent);
        });
    }
}