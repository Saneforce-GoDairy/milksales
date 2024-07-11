package com.saneforce.godairy.SFA_Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Activity_Hap.SFA_Activity;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Model_Class.PaymentModel;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.PaymentAdapter;
import com.saneforce.godairy.SFA_Model_Class.Retailer_Modal_List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VanSalePaymentNewActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    private static final String TAG = VanSalePaymentNewActivity.class.getSimpleName();
    public static final int CASH_PAYMENT_MODE = 1;
    List<Common_Model> FRetailer_Master = new ArrayList<>();
    List<Common_Model> FPayment_Mode = new ArrayList<>();
    List<Retailer_Modal_List> Retailer_Modal_List = new ArrayList<>();
    List<PaymentModel> paymentList = new ArrayList<>();
    TextView tv_selected_retailer;
    TextView tv_amount_label;
    String selectedRetailerId = "";
    String routeId = "";
    String stockistId = "";
    int paymentModeId = 0;
    int activityFromSec=0;
    Double amt=0.0;

    //  DBController dbController;
    EditText et_received_amt, et_ref_no, et_bank_name;//et_collected_by
    TextView tv_payment_mode, tv_pay_date, tv_advance_pay, tv_refund_amt, btn_submit;//, tv_dist_name ,btn_refund
    CardView cv_ref_no, cv_bank_name;
    LinearLayout ll_ref_no, ll_bank_name, ll_advance_pay, ll_refund;
    RecyclerView rv_pending_payment;
    PaymentAdapter paymentAdapter;
    // Button btn_submit,btn_refund;
    String sf_name;
    String pay_date = "";
    public static Shared_Common_Pref shared_common_pref;
    public static Common_Class common_class;
    Gson gson;
    Type userTypeRetailor;
    Common_Model Model_Pojo;
    Switch swRefund;
    Double advanceAmt=0.0;
    ImageView  ivToolbarHome;
    String ekey = "";
    NumberFormat formatter = new DecimalFormat("##0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vansale_payment_new);
        init();
    }

    public void init() {
        CardView cv_selected_retailer = findViewById(R.id.cv_selected_retailer);
        tv_selected_retailer = findViewById(R.id.tv_selected_retailer);
        et_received_amt = findViewById(R.id.et_received_amt);
        tv_payment_mode = findViewById(R.id.tv_payment_mode);
        et_ref_no = findViewById(R.id.et_ref_no);
        et_bank_name = findViewById(R.id.et_bank_name);
        //   et_collected_by = view.findViewById(R.id.et_collected_by);
        //   tv_dist_name = view.findViewById(R.id.tv_dist_name);
        tv_pay_date = findViewById(R.id.tv_payment_date);
        tv_advance_pay = findViewById(R.id.tv_advance_pay);
        tv_refund_amt = findViewById(R.id.tv_refund_amt);
        cv_ref_no = findViewById(R.id.cv_ref_no);
        cv_bank_name = findViewById(R.id.cv_bank_name);
        ll_ref_no = findViewById(R.id.ll_ref_no);
        ll_bank_name = findViewById(R.id.ll_bank_name);

        ll_advance_pay = findViewById(R.id.ll_advance_pay);
        ll_refund = findViewById(R.id.ll_refund);
        rv_pending_payment = findViewById(R.id.rv_pending_payment);
        btn_submit = findViewById(R.id.btn_submit);
        // btn_refund = findViewById(R.id.btn_refund);
        swRefund = findViewById(R.id.swRefund);
        ivToolbarHome=  findViewById(R.id.toolbar_home);
        tv_amount_label=findViewById(R.id.tv_amount_label);
        LinearLayout llToolbar = findViewById(R.id.ll_toolbar);


        llToolbar.setVisibility(View.VISIBLE);



        // dbController = new DBController(SanSalesApplication.getApplication());
        // currentRsfCode = Constant.getRsfCode("", requireActivity());
        //  routeName = Constant.getResponseFromArrayValue(dbController.getResponse(MY_DAY_PLAN), "ClstrName");
        // sf_name=Constant.getInstance().getSetup(StringConstants.SF_NAME,"",dbController);

        //  getRetailerListFromDB();
        //  getPaymentModeList();
        // getDistributorList();
        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);
        gson = new Gson();
        stockistId = shared_common_pref.getvalue(Constants.Distributor_Id);
        String outletserializableob = shared_common_pref.getvalue(Constants.Retailer_OutletList);

        userTypeRetailor = new TypeToken<ArrayList<Retailer_Modal_List>>() {
        }.getType();
        //   Retailer_Modal_List = gson.fromJson(outletserializableob, userTypeRetailor);
        //  Log.d(TAG, "RetailList" + Retailer_Modal_List.toString());

        if(shared_common_pref.getvalue(Constants.LOGIN_TYPE).equalsIgnoreCase(Constants.DISTRIBUTER_TYPE)) {
            Retailer_Modal_List = gson.fromJson(outletserializableob, userTypeRetailor);
        }


        ekey = shared_common_pref.getvalue(Constants.Distributor_Id)+"-"+Common_Class.getTimeStamp(Common_Class.GetDate(), "yyyy-MM-dd HH:mm:ss");
        Log.d(TAG, "ekey" + ekey);
        getRetailerList();
        getPaymentModeList();
        Intent intent=getIntent();
        if(intent.hasExtra("activityFromSec")){
            activityFromSec=intent.getIntExtra("activityFromSec",0);

        }

            selectedRetailerId=Shared_Common_Pref.OutletCode;
            tv_selected_retailer.setText(shared_common_pref.getvalue(Constants.Retailor_Name_ERP_Code));
            routeId=shared_common_pref.getvalue(Constants.Route_Id);
            paymentList.clear();
            getPendingPaymentDets();
            cv_selected_retailer.clearFocus();





        // cv_selected_retailer.setOnClickListener(this);
        tv_payment_mode.setOnClickListener(this);
        ivToolbarHome.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_pending_payment.setLayoutManager(layoutManager);
        paymentAdapter = new PaymentAdapter(getApplicationContext(), paymentList);
        rv_pending_payment.setAdapter(paymentAdapter);


       /* tv_dist_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_selected_retailer.getText().toString().equals("")) {
                    Toast.makeText(requireActivity(), "Please Select Customer Name", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(requireActivity(), SelectionActivity.class);
                    intent.putExtra("selectionList", distributorList);
                    startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
                }
            }
        });

        et_collected_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_payment_mode.getText().toString().equals("")) {
                    Toast.makeText(requireActivity(), "Please Select Payment Mode", Toast.LENGTH_SHORT).show();
                } else if (paymentModeId != CASH_PAYMENT_MODE && (et_ref_no.getText().toString().equals("") || et_bank_name.getText().toString().equals(""))) {
                    Toast.makeText(requireActivity(), "Please Enter Reference No & Bank Name ", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(requireActivity(), SelectionActivity.class);
                    intent.putExtra("selectionList", collectedbyList);
                    startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
                }

            }
        });*/
        tv_pay_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day, month, year;
                if (!tv_pay_date.getText().toString().equals("")) {
                    String[] dateArray = tv_pay_date.getText().toString().split("-");
                    day = Integer.parseInt(dateArray[0]);
                    month = Integer.parseInt(dateArray[1]) - 1;
                    year = Integer.parseInt(dateArray[2]);
                } else {
                    Calendar c = Calendar.getInstance();

                    day = c.get(Calendar.DAY_OF_MONTH);
                    month = c.get(Calendar.MONTH);
                    year = c.get(Calendar.YEAR);
                }
                DatePickerDialog dialog = new DatePickerDialog(VanSalePaymentNewActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String _year = String.valueOf(year);
                        String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                        String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String _pickedDate = _date + "-" + _month + "-" + _year;
                        Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
                        // currentDate = _date +"/"+_month+"/"+_year;
                        pay_date = _year + "-" + _month + "-" + _date;
                        tv_pay_date.setText(_pickedDate);

                    }
                }, year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }

        });

        et_received_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double recAmt = 0.0;
                if (!editable.toString().equals("")) {
                    recAmt = Double.parseDouble(editable.toString());
                }
                paymentAdapter.setRec_amt(recAmt);
                setRecAmt(recAmt);

            }

        });
        if(swRefund.isChecked()){
            tv_advance_pay.setText("0.0");

            ll_refund.setVisibility(View.VISIBLE);
            amt = Double.parseDouble(et_received_amt.getText().toString()) - getTotalPaidAmt();
            tv_refund_amt.setText("(" + et_received_amt.getText().toString() + "-" + formatter.format(getTotalPaidAmt()) + ")" + " = " + formatter.format(amt));

        }else{
            ll_refund.setVisibility(View.GONE);
        }

        swRefund.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // tv_advance_pay.setText("0.0");

                //  ll_refund.setVisibility(View.VISIBLE);
                //  Double amt = Double.parseDouble(et_received_amt.getText().toString()) - getTotalPaidAmt();
                //  tv_refund_amt.setText("(" + et_received_amt.getText().toString() + "-" + common_class.roundTwoDecimals(getTotalPaidAmt()) + ")" + " = " + common_class.roundTwoDecimals(amt));
                if(swRefund.isChecked()){
                    tv_advance_pay.setText("0.0");

                    ll_refund.setVisibility(View.VISIBLE);
                    amt = Double.parseDouble(et_received_amt.getText().toString()) - getTotalPaidAmt();
                    tv_refund_amt.setText("(" + et_received_amt.getText().toString() + "-" + formatter.format(getTotalPaidAmt()) + ")" + " = " + formatter.format(amt));

                }else{
                    ll_refund.setVisibility(View.GONE);
                    tv_advance_pay.setText(""+advanceAmt);
                }
            }
        });

      /*  btn_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_advance_pay.setText("0.0");

                ll_refund.setVisibility(View.VISIBLE);
                Double amt = Double.parseDouble(et_received_amt.getText().toString()) - getTotalPaidAmt();
                tv_refund_amt.setText("(" + et_received_amt.getText().toString() + "-" + common_class.roundTwoDecimals(getTotalPaidAmt()) + ")" + " = " + common_class.roundTwoDecimals(amt));
            }
        });*/

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_selected_retailer.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Customer Name", Toast.LENGTH_SHORT).show();
                } else if (et_received_amt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), " Enter Amount", Toast.LENGTH_SHORT).show();
                } else if (tv_payment_mode.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), " Please Select Payment Mode", Toast.LENGTH_SHORT).show();
                } else if (paymentModeId != CASH_PAYMENT_MODE && et_ref_no.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), " Enter The Reference Number", Toast.LENGTH_SHORT).show();
                } else if (paymentModeId != CASH_PAYMENT_MODE && et_bank_name.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), " Enter Bank Name", Toast.LENGTH_SHORT).show();
                } else if (tv_pay_date.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Select Pay Date", Toast.LENGTH_SHORT).show();
                }else if(paymentList.size()==0){
                    new AlertDialog.Builder(VanSalePaymentNewActivity.this)
                            .setTitle("Payment Confirmation")
                            .setMessage("Do you want to Pay Amount in Advance?")

                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    saveAdvancePayment();
                                    dialog.dismiss();
                                }
                            })

                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    // if(activityFromSec==1)
                    //Toast.makeText(getApplicationContext(), "No Pending Amount to this Customer", Toast.LENGTH_SHORT).show();
                    //else
                    //  Toast.makeText(getApplicationContext(), "Please Select Valid Customer Name", Toast.LENGTH_SHORT).show();

                } else{
                    // savePaymentDetails();
                    new AlertDialog.Builder(VanSalePaymentNewActivity.this)
                            .setTitle("Payment Confirmation")
                            .setMessage("Do you want to Submit Payment?")

                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    saveVanPaymentDetails();
                                    dialog.dismiss();
                                }
                            })

                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });

    }

    private void getRetailerList() {
        try{
            FRetailer_Master.clear();
            for (int i = 0; i < Retailer_Modal_List.size(); i++) {
                Retailer_Modal_List retailerModel = Retailer_Modal_List.get(i);


                String   id = retailerModel.getId();
                String name = retailerModel.getName();
                String route=retailerModel.getTownCode();
                // String flag = retailerModel.getInvoice_Flag();
                Model_Pojo = new Common_Model(id, name, route);
                // Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                FRetailer_Master.add(Model_Pojo);

            }
            Log.d(TAG, "getRetailerList" + FRetailer_Master.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getPaymentModeList() {

        String templateListResponse = "[{\"id\":\"1\",\"name\":\"CASH\"},{\"id\":\"2\",\"name\":\"CHEQUE\"},{\"id\":\"3\",\"name\":\"CREDIT/DEBIT CARD\"},{\"id\":\"4\",\"name\":\"UPI PAYMENT\"},{\"id\":\"5\",\"name\":\"CHALLAN\"}]";
        Log.d(TAG, "paymenyModeList" + templateListResponse);

        if (templateListResponse != null && !templateListResponse.equals("")) {
            FPayment_Mode.clear();
            try {
                JSONArray jsonArray = new JSONArray(templateListResponse);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    Model_Pojo = new Common_Model(id, name, "");
                    FPayment_Mode.add(Model_Pojo);
                }
                Log.d(TAG, "getpaymentModeList: " +FPayment_Mode.toString() );

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
    private void saveAdvancePayment(){
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("axn", "get/saveadvpayment");
        queryParams.put("divisionCode",Shared_Common_Pref.Div_Code);
        queryParams.put("Advance_Amt",et_received_amt.getText().toString());
        queryParams.put("Customer_Code",selectedRetailerId);
        queryParams.put("stk_code",stockistId);


        Log.d(TAG,"Queryparams"+queryParams.toString());
        ApiInterface apiClient = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiClient.getPendPayDets(queryParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ResponseBody res = response.body();
                Log.d(TAG, "Advance Amount" + res.toString());
                if (res != null && !res.equals("")) {
                    Toast.makeText(getApplicationContext(), "Advance Amount Paid successfully", Toast.LENGTH_SHORT).show();

                    onBackPressed();
                }


            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                Toast.makeText(getApplicationContext(), "Server problem, please try again", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure" + t.toString());

            }
        });
    }


    private void saveVanPaymentDetails() {
        JsonArray jsonArray = new JsonArray();

        JsonObject dailyInventoryObject = new JsonObject();


        JsonObject dailyInventoryDataObject = new JsonObject();

        dailyInventoryDataObject.addProperty("Stockist_Id", stockistId);
        dailyInventoryDataObject.addProperty("Stockist_Name", shared_common_pref.getvalue(Constants.Distributor_name));
        dailyInventoryDataObject.addProperty("Cust_ID", selectedRetailerId);
        dailyInventoryDataObject.addProperty("Cust_Name", tv_selected_retailer.getText().toString());
        dailyInventoryDataObject.addProperty("Total_amount", et_received_amt.getText().toString());
        dailyInventoryDataObject.addProperty("Refund_Amount", amt);
        dailyInventoryDataObject.addProperty("Mode", tv_payment_mode.getText().toString());
        dailyInventoryDataObject.addProperty("Reference_No", et_ref_no.getText().toString());
        dailyInventoryDataObject.addProperty("Bk_name", et_bank_name.getText().toString());
        dailyInventoryDataObject.addProperty("Pay_Date", pay_date + " 00:00:00:00");
        dailyInventoryDataObject.addProperty("collect_by",shared_common_pref.getvalue(Constants.Distributor_name) );
        dailyInventoryDataObject.addProperty("Remark", "");
        dailyInventoryDataObject.addProperty("Advance_pay", tv_advance_pay.getText().toString());
        dailyInventoryDataObject.addProperty("Type", 1);
        dailyInventoryDataObject.addProperty("invoice_no", getPaidInvoiceNo());
        dailyInventoryDataObject.addProperty("Route_code", routeId);
        dailyInventoryDataObject.addProperty("eKey", ekey);

        JsonArray paymentArray = new JsonArray();
        //   Log.d("hjb","ujh"+paymentList.toString());
        for (PaymentModel p : paymentList) {
            //     Log.d("hjb","ujhxccv"+p.getAmt());
            if (p.getAmt() > 0.0) {
                JsonObject jsonObject = new JsonObject();


                jsonObject.addProperty("bill_no", p.getBillNo());

                jsonObject.addProperty("bill_date", p.getBillDate());
                jsonObject.addProperty("bill_amt", p.getBilledAmt());
                jsonObject.addProperty("Pen_amt", p.getPendingAmt());
                jsonObject.addProperty("paid_amt", p.getAmt());
                jsonObject.addProperty("order_no", p.getOrderNo());
                paymentArray.add(jsonObject);

            }
        }
        dailyInventoryDataObject.add("Payment_Details", paymentArray);
        dailyInventoryObject.add("VanPendingPaymentDetailsNative", dailyInventoryDataObject);
        jsonArray.add(dailyInventoryObject);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("axn", "dcr/save");
        queryParams.put("divisionCode", Shared_Common_Pref.Div_Code);
        queryParams.put("sfCode", shared_common_pref.getvalue(Constants.Distributor_Id));
        queryParams.put("sfName",shared_common_pref.getvalue(Constants.Distributor_name) );
        queryParams.put("stateCode", Shared_Common_Pref.StateCode);
        queryParams.put("Type", "Payment");

        Log.d(TAG, "updateLoadingStock: jsonArray " + jsonArray);
        Log.d(TAG, "updateLoadingStock: queryParams " + queryParams);

        ApiInterface apiClient = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiClient.getResponses(Common_Class.toRequestBody(jsonArray), queryParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ResponseBody res = response.body();
                    Log.d("PaymentFragment", "pendingpaymentlist" + res.toString());
                    if (res != null && !res.equals("")) {
                        JSONObject jsonObject2 = new JSONObject(res.string());

                        if (jsonObject2.getBoolean("success")) {
                            Toast.makeText(getApplicationContext(), "Bill Payment Updated successfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(getApplicationContext(), "Server problem, please try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (Exception e) {
                    Log.d("sdf", "gfdhg" + e.getMessage());
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("SubmittedOrderActivity", "onFailure" + t.toString());

            }
        });


    }

    private void getPendingPaymentDets() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("axn", "get/pendingpaymentdets");
        queryParams.put("divisionCode", Shared_Common_Pref.Div_Code);
        queryParams.put("stockist_code", stockistId);
        queryParams.put("Customer_Code", selectedRetailerId);
       /* queryParams.put("From_Year", "2022");
        queryParams.put("To_Year", "2023");
        queryParams.put("From_Month", "06");
        queryParams.put("To_Month", "05");
        queryParams.put("Type", 2);*/
        Log.d("PaymentFragment", "Queryparams" + queryParams.toString());

        ApiInterface apiClient = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiClient.getPendPayDets(queryParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ResponseBody res = response.body();
                    Log.d("PaymentFragment", "pendingpaymentlist" + res.toString());
                    if (res != null && !res.equals("")) {
                        paymentList.clear();
                        try {
                           // JSONArray jsonArray = new JSONArray(res.string());
                            JSONArray jsonArray =new JSONArray(res.string());
                            // JSONObject jsonArray=new JSONObject(res);
                            Log.d("PaymentFragment", "jsonArray" + jsonArray.length());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String bill_no = "", bill_date = "", order_no = "";
                                double billed_amt = 0, pending_amt = 0;


                                if (jsonObject.has("Invoice_No")) {
                                    bill_no = jsonObject.getString("Invoice_No");
                                }
                                if (jsonObject.has("Invoice_Date1")) {
                                    bill_date = jsonObject.getString("Invoice_Date1");
                                }
                                if (jsonObject.has("BillAmt")) {
                                    billed_amt = jsonObject.getDouble("BillAmt");
                                }
                                if (jsonObject.has("Bal_Amt")) {
                                    pending_amt = jsonObject.getDouble("Bal_Amt");
                                }
                                if (jsonObject.has("BillNo")) {
                                    order_no = jsonObject.getString("BillNo");
                                }

                                PaymentModel paymentModel = new PaymentModel(bill_no, bill_date, billed_amt, pending_amt, order_no);
                                paymentList.add(paymentModel);


                            }

                            if (paymentList.size() > 0) {
                                //  ll_header.setVisibility(View.VISIBLE);
                                rv_pending_payment.setVisibility(View.VISIBLE);
                                tv_amount_label.setText(R.string.received_amt);
                            } else {
                                tv_amount_label.setText(R.string.advance_amt);
                                rv_pending_payment.setVisibility(View.GONE);
                            }
                            paymentAdapter.setPaymentList(paymentList);
                            if(!et_received_amt.getText().toString().isEmpty()) {
                                setRecAmt(Double.parseDouble(et_received_amt.getText().toString()));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("SubmittedOrderActivity", "onFailure" + t.toString());

            }
        });


    }




    public void setRecAmt(Double recAmt) {
        double rcamt = recAmt;

        Log.d("sda", "gh" + paymentList.size());
        if (paymentList.size() > 0) {
            for (int i = 0; i < paymentList.size(); i++) {
                if (paymentList.get(i).getPendingAmt() < rcamt) {
                    double amt = rcamt - paymentList.get(i).getPendingAmt();
                    paymentList.get(i).setAmt(paymentList.get(i).getPendingAmt());
                    rcamt = amt;
                } else {
                    paymentList.get(i).setAmt(rcamt);
                    rcamt = 0;
                }
            }


            if (rcamt > 0.0) {
                ll_advance_pay.setVisibility(View.VISIBLE);
                tv_advance_pay.setText("" + formatter.format(rcamt));
                advanceAmt= Double.valueOf(formatter.format(rcamt));
                Double amt = Double.parseDouble(et_received_amt.getText().toString()) - getTotalPaidAmt();
                tv_refund_amt.setText("(" + et_received_amt.getText().toString() + "-" + formatter.format(getTotalPaidAmt()) + ")" + " = " + formatter.format(amt));
            } else {
                ll_advance_pay.setVisibility(View.GONE);
                ll_refund.setVisibility(View.GONE);
            }
            paymentAdapter.setPaymentList(paymentList);
        } else {
            ll_advance_pay.setVisibility(View.GONE);
            ll_refund.setVisibility(View.GONE);
        }
    }


    private String getPaidInvoiceNo() {
        String s = "";
        for (int i = 0; i < paymentList.size(); i++) {
            if (paymentList.get(i).getAmt() > 0) {
                s = s + paymentList.get(i).getBillNo() + ",";
            }
        }
        return s;
    }

    private double getTotalPaidAmt() {
        double amt = 0.0;
        for (int i = 0; i < paymentList.size(); i++) {
            if (paymentList.get(i).getAmt() > 0) {
                amt = amt + paymentList.get(i).getAmt();
            }
        }
        Log.d("PaymentFragment", "getTotalPaidAmt" + amt);
        return amt;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_selected_retailer:
                if (FRetailer_Master != null && FRetailer_Master.size() > 1) {
                    common_class.showCommonDialog(FRetailer_Master, 10, this);
                }
                break;
            case R.id.tv_payment_mode:
                if(FPayment_Mode!=null&& FPayment_Mode.size()>1){
                    common_class.showCommonDialog(FPayment_Mode,11,this);
                }
                break;
            case R.id.toolbar_home:
                common_class.CommonIntentwithoutFinish(SFA_Activity.class);
                break;
        }
    }


    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        if (type == 10) {
            try {
                Log.d("fghg", "gfuk" + myDataset.get(position).getName()+myDataset.get(position).getId());
                Log.d("fghg", "gfu" +myDataset.get(position).getFlag());
                tv_selected_retailer.setText(myDataset.get(position).getName());
                selectedRetailerId= myDataset.get(position).getId();
                routeId = myDataset.get(position).getFlag();
                paymentList.clear();
                getPendingPaymentDets();

            } catch (Exception e) {
                Log.d("fghg", "gfuk1" + e.toString());
            }
        }else if(type==11){
            Log.d("fghg", "gfuk" + myDataset.get(position).getName()+myDataset.get(position).getId());
            tv_payment_mode.setText(myDataset.get(position).getName());
            paymentModeId = Integer.parseInt(myDataset.get(position).getId());
            if (paymentModeId != CASH_PAYMENT_MODE) {
                ll_ref_no.setVisibility(View.VISIBLE);
                ll_bank_name.setVisibility(View.VISIBLE);
            } else {
                ll_ref_no.setVisibility(View.GONE);
                ll_bank_name.setVisibility(View.GONE);
            }
        }
    }
}
