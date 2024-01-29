package com.saneforce.godairy.procurement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityProcurementHomeBinding;
import com.saneforce.godairy.procurement.reports.AgronomistReportActivity;
import com.saneforce.godairy.procurement.reports.ProcReportsHomeActivity;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcurementHome extends AppCompatActivity {
    private ActivityProcurementHomeBinding binding;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProcurementHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClick();
        loadHome();
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
                    R.drawable.ic_procurement));

    ArrayList<String> dashboardName = new ArrayList<>(Arrays.asList(
            "Agronomist",
            "AIT Form",
            "Veterinary",
            "Quality" ,
            "Maintanence-Regular Form",
            "Existing Agent Visit" ,
            "Collection Center Location" ,
            "Procurement Asset",
            "Farmer creation"
    ));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemViewCacheSize(20);
        Adapter adapter6 = new Adapter(context, dashboardImage, dashboardName);
        binding.recyclerView.setAdapter(adapter6);
    }

    private void onClick() {

        binding.logout.setOnClickListener(v -> finish());
        binding.oneAgronomistForm.setOnClickListener(v -> {
            startActivity(new Intent(context, AgronomistFormActivity.class));
        });

        binding.aitForm.setOnClickListener(view -> {
            startActivity(new Intent(context, AITFormActivity.class));
        });

        binding.veterinaryDoctorForm.setOnClickListener(view -> {
            startActivity(new Intent(context, VeterinaryDoctorsFormActivity.class));
        });

        binding.qualityForm.setOnClickListener(view -> {
            startActivity(new Intent(context, QualityFormActivity.class));
        });

        binding.maintenceRequlation.setOnClickListener(view -> {
            startActivity(new Intent(context, MaintanenceIssuesFormActivity.class));
        });

        binding.existingAgentVisit.setOnClickListener(view -> {
            startActivity(new Intent(context, ExistingAgentVisitActivity.class));
        });

        binding.collectionCenter.setOnClickListener(view -> {
            startActivity(new Intent(context, CollectionCenterLocationActivity.class));
        });

        binding.procurementAsset.setOnClickListener(view -> {
            startActivity(new Intent(context, ProcurementAssetActivity.class));
        });

        binding.reports.setOnClickListener(v -> {
            startActivity(new Intent(context, ProcReportsHomeActivity.class));
        });
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