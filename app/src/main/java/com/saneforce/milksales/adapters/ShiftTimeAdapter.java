package com.saneforce.milksales.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Activity_Hap.ImageCapture;
import com.saneforce.milksales.Activity_Hap.MainActivity;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.HAPApp;
import com.saneforce.milksales.databinding.ShiftListItemBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShiftTimeAdapter extends RecyclerView.Adapter<ShiftTimeAdapter.ViewHolder> {
    private final JsonArray mShift_time;
    private final Context mContext;
    private final String checkflag;
    private final String OnDutyFlag;
    private final String exData;
    int selectedPosition = -1;
    int lastSelectedPosition = -1;

    public ShiftTimeAdapter(JsonArray mShift_time, Context mContext, String checkflag, String OnDutyFlag, String exData) {
        this.mShift_time = mShift_time;
        this.mContext = mContext;
        this.checkflag = checkflag;
        this.OnDutyFlag = OnDutyFlag;
        this.exData=exData;
    }

    @NonNull
    @Override
    public ShiftTimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShiftListItemBinding binding = ShiftListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.getRoot().setOnClickListener(v -> {
            lastSelectedPosition = selectedPosition;

            selectedPosition = holder.getBindingAdapterPosition();
            notifyItemChanged(lastSelectedPosition);
            notifyItemChanged(selectedPosition);
        });

        if (selectedPosition == holder.getBindingAdapterPosition()) {
            holder.binding.card.setCardBackgroundColor(Color.parseColor("#DDEFF9"));
            holder.binding.card.setForeground(ContextCompat.getDrawable(mContext, R.drawable.border_blue_box));
        } else {
            holder.binding.card.setCardBackgroundColor(Color.WHITE);
            holder.binding.card.setForeground(ContextCompat.getDrawable(mContext, R.drawable.border_gray_box));
        }

        JsonObject jsonObject = mShift_time.get(position).getAsJsonObject();
        holder.binding.shiftName.setText(jsonObject.get("name").getAsString());

        String startTime = jsonObject.getAsJsonObject("Sft_STime").get("date").getAsString().substring(11);
        String endTime = jsonObject.getAsJsonObject("sft_ETime").get("date").getAsString().substring(11);

        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        try {
            final Date startTimeObject = sdf.parse(startTime);
            final Date endTimeObject = sdf.parse(endTime);
            holder.binding.shiftTime.setText(new SimpleDateFormat("K:mm").format(startTimeObject) + "  -  "+new SimpleDateFormat("K:mm").format(endTimeObject));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        holder.binding.card.setOnClickListener(view -> {
            JsonObject itm1 = mShift_time.get(position).getAsJsonObject();
            String mMessage = "Do you Want to Confirm This ShiftTime : <br /> <span style=\"color:#cc2311\">" + itm1.get("name").getAsString() + "</span>";

            AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                    .setTitle(HAPApp.Title)
                    .setMessage(Html.fromHtml(mMessage))
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        Intent takePhoto = new Intent(mContext, ImageCapture.class);

                        takePhoto.putExtra("Mode", checkflag);
                        takePhoto.putExtra("ShiftId", itm1.get("id").getAsString());
                        takePhoto.putExtra("ShiftName", itm1.get("name").getAsString());
                        takePhoto.putExtra("On_Duty_Flag", OnDutyFlag);
                        takePhoto.putExtra("ShiftStart", itm1.getAsJsonObject("Sft_STime").get("date").getAsString());
                        takePhoto.putExtra("ShiftEnd", itm1.getAsJsonObject("sft_ETime").get("date").getAsString());
                        takePhoto.putExtra("ShiftCutOff", itm1.getAsJsonObject("ACutOff").get("date").getAsString());
                        takePhoto.putExtra("data",exData);
                        mContext.startActivity(takePhoto);
                        ((AppCompatActivity) mContext).finish();
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        // Do something
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return mShift_time.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShiftListItemBinding binding;

        public ViewHolder(ShiftListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
