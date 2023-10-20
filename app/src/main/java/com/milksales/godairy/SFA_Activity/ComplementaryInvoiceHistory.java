package com.milksales.godairy.SFA_Activity;

import static com.milksales.godairy.Common_Class.Constants.Rout_List;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Common_Model;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.ApiClient;
import com.milksales.godairy.Interface.ApiInterface;
import com.milksales.godairy.Interface.Master_Interface;
import com.milksales.godairy.Interface.UpdateResponseUI;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Adapter.ComplementaryInvoiceHistoryAdapter;
import com.milksales.godairy.SFA_Adapter.PosOrder_History_Adapter;
import com.milksales.godairy.SFA_Model_Class.ComplementaryInvoiceHistoryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplementaryInvoiceHistory extends AppCompatActivity implements Master_Interface, View.OnClickListener, UpdateResponseUI {

    public static String stDate = "", endDate = "";
    TextView tvStartDate, tvEndDate, distributor_text, route_text, tvGrandTot;
    Common_Class common_class;
    PosOrder_History_Adapter mReportViewAdapter;
    RecyclerView invoicerecyclerview;
    Shared_Common_Pref sharedCommonPref;
    DatePickerDialog fromDatePickerDialog;
    String date = "";
    LinearLayout llDistributor, btnCmbRoute;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    Common_Model Model_Pojo;

    Context context = this;
    ArrayList<ComplementaryInvoiceHistoryModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_complementary_invoice_history);
            sharedCommonPref = new Shared_Common_Pref(ComplementaryInvoiceHistory.this);
            common_class = new Common_Class(this);

            tvStartDate = findViewById(R.id.tvStartDate);
            tvEndDate = findViewById(R.id.tvEndDate);
            invoicerecyclerview = (RecyclerView) findViewById(R.id.invoicerecyclerview);
            distributor_text = findViewById(R.id.distributor_text);
            llDistributor = findViewById(R.id.llDistributor);
            btnCmbRoute = findViewById(R.id.btnCmbRoute);
            route_text = findViewById(R.id.route_text);
            tvGrandTot = findViewById(R.id.txtTotAmt);

            tvStartDate.setOnClickListener(this);
            tvEndDate.setOnClickListener(this);
            llDistributor.setOnClickListener(this);
            btnCmbRoute.setOnClickListener(this);

            stDate = Common_Class.GetDatewothouttime();
            endDate = Common_Class.GetDatewothouttime();
            tvStartDate.setText(stDate);
            tvEndDate.setText(endDate);

            list = new ArrayList<>();

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);
//            common_class.getDataFromApi(Constants.GetPosOrderHistory, this, false);
            getDataFromAPI();


            if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
                llDistributor.setEnabled(false);
                btnCmbRoute.setVisibility(View.GONE);
                findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);
                distributor_text.setText("HI! " + sharedCommonPref.getvalue(Constants.Distributor_name, ""));
            } else {
                if (!sharedCommonPref.getvalue(Constants.Distributor_Id).equals("")) {
                    common_class.getDb_310Data(Rout_List, this);
                    distributor_text.setText(/*"Hi! " +*/ sharedCommonPref.getvalue(Constants.Distributor_name, ""));
                } else {
                    btnCmbRoute.setVisibility(View.GONE);
                }
            }

        } catch (Exception ignored) {

        }

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 2:
                route_text.setText("");
                sharedCommonPref.save(Constants.Route_name, "");
                sharedCommonPref.save(Constants.Route_Id, "");
                // btnCmbRoute.setVisibility(View.VISIBLE);
                distributor_text.setText(myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Distributor_name, myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Distributor_Id, myDataset.get(position).getId());
                sharedCommonPref.save(Constants.DivERP, myDataset.get(position).getDivERP());
                sharedCommonPref.save(Constants.DistributorERP, myDataset.get(position).getCont());
                sharedCommonPref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());
                sharedCommonPref.save(Constants.Distributor_phone, myDataset.get(position).getPhone());
                sharedCommonPref.save(Constants.CusSubGrpErp, myDataset.get(position).getCusSubGrpErp());

