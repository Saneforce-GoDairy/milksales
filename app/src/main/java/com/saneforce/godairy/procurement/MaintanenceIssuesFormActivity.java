package com.saneforce.godairy.procurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityMaintanenceIssuesFormBinding;

public class MaintanenceIssuesFormActivity extends AppCompatActivity {
    private ActivityMaintanenceIssuesFormBinding binding;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaintanenceIssuesFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSpinnerArray();
        onClick();
    }

    private void initSpinnerArray() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.company_array, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompany.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.plant_array, R.layout.custom_spinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPlant.setAdapter(adapter2);
    }

    private void onClick() {
        binding.back.setOnClickListener(view -> finish());
    }
}