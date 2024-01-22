package com.saneforce.godairy.procurement.reports;

import static android.view.View.GONE;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_AIT_REPORT;

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
import com.saneforce.godairy.Model_Class.ProcAITReport;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityAitreportBinding;

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

public class AITReportActivity extends AppCompatActivity {
    private ActivityAitreportBinding binding;
    private final Context context = this;
    private List<ProcAITReport> procAITReportList;
    private AITReportListAdapter aitReportListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aitreport);

        procAITReportList = new ArrayList<>();
        loadList();
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getAITReportList(PROCUREMENT_GET_AIT_REPORT);

        call.enqueue(new Callback<>() {
            @Override
            @SuppressLint("NotifyDataSetChanged")
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    binding.shimmerLayout.setVisibility(GONE);
                    String  aitList;

                    try {
                        aitList = response.body().string();
                        JSONArray jsonArray = new JSONArray(aitList);

                        for (int i = 0; i<jsonArray.length(); i++) {
                            ProcAITReport procAITReport = new ProcAITReport();
                            JSONObject object = jsonArray.getJSONObject(i);
                            procAITReport.setId(object.getString("id"));

                            procAITReportList.add(procAITReport);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        binding.recyclerView.setLayoutManager(linearLayoutManager);
                        binding.recyclerView.setHasFixedSize(true);
                        binding.recyclerView.setItemViewCacheSize(20);
                        aitReportListAdapter = new AITReportListAdapter(context, procAITReportList);
                        binding.recyclerView.setAdapter(aitReportListAdapter);
                        aitReportListAdapter.notifyDataSetChanged();
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class AITReportListAdapter extends RecyclerView.Adapter<AITReportListAdapter.ViewHolder>{
        private final List<ProcAITReport> aitReportList;
        private final Context context;

        public AITReportListAdapter(Context context, List<ProcAITReport> aitReportList) {
            this.aitReportList = aitReportList;
            this.context = context;
        }


        @NonNull
        @Override
        public AITReportListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_ait_report, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AITReportListAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return aitReportList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}