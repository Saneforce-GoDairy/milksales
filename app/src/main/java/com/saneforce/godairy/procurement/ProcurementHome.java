package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_SUBDIVISION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.ProcSubDivison;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityProcurementHomeBinding;
import com.saneforce.godairy.procurement.custom_form.CustomFormDashboardActivity;
import com.saneforce.godairy.procurement.custom_form.ReportHomeActivity;
import com.saneforce.godairy.procurement.database.DatabaseManager;
import com.saneforce.godairy.procurement.reports.ProcReportsHomeActivity;
import com.saneforce.godairy.procurement.ska.ExistingFarmerVisitActivity;
import com.saneforce.godairy.procurement.ska.NewFarmerCreationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcurementHome extends AppCompatActivity {
    private ActivityProcurementHomeBinding binding;
    private final Context context = this;
    private static final String TAG = "Procurement_";
    private List<ProcSubDivison> subDivisionList;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProcurementHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        subDivisionList = new ArrayList<>();

        databaseManager = new DatabaseManager(getApplicationContext());
        databaseManager.open();

        onClick();
        loadHome();
        loadSubdivision();
    }

    private void loadSubdivision() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call =
                apiInterface.getSubDivision(PROCUREMENT_GET_SUBDIVISION);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    String subDivisionList1 = "";

                    try {
                        subDivisionList1 = response.body().string();

                        JSONObject jsonObject = new JSONObject(subDivisionList1);
                        boolean mRecords = jsonObject.getBoolean("status");

                        ArrayList<String> SubDivisionArray = new ArrayList<>();

                        if (mRecords){
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                ProcSubDivison subDivison = new ProcSubDivison();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                subDivison.setSubdivision_sname(object.getString("subdivision_sname"));
                                subDivisionList.add(subDivison);
                                SubDivisionArray.add(object.getString("subdivision_sname"));

                                // for database
                                databaseManager.open();
                                databaseManager.deleteAllSubDivision();
                                databaseManager.saveSubDivision(SubDivisionArray);
                            }
                        }
                    } catch (IOException | JSONException e) {
                       // throw new RuntimeException(e);
                        Log.e(TAG, "Unable to parse json " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG,  t.getMessage());
            }
        });
    }

    private void loadHome() {
            ArrayList<Integer> dashboardImage = new ArrayList<>(Arrays.asList(
            R.drawable.doctor,
            R.drawable.ait_form,
            R.drawable.veterinary,
            R.drawable.ic_quality,
            R.drawable.ic_maintanence,
            R.drawable.ic_agent,
            R.drawable.ic_collection,
            R.drawable.ic_procurement,
                    R.drawable.ic_procurement,
                    R.drawable.ic_maintanence,
                    R.drawable.ic_maintanence,
                    R.drawable.new_farmer_crea,
                    R.drawable.ic_agent));

    ArrayList<String> dashboardName = new ArrayList<>(Arrays.asList(
            "Agronomist",
            "AIT Form",
            "Veterinary",
            "Quality" ,
            "Maintanence-Regular Form",
            "Existing Agent Visit" ,
            "Collection Center Location" ,
            "Procurement Asset",
            "Farmer Creation",
            "Maintenance Regular",
            "Existing center visit",
            "New farmer creation",
            "Existing Farmer Visit"
    ));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemViewCacheSize(20);
        Adapter adapter6 = new Adapter(context, dashboardImage, dashboardName);
        binding.recyclerView.setAdapter(adapter6);
    }

    private void onClick() {

        binding.logout.setOnClickListener(v -> finish());

        binding.reports.setOnClickListener(v -> {
            startActivity(new Intent(context, ProcReportsHomeActivity.class));
        });

        binding.csForm.setOnClickListener(v -> startActivity(new Intent(context, CustomFormDashboardActivity.class)));
        binding.csFormReport.setOnClickListener(v -> startActivity(new Intent(context, ReportHomeActivity.class)));
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        ArrayList<Integer> exploreImage;
        ArrayList<String> exploreName;
        Context context;

        public Adapter(Context context, ArrayList<Integer> courseImg, ArrayList<String> courseName) {
            this.context = context;
            this.exploreImage = courseImg;
            this.exploreName = courseName;
        }

        @NonNull
        @Override
        public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_dash_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
            int res = exploreImage.get(position);
            holder.images.setImageResource(res);
            holder.text.setText(exploreName.get(position));

            holder.layout.setOnClickListener(v -> {
                switch (position){
                    case 0:
                        startActivity(new Intent(context, AgronomistFormActivity.class));
                        break;

                    case 1:
                        startActivity(new Intent(context, AITFormActivity.class));
                        break;

                    case 2:
                        startActivity(new Intent(context, VeterinaryDoctorsFormActivity.class));
                        break;

                    case 3:
                        startActivity(new Intent(context, QualityFormActivity.class));
                        break;

                    case 4:
                        startActivity(new Intent(context, MaintanenceIssuesFormActivity.class));
                        break;

                    case 5:
                        startActivity(new Intent(context, ExistingAgentVisitActivity.class));
                        break;

                    case 6:
                        startActivity(new Intent(context, CollectionCenterLocationActivity.class));
                        break;

                    case 7:
                        startActivity(new Intent(context, ProcurementAssetActivity.class));
                        break;

                    case 8:
                        startActivity(new Intent(context, FarmerCreationActivity.class));
                        break;
                    case 9:
                        startActivity(new Intent(context, MaintanenceRegularActivity.class));
                        break;

                    case 10:
                        startActivity(new Intent(context, ExistingCenterVisitActivity.class));
                        break;

                    // for ska clients
                    case 11:
                        startActivity(new Intent(context, NewFarmerCreationActivity.class));
                        break;
                    case 12:
                        startActivity(new Intent(context, ExistingFarmerVisitActivity.class));
                        break;
                }
            });
        }

        @Override
        public int getItemCount() {
            return exploreImage.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView images;
            TextView text;
            CardView layout;

            public ViewHolder(View view) {
                super(view);
                images = view.findViewById(R.id.iconImageView);
                text = view.findViewById(R.id.name_text);
                layout = view.findViewById(R.id.layout);
            }
        }
    }
}