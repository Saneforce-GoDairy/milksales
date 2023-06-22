package com.saneforce.milksales.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.saneforce.milksales.Activity_Hap.UpdateTaskActivity;

import com.saneforce.milksales.Interface.On_ItemCLick_Listner;
import com.saneforce.milksales.Model_Class.EventCapture;
import com.saneforce.milksales.R;

import java.util.ArrayList;
import java.util.List;

public class EventCaptureAdapter extends RecyclerView.Adapter<EventCaptureAdapter.ViewHolder> {
    List<EventCapture> eventCapture;
    EventCapture evC;
    Context mContext;
    ArrayList<Uri> uriArrayList;

    int post;
    On_ItemCLick_Listner on_itemCLick_listner;


    public EventCaptureAdapter(List<EventCapture> eventCapture, Context mContext, On_ItemCLick_Listner on_itemCLick_listner) {
        this.eventCapture = eventCapture;
        this.mContext = mContext;
        this.on_itemCLick_listner = on_itemCLick_listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_event_capture, parent, false);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on_itemCLick_listner.onIntentClick(post);
            }
        });

        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageSet.setImageURI(Uri.parse(eventCapture.get(position).getTask()));
        holder.txtTitle.setText(eventCapture.get(position).getDesc());
        holder.txtRemarks.setText(eventCapture.get(position).getFinishBy());
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventCapture task = eventCapture.get(position);
                Intent intent = new Intent(mContext, UpdateTaskActivity.class);
                intent.putExtra("task", task);
                mContext.startActivity(intent);
            }
        });

        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on_itemCLick_listner.onIntentClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventCapture.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSet;
        TextView txtTitle, txtRemarks;
        MaterialCardView materialCardView;
        ImageView deleteProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSet = itemView.findViewById(R.id.image_captured);
            txtTitle = itemView.findViewById(R.id.edt_title);
            txtRemarks = itemView.findViewById(R.id.edt_remarks);
            materialCardView = itemView.findViewById(R.id.card_event_capture);
            deleteProduct = itemView.findViewById(R.id.delete_product);

        }


    }
}
