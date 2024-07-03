package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_CENTER;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFarmerCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSpinnerArray();
        onClick();

        list = new ArrayList<>();
    }

    private void onClick() {

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

        binding.msHatsun.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list.add("Hatsun");
            }else {
                binding.msHatsun.setChecked(false);
                list.remove("Hatsun");
            }
        });

        binding.msDolda.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list.add("Dolda");
            }else {
                binding.msDolda.setChecked(false);
                list.remove("Dolda");
            }
        });

        binding.msJersey.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list.add("Jersey");
            }else {
                binding.msJersey.setChecked(false);
                list.remove("Jersey");
            }
        });

        binding.msHeritage.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list.add("Heritage");
            }else {
                binding.msHeritage.setChecked(false);
                list.remove("Heritage");
            }
        });

        binding.msCooperative.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list.add("Cooperative");
            }else {
                binding.msCooperative.setChecked(false);
                list.remove("Cooperative");
            }
        });

        binding.msMmda.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list.add("MMDA");
            }else {
                binding.msMmda.setChecked(false);
                list.remove("MMDA");
            }
        });

        binding.msSka.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list.add("SKA");
            }else {
                binding.msSka.setChecked(false);
                list.remove("SKA");
            }
        });

        binding.msVijaya.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list.add("Vijaya");
            }else {
                binding.msVijaya.setChecked(false);
                list.remove("Vijaya");
            }
        });

        binding.interestedYes.setOnClickListener(v -> {
            binding.interestedNo.setChecked(false);
            mInterestedForSupply = "Yes";
        });

        binding.interestedNo.setOnClickListener(v -> {
            binding.interestedYes.setChecked(false);
            mInterestedForSupply = "No";
        });

        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
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

        binding.edFarmerName.addTextChangedListener(new TextWatcher() {
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

        binding.edAddress.addTextChangedListener(new TextWatcher() {
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

        binding.edPhoneNumber.addTextChangedListener(new TextWatcher() {
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

        binding.edPinCode.addTextChangedListener(new TextWatcher() {
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

        binding.edNoOfAnimalsCow.addTextChangedListener(new TextWatcher() {
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

        binding.edNoOfAnimalsBuffalo.addTextChangedListener(new TextWatcher() {
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

        binding.edMilkAvailabilityCow.addTextChangedListener(new TextWatcher() {
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

        binding.edMilkAvailabilityBuffalo.addTextChangedListener(new TextWatcher() {
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

    }

    private void saveNow() {

        Set<String> s = new LinkedHashSet<>(list);
        String arrayList = s.toString();
        Log.d("list__", arrayList);

        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("center", mCenter);
        serviceIntent.putExtra("farmer_gategory", mFarmerGategory);
        serviceIntent.putExtra("farmer_name", mFarmerName);
        serviceIntent.putExtra("farmer_addr", mAddress);
        serviceIntent.putExtra("phone_number", mPhoneNumber);
        serviceIntent.putExtra("pin_code", mPinCode);
        serviceIntent.putExtra("cow_total", mNoOfAnimalsCow);
        serviceIntent.putExtra("buffalo_total", mNoOfAnimalsBuffalo);
        serviceIntent.putExtra("cow_available_ltrs", mMilkAvailabilityCowLtrs);
        serviceIntent.putExtra("buffalo_available_ltrs", mMilkAvailabilityBuffaloLtrs);
        serviceIntent.putExtra("milk_supply_company", arrayList);
        serviceIntent.putExtra("interested_supply", mInterestedForSupply);
        serviceIntent.putExtra("active_flag", "1");
        serviceIntent.putExtra("upload_service_id", "2");
        ContextCompat.startForegroundService(this, serviceIntent);

        finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
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
            binding.txtErrorFound.setVisibility(View.VISIBLE);
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
//        if ("".equals(mMilkSupplyCompany)){
//            Toast.makeText(context, "Please select milk supply company", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if ("".equals(mInterestedForSupply)){
            Toast.makeText(context, "Please select Yes or No", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initSpinnerArray() {
        loadCenterList();

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.farmer_gategory_array, R.layout.custom_spinner);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFarmerGategory.setAdapter(adapter1);
    }

    private void loadCenterList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getProcCenterList(PROCUREMENT_GET_CENTER);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String plantList;
                    try {
                        plantList = response.body().string();

                        JSONArray jsonArray = new JSONArray(plantList);
                        List<String> list = new ArrayList<>();
                        list.add("Select");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String plantName = object.optString("sap_center_name");

                            binding.spinVillageCenter.setPrompt(plantName);
                            list.add(plantName);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinVillageCenter.setAdapter(adapter);
                    } catch (IOException | JSONException e) {
                       // throw new RuntimeException(e);
                        Toast.makeText(context, "Center list load error!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        File fileFarmerImage = new File(getExternalFilesDir(null), "/procurement/" + "FAMC_123.jpg");
        bitmapFarmerImage = BitmapFactory.decodeFile(fileFarmerImage.getAbsolutePath());
        if (bitmapFarmerImage != null){
            binding.imageViewFarmerImageLayout.setVisibility(View.VISIBLE);
            binding.imageFarmerImage.setImageBitmap(bitmapFarmerImage);
            binding.txtFarmerImageNotValid.setVisibility(View.GONE);
            binding.txtErrorFound.setVisibility(View.GONE);
        }
    }
}