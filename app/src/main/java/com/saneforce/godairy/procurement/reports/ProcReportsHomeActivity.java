package com.saneforce.godairy.procurement.reports;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.saneforce.godairy.databinding.ActivityProcReportsHomeBinding;

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

        binding.quality.setOnClickListener(v -> {
            Intent intent = new Intent(context, QualityReportActivity.class);
            startActivity(intent);
        });

        binding.maintenance.setOnClickListener(v -> {
            Intent intent = new Intent(context, MaintenanceReportActivity.class);
            startActivity(intent);
        });

        binding.existing.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExistingAgentVisitReportActivity.class);
            startActivity(intent);
        });

        binding.collectionCenter.setOnClickListener(v -> {
            Intent intent = new Intent(context, CollectionCenterReportActivity.class);
            startActivity(intent);
        });

        binding.asset.setOnClickListener(v -> {
            Intent intent = new Intent(context, AssetReportActivity.class);
            startActivity(intent);
        });

        binding.farmerCreation.setOnClickListener(v -> {
            Intent intent = new Intent(context, FarmerCreationReportActivity.class);
            startActivity(intent);
        });
    }
}