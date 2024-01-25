package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_EXISTING_AGENT_REPORT;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_QUALITY_REPORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcExistingAgentReport;
import com.saneforce.godairy.Model_Class.ProcQualityReport;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityExistingAgentVisitReportBinding;
import com.saneforce.godairy.databinding.ModelExistingReportBinding;
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

public class ExistingAgentVisitReportActivity extends AppCompatActivity {
    private ActivityExistingAgentVisitReportBinding binding;
    private final Context context = this;
    private List<ProcExistingAgentReport> existingAgentReportList;
    private ExistingAgentReportAdapter existingAgentReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExistingAgentVisitReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        existingAgentReportList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getExistingAgentReport(PROCUREMENT_GET_EXISTING_AGENT_REPORT);

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout2.setVisibility(GONE);
                    String existingAgentList;
                    try {
                        existingAgentList = response.body().string();
                        JSONArray jsonArray = new JSONArray(existingAgentList);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ProcExistingAgentReport existingAgentReport = new ProcExistingAgentReport();
                            JSONObject object = jsonArray.getJSONObject(i);
                            existingAgentReport.setCompany(object.getString("company"));
                            existingAgentReport.setAgent(object.getString("visit_agent"));
                            existingAgentReport.setTotal_milk_availability(object.getString("total_milk_available"));
                            existingAgentReport.setOur_company_ltrs(object.getString("our_company_ltrs"));
                            existingAgentReport.setCompetitor_rate(object.getString("competitor_rate"));
                            existingAgentReport.setOur_company_rate(object.getString("our_company_rate"));
                            existingAgentReport.setDemand(object.getString("demand"));
                            existingAgentReport.setSupply_start_dt(object.getString("supply_start_dt"));
                            existingAgentReport.setCreated_dt(object.getString("created_dt"));

                            existingAgentReportList.add(existingAgentReport);
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        binding.recyclerView.setLayoutManager(linearLayoutManager);
                        binding.recyclerView.setHasFixedSize(true);
                        binding.recyclerView.setItemViewCacheSize(20);
                        existingAgentReportAdapter = new ExistingAgentReportAdapter(context, existingAgentReportList);
                        binding.recyclerView.setAdapter(existingAgentReportAdapter);
                        existingAgentReportAdapter.notifyDataSetChanged();
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

    public static class ExistingAgentReportAdapter extends RecyclerView.Adapter<ExistingAgentReportAdapter.ViewHolder>{
        private final List<ProcExistingAgentReport> existingAgentReportList;
        private  final Context context;

        public ExistingAgentReportAdapter(Context context , List<ProcExistingAgentReport> existingAgentReportList) {
            this.existingAgentReportList = existingAgentReportList;
            this.context = context;
        }

        @NonNull
        @Override
        public ExistingAgentReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ModelExistingReportBinding binding = ModelExistingReportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ExistingAgentReportAdapter.ViewHolder holder, int position) {
            holder.binding.txtCompanyName.setText(existingAgentReportList.get(position).getCompany());
            String upToNCharacters = existingAgentReportList.get(position).getCreated_dt().substring(0, Math.min(existingAgentReportList.get(position).getCreated_dt().length(), 10));
            holder.binding.txtDate.setText(upToNCharacters);
            holder.binding.txtAgentVisitType.setText(existingAgentReportList.get(position).getAgent());
            holder.binding.txtTotalMilkAvail.setText(existingAgentReportList.get(position).getTotal_milk_availability());
            holder.binding.txtOurCompanyLtrs.setText(existingAgentReportList.get(position).getOur_company_ltrs());
            holder.binding.txtCompetitorRate.setText(existingAgentReportList.get(position).getCompetitor_rate());
            holder.binding.txtOurCompanyRate.setText(existingAgentReportList.get(position).getOur_company_rate());
            holder.binding.txtDemand.setText(existingAgentReportList.get(position).getDemand());
            holder.binding.txtSupplyStartDt.setText(existingAgentReportList.get(position).getSupply_start_dt());

            holder.binding.txtViewDetails.setOnClickListener(v -> {
                holder.binding.secondCn.setVisibility(View.VISIBLE);
                holder.binding.txtViewDetails.setVisibility(GONE);
                holder.binding.txtViewLess.setVisibility(View.VISIBLE);
            });

            holder.binding.txtViewLess.setOnClickListener(v -> {
                holder.binding.secondCn.setVisibility(View.GONE);
                holder.binding.txtViewDetails.setVisibility(View.VISIBLE);
                holder.binding.txtViewLess.setVisibility(View.GONE);
            });

        }

        @Override
        public int getItemCount() {
            return existingAgentReportList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ModelExistingReportBinding binding;

            public ViewHolder(ModelExistingReportBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}