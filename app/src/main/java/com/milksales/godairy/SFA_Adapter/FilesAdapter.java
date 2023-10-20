package com.milksales.godairy.SFA_Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.milksales.godairy.Activity_Hap.ProductImageView;
import com.milksales.godairy.Common_Class.AlertDialogBox;
import com.milksales.godairy.Interface.AlertBox;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Activity.HAPApp;

import java.util.ArrayList;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.MyViewHolder> {
    List<String> AryDta = new ArrayList<>();
    int salRowDetailLayout;
    Context context;
    String itm = "";

    /*public FilesAdapter(List<String> jAryDta, int rowLayout, Context mContext) {
        AryDta = jAryDta;
        this.context = mContext;
        salRowDetailLayout = rowLayout;
    }*/

    public FilesAdapter(List<String> aryDta, int rowLayout, Context context) {
        this.AryDta = aryDta;
        this.salRowDetailLayout = rowLayout;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(salRowDetailLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int positions) {
        try {

            if (AryDta != null && AryDta.size() > 0) {
                itm = AryDta.get(positions);
                Glide.with(context)
                        .load(itm)
                        .error(R.drawable.profile_img)
                        .into(holder.ivFile);
            } else {
                holder.ivFile.setVisibility(View.GONE);
            }

//            try {
//                if (ApproveOutletsDetailedActivity.class.equals(context.getClass())) {
//                    holder.ivDel.setVisibility(View.GONE);
//                }
//            } catch (Exception ignored) {}

            holder.ivFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {


                        Intent intent = new Intent(context, ProductImageView.class);
                        intent.putExtra("ImageUrl", AryDta.get(holder.getBindingAdapterPosition()));
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Log.e("FileAdapter: ", e.getMessage());
                    }
                }
            });

            holder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialogBox.showDialog(context, HAPApp.Title, "Are You Sure Want to DELETE?", "OK", "Cancel", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            AryDta.remove(holder.getBindingAdapterPosition());
                            notifyDataSetChanged();

                        }


                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                            dialog.dismiss();

                        }
                    });
                }
            });


        } catch (Exception e) {
            Log.v("Files:", e.getMessage());
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
        if (AryDta != null && AryDta.size() > 0)
            return AryDta.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFile, ivDel;

        public MyViewHolder(View view) {
            super(view);
            ivFile = view.findViewById(R.id.ivFile);
            ivDel = view.findViewById(R.id.ivDel);

        }
    }
}
