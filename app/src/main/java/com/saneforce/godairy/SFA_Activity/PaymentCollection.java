package com.saneforce.godairy.SFA_Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

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
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.OutletCategoryFilterAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentCollection extends AppCompatActivity {
    private SharedPreferences CheckInDetails, UserDetails;
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";
    public static final String Tag ="Pending Invoice";
    Shared_Common_Pref shared_common_pref;
    RecyclerView rvBillDets;
    TextView tvACBal,outlet_name,tvDistId;
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.payment_collection);

    CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
    UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

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
                   for(int li=0;li<PndBills.length();li++){
                       JSONObject item = PndBills.getJSONObject(li);
                       totAmt+=item.getDouble("PendAmt");
                   }
                    tvACBal.setText(new DecimalFormat("##0.00").format(totAmt));
                    rvBillDets.setAdapter(new Bills_Adapter(PndBills,R.layout.ada_payprimarybills, PaymentCollection.this));
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
        JSONArray dataList;
        private final int rowLayout;


        public Bills_Adapter(JSONArray data, int rowLayout, Context context) {
            this.dataList = data;
            this.rowLayout = rowLayout;
            this.context = context;


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


                JSONObject item = dataList.getJSONObject(position);
                holder.txInvNo.setText("" + item.getString("InvoiceNo"));
                holder.txInvDate.setText("" + item.getString("InvDate"));
                holder.txInvAmt.setText("" + item.getString("BillAmount"));
                holder.txInvPAmt.setText("" + item.getString("PendAmt"));

            } catch (Exception e) {
                Log.e("Pri.Payment", "adapterProduct: " + e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return dataList.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView txInvNo, txInvAmt,txInvDate,txInvPAmt;
            public CheckBox cbPndBill;

            public MyViewHolder(View view) {
                super(view);
                txInvNo = view.findViewById(R.id.txInvNo);
                txInvAmt = view.findViewById(R.id.txInvAmt);
                txInvDate = view.findViewById(R.id.txInvDate);
                txInvPAmt = view.findViewById(R.id.txInvPAmt);
                cbPndBill = view.findViewById(R.id.cbPndBill);

            }
        }
    }
}