package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Interface.onListItemClick;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RyclListItemAdb extends RecyclerView.Adapter<RyclListItemAdb.ViewHolder> {
    private static final String TAG = "RecycleItem";
    private JSONArray mlist = new JSONArray();
    private Context mContext;
    static onListItemClick itemClick;
    RyclListItemAdb.ViewHolder pholder;
    Common_Class common_class;

    String id = "";

    public RyclListItemAdb(JSONArray mlist, Context mContext, onListItemClick mItemClick) {
        this.mlist = mlist;
        this.mContext = mContext;
        this.itemClick = mItemClick;
        common_class = new Common_Class(mContext);
    }

//    public void notify(JSONArray mlist, Context mContext, onListItemClick mItemClick, String id) {
//        this.mlist = mlist;
//        this.mContext = mContext;
//        this.itemClick = mItemClick;
//        common_class = new Common_Class(mContext);
//        this.id = id;
//    }

    @NonNull
    @Override
    public RyclListItemAdb.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_order_types_ryclv, parent, false);
        RyclListItemAdb.ViewHolder holder = new RyclListItemAdb.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RyclListItemAdb.ViewHolder holder, int position) {

        JSONObject itm = null;
        try {
            itm = mlist.getJSONObject(position);
            holder.icon.setText(itm.getString("name"));




            holder.gridcolor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    JSONObject itm = null;
                    try {
                        itm = mlist.getJSONObject(holder.getBindingAdapterPosition());
                        if (itemClick != null) itemClick.onItemClick(itm);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (pholder != null) {
                        pholder.gridcolor.setBackground(mContext.getResources().getDrawable(R.drawable.cardbutton));
                        pholder.icon.setTextColor(mContext.getResources().getColor(R.color.black));
                        pholder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    }
                    pholder = holder;
                    common_class.grpPos = holder.getBindingAdapterPosition();
                    holder.gridcolor.setBackground(mContext.getDrawable(R.drawable.cardbtnprimary));
                    holder.icon.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                    holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                }
            });

            if (position == common_class.grpPos) {

                holder.gridcolor.setBackground(mContext.getResources().getDrawable(R.drawable.cardbtnprimary));
                holder.icon.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                pholder = holder;
            } else {
                holder.gridcolor.setBackground(mContext.getResources().getDrawable(R.drawable.cardbutton));
                holder.icon.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return mlist.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView icon;
        LinearLayout gridcolor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.textView);
            gridcolor = itemView.findViewById(R.id.gridcolor);
        }
    }
}