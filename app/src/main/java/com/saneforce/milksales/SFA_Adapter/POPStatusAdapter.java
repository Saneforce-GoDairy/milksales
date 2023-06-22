package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class POPStatusAdapter extends RecyclerView.Adapter<POPStatusAdapter.MyViewHolder> {
    Context context;
  POPMaterialAdapter popMaterialAdapter;

    JSONArray jsonArray;

    public POPStatusAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;

    }

    @NonNull
    @Override
    public POPStatusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_popstatus_layout, null, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(POPStatusAdapter.MyViewHolder holder, int position) {
        try {
            JSONObject itm = jsonArray.getJSONObject(position);

            holder.sNo.setText("" + itm.getString("SlNo"));
            holder.requestNo.setText("" + itm.getString("Requset_No"));
            holder.bookingDate.setText("" + itm.getString("Booking_Date"));


            List<QPS_Modal> statusList = new ArrayList<>();
            JSONArray arr = itm.getJSONArray("Details");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                statusList.add(new QPS_Modal(obj.getString("POP_ID"), obj.getString("POP_Status"), obj.getString("Received_Date"), obj.getString("POP_Image")
                        , obj.getString("POP_Req_ID"), obj.getString("Trans_Sl_No")));
            }


            popMaterialAdapter = new POPMaterialAdapter(context, statusList);
            holder.rvMaterials.setAdapter(popMaterialAdapter);


        } catch (Exception e) {
            Log.e("POPStatusAdapter:", e.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sNo, requestNo, bookingDate;
        RecyclerView rvMaterials;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sNo = itemView.findViewById(R.id.tvQpsSno);
            requestNo = itemView.findViewById(R.id.tvQPSReqNo);
            bookingDate = itemView.findViewById(R.id.tvBookingDate);
            rvMaterials = itemView.findViewById(R.id.rvMaterials);

        }
    }
}