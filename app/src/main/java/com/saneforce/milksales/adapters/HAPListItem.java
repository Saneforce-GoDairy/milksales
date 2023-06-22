package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.onPayslipItemClick;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HAPListItem extends RecyclerView.Adapter<HAPListItem.ViewHolder> {
    private static final String TAG = "ShiftList";
    private JSONArray mlist = new JSONArray();
    private Context mContext;
    static onPayslipItemClick payClick;
    public HAPListItem(JSONArray mlist, Context mContext) {
        this.mlist = mlist;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HAPListItem.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_list_item, parent, false);
        HAPListItem.ViewHolder holder = new HAPListItem.ViewHolder(view);
        return holder;

    }
    public static void SetPayOnClickListener(onPayslipItemClick mPayClick){
        payClick=mPayClick;
    }
    @Override
    public void onBindViewHolder(@NonNull HAPListItem.ViewHolder holder, int position) {

        JSONObject itm = null;
        try {
            itm = mlist.getJSONObject(position);
        holder.shift_time.setText(itm.getString("name"));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject itm = null;
                try {
                    itm = mlist.getJSONObject(position);
                    if(payClick!=null) payClick.onClick(itm);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return mlist.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shift_time;
        LinearLayout parentLayout;
        //CardView secondarylayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shift_time = itemView.findViewById(R.id.ShiftName);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            //secondarylayout=itemView.findViewById(R.id.secondary_layout);
        }
    }
}
