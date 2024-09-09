package com.saneforce.godairy.SFA_Activity;

import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Activity_Hap.Invoice_Vansales_Select;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.Invoice_History_Adapter;
import com.saneforce.godairy.SFA_Model_Class.OutletReport_View_Modal;
import com.saneforce.godairy.SFA_Model_Class.Product_Details_Modal;
import com.saneforce.godairy.common.DatabaseHandler;
import com.saneforce.godairy.common.LocationFinder;
import com.saneforce.godairy.procurement.custom_form.CustomFormHomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Invoice_History extends AppCompatActivity implements Master_Interface, View.OnClickListener, UpdateResponseUI {

    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";
    public static TextView tvStartDate, tvEndDate;
    TextView outlet_name, lastinvoice, tvOtherBrand, tvQPS, tvPOP, tvCoolerInfo, tvOrder, txRmkTmplSpinn,
            txRmksNoOrd, tvOutstanding, txPrvBal, txSalesAmt, txPayment, tvSalesReturn;
    LinearLayout marketingActivityLayout, lin_order, lin_repeat_order, lin_invoice, lin_complementary, lin_repeat_invoice, lin_noOrder, linNoOrderRmks, linPayment, linRpt,
            linVanSales, linintent, linSalesReturn, linStockRot;
    Common_Class common_class;
    List<OutletReport_View_Modal> OutletReport_View_Modal = new ArrayList<>();
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();
    Type userType;
    Gson gson;
    Invoice_History_Adapter mReportViewAdapter;
    RecyclerView invoicerecyclerview;
    Shared_Common_Pref sharedCommonPref;
    DatabaseHandler db;
    private String[] strLoc;
    private String Worktype_code;
    Location mlocation;
    List<Common_Model> ldgRemarks = new ArrayList<>();
    Button btnSbmtNOrd;
    ImageView btnRmkClose;
    public static String TAG = "Invoice_History";
    private DatePickerDialog fromDatePickerDialog;
    String type = "";

    ImageView swipe_left_image;
    TextView tv_no_data;
    CardView card_date;
int approveFlagValue=-1;
    //Updateed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_invoice__history);
            db = new DatabaseHandler(this);
            gson = new Gson();
            sharedCommonPref = new Shared_Common_Pref(Invoice_History.this);
            common_class = new Common_Class(this);

            new LocationFinder(getApplication(), new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    mlocation = location;
                }
            });

            CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
            UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

