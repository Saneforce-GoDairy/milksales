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
import com.saneforce.godairy.databinding.ActivityVeterinaryDoctorsFormBinding;
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

public class VeterinaryDoctorsFormActivity extends AppCompatActivity {
    private ActivityVeterinaryDoctorsFormBinding binding;
    Toolbar mToolbar;
    ArrayAdapter<String> mAdapter;
    ListView mListView;
    TextView mEmptyView;
    private final Context context = this;
    private String mCompanyName, mPlant, mCenterName, mFarmerName, mTypeOfService, mTypeOfProduct, mSeedSales;
    private String mMinaralMixture, mFodderSales, mCattleOrder, mTeatTipCup, mEmergencyEvm, mTypesOfCases;
    private String mFamilyFarmDev, mNoOfFarmersEnrolled, mNoOfFarmersInducted;
    private Bitmap bitmapTypeOfSer, bitmapEVM;
    private final List<String> list = new ArrayList<>();
    private static final String TAG = "Procurement";
    private DatabaseManager databaseManager;
    private final List<String> listSub = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVeterinaryDoctorsFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        loadSubDivision();
        initSpinnerArray();
        onClick();

        binding.edPlant.setFocusable(false);

        binding.edPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.formCon.setVisibility(View.GONE);
                binding.plantCon.setVisibility(View.VISIBLE);
            }
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


    private void initSpinnerArray() {
        loadPlant();
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.veterinary_type_of_service_array, R.layout.custom_spinner);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfService.setAdapter(adapter3);

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.veterinary_type_of_product_array, R.layout.custom_spinner);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfProduct.setAdapter(adapter4);

        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.veterinary_evm_array, R.layout.custom_spinner);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerEmerEvm.setAdapter(adapter5);

        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this,
                R.array.veterinary_type_of_case_array, R.layout.custom_spinner);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfCases.setAdapter(adapter5);
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

    private void onClick() {

        binding.cameraTypeOfService.setOnClickListener(view -> {
            binding.txtImgTypeOfServiceNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Type of service");
            intent.putExtra("camera_id", "6");
            startActivity(intent);
        });

        binding.cameraEmerEvm.setOnClickListener(view -> {
            binding.txtImgTypeOfServiceNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Emergency treatment/EVM");
            intent.putExtra("camera_id", "7");
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
                mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
                binding.txtCompanyNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerTypeOfService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerTypeOfService.getSelectedItem().toString();
                binding.txtTypeOfServiceNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerTypeOfProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerTypeOfProduct.getSelectedItem().toString();
                binding.txtTypeOfProNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerEmerEvm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerEmerEvm.getSelectedItem().toString();
                binding.txtEmerEvmNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerTypeOfCases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerTypeOfCases.getSelectedItem().toString();
                binding.txtTypeOfCasesNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.edCenterNameVisit.addTextChangedListener(new TextWatcher() {
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

        binding.edFarmerCode.addTextChangedListener(new TextWatcher() {
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

        binding.edMineralMixture.addTextChangedListener(new TextWatcher() {
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

        binding.edFodderSettsSale.addTextChangedListener(new TextWatcher() {
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

        binding.edCattleFeedOrder.addTextChangedListener(new TextWatcher() {
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

        binding.edTeatDipCup.addTextChangedListener(new TextWatcher() {
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

        binding.edFamilyFarmDev.addTextChangedListener(new TextWatcher() {
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


        binding.edNoOfFarmerEnrolled.addTextChangedListener(new TextWatcher() {
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

        binding.edNoOfFarmerInducted.addTextChangedListener(new TextWatcher() {
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


        binding.back.setOnClickListener(view -> finish());
    }

    private void saveNow() {
        String mActiveFlag = "1";
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("company", mCompanyName);
        serviceIntent.putExtra("plant", mPlant);
        serviceIntent.putExtra("center_name", mCenterName);
        serviceIntent.putExtra("farmer_name", mFarmerName);
        serviceIntent.putExtra("service_type", mTypeOfService);
        serviceIntent.putExtra("product_type", mTypeOfProduct);
        serviceIntent.putExtra("seed_sale", mSeedSales);
        serviceIntent.putExtra("mineral_mixture", mMinaralMixture);
        serviceIntent.putExtra("fodder_setts_sale_kg", mFodderSales);
        serviceIntent.putExtra("cattle_feed_order_kg", mCattleOrder);
        serviceIntent.putExtra("teat_dip_cup", mTeatTipCup);
        serviceIntent.putExtra("evm_treatment", mEmergencyEvm);
        serviceIntent.putExtra("case_type", mTypesOfCases);
        serviceIntent.putExtra("identified_farmer_count", mFamilyFarmDev);
        serviceIntent.putExtra("farmer_enrolled", mNoOfFarmersEnrolled);
        serviceIntent.putExtra("farmer_inducted", mNoOfFarmersInducted);
        serviceIntent.putExtra("active_flag", mActiveFlag);
        serviceIntent.putExtra("upload_service_id", "4");
        ContextCompat.startForegroundService(this, serviceIntent);

        finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs() {
        mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
        mPlant = binding.edPlant.getText().toString().trim();
        mCenterName = binding.edCenterNameVisit.getText().toString().trim();
        mFarmerName = binding.edFarmerCode.getText().toString().trim();
        mTypeOfService = binding.spinnerTypeOfService.getSelectedItem().toString();
        mTypeOfProduct = binding.spinnerTypeOfProduct.getSelectedItem().toString();
        mSeedSales = binding.edSeedSale.getText().toString().trim();
        mMinaralMixture = binding.edMineralMixture.getText().toString().trim();
        mFodderSales = binding.edFodderSettsSale.getText().toString().trim();
        mCattleOrder = binding.edCattleFeedOrder.getText().toString().trim();
        mTeatTipCup = binding.edTeatDipCup.getText().toString().trim();
        mEmergencyEvm = binding.spinnerEmerEvm.getSelectedItem().toString();
        mTypesOfCases = binding.spinnerTypeOfCases.getSelectedItem().toString();
        mFamilyFarmDev = binding.edFamilyFarmDev.getText().toString().trim();
        mNoOfFarmersEnrolled = binding.edNoOfFarmerEnrolled.getText().toString().trim();
        mNoOfFarmersInducted = binding.edNoOfFarmerInducted.getText().toString().trim();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        File fileTypeOfSer = new File(getExternalFilesDir(null), "/procurement/" + "VET_TOS_123.jpg");
        bitmapTypeOfSer = BitmapFactory.decodeFile(fileTypeOfSer.getAbsolutePath());

        File fileEVM = new File(getExternalFilesDir(null), "/procurement/" + "VET_EVM_123.jpg");
        bitmapEVM = BitmapFactory.decodeFile(fileEVM.getAbsolutePath());

        if (bitmapTypeOfSer != null){
            binding.imageViewTypeOfServiceLayout.setVisibility(View.VISIBLE);
            binding.imageTypeOfService.setImageBitmap(bitmapTypeOfSer);
            binding.txtImgTypeOfServiceNotValid.setVisibility(View.GONE);
            binding.txtErrorFound.setVisibility(View.GONE);
        }

        if (bitmapEVM != null){
            binding.imageViewEmerEvmLayout.setVisibility(View.VISIBLE);
            binding.imageEmerEvm.setImageBitmap(bitmapEVM);
            binding.txtImgEmerEvmNotValid.setVisibility(View.GONE);
            binding.txtErrorFound.setVisibility(View.GONE);
        }
    }
}