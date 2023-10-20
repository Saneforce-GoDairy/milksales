package com.milksales.godairy.SFA_Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.Interface.AdapterOnClick;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Activity.MapDirectionActivity;
import com.milksales.godairy.SFA_Activity.Outlet_Info_Activity;
import com.milksales.godairy.SFA_Model_Class.Retailer_Modal_List;

import java.util.List;

public class Outlet_Info_Adapter extends RecyclerView.Adapter<Outlet_Info_Adapter.MyViewHolder> {

    private List<Retailer_Modal_List> Retailer_Modal_Listitem;
    private int rowLayout;
    private Context context;
    AdapterOnClick mAdapterOnClick;
    String activityName;
    Common_Class common_class;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewname, textviewdate, txRetNo, status, invoice, outletAddress, textId, clsdRmks, txCustStatus, lupdDt,tvPhone;
        public LinearLayout retStaBdg, icAC, icFreezer, layparent, linDirection,llCallMob, pending;
        Button btnSend;
        EditText etSNo;
        RelativeLayout rlSeqParent;


        public MyViewHolder(View view) {
            super(view);
            linDirection = view.findViewById(R.id.linDirection);
            icFreezer = view.findViewById(R.id.icFreezer);

            layparent = view.findViewById(R.id.layparent);
            textviewname = view.findViewById(R.id.retailername);
            textId = view.findViewById(R.id.txCustID);
            clsdRmks = view.findViewById(R.id.txClsdRmks);
            outletAddress = view.findViewById(R.id.ShopAddr);
            txCustStatus = view.findViewById(R.id.txCustStatus);
            retStaBdg = view.findViewById(R.id.retStaBdg);
            status = view.findViewById(R.id.status);
            invoice = view.findViewById(R.id.invoice);
            icAC = view.findViewById(R.id.icAC);
            lupdDt = view.findViewById(R.id.lupdDt);
            btnSend = view.findViewById(R.id.btnSend);
            etSNo = view.findViewById(R.id.etSNo);
            txRetNo = view.findViewById(R.id.txRetNo);
            rlSeqParent = view.findViewById(R.id.rlSequence);
            llCallMob=view.findViewById(R.id.btnCallMob);
            tvPhone=view.findViewById(R.id.retailePhoneNum);
            pending=view.findViewById(R.id.PendingStatus);
        }
    }


    public Outlet_Info_Adapter(List<Retailer_Modal_List> Retailer_Modal_Listitem, int rowLayout, Context context,
                               String activityName, AdapterOnClick mAdapterOnClick) {
        this.Retailer_Modal_Listitem = Retailer_Modal_Listitem;
        this.rowLayout = rowLayout;
        this.context = context;
        this.activityName = activityName;
        this.mAdapterOnClick = mAdapterOnClick;
        common_class=new Common_Class(context);
    }

    @Override
    public Outlet_Info_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Outlet_Info_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Outlet_Info_Adapter.MyViewHolder holder, int position) {
        Retailer_Modal_List Retailer_Modal_List = Retailer_Modal_Listitem.get(position);
        String typ = Retailer_Modal_List.getType() == null ? "0" : Retailer_Modal_List.getType();

        if (activityName.equalsIgnoreCase("Outlets")) {
            holder.rlSeqParent.setVisibility(View.VISIBLE);
        }

        holder.textviewname.setText("" + Retailer_Modal_List.getName().toUpperCase());
        // holder.textId.setText("" + Retailer_Modal_List.getId());
        holder.textId.setText("" + Retailer_Modal_List.getERP_Code());
        holder.outletAddress.setText("" + Retailer_Modal_List.getListedDrAddress1());
        holder.clsdRmks.setText("" + Retailer_Modal_List.getClosedRemarks());

        String data = Retailer_Modal_List.getDoctor_Active_flag();
        if (data.equalsIgnoreCase("2")) {
            holder.rlSeqParent.setVisibility(View.GONE);
            holder.pending.setVisibility(View.VISIBLE);
        }

        holder.lupdDt.setText((Retailer_Modal_List.getLastUpdt_Date().equalsIgnoreCase("")) ? "" : "Last Updated On " + Retailer_Modal_List.getLastUpdt_Date());
        holder.icAC.setVisibility(View.INVISIBLE);
        if (Retailer_Modal_List.getDelivType() != null && Retailer_Modal_List.getDelivType().equalsIgnoreCase("AC")) {
            holder.icAC.setVisibility(View.VISIBLE);
        }
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

        holder.layparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterOnClick.onIntentClick(holder.getBindingAdapterPosition());
            }
        });

        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.etSNo.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(context, "Enter Delivery Sequence", Toast.LENGTH_SHORT).show();

                } else {
                    int val = Integer.parseInt(holder.etSNo.getText().toString());
                    if (val <= Outlet_Info_Activity.retailerSize)
                        Outlet_Info_Activity.outlet_info_activity.submitSeqNo(val, Retailer_Modal_List.getId());
                    else
                        Toast.makeText(context, "Invalid Delivery Sequence", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.txRetNo.setText("" + Retailer_Modal_List.getListedDrSlNo().toString());


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

        if (!Common_Class.isNullOrEmpty(Retailer_Modal_List.getFreezer_required()) && Retailer_Modal_List.getFreezer_required().equalsIgnoreCase("yes")) {
            holder.icFreezer.setVisibility(View.VISIBLE);
        } else {
            holder.icFreezer.setVisibility(View.GONE);
        }

        holder.llCallMob.setVisibility(View.GONE);

        if (!Common_Class.isNullOrEmpty(Retailer_Modal_List.getPrimary_No())) {
            holder.llCallMob.setVisibility(View.VISIBLE);
            holder.tvPhone.setText(""+Retailer_Modal_List.getPrimary_No());
        }
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
}