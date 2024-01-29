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
           startActivity(new Intent(context, AgronomistReportActivity.class));
       });

       binding.ait.setOnClickListener(v -> {
           startActivity(new Intent(context, AITReportActivity.class));
       });

        binding.veterinary.setOnClickListener(v -> {
            startActivity(new Intent(context, VeterinaryReportActivity.class));
        });

        binding.quality.setOnClickListener(v -> {
            startActivity(new Intent(context, QualityReportActivity.class));
        });

        binding.maintenance.setOnClickListener(v -> {
            startActivity(new Intent(context, MaintenanceReportActivity.class));
        });

        binding.existing.setOnClickListener(v -> {
            startActivity(new Intent(context, ExistingAgentVisitReportActivity.class));
        });

        binding.collectionCenter.setOnClickListener(v -> {
            startActivity(new Intent(context, CollectionCenterReportActivity.class));
        });

        binding.asset.setOnClickListener(v -> {
            startActivity(new Intent(context, AssetReportActivity.class));
        });

        binding.farmerCreation.setOnClickListener(v -> {
            startActivity(new Intent(context, FarmerCreationReportActivity.class));
        });
    }
}