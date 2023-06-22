package com.saneforce.milksales.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Interface.ChildListInterface;
import com.saneforce.milksales.Model_Class.Product;
import com.saneforce.milksales.Model_Class.Product_Array;
import com.saneforce.milksales.R;

import java.util.ArrayList;
import java.util.List;

public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.MyViewHolder> {

    private List<Product> eventsArrayList;
    private Activity activity;
    ArrayList<Integer> stringValue;
    private long intSum;
    private ChildListInterface itemClick;
    Integer subTotalRate = 0;
    Integer editValue = 0;
    String getItemID, productNameValue, productCodeValue;
    Integer productQuantityValue;
    String listItemId;
    Integer getPositionValue = 0;
    String productUnit = "";
    ArrayList<Product_Array> dataValue;
    Product_Array product_array;
    ArrayList<String> productNameList;
    ArrayList<String> productCodeList;
    ArrayList<String> productQuantityList;
    String ProductunitType;
    List<Common_Model> myDataset;
    Integer Value = 0;

    public ChildListAdapter(Activity activity, List<Product> eventsArrayList, List<Common_Model> myDataset, ChildListInterface itemClick) {
        this.eventsArrayList = eventsArrayList;
        this.activity = activity;
        this.itemClick = itemClick;
        this.ProductunitType = ProductunitType;
        stringValue = new ArrayList<>();
        productCodeList = new ArrayList<>();
        productNameList = new ArrayList<>();
        productQuantityList = new ArrayList<>();
        this.myDataset = myDataset;
        dataValue = new ArrayList<>();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_child_sub_category, null, false);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemClick.onClickInterface(String.valueOf(intSum), 0, listItemId, getPositionValue, productNameValue, productCodeValue, productQuantityValue, productUnit,Value);
                // itemClick.onProductUnit(productUnit, getItemID);
            }
        });
        listItem.setClickable(false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildListAdapter.MyViewHolder holder, int position) {


        Product events = eventsArrayList.get(position);
        getItemID = eventsArrayList.get(position).getId();
        holder.subProdcutChildName.setText(eventsArrayList.get(position).getName());
        holder.subProdcutChildRate.setText("Rs:" + eventsArrayList.get(position).getProductCatCode() + ".00");
        /*   holder.productEdt.setText("" + events.getmQuantity());*/

        holder.unitBox.setText(eventsArrayList.get(position).getProductSaleUnit());
        if (myDataset.size() != 0) {
            if (myDataset.get(0).getId().equals(getItemID)) {
                holder.unitBox.setText("One " + myDataset.get(0).getId());
                Log.e("Category_Value", myDataset.get(0).getId());
                Log.e("Category_Value", getItemID);
                Log.e("Category_Value", String.valueOf(myDataset.size()));
            }

        }



        Log.e("Product_sale_unit", "  " + productUnit);


        if (holder.productEdt.getText().toString().equals("0")) {
            holder.DisableMinus.setVisibility(View.VISIBLE);
            holder.proudctMinus.setVisibility(View.GONE);


        } else {
            holder.proudctMinus.setVisibility(View.VISIBLE);
            holder.DisableMinus.setVisibility(View.GONE);

        }


        holder.unitBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onProductUnit(productUnit, getItemID);
            }
        });

        holder.productEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (holder.productEdt.getText().toString().equals("")) {
                        holder.productEdt.setText("0");
                        Log.e("FOCUS", String.valueOf(hasFocus));
                    }
                } else {
                    if (holder.productEdt.getText().toString().equals("0")) {
                        holder.productEdt.setText("");
                        Log.e("FOCUS", String.valueOf(hasFocus));
                    }
                }
            }
        });


        holder.productEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (!holder.productEdt.getText().toString().equals("")) {
                    holder.proudctMinus.setVisibility(View.VISIBLE);
                    holder.DisableMinus.setVisibility(View.GONE);
                    editValue = Integer.valueOf(holder.productEdt.getText().toString());
                    Log.e("EDIT_VALUE", String.valueOf(editValue));
                    events.setmQuantity(editValue);
                    eventsArrayList.get(position).setmQuantity(Integer.parseInt("" + holder.productEdt.getText()));
                    subTotalRate = eventsArrayList.get(position).getProductCatCode() * events.getmQuantity();


                } else {

                    holder.DisableMinus.setVisibility(View.VISIBLE);
                    holder.proudctMinus.setVisibility(View.GONE);
                    if (holder.productEdt.getText().toString().equals("0")) {
                        holder.productEdt.setText("");
                        holder.DisableMinus.setVisibility(View.VISIBLE);
                        holder.proudctMinus.setVisibility(View.GONE);
                    }else{
                        holder.proudctMinus.setVisibility(View.VISIBLE);
                        holder.DisableMinus.setVisibility(View.GONE);

                    }

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

                getItemID = eventsArrayList.get(position).getId();
                productCodeValue = String.valueOf(eventsArrayList.get(position).getProductCatCode());
                productQuantityValue = eventsArrayList.get(position).getmQuantity();

                if (holder.productEdt.getText().toString().equals("0")) {
                    holder.DisableMinus.setVisibility(View.VISIBLE);
                    holder.proudctMinus.setVisibility(View.GONE);
                }else{
                    holder.proudctMinus.setVisibility(View.VISIBLE);
                    holder.DisableMinus.setVisibility(View.GONE);
                }


                if (dataValue.size() == 0) {
                    subTotalRate = productQuantityValue * Integer.parseInt(productCodeValue);
                    dataValue.add(new Product_Array(getItemID, productNameValue, productQuantityValue, productQuantityValue * Integer.parseInt(productCodeValue), Integer.parseInt(productCodeValue)));

                } else {
                    System.out.println("PRODUCT_Array_SIzeElse" + dataValue.size());
                    int Total_Size = dataValue.size();
                    for (int i = 0; i < Total_Size; i++) {
                        product_array = dataValue.get(i);
                        if (getItemID == product_array.getProductcode()) {
                            System.out.println("Product_Code" + getItemID);
                            System.out.println("Existing_Code" + product_array.getProductcode());
                            System.out.println("Position_Count" + i);
                            dataValue.remove(i);
                            System.out.println("PRODUCT_Array_SIZE_REMOVE" + dataValue.size());
                            Total_Size = Total_Size - 1;
                            System.out.println("AlreadyExist" + product_array.getProductcode());
                        }

                    }
                    dataValue.add(new Product_Array(getItemID, productNameValue, productQuantityValue, productQuantityValue * Integer.parseInt(productCodeValue), Integer.parseInt(productCodeValue)));

                }


                float sum = 0;

                Log.e("PRODUCT_ARRAY_SIZE", String.valueOf(dataValue));
                for (int i = 0; i < dataValue.size(); i++) {
                    sum = sum + dataValue.get(i).getSampleqty();
                    System.out.println("Final_Name" + dataValue.get(i).getProductname() + "Qty" + dataValue.get(i).getSampleqty() + "SampleQty" + dataValue.get(i).getSampleqty());
                    Log.e("PARENT_SUM", String.valueOf(sum));

                }
                Log.e("PARENT_TOTAL_SUM", String.valueOf(sum));


                itemClick.onClickInterface(String.valueOf(sum), 0, getItemID, 0, productNameValue, productCodeValue, productQuantityValue, productUnit,Value);
                Log.e("djfkgsd", "" + String.valueOf(subTotalRate));

            }



        });


        holder.productEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                holder.productEdt.clearFocus();
                Log.e("Editor_Value", "DONE");
                return false;
            }
        });

        holder.productPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.productEdt.clearFocus();
                Value = 1;
                events.addToQuantity();
                holder.productEdt.setText("" + events.getmQuantity());
                getItemID = eventsArrayList.get(position).getId();

                Log.e("GET_ID_VALUE", getItemID);
                productNameValue = eventsArrayList.get(position).getName();

                productCodeValue = String.valueOf(eventsArrayList.get(position).getProductCatCode());

                productQuantityValue = eventsArrayList.get(position).getmQuantity();

                if (dataValue.size() == 0) {
                    subTotalRate = productQuantityValue * Integer.parseInt(productCodeValue);

                    dataValue.add(new Product_Array(getItemID, productNameValue, productQuantityValue, productQuantityValue * Integer.parseInt(productCodeValue), Integer.parseInt(productCodeValue)));
                    Log.e("Parent_product_value", "" + subTotalRate);
                    Log.e("Parent_product_value", "" + dataValue.size());
                } else {
                    System.out.println("PRODUCT_Array_SIzeElse" + dataValue.size());
                    int Total_Size = dataValue.size();
                    for (int i = 0; i < Total_Size; i++) {
                        product_array = dataValue.get(i);
                        if (getItemID == product_array.getProductcode()) {
                            System.out.println("Product_Code" + getItemID);
                            System.out.println("Existing_Code" + product_array.getProductcode());
                            System.out.println("Position_Count" + i);
                            dataValue.remove(i);
                            System.out.println("PRODUCT_Array_SIZE_REMOVE" + dataValue.size());
                            Total_Size = Total_Size - 1;
                            System.out.println("AlreadyExist" + product_array.getProductcode());
                        }

                    }
                    dataValue.add(new Product_Array(getItemID, productNameValue, productQuantityValue, productQuantityValue * Integer.parseInt(productCodeValue), Integer.parseInt(productCodeValue)));

                }


                int sum = 0;

                Log.e("PRODUCT_ARRAY_SIZE", String.valueOf(dataValue));
                for (int i = 0; i < dataValue.size(); i++) {
                    sum = sum + dataValue.get(i).getSampleqty();
                    System.out.println("Final_Name" + dataValue.get(i).getProductname() + "Qty" + dataValue.get(i).getSampleqty() + "SampleQty" + dataValue.get(i).getSampleqty());
                    Log.e("PARENT_SUM", String.valueOf(sum));

                }
                Log.e("PARENT_TOTAL_SUM", String.valueOf(sum));


                itemClick.onClickInterface(String.valueOf(sum), 0, getItemID, getPositionValue, productNameValue, productCodeValue, productQuantityValue, productUnit,Value);
                notifyDataSetChanged();
            }
        });

        holder.proudctMinus.setEnabled(true);
        holder.proudctMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("OnClick", "Click is wroking");

                Value = 1;

                holder.productEdt.clearFocus();
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


                events.removeFromQuantity();
                holder.productEdt.setText("" + events.getmQuantity());


                getItemID = eventsArrayList.get(position).getId();

                productNameValue = eventsArrayList.get(position).getName();

                productCodeValue = String.valueOf(eventsArrayList.get(position).getProductCatCode());

                productQuantityValue = eventsArrayList.get(position).getmQuantity();


                holder.proudctMinus.setEnabled(true);
                Log.e("ZERO", "NOT ZERO");


