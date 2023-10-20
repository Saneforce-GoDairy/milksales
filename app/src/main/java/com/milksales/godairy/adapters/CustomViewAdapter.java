package com.milksales.godairy.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.milksales.godairy.Common_Class.AlertDialogBox;
import com.milksales.godairy.Interface.AlertBox;
import com.milksales.godairy.Interface.viewProduct;
import com.milksales.godairy.Model_Class.Product_Array;
import com.milksales.godairy.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class CustomViewAdapter extends RecyclerView.Adapter<CustomViewAdapter.MyViewHolder> {

    Integer editCountValue = 0, quntaity, price, subTotalRate = 0, deletePosition;
    List<Product_Array> mProduct_arrays;
    viewProduct viewProd;
    Context context;
    AlertDialog.Builder builder;
    String productname;
    String catName;
    String catImg;
    String productID;
    String productcode;
    Integer productqty;
    Integer productRate;
    Product_Array newProductArray;
    ArrayList<Product_Array> Product_Array_List;
    String productUnit;
    JSONArray jsonValue;

    viewProduct mProducrtDelete;

    public CustomViewAdapter(Context context, JSONArray jsonValue, List<Product_Array> mProduct_arrays, viewProduct viewProd) {
        this.context = context;
        this.viewProd = viewProd;
        this.mProduct_arrays = mProduct_arrays;
        this.jsonValue = jsonValue;
        this.mProducrtDelete = mProducrtDelete;
        Product_Array_List = new ArrayList<Product_Array>();
    }


    @NonNull
    @Override
    public CustomViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_item_viewcart, null, false);

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProd.onViewItemClick(productID, productname, catName, catImg, productqty, productRate, productUnit);
            }
        });

        return new CustomViewAdapter.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(CustomViewAdapter.MyViewHolder holder, int position) {


        Product_Array mProductArray = mProduct_arrays.get(position);
        final int positions = holder.getBindingAdapterPosition();
        if (!mProductArray.getProductqty().toString().equals("0")) {

            quntaity = Integer.valueOf(mProductArray.getProductqty());
            price = Integer.valueOf(mProductArray.getProductRate());

            productqty = Integer.valueOf(mProductArray.getProductqty());
            holder.txtName.setText(mProductArray.getProductname());
            holder.txtCatName.setText(mProductArray.getCatName());
            holder.txtPrice.setText("Total :" + mProductArray.getProductRate());
            holder.txtQty.setText("Qty :" + mProductArray.getProductqty());
            holder.totalAmount.setText("Total :" + mProduct_arrays.get(position).getProductqty() * mProduct_arrays.get(position).getProductRate());
            Glide.with(context).load(mProductArray.getCatImage()).error(R.drawable.product_logo).into(holder.productImage);
            holder.editCount.setText("" + quntaity);


/*
            holder.editCount.setSelection(holder.editCount.getText().length());
            holder.editCount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!holder.editCount.getText().toString().equals("")) {
                        price = Integer.valueOf(mProduct_arrays.get(position).getProductRate());

                        editCountValue = Integer.valueOf(holder.editCount.getText().toString());
                        productqty = editCountValue;
                        holder.txtQty.setText("Qty :" + editCountValue);
                        subTotalRate = editCountValue * price;
                        holder.totalAmount.setText("Total :" + subTotalRate);


                    } else {
                        holder.totalAmount.setText("Total :" + 0);
                        holder.txtQty.setText("Qty : " + 0
                        );
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });*/

            holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialogBox.showDialog(context, "", "Do you want to delete this product?", "Yes", "No", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {

                            mProduct_arrays.remove(position);
                            notifyItemRemoved(positions);
                            notifyItemRangeChanged(positions, mProduct_arrays.size());

                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {

                        }
                    });
                }
            });
        }
    }

    private JSONArray removeFromJsonArray(JSONArray array, int position) {
        if (array == null) return null;
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            //Excluding the item at position
            if (i != position) {
                try {
                    newArray.put(jsonValue.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return newArray;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mProduct_arrays.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtQty;
        TextView txtPrice;
        TextView totalAmount;
        TextView txtCatName;
        ImageView productImage;
        TextView editCount;
        ImageView deleteProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.item_name);
            txtQty = (TextView) itemView.findViewById(R.id.item_qty);
            txtPrice = (TextView) itemView.findViewById(R.id.item_price);
            txtCatName = (TextView) itemView.findViewById(R.id.item_product_name);
            totalAmount = (TextView) itemView.findViewById(R.id.total_amount);
            productImage = (ImageView) itemView.findViewById(R.id.image_product);
            editCount = (TextView) itemView.findViewById(R.id.edit_qty);
            deleteProduct = (ImageView) itemView.findViewById(R.id.delete_product);

        }
    }
}