package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_COLLECTION_CENTER_REPORT;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcCollectionCeReport;
import com.saneforce.godairy.databinding.ActivityCollectionCenterReportBinding;
import com.saneforce.godairy.procurement.adapter.CollectionCeReportAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionCenterReportActivity extends AppCompatActivity {
    private ActivityCollectionCenterReportBinding binding;
    private final Context context = this;
    private List<ProcCollectionCeReport> collectionCeReportList;
    private CollectionCeReportAdapter collectionCeReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionCenterReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        collectionCeReportList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getCollectionCenterReport(PROCUREMENT_GET_COLLECTION_CENTER_REPORT);

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(GONE);
                    String collectionCenterList;
                    try {
                        collectionCenterList = response.body().string();

                        if (collectionCenterList.isEmpty()){
                            showError();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(collectionCenterList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                ProcCollectionCeReport collectionCeReport = new ProcCollectionCeReport();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                collectionCeReport.setCompany(object.getString("company"));
                                collectionCeReport.setPlant(object.getString("plant"));
                                collectionCeReport.setCreated_dt(object.getString("created_dt"));
                                collectionCeReport.setCollection_ce_image(object.getString("coll_center_image"));
                                collectionCeReport.setSap_center_code(object.getString("sap_center_code"));
                                collectionCeReport.setSap_center_name(object.getString("sap_center_name"));
                                collectionCeReport.setCenter_addr(object.getString("center_address"));
                                collectionCeReport.setLocatlis_lpd(object.getString("lactlis_potential_lpd"));
                                collectionCeReport.setFarmers_enrolled(object.getString("no_enrolled_farmers"));
                                collectionCeReport.setCompetitor_lpd(object.getString("competitor1"));
                                collectionCeReport.setCompetitor_lpd2(object.getString("competitor1_txt"));
                                collectionCeReportList.add(collectionCeReport);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            collectionCeReportAdapter = new CollectionCeReportAdapter(context, collectionCeReportList);
                            binding.recyclerView.setAdapter(collectionCeReportAdapter);
                            collectionCeReportAdapter.notifyDataSetChanged();
                            return;
                        }
                        binding.shimmerLayout.setVisibility(GONE);
                        binding.recyclerView.setVisibility(GONE);
                        binding.noRecords.setVisibility(View.VISIBLE);
                    } catch (IOException | JSONException e) {
                        //  throw new RuntimeException(e);
                        showError();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                showError();
            }
        });
    }

    private void showError() {
        binding.shimmerLayout.setVisibility(GONE);
        binding.recyclerView.setVisibility(GONE);
        binding.nullError.setVisibility(View.VISIBLE);
        binding.message.setText("Something went wrong!");
    }
}