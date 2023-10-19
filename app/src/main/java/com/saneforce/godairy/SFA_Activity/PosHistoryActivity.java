package com.saneforce.godairy.SFA_Activity;

import static com.saneforce.godairy.Common_Class.Constants.Rout_List;
import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.PosOrder_History_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PosHistoryActivity extends AppCompatActivity implements Master_Interface, View.OnClickListener, UpdateResponseUI {

    TextView tvStartDate, tvEndDate, distributor_text, route_text, tvGrandTot;
    Common_Class common_class;

    PosOrder_History_Adapter mReportViewAdapter;
    RecyclerView invoicerecyclerview;
    Shared_Common_Pref sharedCommonPref;
    public static String stDate = "", endDate = "";
    DatePickerDialog fromDatePickerDialog;
    String date = "";
    LinearLayout llDistributor, btnCmbRoute;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    Common_Model Model_Pojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_pos_history);
            sharedCommonPref = new Shared_Common_Pref(PosHistoryActivity.this);
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

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);
            common_class.getDataFromApi(Constants.GetPosOrderHistory, this, false);


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

        } catch (Exception e) {

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
                sharedCommonPref.save(Constants.DivERP,myDataset.get(position).getDivERP());
                sharedCommonPref.save(Constants.DistributorERP, myDataset.get(position).getCont());
                sharedCommonPref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());
                sharedCommonPref.save(Constants.Distributor_phone, myDataset.get(position).getPhone());
                sharedCommonPref.save(Constants.DistributorGst,myDataset.get(position).getDisGst());
                sharedCommonPref.save(Constants.DistributorFSSAI,myDataset.get(position).getDisFssai());

                sharedCommonPref.save(Constants.CusSubGrpErp, myDataset.get(position).getCusSubGrpErp());

                common_class.getDataFromApi(Constants.GetPosOrderHistory, PosHistoryActivity.this, false);
                common_class.getDb_310Data(Rout_List, this);
                //common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);

                break;
            case 3:
                route_text.setText(myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Route_name, myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Route_Id, myDataset.get(position).getId());
                //common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);

                break;
        }
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
        fromDatePickerDialog = new DatePickerDialog(PosHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (common_class.checkDates(date, tvEndDate.getText().toString(), PosHistoryActivity.this) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        stDate = tvStartDate.getText().toString();
                        common_class.getDataFromApi(Constants.GetPosOrderHistory, PosHistoryActivity.this, false);
                    } else
                        common_class.showMsg(PosHistoryActivity.this, "Please select valid date");
                } else {
                    if (common_class.checkDates(tvStartDate.getText().toString(), date, PosHistoryActivity.this) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        endDate = tvEndDate.getText().toString();
                        common_class.getDataFromApi(Constants.GetPosOrderHistory, PosHistoryActivity.this, false);

                    } else
                        common_class.showMsg(PosHistoryActivity.this, "Please select valid date");

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

                switch (key) {
                    case Rout_List:
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
                        break;

                    case Constants.GetPosOrderHistory:

                        JSONArray arr = new JSONArray(apiDataResponse);

                        mReportViewAdapter = new PosOrder_History_Adapter(PosHistoryActivity.this, arr, apiDataResponse, new AdapterOnClick() {
                            @Override
                            public void onIntentClick(int position) {
                                Log.v("POSorder: ", arr.toString());
                                try {
                                    JSONObject obj = arr.getJSONObject(position);
                                    Shared_Common_Pref.TransSlNo = obj.getString("Trans_Sl_No");
                                    Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
                                    sharedCommonPref.save(Constants.FLAG, "POS INVOICE");
                                    sharedCommonPref.save(Constants.Retailor_Name_ERP_Code,obj.getString("Cus_Name"));
                                    sharedCommonPref.save(Constants.Retailor_PHNo,obj.getString("Cust_Mobie_No"));
                                    intent.putExtra("Order_Values", obj.getString("Order_Value"));
                                    intent.putExtra("Invoice_Values", obj.getString("invoicevalues"));
                                    //intent.putExtra("No_Of_Items", FilterOrderList.get(position).getNo_Of_items());
                                    intent.putExtra("Invoice_Date", obj.getString("Order_Date"));
                                    intent.putExtra("NetAmount", obj.getString("NetAmount"));
                                    intent.putExtra("Discount_Amount", obj.getString("Discount_Amount"));
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.in, R.anim.out);
                                } catch (Exception e) {
                                    Log.v("primary:", e.getMessage());
                                }

                            }
                        });
                        invoicerecyclerview.setAdapter(mReportViewAdapter);

                        double totAmt = 0;
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            totAmt += Double.parseDouble(obj.getString("Order_Value"));
                        }

                        if (totAmt > 0) {
                            findViewById(R.id.cvTotParent).setVisibility(View.VISIBLE);
                            tvGrandTot.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(totAmt));
                        } else
                            findViewById(R.id.cvTotParent).setVisibility(View.GONE);
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
            common_class.CommonIntentwithFinish(POSActivity.class);
            overridePendingTransition(R.anim.in, R.anim.out);
            return true;
        }
        return false;
    }

}