package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_PLANT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import android.widget.TextView;
import android.widget.Toast;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcSubDivison;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityMaintanenceRegularBinding;
import com.saneforce.godairy.procurement.database.DatabaseManager;

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

public class MaintanenceRegularActivity extends AppCompatActivity {
    private ActivityMaintanenceRegularBinding binding;
    private final Context context = this;
    private static final String TAG = "Procurement_";
    private DatabaseManager databaseManager;
    private ArrayList<ProcSubDivison> subDivisonArrayList;
    private final List<String> listSub = new ArrayList<>();
    private final List<String> list = new ArrayList<>();
    private Bitmap bitmap;
    private String mCompanyName, mPlant, mBmcNoHrsRunning, mBmcVolumeCollect, mCcNoHrsRunning, mCcVolumeCollect, mIBTRunningHrs, mDgSetRunnings, mPowerFactor;
    private String mPipeline, mLeakages, mScale, mFuelConsStockPerBook, mFuelConsStockPerPhysical, mETP = "", mHotWater = "", mFactoryLicenceIns = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaintanenceRegularBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        loadSubDivision();
        loadPlant();
        onClick();
    }

    private void onClick() {
        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                saveNow();
            }
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

            8, MaintenanceRegularActivity
               DG Set Running Hrs, After Last Services = 15

            9, New farmer creation ska
               Competitors = 16
         */

        binding.cameraDgRunningHrs.setOnClickListener(v -> {
            binding.txtDgRunningHrsImageNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "DG Set Running Hrs");
            intent.putExtra("camera_id", "15");
            startActivity(intent);
        });

        binding.imageViewDgRunningHrsLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "MAIN_REGU_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "DG Set Running Hrs");
            startActivity(intent);
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

        binding.edBmcHrsRunning.addTextChangedListener(new TextWatcher() {
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

        binding.edBmcVolumeCollected.addTextChangedListener(new TextWatcher() {
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

        binding.edCcHrsRunning.addTextChangedListener(new TextWatcher() {
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

        binding.edCcVolumeCollected.addTextChangedListener(new TextWatcher() {
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

        binding.edIbtRunningHrs.addTextChangedListener(new TextWatcher() {
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

        binding.edDgSetRungAfLs.addTextChangedListener(new TextWatcher() {
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

        binding.edPowerFactor.addTextChangedListener(new TextWatcher() {
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

        binding.edPipeCond.addTextChangedListener(new TextWatcher() {
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

        binding.edLeakage.addTextChangedListener(new TextWatcher() {
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

        binding.edScale.addTextChangedListener(new TextWatcher() {
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

        binding.edPerBook.addTextChangedListener(new TextWatcher() {
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

        binding.edPhysical.addTextChangedListener(new TextWatcher() {
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

        binding.rdEtpWorking.setOnClickListener(v -> {
            Toast.makeText(context, "Working", Toast.LENGTH_SHORT).show();
            binding.rdEtpRepair.setChecked(false);
            mETP = "Working";
        });

        binding.rdEtpRepair.setOnClickListener(v -> {
            Toast.makeText(context, "Repair", Toast.LENGTH_SHORT).show();
            binding.rdEtpWorking.setChecked(false);
            mETP = "Repair";
        });

        binding.hwSolar.setOnClickListener(v -> {
            Toast.makeText(context, "Solar", Toast.LENGTH_SHORT).show();
            binding.hwElectric.setChecked(false);
            mHotWater = "Solar";
        });

        binding.hwElectric.setOnClickListener(v -> {
            Toast.makeText(context, "Electric", Toast.LENGTH_SHORT).show();
            binding.hwSolar.setChecked(false);
            mHotWater = "Electric";
        });

        binding.renewed.setOnClickListener(v -> {
            Toast.makeText(context, "Renewed", Toast.LENGTH_SHORT).show();
            binding.nonRenewed.setChecked(false);
            mFactoryLicenceIns = "Renewed";
        });

        binding.nonRenewed.setOnClickListener(v -> {
            Toast.makeText(context, "Non Renewed", Toast.LENGTH_SHORT).show();
            binding.renewed.setChecked(false);
            mFactoryLicenceIns = "Non Renewed";
        });

        binding.back.setOnClickListener(view -> finish());
    }

    private void saveNow() {
        String mActiveFlag = "1";
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("company", mCompanyName);
        serviceIntent.putExtra("plant", mPlant);
        serviceIntent.putExtra("bmc_hrs_run", mBmcNoHrsRunning);
        serviceIntent.putExtra("bmc_volume_coll", mBmcVolumeCollect);
        serviceIntent.putExtra("cc_hrs_running", mCcNoHrsRunning);
        serviceIntent.putExtra("cc_volume_coll", mCcVolumeCollect);
        serviceIntent.putExtra("ibt_running_hrs", mIBTRunningHrs);
        serviceIntent.putExtra("dg_set_running", mDgSetRunnings);
        serviceIntent.putExtra("power_factor", mPowerFactor);
        serviceIntent.putExtra("pipeline_condition", mPipeline);
        serviceIntent.putExtra("leakage", mLeakages);
        serviceIntent.putExtra("scale", mScale);
        serviceIntent.putExtra("per_book", mFuelConsStockPerBook);
        serviceIntent.putExtra("physical", mFuelConsStockPerPhysical);
        serviceIntent.putExtra("etp", mETP);
        serviceIntent.putExtra("hot_water", mHotWater);
        serviceIntent.putExtra("factory_license_ins", mFactoryLicenceIns);
        serviceIntent.putExtra("active_flag", mActiveFlag);
        serviceIntent.putExtra("upload_service_id", "10");
        ContextCompat.startForegroundService(this, serviceIntent);
        finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs() {
        mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
        mPlant = binding.spinnerPlant.getSelectedItem().toString();
        mBmcNoHrsRunning = binding.edBmcHrsRunning.getText().toString();
        mBmcVolumeCollect = binding.edBmcVolumeCollected.getText().toString();
        mCcNoHrsRunning = binding.edCcHrsRunning.getText().toString();
        mCcVolumeCollect = binding.edCcVolumeCollected.getText().toString();
        mIBTRunningHrs = binding.edIbtRunningHrs.getText().toString();
        mDgSetRunnings = binding.edDgSetRungAfLs.getText().toString();
        mPowerFactor = binding.edPowerFactor.getText().toString();
        mPipeline = binding.edPipeCond.getText().toString();
        mLeakages = binding.edLeakage.getText().toString();
        mScale = binding.edScale.getText().toString();
        mFuelConsStockPerBook = binding.edPerBook.getText().toString();
        mFuelConsStockPerPhysical = binding.edPhysical.getText().toString();

/*
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
        if ("".equals(mBmcNoHrsRunning)){
            binding.edBmcHrsRunning.setError("Empty field");
            binding.edBmcHrsRunning.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mBmcVolumeCollect)){
            binding.edBmcVolumeCollected.setError("Empty field");
            binding.edBmcVolumeCollected.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCcNoHrsRunning)){
            binding.edCcHrsRunning.setError("Empty field");
            binding.edCcHrsRunning.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCcVolumeCollect)){
            binding.edCcVolumeCollected.setError("Empty field");
            binding.edCcVolumeCollected.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mIBTRunningHrs)){
            binding.edIbtRunningHrs.setError("Empty field");
            binding.edIbtRunningHrs.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mDgSetRunnings)){
            binding.edDgSetRungAfLs.setError("Empty field");
            binding.edDgSetRungAfLs.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmap == null){
            binding.txtDgRunningHrsImageNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mPowerFactor)){
            binding.edPowerFactor.setError("Empty field");
            binding.edPowerFactor.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mPipeline)){
            binding.edPipeCond.setError("Empty field");
            binding.edPipeCond.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mLeakages)){
            binding.edLeakage.setError("Empty field");
            binding.edLeakage.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mScale)){
            binding.edScale.setError("Empty field");
            binding.edScale.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mFuelConsStockPerBook)){
            binding.edPerBook.setError("Empty field");
            binding.edPerBook.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mFuelConsStockPerPhysical)){
            binding.edPhysical.setError("Empty field");
            binding.edPhysical.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mETP)){
            Toast.makeText(context, "Please select Effluent Treatment Plant", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(mHotWater)){
            Toast.makeText(context, "Please select Heater", Toast.LENGTH_SHORT).show();
            return false;
        }

        if ("".equals(mFactoryLicenceIns)){
            Toast.makeText(context, "Please select Factory Licence Inspection", Toast.LENGTH_SHORT).show();
            return false;
        }
 */
        return true;
    }

    private void loadSubDivision() {
        subDivisonArrayList = new ArrayList<>(databaseManager.loadSubDivision());
        listSub.add("Select");
        for (int i = 0; i<subDivisonArrayList.size(); i++){
            Log.e(TAG, subDivisonArrayList.get(i).getSubdivision_sname());
            listSub.add(subDivisonArrayList.get(i).getSubdivision_sname());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listSub);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompany.setAdapter(adapter);
    }

    private void loadPlant() {
        list.add("Select");
        updatePlant();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getProcPlant(PROCUREMENT_GET_PLANT);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String plantList;
                    try {
                        binding.spinnerPlant.setAdapter(null);
                        plantList = response.body().string();

                        JSONArray jsonArray = new JSONArray(plantList);

                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String plantName = object.optString("plant_name");

                            binding.spinnerPlant.setPrompt(plantName);
                            list.add(plantName);
                        }
                        updatePlant();
                    } catch (IOException | JSONException e) {
                        //  throw new RuntimeException(e);
                        Toast.makeText(context, "Plant list load error!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePlant() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPlant.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        File file = new File(getExternalFilesDir(null), "/procurement/" + "MAIN_REGU_123.jpg");
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        if (bitmap != null){
            binding.imageViewDgRunningHrsLayout.setVisibility(View.VISIBLE);
            binding.imageDgRunningHrs.setImageBitmap(bitmap);
            binding.txtErrorFound.setVisibility(View.GONE);
        }
    }
}