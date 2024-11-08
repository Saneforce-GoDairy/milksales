package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_PLANT;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcSubDivison;
import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityQualityFormBinding;
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

public class QualityFormActivity extends AppCompatActivity {
    private ActivityQualityFormBinding binding;
    Toolbar mToolbar;
    ArrayAdapter<String> mAdapter;
    ListView mListView;
    TextView mEmptyView;
    private String mCompanyName, mPlant, mMassBalance, mMilkCollection, mMBRT, mRejection, mSpecialCleaning, mCleaningEfficiency;
    private String mNoOfVehiclesReceivedWithHood, mNoOfVehiclesReceivedWithOutHood,mRecordChemicals, mRecordStock, mRecordMilk;
    private String mAwarenessProgram = "", mSamplesCalibrationNoOfFat, mSamplesCalibrationNoOfSnf, mSamplesCalibrationNoOfWeight;
    private Bitmap bitmapFat, bitmapSnf, bitmapWithHood, bitmapWithoutHood, bitmapAwarenessProgram;
    private final Context context = this;
    private final List<String> list = new ArrayList<>();
    private static final String TAG = "Procurement_";
    private DatabaseManager databaseManager;
    private final List<String> listSub = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQualityFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        loadSubDivision();
        loadPlant();
        onClick();

        binding.edPlant.setFocusable(false);

