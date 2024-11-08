package com.saneforce.godairy.Status_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.R;

public class Travel_Approval_Adapter extends RecyclerView.Adapter<Travel_Approval_Adapter.MyViewHolder> {


    JsonArray leave_Approval_ModelsList;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    int dummy;


    public Travel_Approval_Adapter(JsonArray leave_Approval_ModelsList, int rowLayout, Context context, AdapterOnClick mAdapterOnClick) {
        this.leave_Approval_ModelsList = leave_Approval_ModelsList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.mAdapterOnClick = mAdapterOnClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(dummy);
            }
        });*/

        return new Travel_Approval_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        JsonObject jsonObject = (JsonObject) leave_Approval_ModelsList.get(position);
        {
         //   holder.textviewname.setVisibility(View.GONE);
            //Log.v("Leave_APPROEV", jsonObject.toString());
            holder.textviewname.setText(jsonObject.get("Sf_Name").getAsString());
            holder.textviewdate.setText(jsonObject.get("id").getAsString());
            holder.leavedays.setText(jsonObject.get("Total_Amount").getAsString());
            holder.open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mAdapterOnClick.onIntentClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return leave_Approval_ModelsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, open, leavedays;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textviewname = itemView.findViewById(R.id.textviewname);
            textviewdate = itemView.findViewById(R.id.textviewdate);
            open = itemView.findViewById(R.id.open);
            leavedays = itemView.findViewById(R.id.leavedays);
        }
    }
}
