package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_PLANT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcSubDivison;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityProcurementAssetBinding;
import com.saneforce.godairy.procurement.database.DatabaseManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcurementAssetActivity extends AppCompatActivity {
    private ActivityProcurementAssetBinding binding;
    private final Context context = this;
    private String mCompany, mPlant, mTypeOfAsset = "", mComments;
    private final List<String> list = new ArrayList<>();
    private ArrayList<String> list2;
    private static final String TAG = "Procurement_";
    private DatabaseManager databaseManager;
    private ArrayList<ProcSubDivison> subDivisonArrayList;
    private final List<String> listSub = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProcurementAssetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list2 = new ArrayList<>();

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        loadSubDivision();
        loadPlant();
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

    private void onClick() {
        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                saveNow();
            }
        });

        binding.msAmcuSet.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("AMCU Set");
            }else {
                binding.msAmcuSet.setChecked(false);
                list2.remove("AMCU Set");
            }
        });

        binding.msBulkMilkCo.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Bulk Milk Cooler");
            }else {
                binding.msBulkMilkCo.setChecked(false);
                list2.remove("Bulk Milk Cooler");
            }
        });

        binding.msChiller.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Chiller");
            }else {
                binding.msChiller.setChecked(false);
                list2.remove("Chiller");
            }
        });

        binding.msMilkPump.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Milk Pump");
            }else {
                binding.msMilkPump.setChecked(false);
                list2.remove("Milk Pump");
            }
        });

        binding.msAmmoCompres.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Ammoniya Compressor");
            }else {
                binding.msAmmoCompres.setChecked(false);
                list2.remove("Ammoniya Compressor");
            }
        });

        binding.msCondencingUnit.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Condensing Unit");
            }else {
                binding.msCondencingUnit.setChecked(false);
                list2.remove("Condensing Unit");
            }
        });

        binding.msChillWaterPump.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Chill Water Pump");
            }else {
                binding.msChillWaterPump.setChecked(false);
                list2.remove("Chill Water Pump");
            }
        });

        binding.msMiniFridge.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Mini Fridge");
            }else {
                binding.msMiniFridge.setChecked(false);
                list2.remove("Mini Fridge");
            }
        });

        binding.msCans.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Cans");
            }else {
                binding.msCans.setChecked(false);
                list2.remove("Cans");
            }
        });

        binding.msCennterifuge.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Centrifuge");
            }else {
                binding.msCennterifuge.setChecked(false);
                list2.remove("Centrifuge");
            }
        });

        binding.msIncubator.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Incubator");
            }else {
                binding.msIncubator.setChecked(false);
                list2.remove("Incubator");
            }
        });

        binding.msHotAirOven.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Hot Air Oven");
            }else {
                binding.msHotAirOven.setChecked(false);
                list2.remove("Hot Air Oven");
            }
        });

        binding.msSilo.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("SILO");
            }else {
                binding.msSilo.setChecked(false);
                list2.remove("SILO");
            }
        });

        binding.msWaterBath.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Water Bath");
            }else {
                binding.msWaterBath.setChecked(false);
                list2.remove("Water Bath");
            }
        });

        binding.msSolarPanels.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Solar Panels");
            }else {
                binding.msSolarPanels.setChecked(false);
                list2.remove("Solar Panels");
            }
        });

        binding.msMotors.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("Motors");
            }else {
                binding.msMotors.setChecked(false);
                list2.remove("Motors");
            }
        });

        binding.msUps.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("UPS Batteries Stabilizer");
            }else {
                binding.msUps.setChecked(false);
                list2.remove("UPS Batteries Stabilizer");
            }
        });

        binding.msIptCoil.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("IBT Coil");
            }else {
                binding.msIptCoil.setChecked(false);
                list2.remove("IBT Coil");
            }
        });

        binding.msItAsset.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                list2.add("IT Asset (Desktop, Printer &amp; Tab)");
            }else {
                binding.msItAsset.setChecked(false);
                list2.remove("IT Asset (Desktop, Printer &amp; Tab)");
            }
        });


        binding.spinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCompany = binding.spinnerCompany.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerPlant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerPlant.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

//        binding.typeOfAsset.setOnClickListener(v -> {
//            Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show();
//            mTypeOfAsset = "Yes";
//        });

        binding.back.setOnClickListener(view -> finish());
    }

    private void saveNow() {

        Set<String> s = new LinkedHashSet<>(list2);
        String arrayList = s.toString();
        Log.d("list__", arrayList);

        String mActiveFlag = "1";
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("company", mCompany);
        serviceIntent.putExtra("plant", mPlant);
        serviceIntent.putExtra("asset_type", arrayList);
        serviceIntent.putExtra("comments", mComments);
        serviceIntent.putExtra("active_flag", mActiveFlag);
        serviceIntent.putExtra("upload_service_id", "9");
        ContextCompat.startForegroundService(this, serviceIntent);
        finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
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
//        if ("".equals(mTypeOfAsset)){
//            Toast.makeText(context, "Please select asset", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        /*
        if ("".equals(mComments)){
            binding.edComments.setError("Enter comments");
            binding.edComments.requestFocus();
            return false;
        }
         */
        return true;
    }
}