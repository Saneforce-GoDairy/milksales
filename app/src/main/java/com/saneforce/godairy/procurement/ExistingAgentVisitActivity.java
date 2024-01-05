package com.saneforce.godairy.procurement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityExistingAgentVisitBinding;

public class ExistingAgentVisitActivity extends AppCompatActivity {
    private ActivityExistingAgentVisitBinding binding;
    private final Context context = this;
    private String mAgent = "", mListSelectedCompany, mTotalMilkAvailability, mOurCompanyLtrs, mCompetitorRate, mOurCompanyRate;
    private String mDemand, mSupplyStartDate, mCompanyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExistingAgentVisitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSpinnerArray();
        onClick();
    }

    private void initSpinnerArray() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.company_array, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompany.setAdapter(adapter);
    }

    private void onClick() {

        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                saveNow();
            }
        });

        binding.spinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
                binding.txtCompanyNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.agExclusiveAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Exclusive Agent", Toast.LENGTH_SHORT).show();
                mAgent = "Exclusive Agent";

                binding.agDualAgent.setChecked(false);
            }
        });

        binding.agDualAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Dual Agent", Toast.LENGTH_SHORT).show();
                mAgent = "Dual Agent";

                binding.agExclusiveAgent.setChecked(false);
            }
        });

        binding.edTotalMilkAvai.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.edCompetitorRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        binding.edOurCompnyRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.edDemand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.edSupplyStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.edOurCompanyLtrs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        binding.back.setOnClickListener(view -> finish());
    }

    private void saveNow() {
        String mActiveFlag = "1";
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("visit_agent", mAgent);
        serviceIntent.putExtra("company", mCompanyName);
        serviceIntent.putExtra("total_milk_available", mTotalMilkAvailability);
        serviceIntent.putExtra("our_company_ltrs", mOurCompanyLtrs);
        serviceIntent.putExtra("competitor_rate", mCompetitorRate);
        serviceIntent.putExtra("our_company_rate", mOurCompanyRate);
        serviceIntent.putExtra("demand", mDemand);
        serviceIntent.putExtra("supply_start_dt", mSupplyStartDate);

        serviceIntent.putExtra("active_flag", mActiveFlag);
        serviceIntent.putExtra("upload_service_id", "6");
        ContextCompat.startForegroundService(this, serviceIntent);

        finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();

    }

    private boolean validateInputs() {
        mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
        mTotalMilkAvailability = binding.edTotalMilkAvai.getText().toString().trim();
        mOurCompanyLtrs = binding.edOurCompanyLtrs.getText().toString().trim();
        mCompetitorRate = binding.edCompetitorRate.getText().toString().trim();
        mOurCompanyRate = binding.edOurCompnyRate.getText().toString().trim();
        mDemand = binding.edDemand.getText().toString().trim();
        mSupplyStartDate = binding.edSupplyStartDate.getText().toString().trim();

        if ("".equals(mAgent)){
            Toast.makeText(context, "Please select agent", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("Select".equals(mCompanyName)){
            ((TextView)binding.spinnerCompany.getSelectedView()).setError("Select company");
            binding.spinnerCompany.getSelectedView().requestFocus();
            binding.txtCompanyNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mTotalMilkAvailability)){
            binding.edTotalMilkAvai.setError("Enter Total Milk Availability");
            binding.edTotalMilkAvai.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mOurCompanyLtrs)){
            binding.edOurCompanyLtrs.setError("Enter Company Ltrs");
            binding.edOurCompanyLtrs.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCompetitorRate)){
            binding.edCompetitorRate.setError("Enter Competitor Rate");
            binding.edCompetitorRate.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mOurCompanyRate)){
            binding.edOurCompnyRate.setError("Enter Our Compny Rate");
            binding.edOurCompnyRate.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mDemand)){
            binding.edDemand.setError("Enter Demand");
            binding.edDemand.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mSupplyStartDate)){
            binding.edSupplyStartDate.setError("Enter Supply Start Date");
            binding.edSupplyStartDate.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
}