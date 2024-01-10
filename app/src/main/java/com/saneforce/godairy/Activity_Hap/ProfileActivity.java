package com.saneforce.godairy.Activity_Hap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {
    private SharedPreferences UserDetails;
    public static final String UserDetail = "MyPrefs";
    private SharedPreferences.Editor editors;
    public static final String MyPREFERENCES = "MyPrefs";

    private Shared_Common_Pref mShared_common_pref;

    private TextView tvchangepassword;
    LinearLayout ll_change_password;
    private TextView tvUserName, tv_head_quarters,tvmobileno,tvreport;
    private ImageView userImage;
    ImageView logout, iv_arrow,edit;


    private final Context context = this;
    private String mProfileUrl;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        tvUserName = findViewById(R.id.user_name);
        userImage = findViewById(R.id.user_image);
        tv_head_quarters = findViewById(R.id.tv_head_quarters);

        tvchangepassword = findViewById(R.id.tv_change_password);
        tvmobileno = findViewById(R.id.tv_mob_no);
        tvreport = findViewById(R.id.tv_report);
        logout = findViewById(R.id.btn_logout);
        iv_arrow=  findViewById(R.id.iv_arrow);
        ll_change_password =  findViewById(R.id.ll_change_password);
        edit = findViewById(R.id.edit);


        ll_change_password.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ChangePasswordActivity.class);
            startActivity(intent);
        });

    logout.setOnClickListener(view -> {
        Intent intent = new Intent(context, LoginHome.class);
        startActivity(intent);
    });
        edit.setOnClickListener(view -> {
            Intent intent = new Intent(context, ImageCapture.class);
            startActivity(intent);
        });

        String mProfileImage = UserDetails.getString("Profile", "");
        String sUName = UserDetails.getString("SfName", "");
//        String eMail = UserDetails.getString("email", "");
        String SFDesig = UserDetails.getString("SFDesig", "");
        String SFRptName = UserDetails.getString("SFRptName","");
        String SFMobile = UserDetails.getString("SFMobile","");
//        String SF_EMP_ID = UserDetails.getString("EmpId","");



     /*   userImage.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
            intent.putExtra("ImageUrl", mProfileImage);
            startActivity(intent);

        });*/
        tv_head_quarters.setText(SFDesig);
        tvUserName.setText(sUName);
        tvreport.setText(SFRptName);
//        tvmobileno.setText(SF_EMP_ID);
        tvmobileno.setText(SFMobile);
        mShared_common_pref = new Shared_Common_Pref(this);

        mProfileUrl = mShared_common_pref.getvalue("mProfile");
        Log.e("hgfhg", mProfileUrl);
        if (!com.saneforce.godairy.Common_Class.Common_Class.isNullOrEmpty(mProfileUrl)) {
            String[] image = mProfileUrl.split("/");
            if (image.length > 0 && image[(image.length - 1)].contains(".")) {
                loadImage(mProfileUrl);
            }
        }
    }

    private void loadImage(String mProfileUrl) {
        Glide.with(this.context)
                .load(mProfileUrl)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userImage);
    }

    public class Adapter extends RecyclerView.Adapter<Dashboard.Adapter.ViewHolder> {
        ArrayList exploreImage, exploreName, exploreheadquarters,exploremobile,explorereport;
        Context context;

        public Adapter(Context context, ArrayList courseImg, ArrayList courseName, ArrayList courseheadquarters,ArrayList coursereport,ArrayList coursemobile) {
            this.context = context;
            this.exploreImage = courseImg;
            this.exploreName = courseName;
            this.exploreheadquarters = courseheadquarters;
            this.exploremobile = coursemobile;
            this.explorereport = coursereport;
        }

        @NonNull
        @Override
        public Dashboard.Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull Dashboard.Adapter.ViewHolder holder, int position) {
            int res = (int) exploreImage.get(position);
            holder.images.setImageResource(res);
            holder.text.setText((String) exploreName.get(position));
            holder.text.setText((String) exploreheadquarters.get(position));
            holder.text.setText((String) exploremobile.get(position));
            holder.text.setText((String) explorereport.get(position));
        }

        @Override
        public int getItemCount() {
            return exploreImage.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView images;
            TextView text,tvmobileno,tvreport;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                images = itemView.findViewById(R.id.image);
                text = itemView.findViewById(R.id.name);
                tvmobileno =itemView.findViewById(R.id.tv_mob_no);
                tvreport = itemView.findViewById(R.id.tv_report);
            }
        }
    }
}
