package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.milksales.R;

import java.text.DecimalFormat;

public class foodGrid extends BaseAdapter {
    private Context mContext;
    private JsonArray mlist = new JsonArray();

    // Constructor
    public foodGrid(JsonArray mlist, Context mContext) {
        this.mContext = mContext;
        this.mlist = mlist;
    }

    public int getCount() {
        return mlist.size();
    }

    public Object getItem(int position) {
        return mlist.get(position).getAsJsonObject();
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(mContext).inflate(R.layout.grdfoodad, parent, false);
        }
        JsonObject item = (JsonObject) getItem(position);
        TextView txtNm = listitemView.findViewById(R.id.txt_name);
        TextView txtAmt = listitemView.findViewById(R.id.txt_fAmt);
        TextView txtOnDuty = listitemView.findViewById(R.id.txt_onDuty);
        TextView txtPlant = listitemView.findViewById(R.id.txt_plant);

        txtNm.setText(item.get("name").getAsString());
        txtAmt.setText("Rs. " + new DecimalFormat("##0.00").format(Double.valueOf(item.get("amount").getAsString())));

        try {
            txtPlant.setText(item.get("plantName").getAsString() + "-" + item.get("plant").getAsString());
            txtOnDuty.setText(item.get("On-duty").getAsString().equalsIgnoreCase("Yes") ? "On-duty" : item.get("freeMeal").getAsString().equalsIgnoreCase("yes") ? "FreeMeal" : "");

        } catch (Exception e) {

        }

        return listitemView;
    }

}
