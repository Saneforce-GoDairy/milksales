package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_ASSET_REPORT;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_QUALITY_REPORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcAssetReport;
import com.saneforce.godairy.Model_Class.ProcQualityReport;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityAssetReportBinding;
import com.saneforce.godairy.databinding.ModelAssetReportBinding;
import com.saneforce.godairy.databinding.ModelQualityReportBinding;

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

public class AssetReportActivity extends AppCompatActivity {
    private ActivityAssetReportBinding binding;
    private Context context = this;
    private List<ProcAssetReport> assetReportList;
    private AssetReportAdapter assetReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssetReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        assetReportList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getAssetReport(PROCUREMENT_GET_ASSET_REPORT);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout2.setVisibility(GONE);
                    String assetList;
                    try {
                        assetList = response.body().string();
                        JSONArray jsonArray = new JSONArray(assetList);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ProcAssetReport assetReport = new ProcAssetReport();
                            JSONObject object = jsonArray.getJSONObject(i);
                            assetReport.setCompany(object.getString("company"));
                            assetReport.setPlant(object.getString("plant"));
                            assetReport.setAsset_type(object.getString("asset_type"));
                            assetReport.setComments(object.getString("comments"));
                            assetReport.setCreated_dt(object.getString("created_dt"));
                            assetReportList.add(assetReport);
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        binding.recyclerView.setLayoutManager(linearLayoutManager);
                        binding.recyclerView.setHasFixedSize(true);
                        binding.recyclerView.setItemViewCacheSize(20);
                        assetReportAdapter = new AssetReportAdapter(context, assetReportList);
                        binding.recyclerView.setAdapter(assetReportAdapter);
                        assetReportAdapter.notifyDataSetChanged();
                    } catch (IOException | JSONException e) {
                        //  throw new RuntimeException(e);
                        Toast.makeText(context, "List load error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class AssetReportAdapter extends RecyclerView.Adapter<AssetReportAdapter.ViewHolder>{
        private final List<ProcAssetReport> assetReportList;
        private  final Context context;

        public AssetReportAdapter(Context context , List<ProcAssetReport> assetReportList) {
            this.assetReportList = assetReportList;
            this.context = context;
        }


        @NonNull
        @Override
        public AssetReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ModelAssetReportBinding binding = ModelAssetReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull AssetReportAdapter.ViewHolder holder, int position) {
            holder.binding.txtCompanyName.setText(assetReportList.get(position).getCompany());
            holder.binding.txtPlant.setText(assetReportList.get(position).getPlant());
            holder.binding.txtAssetType.setText(assetReportList.get(position).getAsset_type());
            holder.binding.txtComments.setText(assetReportList.get(position).getComments());
            String upToNCharacters = assetReportList.get(position).getCreated_dt().substring(0, Math.min(assetReportList.get(position).getCreated_dt().length(), 10));
            holder.binding.txtDate.setText(upToNCharacters);
        }

        @Override
        public int getItemCount() {
            return assetReportList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ModelAssetReportBinding binding;

            public ViewHolder(ModelAssetReportBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}