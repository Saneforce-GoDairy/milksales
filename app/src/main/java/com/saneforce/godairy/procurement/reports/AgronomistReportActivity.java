package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.Interface.ApiClient.BASE_URL;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_AGRONOMIST;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcAgronoListModel;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityAgronomistReportBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;
import com.saneforce.godairy.procurement.adapter.AgronomistListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgronomistReportActivity extends AppCompatActivity {
    private ActivityAgronomistReportBinding binding;
    private final Context context = this;
    private List<ProcAgronoListModel> agronomistListsMain;
    private AgronomistListAdapter primaryNoOrderListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgronomistReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        agronomistListsMain = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getAgronomistReport(PROCUREMENT_GET_AGRONOMIST);
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(GONE);
                    String  agronomistList;
                    try {
                        agronomistList = response.body().string();
                        JSONArray jsonArray = new JSONArray(agronomistList);
                        for (int i = 0; i<jsonArray.length(); i++) {
                            ProcAgronoListModel agronomistListModel = new ProcAgronoListModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            agronomistListModel.setCreated_dt(object.getString("created_dt"));
                            agronomistListModel.setCompany(object.getString("company"));
                            agronomistListModel.setFarmer_name(object.getString("farmer_name"));
                            agronomistListModel.setCenter_name(object.getString("center_name"));
                            agronomistListModel.setService_type(object.getString("service_type"));
                            agronomistListModel.setProduct_type(object.getString("product_type"));
                            agronomistListModel.setFarmers_meeting_img(object.getString("farmers_meeting_image"));
                            agronomistListModel.setCsr_img(object.getString("csr_image"));
                            agronomistListModel.setFodder_acres_img(object.getString("fodder_dev_acres_image"));
                            agronomistListModel.setPlant_name(object.getString("plant"));
                            agronomistListModel.setTeat_dip(object.getString("teat_dip"));
                            agronomistListModel.setFodder_dev(object.getString("fodder_dev_acres"));
                            agronomistListModel.setFarmers_enrolled(object.getString("farmers_enrolled"));
                            agronomistListsMain.add(agronomistListModel);
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        binding.agronomistRecyclerView.setLayoutManager(linearLayoutManager);
                        binding.agronomistRecyclerView.setHasFixedSize(true);
                        binding.agronomistRecyclerView.setItemViewCacheSize(20);
                        primaryNoOrderListAdapter = new AgronomistListAdapter(context, agronomistListsMain);
                        binding.agronomistRecyclerView.setAdapter(primaryNoOrderListAdapter);
                        primaryNoOrderListAdapter.notifyDataSetChanged();
                    } catch (IOException | JSONException e) {
                       // throw new RuntimeException(e);
                        Toast.makeText(context, "List load error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}