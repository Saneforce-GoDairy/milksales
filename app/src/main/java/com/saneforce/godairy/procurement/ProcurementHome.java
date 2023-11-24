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

import java.util.ArrayList;
import java.util.Arrays;

public class ProcurementHome extends AppCompatActivity {
    private ActivityProcurementHomeBinding binding;
    private Context context = this;

    ArrayList dashboardImage = new ArrayList(Arrays.asList(
            R.drawable.doctor,
            R.drawable.ait_form,
            R.drawable.veterinary,
            R.drawable.ic_quality,
            R.drawable.ic_maintanence,
            R.drawable.ic_agent,
            R.drawable.ic_collection,
            R.drawable.ic_procurement));

    ArrayList dashboardName = new ArrayList(Arrays.asList(
            "Agronomist",
            "AIT Form",
            "Veterinary",
            "Quality" ,
            "Maintanence-Regular Form",
            "Existing Agent Visit" ,
            "Collection Center Location" ,
            "Procurement Asset"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProcurementHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClick();
        loadHome();
    }

    private void loadHome() {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemViewCacheSize(20);
        Adapter adapter6 = new Adapter(context, dashboardImage, dashboardName);
        binding.recyclerView.setAdapter(adapter6);
    }

    private void onClick() {
        binding.oneAgronomistForm.setOnClickListener(v -> {
            Intent intent = new Intent(context, AgronomistFormActivity.class);
            startActivity(intent);
        });

        binding.aitForm.setOnClickListener(view -> {
            Intent intent = new Intent(context, AITFormActivity.class);
            startActivity(intent);
        });

        binding.veterinaryDoctorForm.setOnClickListener(view -> {
            Intent intent = new Intent(context, VeterinaryDoctorsFormActivity.class);
            startActivity(intent);
        });

        binding.qualityForm.setOnClickListener(view -> {
            Intent intent = new Intent(context, QualityFormActivity.class);
            startActivity(intent);
        });

        binding.maintenceRequlation.setOnClickListener(view -> {
            Intent intent = new Intent(context, MaintanenceIssuesFormActivity.class);
            startActivity(intent);
        });

        binding.existingAgentVisit.setOnClickListener(view -> {
            Intent intent = new Intent(context, ExistingAgentVisitActivity.class);
            startActivity(intent);
        });

        binding.collectionCenter.setOnClickListener(view -> {
            Intent intent = new Intent(context, ColletionCenterLocationActivity.class);
            startActivity(intent);
        });

        binding.procurementAsset.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProcurementAssetActivity.class);
            startActivity(intent);
        });
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        ArrayList exploreImage, exploreName;
        Context context;

        public Adapter(Context context, ArrayList courseImg, ArrayList courseName) {
            this.context = context;
            this.exploreImage = courseImg;
            this.exploreName = courseName;
        }

        @NonNull
        @Override
        public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_dash_item, parent, false);
            Adapter.ViewHolder viewHolder = new Adapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
            int res = (int) exploreImage.get(position);
            holder.images.setImageResource(res);
            holder.text.setText((String) exploreName.get(position));

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
                        startActivity(new Intent(context, ColletionCenterLocationActivity.class));
                        break;

                    case 7:
                        startActivity(new Intent(context, ProcurementAssetActivity.class));
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