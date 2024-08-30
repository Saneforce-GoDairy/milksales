package com.saneforce.godairy.procurement.custom_form.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.R;
import com.saneforce.godairy.procurement.custom_form.ReportPreviewActivity;
import com.saneforce.godairy.procurement.custom_form.model.CustomReportModel;

import java.util.ArrayList;

public class DynamicFormReportDetAdapter  extends RecyclerView.Adapter<DynamicFormReportDetAdapter.MyviewHolder>{
    private static final String TAG = "DynamicFormReportDetAdapter";
    ArrayList<CustomReportModel> modelArrayList;
    Context context;
    String moduleId="";
    String sfCode="";
    public DynamicFormReportDetAdapter(Context context,ArrayList<CustomReportModel> modelArrayList){
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    public void setModelArrayList(ArrayList<CustomReportModel> modelArrayList){
        this.modelArrayList = modelArrayList;
        notifyDataSetChanged();
    }
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    @NonNull
    @Override
    public DynamicFormReportDetAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_custom_report_key,parent,false);
        return new DynamicFormReportDetAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicFormReportDetAdapter.MyviewHolder holder, int position) {
        CustomReportModel model=modelArrayList.get(position);


        holder.tv_name.setText(model.getEntryId());

        holder.ll_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, moduleId);
                Intent intent = new Intent(context, ReportPreviewActivity.class);
                intent.putExtra("moduleId",moduleId);
                intent.putExtra("entryId",model.getEntryId());
                intent.putExtra("sfCode",sfCode);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.tv_back.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_parent;
        TextView tv_name;
        TextView tv_back;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            ll_parent=itemView.findViewById(R.id.ll_parent);
            tv_name=itemView.findViewById(R.id.tv_item_name);
            tv_back=itemView.findViewById(R.id.tv_back);
        }
    }
}
