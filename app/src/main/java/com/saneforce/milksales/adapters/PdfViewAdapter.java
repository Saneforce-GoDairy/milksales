package com.saneforce.milksales.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.saneforce.milksales.R;

public class PdfViewAdapter extends RecyclerView.Adapter<PdfViewAdapter.ViewHolder> {
    private JsonArray jsonArray;
    private Context mContext;

    public PdfViewAdapter(JsonArray jsonArray, Context mContext) {
        this.jsonArray = jsonArray;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_pdf_viewer_list, parent, false);
        return new PdfViewAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtPDF.setText("Basic Info");
        holder.linPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return jsonArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPDF;
        LinearLayout linPDF;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPDF = itemView.findViewById(R.id.txt_pdf);
            linPDF = itemView.findViewById(R.id.lin_pdf);
        }
    }
}
