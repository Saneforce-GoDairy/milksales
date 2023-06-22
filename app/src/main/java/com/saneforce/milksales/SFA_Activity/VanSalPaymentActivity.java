package com.saneforce.milksales.SFA_Activity;

import static com.saneforce.milksales.SFA_Activity.HAPApp.CurrencySymbol;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class VanSalPaymentActivity extends AppCompatActivity implements UpdateResponseUI {

    Common_Class common_class;
    TextView tvDt, tvLoadAmt, tvUnLoadAmt, tvTotVanSal;
    NumberFormat formatter = new DecimalFormat("##0.00");
    RecyclerView rvVanSales;
    private double salAmt;
    private double totStkAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_van_sal_payment);

        common_class = new Common_Class(this);

        tvLoadAmt = findViewById(R.id.tvLoadStkAmt);
        tvUnLoadAmt = findViewById(R.id.tvUnLoadStkAmt);
        tvDt = findViewById(R.id.tvVSPayDate);
        rvVanSales = findViewById(R.id.rvVanSal);
        tvTotVanSal = findViewById(R.id.tvTotSal);
        tvDt.setText("Date : " + Common_Class.GetDatemonthyearformat());

        totStkAmt = getIntent().getDoubleExtra("stkLoadAmt", -1);

        if (totStkAmt == -1) {
            common_class.getDb_310Data(Constants.VAN_STOCK, this);
        } else {
            tvLoadAmt.setText(CurrencySymbol+" " + formatter.format(totStkAmt));

        }

        common_class.getDataFromApi(Constants.VanSalOrderList, this, false);

        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);


    }


    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            switch (key) {
                case Constants.VanSalOrderList:
                    Log.v(key, apiDataResponse);
                    setHistoryAdapter(apiDataResponse);
                    break;
                case Constants.VAN_STOCK:
                    JSONObject stkObj = new JSONObject(apiDataResponse);
                    totStkAmt = 0;
                    if (stkObj.getBoolean("success")) {
                        JSONArray arr = stkObj.getJSONArray("Data");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            totStkAmt += obj.getDouble("CrAmt");

                        }
                    }

                    tvLoadAmt.setText(CurrencySymbol+" " + formatter.format(totStkAmt));
                    tvUnLoadAmt.setText(CurrencySymbol+" " + formatter.format(totStkAmt - salAmt));

                    break;

            }
        } catch (Exception e) {

        }
    }

    private void setHistoryAdapter(String apiDataResponse) {
        try {

            JSONArray arr = new JSONArray(apiDataResponse);

            if (!Common_Class.isNullOrEmpty(apiDataResponse) && !apiDataResponse.equalsIgnoreCase("[]")) {

                JSONArray filterArr = new JSONArray();
                salAmt = 0;

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    if (obj.getString("Status") != null && obj.getString("Status").equalsIgnoreCase("VANSALES")) {
                        salAmt += obj.getDouble("Order_Value");
                        filterArr.put(obj);
                    }
                }
                tvTotVanSal.setText(CurrencySymbol+" "+ formatter.format(salAmt));

                tvUnLoadAmt.setText(CurrencySymbol+" " + formatter.format(totStkAmt - salAmt));
                rvVanSales.setAdapter(new Pay_Adapter(filterArr, R.layout.adapter_vansales_pay, VanSalPaymentActivity.this));
            }
        } catch (Exception e) {
            Log.v("adap:", e.getMessage());
        }

    }

    public class Pay_Adapter extends RecyclerView.Adapter<Pay_Adapter.MyViewHolder> {
        Context context;
        private JSONArray arr;
        private int rowLayout;


        public Pay_Adapter(JSONArray arr, int rowLayout, Context context) {
            this.arr = arr;
            this.rowLayout = rowLayout;
            this.context = context;


        }

        @Override
        public Pay_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new Pay_Adapter.MyViewHolder(view);
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
        public void onBindViewHolder(Pay_Adapter.MyViewHolder holder, int position) {
            try {


                JSONObject obj = arr.getJSONObject(position);

                holder.tvSNo.setText("" + (position + 1));

                holder.tvInvNo.setText("" + obj.getString("OrderNo"));

                holder.tvAmt.setText(CurrencySymbol+" " + formatter.format(obj.getDouble("Order_Value")));


            } catch (Exception e) {
                Log.e("adapterProduct: ", e.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return arr.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvSNo, tvInvNo, tvAmt;


            public MyViewHolder(View view) {
                super(view);
                tvSNo = view.findViewById(R.id.tvSNo);
                tvInvNo = view.findViewById(R.id.tvInvNum);
                tvAmt = view.findViewById(R.id.tvAmount);

            }
        }


    }

}