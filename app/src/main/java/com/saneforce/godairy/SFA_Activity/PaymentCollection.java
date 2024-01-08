package com.saneforce.godairy.SFA_Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.google.common.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Activity_Hap.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.AdapterSingleClickListener;
import com.saneforce.godairy.Interface.AdapterTwoClickListener;
import com.saneforce.godairy.Interface.AlertDialogClickListener;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.OutletCategoryFilterAdapter;
import com.saneforce.godairy.SFA_Model_Class.ModelPaymentCollection;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.PaymentCollectionBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentCollection extends AppCompatActivity {
    PaymentCollectionBinding binding;
    private SharedPreferences CheckInDetails, UserDetails;
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";
    public static final String Tag ="Pending Invoice";
    Shared_Common_Pref shared_common_pref;
    RecyclerView rvBillDets;
    TextView tvACBal,outlet_name,tvDistId;
    ArrayList<ModelPaymentCollection> list;
    AssistantClass assistantClass;
    Context context = this;
    boolean isMultipleSelected;
    Bills_Adapter adapter;
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = PaymentCollectionBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
    UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

    isMultipleSelected = false;
    list = new ArrayList<>();
    assistantClass = new AssistantClass(context);
    shared_common_pref = new Shared_Common_Pref(this);
    rvBillDets=findViewById(R.id.rvBillDets);
    tvACBal=findViewById(R.id.tvACBal);
    outlet_name=findViewById(R.id.outlet_name);
    tvDistId=findViewById(R.id.tvDistId);
    //if (shared_common_pref.getvalue(Constants.LOGIN_TYPE).equalsIgnoreCase(Constants.DISTRIBUTER_TYPE)) {
        outlet_name.setText(shared_common_pref.getvalue(Constants.Distributor_name, ""));
        tvDistId.setText("" + shared_common_pref.getvalue(Constants.DistributorERP));
    //    } else {
    //        distributor_text.setText(shared_common_pref.getvalue(Constants.Distributor_name, ""));
    //        distributor_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round_arrow_drop_down_24, 0);
    //        findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);
    //    }
    getPndBills();
    binding.pay.setOnClickListener(v -> {
        Toast.makeText(context, "Multiple Payment Clicked", Toast.LENGTH_SHORT).show();
    });

}

    private void getPndBills() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> rptCall = apiInterface.getData310List("get/pripndbills",
                UserDetails.getString("Divcode", ""),
                shared_common_pref.getvalue(Constants.Distributor_Id),"", "", "","");
        rptCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject res = new JSONObject(String.valueOf(response.body()));
                    Log.d("Pending Bills", String.valueOf(res));
                   JSONArray PndBills= res.getJSONArray("response");
                   double totAmt=0.0;
                   list = new ArrayList<>();
                   for(int li=0;li<PndBills.length();li++){
                       JSONObject item = PndBills.getJSONObject(li);
                       String InvoiceNo = item.optString("InvoiceNo");
                       String InvDate = item.optString("InvDate");
                       double BillAmount = item.optDouble("BillAmount");
                       double PendAmt = item.optDouble("PendAmt");
                       list.add(new ModelPaymentCollection(InvoiceNo, InvDate, BillAmount, PendAmt, false));
                       totAmt+=item.getDouble("PendAmt");
                   }
                    tvACBal.setText(new DecimalFormat("##0.00").format(totAmt));
                    adapter = new Bills_Adapter(list, R.layout.ada_payprimarybills, PaymentCollection.this, new AdapterTwoClickListener() {
                        @Override
                        public void onClickOne(int position) {
                            assistantClass.showAlertDialog(PndBills.optJSONObject(position).optString("InvoiceNo"), "Select a payment mode", true, "Online", "Offline", new AlertDialogClickListener() {
                                @Override
                                public void onPositiveButtonClick(DialogInterface dialog) {
                                    Toast.makeText(context, "Online Payment method selected", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNegativeButtonClick(DialogInterface dialog) {
                                    Toast.makeText(context, "Offline Payment method selected", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onClickTwo(int position) {
                            int checked = 0;
                            for (ModelPaymentCollection item : list) {
                                if (item.isChecked()) {
                                    checked ++;
                                }
                            }
                            isMultipleSelected = checked > 1;
                            binding.pay.setEnabled(isMultipleSelected);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    rvBillDets.setAdapter(adapter);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(Tag, String.valueOf(t));
            }
        });
    }

    public class Bills_Adapter extends RecyclerView.Adapter<Bills_Adapter.MyViewHolder> {
        Context context;
        ArrayList<ModelPaymentCollection> list;
        private final int rowLayout;
        AdapterTwoClickListener listener;

        public Bills_Adapter(ArrayList<ModelPaymentCollection> list, int rowLayout, Context context, AdapterTwoClickListener listener) {
            this.list = list;
            this.rowLayout = rowLayout;
            this.context = context;
            this.listener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                ModelPaymentCollection item = list.get(holder.getBindingAdapterPosition());
                holder.txInvNo.setText(item.getInvoice());
                holder.payNow.setEnabled(!isMultipleSelected);
                holder.txInvDate.setText(item.getInvoiceDate());
                holder.txInvAmt.setText("" + item.getInvoiceAmt());
                holder.txInvPAmt.setText("" + item.getInvoicePAmt());
                holder.payNow.setOnClickListener(v -> listener.onClickOne(holder.getBindingAdapterPosition()));
                holder.cbPndBill.setChecked(item.isChecked());
                holder.cbPndBill.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    item.setChecked(isChecked);
                    listener.onClickTwo(holder.getBindingAdapterPosition());
                });
            } catch (Exception e) {
                Log.e("Pri.Payment", "adapterProduct: " + e.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView txInvNo, txInvAmt,txInvDate,txInvPAmt, payNow;
            public CheckBox cbPndBill;

            public MyViewHolder(View view) {
                super(view);
                txInvNo = view.findViewById(R.id.txInvNo);
                txInvAmt = view.findViewById(R.id.txInvAmt);
                txInvDate = view.findViewById(R.id.txInvDate);
                txInvPAmt = view.findViewById(R.id.txInvPAmt);
                cbPndBill = view.findViewById(R.id.cbPndBill);
                payNow = view.findViewById(R.id.payNow);
            }
        }
    }
}