package com.saneforce.godairy.SFA_Activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Model_Class.Retailer_Modal_List;

import java.util.List;

public class InshopRetailerAdapter extends RecyclerView.Adapter<InshopRetailerAdapter.MyViewHolder>{

    private Context context;
    private List<Retailer_Modal_List> Retailer_Modal_Listitem;
    private int rowLayout;
    AdapterOnClick mAdapterOnClick;
    String activityName;
    Common_Class common_class;

    @NonNull
    @Override
    public InshopRetailerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v;
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        v=layoutInflater.inflate(R.layout.inshop_retailer_list,parent,false);
//        return new MyViewHolder(v);

        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new InshopRetailerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InshopRetailerAdapter.MyViewHolder holder, int position) {
        Retailer_Modal_List Retailer_Modal_List = Retailer_Modal_Listitem.get(position);
        String typ = Retailer_Modal_List.getType() == null ? "0" : Retailer_Modal_List.getType();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, InshopCheckinActivity.class);
                intent.putExtra("idData",Retailer_Modal_List.getName());
                intent.putExtra("rcode",Retailer_Modal_List.getId());
                intent.putExtra("rmobile",Retailer_Modal_List.getPrimary_No());
                intent.putExtra("rroute",Retailer_Modal_List.getTownName());
                intent.putExtra("type", Retailer_Modal_List.getType());
                intent.putExtra("id", Retailer_Modal_List.getId());
                intent.putExtra("position", position);

//                Toast.makeText(context,"posdsfdghfghfgg ppp   " + position,Toast.LENGTH_SHORT).show();
                Log.v("position", String.valueOf(position));
                Log.v("position", Retailer_Modal_List.getName());
                context.startActivity(intent);




            }
        });

        holder.textviewname.setText("" + Retailer_Modal_List.getName().toUpperCase());
        holder.textId.setText("" + Retailer_Modal_List.getERP_Code());
        holder.outletAddress.setText("" + Retailer_Modal_List.getListedDrAddress1());
//        holder.clsdRmks.setText("" + Retailer_Modal_List.getClosedRemarks());
        holder.txRetNo.setText("" + Retailer_Modal_List.getListedDrSlNo().toString());

        if (!Common_Class.isNullOrEmpty(Retailer_Modal_List.getPrimary_No())) {
            holder.llCallMob.setVisibility(View.VISIBLE);
            holder.tvPhone.setText(""+Retailer_Modal_List.getPrimary_No());
        }

        holder.llCallMob.setVisibility(View.GONE);

        holder.llCallMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    common_class.showCalDialog(context, "Do you want to Call this Outlet?", holder.tvPhone.getText().toString().replaceAll(",", ""));
                }
                catch (Exception e){
                    Log.v("Call:Outlet:",e.getMessage());
                }
            }
        });

//        holder.layparent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAdapterOnClick.onIntentClick(holder.getBindingAdapterPosition());
//            }
//        });

        holder.linDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common_Class.isNullOrEmpty(Retailer_Modal_List.getLat()) || Common_Class.isNullOrEmpty(Retailer_Modal_List.getLong())) {
                    Toast.makeText(context, "No route is found", Toast.LENGTH_SHORT).show();
                } else {
                    drawRoute(Retailer_Modal_List.getName(), Retailer_Modal_List.getLat(), Retailer_Modal_List.getLong());
                }
            }
        });

        holder.txCustStatus.setText((typ.equalsIgnoreCase("3") ? "Duplicate" : typ.equalsIgnoreCase("2") ? "Closed" : (typ.equalsIgnoreCase("1") ? "Service" : "Non Service")));

        switch (typ) {
            case "1":
                holder.retStaBdg.setBackground(context.getDrawable(R.drawable.button_green));
                break;
            case "2":
                holder.retStaBdg.setBackground(context.getDrawable(R.drawable.round_status_blue));
                break;
            case "3":
                holder.retStaBdg.setBackground(context.getDrawable(R.drawable.button_yellows));
                break;
            default:
                holder.retStaBdg.setBackground(context.getDrawable(R.drawable.button_blueg));
                break;
        }

        holder.icAC.setVisibility(View.INVISIBLE);
        if (Retailer_Modal_List.getDelivType() != null && Retailer_Modal_List.getDelivType().equalsIgnoreCase("AC")) {
            holder.icAC.setVisibility(View.VISIBLE);
        }
    }

    private void drawRoute(String OutletName, String sLat, String sLng) {
        try {
            Intent intent = new Intent(context, MapDirectionActivity.class);
            intent.putExtra(Constants.DEST_LAT, sLat);
            intent.putExtra(Constants.DEST_LNG, sLng);
            intent.putExtra(Constants.DEST_NAME, OutletName);
            intent.putExtra(Constants.NEW_OUTLET, "");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.v("OutletInfoDrawRoute:", e.getMessage());
        }

    }


    @Override
    public int getItemCount() {

        if (Retailer_Modal_Listitem != null) {
            return Retailer_Modal_Listitem.size();
        } else {
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, txRetNo, status, invoice, outletAddress, textId, clsdRmks, txCustStatus, lupdDt,tvPhone;
        public LinearLayout retStaBdg, icAC, icFreezer, layparent, linDirection,llCallMob;
        Button btnSend;
        EditText etSNo;


        public MyViewHolder(View view) {
            super(view);
            linDirection = view.findViewById(R.id.linDirection);
            layparent = view.findViewById(R.id.layparent);
            textviewname = view.findViewById(R.id.isRetailername);
            textId = view.findViewById(R.id.txISCustID);
            outletAddress = view.findViewById(R.id.txISshopAddress);
            txCustStatus = view.findViewById(R.id.txISCustStatus);
            retStaBdg = view.findViewById(R.id.isRetStaBdg);
//            status = view.findViewById(R.id.status);
//            invoice = view.findViewById(R.id.invoice);
            icAC = view.findViewById(R.id.isIcAC);
//            lupdDt = view.findViewById(R.id.lupdDt);
//            btnSend = view.findViewById(R.id.btnSend);
//            etSNo = view.findViewById(R.id.etSNo);
            txRetNo = view.findViewById(R.id.txIsRetNo);
            llCallMob=view.findViewById(R.id.btnISCallMob);
            tvPhone=view.findViewById(R.id.txISMobile);
        }
    }

    public InshopRetailerAdapter(List<Retailer_Modal_List> Retailer_Modal_Listitem, int rowLayout, Context context,
                                 String activityName, AdapterOnClick mAdapterOnClick) {
        this.Retailer_Modal_Listitem = Retailer_Modal_Listitem;
        this.rowLayout = rowLayout;
        this.context = context;
        this.activityName = activityName;
        this.mAdapterOnClick = mAdapterOnClick;
        common_class=new Common_Class(context);
    }

}