        binding.edPlant.setOnClickListener(v -> {
            binding.formCon.setVisibility(View.GONE);
            binding.plantCon.setVisibility(View.VISIBLE);
        });

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mListView = findViewById(R.id.list);
        mEmptyView = findViewById(R.id.emptyView);

        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            binding.edPlant.setText(adapterView.getItemAtPosition(i).toString());
            binding.plantCon.setVisibility(View.GONE);
            binding.formCon.setVisibility(View.VISIBLE);
        });

        mListView.setEmptyView(mEmptyView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_toolbar, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search plant");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    private void loadSubDivision() {
        ArrayList<ProcSubDivison> subDivisonArrayList = new ArrayList<>(databaseManager.loadSubDivision());
        listSub.add("Select");
        for (int i = 0; i< subDivisonArrayList.size(); i++){
            Log.e(TAG, subDivisonArrayList.get(i).getSubdivision_sname());
            listSub.add(subDivisonArrayList.get(i).getSubdivision_sname());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listSub);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompany.setAdapter(adapter);
    }

    private void loadPlant() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getProcPlant(PROCUREMENT_GET_PLANT);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String plantList;
                    try {
                        plantList = response.body().string();

                        JSONArray jsonArray = new JSONArray(plantList);

                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String plantName = object.optString("plant_name");

                            list.add(plantName);
                        }
                        mAdapter = new ArrayAdapter<>(context,
                                android.R.layout.simple_list_item_1,
                                list);

                        mListView.setAdapter(mAdapter);
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

    private void onClick() {
        binding.cameraFat.setOnClickListener(view -> {
            binding.txtImgFatNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Quality FAT");
            intent.putExtra("camera_id", "8");
            startActivity(intent);
        });

        binding.imageViewFatLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "QUA_FAT_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Quality FAT");
            startActivity(intent);
        });

        binding.cameraSnf.setOnClickListener(view -> {
            binding.txtImgSnfNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Quality SNF");
            intent.putExtra("camera_id", "9");
            startActivity(intent);
        });

        binding.imageViewSnfLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "QUA_SNF_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Quality SNF");
            startActivity(intent);
        });

        binding.cameraWithHood.setOnClickListener(view -> {
            binding.txtImgWithHoodNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Received No of vehicle Hoods");
            intent.putExtra("camera_id", "10");
            startActivity(intent);
        });

        binding.imageViewWithHoodLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "QUA_RNV_WITH_HOODS_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Received No of vehicle Hoods");
            startActivity(intent);
        });

        binding.cameraWithoutHood.setOnClickListener(view -> {
            binding.txtImgWithoutHoodNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Received No of vehicle Without Hood");
            intent.putExtra("camera_id", "11");
            startActivity(intent);
        });

        binding.imageViewWithoutHoodLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "QUA_RNV_WITHOUT_HOODS_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Received No of vehicle Without Hood");
            startActivity(intent);
        });

        binding.awsGeneral.setOnClickListener(v -> {
            Toast.makeText(context, "General", Toast.LENGTH_SHORT).show();
            mAwarenessProgram = "General";

            binding.awsCalibration.setChecked(false);
            binding.awsAudit.setChecked(false);
            binding.awsFieldIntelligenceReport.setChecked(false);
            binding.awsHealthCamp.setChecked(false);
            binding.awsCleanMilkProduction.setChecked(false);
            binding.awsTraining.setChecked(false);
            binding.awsAntibioticAwareness.setChecked(false);
        });

        binding.awsCalibration.setOnClickListener(v -> {
            Toast.makeText(context, "Calibration", Toast.LENGTH_SHORT).show();
            mAwarenessProgram = "Calibration";

            binding.awsGeneral.setChecked(false);
            binding.awsAudit.setChecked(false);
            binding.awsFieldIntelligenceReport.setChecked(false);
            binding.awsHealthCamp.setChecked(false);
            binding.awsCleanMilkProduction.setChecked(false);
            binding.awsTraining.setChecked(false);
            binding.awsAntibioticAwareness.setChecked(false);
        });

        binding.awsAudit.setOnClickListener(v -> {
            Toast.makeText(context, "Audit", Toast.LENGTH_SHORT).show();
            mAwarenessProgram = "Audit";

            binding.awsGeneral.setChecked(false);
            binding.awsCalibration.setChecked(false);
            binding.awsFieldIntelligenceReport.setChecked(false);
            binding.awsHealthCamp.setChecked(false);
            binding.awsCleanMilkProduction.setChecked(false);
            binding.awsTraining.setChecked(false);
            binding.awsAntibioticAwareness.setChecked(false);
        });

        binding.awsFieldIntelligenceReport.setOnClickListener(v -> {
            Toast.makeText(context, "Field Intelligence Report", Toast.LENGTH_SHORT).show();
            mAwarenessProgram = "Field Intelligence Report";

            binding.awsGeneral.setChecked(false);
            binding.awsCalibration.setChecked(false);
            binding.awsAudit.setChecked(false);
            binding.awsHealthCamp.setChecked(false);
            binding.awsCleanMilkProduction.setChecked(false);
            binding.awsTraining.setChecked(false);
            binding.awsAntibioticAwareness.setChecked(false);
        });

        binding.awsHealthCamp.setOnClickListener(v -> {
            Toast.makeText(context, "Health Camp", Toast.LENGTH_SHORT).show();
            mAwarenessProgram = "Health Camp";

            binding.awsGeneral.setChecked(false);
            binding.awsCalibration.setChecked(false);
            binding.awsAudit.setChecked(false);
            binding.awsFieldIntelligenceReport.setChecked(false);
            binding.awsCleanMilkProduction.setChecked(false);
            binding.awsTraining.setChecked(false);
            binding.awsAntibioticAwareness.setChecked(false);
        });

        binding.awsCleanMilkProduction.setOnClickListener(v -> {
            Toast.makeText(context, "Clean Milk Production", Toast.LENGTH_SHORT).show();
            mAwarenessProgram = "Clean Milk Production";

            binding.awsGeneral.setChecked(false);
            binding.awsCalibration.setChecked(false);
            binding.awsAudit.setChecked(false);
            binding.awsFieldIntelligenceReport.setChecked(false);
            binding.awsHealthCamp.setChecked(false);
            binding.awsTraining.setChecked(false);
            binding.awsAntibioticAwareness.setChecked(false);
        });

        binding.awsTraining.setOnClickListener(v -> {
            Toast.makeText(context, "Training", Toast.LENGTH_SHORT).show();
            mAwarenessProgram = "Training";

            binding.awsGeneral.setChecked(false);
            binding.awsCalibration.setChecked(false);
            binding.awsAudit.setChecked(false);
            binding.awsFieldIntelligenceReport.setChecked(false);
            binding.awsHealthCamp.setChecked(false);
            binding.awsCleanMilkProduction.setChecked(false);
            binding.awsAntibioticAwareness.setChecked(false);
        });

        binding.awsAntibioticAwareness.setOnClickListener(v -> {
            Toast.makeText(context, "Antibiotic Awareness", Toast.LENGTH_SHORT).show();
            mAwarenessProgram = "Antibiotic Awareness";

            binding.awsGeneral.setChecked(false);
            binding.awsCalibration.setChecked(false);
            binding.awsAudit.setChecked(false);
            binding.awsFieldIntelligenceReport.setChecked(false);
            binding.awsHealthCamp.setChecked(false);
            binding.awsCleanMilkProduction.setChecked(false);
            binding.awsTraining.setChecked(false);

            binding.antiBioticAwarenesLayout.setVisibility(View.VISIBLE);
        });

        // Awareness program capture image
        binding.cameraAwarenes.setOnClickListener(view -> {
            binding.txtImgAwarenesNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Awareness program");
            intent.putExtra("camera_id", "12");
            startActivity(intent);
        });

        binding.imageViewAwarenesLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "QUA_AWS_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Awareness program");
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

        binding.edMassBalance.addTextChangedListener(new TextWatcher() {
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

        binding.edMilkCollection.addTextChangedListener(new TextWatcher() {
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

        binding.edMbrt.addTextChangedListener(new TextWatcher() {
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

        binding.edRejection.addTextChangedListener(new TextWatcher() {
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

        binding.edSpecialCleaning.addTextChangedListener(new TextWatcher() {
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

        binding.edEfficiency.addTextChangedListener(new TextWatcher() {
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

        binding.edWithHood.addTextChangedListener(new TextWatcher() {
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

        binding.edWithoutHood.addTextChangedListener(new TextWatcher() {
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

        binding.edRecordChemicals.addTextChangedListener(new TextWatcher() {
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

        binding.edRecordStock.addTextChangedListener(new TextWatcher() {
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

        binding.edRecordMilk.addTextChangedListener(new TextWatcher() {
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

        binding.edNoOfFat.addTextChangedListener(new TextWatcher() {
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

        binding.edNoOfSnf.addTextChangedListener(new TextWatcher() {
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

        binding.edNoOfWeight.addTextChangedListener(new TextWatcher() {
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

        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                saveNow();
            }
        });

        binding.back.setOnClickListener(view -> finish());
    }

    private void saveNow() {
        String mActiveFlag = "1";
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("company", mCompanyName);
        serviceIntent.putExtra("plant", mPlant);
        serviceIntent.putExtra("mass_balance", mMassBalance);
        serviceIntent.putExtra("milk_collection", mMilkCollection);
        serviceIntent.putExtra("mbrt", mMBRT);
        serviceIntent.putExtra("rejection", mRejection);
        serviceIntent.putExtra("spl_cleaning", mSpecialCleaning);
        serviceIntent.putExtra("cleaning_efficiency", mCleaningEfficiency);
        serviceIntent.putExtra("vehicle_with_hood", mNoOfVehiclesReceivedWithHood);
        serviceIntent.putExtra("vehicle_without_hood", mNoOfVehiclesReceivedWithOutHood);
        serviceIntent.putExtra("chemicals", mRecordChemicals);
        serviceIntent.putExtra("stock", mRecordStock);
        serviceIntent.putExtra("milk", mRecordMilk);
        serviceIntent.putExtra("awareness_program", mAwarenessProgram);
        serviceIntent.putExtra("no_of_fat", mSamplesCalibrationNoOfFat);
        serviceIntent.putExtra("no_of_snf", mSamplesCalibrationNoOfSnf);
        serviceIntent.putExtra("no_of_weight", mSamplesCalibrationNoOfWeight);
        serviceIntent.putExtra("active_flag", mActiveFlag);
        serviceIntent.putExtra("upload_service_id", "7");
        ContextCompat.startForegroundService(this, serviceIntent);
        finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs() {
        mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
        mPlant = binding.edPlant.getText().toString().trim();

        mMassBalance = binding.edMassBalance.getText().toString().trim();
        mMilkCollection = binding.edMilkCollection.getText().toString().trim();

        mMBRT = binding.edMbrt.getText().toString().trim().trim();
        mRejection = binding.edRejection.getText().toString().trim();

        mSpecialCleaning = binding.edSpecialCleaning.getText().toString().trim();
        mCleaningEfficiency = binding.edEfficiency.getText().toString().trim();

        mNoOfVehiclesReceivedWithHood = binding.edWithHood.getText().toString().trim();
        mNoOfVehiclesReceivedWithOutHood = binding.edWithoutHood.getText().toString().trim();

        mRecordChemicals = binding.edRecordChemicals.getText().toString().trim();
        mRecordStock = binding.edRecordStock.getText().toString().trim();
        mRecordMilk = binding.edRecordMilk.getText().toString().trim();

        mSamplesCalibrationNoOfFat = binding.edNoOfFat.getText().toString().trim();
        mSamplesCalibrationNoOfSnf = binding.edNoOfSnf.getText().toString().trim();
        mSamplesCalibrationNoOfWeight = binding.edNoOfWeight.getText().toString().trim();

        if ("Select".equals(mCompanyName)){
            ((TextView)binding.spinnerCompany.getSelectedView()).setError("Select company");
            binding.spinnerCompany.getSelectedView().requestFocus();
            binding.txtCompanyNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mPlant)){
            binding.txtPlantNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mMassBalance)){
            binding.edMassBalance.setError("Enter mass balance");
            binding.edMassBalance.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mMilkCollection)){
            binding.edMilkCollection.setError("Enter milk collection");
            binding.edMilkCollection.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapFat == null){
            binding.txtImgFatNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapSnf == null){
            binding.txtImgSnfNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mMBRT)){
            binding.edMbrt.setError("Enter MBRT");
            binding.edMbrt.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mRejection)){
            binding.edRejection.setError("Enter Rejection");
            binding.edRejection.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mSpecialCleaning)){
            binding.edSpecialCleaning.setError("Enter Special Cleaning");
            binding.edSpecialCleaning.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCleaningEfficiency)){
            binding.edEfficiency.setError("Enter Cleaning Efficiency");
            binding.edEfficiency.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfVehiclesReceivedWithHood)){
            binding.edWithHood.setError("Enter With Hood");
            binding.edWithHood.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapWithHood == null){
            binding.txtImgWithHoodNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfVehiclesReceivedWithOutHood)){
            binding.edWithoutHood.setError("Enter Without Hood");
            binding.edWithoutHood.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapWithoutHood == null){
            binding.txtImgWithoutHoodNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mRecordChemicals)){
            binding.edRecordChemicals.setError("Enter Record Chemicals");
            binding.edRecordChemicals.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mRecordStock)){
            binding.edRecordStock.setError("Enter Record Stock");
            binding.edRecordStock.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mRecordMilk)){
            binding.edRecordMilk.setError("Enter Record Milk");
            binding.edRecordMilk.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mAwarenessProgram)){
            Toast.makeText(context, "Please select awareness program", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bitmapAwarenessProgram == null){
            binding.txtImgAwarenesNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mSamplesCalibrationNoOfFat)){
            binding.edNoOfFat.setError("Enter No Of Fat");
            binding.edNoOfFat.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mSamplesCalibrationNoOfSnf)){
            binding.edNoOfSnf.setError("Enter No Of Snf");
            binding.edNoOfSnf.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (mSamplesCalibrationNoOfWeight.isEmpty()){
            binding.edNoOfWeight.setError("Enter No Of Weight");
            binding.edNoOfWeight.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        File fileFat = new File(getExternalFilesDir(null), "/procurement/" + "QUA_FAT_123.jpg");
        bitmapFat = BitmapFactory.decodeFile(fileFat.getAbsolutePath());

        File fileSnf = new File(getExternalFilesDir(null), "/procurement/" + "QUA_SNF_123.jpg");
        bitmapSnf = BitmapFactory.decodeFile(fileSnf.getAbsolutePath());

        File fileWithHoods = new File(getExternalFilesDir(null), "/procurement/" + "QUA_RNV_WITH_HOODS_123.jpg");
        bitmapWithHood = BitmapFactory.decodeFile(fileWithHoods.getAbsolutePath());

        File fileWithoutHoods = new File(getExternalFilesDir(null), "/procurement/" + "QUA_RNV_WITHOUT_HOODS_123.jpg");
        bitmapWithoutHood = BitmapFactory.decodeFile(fileWithoutHoods.getAbsolutePath());

        File fileAwarenessProgram = new File(getExternalFilesDir(null), "/procurement/" + "QUA_AWS_123.jpg");
        bitmapAwarenessProgram = BitmapFactory.decodeFile(fileAwarenessProgram.getAbsolutePath());


        if (bitmapFat != null){
            binding.imageViewFatLayout.setVisibility(View.VISIBLE);
            binding.imageFat.setImageBitmap(bitmapFat);
            binding.txtImgFatNotValid.setVisibility(View.GONE);
            binding.txtErrorFound.setVisibility(View.GONE);
        }

        if (bitmapSnf != null){
            binding.imageViewSnfLayout.setVisibility(View.VISIBLE);
            binding.imageSnf.setImageBitmap(bitmapSnf);
            binding.txtImgSnfNotValid.setVisibility(View.GONE);
            binding.txtErrorFound.setVisibility(View.GONE);
        }

        if (bitmapWithHood != null){
            binding.imageViewWithHoodLayout.setVisibility(View.VISIBLE);
            binding.imageWithHood.setImageBitmap(bitmapWithHood);
            binding.txtImgWithHoodNotValid.setVisibility(View.GONE);
            binding.txtErrorFound.setVisibility(View.GONE);
        }

        if (bitmapWithoutHood != null){
            binding.imageViewWithoutHoodLayout.setVisibility(View.VISIBLE);
            binding.imageWithoutHood.setImageBitmap(bitmapWithoutHood);
            binding.txtImgWithoutHoodNotValid.setVisibility(View.GONE);
            binding.txtErrorFound.setVisibility(View.GONE);
        }

        if (bitmapAwarenessProgram != null){
            binding.imageViewAwarenesLayout.setVisibility(View.VISIBLE);
            binding.imageAwarenes.setImageBitmap(bitmapAwarenessProgram);
            binding.txtImgAwarenesNotValid.setVisibility(View.GONE);
            binding.txtErrorFound.setVisibility(View.GONE);
        }
    }
}