package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Activity_Hap.ProductImageView;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.MyAlertDialog;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class AdapterShowMultipleImages extends RecyclerView.Adapter<AdapterShowMultipleImages.ViewHolder> {
    Context context;
    ArrayList<String> list;
    Common_Class common_class;

    public AdapterShowMultipleImages(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        this.common_class = new Common_Class(context);
    }

    @NonNull
    @Override
    public AdapterShowMultipleImages.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterShowMultipleImages.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_with_delete, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterShowMultipleImages.ViewHolder holder, int position) {
        common_class.getImageFromS3Bucket(context, list.get(holder.getBindingAdapterPosition()), "stockist_info", (bmp, path) -> {
            holder.image.setImageBitmap(bmp);
            holder.image.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProductImageView.class);
                intent.putExtra("ImageUrl", Uri.fromFile(new File(path)).toString());
                context.startActivity(intent);
            });
        });
        holder.delete.setOnClickListener(v -> MyAlertDialog.show(context, "", "Are you sure you want to delete?", true, "Yes", "No", new AlertBox() {
            @Override
            public void PositiveMethod(DialogInterface dialog, int id) {
                list.remove(holder.getBindingAdapterPosition());
                notifyItemRemoved(holder.getBindingAdapterPosition());
                notifyItemRangeChanged(holder.getBindingAdapterPosition(), list.size());
            }

            @Override
            public void NegativeMethod(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
