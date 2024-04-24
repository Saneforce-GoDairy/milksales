package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_PLANT;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcSubDivison;
import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityMaintanenceIssuesFormBinding;
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

public class MaintanenceIssuesFormActivity extends AppCompatActivity {
    private ActivityMaintanenceIssuesFormBinding binding;
    private String mCompany, mPlant, mNoOfEquipment, mTypeOfRepair;
    private final Context context = this;
    private Bitmap bitmapRepair;
    private final List<String> list = new ArrayList<>();
    private static final String TAG = "Procurement_";
    private DatabaseManager databaseManager;
    private ArrayList<ProcSubDivison> subDivisonArrayList;
    private final List<String> listSub = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaintanenceIssuesFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        loadSubDivision();
        initSpinnerArray();
        onClick();
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

    private void initSpinnerArray() {
        loadPlant();

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.plant_array, R.layout.custom_spinner);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfRepair.setAdapter(adapter3);
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
                       // throw new RuntimeException(e);
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

            7, MaintenanceIssueActivity
               Type of repair image = 14

            8, MaintenanceRegularActivity
               DG Set Running Hrs, After Last Services = 15

            9, New farmer creation ska
               Competitors = 16
         */


        binding.cameraRepair.setOnClickListener(view -> {
            binding.txtImgRepairNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Maintenance repair");
            intent.putExtra("camera_id", "14");
            startActivity(intent);
        });

        binding.imageViewRepairLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "MAIN_RE_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Maintenance repair");
            startActivity(intent);
        });


        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                saveNow();
            }
        });

        binding.spinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCompany = binding.spinnerCompany.getSelectedItem().toString();
                binding.txtCompanyNotValid.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerPlant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerPlant.getSelectedItem().toString();
                binding.txtPlantNotValid.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.back.setOnClickListener(view -> finish());
    }

    private void saveNow() {
        String mActiveFlag = "1";
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("company", mCompany);
        serviceIntent.putExtra("plant", mPlant);
        serviceIntent.putExtra("equipment", mNoOfEquipment);
        serviceIntent.putExtra("repair_type", mTypeOfRepair);
        serviceIntent.putExtra("active_flag", mActiveFlag);
        serviceIntent.putExtra("upload_service_id", "8");
        ContextCompat.startForegroundService(this, serviceIntent);
        finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs() {
        mCompany = binding.spinnerCompany.getSelectedItem().toString();
        mPlant = binding.spinnerPlant.getSelectedItem().toString();
        mNoOfEquipment = binding.edNoOfEqp.getText().toString().trim();
        mTypeOfRepair = binding.spinnerTypeOfRepair.getSelectedItem().toString();

        if ("Select".equals(mCompany)){
            ((TextView)binding.spinnerCompany.getSelectedView()).setError("Select company");
            binding.spinnerCompany.getSelectedView().requestFocus();
            binding.txtCompanyNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mPlant)){
            ((TextView)binding.spinnerPlant.getSelectedView()).setError("Select plant");
            binding.spinnerPlant.getSelectedView().requestFocus();
            binding.txtPlantNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfEquipment)){
            binding.edNoOfEqp.setError("Enter center name");
            binding.edNoOfEqp.requestFocus();
            return false;
        }
        if ("Select".equals(mTypeOfRepair)){
            ((TextView)binding.spinnerTypeOfRepair.getSelectedView()).setError("Select type of repair");
            binding.spinnerTypeOfRepair.getSelectedView().requestFocus();
            return false;
        }
        if (bitmapRepair == null){
            binding.txtImgRepairNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        File fileRepair = new File(getExternalFilesDir(null), "/procurement/" + "MAIN_RE_123.jpg");
        bitmapRepair = BitmapFactory.decodeFile(fileRepair.getAbsolutePath());
        binding.txtImgRepairNotValid.setVisibility(View.GONE);

        if (bitmapRepair != null){
            binding.imageViewRepairLayout.setVisibility(View.VISIBLE);
            binding.imageRepair.setImageBitmap(bitmapRepair);
            binding.txtImgRepairNotValid.setVisibility(View.GONE);
        }
    }
}