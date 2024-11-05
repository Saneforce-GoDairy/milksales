package com.saneforce.godairy.procurement;

import static android.view.View.GONE;
import static com.saneforce.godairy.Interface.ApiClient.BASE_URL;
import static com.saneforce.godairy.procurement.AppConstants.MAS_GET_DISTRICTS;
import static com.saneforce.godairy.procurement.AppConstants.MAS_GET_STATES;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.Procurement;
import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityAgentUpdateBinding;
import com.saneforce.godairy.procurement.adapter.SelectionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentUpdateActivity extends AppCompatActivity implements SelectionAdapter.OnClickInterface{
    private ActivityAgentUpdateBinding binding;
    private final Context context = this;
    private static final String TAG = "agent_update_";
    private List<Procurement> selectionsLists;
    private Bitmap bitmapAgentPhoto;
    private String mSelectedName;
    private String mSelectedCode;
    private int mSelect = 0;
    private String mStateName, mDistrictName, mTown, mCollectionCenter, mAgentGategory, mCompany, mAddress, mPincode, mCity;
    private String mMobileNo, mEmailId, mIncentiveAmt, mCartageAmt, mAgentName;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deletePreviousData();
      //  deleteCache(context);
        createDirectory();
        binding = ActivityAgentUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        binding.edState.setFocusable(false);
        binding.edDistrict.setFocusable(false);

        selectionsLists = new ArrayList<>();

        onClick();
        loadFormEdit();
        loadCollectionCenter();
        initSpinner();
    }

    private void  createDirectory() {
        File dir = getExternalFilesDir("/procurement");
        if(!dir.exists()) {
            if (!dir.mkdir()) {
                Toast.makeText(getApplicationContext(), "The folder " + dir.getPath() + "was not created", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private boolean validateInputs() {
        mAgentName = binding.edAgentName.getText().toString();
        mStateName = binding.edState.getText().toString();
        mDistrictName = binding.edDistrict.getText().toString();
        mTown = binding.edTown.getText().toString();
        mCollectionCenter = binding.spinnerCollectionCe.getSelectedItem().toString();

        mAgentGategory = binding.spinnerAgentGate.getSelectedItem().toString();
        mCompany = binding.edCompany.getText().toString();
        mAddress = binding.edAddress.getText().toString();
        mPincode = binding.edPinCode.getText().toString();

        mCity = binding.edCity.getText().toString();
        mMobileNo = binding.edMobileNo.getText().toString();
        mEmailId = binding.edEmailId.getText().toString();
        mIncentiveAmt = binding.edIncen.getText().toString();
        mCartageAmt = binding.edCartage.getText().toString();

        if ("".equals(mAgentName)) {
            binding.edAgentName.setError("Empty field");
            binding.edAgentName.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapAgentPhoto == null) {
            binding.txtAgentImageNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mStateName)) {
            binding.edState.setError("Empty field");
            binding.edState.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mDistrictName)) {
            binding.edDistrict.setError("Empty field");
            binding.edDistrict.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mTown)) {
            binding.edTown.setError("Empty field");
            binding.edTown.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mCollectionCenter)) {
            ((TextView) binding.spinnerCollectionCe.getSelectedView()).setError("Select collection center");
            binding.spinnerCollectionCe.getSelectedView().requestFocus();
            binding.txtCollNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mAgentGategory)) {
            ((TextView) binding.spinnerAgentGate.getSelectedView()).setError("Select Agent Gategory");
            binding.spinnerAgentGate.getSelectedView().requestFocus();
            binding.txtAgentNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCompany)) {
            binding.edCompany.setError("Empty field");
            binding.edCompany.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mAddress)) {
            binding.edAddress.setError("Empty field");
            binding.edAddress.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mPincode)) {
            binding.edPinCode.setError("Empty field");
            binding.edPinCode.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCity)) {
            binding.edCity.setError("Empty field");
            binding.edCity.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mMobileNo)){
            binding.edMobileNo.setError("Empty field");
            binding.edMobileNo.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mEmailId)) {
            binding.edEmailId.setError("Empty field");
            binding.edEmailId.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mIncentiveAmt)) {
            binding.edIncen.setError("Empty field");
            binding.edIncen.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCartageAmt)) {
            binding.edCartage.setError("Empty field");
            binding.edCartage.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private void onClick() {
        binding.update.setOnClickListener(v -> {
            if (validateInputs()) {
                updateNow();
            }
        });

        binding.edState.setOnClickListener(v -> {
            binding.title.setText("Select State");
            loadStates();
            binding.scrollView1.setVisibility(View.GONE);
            binding.selectionCon.setVisibility(View.VISIBLE);
            binding.shimmerLayout.setVisibility(View.VISIBLE);
            mSelect = 1;
        });

        binding.edDistrict.setOnClickListener(v -> {
            binding.title.setText("Select District");
            loadDistricts(mSelectedCode);
            binding.scrollView1.setVisibility(View.GONE);
            binding.selectionCon.setVisibility(View.VISIBLE);
            binding.shimmerLayout.setVisibility(View.VISIBLE);
            mSelect = 1;

            String mState = binding.edState.getText().toString();

            if (mState.isEmpty()){
                binding.shimmerLayout.setVisibility(View.GONE);
                binding.stateNotSelectError.setVisibility(View.VISIBLE);
            }
        });

        binding.cameraAgent.setOnClickListener(view -> {
            binding.txtAgentImageNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Agent Photo");
            intent.putExtra("camera_id", "23");
            startActivity(intent);
        });

        binding.imageViewAgentLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "AGENT_CREAT_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Agent Photo");
            startActivity(intent);
        });

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.edState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtErrorFound.setVisibility(View.GONE);
                binding.edState.setError(null);
                if (mSelectedCode != null && !mSelectedCode.isEmpty()){
                    loadDistricts(mSelectedCode);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.edDistrict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.edDistrict.setError(null);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edTown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.edTown.setError(null);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.edCompany.setError(null);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.spinnerCollectionCe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.txtCollNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerAgentGate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.txtAgentNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
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
            public void afterTextChanged(Editable s) {

            }
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
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edEmailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edIncen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edCartage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updateNow() {
        String mActiveFlag = "1";
        String mId = getIntent().getStringExtra("id");
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("id", mId);
        serviceIntent.putExtra("agent_name", mAgentName);
        serviceIntent.putExtra("state", mStateName);
        serviceIntent.putExtra("district", mDistrictName);
        serviceIntent.putExtra("town", mTown);
        serviceIntent.putExtra("coll_center", mCollectionCenter);
        serviceIntent.putExtra("ag_category", mAgentGategory);
        serviceIntent.putExtra("company", mCompany);
        serviceIntent.putExtra("addr", mAddress);
        serviceIntent.putExtra("pin_code", mPincode);
        serviceIntent.putExtra("city", mCity);
        serviceIntent.putExtra("mobile_no", mMobileNo);
        serviceIntent.putExtra("email", mEmailId);
        serviceIntent.putExtra("incentive_amt", mIncentiveAmt);
        serviceIntent.putExtra("cartage_amt", mCartageAmt);
        serviceIntent.putExtra("active_flag", mActiveFlag);
        serviceIntent.putExtra("upload_service_id", "17");
        ContextCompat.startForegroundService(this, serviceIntent);

      //  finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
    }

    private void loadFormEdit() {
        String agentName = getIntent().getStringExtra("agent_name");
        String agent_photo = getIntent().getStringExtra("agent_photo");
        String state = getIntent().getStringExtra("state");
        String district = getIntent().getStringExtra("district");
        String town = getIntent().getStringExtra("town");
        String coll_center = getIntent().getStringExtra("coll_center");
        String agent_category = getIntent().getStringExtra("agent_category");
        String company = getIntent().getStringExtra("company");
        String addr = getIntent().getStringExtra("address");
        String pin_code = getIntent().getStringExtra("pin_code");
        String city = getIntent().getStringExtra("city");
        String mobile_no = getIntent().getStringExtra("mobile_no");
        String email = getIntent().getStringExtra("email");
        String incentive_amt = getIntent().getStringExtra("incentive_amt");
        String cartage_amt = getIntent().getStringExtra("cartage_amt");

        binding.edAgentName.setText(agentName);

        if (agent_photo != null) {
            binding.imageViewAgentLayout.setVisibility(View.VISIBLE);
            String baseURL = getBaseUrl() + agent_photo;
            File file = new File(getExternalFilesDir(null), "/procurement/" + "AGENT_CREAT_123.jpg");

          Thread thread = new Thread(new Runnable() {
              @Override
              public void run() {
                  downloadFile(baseURL, file, context);
              }
          });
          thread.start();

            Log.e(TAG, "Perfect url :" + baseURL);
        }else {
            Log.e(TAG, "not valid image url");
            binding.imageViewAgentLayout.setVisibility(View.VISIBLE);
            binding.imageAgent.setImageResource(R.drawable.error_image1);
        }

        if (state != null){
            binding.edState.setText(state);
        }

        if (district != null){
            binding.edDistrict.setText(district);
        }

        if (town != null){
            binding.edTown.setText(town);
        }

        /*
        if (coll_center != null){
            binding.edCollCenter.setVisibility(View.VISIBLE);
            binding.edCollCenter.setFocusable(false);
            binding.edCollCenter.setText(coll_center);
        }
        if (agent_category != null){
            binding.edAgentGate.setVisibility(View.VISIBLE);
            binding.edAgentGate.setFocusable(false);
            binding.edAgentGate.setText(agent_category);
        }
         */

        if (company != null){
            binding.edCompany.setText(company);
        }

        if (addr != null){
            binding.edAddress.setText(addr);
        }

        if (pin_code != null){
            binding.edPinCode.setText(pin_code);
        }

        if (city != null){
            binding.edCity.setText(city);
        }

        if (mobile_no != null){
            binding.edMobileNo.setText(mobile_no);
        }

        if (email != null){
            binding.edEmailId.setText(email);
        }

        if (incentive_amt != null){
            binding.edIncen.setText(incentive_amt);
        }

        if (cartage_amt != null){
            binding.edCartage.setText(cartage_amt);
        }
    }

    private String getBaseUrl() {
        URL url = null;
        try {
            url = new URL(BASE_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return url.getProtocol() + "://" + url.getHost() + "/" + "Procurement/Proc_Photos/";
    }

    void downloadFile(String url, File outputFile, Context context) {
        Thread gfgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                        URL u = new URL(url);
                        Log.e(TAG, u.toString());
                        URLConnection conn = u.openConnection();
                        int contentLength = conn.getContentLength();

                        DataInputStream stream = new DataInputStream(u.openStream());

                        byte[] buffer = new byte[contentLength];
                        stream.readFully(buffer);
                        stream.close();

                        DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
                        fos.write(buffer);
                        fos.flush();
                        fos.close();

                        runOnUiThread(() -> {
                            loadAgentImage();
//                            Toast.makeText(context, "Download success", Toast.LENGTH_SHORT).show();
                        });

                } catch (IOException e) {
                   runOnUiThread(() -> {
                       binding.imageLoadingProgress.setVisibility(GONE);
                       binding.imageAgent.setImageResource(R.drawable.error_image1);
                   });
                }
            }
        });

        gfgThread.start();
    }

    private void loadAgentImage() {
        File file = new File(getExternalFilesDir(null), "/procurement/" + "AGENT_CREAT_123.jpg");
        bitmapAgentPhoto = BitmapFactory.decodeFile(file.getAbsolutePath());

        if (bitmapAgentPhoto != null){
            binding.imageViewAgentLayout.setVisibility(View.VISIBLE);
            binding.imageAgent.setImageBitmap(bitmapAgentPhoto);
            binding.txtAgentImageNotValid.setVisibility(GONE);
            binding.txtErrorFound.setVisibility(GONE);
            binding.imageLoadingProgress.setVisibility(GONE);
        }
    }

    private void loadStates() {
        if (selectionsLists != null && !selectionsLists.isEmpty()){
            selectionsLists.clear();
        }

        Call<ResponseBody> call =
                apiInterface.getStates(MAS_GET_STATES);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    binding.shimmerLayout.setVisibility(View.GONE);
                    binding.selectionCon.setVisibility(View.VISIBLE);
                    String stateList = "";

                    try {
                        stateList = response.body().string();
                        JSONObject jsonObject = new JSONObject(stateList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords){
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                Procurement procurement = new Procurement();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                procurement.setSelectionCode(object.getString("State_Code"));
                                procurement.setSelectionName(object.getString("StateName"));
                                selectionsLists.add(procurement);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            SelectionAdapter selectionAdapter = new SelectionAdapter(0,selectionsLists, context);
                            binding.recyclerView.setAdapter(selectionAdapter);
                            selectionAdapter.notifyDataSetChanged();
                        }
                    } catch (IOException | JSONException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDistricts(String mSelectedStateCode) {
        if (selectionsLists != null && !selectionsLists.isEmpty()){
            selectionsLists.clear();
        }

        Call<ResponseBody> call =
                apiInterface.getDistricts(MAS_GET_DISTRICTS, mSelectedStateCode);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    binding.shimmerLayout.setVisibility(View.GONE);
                    binding.selectionCon.setVisibility(View.VISIBLE);
                    String districtList = "";
                    try {
                        districtList = response.body().string();
                        JSONObject jsonObject = new JSONObject(districtList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords){
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                Procurement procurement = new Procurement();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                procurement.setSelectionCode(object.getString("Dist_code"));
                                procurement.setSelectionName(object.getString("Dist_name"));
                                selectionsLists.add(procurement);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            SelectionAdapter selectionAdapter = new SelectionAdapter(1, selectionsLists, context);
                            binding.recyclerView.setAdapter(selectionAdapter);
                            selectionAdapter.notifyDataSetChanged();
                        }
                    } catch (IOException | JSONException e) {
                        // throw new RuntimeException(e);
                        binding.shimmerLayout.setVisibility(View.GONE);
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onClickInterface(Intent intent) {
        String requestId = intent.getStringExtra("request_id");

        int mRequestId = Integer.parseInt(requestId);

        if (mRequestId == 0) {
            mSelectedName = intent.getStringExtra("selection_name");
            mSelectedCode = intent.getStringExtra("selection_code");
            binding.edState.setText(mSelectedName);
        }

        if (mRequestId == 1) {
            mSelectedName = intent.getStringExtra("selection_name");
            binding.edDistrict.setText(mSelectedName);
        }

        if (mSelect == 1){
            binding.scrollView1.setVisibility(View.VISIBLE);
            binding.title.setText("Agent creation");
            binding.selectionCon.setVisibility(View.GONE);
            mSelect = 0;
        }
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.agent_type, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAgentGate.setAdapter(adapter);
    }

    private void loadCollectionCenter() {
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
                            String plantName = object.optString("title");

                            binding.spinnerCollectionCe.setPrompt(plantName);
                            list.add(plantName);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinnerCollectionCe.setAdapter(adapter);
                    } catch (IOException | JSONException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Collection Center list load error!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePreviousData() {
        File file = new File(getExternalFilesDir(null), "/procurement/" + "AGENT_CREAT_123.jpg");
        if (file.exists()) {
            if (file.delete()) {
                Log.d(TAG, "File deleted successfully!");

            } else {
                System.out.println("File not Deleted" + file.getAbsoluteFile());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        File file = new File(getExternalFilesDir(null), "/procurement/" + "AGENT_CREAT_123.jpg");
        bitmapAgentPhoto = BitmapFactory.decodeFile(file.getAbsolutePath());

        if (bitmapAgentPhoto != null){
            binding.imageViewAgentLayout.setVisibility(View.VISIBLE);
            binding.imageAgent.setImageBitmap(bitmapAgentPhoto);
            binding.txtAgentImageNotValid.setVisibility(View.GONE);
            binding.txtErrorFound.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.scrollView1.getVisibility() == View.GONE){
            binding.scrollView1.setVisibility(View.VISIBLE);
            binding.selectionCon.setVisibility(View.GONE);
            binding.shimmerLayout.setVisibility(View.GONE);
            binding.title.setText("Agent Update");
            mSelect = 0;
            return;
        }
        finish();
    }
}