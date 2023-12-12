package com.saneforce.godairy.procurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityFarmerCreationBinding;

public class FarmerCreationActivity extends AppCompatActivity {
    private ActivityFarmerCreationBinding binding;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFarmerCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}