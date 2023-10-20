package com.milksales.godairy.Status_Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.Common_Class.Common_Model;
import com.milksales.godairy.Interface.Master_Interface;
import com.milksales.godairy.R;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.FruitViewHolder> implements Filterable {
    List<Common_Model> contactList;
    Master_Interface updateUi;
    int typeName;
    private List<Common_Model> contactListFiltered;

    public DataAdapter(List<Common_Model> myDataset, Context context, int type) {
        contactList = myDataset;
        typeName = type;
        contactListFiltered = myDataset;
        updateUi = ((Master_Interface) context);
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        FruitViewHolder vh = new FruitViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder fruitViewHolder, int i) {
        Common_Model contact = contactListFiltered.get(i);
        fruitViewHolder.mTextName.setText(contact.getName());
        String getAddress = contact.getAddress();
        String getPhone = contact.getPhone();
        if (typeName == -1) {
            Log.e("ADAPTER_SELECTED", String.valueOf(contact.isSelected()));
            if (contact.isSelected() == true) {
                fruitViewHolder.checkBox_select.setChecked(true);
            }
            fruitViewHolder.Checkboxname.setText(contact.getName());
            fruitViewHolder.checkboxLin.setVisibility(View.VISIBLE);
            fruitViewHolder.linear_row.setVisibility(View.GONE);
        }
        if (!isNullOrEmpty(getAddress)) {
            fruitViewHolder.mTextAddress.setText(contact.getAddress());
            fruitViewHolder.mTextAddress.setVisibility(View.VISIBLE);
        } else {
            fruitViewHolder.mTextAddress.setVisibility(View.GONE);
        }
        if (!isNullOrEmpty(getPhone)) {
            fruitViewHolder.mTextPhone.setText(contact.getPhone());
            fruitViewHolder.mTextPhone.setVisibility(View.VISIBLE);
        } else {
            fruitViewHolder.mTextPhone.setVisibility(View.GONE);
        }
        fruitViewHolder.checkBox_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fruitViewHolder.checkBox_select.isChecked()) {
                    contactListFiltered.get(i).setSelected(true);
                    System.out.println("THIRUMALAIVASAN" + i);
                    updateUi.OnclickMasterType(contactListFiltered, i, 1);

                } else if (!fruitViewHolder.checkBox_select.isChecked()) {
                    contactListFiltered.get(i).setSelected(false);
                    updateUi.OnclickMasterType(contactListFiltered, i, 0);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        int siz = 0;

        siz = contactListFiltered.size();

        return siz;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.e("FIlter_VAlues", charString);
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Common_Model> filteredList = new ArrayList<>();
                    for (Common_Model row : contactList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().trim().replaceAll("\\s", "").contains(charString.toLowerCase().trim().replaceAll("\\s", ""))) {
                            filteredList.add(row);
                            Log.e("FIlter_Rowvalues", String.valueOf(row.getName().toLowerCase()));
                        }
                    }
                    contactListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Common_Model>) filterResults.values;
                Log.e("FILTERED_RESULT", String.valueOf(contactListFiltered.size()));
                notifyDataSetChanged();
            }
        };

    }

    public class FruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextName, mTextPhone, mTextAddress, Checkboxname;
        LinearLayout checkboxLin, linear_row;
        CheckBox checkBox_select;

        public FruitViewHolder(View v) {
            super(v);
            mTextName = v.findViewById(R.id.txt_name);
            Checkboxname = v.findViewById(R.id.Checkboxname);
            checkBox_select = v.findViewById(R.id.checkBox_select);
            mTextPhone = v.findViewById(R.id.txt_phone);
            mTextAddress = v.findViewById(R.id.txt_address);
            checkboxLin = v.findViewById(R.id.checkboxLin);
            linear_row = v.findViewById(R.id.linear_row);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            updateUi.OnclickMasterType(contactListFiltered, this.getBindingAdapterPosition(), typeName);
        }
    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

}
