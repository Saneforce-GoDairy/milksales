package com.saneforce.milksales.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.R;
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
    private OnClickInterface onClickInterface;

    public ShiftTimeAdapter(JsonArray mShift_time, Context mContext, String checkflag, String OnDutyFlag, String exData) {
        this.mShift_time = mShift_time;
        this.mContext = mContext;
        this.checkflag = checkflag;
        this.OnDutyFlag = OnDutyFlag;
        this.exData=exData;

        try{
            this.onClickInterface = ((OnClickInterface) mContext);
        }catch (ClassCastException e){
            throw new ClassCastException(e.getMessage());
        }
    }

    @NonNull
    @Override
    public ShiftTimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShiftListItemBinding binding = ShiftListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        holder.binding.getRoot().setOnClickListener(v -> {
            lastSelectedPosition = selectedPosition;

            Log.e("click_in", "clicked");
            selectedPosition = holder.getBindingAdapterPosition();
            notifyItemChanged(lastSelectedPosition);
            notifyItemChanged(selectedPosition);
        });

        if (selectedPosition == holder.getBindingAdapterPosition()) {
            holder.binding.card.setCardBackgroundColor(Color.parseColor("#DDEFF9"));
            holder.binding.card.setForeground(ContextCompat.getDrawable(mContext, R.drawable.border_blue_box));

            JsonObject item = mShift_time.get(position).getAsJsonObject();
            Intent intent = new Intent();
            intent.putExtra("Mode", checkflag);
            intent.putExtra("ShiftId", item.get("id").getAsString());
            intent.putExtra("ShiftName", item.get("name").getAsString());
            intent.putExtra("On_Duty_Flag", OnDutyFlag);
            intent.putExtra("ShiftStart", item.get("Sft_STime").getAsString());
            intent.putExtra("ShiftEnd", item.get("sft_ETime").getAsString());
            intent.putExtra("ShiftCutOff", item.getAsJsonObject("ACutOff").get("date").getAsString());
            intent.putExtra("data",exData);
//          Intent takePhoto = new Intent(mContext, ImageCapture.class);
            onClickInterface.onClickInterface(intent);
        } else {
            holder.binding.card.setCardBackgroundColor(Color.WHITE);
            holder.binding.card.setForeground(ContextCompat.getDrawable(mContext, R.drawable.border_gray_box));
        }

        holder.binding.shiftName.setText(mShift_time.get(position).getAsJsonObject().get("name").getAsString());
        String sTime = mShift_time.get(position).getAsJsonObject().get("Sft_STime").getAsString();
        String eTime = mShift_time.get(position).getAsJsonObject().get("sft_ETime").getAsString();
        holder.binding.shiftTime.setText(sTime + " - " + eTime);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    public interface OnClickInterface{
        public void onClickInterface(Intent intent);
    }
}