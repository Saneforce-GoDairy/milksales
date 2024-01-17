package com.saneforce.godairy.procurement.reports;

import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_GET_AGRONOMIST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.AgronomistListModel;
import com.saneforce.godairy.Model_Class.PrimaryNoOrderList;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.TodayPrimOrdActivity;
import com.saneforce.godairy.databinding.ActivityAgronomistReportBinding;

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

public class AgronomistReportActivity extends AppCompatActivity {
    private ActivityAgronomistReportBinding binding;
    private final Context context = this;
    private List<AgronomistListModel> agronomistListsMain;
    private AgronomistListAdapter primaryNoOrderListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgronomistReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        agronomistListsMain = new ArrayList<>();
        loadList();
    }

    public static class AgronomistListAdapter extends  RecyclerView.Adapter<AgronomistListAdapter.ViewHolder>{
        private List<AgronomistListModel> agronomistListModel;
        private Context context;

        public AgronomistListAdapter(Context context, List<AgronomistListModel> agronomistListModel) {
            this.context = context;
            this.agronomistListModel = agronomistListModel;
        }

        @NonNull
        @Override
        public AgronomistListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proc_agronomist_report, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AgronomistListAdapter.ViewHolder holder, int position) {
            holder.txtCompanyName.setText(agronomistListModel.get(position).getCompany());
            holder.txtFarmerName.setText(agronomistListModel.get(position).getFarmer_name());
            String upToNCharacters = agronomistListModel.get(position).getCreated_dt().substring(0, Math.min(agronomistListModel.get(position).getCreated_dt().length(), 10));
            holder.txtDate.setText(upToNCharacters);
            holder.txtCenterName.setText(agronomistListModel.get(position).getCenter_name());
            holder.txtServeiceType.setText(agronomistListModel.get(position).getService_type());
            holder.txtProductType.setText(agronomistListModel.get(position).getProduct_type());
        }

        @Override
        public long getItemId(int position){
            return position;
        }
        @Override
        public int getItemViewType(int position){
            return position;
        }

        @Override
        public int getItemCount() {
            return agronomistListModel.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtCompanyName, txtFarmerName, txtDate, txtCenterName, txtServeiceType, txtProductType;
            CardView layout;

            public ViewHolder(View view) {
                super(view);
                txtCompanyName = view.findViewById(R.id.txt_company_name);
                layout = view.findViewById(R.id.layout);
                txtFarmerName = view.findViewById(R.id.txt_farmer_name);
                txtDate = view.findViewById(R.id.txt_date);
                txtCenterName = view.findViewById(R.id.txt_center_name);
                txtServeiceType = view.findViewById(R.id.txt_service_type);
                txtProductType = view.findViewById(R.id.txt_product_type);
            }
        }
    }

    private void loadList() {
        ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getAgronomistReportList(PROCUREMENT_GET_AGRONOMIST);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String  agronomistList;
                    try {
                        agronomistList = response.body().string();
                        JSONArray jsonArray = new JSONArray(agronomistList);

                        for (int i = 0; i<jsonArray.length(); i++) {
                            AgronomistListModel agronomistListModel = new AgronomistListModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            agronomistListModel.setCreated_dt(object.getString("created_dt"));
                            agronomistListModel.setCompany(object.getString("company"));
                            agronomistListModel.setFarmer_name(object.getString("farmer_name"));
                            agronomistListModel.setCenter_name(object.getString("center_name"));
                            agronomistListModel.setService_type(object.getString("service_type"));
                            agronomistListModel.setProduct_type(object.getString("product_type"));
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
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
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
}