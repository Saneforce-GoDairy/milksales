package com.saneforce.milksales.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.saneforce.milksales.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mShift_time = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mShift_time, Context mContext) {
        this.mShift_time = mShift_time;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product, parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.shift_time.setText(mShift_time.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on :"+ mShift_time.get(position));
/*
                Toast.makeText(mContext, mShift_time.get(position), Toast.LENGTH_SHORT).show();
*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return mShift_time.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
          TextView shift_time;
          LinearLayout parentLayout;
          CardView secondarylayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shift_time=itemView.findViewById(R.id.textView13);
            parentLayout=itemView.findViewById(R.id.parent_layout);
            secondarylayout=itemView.findViewById(R.id.secondary_layout);
        }
    }
}
