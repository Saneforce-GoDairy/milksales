package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_PLANT;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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
    Toolbar mToolbar;
    ArrayAdapter<String> mAdapter;
    ListView mListView;
    TextView mEmptyView;
    private String mCompany, mPlant, mNoOfEquipment, mTypeOfRepair, mOthersText;
    private final Context context = this;
    private Bitmap bitmapRepair, bitmapBoardIssue, bitmapSmbs, bitmapMotor, bitmapWeighScals;
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
                R.array.proc_type_of_repair, R.layout.custom_spinner);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfRepair.setAdapter(adapter3);
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
        binding.cameraSensor.setOnClickListener(view -> {
            binding.txtImgSensorNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Maintenance repair");
            intent.putExtra("camera_id", "14");
            startActivity(intent);
        });

        binding.imageViewSensorLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "MAIN_RE_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Maintenance repair");
            startActivity(intent);
        });

        binding.cameraBoardIssue.setOnClickListener(v -> {
            binding.txtImgBoardIssueNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Board issue");
            intent.putExtra("camera_id", "17");
            startActivity(intent);
        });

        binding.imageViewBoardIssueLayout.setOnClickListener(v -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "MAIN_IS_BOARD_IS.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Board Issue");
            startActivity(intent);
        });

        binding.cameraSmbs.setOnClickListener(v -> {
            binding.txtImgSmbsNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "SMBS Issue");
            intent.putExtra("camera_id", "18");
            startActivity(intent);
        });

        binding.imageViewSmbsLayout.setOnClickListener(v -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "MAIN_SMBS_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "SMBS Issue");
            startActivity(intent);
        });

        binding.cameraMotor.setOnClickListener(v -> {
            binding.txtImgMotorNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Motor Issue");
            intent.putExtra("camera_id", "19");
            startActivity(intent);
        });

        binding.imageViewMotorLayout.setOnClickListener(v -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "MAIN_MOTOR_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Motor Issue");
            startActivity(intent);
        });

        binding.cameraWeighScales.setOnClickListener(v -> {
            binding.txtImgWeighScalesNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Weighning scales");
            intent.putExtra("camera_id", "20");
            startActivity(intent);
        });

        binding.imageViewWeighScalesLayout.setOnClickListener(v -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "MAIN_WEIGH_SC_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Weighning scales");
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

        binding.spinnerTypeOfRepair.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              switch (i){
                  case 0:
                      binding.sensorContainer.setVisibility(View.GONE);
                      binding.boardContainer.setVisibility(View.GONE);
                      binding.smbsContainer.setVisibility(View.GONE);
                      binding.motorContainer.setVisibility(View.GONE);
                      binding.weighningContainer.setVisibility(View.GONE);
                      binding.edOthers.setVisibility(View.GONE);
                      break;

                  case 1:
                      onHide();
                     binding.sensorContainer.setVisibility(View.VISIBLE);
                      break;

                      case 2:
                          onHide();
                          binding.boardContainer.setVisibility(View.VISIBLE);
                      break;

                      case 3:
                          onHide();
                          binding.smbsContainer.setVisibility(View.VISIBLE);
                      break;

                      case 4:
                          onHide();
                          binding.motorContainer.setVisibility(View.VISIBLE);
                      break;

                      case 5:
                          onHide();
                          binding.weighningContainer.setVisibility(View.VISIBLE);
                          break;

                          case 6:
                              onHide();
                              binding.edOthers.setVisibility(View.VISIBLE);
                              break;
              }
            }

            private void onHide() {
                binding.sensorContainer.setVisibility(View.GONE);
                binding.boardContainer.setVisibility(View.GONE);
                binding.smbsContainer.setVisibility(View.GONE);
                binding.motorContainer.setVisibility(View.GONE);
                binding.weighningContainer.setVisibility(View.GONE);
                binding.edOthers.setVisibility(View.GONE);
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
        serviceIntent.putExtra("others", mOthersText);
        serviceIntent.putExtra("active_flag", mActiveFlag);
        serviceIntent.putExtra("upload_service_id", "8");
        ContextCompat.startForegroundService(this, serviceIntent);
        finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs() {
        mCompany = binding.spinnerCompany.getSelectedItem().toString();
        mPlant = binding.edPlant.getText().toString().trim();
        mNoOfEquipment = binding.edNoOfEqp.getText().toString().trim();
        mTypeOfRepair = binding.spinnerTypeOfRepair.getSelectedItem().toString();
        mOthersText = binding.edOthers.getText().toString().trim();

        if ("Select".equals(mCompany)){
            ((TextView)binding.spinnerCompany.getSelectedView()).setError("Select company");
            binding.spinnerCompany.getSelectedView().requestFocus();
            binding.txtCompanyNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mPlant)){
            binding.edPlant.setError("Select Plant");
            binding.edPlant.requestFocus();
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
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        File fileRepair = new File(getExternalFilesDir(null), "/procurement/" + "MAIN_RE_123.jpg");
        bitmapRepair = BitmapFactory.decodeFile(fileRepair.getAbsolutePath());

        File fileBoardIssue = new File(getExternalFilesDir(null), "/procurement/" + "MAIN_IS_BOARD_IS.jpg");
        bitmapBoardIssue = BitmapFactory.decodeFile(fileBoardIssue.getAbsolutePath());

        File fileSmbs = new File(getExternalFilesDir(null), "/procurement/" + "MAIN_SMBS_123.jpg");
        bitmapSmbs = BitmapFactory.decodeFile(fileSmbs.getAbsolutePath());

        File fileMotor = new File(getExternalFilesDir(null), "/procurement/" + "MAIN_MOTOR_123.jpg");
        bitmapMotor = BitmapFactory.decodeFile(fileMotor.getAbsolutePath());

        File fileWeighScals = new File(getExternalFilesDir(null), "/procurement/" + "MAIN_WEIGH_SC_123.jpg");
        bitmapWeighScals = BitmapFactory.decodeFile(fileWeighScals.getAbsolutePath());

        if (bitmapRepair != null){
            binding.imageViewSensorLayout.setVisibility(View.VISIBLE);
            binding.imageSensor.setImageBitmap(bitmapRepair);
            binding.txtImgSensorNotValid.setVisibility(View.GONE);
        }

        if (bitmapBoardIssue != null){
            binding.imageViewBoardIssueLayout.setVisibility(View.VISIBLE);
            binding.imageBoardIssue.setImageBitmap(bitmapBoardIssue);
            binding.txtImgBoardIssueNotValid.setVisibility(View.GONE);
        }

        if (bitmapSmbs != null){
            binding.imageViewSmbsLayout.setVisibility(View.VISIBLE);
            binding.imageSmbs.setImageBitmap(bitmapSmbs);
            binding.txtImgSmbsNotValid.setVisibility(View.GONE);
        }

        if (bitmapMotor != null){
            binding.imageViewMotorLayout.setVisibility(View.VISIBLE);
            binding.imageMotor.setImageBitmap(bitmapMotor);
            binding.txtImgMotorNotValid.setVisibility(View.GONE);
        }

        if (bitmapWeighScals != null){
            binding.imageViewWeighScalesLayout.setVisibility(View.VISIBLE);
            binding.imageWeighScales.setImageBitmap(bitmapWeighScals);
            binding.txtImgWeighScalesNotValid.setVisibility(View.GONE);
        }
    }
}