package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_PLANT;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityAgronomistFormBinding;
import com.saneforce.godairy.procurement.database.DatabaseManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgronomistFormActivity extends AppCompatActivity {
    private ActivityAgronomistFormBinding binding;
    private String mCompanyName, mPlant, mCenterName, mFarmerCodeName, mTypeOfProduct, mTeatTipCup, mTypeOfService, mFodderDev;
    private String mNoOfFarmersEnrolled, mNoOfFarmersInducted;
    private final Context context = this;
    private File fileFormersMeeting, fileCSRActivity, fileFodderDevAcres;
    private Bitmap bitmapFormersMeeting, bitmapCSRActivity , bitmapFodderDevAcres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgronomistFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.open();

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
        binding.spinnerTypeOfProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTypeOfProduct = binding.spinnerTypeOfProduct.getSelectedItem().toString();
                binding.txtTypeOfProNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        binding.spinnerTypeOfService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTypeOfProduct = binding.spinnerTypeOfService.getSelectedItem().toString();
                binding.txtTypeOfServiceNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

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

            7, MaintenanceIssueActivity
               Type of repair image = 14
         */


        binding.cameraFarmersMeeting.setOnClickListener(view -> {
            binding.txtFarmersImageNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Farmers meeting");
            intent.putExtra("camera_id", "1");
            startActivity(intent);
        });

        binding.cameraCsrActivity.setOnClickListener(view -> {
            binding.txtCsrImageNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "CSR Activity");
            intent.putExtra("camera_id", "2");
            startActivity(intent);
        });
        binding.cameraFoder.setOnClickListener(view -> {
            binding.txtFodderImageNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Fodder Development Acres");
            intent.putExtra("camera_id", "3");
            startActivity(intent);
        });
        binding.imageViewFormersMeetingLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "FAR_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Formers Meeting");
            startActivity(intent);
        });
        binding.imageViewCsrActivityLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "CSR_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "CSR Activity");
            startActivity(intent);
        });
        binding.imageViewFoderLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "FDA_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Fodder Development Acres");
            startActivity(intent);
        });
        binding.back.setOnClickListener(view -> {
            finish();
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
        binding.edTeatTipCup.addTextChangedListener(new TextWatcher() {
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
        binding.edFodderDevelopmentAcres.addTextChangedListener(new TextWatcher() {
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
        binding.edNoOfFarmersEnrolled.addTextChangedListener(new TextWatcher() {
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
        binding.edNoOfFarmersInducted.addTextChangedListener(new TextWatcher() {
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

    private void initSpinnerArray() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.company_array, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompany.setAdapter(adapter);

        loadPlant();

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.type_of_product_array, R.layout.custom_spinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfProduct.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.typs_of_service_array, R.layout.custom_spinner);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfService.setAdapter(adapter3);
    }

    private void loadPlant() {
        ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getProcPlant(PROCUREMENT_GET_PLANT);

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

                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String plantName = object.optString("Plant_Name");

                            binding.spinnerPlant.setPrompt(plantName);
                            list.add(plantName);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinnerPlant.setAdapter(adapter);
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

    private boolean validateInputs() {
        mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
        mPlant = binding.spinnerPlant.getSelectedItem().toString();
        mCenterName = binding.edCenterName.getText().toString().trim();
        mFarmerCodeName = binding.edFarmerCodeName.getText().toString().trim();
        mTypeOfProduct = binding.spinnerTypeOfProduct.getSelectedItem().toString();
        mTeatTipCup = binding.edTeatTipCup.getText().toString().trim();
        mTypeOfService = binding.spinnerTypeOfService.getSelectedItem().toString();
        mFodderDev = binding.edFodderDevelopmentAcres.getText().toString();
        mNoOfFarmersEnrolled = binding.edNoOfFarmersEnrolled.getText().toString().trim();
        mNoOfFarmersInducted = binding.edNoOfFarmersInducted.getText().toString().trim();

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
        if ("".equals(mFarmerCodeName)){
            binding.edFarmerCodeName.setError("Empty field");
            binding.edFarmerCodeName.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mTypeOfProduct)){
            ((TextView)binding.spinnerTypeOfProduct.getSelectedView()).setError("Select");
            binding.spinnerTypeOfProduct.getSelectedView().requestFocus();
            binding.txtTypeOfProNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mTeatTipCup)){
            binding.edTeatTipCup.setError("Empty field");
            binding.edTeatTipCup.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mTypeOfService)){
            ((TextView)binding.spinnerTypeOfService.getSelectedView()).setError("Select");
            binding.spinnerTypeOfService.getSelectedView().requestFocus();
            binding.txtTypeOfServiceNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapFormersMeeting == null){
            binding.txtFarmersImageNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapCSRActivity == null){
            binding.txtCsrImageNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mFodderDev)){
            binding.edFodderDevelopmentAcres.setError("Empty field");
            binding.edFodderDevelopmentAcres.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapFodderDevAcres == null){
            binding.txtFodderImageNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfFarmersEnrolled)){
            binding.edNoOfFarmersEnrolled.setError("Empty field");
            binding.edNoOfFarmersEnrolled.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfFarmersInducted)){
            binding.edNoOfFarmersInducted.setError("Empty field");
            binding.edNoOfFarmersInducted.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private void saveNow() {
        String mActiveFlag = "1";
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("company", mCompanyName);
        serviceIntent.putExtra("plant", mPlant);
        serviceIntent.putExtra("center_name", mCenterName);

        serviceIntent.putExtra("farmer_name", mFarmerCodeName);
        serviceIntent.putExtra("product_type", mTypeOfProduct);
        serviceIntent.putExtra("teat_dip", mTeatTipCup);
        serviceIntent.putExtra("service_type", mTypeOfService);

        serviceIntent.putExtra("fodder_dev_acres", mFodderDev);


        serviceIntent.putExtra("active_flag", mActiveFlag);
        serviceIntent.putExtra("upload_service_id", "3");
        ContextCompat.startForegroundService(this, serviceIntent);

        finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Farmers meeting
        fileFormersMeeting = new File(getExternalFilesDir(null), "/procurement/" +"FAR_123.jpg");
        bitmapFormersMeeting = BitmapFactory.decodeFile(fileFormersMeeting.getAbsolutePath());

        // CSR Activity
        fileCSRActivity = new File(getExternalFilesDir(null), "/procurement/" +"CSR_123.jpg");
        bitmapCSRActivity = BitmapFactory.decodeFile(fileCSRActivity.getAbsolutePath());

        // Fodder dev acres
        fileFodderDevAcres = new File(getExternalFilesDir(null), "/procurement/" +"FDA_123.jpg");
        bitmapFodderDevAcres = BitmapFactory.decodeFile(fileFodderDevAcres.getAbsolutePath());

        if (bitmapFormersMeeting != null){
            binding.imageViewFormersMeetingLayout.setVisibility(View.VISIBLE);
            binding.imageFarmersMeeting.setImageBitmap(bitmapFormersMeeting);
            binding.txtErrorFound.setVisibility(View.GONE);
        }

        if (bitmapCSRActivity != null){
            binding.imageViewCsrActivityLayout.setVisibility(View.VISIBLE);
            binding.imageCsrActivity.setImageBitmap(bitmapCSRActivity);
            binding.txtErrorFound.setVisibility(View.GONE);
        }

        if (bitmapFodderDevAcres != null){
            binding.imageViewFoderLayout.setVisibility(View.VISIBLE);
            binding.imageFoder.setImageBitmap(bitmapFodderDevAcres);
            binding.txtErrorFound.setVisibility(View.GONE);
        }
    }
}