
package com.saneforce.milksales.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Model_Class.DateReport;
import com.saneforce.milksales.Model_Class.DateResult;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.DateReportAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewReportActivity extends AppCompatActivity {
    TextView toolHeader, txtProductId, txtProductDate;
    ImageView imgBack;
    EditText toolSearch;
    RecyclerView DateRecyclerView;
    String productId, orderDate;
    DateReportAdapter mDateReportAdapter;
    ArrayList<Integer> mArrayList;
    TextView TotalValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        mArrayList = new ArrayList<Integer>();
        TotalValue = findViewById(R.id.total_value);
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
                startActivity(new Intent(getApplicationContext(), PayslipFtp.class));
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
                startActivity(new Intent(getApplicationContext(), OrderDashBoard.class));

            }
        });

        Intent intent = getIntent();
        productId = intent.getStringExtra("ProductID");
        orderDate = intent.getStringExtra("OrderDate");
        //Log.e("productID1234567,", productId + "    " + orderDate);

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

        txtProductId = (TextView) findViewById(R.id.txt_product_id);
        txtProductDate = (TextView) findViewById(R.id.txt_order_Date);
        txtProductId.setText(productId);
        txtProductDate.setText(orderDate);
        ViewDateReport();
    }



    public void ViewDateReport() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DateReport> responseBodyCall = apiInterface.dateReport(productId, "27");
        responseBodyCall.enqueue(new Callback<DateReport>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<DateReport> call, Response<DateReport> response) {

                DateReport mReportActivities = response.body();

                List<DateResult> mDReportModels = mReportActivities.getData();

                //Log.e("MdReportModels", String.valueOf(mDReportModels.size()));

                for (int i = 0; i < mDReportModels.size(); i++) {
                    mArrayList.add(((mDReportModels.get(i).getValue())));

                }
                long intSum = (mArrayList.stream()
                        .mapToLong(Integer::longValue)
                        .sum());


                //Log.e("TOTAL_SUM", String.valueOf(intSum));


                TotalValue.setText(String.valueOf(intSum));


                mDateReportAdapter = new DateReportAdapter(ViewReportActivity.this, mDReportModels);

                DateRecyclerView.setAdapter(mDateReportAdapter);
            }

            @Override
            public void onFailure(Call<DateReport> call, Throwable t) {

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