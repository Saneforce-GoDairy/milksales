package com.saneforce.godairy.SFA_Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.PrimaryOrder_History_Adapter;
import com.saneforce.godairy.databinding.ActivityVwallPrimaryOrdersBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class vwAllPrimaryOrders extends AppCompatActivity implements Master_Interface,UpdateResponseUI {
    private ActivityVwallPrimaryOrdersBinding binding;
    private final Context context = this;
    private Common_Class common_class;
    private Shared_Common_Pref sharedCommonPref;
    private RecyclerView recyclerView;
    private LinearLayout ldllDist;
    private TextView txtDist;
    JSONArray jData;
    private DistributerAdapter distributerAdapter;
    String orderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vwall_primary_orders);
        binding = ActivityVwallPrimaryOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView=findViewById(R.id.rvPrimary);
        ldllDist=findViewById(R.id.layout_distributer);
        txtDist=findViewById(R.id.txt_distributor);

        common_class = new Common_Class(this);
        sharedCommonPref = new Shared_Common_Pref(vwAllPrimaryOrders.this);
        common_class.getDb_310Data(Constants.PRIMARY_VIEWALL, vwAllPrimaryOrders.this);
        ldllDist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                common_class.showCommonDialog(common_class.getDistList(), 2, vwAllPrimaryOrders.this);
            }
        });

        loadDistributer(common_class.getDistList(), 2);
    }

    private void loadDistributer(List<Common_Model> distList, int i) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setItemViewCacheSize(20);
        distributerAdapter = new DistributerAdapter(context, distList, 2);
        binding.recyclerView.setAdapter(distributerAdapter);
    }

    public class DistributerAdapter extends RecyclerView.Adapter<DistributerAdapter.ViewHolder> implements Filterable {
        Context context;
        List<Common_Model> distList;
        int mType;
        Activity activity;
        private final List<Common_Model> mFilteredList;
        public MyFilter mFilter;

        public DistributerAdapter(Context context, List<Common_Model> distList, int i) {
            this.context = context;
            this.distList = distList;
            this.mType = i;
            this.mFilteredList = new ArrayList< >();
        }

        @Override
        public Filter getFilter() {
            if (mFilter == null){
                mFilteredList.clear();
                mFilteredList.addAll(this.distList);
                mFilter = new DistributerAdapter.MyFilter(this,mFilteredList);
            }
            return mFilter;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_primary_no_orders_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Common_Model contact = distList.get(holder.getBindingAdapterPosition());
            holder.mCustomerName.setText(contact.getName());
            holder.mMobileNo.setText(contact.getPhone());
        }

        @Override
        public int getItemCount() {
            if (distList == null) return 0;
            return distList.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mCustomerName, mMobileNo;
            LinearLayout mainLayout;

            public ViewHolder(View view) {
                super(view);
                mCustomerName = view.findViewById(R.id.customer_name);
                mMobileNo = view.findViewById(R.id.mobile);
                mainLayout = view.findViewById(R.id.layout);
            }
        }

        private class MyFilter extends Filter {

            private final DistributerAdapter myAdapter;
            private final List<Common_Model> originalList;
            private final List<Common_Model> filteredList;

            private MyFilter(DistributerAdapter myAdapter, List<Common_Model> originalList) {
                this.myAdapter = myAdapter;
                this.originalList = originalList;
                this.filteredList = new ArrayList<>();
            }

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                filteredList.clear();
                final FilterResults results = new FilterResults();
                if (charSequence.length() == 0){
                    filteredList.addAll(originalList);
                }else {
                    final String filterPattern = charSequence.toString().toLowerCase().trim();
                    for ( Common_Model user : originalList){
                        if (user.getName().toLowerCase().contains(filterPattern)){
                            filteredList.add(user);
                        }
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                myAdapter.distList.clear();
                myAdapter.distList.addAll((ArrayList<Common_Model>)filterResults.values);
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 2:
                sharedCommonPref.save(Constants.Route_name, "");
                sharedCommonPref.save(Constants.Route_Id, "");
                txtDist.setText(myDataset.get(position).getName());
                JSONArray jfData=new JSONArray();
                for(int j=0;j<jData.length();j++){
                    try {
                        JSONObject itm=jData.getJSONObject(j);
                        if (itm.getString("Stockist_Code").equalsIgnoreCase(myDataset.get(position).getId()))
                        {
                            jfData.put(itm);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                setHistoryAdapter(jfData);
                break;
        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null && !apiDataResponse.equals("")) {
            switch (key) {
                case Constants.PRIMARY_VIEWALL:
                    JSONArray priArrData = new JSONArray(apiDataResponse);
                    jData=priArrData.getJSONObject(0).getJSONArray("Data");
                    setHistoryAdapter(jData);
                    break;
            }
        }
        } catch (Exception e) {
            Log.v("Invoice History: ", e.getMessage());
        }
    }

    @SuppressLint("SetTextI18n")
    void setHistoryAdapter(JSONArray arr) {
        try {
            JSONArray filterArr = new JSONArray();
            for (int i = 0; i < arr.length(); i++) {
              //  if (Common_Class.isNullOrEmpty(groupType) || groupType.equalsIgnoreCase("All") || groupType.equalsIgnoreCase(arr.getJSONObject(i).getString("category_type"))) {
                    filterArr.put(arr.getJSONObject(i));
                //}
            }

            PrimaryOrder_History_Adapter mReportViewAdapter = new PrimaryOrder_History_Adapter(vwAllPrimaryOrders.this, filterArr, new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    try {
                        JSONObject obj = filterArr.getJSONObject(position);
                        Shared_Common_Pref.TransSlNo = obj.getString("Trans_Sl_No");
                        Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
                        sharedCommonPref.save(Constants.FLAG, "Primary Order");
                        sharedCommonPref.save(Constants.Distributor_Id, obj.getString("Stockist_Code"));
                        sharedCommonPref.save(Constants.DistributorERP, obj.getString("ERPCode"));
                        sharedCommonPref.save(Constants.Distributor_name,obj.getString("CustomerName"));
                        intent.putExtra("Mode", "order_view");
                        intent.putExtra("Order_Values", obj.getString("Order_Value"));
                        intent.putExtra("Invoice_Values", obj.getString("invoicevalues"));
                        //intent.putExtra("No_Of_Items", FilterOrderList.get(position).getNo_Of_items());
                        intent.putExtra("Invoice_Date", obj.getString("Order_Date"));
                        intent.putExtra("NetAmount", obj.getString("NetAmount"));
                        intent.putExtra("Discount_Amount", obj.getString("Discount_Amount"));
                        intent.putExtra("gstn", sharedCommonPref.getvalue(Constants.DistributorGst));
                        startActivity(intent);
                        overridePendingTransition(R.anim.in, R.anim.out);
                    } catch (Exception e) {
                        Log.v("TAG", e.getMessage());
                    }
                }

                @Override
                public void onEditOrder(String orderNo, String cutoff_time, String categoryType) {
                    AdapterOnClick.super.onEditOrder(orderNo, cutoff_time, categoryType);
                    try {
                        if (Common_Class.isNullOrEmpty(cutoff_time)) {
                            common_class.showMsg(vwAllPrimaryOrders.this, "Time UP...");
                        } else {
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            Date d1 = sdf.parse(Common_Class.GetTime());
                            Date d2 = sdf.parse(cutoff_time);
                            long elapsed = d2.getTime() - d1.getTime();
                            if (elapsed >= 0) {
                                sharedCommonPref.clear_pref(Constants.LOC_PRIMARY_DATA);
                                Intent intent = new Intent(vwAllPrimaryOrders.this, PrimaryOrderActivity.class);
                                intent.putExtra(Constants.ORDER_ID, orderNo);
                                intent.putExtra(Constants.CATEGORY_TYPE, categoryType);
                                intent.putExtra("Mode", "order_view");
                                Shared_Common_Pref.TransSlNo = orderNo;
                                startActivity(intent);
                                overridePendingTransition(R.anim.in, R.anim.out);

                            } else {
                                common_class.showMsg(vwAllPrimaryOrders.this, "Time UP...");
                            }
                        }
                    } catch (Exception e) {
                        Log.v("Edit Order ", e.getMessage());
                    }
                }
            });

            recyclerView.setAdapter(mReportViewAdapter);

            double totAmt = 0;
            for (int i = 0; i < filterArr.length(); i++) {
                JSONObject obj = filterArr.getJSONObject(i);
                totAmt += Double.parseDouble(obj.getString("Order_Value"));
            }

            if (totAmt > 0) {
                findViewById(R.id.cvTotParent).setVisibility(View.VISIBLE);
                //txtGrandTotal.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(totAmt));
            } else
                findViewById(R.id.cvTotParent).setVisibility(View.GONE);
        } catch (Exception e) {
            Log.v("TAG", e.getMessage());
        }
    }
}