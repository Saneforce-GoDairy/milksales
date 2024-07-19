package com.saneforce.godairy.procurement;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.saneforce.godairy.databinding.ActivityMilkCollEntryBinding;

public class MilkCollEntryActivity extends AppCompatActivity {
    private ActivityMilkCollEntryBinding binding;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMilkCollEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClick();
    }

    private void onClick() {

    }
}