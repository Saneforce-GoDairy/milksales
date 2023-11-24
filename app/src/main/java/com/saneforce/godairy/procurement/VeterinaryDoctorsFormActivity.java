package com.saneforce.godairy.procurement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityVeterinaryDoctorsFormBinding;

public class VeterinaryDoctorsFormActivity extends AppCompatActivity {
    private ActivityVeterinaryDoctorsFormBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVeterinaryDoctorsFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClick();
    }

    private void onClick() {
        binding.back.setOnClickListener(view -> finish());
    }
}