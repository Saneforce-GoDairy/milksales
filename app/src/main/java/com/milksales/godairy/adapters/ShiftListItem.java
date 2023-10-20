package com.milksales.godairy.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.milksales.godairy.Activity_Hap.ImageCapture;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Activity.HAPApp;

public class ShiftListItem extends RecyclerView.Adapter<ShiftListItem.ViewHolder> {
    private static final String TAG = "ShiftList";
    private JsonArray mShift_time = new JsonArray();
    private Context mContext;
    private String checkflag;
    private String OnDutyFlag;
    private String exData;

    public ShiftListItem(JsonArray mShift_time, Context mContext, String checkflag, String OnDutyFlag) {
        this.mShift_time = mShift_time;
        this.mContext = mContext;
        this.checkflag = checkflag;
        this.OnDutyFlag = OnDutyFlag;
        this.exData="";
    }

    public ShiftListItem(JsonArray mShift_time, Context mContext, String checkflag, String OnDutyFlag, String exData) {
        this.mShift_time = mShift_time;
        this.mContext = mContext;
        this.checkflag = checkflag;
        this.OnDutyFlag = OnDutyFlag;
        this.exData=exData;
    }

    @NonNull
    @Override
    public ShiftListItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_list_item, parent, false);
        ShiftListItem.ViewHolder holder = new ShiftListItem.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        JsonObject itm = mShift_time.get(position).getAsJsonObject();
        holder.shift_time.setText(itm.get("name").getAsString());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JsonObject itm = mShift_time.get(position).getAsJsonObject();
                String mMessage = "Do you Want to Confirm This ShiftTime : <br /> <span style=\"color:#cc2311\">" + itm.get("name").getAsString() + "</span>";

                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle(HAPApp.Title)
                        .setMessage(Html.fromHtml(mMessage))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent takePhoto = new Intent(mContext, ImageCapture.class);

                                takePhoto.putExtra("Mode", checkflag);
                                takePhoto.putExtra("ShiftId", itm.get("id").getAsString());
                                takePhoto.putExtra("ShiftName", itm.get("name").getAsString());
                                takePhoto.putExtra("On_Duty_Flag", OnDutyFlag);
                                takePhoto.putExtra("ShiftStart", itm.getAsJsonObject("Sft_STime").get("date").getAsString());
                                takePhoto.putExtra("ShiftEnd", itm.getAsJsonObject("sft_ETime").get("date").getAsString());
                                takePhoto.putExtra("ShiftCutOff", itm.getAsJsonObject("ACutOff").get("date").getAsString());
                                takePhoto.putExtra("data",exData);
                                mContext.startActivity(takePhoto);
                                ((AppCompatActivity) mContext).finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Do something
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return mShift_time.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shift_time;
        LinearLayout parentLayout;
        //CardView secondarylayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shift_time = itemView.findViewById(R.id.shift_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            //secondarylayout=itemView.findViewById(R.id.secondary_layout);
        }
    }
}