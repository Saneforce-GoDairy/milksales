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
    Toolbar mToolbar;
    ArrayAdapter<String> mAdapter;
    ListView mListView;
    TextView mEmptyView;
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


    private void onClick() {
        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                saveNow();
            }
        });

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

        binding.cameraNoHrsRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtImgNoHrsRunningNotValid.setVisibility(View.GONE);
                Intent intent = new Intent(context, ProcurementCameraX.class);
                intent.putExtra("event_name", "No Hrs Running");
                intent.putExtra("camera_id", "21");
                startActivity(intent);
            }
        });

        binding.imageViewNoHrsRunningLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "MAIN_RE_NOHRS_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "No Hrs Running");
            startActivity(intent);
        });

        binding.cameraAsPerBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtImgAsPerBookNotValid.setVisibility(View.GONE);
                Intent intent = new Intent(context, ProcurementCameraX.class);
                intent.putExtra("event_name", "As Per Book");
                intent.putExtra("camera_id", "22");
                startActivity(intent);
            }
        });

        binding.imageViewAsPerBookLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "MAIN_RE_AS_PER_BOOK_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "As Per Book");
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
        mPlant = binding.edPlant.getText().toString();
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
        return true;
    }

    private void loadSubDivision() {
        subDivisonArrayList = new ArrayList<>(databaseManager.loadSubDivision());
        listSub.add("Select");
        for (int i = 0; i<subDivisonArrayList.size(); i++){
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

    @Override
    protected void onResume() {
        super.onResume();

        File file = new File(getExternalFilesDir(null), "/procurement/" + "MAIN_REGU_123.jpg");
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        File fileNofHrsRuns = new File(getExternalFilesDir(null), "/procurement/" + "MAIN_RE_NOHRS_123.jpg");
        Bitmap bitmap2 = BitmapFactory.decodeFile(fileNofHrsRuns.getAbsolutePath());

        File fileAsPerBook = new File(getExternalFilesDir(null), "/procurement/" + "MAIN_RE_AS_PER_BOOK_123.jpg");
        Bitmap bitmap3 = BitmapFactory.decodeFile(fileAsPerBook.getAbsolutePath());


        if (bitmap != null){
            binding.imageViewDgRunningHrsLayout.setVisibility(View.VISIBLE);
            binding.imageDgRunningHrs.setImageBitmap(bitmap);
            binding.txtErrorFound.setVisibility(View.GONE);
        }

        if (bitmap2 != null){
            binding.imageViewNoHrsRunningLayout.setVisibility(View.VISIBLE);
            binding.imageNoHrsRunning.setImageBitmap(bitmap2);
            binding.txtImgNoHrsRunningNotValid.setVisibility(View.GONE);
        }

        if (bitmap3 != null){
            binding.imageViewAsPerBookLayout.setVisibility(View.VISIBLE);
            binding.imageAsPerBook.setImageBitmap(bitmap3);
            binding.txtImgAsPerBookNotValid.setVisibility(View.GONE);
        }
    }
}