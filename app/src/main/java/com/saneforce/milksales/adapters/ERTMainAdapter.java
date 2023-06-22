package com.saneforce.milksales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Model_Class.ERTParent;
import com.saneforce.milksales.R;

import java.util.List;

/**
 * Designed and Developed by Mohammad suhail ahmed on 24/02/2020
 */
public class ERTMainAdapter extends RecyclerView.Adapter<ERTMainAdapter.MainMenuViewHolder> {
    private List<ERTParent> cacheMenuRes;
    private Context context;
    ERTParent md;

    public class MainMenuViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView subMenuRecycler;
        private TextView subcat;
        private Button ap, rj;

        public MainMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            subMenuRecycler = itemView.findViewById(R.id.submenurecycler);
            subcat = itemView.findViewById(R.id.team_name);
        }
    }

    public ERTMainAdapter(Context context, List<ERTParent> cacheMenuRes) {
        this.context = context;
        this.cacheMenuRes = cacheMenuRes;
    }

    @NonNull
    @Override
    public ERTMainAdapter.MainMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_parent_ert, parent, false);
        return new MainMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ERTMainAdapter.MainMenuViewHolder holder, final int position) {
        md = cacheMenuRes.get(position);
        holder.subcat.setText(md.getSubCategoryName());

        ERTSubAdapter subMenuAdapter = new ERTSubAdapter(context, md.getData());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.subMenuRecycler.setLayoutManager(layoutManager);
        holder.subMenuRecycler.setItemViewCacheSize(20);

        holder.subMenuRecycler.setAdapter(subMenuAdapter);


    }

    @Override
    public int getItemCount() {
        return cacheMenuRes.size();
    }

    public void setCacheMenuRes(List<ERTParent> cacheMenuRes) {
        this.cacheMenuRes = cacheMenuRes;
        notifyDataSetChanged();
    }
}
