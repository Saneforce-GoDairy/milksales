package com.saneforce.godairy.procurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityAitformBinding;
import java.io.File;

public class AITFormActivity extends AppCompatActivity {
    private ActivityAitformBinding binding;
    private String mCompanyName, mPlant, mCenterName, mFarmerCode, mBreed, mNoOfAi, mBullNos;
    private String mPdVerification = "", mCalfBirthVerification, mMineralMixtureSale, mSeedSale;
    private final Context context = this;
    private File fileBreed;
    private Bitmap bitmapBreed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAitformBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSpinnerArray();
        onClick();
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

        binding.spinnerPlant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerPlant.getSelectedItem().toString();
                binding.txtPlantNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBreed = binding.spinnerBreed.getSelectedItem().toString();
                binding.txtSpinnerBreedNameNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.spinnerCalfbirthVerification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBreed = binding.spinnerCalfbirthVerification.getSelectedItem().toString();
                binding.txtCalfbirthVeriNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

          /*
           Camera access id

           1, AgronomistFormActivity
              Farmers meeting = 1
              CSR Activity    = 2
              Fodder Development Ac = 3

           2, AITFormActivity
              breed = 4
         */

        binding.cameraBreed.setOnClickListener(view -> {
            binding.txtSpinnerBreedNameNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Name of breed");
            intent.putExtra("camera_id", "4");
            startActivity(intent);
        });

        binding.imageViewBreedLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "NOB_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Name of breed");
            startActivity(intent);
        });

        binding.pdVerificationPositive.setOnClickListener(v -> {
            binding.pdVerificationNegative.setChecked(false);
            mPdVerification = binding.pdVerificationPositive.getText().toString();
            binding.txtPdVerificationNotValid.setVisibility(View.GONE);
        });

        binding.pdVerificationNegative.setOnClickListener(v -> {
            binding.pdVerificationPositive.setChecked(false);
            mPdVerification = binding.pdVerificationNegative.getText().toString();
            binding.txtPdVerificationNotValid.setVisibility(View.GONE);
        });

        binding.edCenterName.addTextChangedListener(new TextWatcher() {
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
        binding.edFarmerCodeName.addTextChangedListener(new TextWatcher() {
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
        binding.edNoOfAi.addTextChangedListener(new TextWatcher() {
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
        binding.edBullNos.addTextChangedListener(new TextWatcher() {
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
        binding.edMineralMixtureSale.addTextChangedListener(new TextWatcher() {
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
        binding.edSeedSale.addTextChangedListener(new TextWatcher() {
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
        binding.spinnerCalfbirthVerification.setAdapter(adapter3);

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.breed_names_array, R.layout.custom_spinner);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerBreed.setAdapter(adapter4);
    }

    private void saveNow() {
        Toast.makeText(context, "valid", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs() {
        mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
        mPlant = binding.spinnerPlant.getSelectedItem().toString();
        mCenterName = binding.edCenterName.getText().toString().trim();
        mFarmerCode = binding.edFarmerCodeName.getText().toString();
        mBreed = binding.spinnerBreed.getSelectedItem().toString();
        mNoOfAi = binding.edNoOfAi.getText().toString().trim();
        mBullNos = binding.edBullNos.getText().toString().trim();
        mCalfBirthVerification = binding.spinnerCalfbirthVerification.getSelectedItem().toString();
        mMineralMixtureSale = binding.edMineralMixtureSale.getText().toString().trim();
        mSeedSale = binding.edSeedSale.getText().toString().trim();

        if ("Select".equals(mCompanyName)){
            ((TextView)binding.spinnerCompany.getSelectedView()).setError("Select company");
            binding.spinnerCompany.getSelectedView().requestFocus();
            binding.txtCompanyNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mPlant)){
            ((TextView)binding.spinnerPlant.getSelectedView()).setError("Select plant");
            binding.spinnerPlant.getSelectedView().requestFocus();
            binding.txtPlantNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCenterName)){
            binding.edCenterName.setError("Empty field");
            binding.edCenterName.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mFarmerCode)){
            binding.edFarmerCodeName.setError("Empty field");
            binding.edFarmerCodeName.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mBreed)){
            ((TextView)binding.spinnerBreed.getSelectedView()).setError("Select breed");
            binding.spinnerBreed.getSelectedView().requestFocus();
            binding.txtSpinnerBreedNameNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapBreed == null){
            binding.txtBreedImageNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfAi)){
            binding.edNoOfAi.setError("Empty field");
            binding.edNoOfAi.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mBullNos)){
            binding.edBullNos.setError("Empty field");
            binding.edBullNos.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mPdVerification))
        {
            binding.txtPdVerificationNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mCalfBirthVerification)){
            ((TextView)binding.spinnerCalfbirthVerification.getSelectedView()).setError("Select plant");
            binding.spinnerCalfbirthVerification.getSelectedView().requestFocus();
            binding.txtCalfbirthVeriNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mMineralMixtureSale)){
            binding.edMineralMixtureSale.setError("Empty field");
            binding.edMineralMixtureSale.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mSeedSale)){
            binding.edSeedSale.setError("Empty field");
            binding.edSeedSale.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Farmers meeting
        fileBreed = new File(getExternalFilesDir(null), "/procurement/" + "NOB_123.jpg");
        bitmapBreed = BitmapFactory.decodeFile(fileBreed.getAbsolutePath());

        if (bitmapBreed != null){
            binding.imageViewBreedLayout.setVisibility(View.VISIBLE);
            binding.imageBreed.setImageBitmap(bitmapBreed);
            binding.txtBreedImageNotValid.setVisibility(View.GONE);
        }
    }
}