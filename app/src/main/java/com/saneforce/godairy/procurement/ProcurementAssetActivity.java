package com.saneforce.godairy.procurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityProcurementAssetBinding;

public class ProcurementAssetActivity extends AppCompatActivity {
    private ActivityProcurementAssetBinding binding;
    private final Context context = this;
    private String mCompany, mPlant, mTypeOfAsset = "", mComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProcurementAssetBinding.inflate(getLayoutInflater());
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
        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                Toast.makeText(context, "valid", Toast.LENGTH_SHORT).show();
                saveNow();
            }
        });

        binding.spinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCompany = binding.spinnerCompany.getSelectedItem().toString();
          //      binding.txtCompanyNotValid.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerPlant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerPlant.getSelectedItem().toString();
        //        binding.txtPlantNotValid.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.typeOfAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show();

                mTypeOfAsset = "Yes";
            }
        });

        binding.back.setOnClickListener(view -> finish());
    }

    private void saveNow() {

    }

    private boolean validateInputs() {
        mCompany = binding.spinnerCompany.getSelectedItem().toString();
        mPlant = binding.spinnerPlant.getSelectedItem().toString();
        mComments = binding.edComments.getText().toString().trim();

        if ("Select".equals(mCompany)){
            ((TextView)binding.spinnerCompany.getSelectedView()).setError("Select company");
            binding.spinnerCompany.getSelectedView().requestFocus();
       //     binding.txtCompanyNotValid.setVisibility(View.VISIBLE);
            binding.buttonSave.setVisibility(View.VISIBLE);
            Toast.makeText(context, "Please select company", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("Select".equals(mPlant)){
            ((TextView)binding.spinnerPlant.getSelectedView()).setError("Select plant");
            Toast.makeText(context, "Please select plant", Toast.LENGTH_SHORT).show();
       //     binding.txtPlantNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mTypeOfAsset)){
            Toast.makeText(context, "Please select asset", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(mComments)){
            binding.edComments.setError("Enter comments");
            binding.edComments.requestFocus();
            return false;
        }
        return true;
    }
}