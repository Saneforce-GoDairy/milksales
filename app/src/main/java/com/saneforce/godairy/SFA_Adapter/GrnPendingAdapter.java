package com.saneforce.godairy.SFA_Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Model_Class.OutletReport_View_Modal;
import java.util.List;

public class GrnPendingAdapter extends RecyclerView.Adapter<GrnPendingAdapter.MyViewHolder>  {
    Context context;
    List<OutletReport_View_Modal> listt;
    int temp2;

    public GrnPendingAdapter(Context applicationContext, List<OutletReport_View_Modal> list){
        this.context = applicationContext;
        this.listt = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grn_pending_listitems, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            OutletReport_View_Modal Product_Details_Modal = listt.get(holder.getBindingAdapterPosition());

            holder.prodName.setText(Product_Details_Modal.getProductName());
            holder.prodCode.setText(Product_Details_Modal.getProductCode());
            holder.prodPrice.setText(Product_Details_Modal.getMrp());
            holder.manufDate.setText(Product_Details_Modal.getManufDate());
            holder.expDate.setText(Product_Details_Modal.getExpDate());
            holder.orderQnty.setText(Product_Details_Modal.getBilledQty());
            holder.receivedQnty.setText(Product_Details_Modal.getBilledQty());
            holder.batchNo.setText(Product_Details_Modal.getBatchNo());
            holder.invDate.setText("" + Product_Details_Modal.getBillingDate());

            if (holder.damageQnty.getText().equals("")){
                holder.damageQnty.setText("0");
                Product_Details_Modal.setDamaged("0");
            }


            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String s=editable.toString();
                    if (!holder.orderQnty.getText().toString().equals("") && !holder.receivedQnty.getText().toString().equals("")) {
                        int temp1 = Integer.parseInt(holder.orderQnty.getText().toString());
                         temp2 = Integer.parseInt(holder.receivedQnty.getText().toString());
                         Product_Details_Modal.setDamaged(String.valueOf(temp1 - temp2));

                        holder.damageQnty.setText(String.valueOf(temp1 - temp2));

                    }

                }
            };

            holder.orderQnty.addTextChangedListener(textWatcher);
            holder.receivedQnty.addTextChangedListener(textWatcher);

            try {
                if (!Product_Details_Modal.getProductImage().equalsIgnoreCase("")) {
                    holder.prodImage.clearColorFilter();
                    Glide.with(context).load("http://hap.sanfmcg.com/MasterFiles/PImage/" + Product_Details_Modal.getProductImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.prodImage);
                    Log.v("wertyu123", "http://hap.sanfmcg.com/MasterFiles/PImage/"+Product_Details_Modal.getProductImage());
                }
            }
            catch (Exception e){
                Log.v("imageData",e.toString());
            }

        }
        catch (Exception e){
            Log.v("wertyu",e.toString());
        }

    }


    @Override
    public int getItemCount() {
        return listt.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView invNo,invDate,prodName,prodCode,prodPrice,batchNo,manufDate,expDate,invItems,damageQnty,total;
        public EditText receivedQnty,orderQnty;
        public ImageView prodImage;

        public MyViewHolder(View view) {
            super(view);
            invDate= view.findViewById(R.id.invoiceDate);
            prodName= view.findViewById(R.id.productname);
            prodCode= view.findViewById(R.id.prodCode);
            prodPrice= view.findViewById(R.id.mrp);
            batchNo= view.findViewById(R.id.batchNo);
            manufDate= view.findViewById(R.id.manufactureDate);
            expDate= view.findViewById(R.id.expiryDate);
            invItems= view.findViewById(R.id.invoiceItems);
            orderQnty= view.findViewById(R.id.orderQnty);
            receivedQnty= view.findViewById(R.id.receivedQnty);
            damageQnty= view.findViewById(R.id.damageQnty);
            prodImage=view.findViewById(R.id.productImage);
            total=view.findViewById(R.id.NetTotalValue);

        }
    }
}

