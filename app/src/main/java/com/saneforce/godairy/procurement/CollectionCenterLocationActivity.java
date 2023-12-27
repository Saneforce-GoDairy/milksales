package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_PLANT;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_POST_COLL_CENTER_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.saneforce.godairy.Model_Class.PrimaryNoOrderList;
import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityColletionCenterLocationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionCenterLocationActivity extends AppCompatActivity {
    private ActivityColletionCenterLocationBinding binding;
    private String mCompanyName, mPlant, mSapCenterCode, mSapCenterName, mCenterAddress, mPotentialLpd;
    private String mNoOfFarmersEnrolled, mCompetitorLpdSinner1, mCompetitorLpdSinner2, mCompetitorLpdSinner3, mCompetitorLpdSinner4, mCompetitorLpdSinner5;
    private String mCompetitorLpdEdText1, mCompetitorLpdEdText2, mCompetitorLpdEdText3, mCompetitorLpdEdText4, mCompetitorLpdEdText5;
    private final Context context = this;
    private Bitmap bitmapCollectCenter;
    private File fileCollectCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityColletionCenterLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSpinnerArray();
        onClick();
    }

    private void initSpinnerArray() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.company_array, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompany.setAdapter(adapter);

        loadPlant();

//        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
//                R.array.plant_array, R.layout.custom_spinner);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        binding.spinnerPlant.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.competitor_array, R.layout.custom_spinner);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompetitorLpd1.setAdapter(adapter3);
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
                            PlantModel plantModel = new PlantModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            String plantName = object.optString("Plant_Name");

                            binding.spinnerPlant.setPrompt(plantName);
                            list.add(plantName);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
                        adapter.setDropDownViewResource(R.layout.custom_spinner);
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

    public class PlantModel {
        private String Plant_Name;

        public String getPlant_Name() {
            return Plant_Name;
        }

        public void setPlant_Name(String plant_Name) {
            Plant_Name = plant_Name;
        }
    }

    private void onClick() {

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

        binding.spinnerCompetitorLpd1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerCompetitorLpd1.getSelectedItem().toString();
                binding.txtCompetitorLpd1NotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.back.setOnClickListener(view -> finish());

          /*
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


        binding.cameraCollectCenter.setOnClickListener(view -> {
            binding.txtCollectCenterNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Collection center");
            intent.putExtra("camera_id", "5");
            startActivity(intent);
        });

        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
              //  Toast.makeText(context, "valid", Toast.LENGTH_SHORT).show();
                saveNow();
            }
        });

        binding.imageViewCollectCenterLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "CC_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Collection Center");
            startActivity(intent);
        });
    }

    private void saveNow() {

         String mActiveFlag = "1";

        String mDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String mTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String mTimeDate  = mDate +" "+mTime;
//
//
//        ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);
//
//        Call<ResponseBody> call = apiInterface.submitProcCollectionCenterLo(PROCUREMENT_POST_COLL_CENTER_LOCATION,
//                                                                            mCompanyName,
//                                                                            mPlant,
//                                                                            mSapCenterCode,
//                                                                            mSapCenterName,
//                                                                            mCenterAddress,
//                                                                            mPotentialLpd,
//                                                                            mNoOfFarmersEnrolled,
//                                                                            mCompetitorLpdSinner1,
//                                                                            mCompetitorLpdEdText1,
//                                                                            mActiveFlag,
//                                                                            mTimeDate);
//
//        call.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    String res;
//
//                    try {
//                        res = response.body().string();
//
//                        Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        String dir = getExternalFilesDir("/").getPath() + "/" + ".skyblue/";

        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("company", mCompanyName);
        serviceIntent.putExtra("plant", mPlant);
        serviceIntent.putExtra("center_code", mSapCenterCode);
        serviceIntent.putExtra("center_name", mSapCenterName);
        serviceIntent.putExtra("center_addr", mCenterAddress);
        serviceIntent.putExtra("potential_lpd", mPotentialLpd);
        serviceIntent.putExtra("farmers_enrolled", mNoOfFarmersEnrolled);
        serviceIntent.putExtra("competitor_lpd", mCompetitorLpdSinner1);
        serviceIntent.putExtra("competitor_lpd1", mCompetitorLpdEdText1);
        serviceIntent.putExtra("active_flag", mActiveFlag);
        serviceIntent.putExtra("time_date", mTimeDate);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private boolean validateInputs() {
        mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
        mPlant = binding.spinnerPlant.getSelectedItem().toString();
        mSapCenterCode = binding.edSapCenterCode.getText().toString().trim();
        mSapCenterName = binding.edSapCenterName.getText().toString().trim();
        mCenterAddress = binding.edSapAddress.getText().toString().trim();
        mPotentialLpd = binding.edPotentialLpd.getText().toString().trim();
        mNoOfFarmersEnrolled = binding.edEnrolled.getText().toString().trim();

        mCompetitorLpdSinner1 = binding.spinnerCompetitorLpd1.getSelectedItem().toString();
        mCompetitorLpdEdText1 = binding.edCompetitorLpd1.getText().toString().trim();

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
//        if (bitmapCollectCenter == null){
//            binding.txtCollectCenterNotValid.setVisibility(View.VISIBLE);
//            binding.txtErrorFound.setVisibility(View.VISIBLE);
//            return false;
//        }
        if ("".equals(mSapCenterCode)){
            binding.edSapCenterCode.setError("Enter Sap Center Code");
            binding.edSapCenterCode.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mSapCenterName)){
            binding.edSapCenterName.setError("Enter Sap Center Name");
            binding.edSapCenterName.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCenterAddress)){
            binding.edSapAddress.setError("Enter Center Address");
            binding.edSapAddress.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mPotentialLpd)){
            binding.edPotentialLpd.setError("Enter Potential LPD");
            binding.edPotentialLpd.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfFarmersEnrolled)){
            binding.edEnrolled.setError("Enter Farmers Enrolled");
            binding.edEnrolled.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mCompetitorLpdSinner1)){
            ((TextView)binding.spinnerCompetitorLpd1.getSelectedView()).setError("Select Competitor LPD1");
            binding.spinnerCompetitorLpd1.getSelectedView().requestFocus();
            binding.txtCompetitorLpd1NotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
//        if ("".equals(mCompetitorLpdEdText1)){
//            binding.edCompetitorLpd1.setError("Enter Competitor LPD1");
//            binding.edCompetitorLpd1.requestFocus();
//            binding.txtErrorFound.setVisibility(View.VISIBLE);
//            return false;
//        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Farmers meeting
        fileCollectCenter = new File(getExternalFilesDir(null), "/procurement/" + "CC_123.jpg");
        bitmapCollectCenter = BitmapFactory.decodeFile(fileCollectCenter.getAbsolutePath());

        if (bitmapCollectCenter != null){
            binding.imageViewCollectCenterLayout.setVisibility(View.VISIBLE);
            binding.imageCollectCenter.setImageBitmap(bitmapCollectCenter);
            binding.txtCollectCenterNotValid.setVisibility(View.GONE);
            binding.txtErrorFound.setVisibility(View.GONE);
        }
    }
}