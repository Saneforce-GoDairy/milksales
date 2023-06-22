package com.saneforce.milksales.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Interface.ChildListInterface;
import com.saneforce.milksales.Interface.ParentListInterface;
import com.saneforce.milksales.Model_Class.HeaderCat;
import com.saneforce.milksales.Model_Class.HeaderName;
import com.saneforce.milksales.Model_Class.Product;
import com.saneforce.milksales.Model_Class.Product_Array;
import com.saneforce.milksales.R;

import java.util.ArrayList;
import java.util.List;

public class ParentListAdapter extends RecyclerView.Adapter<ParentListAdapter.MyViewHolder> implements Filterable {

    //private List<Movie> moviesList;
    List<HeaderName> eventInfor;
    List<HeaderName> evenParent;

    private Activity activity;
    ArrayList<String> stringValue;
    ArrayList<Product_Array> dataValue;
    String gTotal;

    ArrayList<String> productName, productCode, productQuantity, productTotalDetails;
    HeaderCat headercat;

    List<Product> eventsArrayList;
    ParentListInterface itemClick;
    String getIdFromChild;

    Integer getPositionValue = 0;
    String productNameValue, productCodeValue;
    Integer productQuantityValue;
    ArrayList<String> mString;
    String catIma, catNam;
    String productUnits, productItemId, ProductunitType;
    List<Common_Model> myDataset;
    String ImageViewUrl = "";
    Integer Value = 0;

    public ParentListAdapter(HeaderCat headercat, List<HeaderName> eventInfor, List<Product> eventsArrayList, Activity activity, ArrayList<String> mString, List<Common_Model> myDataset, ParentListInterface itemClick) {
        this.eventInfor = eventInfor;
        this.headercat = headercat;
        this.activity = activity;
        stringValue = new ArrayList<String>();

        this.eventsArrayList = eventsArrayList;
        this.mString = mString;
        this.evenParent = eventInfor;
        this.itemClick = itemClick;
        this.ProductunitType = ProductunitType;
        dataValue = new ArrayList<>();
        productTotalDetails = new ArrayList<>();
        productCode = new ArrayList<>();
        productName = new ArrayList<>();
        productQuantity = new ArrayList<>();
        this.myDataset = myDataset;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_header_sub_category, null);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onClickParentInter(gTotal, 0, getIdFromChild, getPositionValue, productNameValue, productCodeValue, productQuantityValue, catIma, catNam, productUnits,Value);
                itemClick.onProductUnit(productUnits, productItemId);
                itemClick.ProductImage(ImageViewUrl);

            }
        });
        itemView.setClickable(false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        List<Product> mProducts = evenParent.get(position).getProduct();

        Glide.with(activity)
                .load(evenParent.get(position).getCatImage())
                .error(R.drawable.no_prod)
                .into(holder.subProdcutImage);

        holder.subProdcutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.ProductImage(evenParent.get(position).getCatImage());
            }
        });


        Log.e("PRODUCT_TYPE_VALUE_P", "PRODUCT_TYPE" + myDataset.size());


        holder.subProdcutName.setText(evenParent.get(position).getName());
        if (myDataset.size() != 0) {
            Log.e("PRODUCT_TYPE_VALUE_P", "PRODUCT_TYPE_GSFDG" +

                    myDataset.get(0).getName());
            Log.e("Product_type_id", myDataset.get(0).getId());
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false);
        holder.subProductChild.setLayoutManager(layoutManager);
        holder.subProductChild.setNestedScrollingEnabled(false);
        holder.subProductChild.setItemAnimator(new DefaultItemAnimator());
        ChildListAdapter eventListChildAdapter = new ChildListAdapter(this.activity, mProducts, myDataset, new ChildListInterface() {

            @Override
            public void onClickInterface(String value, int totalValue, String itemID, Integer positionValue, String productName, String productCode, Integer productQuantiy, String productUnit,Integer Value) {
                /*PRODUCT_ITEM_ID_VALUE*/
                getIdFromChild = itemID;
                getPositionValue = positionValue;
                productNameValue = productName;
                productCodeValue = productCode;
                productQuantityValue = productQuantiy;
                holder.subProdcutRate.setText(value);
                Log.e("PRODUCT", String.valueOf(Value));
                catIma = evenParent.get(position).getCatImage();
                catNam = evenParent.get(position).getName();
                productUnits = productUnit;
                Log.e("productUnit", "" + productUnit);
                itemClick.onClickParentInter(value, 0, getIdFromChild, getPositionValue, productNameValue, productCodeValue, productQuantityValue, catIma, catNam, productUnits,Value);


            }

            @Override
            public void onProductUnit(String productSaleUnit, String productItemId) {
                itemClick.onProductUnit(productSaleUnit, productItemId);
            }


        });

        holder.subProductChild.setAdapter(eventListChildAdapter);


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
        return evenParent.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView subProdcutImage;
        TextView subProdcutName, subProdcutRate;
        RecyclerView subProductChild;

        public MyViewHolder(View view) {
            super(view);
            subProdcutImage = (ImageView) itemView.findViewById(R.id.product_header_image);
            subProdcutName = (TextView) itemView.findViewById(R.id.product_header_Name);
            subProdcutRate = (TextView) itemView.findViewById(R.id.product_row_total);
            subProductChild = (RecyclerView) itemView.findViewById(R.id.recycler_child);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    evenParent = eventInfor;
                } else {
                    List<HeaderName> filteredList = new ArrayList<>();
                    for (HeaderName row : eventInfor) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    evenParent = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = evenParent;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Log.e("FILTER_LIST", String.valueOf(filterResults.values));
                mString = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void filterList(ArrayList<HeaderName> filterdNames) {
        this.eventInfor = filterdNames;
        notifyDataSetChanged();
    }

}