//            common_class.ProgressdialogShow(1, "Updating Material Details");
//            common_class.getProductDetails(this, new OnLiveUpdateListener() {
//                @Override
//                public void onUpdate(String mode) {
//
//                    common_class.ProgressdialogShow(0, "");
//                }
//            });

            lin_order = findViewById(R.id.lin_order);
            outlet_name = findViewById(R.id.outlet_name);
            outlet_name.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
            lin_repeat_order = findViewById(R.id.lin_repeat_order);
            lin_invoice = findViewById(R.id.lin_invoice);
            lin_complementary = findViewById(R.id.lin_complementary);
            marketingActivityLayout = findViewById(R.id.marketingActivityLayout);
            lin_repeat_invoice = findViewById(R.id.lin_repeat_invoice);
            lastinvoice = findViewById(R.id.lastinvoice);
            lin_noOrder = findViewById(R.id.lin_noOrder);
            linNoOrderRmks = findViewById(R.id.linNoOrderRmks);
            btnSbmtNOrd = findViewById(R.id.btnSbmtNOrd);
            tvOrder = (TextView) findViewById(R.id.tvOrder);

            tvOrder.setVisibility(View.GONE);
            tvOtherBrand = (TextView) findViewById(R.id.tvOtherBrand);
            tvPOP = (TextView) findViewById(R.id.tvPOP);
            tvQPS = (TextView) findViewById(R.id.tvQPS);
            tvCoolerInfo = (TextView) findViewById(R.id.tvCoolerInfo);
            txRmkTmplSpinn = (TextView) findViewById(R.id.txRmkTmplSpinn);
            txRmksNoOrd = (TextView) findViewById(R.id.txRmksNoOrd);
            btnRmkClose = (ImageView) findViewById(R.id.btnRmkClose);
            linPayment = (LinearLayout) findViewById(R.id.lin_payment);
            linRpt = (LinearLayout) findViewById(R.id.llRpt);
            linVanSales = findViewById(R.id.lin_vanSales);
            linintent = findViewById(R.id.lin_indent);
            linSalesReturn = findViewById(R.id.lin_salesReturn);
            linStockRot = findViewById(R.id.lin_stockRotation);
            tvOutstanding = findViewById(R.id.txOutstanding);

            swipe_left_image = findViewById(R.id.swipe_left_image);

            txPrvBal = findViewById(R.id.PrvOutAmt);
            txSalesAmt = findViewById(R.id.SalesAmt);
            txPayment = findViewById(R.id.PaymentAmt);
            tvStartDate = findViewById(R.id.tvStartDate);
            tvEndDate = findViewById(R.id.tvEndDate);
            tvSalesReturn = findViewById(R.id.tvSalesReturn);
            tv_no_data=findViewById(R.id.tv_no_data);
            card_date=findViewById(R.id.card_date);

            lin_noOrder.setOnClickListener(this);
            lastinvoice.setOnClickListener(this);
            lin_order.setOnClickListener(this);
            tvOtherBrand.setOnClickListener(this);
            tvQPS.setOnClickListener(this);
            tvPOP.setOnClickListener(this);
            tvOrder.setOnClickListener(this);
            tvCoolerInfo.setOnClickListener(this);
            linPayment.setOnClickListener(this);
            linRpt.setOnClickListener(this);
            linVanSales.setOnClickListener(this);
            linintent.setOnClickListener(this);
            tvStartDate.setOnClickListener(this);
            tvEndDate.setOnClickListener(this);
            tvSalesReturn.setOnClickListener(this);
            linSalesReturn.setOnClickListener(this);
            linStockRot.setOnClickListener(this);

            txPrvBal.setText(CurrencySymbol+" 0.00");
            txSalesAmt.setText(CurrencySymbol+" 0.00");
            txPayment.setText(CurrencySymbol+" 0.00");
            tvOutstanding.setText(CurrencySymbol+" 0.00");

            loadNoOrdRemarks();
            getApprovalData();
            btnRmkClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linNoOrderRmks.setVisibility(View.GONE);
                }
            });
            txRmkTmplSpinn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    common_class.showCommonDialog(ldgRemarks, 1, Invoice_History.this);
                }
            });
            btnSbmtNOrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txRmksNoOrd.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(Invoice_History.this, "Select the Reason", Toast.LENGTH_LONG).show();
                        return;
                    }
                    linNoOrderRmks.setVisibility(View.GONE);
                    SaveOrder();
                }
            });
            invoicerecyclerview = (RecyclerView) findViewById(R.id.invoicerecyclerview);

            lin_invoice.setOnClickListener(this);
            lin_complementary.setOnClickListener(this);
            marketingActivityLayout.setOnClickListener(this);

            GetJsonData(String.valueOf(db.getMasterData(Constants.Todaydayplanresult)), "6");

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

            tvStartDate.setText(Common_Class.GetDatewothouttime());
            tvEndDate.setText(Common_Class.GetDatewothouttime());

            //common_class.getDb_310Data(Constants.TAXList, this);
            common_class.getDb_310Data(Constants.FreeSchemeDiscList, this);
            //common_class.getDb_310Data(Constants.OUTSTANDING, this);

            SharedPreferences sessionCount = PreferenceManager.getDefaultSharedPreferences(this);
            int count = sessionCount.getInt("count", 0);
            if (count < 1) {
                ShowSwipeAnimation(sessionCount, count);
            }

            getOutstanding();

