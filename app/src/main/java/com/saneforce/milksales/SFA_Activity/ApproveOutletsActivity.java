package com.saneforce.milksales.SFA_Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.Approve_Outlets_Adapter;
import com.saneforce.milksales.SFA_Model_Class.ModelApproveOutlets;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Created by RAGU on 27/01/2023
public class ApproveOutletsActivity extends AppCompatActivity {
    ImageView home;
    TextView outletsCount;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    String sfCode, stockistCode;

    ArrayList<ModelApproveOutlets> list = new ArrayList<>();
    Approve_Outlets_Adapter adapter;

    Context context = this;
    Common_Class common_class;

    public static boolean refresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_outlets);

        home = findViewById(R.id.toolbar_home_ApproveOutletsActivity);
        outletsCount = findViewById(R.id.count_ApproveOutletsActivity);
        recyclerView = findViewById(R.id.recyclerview_ApproveOutletsActivity);
        progressBar = findViewById(R.id.progressbar_ApproveOutletsActivity);

        sfCode = getIntent().getStringExtra("sfCode");
        stockistCode = getIntent().getStringExtra("stockistCode");

        common_class = new Common_Class(context);
        home.setOnClickListener(v -> {
            common_class.gotoHomeScreen(context, v);
        });

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(this::StartLoading);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refresh) {
            PendingOutletsCategory.refresh = true;
            StartLoading();
            refresh = false;
        }
    }

    private void StartLoading() {
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        Map<String, String> params = new HashMap<>();
        params.put("sfCode", sfCode);
        params.put("stockistCode", stockistCode);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getPendingOutlets("gets_pendings_outlets", params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("success")) {
                            JSONArray array = object.getJSONArray("response");
                            for (int i = 0; i < array.length(); i++) {
                                String customerName = array.getJSONObject(i).getString("OutletName");
                                String customerID = array.getJSONObject(i).getString("OutletCode");
                                String customerMobile = array.getJSONObject(i).getString("OutletMobile");
                                String customerAddress = array.getJSONObject(i).getString("OutletAddress");
                                String listedDrCode = array.getJSONObject(i).getString("ListedDrCode");
                                list.add(new ModelApproveOutlets(customerName, customerID, customerMobile, customerAddress, listedDrCode));
                            }
                            runOnUiThread(() -> {
                                String c = "No. of Outlets\n" + list.size();
                                outletsCount.setText(c);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                adapter = new Approve_Outlets_Adapter(list, context);
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                outletsCount.setVisibility(View.VISIBLE);

                                adapter.setApproveClicked((model, message, flagType, position) -> {
                                    UpdateData(model, message, flagType, position); // 0 means approve, 1 means reject, 2 means pending
                                });

                                adapter.setRejectClicked((model, message, flagType, position) -> {
                                    UpdateData(model, message, flagType, position); // 0 means approve, 1 means reject, 2 means pending
                                });

                            });
                        } else {
                            Toast.makeText(context, "List is empty", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            outletsCount.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            outletsCount.setVisibility(View.GONE);
                            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        outletsCount.setVisibility(View.GONE);
                        Toast.makeText(context, "Error: Response Failed", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    outletsCount.setVisibility(View.GONE);
                    Toast.makeText(context, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


    private void UpdateData(ModelApproveOutlets model, String message, String type, int position) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String listedDrCode = model.getListedDrCode();
        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(context);
        String sfCode = shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code, "");
        Map<String, String> params = new HashMap<>();
        params.put("type", type); // 0 means approve, 1 means reject, 2 means pending
        params.put("listedDrCode", listedDrCode);
        params.put("ModifiedBy", sfCode);
        params.put("remarks", message);
        params.put("ModifiedOn", new Common_Class(context).GetDatemonthyearTimeformat());

        Log.e("status", params.toString());

        Call<ResponseBody> call = apiInterface.setOutletStatus("set_outlet_status", params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject js = new JSONObject(response.body().string());
                        if (js.getBoolean("success")) {
                            PendingOutletsCategory.refresh = true;
                            list.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(0, list.size());
                            if (type.equals("0")) {
                                Toast.makeText(context, "Outlet Approved Successfully", Toast.LENGTH_SHORT).show();
                            } else if (type.equals("1")) {
                                Toast.makeText(context, "Outlet Rejected Successfully", Toast.LENGTH_SHORT).show();
                            }
                            String c = "No. of Outlets\n" + list.size();
                            outletsCount.setText(c);
                            if (list.isEmpty()) {
                                Toast.makeText(context, "List is empty", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Error: Response not successfull", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}