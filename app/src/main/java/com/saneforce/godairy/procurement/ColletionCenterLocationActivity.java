package com.saneforce.godairy.procurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityColletionCenterLocationBinding;

public class ColletionCenterLocationActivity extends AppCompatActivity {
    private ActivityColletionCenterLocationBinding binding;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityColletionCenterLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClick();
    }

    private void onClick() {
        binding.back.setOnClickListener(view -> finish());
    }
}