//            if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE))
//                findViewById(R.id.orderTypesLayout).setVisibility(View.GONE);


            if (Shared_Common_Pref.SFA_MENU.equalsIgnoreCase("VanSalesDashboardRoute")) {
                linVanSales.setVisibility(View.VISIBLE);
                lin_invoice.setVisibility(View.GONE);
                lin_complementary.setVisibility(View.GONE);
                lin_order.setVisibility(View.GONE);

                lin_noOrder.setVisibility(View.VISIBLE);

                card_date.setVisibility(View.GONE);
                marketingActivityLayout.setVisibility(View.GONE);
            }
            if (!Common_Class.isNullOrEmpty(Shared_Common_Pref.CUSTOMER_CODE)) {
                //  common_class.getDentDatas(this);
                linintent.setVisibility(View.VISIBLE);
                lin_noOrder.setVisibility(View.GONE);
                // tvSalesReturn.setVisibility(View.VISIBLE);
            }

            if (sharedCommonPref.getvalue(Shared_Common_Pref.DCRMode).equalsIgnoreCase("SR")) {
                //  common_class.getDentDatas(this);
                findViewById(R.id.llSalesParent).setVisibility(View.VISIBLE);
                findViewById(R.id.llSecParent).setVisibility(View.GONE);
                common_class.getDataFromApi(Constants.SR_GetTodayOrder_List, this, false);

            } else if(Shared_Common_Pref.SFA_MENU.equalsIgnoreCase("VanSalesDashboardRoute")){

            common_class.getDataFromApi(Constants.Van_GetTodayOrder_List, this, false);

        }else {
                common_class.getDataFromApi(Constants.GetTodayOrder_List, this, false);

            }

            tvCoolerInfo.setVisibility(View.GONE);

            if (Shared_Common_Pref.Freezer_Required.equalsIgnoreCase("yes"))
                tvCoolerInfo.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void ShowSwipeAnimation(SharedPreferences sessionCount, int count) {
        swipe_left_image.setVisibility(View.VISIBLE);
        Animation slideLeftAnimation = new TranslateAnimation(150, -150, 0, 0);
        slideLeftAnimation.setDuration(1000);
        slideLeftAnimation.setRepeatCount(Animation.INFINITE);
        slideLeftAnimation.setRepeatMode(Animation.RESTART);
        sessionCount.edit().putInt("count", (count + 1)).apply();
        swipe_left_image.startAnimation(slideLeftAnimation);
        new Handler().postDelayed(() -> {
            swipe_left_image.clearAnimation();
            swipe_left_image.setVisibility(View.GONE);
        }, 10000);

        View rootView = getWindow().getDecorView().getRootView();
        rootView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    swipe_left_image.clearAnimation();
                    swipe_left_image.setVisibility(View.GONE);
                    break;
                case MotionEvent.ACTION_MOVE:
                    // User is moving their finger on the screen
                    break;
                case MotionEvent.ACTION_UP:
                    // User lifted their finger off the screen
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    void showDatePickerDialog(int pos, TextView tv) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(Invoice_History.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                String date = ("" + year + "-" + month + "-" + dayOfMonth);

                if (common_class.checkDates(pos == 0 ? date : tvStartDate.getText().toString(), pos == 1 ? date : tvEndDate.getText().toString(), Invoice_History.this)) {
                    tv.setText(date);
                    if (sharedCommonPref.getvalue(Shared_Common_Pref.DCRMode).equalsIgnoreCase("SR"))
                        common_class.getDataFromApi(Constants.SR_GetTodayOrder_List, Invoice_History.this, false);
                    else if(Shared_Common_Pref.SFA_MENU.equalsIgnoreCase("VanSalesDashboardRoute")){

                        common_class.getDataFromApi(Constants.Van_GetTodayOrder_List, Invoice_History.this, false);

                    }else

                        common_class.getDataFromApi(Constants.GetTodayOrder_List, Invoice_History.this, false);
                } else {
                    common_class.showMsg(Invoice_History.this, "Please select valid date");
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }


    private void getOutstanding() {
        JSONObject jParam = new JSONObject();
        try {
            jParam.put("SF", UserDetails.getString("Sfcode", ""));
            jParam.put("Stk", sharedCommonPref.getvalue(Constants.Distributor_Id));
            jParam.put("Cus", Shared_Common_Pref.OutletCode);
            jParam.put("div", UserDetails.getString("Divcode", ""));

            ApiClient.getClient().create(ApiInterface.class)
                    .getDataArrayList("get/outstanding", jParam.toString())
                    .enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            try {
                                JsonArray res = response.body();
                                JsonObject jItem = res.get(0).getAsJsonObject();
                                txPrvBal.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(jItem.get("pBal").getAsDouble()));
                                txSalesAmt.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(jItem.get("Debit").getAsDouble()));
                                txPayment.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(jItem.get("Credit").getAsDouble()));
                                tvOutstanding.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(jItem.get("Balance").getAsDouble()));

                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {

                            Log.d("InvHistory", String.valueOf(t));
                        }
                    });
        } catch (JSONException e) {

        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        if (type == 1) {
            txRmksNoOrd.setText(myDataset.get(position).getName());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_salesReturn:
                JsonObject data = new JsonObject();
                //   {"Stk":"","Dt":"","RetID":"","CustomerCode":""}
                data.addProperty("Stk", sharedCommonPref.getvalue(Constants.Distributor_Id));
                data.addProperty("Dt", Common_Class.GetDatewothouttime());
                data.addProperty("RetID", Shared_Common_Pref.OutletCode);
                data.addProperty("CustomerCode", Shared_Common_Pref.CUSTOMER_CODE);

                common_class.getDb_310Data(Constants.SALES_RETURN, this, data);//                if(FilterOrderList.size()>0)
                //
                break;
            case R.id.lin_stockRotation:
                type = "Stock Rotation";
                getIndentDatas();
                break;
            case R.id.tvStartDate:
                showDatePickerDialog(0, tvStartDate);
                break;
            case R.id.tvEndDate:
                showDatePickerDialog(1, tvEndDate);
                break;

            case R.id.llRpt:
                common_class.CommonIntentwithoutFinish(PayLedgerActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.lin_payment:
                /*common_class.CommonIntentwithoutFinish(PaymentActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);*/
                if(Shared_Common_Pref.SFA_MENU.equalsIgnoreCase("VanSalesDashboardRoute")){

                    common_class.CommonIntentwithoutFinish(VanSalePaymentNewActivity.class);

                    overridePendingTransition(R.anim.in, R.anim.out);

                }else {

                    common_class.CommonIntentwithoutFinish(PaymentActivity.class);

                    overridePendingTransition(R.anim.in, R.anim.out);

                }
                break;
            case R.id.tvOtherBrand:
                common_class.CommonIntentwithFinish(OtherBrandActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.tvQPS:
                common_class.CommonIntentwithFinish(QPSActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.tvPOP:
                common_class.CommonIntentwithFinish(POPActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.tvCoolerInfo:
                common_class.CommonIntentwithFinish(CoolerInfoActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;

            case R.id.lin_order:
                Shared_Common_Pref.Invoicetoorder = "0";
                common_class.getDb_310Data(Constants.PreOrderQtyList, this);

                //Shared_Common_Pref.TransSlNo = "0";
                break;
            case R.id.lin_repeat_order:
                break;
            case R.id.lin_invoice:
                Shared_Common_Pref.Invoicetoorder = "2";
                //getInvoiceOrderQty();
                common_class.CommonIntentwithFinish(Invoice_Category_Select.class);
                overridePendingTransition(R.anim.in, R.anim.out);

                break;
            case R.id.lin_complementary:
                Shared_Common_Pref.Invoicetoorder = "2";
                startActivity(new Intent(this, CompementaryInvoiceActivity.class));
                overridePendingTransition(R.anim.in, R.anim.out);
                break;

            case R.id.marketingActivityLayout:
                Intent intent = new Intent(this, CustomFormHomeActivity.class);
                intent.putExtra("isPrimary", 2);
                startActivity(intent);
                break;

            case R.id.lin_vanSales:
                if(approveFlagValue==0){
                    Toast.makeText(getApplicationContext(),"Please load stock or get approved by Admin",Toast.LENGTH_SHORT).show();
                }else if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.VAN_STOCK_LOADING))) {
                    common_class.showMsg(Invoice_History.this, "No Stock");
                } else {
                Shared_Common_Pref.Invoicetoorder = "1";
                    Constants.VAN_SALES_MODE = Constants.VAN_SALES_ORDER;
                    startActivity(new Intent(getApplicationContext(), Invoice_Vansales_Select.class));
                    overridePendingTransition(R.anim.in, R.anim.out);
                }


//                if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.VAN_STOCK_LOADING))) {
//                    common_class.showMsg(Invoice_History.this, "No Stock");
//                }
//                else {
//                    Shared_Common_Pref.Invoicetoorder = "1";
//                    common_class.getDb_310Data(Constants.VAN_SALES_ORDER, this);
//
//                    startActivity(new Intent(getApplicationContext(), Invoice_Vansales_Select.class));
//                    overridePendingTransition(R.anim.in, R.anim.out);
//                }


                break;
            case R.id.lin_indent:
                type = "Indent";
                getIndentDatas();
                break;
            case R.id.lin_repeat_invoice:
                break;
            case R.id.lastinvoice:
                common_class.CommonIntentwithoutFinish(HistoryInfoActivity.class);
                overridePendingTransition(R.anim.in, R.anim.out);
                // common_class.CommonIntentwithoutFinish(More_Info_Activity.class);

                break;
            case R.id.lin_noOrder:
                if (mlocation == null) {
                    new LocationFinder(getApplication(), new LocationEvents() {
                        @Override
                        public void OnLocationRecived(Location location) {
                            strLoc = (location.getLatitude() + ":" + location.getLongitude()).split(":");

                            linNoOrderRmks.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    strLoc = (mlocation.getLatitude() + ":" + mlocation.getLongitude()).split(":");

                    linNoOrderRmks.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.tvOrder:
                common_class.getDb_310Data(Constants.PreOrderQtyList, this);
                break;

            case R.id.tvSalesReturn:
                startActivity(new Intent(this, SalesReturnActivity.class));
                break;
        }
    }
    private void getApprovalData() {
        try {

            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                JSONObject HeadItem = new JSONObject();
                HeadItem.put("distributorCode", sharedCommonPref.getvalue(Constants.Distributor_Id));

                String div_code = Shared_Common_Pref.Div_Code.replaceAll(",", "");
                HeadItem.put("divisionCode", div_code);


                Call<ResponseBody> call = service.getVanApproveData(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {


                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }
                                JSONArray array=new JSONArray(is.toString());
                                JSONObject jsonObject = array.getJSONObject(0);
                                approveFlagValue = jsonObject.getInt("ApproveFlag");

                                // shared_common_pref.save(Constants.RetailorTodayData, is.toString());

                            }

                        } catch (Exception e) {

                            Log.v("fail>>1", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>2", t.toString());


                    }
                });
            } else {
                common_class.showMsg(VanSalesDashboardRoute.dashboard_route, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }

    public void loadNoOrdRemarks() {
        db = new DatabaseHandler(this);
        try {
            JSONArray HAPRmks = db.getMasterData("HAPNoOrdRmks");
            if (HAPRmks != null) {
                for (int li = 0; li < HAPRmks.length(); li++) {
                    JSONObject jItem = HAPRmks.getJSONObject(li);
                    Common_Model item = new Common_Model(jItem.getString("id"), jItem.getString("name"), jItem);
                    ldgRemarks.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getIndentDatas() {

        if (common_class.isNetworkAvailable(this)) {


            DatabaseHandler db = new DatabaseHandler(this);
            JSONObject jParam = new JSONObject();
            try {
                jParam.put("SF", sharedCommonPref.getvalue(Constants.Distributor_Id));
                jParam.put("Stk", Shared_Common_Pref.CUSTOMER_CODE);
                jParam.put("div", UserDetails.getString("Divcode", ""));
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                service.getDataArrayList("get/indentprodgroup", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.v(TAG, response.toString());
                        db.deleteMasterData(Constants.INDENT_ProdGroups_List);
                        db.addMasterData(Constants.INDENT_ProdGroups_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getDataArrayList("get/indentprodtypes", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        db.deleteMasterData(Constants.INDENT_ProdTypes_List);
                        db.addMasterData(Constants.INDENT_ProdTypes_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });
                service.getDataArrayList("get/indentprodcate", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        db.deleteMasterData(Constants.INDENT_Category_List);
                        db.addMasterData(Constants.INDENT_Category_List, response.body());
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

                    }
                });


                service.getDataArrayList("get/indentproddets", jParam.toString()).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        try {
                            Log.v("INDENT:", response.body().toString());
                            db.deleteMasterData(Constants.INDENT_Product_List);
                            db.addMasterData(Constants.INDENT_Product_List, response.body());

                            Intent intent = new Intent(getApplicationContext(), IndentActivity.class);
                            intent.putExtra("type", type);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in, R.anim.out);
                        } catch (Exception e) {
                            common_class.showMsg(Invoice_History.this, e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        common_class.showMsg(Invoice_History.this, t.getMessage());
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            common_class.showMsg(this, "No Internet Connection");
        }

    }


    private void GetJsonData(String jsonResponse, String type) {

        //type =1 product category data values
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (type.equals("1")) {

                } else {
                    Worktype_code = jsonObject1.optString("wtype");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void SaveOrder() {
        if (common_class.isNetworkAvailable(this)) {

            AlertDialogBox.showDialog(Invoice_History.this, HAPApp.Title, "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {

                    JSONArray data = new JSONArray();
                    JSONObject ActivityData = new JSONObject();


                    try {
                        JSONObject HeadItem = new JSONObject();
                        HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("Worktype_code", Worktype_code);
                        HeadItem.put("Town_code", sharedCommonPref.getvalue(Constants.Route_Id));
                        HeadItem.put("dcr_activity_date", Common_Class.GetDate());
                        HeadItem.put("Daywise_Remarks", txRmksNoOrd.getText().toString());
                        HeadItem.put("UKey", Common_Class.GetEkey());
                        HeadItem.put("orderValue", "0");
                        HeadItem.put("DataSF", Shared_Common_Pref.Sf_Code);
                        String orderType="secOrder";
                        if(Shared_Common_Pref.SFA_MENU.equalsIgnoreCase("VanSalesDashboardRoute"))
                            orderType="vanSales";
                        HeadItem.put("ordType", orderType);
                        ActivityData.put("Activity_Report_Head", HeadItem);

                        JSONObject OutletItem = new JSONObject();
                        OutletItem.put("Doc_Meet_Time", Common_Class.GetDate());
                        OutletItem.put("modified_time", Common_Class.GetDate());
                        OutletItem.put("stockist_code", sharedCommonPref.getvalue(Constants.Distributor_Id));
                        OutletItem.put("stockist_name", sharedCommonPref.getvalue(Constants.Distributor_name));
                        OutletItem.put("orderValue", "0");
                        OutletItem.put("CashDiscount", "0");
                        OutletItem.put("NetAmount", "0");
                        OutletItem.put("No_Of_items", "0");
                        OutletItem.put("Invoice_Flag", Shared_Common_Pref.Invoicetoorder);
                        OutletItem.put("TransSlNo", Shared_Common_Pref.TransSlNo);
                        OutletItem.put("doctor_code", Shared_Common_Pref.OutletCode);
                        OutletItem.put("doctor_name", Shared_Common_Pref.OutletName);
                        OutletItem.put("ordertype", "no order");
                        OutletItem.put("reason", txRmksNoOrd.getText().toString());
                        if (strLoc.length > 0) {
                            OutletItem.put("Lat", strLoc[0]);
                            OutletItem.put("Long", strLoc[1]);
                        } else {
                            OutletItem.put("Lat", "");
                            OutletItem.put("Long", "");
                        }
                        ActivityData.put("Activity_Doctor_Report", OutletItem);
                        data.put(ActivityData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonObject> responseBodyCall = apiInterface.saveCalls(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
                    responseBodyCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                try {
                                    Log.e("JSON_VALUES", response.body().toString());
                                    JSONObject jsonObjects = new JSONObject(response.body().toString());
                                    linNoOrderRmks.setVisibility(View.GONE);
                                    common_class.showMsg(Invoice_History.this, jsonObjects.getString("Msg"));

                                } catch (Exception e) {

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("SUBMIT_VALUE", "ERROR");
                        }
                    });

                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null && !apiDataResponse.equals("")) {

                switch (key) {
                    case Constants.SALES_RETURN:
                        Log.v(TAG, apiDataResponse);
                        JSONObject obj = new JSONObject(apiDataResponse);
                        if (obj.getBoolean("success")) {

                            sharedCommonPref.save(Constants.SALES_RETURN, obj.getJSONArray("Data").toString());
                            Shared_Common_Pref.Invoicetoorder = "1";
                            Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
                            sharedCommonPref.save(Constants.FLAG, "Return Invoice");
                            intent.putExtra("Order_Values", "0");
                            intent.putExtra("Invoice_Values", "0");
                            intent.putExtra("No_Of_Items", "0");
                            intent.putExtra("Invoice_Date", "0");
                            intent.putExtra("NetAmount", "0");
                            intent.putExtra("Discount_Amount", "0");
                            startActivity(intent);
                            overridePendingTransition(R.anim.in, R.anim.out);

                        } else {
                            sharedCommonPref.clear_pref(Constants.SALES_RETURN);
                            common_class.showMsg(this, obj.getString("Msg"));

                        }
                        break;
                    case Constants.INDENT_Product_List:
                        startActivity(new Intent(getApplicationContext(), IndentActivity.class));
                        overridePendingTransition(R.anim.in, R.anim.out);
                        break;
                    case Constants.OUTSTANDING:
                        JSONObject jsonObjectOutStd = new JSONObject(apiDataResponse);
                        if (jsonObjectOutStd.getBoolean("success")) {

                            JSONArray jsonArray = jsonObjectOutStd.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tvOutstanding.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(
                                        jsonArray.getJSONObject(i).getDouble("Outstanding")));

                            }

                        } else {

                            tvOutstanding.setText(CurrencySymbol+" " + 0.00);
                        }
                        break;
                    case Constants.FreeSchemeDiscList:
                        JSONObject jsonObject = new JSONObject(apiDataResponse);
                        if (jsonObject.getBoolean("success")) {
                            Gson gson = new Gson();
                            List<Product_Details_Modal> product_details_modalArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");

                            if (jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    product_details_modalArrayList.add(new Product_Details_Modal(jsonObject1.getString("Product_Code"),
                                            jsonObject1.getString("Scheme"), jsonObject1.getString("Free"),
                                            Double.valueOf(jsonObject1.getString("Discount")), jsonObject1.getString("Discount_Type"),
                                            jsonObject1.getString("Package"), 0, jsonObject1.getString("Offer_Product"),
                                            jsonObject1.getString("Offer_Product_Name"), jsonObject1.getString("offer_product_unit")));
                                }
                            }
                            sharedCommonPref.save(Constants.FreeSchemeDiscList, gson.toJson(product_details_modalArrayList));
                        } else {
                            sharedCommonPref.clear_pref(Constants.FreeSchemeDiscList);
                        }
                        break;
                    case Constants.TAXList:
                        JSONObject jsonObjectTax = new JSONObject(apiDataResponse);
                        Log.v("TAX_PRIMARY:", apiDataResponse);

                        if (jsonObjectTax.getBoolean("success")) {
                            sharedCommonPref.save(Constants.TAXList, apiDataResponse);

                        } else {
                            sharedCommonPref.clear_pref(Constants.TAXList);

                        }
                        break;
                    case Constants.GetTodayOrder_List:
                        setHistoryAdapter(apiDataResponse);

                        break;
                    case Constants.SR_GetTodayOrder_List:
                        setHistoryAdapter(apiDataResponse);
                        break;
                    case Constants.Van_GetTodayOrder_List:
                        setHistoryAdapter(apiDataResponse);
                        break;
                    case Constants.PreOrderQtyList:
                        JSONObject jsonObjectPreOrder = new JSONObject(apiDataResponse);
                        if (jsonObjectPreOrder.getBoolean("success")) {
                            sharedCommonPref.save(Constants.PreOrderQtyList, apiDataResponse);
                            common_class.CommonIntentwithFinish(Order_Category_Select.class);

                            overridePendingTransition(R.anim.in, R.anim.out);
                        } else {
                            sharedCommonPref.clear_pref(Constants.PreOrderQtyList);
                            Log.v("PreOrderList: ", "" + "not success");
                        }
                        break;
                }

            }
        } catch (Exception e) {
            Log.v("Invoice History: ", e.getMessage());

        }
    }

    private void setHistoryAdapter(String apiDataResponse) {
        FilterOrderList.clear();
        userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
        }.getType();
        OutletReport_View_Modal = gson.fromJson(apiDataResponse, userType);
        if (OutletReport_View_Modal != null && OutletReport_View_Modal.size() > 0) {
            for (OutletReport_View_Modal filterlist : OutletReport_View_Modal) {
                if (filterlist.getOutletCode().equals(Shared_Common_Pref.OutletCode)) {
                    FilterOrderList.add(filterlist);
                }
            }
        }
        if(FilterOrderList.size()==0){

            tv_no_data.setVisibility(View.VISIBLE);

            invoicerecyclerview.setVisibility(View.GONE);

        }else{

            tv_no_data.setVisibility(View.GONE);

            invoicerecyclerview.setVisibility(View.VISIBLE);

        }

        mReportViewAdapter = new Invoice_History_Adapter(Invoice_History.this, FilterOrderList, new AdapterOnClick() {
            @Override
            public void onIntentClick(int position) {

                navigatePrintScreen(position, FilterOrderList.get(position).getStatus());


            }
        });
        invoicerecyclerview.setAdapter(mReportViewAdapter);

    }


    public void navigatePrintScreen(int position, String status) {
        Log.e("TRANS_SLNO", FilterOrderList.get(position).getTransSlNo());
        Shared_Common_Pref.TransSlNo = FilterOrderList.get(position).getTransSlNo();
        Shared_Common_Pref.Invoicetoorder = "1";
        Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
        sharedCommonPref.save(Constants.FLAG, status);
        Log.e("Sub_Total", String.valueOf(FilterOrderList.get(position).getOrderValue() + ""));
        intent.putExtra("Order_Values", FilterOrderList.get(position).getOrderValue() + "");
        intent.putExtra("Invoice_Values", FilterOrderList.get(position).getInvoicevalues());
        intent.putExtra("No_Of_Items", FilterOrderList.get(position).getNo_Of_items());
        intent.putExtra("Invoice_Date", FilterOrderList.get(position).getOrderDate());
        intent.putExtra("PONumber", FilterOrderList.get(position).getOrderNo());
        intent.putExtra("NetAmount", FilterOrderList.get(position).getNetAmount());
        intent.putExtra("Discount_Amount", FilterOrderList.get(position).getDiscount_Amount());
        intent.putExtra("Cash_Discount",FilterOrderList.get(position).getTotCashDisc());
        startActivity(intent);
        overridePendingTransition(R.anim.in, R.anim.out);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (linVanSales.getVisibility() == View.VISIBLE)
//                common_class.CommonIntentwithFinish(VanSalesDashboardRoute.class);
//
//            else
//                common_class.CommonIntentwithFinish(Dashboard_Route.class);
//            overridePendingTransition(R.anim.in, R.anim.out);


            finish();

            return true;
        }
        return false;
    }

}