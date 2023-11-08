package com.saneforce.godairy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.Nearby_Outlets;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.FruitViewHolder> implements Filterable {

    Master_Interface updateUi;
    private List<Common_Model> contactList;
    private List<Common_Model> contactListFiltered;
    int typeName;

    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;

    Context mContext;

    Shared_Common_Pref shared_common_pref;


    public DataAdapter(List<Common_Model> myDataset, Context context, int type) {
        contactList = myDataset;
        typeName = type;
        contactListFiltered = myDataset;
        if (type == 1000) {
            mContext = context;
        } else {
            updateUi = ((Master_Interface) context);

        }

    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        return new FruitViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FruitViewHolder fruitViewHolder, final int position) {
        if (fruitViewHolder.getBindingAdapterPosition() >= contactListFiltered.size()) return;
        final Common_Model contact = contactListFiltered.get(fruitViewHolder.getBindingAdapterPosition());
        fruitViewHolder.mTextName.setText("" + contact.getName());
        String getAddress = contact.getAddress();
        String getPhone = contact.getPhone();

        if (!isNullOrEmpty(getAddress) && typeName != 1000 && typeName != 6) {
            fruitViewHolder.mTextAddress.setText(contact.getAddress());
            fruitViewHolder.mTextAddress.setVisibility(View.VISIBLE);
        } else {
            fruitViewHolder.mTextAddress.setVisibility(View.GONE);
        }
        if (!isNullOrEmpty(getPhone) && typeName != 6) {
            fruitViewHolder.mTextPhone.setText(contact.getPhone());
            fruitViewHolder.mTextPhone.setVisibility(View.VISIBLE);
        } else {
            fruitViewHolder.mTextPhone.setVisibility(View.GONE);
        }

        if (typeName == 1000) {
            shared_common_pref = new Shared_Common_Pref(mContext);

            fruitViewHolder.checkboxLin.setVisibility(View.VISIBLE);
            fruitViewHolder.cbTextName.setText(contact.getName());
            fruitViewHolder.mTextName.setVisibility(View.GONE);

            if (shared_common_pref.getvalue(Constants.MAP_KEY).equals(contact.getName())) {
                fruitViewHolder.checkBox_select.setChecked(true);
                lastCheckedPos = fruitViewHolder.getBindingAdapterPosition();
                lastChecked = fruitViewHolder.checkBox_select;
            }
        } else if (typeName == 500) {

            fruitViewHolder.mTextPhone.setVisibility(View.VISIBLE);
            fruitViewHolder.mTextAddress.setVisibility(View.VISIBLE);
            fruitViewHolder.tvPerDay.setVisibility(View.VISIBLE);

            fruitViewHolder.mTextPhone.setText("Period : " + contact.getName() + " days");
            fruitViewHolder.mTextAddress.setText("Target : " + contact.getTotal_Ltrs() + " ltrs");
            fruitViewHolder.mTextName.setText("Gift   : " + contact.getQPS_Name());

            float perday = (contact.getTotal_Ltrs() / Float.parseFloat(contact.getName()));
            fruitViewHolder.tvPerDay.setText("Per Day : " + perday + " ltrs");
            //fruitViewHolder.mTextAddress.setTypeface(fruitViewHolder.mTextAddress.getTypeface(), Typeface.BOLD);

            fruitViewHolder.mTextName.setTextColor(Color.parseColor("#72D043"));
        }

        fruitViewHolder.checkBox_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckBox cb = (CheckBox) buttonView;
                int clickedPos = fruitViewHolder.getBindingAdapterPosition();


                if (cb.isChecked()) {
                    if (lastChecked != null) {
                        lastChecked.setChecked(false);
                        contactList.get(lastCheckedPos).setSelected(false);
                    }

                    lastChecked = cb;
                    lastCheckedPos = clickedPos;

                    contactList.get(clickedPos).setSelected(cb.isSelected());

                    shared_common_pref.save(Constants.MAP_KEY, contactList.get(clickedPos).getName());
                    Nearby_Outlets.nearby_outlets.getExploreDr(false);
                } else {
                    lastChecked = null;
                    shared_common_pref.save(Constants.MAP_KEY, "");
                    // Nearby_Outlets.nearby_outlets.getExploreDr(false);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        if (contactListFiltered == null) return 0;
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase().trim().replaceAll("\\s", "");
                List<Common_Model> filteredList = new ArrayList<>();
                List<Common_Model> filteredany = new ArrayList<>();
                for (Common_Model row : contactList) {
                    String sName = row.getName().toLowerCase().trim().replaceAll("\\s", "");
                    String getAddress = (row.getAddress() != null) ? row.getAddress().toLowerCase().trim().replaceAll("\\s", "") : "";
                    String getPhone = (row.getPhone() != null) ? row.getPhone().toLowerCase().trim().replaceAll("\\s", "") : "";
                    if ((";" + sName).contains(";" + charString) || (";" + getAddress).contains(";" + charString) || (";" + getPhone).contains(";" + charString)) {
                        filteredList.add(row);
                    } else if (sName.contains(charString) || getAddress.contains(charString) || getPhone.contains(charString)) {
                        filteredany.add(row);
                    }
                }
                filteredList.addAll(filteredany);
                contactListFiltered = filteredList;
                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Common_Model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class FruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextName, mTextPhone, mTextAddress, Checkboxname;
        LinearLayout checkboxLin, linear_row;
        CheckBox checkBox_select;
        TextView cbTextName, tvPerDay;

        public FruitViewHolder(View v) {
            super(v);
            mTextName = v.findViewById(R.id.txt_name);
            Checkboxname = v.findViewById(R.id.Checkboxname);
            checkBox_select = v.findViewById(R.id.checkBox_select);
            mTextPhone = v.findViewById(R.id.txt_phone);
            mTextAddress = v.findViewById(R.id.txt_address);
            checkboxLin = v.findViewById(R.id.checkboxLin);
            cbTextName = v.findViewById(R.id.Checkboxname);
            linear_row = v.findViewById(R.id.linear_row);
            tvPerDay = v.findViewById(R.id.txt_per_day);
            v.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            try {


                if (typeName == 1000) {

                } else {
                    updateUi.OnclickMasterType(contactListFiltered, this.getBindingAdapterPosition(), typeName);
                }

            } catch (Exception e) {
                Log.v("dataAdapter:click:", e.getMessage());
            }
        }
    }

    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

}
