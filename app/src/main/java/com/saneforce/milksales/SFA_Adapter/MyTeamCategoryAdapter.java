package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Activity.MyTeamActivity;

import org.json.JSONArray;

public class MyTeamCategoryAdapter extends RecyclerView.Adapter<MyTeamCategoryAdapter.MyViewHolder> {
    JSONArray AryDta;
    private Context context;
    Common_Class common_class;
    int salRowDetailLayout;
    public static String TAG = "MyTeamCategoryAdapter";

    public MyTeamCategoryAdapter(JSONArray jAryDta, int rowLayout, Context mContext) {
        AryDta = jAryDta;
        context = mContext;
        salRowDetailLayout = rowLayout;
        common_class = new Common_Class(context);
    }

    @NonNull
    @Override
    public MyTeamCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(salRowDetailLayout, parent, false);
        return new MyTeamCategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTeamCategoryAdapter.MyViewHolder holder, int position) {
        try {
            String itm = AryDta.getString(position);
            holder.txCatname.setText(itm);

            holder.llTeamType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        MyTeamActivity.myTeamActivity.getTeamLoc(itm);
                        MyTeamActivity.selectedPos = position;
                        notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.v(TAG, e.getMessage());
                    }
                }
            });

            if (position == MyTeamActivity.selectedPos) {
                holder.llTeamType.setBackground(context.getResources().getDrawable(R.drawable.cardbtnprimary));
                holder.txCatname.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                holder.txCatname.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                holder.llTeamType.setBackground(context.getResources().getDrawable(R.drawable.cardbutton));
                holder.txCatname.setTextColor(context.getResources().getColor(R.color.grey_800));
                holder.txCatname.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return AryDta.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txCatname;
        LinearLayout llTeamType;

        public MyViewHolder(View view) {
            super(view);
            txCatname = view.findViewById(R.id.tvCategoryName);
            llTeamType = view.findViewById(R.id.llTeamType);

        }
    }
}
