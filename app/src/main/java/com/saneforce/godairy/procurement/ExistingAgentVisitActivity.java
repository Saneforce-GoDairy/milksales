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
import com.saneforce.godairy.databinding.ActivityExistingAgentVisitBinding;

public class ExistingAgentVisitActivity extends AppCompatActivity {
    private ActivityExistingAgentVisitBinding binding;
    private final Context context = this;
    private String mAgent = "", mListSelectedCompany, mTotalMilkAvailability, mOurCompanyLtrs, mCompetitorRate, mOurCompanyRate;
    private String mDemand, mSupplyStartDate;

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
        binding.spinnerListCompany.setAdapter(adapter);
    }

    private void onClick() {

        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                Toast.makeText(context, "valid", Toast.LENGTH_SHORT).show();
                saveNow();
            }
        });

        binding.spinnerListCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mListSelectedCompany = binding.spinnerListCompany.getSelectedItem().toString();
             //   binding.txtListCompanyNotValid.setVisibility(View.GONE);
            //    binding.txtErrorFound.setVisibility(View.GONE);
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

        binding.back.setOnClickListener(view -> finish());
    }

    private void saveNow() {

    }

    private boolean validateInputs() {
        mListSelectedCompany = binding.spinnerListCompany.getSelectedItem().toString();
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
        if ("Select".equals(mListSelectedCompany)){
            ((TextView)binding.spinnerListCompany.getSelectedView()).setError("Select company");
            binding.spinnerListCompany.getSelectedView().requestFocus();
     //       binding.txtListCompanyNotValid.setVisibility(View.VISIBLE);
     //       binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mTotalMilkAvailability)){
            binding.edTotalMilkAvai.setError("Enter Total Milk Availability");
            binding.edTotalMilkAvai.requestFocus();
      //      binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mOurCompanyLtrs)){
            binding.edOurCompanyLtrs.setError("Enter Company Ltrs");
            binding.edOurCompanyLtrs.requestFocus();
       //     binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCompetitorRate)){
            binding.edCompetitorRate.setError("Enter Competitor Rate");
            binding.edCompetitorRate.requestFocus();
      //      binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mOurCompanyRate)){
            binding.edOurCompnyRate.setError("Enter Our Compny Rate");
            binding.edOurCompnyRate.requestFocus();
      //      binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mDemand)){
            binding.edDemand.setError("Enter Demand");
            binding.edDemand.requestFocus();
      //      binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mSupplyStartDate)){
            binding.edSupplyStartDate.setError("Enter Supply Start Date");
            binding.edSupplyStartDate.requestFocus();
    //        binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
}