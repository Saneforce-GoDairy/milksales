package com.saneforce.milksales.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.saneforce.milksales.Activity_Hap.ProductImageView;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.R;

import java.util.ArrayList;
import java.util.List;

public class POPFilesAdapter extends RecyclerView.Adapter<POPFilesAdapter.MyViewHolder> {
    List<String> AryDta = new ArrayList<>();
    private Context context;
    int salRowDetailLayout;
    String itm = "";
    String data = "";

    public POPFilesAdapter(String jAryDta, int rowLayout, Context mContext) {
        data = jAryDta;
        context = mContext;
        salRowDetailLayout = rowLayout;

        AryDta.clear();

        String[] res = data.split("[,]", 0);
        for (String myStr : res) {
            if (!Common_Class.isNullOrEmpty(myStr))
                AryDta.add(myStr);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(salRowDetailLayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {



            if (AryDta != null && AryDta.size() > 0) {
                itm = AryDta.get(position);
                Glide.with(context)
                        .load(itm)
                        .into(holder.ivFile);
            } else {
                holder.ivFile.setVisibility(View.GONE);
            }


            holder.ivFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {


                        Intent intent = new Intent(context, ProductImageView.class);
                        intent.putExtra("ImageUrl", AryDta.get(position));
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Log.e("FileAdapter: ", e.getMessage());
                    }
                }
            });


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
        if (AryDta != null && AryDta.size() > 0)
            return AryDta.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFile;

        public MyViewHolder(View view) {
            super(view);
            ivFile = view.findViewById(R.id.ivFile);

        }
    }
}
