package com.milksales.godairy.SFA_Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.ApiClient;
import com.milksales.godairy.Interface.ApiInterface;
import com.milksales.godairy.Interface.UpdateResponseUI;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Adapter.GrnAdapter;
import com.milksales.godairy.SFA_Model_Class.OutletReport_View_Modal;
import com.milksales.godairy.common.DatabaseHandler;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrnListActivity extends AppCompatActivity
        implements View.OnClickListener, UpdateResponseUI {

    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";
    public static TextView tvStartDate, tvEndDate;
    TextView outlet_name, history;
    CircularProgressButton btnSync;

    Common_Class common_class;
    List<OutletReport_View_Modal> OutletReport_View_Modal = new ArrayList<>();
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();
    Type userType;
    Gson gson;
    GrnAdapter mReportViewAdapter;
    RecyclerView invoicerecyclerview;
    Shared_Common_Pref sharedCommonPref;
    DatabaseHandler db;


    public static String TAG = "Invoice_History";
    private DatePickerDialog fromDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_grn_list);


            db = new DatabaseHandler(this);
            gson = new Gson();
            sharedCommonPref = new Shared_Common_Pref(GrnListActivity.this);
            common_class = new Common_Class(this);

            CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
            UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
            common_class.getProductDetails(this);

            outlet_name = findViewById(R.id.outlet_name);
            outlet_name.setText("Hi! " + sharedCommonPref.getvalue(Constants.Distributor_name));

            Log.v("qswq", outlet_name.toString());

            tvStartDate = findViewById(R.id.tvSDate);
            tvEndDate = findViewById(R.id.tvEDate);
            history = findViewById(R.id.txtGrnHistory);
            btnSync = findViewById(R.id.btngrnSync);

            btnSync.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    syncData();
                }
            });

            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(GrnListActivity.this, GrnHistory.class));
                }
            });


            tvStartDate.setOnClickListener(this);
            tvEndDate.setOnClickListener(this);


            invoicerecyclerview = (RecyclerView) findViewById(R.id.grnListRecyclerview);


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

            tvStartDate.setText(Common_Class.GetDatewothouttime());
            tvEndDate.setText(Common_Class.GetDatewothouttime());

            common_class.getDataFromApi(Constants.GetGrn_List, this, false);

            Log.v("qwertq", sharedCommonPref.getvalue(Constants.DistributorERP));
            Log.v("qwertq1", Shared_Common_Pref.Div_Code);
            Log.v("qwertq2",Shared_Common_Pref.Sf_Code);


        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }

    }

    private void syncData() {
        JSONObject jObj = new JSONObject();

        try {
            jObj.put("SFCode", Shared_Common_Pref.Sf_Code);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Log.v("api",apiInterface.toString());
            Call<JsonObject> responseBodyCall =apiInterface.GRNSync(jObj.toString());
            Log.v("divcodepos",Shared_Common_Pref.Sf_Code);

            responseBodyCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            Log.e("JSON_VALUES", response.body().toString());
                            Toast.makeText(GrnListActivity.this,"GRN Data Synced Successfully",Toast.LENGTH_SHORT).show();
                           // finish();
                        } catch (Exception e) {
                            Log.v("error", e.toString());
                        }
                    } else {
                        Log.v("error_text", "Failed");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.v("errormsg", t.toString());
                }
            });
        }
        catch (Exception e){

        }
    }


    void showDatePickerDialog(int pos, TextView tv) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(GrnListActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                String date = ("" + year + "-" + month + "-" + dayOfMonth);

                if (common_class.checkDates(pos == 0 ? date : tvStartDate.getText().toString(), pos == 1 ? date : tvEndDate.getText().toString(), GrnListActivity.this)) {
                    tv.setText(date);
                    if (pos == 0)
                        tvStartDate.setText(date);
                    else
                        tvEndDate.setText(date);
                    common_class.getDataFromApi(Constants.GetGrn_List, GrnListActivity.this, false);
                } else {
                    common_class.showMsg(GrnListActivity.this, "Please select valid date");
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSDate:
                showDatePickerDialog(0, tvStartDate);
                break;
            case R.id.tvEDate:
                showDatePickerDialog(1, tvEndDate);
                break;

        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {

            if (apiDataResponse != null && !apiDataResponse.equals("")) {

                switch (key) {

                    case Constants.GetGrn_List:
                        FilterOrderList.clear();
                        userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
                        }.getType();
                        OutletReport_View_Modal = gson.fromJson(apiDataResponse, userType);
                        if (OutletReport_View_Modal != null && OutletReport_View_Modal.size() > 0) {
                            for (OutletReport_View_Modal filterlist : OutletReport_View_Modal) {
                                //if (filterlist.getOutletCode().equals(Shared_Common_Pref.OutletCode)) {
                                FilterOrderList.add(filterlist);
                                // }
                            }
                        }


                        mReportViewAdapter = new GrnAdapter(GrnListActivity.this, FilterOrderList);

                        invoicerecyclerview.setAdapter(mReportViewAdapter);
                        mReportViewAdapter.notifyDataSetChanged();

                        break;

                }

            }
        } catch (Exception e) {
            Log.v("Invoice History: ", e.getMessage());

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            return true;
        }
        return false;
    }

}