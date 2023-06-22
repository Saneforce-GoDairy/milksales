package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.onListItemClick;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class adBackFolders extends RecyclerView.Adapter<adBackFolders.ViewHolder> {
    private static final String TAG = "ShiftList";
    private JSONArray mlist = new JSONArray();
    private Context mContext;
    static onListItemClick listItemClick;
    public adBackFolders(JSONArray mlist, Context mContext) {
        this.mlist = mlist;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public adBackFolders.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.backfldrlist, parent, false);
        adBackFolders.ViewHolder holder = new adBackFolders.ViewHolder(view);
        return holder;

    }
    public static void SetOnClickListener(onListItemClick mlistItemClick){
        listItemClick=mlistItemClick;
    }
    @Override
    public void onBindViewHolder(@NonNull adBackFolders.ViewHolder holder, int position) {

        JSONObject itm = null;
        try {
            itm = mlist.getJSONObject(position);
            holder.lblText.setText(itm.getString("name"));

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    JSONObject itm = null;
                    try {
                        itm = mlist.getJSONObject(position);
                        listItemClick.onItemClick(itm);
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
        TextView lblText;
        LinearLayout parentLayout;
        //CardView secondarylayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblText = itemView.findViewById(R.id.lblText);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            //secondarylayout=itemView.findViewById(R.id.secondary_layout);
        }
    }
}