/*
                    if (dataValue.size() == 0) {
                        subTotalRate = productQuantityValue * Integer.parseInt(productCodeValue);

                        dataValue.add(new Product_Array(getItemID, productNameValue, productQuantityValue, productQuantityValue * Integer.parseInt(productCodeValue), Integer.parseInt(productCodeValue)));
                        Log.e("Parent_product_value", "" + subTotalRate);
                        Log.e("Parent_product_value", "" + dataValue.size());
                    }*/


                if (dataValue.size() == 0) {
                    subTotalRate = productQuantityValue * Integer.parseInt(productCodeValue);
                    dataValue.add(new Product_Array(getItemID, productNameValue, productQuantityValue, events.getmQuantity() * Integer.parseInt(productCodeValue), Integer.parseInt(productCodeValue)));
                    Log.e("Parent_product_value", "" + subTotalRate);
                    Log.e("Parent_product_value", "" + dataValue.size());
                } else {
                    System.out.println("PRODUCT_Array_SIzeElse" + dataValue.size());
                    int Total_Size = dataValue.size();
                    for (int i = 0; i < Total_Size; i++) {
                        product_array = dataValue.get(i);
                        if (getItemID == product_array.getProductcode()) {
                            System.out.println("Product_Code" + getItemID);
                            System.out.println("Existing_Code" + product_array.getProductcode());
                            System.out.println("Position_Count" + i);
                            dataValue.remove(i);
                            System.out.println("PRODUCT_Array_SIZE_REMOVE" + dataValue.size());
                            Total_Size = Total_Size - 1;
                            System.out.println("AlreadyExist" + product_array.getProductcode());
                        }

                    }
                    dataValue.add(new Product_Array(getItemID, productNameValue, productQuantityValue, events.getmQuantity() * Integer.parseInt(productCodeValue), Integer.parseInt(productCodeValue)));


                    int sum = 0;

                    Log.e("PRODUCT_ARRAY_SIZE", String.valueOf(dataValue));
                    for (int i = 0; i < dataValue.size(); i++) {
                        sum = sum + dataValue.get(i).getSampleqty();
                        System.out.println("Final_Name" + dataValue.get(i).getProductname() + "Qty" + dataValue.get(i).getSampleqty() + "SampleQty" + dataValue.get(i).getSampleqty());
                        Log.e("PARENT_SUM", String.valueOf(sum));

                    }
                    Log.e("PARENT_TOTAL_SUM", String.valueOf(sum));


                    itemClick.onClickInterface(String.valueOf(sum), 0, getItemID, getPositionValue, productNameValue, productCodeValue, productQuantityValue, productUnit,Value);
                    notifyDataSetChanged();
                }

                Log.e("DELTETING", "DELETING");

            }

        });

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
        return eventsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subProdcutChildName, subProdcutChildRate, productCount, productItem;
        LinearLayout proudctMinus, productPlus;
        LinearLayout DisableMinus;
        Button unitBox;
        EditText productEdt;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subProdcutChildName = (TextView) itemView.findViewById(R.id.child_product_name);
            subProdcutChildRate = (TextView) itemView.findViewById(R.id.child_product_price);
            productCount = (TextView) itemView.findViewById(R.id.product_count_increment);
            proudctMinus = (LinearLayout) itemView.findViewById(R.id.image_minus);
            DisableMinus = (LinearLayout) itemView.findViewById(R.id.disable_minus);
            productPlus = (LinearLayout) itemView.findViewById(R.id.image_plus);
            productEdt = (EditText) itemView.findViewById(R.id.edt_product_count_inc_dec);
            unitBox = itemView.findViewById(R.id.edt_unit);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

}