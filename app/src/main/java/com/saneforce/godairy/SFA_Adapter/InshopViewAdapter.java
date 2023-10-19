package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Model_Class.InshopModel;
import com.saneforce.godairy.SFA_Model_Class.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class InshopViewAdapter extends RecyclerView.Adapter<InshopViewAdapter.MyViewHolder> {
    Context context;
    List<InshopModel> checkInList=new ArrayList<>();
    String todayDate="";

    String date="";

    public int getIsoutlet() {
        return isoutlet;
    }

    public void setIsoutlet(int isoutlet) {
        this.isoutlet = isoutlet;
    }

    int isoutlet=1;
    public InshopViewAdapter(Context context, ArrayList<InshopModel> checkInList){
        this.context=context;
        this. checkInList= checkInList;
    }
    public  void setPopStockList(ArrayList<InshopModel> checkInList){
        this.checkInList=checkInList;

    }
    public void setDate(String date) {
        this.date = date;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outlet_report_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        todayDate= TimeUtils.getCurrentTime(TimeUtils.FORMAT1);
        InshopModel checkInModel=checkInList.get(position);
        holder.tv_name.setText(""+checkInList.get(position).getRetailername());
        holder.tv_stock.setText(checkInModel.getCintime());
        if(checkInModel.getCflag()==0 ){
            holder.tv_sale.setText("-");
            holder.tv_inTime.setText("-");
        }else {
            holder.tv_sale.setText(checkInModel.getCouttime());
            holder.tv_inTime.setText(checkInModel.getIn_Time());
        }

//        holder.ll_parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Log.v("namere",checkInModel.getRetailername());
//
////                Intent intent = new Intent(context, InshopDetailActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                intent.putExtra("retailerName", checkInModel.getRetailername());
////                intent.putExtra("retailerCode", checkInModel.getRetailercode());
////                intent.putExtra("Sl_No", checkInModel.getSlno());
////                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return checkInList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_stock,tv_sale,tv_inTime,tv_activity_view;
        CardView ll_parent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_pname);
            tv_stock=itemView.findViewById(R.id.tv_cinTime);
            tv_sale=itemView.findViewById(R.id.tv_coutTime);
            tv_inTime=itemView.findViewById(R.id.tv_inTime);
            tv_activity_view=itemView.findViewById(R.id.tv_Activity_view);
            ll_parent=itemView.findViewById(R.id.ll_parent);
        }
    }
}
