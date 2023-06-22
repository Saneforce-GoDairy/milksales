package com.saneforce.milksales.SFA_Activity;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity_Hap.Dashboard;
import com.saneforce.milksales.Activity_Hap.Dashboard_Two;
import com.saneforce.milksales.Activity_Hap.ERT;
import com.saneforce.milksales.Activity_Hap.Help_Activity;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.Outlet_Report_ProductDetails_Adapter;
import com.saneforce.milksales.SFA_Model_Class.Outlet_Report_Product_Details_Modal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.saneforce.milksales.Activity_Hap.Leave_Request.CheckInfo;

public class Outet_Report_Details extends AppCompatActivity {
    TextView TotalValue, txtProductId, txtProductDate;
    RecyclerView DateRecyclerView;
    String Order_ID, orderDate;
    Outlet_Report_ProductDetails_Adapter mDateReportAdapter;
    ArrayList<Integer> mArrayList;
    List<Outlet_Report_Product_Details_Modal> Product_Details_ModalList;
    Type userType;
    Gson gson;
    /*@Inject
    Retrofit retrofit;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outet__report__details);
        mArrayList = new ArrayList<Integer>();
        TotalValue = findViewById(R.id.total_value);
        txtProductId = findViewById(R.id.txt_product_id);
        txtProductDate = findViewById(R.id.txt_order_Date);
        gson = new Gson();
        //((MyApplication) getApplication()).getNetComponent().inject(this);
        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });
        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                } else
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));


            }
        });

        Intent intent = getIntent();
        Order_ID = intent.getStringExtra("Order_ID");
        orderDate = intent.getStringExtra("OrderDate");
        Log.e("productID1234567,", Order_ID + "    " + orderDate);
        txtProductId.setText(Order_ID);
        txtProductDate.setText(orderDate);
        TotalValue.setText("0");
        DateRecyclerView = (RecyclerView) findViewById(R.id.date_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DateRecyclerView.setLayoutManager(layoutManager);

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });


        ViewDateReport();
    }


    public void ViewDateReport() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code.replace(",", ""));
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("Order_Id", Order_ID);
        Log.e("Report_ValuesMap", QueryString.toString());
        Call<Object> call = service.GetRouteObject(QueryString, "{\"tableName\":\"getoutletviewdetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("MAster_Product_Details", response.body() + "");
                System.out.println("DEtails_ProductResponse" + new Gson().toJson(response.body()));
                userType = new TypeToken<ArrayList<Outlet_Report_Product_Details_Modal>>() {
                }.getType();
                Product_Details_ModalList = gson.fromJson(new Gson().toJson(response.body()), userType);
                double total = 0;
                for (Outlet_Report_Product_Details_Modal ms : Product_Details_ModalList) {
                    total += ms.getValue();
                }
                TotalValue.setText("" + total);
                mDateReportAdapter = new Outlet_Report_ProductDetails_Adapter(Outet_Report_Details.this, Product_Details_ModalList);
                DateRecyclerView.setAdapter(mDateReportAdapter);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    onSuperBackPressed();
                }
            });

    public void onSuperBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onBackPressed() {

    }
}