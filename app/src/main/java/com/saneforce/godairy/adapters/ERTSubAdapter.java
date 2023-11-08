package com.saneforce.godairy.adapters;

import static android.Manifest.permission.CALL_PHONE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.saneforce.godairy.Activity_Hap.ERT;
import com.saneforce.godairy.Model_Class.ERTChild;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Designed and Developed by Mohammad suhail ahmed on 24/02/2020
 */
public class ERTSubAdapter extends RecyclerView.Adapter<ERTSubAdapter.SubMenuViewHolder> {
    private List<ERTChild> menus;
    private Context context;
    private ERT menuActivity;
    private Date date;
    private DateFormat dateFormat;
    private int orderid;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;

    public class SubMenuViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price, count;
        ImageView profileImage;


        public SubMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.team_name);
            price = itemView.findViewById(R.id.team_position);
            count = itemView.findViewById(R.id.team_phone);
            profileImage = itemView.findViewById(R.id.profile_image);

        }
    }

    public ERTSubAdapter(Context context, List<ERTChild> menus) {
        this.context = context;
        this.menus = menus;

    }

    @NonNull
    @Override
    public ERTSubAdapter.SubMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_child_ert, parent, false);
        return new ERTSubAdapter.SubMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ERTSubAdapter.SubMenuViewHolder holder, int position) {

        ERTChild menu = menus.get(position);

        Glide.with(context)
                .load(menus.get(position).getProfilePic())
                .into(holder.profileImage);
        holder.name.setText(menu.getName());
        holder.price.setText(menu.getDesig());
        holder.count.setText(menu.getSFMobile());

        holder.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int readReq = ContextCompat.checkSelfPermission(context, CALL_PHONE);
                if (readReq != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HAPApp.activeActivity, new String[]{CALL_PHONE}, REQUEST_PERMISSIONS_REQUEST_CODE);
                } else {
                    if (TextUtils.isEmpty(menu.getSFMobile()) || menu.getSFMobile().length() < 10) {
                        Toast.makeText(context, " Invalid phone number ", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + menu.getSFMobile()));//change the number
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(callIntent);
                    }
                }
                /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(menu.getSFMobile()));

                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(callIntent);*/
            }

        });

        Log.e("ERT_DETAILS_SUB", menu.getDesig());
        Log.e("ERT_DETAILS_SUB", menu.getName());
        Log.e("ERT_DETAILS_SUB", menu.getSFMobile());

    }

    @Override
    public int getItemCount() {
        return menus.size();
    }
}