//                common_class.getDataFromApi(Constants.GetPosOrderHistory, ComplementaryInvoiceHistory.this, false);
                getDataFromAPI();
 //               common_class.getDb_310Data(Rout_List, this);
 //               common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);

                break;
            case 3:
                route_text.setText(myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Route_name, myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Route_Id, myDataset.get(position).getId());
//                common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);

                break;
        }
    }

    private void getDataFromAPI() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_complementary_invoice_history");
        params.put("fromdate", ComplementaryInvoiceHistory.stDate);
        params.put("todate", ComplementaryInvoiceHistory.endDate);
        params.put("OutletCode", Shared_Common_Pref.OutletCode);

        Call<ResponseBody> call = apiInterface.getUniversalData(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() == null) {
                            Toast.makeText(context, "Response is Null", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray array = jsonObject.getJSONArray("response");
                            LoadResponse(array);
                            Log.e("status", "Request Result: \n" + jsonObject);
                        } else {
                            Toast.makeText(context, "Request does not reached the server", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error while parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Response Not Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStartDate:
                selectDate(1);
                break;
            case R.id.tvEndDate:
                selectDate(2);
                break;
            case R.id.btnCmbRoute:
                if (FRoute_Master != null && FRoute_Master.size() > 1) {
                    common_class.showCommonDialog(FRoute_Master, 3, this);
                }
                break;
            case R.id.llDistributor:
                common_class.showCommonDialog(common_class.getDistList(), 2, this);
                break;


        }
    }

    void selectDate(int val) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(ComplementaryInvoiceHistory.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (common_class.checkDates(date, tvEndDate.getText().toString(), ComplementaryInvoiceHistory.this) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        stDate = tvStartDate.getText().toString();
//                        common_class.getDataFromApi(Constants.GetPosOrderHistory, ComplementaryInvoiceHistory.this, false);
                        getDataFromAPI();
                    } else
                        common_class.showMsg(ComplementaryInvoiceHistory.this, "Please select valid date");
                } else {
                    if (common_class.checkDates(tvStartDate.getText().toString(), date, ComplementaryInvoiceHistory.this) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        endDate = tvEndDate.getText().toString();
//                        common_class.getDataFromApi(Constants.GetPosOrderHistory, ComplementaryInvoiceHistory.this, false);
                        getDataFromAPI();

                    } else
                        common_class.showMsg(ComplementaryInvoiceHistory.this, "Please select valid date");

                }


            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    public void loadroute() {

        if (FRoute_Master.size() == 1) {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.INVISIBLE);
            route_text.setText(FRoute_Master.get(0).getName());
            sharedCommonPref.save(Constants.Route_name, FRoute_Master.get(0).getName());
            sharedCommonPref.save(Constants.Route_Id, FRoute_Master.get(0).getId());

        } else {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null && !apiDataResponse.equals("")) {
                if (Rout_List.equals(key)) {
                    JSONArray routeArr = new JSONArray(apiDataResponse);
                    FRoute_Master.clear();
                    for (int i = 0; i < routeArr.length(); i++) {
                        JSONObject jsonObject1 = routeArr.getJSONObject(i);
                        String id = String.valueOf(jsonObject1.optInt("id"));
                        String name = jsonObject1.optString("name");
                        String flag = jsonObject1.optString("FWFlg");
                        Model_Pojo = new Common_Model(id, name, flag);
                        Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                        FRoute_Master.add(Model_Pojo);

                    }
                    loadroute();
                }
            }
        } catch (Exception e) {
            Log.v("Invoice History: ", e.getMessage());
        }
    }

    private void LoadResponse(JSONArray array) {
        try {
            list.clear();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String invoice = object.getString("Trans_Inv_Slno");
                String type = object.getString("Type");
                String dateTime = object.getJSONObject("Invoice_Date").getString("date");
                String total = object.getString("Total");
                list.add(new ComplementaryInvoiceHistoryModel(invoice, type, dateTime, total));
            }
            if (list.isEmpty()) {
                Toast.makeText(context, "No Complementary Invoices found for selected time period", Toast.LENGTH_SHORT).show();
            }
            ComplementaryInvoiceHistoryAdapter adapter = new ComplementaryInvoiceHistoryAdapter(context, list);
            invoicerecyclerview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            invoicerecyclerview.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

}