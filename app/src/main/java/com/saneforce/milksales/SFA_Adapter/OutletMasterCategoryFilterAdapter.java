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
import com.saneforce.milksales.Interface.AdapterOnClick;
import com.saneforce.milksales.R;

import java.util.ArrayList;

public class OutletMasterCategoryFilterAdapter extends RecyclerView.Adapter<OutletMasterCategoryFilterAdapter.ViewHolder> {
    private static final String TAG = "RecycleItem";
    private ArrayList<String> mlist = new ArrayList<>();
    private Context mContext;
    static AdapterOnClick itemClick;
    OutletMasterCategoryFilterAdapter.ViewHolder pholder;
    Common_Class common_class;

    public OutletMasterCategoryFilterAdapter(ArrayList<String> mlist, Context mContext, AdapterOnClick mItemClick) {
        this.mlist = mlist;
        this.mContext = mContext;
        this.itemClick = mItemClick;
        common_class = new Common_Class(mContext);
    }

    @NonNull
    @Override
    public OutletMasterCategoryFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_order_types_ryclv, parent, false);
        OutletMasterCategoryFilterAdapter.ViewHolder holder = new OutletMasterCategoryFilterAdapter.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull OutletMasterCategoryFilterAdapter.ViewHolder holder, int position) {

        String itm = null;
        try {
            itm = mlist.get(position);
            holder.icon.setText(itm);

            holder.gridcolor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String itm = null;
                    try {
                        itm = mlist.get(holder.getBindingAdapterPosition());
                        if (itemClick != null) itemClick.CallMobile(itm);
                    } catch (Exception e) {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return mlist.size();
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