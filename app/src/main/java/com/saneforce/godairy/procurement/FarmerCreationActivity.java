package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_CENTER;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_PLANT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityFarmerCreationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmerCreationActivity extends AppCompatActivity {
    private ActivityFarmerCreationBinding binding;
    private final Context context = this;
    private String mCenter, mFarmerGategory, mFarmerName, mAddress, mPhoneNumber, mPinCode, mNoOfAnimalsCow, mNoOfAnimalsBuffalo, mMilkAvailabilityCowLtrs;
    private String mMilkAvailabilityBuffaloLtrs, mMilkSupplyCompany = "", mInterestedForSupply = "";
    private Bitmap bitmapFarmerImage;
    private File fileFarmerImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFarmerCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSpinnerArray();
        onClick();
    }

    private void onClick() {

         /*
           Camera access id

           1, AgronomistFormActivity
              Farmers meeting = 1
              CSR Activity    = 2
              Fodder Development Ac = 3

           2, AITFormActivity
              breed = 4

           3, CollectionCenterLocationActivity
              Collection center image = 5

           4, VeterinaryDoctorsFormActivity
              Type of image image = 6
              Emergency treatment/EVM Treatment (Breed) = 7

            5, QualityFormActivity
               Quality fat = 8
               Quality snf = 9
               No of vehicle received with hoods = 10
               No of vehicle received without hoods = 11
               Awareness program = 12

            6, FarmerCreationActivity
               Farmer image = 13
         */

        binding.cameraFarmerImage.setOnClickListener(view -> {
            binding.txtFarmerImageNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Farmer image");
            intent.putExtra("camera_id", "13");
            startActivity(intent);
        });

        binding.imageViewFarmerImageLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "FAMC_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Farmer image");
            startActivity(intent);
        });

        binding.msHatsun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Hatsun", Toast.LENGTH_SHORT).show();
                mMilkSupplyCompany = "Hatsun";

                binding.msDolda.setChecked(false);
                binding.msJersey.setChecked(false);
                binding.msHeritage.setChecked(false);
                binding.msCooperative.setChecked(false);
                binding.msMmda.setChecked(false);
                binding.msSka.setChecked(false);
                binding.msVijaya.setChecked(false);
            }
        });

        binding.msDolda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Dolda", Toast.LENGTH_SHORT).show();
                mMilkSupplyCompany = "Dolda";

                binding.msHatsun.setChecked(false);
                binding.msJersey.setChecked(false);
                binding.msHeritage.setChecked(false);
                binding.msCooperative.setChecked(false);
                binding.msMmda.setChecked(false);
                binding.msSka.setChecked(false);
                binding.msVijaya.setChecked(false);
            }
        });

        binding.msJersey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Jersey", Toast.LENGTH_SHORT).show();
                mMilkSupplyCompany = "Jersey";

                binding.msHatsun.setChecked(false);
                binding.msDolda.setChecked(false);
                binding.msHeritage.setChecked(false);
                binding.msCooperative.setChecked(false);
                binding.msMmda.setChecked(false);
                binding.msSka.setChecked(false);
                binding.msVijaya.setChecked(false);
            }
        });

        binding.msHeritage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Heritage", Toast.LENGTH_SHORT).show();
                mMilkSupplyCompany = "Heritage";

                binding.msHatsun.setChecked(false);
                binding.msDolda.setChecked(false);
                binding.msJersey.setChecked(false);
                binding.msCooperative.setChecked(false);
                binding.msMmda.setChecked(false);
                binding.msSka.setChecked(false);
                binding.msVijaya.setChecked(false);
            }
        });

        binding.msCooperative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cooperative", Toast.LENGTH_SHORT).show();
                mMilkSupplyCompany = "Cooperative";

                binding.msHatsun.setChecked(false);
                binding.msDolda.setChecked(false);
                binding.msJersey.setChecked(false);
                binding.msHeritage.setChecked(false);
                binding.msMmda.setChecked(false);
                binding.msSka.setChecked(false);
                binding.msVijaya.setChecked(false);
            }
        });

        binding.msMmda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "MMDA", Toast.LENGTH_SHORT).show();
                mMilkSupplyCompany = "MMDA";

                binding.msHatsun.setChecked(false);
                binding.msDolda.setChecked(false);
                binding.msJersey.setChecked(false);
                binding.msHeritage.setChecked(false);
                binding.msCooperative.setChecked(false);
                binding.msSka.setChecked(false);
                binding.msVijaya.setChecked(false);
            }
        });

        binding.msSka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "SKA", Toast.LENGTH_SHORT).show();
                mMilkSupplyCompany = "SKA";

                binding.msHatsun.setChecked(false);
                binding.msDolda.setChecked(false);
                binding.msJersey.setChecked(false);
                binding.msHeritage.setChecked(false);
                binding.msCooperative.setChecked(false);
                binding.msMmda.setChecked(false);
                binding.msVijaya.setChecked(false);
            }
        });

        binding.msVijaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Vijaya", Toast.LENGTH_SHORT).show();
                mMilkSupplyCompany = "Vijaya";

                binding.msHatsun.setChecked(false);
                binding.msDolda.setChecked(false);
                binding.msJersey.setChecked(false);
                binding.msHeritage.setChecked(false);
                binding.msCooperative.setChecked(false);
                binding.msMmda.setChecked(false);
                binding.msSka.setChecked(false);
            }
        });

        binding.interestedYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show();

                binding.interestedNo.setChecked(false);

                mInterestedForSupply = "Yes";
            }
        });

        binding.interestedNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "No", Toast.LENGTH_SHORT).show();

                binding.interestedYes.setChecked(false);

                mInterestedForSupply = "No";
            }
        });

        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                Toast.makeText(context, "valid", Toast.LENGTH_SHORT).show();
                saveNow();
            }
        });

        binding.spinVillageCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCenter = binding.spinVillageCenter.getSelectedItem().toString();
                binding.txtVillageCenterNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerFarmerGategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCenter = binding.spinnerFarmerGategory.getSelectedItem().toString();
                binding.txtFarmerGategoryNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.back.setOnClickListener(view -> finish());

    }

    private void saveNow() {
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("center", mCenter);
        serviceIntent.putExtra("farmer_gategory", mFarmerGategory);
        serviceIntent.putExtra("farmer_name", mFarmerName);
        serviceIntent.putExtra("farmer_address", mAddress);
        serviceIntent.putExtra("phone_number", mPhoneNumber);
        serviceIntent.putExtra("pin_code", mPinCode);
        serviceIntent.putExtra("cow_total", mNoOfAnimalsCow);
        serviceIntent.putExtra("buffalo_total", mNoOfAnimalsBuffalo);
        serviceIntent.putExtra("cow_available_ltrs", mMilkAvailabilityCowLtrs);
        serviceIntent.putExtra("buffalo_available_ltrs", mMilkAvailabilityBuffaloLtrs);
        serviceIntent.putExtra("milk_supply_company", mMilkSupplyCompany);
        serviceIntent.putExtra("interested_supply", mInterestedForSupply);
        serviceIntent.putExtra("active_flag", "1");
        serviceIntent.putExtra("upload_service_id", "2");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private boolean validateInputs() {
        mCenter = binding.spinVillageCenter.getSelectedItem().toString();
        mFarmerGategory = binding.spinnerFarmerGategory.getSelectedItem().toString();

        mFarmerName = binding.edFarmerName.getText().toString().trim();
        mAddress = binding.edAddress.getText().toString().trim();

        mPhoneNumber = binding.edPhoneNumber.getText().toString().trim();
        mPinCode = binding.edPinCode.getText().toString().trim();

        mNoOfAnimalsCow = binding.edNoOfAnimalsCow.getText().toString().trim();
        mNoOfAnimalsBuffalo = binding.edNoOfAnimalsBuffalo.getText().toString().trim();

        mMilkAvailabilityCowLtrs = binding.edMilkAvailabilityCow.getText().toString().trim();
        mMilkAvailabilityBuffaloLtrs = binding.edMilkAvailabilityBuffalo.getText().toString().trim();

        if ("Select".equals(mCenter)){
            ((TextView)binding.spinVillageCenter.getSelectedView()).setError("Select center/village");
            binding.spinVillageCenter.getSelectedView().requestFocus();
            binding.txtVillageCenterNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mFarmerGategory)){
            ((TextView)binding.spinnerFarmerGategory.getSelectedView()).setError("Select farmer gategory");
            binding.spinnerFarmerGategory.getSelectedView().requestFocus();
            binding.txtFarmerGategoryNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mFarmerName)){
            binding.edFarmerName.setError("Enter farmer name");
            binding.edFarmerName.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mAddress)){
            binding.edAddress.setError("Enter address");
            binding.edAddress.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mPhoneNumber)){
            binding.edPhoneNumber.setError("Enter phone number");
            binding.edPhoneNumber.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mPinCode)){
            binding.edPinCode.setError("Enter pin code");
            binding.edPinCode.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapFarmerImage == null){
            binding.txtFarmerImageNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfAnimalsCow)){
            binding.edNoOfAnimalsCow.setError("Enter no of cow");
            binding.edNoOfAnimalsCow.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfAnimalsBuffalo)){
            binding.edNoOfAnimalsBuffalo.setError("Enter no of buffalo");
            binding.edNoOfAnimalsBuffalo.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mMilkAvailabilityCowLtrs)){
            binding.edMilkAvailabilityCow.setError("Enter milk availability in cow");
            binding.edMilkAvailabilityCow.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mMilkAvailabilityBuffaloLtrs)){
            binding.edMilkAvailabilityBuffalo.setError("Enter milk availability in buffalo");
            binding.edMilkAvailabilityBuffalo.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mMilkSupplyCompany)){
            Toast.makeText(context, "Please select milk supply company", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(mInterestedForSupply)){
            Toast.makeText(context, "Please select Yes or No", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initSpinnerArray() {
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.company_array, R.layout.custom_spinner);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        binding.spinVillageCenter.setAdapter(adapter);

        loadCenterList();

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.farmer_gategory_array, R.layout.custom_spinner);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFarmerGategory.setAdapter(adapter1);
    }

    private void loadCenterList() {
        ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getProcCenterList(PROCUREMENT_GET_CENTER);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String plantList;
                    try {
                        plantList = response.body().string();

                        JSONArray jsonArray = new JSONArray(plantList);
                        List<String> list = new ArrayList<>();
                        list.add("Select");

                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String plantName = object.optString("sap_center_name");

                            binding.spinVillageCenter.setPrompt(plantName);
                            list.add(plantName);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinVillageCenter.setAdapter(adapter);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        fileFarmerImage = new File(getExternalFilesDir(null), "/procurement/" + "FAMC_123.jpg");
        bitmapFarmerImage = BitmapFactory.decodeFile(fileFarmerImage.getAbsolutePath());

        if (bitmapFarmerImage != null){
            binding.imageViewFarmerImageLayout.setVisibility(View.VISIBLE);
            binding.imageFarmerImage.setImageBitmap(bitmapFarmerImage);
            binding.txtFarmerImageNotValid.setVisibility(View.GONE);
        }
    }
}