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

    public static class AgronomistListAdapter extends  RecyclerView.Adapter<AgronomistListAdapter.ViewHolder>{
        private final List<ProcAgronoListModel> agronomistListModel;
        private final Context context;

        public AgronomistListAdapter(Context context, List<ProcAgronoListModel> agronomistListModel) {
            this.context = context;
            this.agronomistListModel = agronomistListModel;
        }

        @NonNull
        @Override
        public AgronomistListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_agronomist_report, parent, false);
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
            holder.txtPlant.setText(agronomistListModel.get(position).getPlant_name());
            holder.txtTeatDip.setText(agronomistListModel.get(position).getTeat_dip());
            holder.txtFodderDevAcres.setText(agronomistListModel.get(position).getFodder_dev());
            holder.txtFarmersEnrolled.setText(agronomistListModel.get(position).getFarmers_enrolled());

            /*
               below logic used for access procurement images folder ( its wrks dev and live )
             */
            URL url = null;
            try {
                url = new URL(BASE_URL);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            String BASE_URL_PROCUREMENT_IMG = url.getProtocol() + "://" + url.getHost() + "/" + "Procurement_images/";
            Log.e("proc_img_url", BASE_URL_PROCUREMENT_IMG);

            Glide.with(context)
                    .load(BASE_URL_PROCUREMENT_IMG + agronomistListModel.get(position).getFarmers_meeting_img())
                    .into(holder.imgFarmersMeeting);

            Glide.with(context)
                    .load(BASE_URL_PROCUREMENT_IMG + agronomistListModel.get(position).getCsr_img())
                    .into(holder.imgCSRImage);

            Glide.with(context)
                    .load(BASE_URL_PROCUREMENT_IMG + agronomistListModel.get(position).getFodder_acres_img())
                    .into(holder.imgFodderAcres);

            holder.imgFarmersMeeting.setOnClickListener(v -> {
                String imageUrl = BASE_URL_PROCUREMENT_IMG + agronomistListModel.get(position).getFarmers_meeting_img();
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
                intent.putExtra("event_name", "Farmers meeting"); // This is url ( not URI )
                intent.putExtra("url", imageUrl); // url not URI
                context.startActivity(intent);
            });

            holder.imgCSRImage.setOnClickListener(v -> {
                String imageUrl = BASE_URL_PROCUREMENT_IMG + agronomistListModel.get(position).getCsr_img();
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
                intent.putExtra("event_name", "CSR Activity"); // This is url ( not URI )
                intent.putExtra("url", imageUrl); // url not URI
                context.startActivity(intent);
            });

            holder.imgFodderAcres.setOnClickListener(v -> {
                String imageUrl = BASE_URL_PROCUREMENT_IMG + agronomistListModel.get(position).getFodder_acres_img();
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("access_id", "1"); // 1 for url ( without access for URI storage image )
                intent.putExtra("event_name", "Fodder dev acres"); // This is url ( not URI )
                intent.putExtra("url", imageUrl); // url not URI
                context.startActivity(intent);
            });

            holder.txtViewDetails.setOnClickListener(v -> {
                holder.viewSecondLayout.setVisibility(View.VISIBLE);
                holder.txtViewDetails.setVisibility(GONE);
                holder.txtViewLess.setVisibility(View.VISIBLE);
            });

            holder.txtViewLess.setOnClickListener(v -> {
                holder.viewSecondLayout.setVisibility(View.GONE);
                holder.txtViewDetails.setVisibility(View.VISIBLE);
                holder.txtViewLess.setVisibility(View.GONE);
            });

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
            TextView txtPlant, txtTeatDip, txtFodderDevAcres, txtFarmersEnrolled, txtViewDetails, txtViewLess;
            CardView layout;
            ImageView imgFarmersMeeting, imgCSRImage, imgFodderAcres;
            LinearLayout viewSecondLayout;

            public ViewHolder(View view) {
                super(view);
                txtCompanyName = view.findViewById(R.id.txt_company_name);
                layout = view.findViewById(R.id.layout);
                txtFarmerName = view.findViewById(R.id.txt_farmer_name);
                txtDate = view.findViewById(R.id.txt_date);
                txtCenterName = view.findViewById(R.id.txt_center_name);
                txtServeiceType = view.findViewById(R.id.txt_service_type);
                txtProductType = view.findViewById(R.id.txt_product_type);
                imgFarmersMeeting = view.findViewById(R.id.farmers_meeting_img);
                imgCSRImage = view.findViewById(R.id.csr_activity_img);
                imgFodderAcres = view.findViewById(R.id.fodder_acres_img);
                txtPlant = view.findViewById(R.id.txt_plant_name);
                txtTeatDip = view.findViewById(R.id.txt_teat_dip);
                txtFodderDevAcres = view.findViewById(R.id.txt_fodder_dev_acres);
                txtFarmersEnrolled = view.findViewById(R.id.txt_farmers_enrolled);
                txtViewDetails = view.findViewById(R.id.txt_view_details);
                viewSecondLayout = view.findViewById(R.id.second_cn);
                txtViewLess = view.findViewById(R.id.txt_view_less);
            }
        }
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
                        if (agronomistList.equals("\r\n")){
                            Toast.makeText(context, "list load error!", Toast.LENGTH_SHORT).show();
                            return;
                        }

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