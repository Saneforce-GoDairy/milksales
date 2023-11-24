package com.saneforce.godairy.procurement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityQualityFormBinding;

public class QualityFormActivity extends AppCompatActivity {
    private ActivityQualityFormBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQualityFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClick();
    }

    private void onClick() {
        binding.back.setOnClickListener(view -> finish());
    }
}