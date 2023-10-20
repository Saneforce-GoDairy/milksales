package com.milksales.godairy.SFA_Adapter;

import static com.milksales.godairy.SFA_Activity.PrimaryOrderActivity.selPOS;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.onListItemClick;
import com.milksales.godairy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RyclGrpListItemAdb extends RecyclerView.Adapter<RyclGrpListItemAdb.ViewHolder> {
    private static final String TAG = "RecycleItem";
    private JSONArray mlist = new JSONArray();
    private Context mContext;
    static onListItemClick itemClick;
    Common_Class common_class;

    String id = "";

    public RyclGrpListItemAdb(JSONArray mlist, Context mContext, onListItemClick mItemClick) {
        this.mlist = mlist;
        this.mContext = mContext;
        this.itemClick = mItemClick;
        common_class = new Common_Class(mContext);
    }

    public void notify(JSONArray mlist, Context mContext, String id, onListItemClick mItemClick) {
        this.mlist = mlist;
        this.mContext = mContext;
        this.itemClick = mItemClick;
        common_class = new Common_Class(mContext);
        this.id = id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RyclGrpListItemAdb.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_order_types_ryclv, parent, false);
        RyclGrpListItemAdb.ViewHolder holder = new RyclGrpListItemAdb.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RyclGrpListItemAdb.ViewHolder holder, int position) {

        JSONObject itm = null;
        try {
            itm = mlist.getJSONObject(position);
            holder.icon.setText(itm.getString("name"));


            holder.gridcolor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        JSONObject itm = null;
                        try {
                            itm = mlist.getJSONObject(holder.getBindingAdapterPosition());
                            if (itemClick != null) itemClick.onItemClick(itm);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (id.equalsIgnoreCase("") || (id.equalsIgnoreCase(itm.getString("id")))) {
                            selPOS = position;
                        }
                        notifyDataSetChanged();
                    } catch (Exception e) {

                    }

                }
            });

            if (position == selPOS) {
                //  if (id.equalsIgnoreCase("") || (id.equalsIgnoreCase(itm.getString("id")))) {
                Shared_Common_Pref.ORDER_TYPE = itm.getString("id");
                holder.gridcolor.setBackground(mContext.getResources().getDrawable(R.drawable.cardbtnprimary));
                holder.icon.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                holder.icon.setTypeface(Typeface.DEFAULT_BOLD);


                // }

            } else {
                // if (id.equalsIgnoreCase("") || (!id.equalsIgnoreCase(itm.getString("id")))) {

                holder.gridcolor.setBackground(mContext.getResources().getDrawable(R.drawable.cardbutton));
                if (!id.equalsIgnoreCase("") && !id.equalsIgnoreCase(itm.getString("id")))
                    holder.icon.setTextColor(mContext.getResources().getColor(R.color.grey_500));
                else
                    holder.icon.setTextColor(mContext.getResources().getColor(R.color.black));

                holder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                // }
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