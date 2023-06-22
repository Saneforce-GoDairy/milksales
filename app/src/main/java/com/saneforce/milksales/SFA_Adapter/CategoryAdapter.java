package com.saneforce.milksales.SFA_Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Model_Class.Category_Universe_Modal;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    Context context;
    CategoryAdapter.MyViewHolder pholder;
    List<Category_Universe_Modal> listt;
    int selectedPos=0;
    Activity activity;

    public CategoryAdapter(Context applicationContext, List<Category_Universe_Modal> list, int pos, Activity activity) {
        this.context = applicationContext;
        this.listt = list;
        this.selectedPos=pos;
        this.activity=activity;
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.category_order_horizantal_universe_gridview, parent, false);
        return new MyViewHolder(view);
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
    public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, int position) {
        try {
            holder.icon.setText(listt.get(position).getName());
            if (!listt.get(position).getCatImage().equalsIgnoreCase("")) {
                holder.ivCategoryIcon.clearColorFilter();
                Glide.with(this.context)
                        .load(listt.get(position).getCatImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.ivCategoryIcon);
            } else {
                holder.ivCategoryIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.product_logo));
                holder.ivCategoryIcon.setColorFilter(context.getResources().getColor(R.color.grey_500));
            }

            holder.gridcolor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pholder != null) {
                        pholder.gridcolor.setBackground(context.getResources().getDrawable(R.drawable.cardbutton));
                        pholder.icon.setTextColor(context.getResources().getColor(R.color.black));
                        pholder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        pholder.undrCate.setVisibility(View.GONE);
                    }
                    pholder = holder;
                    selectedPos = position;
                  //  Order_Category_Select.order_category_select.showOrderItemList(position, "");
                    holder.gridcolor.setBackground(context.getResources().getDrawable(R.drawable.cardbtnprimary));
                    holder.icon.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                    holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                    holder.undrCate.setVisibility(View.VISIBLE);
                }
            });


            if (position == selectedPos) {

                holder.gridcolor.setBackground(context.getResources().getDrawable(R.drawable.cardbtnprimary));
                holder.icon.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                holder.icon.setTypeface(Typeface.DEFAULT_BOLD);
                holder.undrCate.setVisibility(View.VISIBLE);
                pholder = holder;
            } else {
                holder.gridcolor.setBackground(context.getResources().getDrawable(R.drawable.cardbutton));
                holder.icon.setTextColor(context.getResources().getColor(R.color.black));
                holder.icon.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            }


        } catch (Exception e) {
            Log.e( "adapterCategory: ", e.getMessage());
        }


    }

    @Override
    public int getItemCount() {
        return listt.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout gridcolor, undrCate;
        TextView icon;
        ImageView ivCategoryIcon;

        public MyViewHolder(View view) {
            super(view);

            icon = view.findViewById(R.id.textView);
            gridcolor = view.findViewById(R.id.gridcolor);
            ivCategoryIcon = view.findViewById(R.id.ivCategoryIcon);
            undrCate = view.findViewById(R.id.undrCate);

        }
    }


}
