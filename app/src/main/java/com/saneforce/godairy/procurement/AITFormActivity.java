package com.saneforce.godairy.procurement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityAitformBinding;

public class AITFormActivity extends AppCompatActivity {
    private ActivityAitformBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAitformBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initSpinnerArray();
        onClick();
    }

    private void onClick() {
        binding.back.setOnClickListener(view -> {
            finish();
        });
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


        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.calf_birth_veri_array, R.layout.custom_spinner);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.calfBirthVeriSpinner.setAdapter(adapter3);

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.breed_names_array, R.layout.custom_spinner);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.breedNameSpinner.setAdapter(adapter4);

    }
}