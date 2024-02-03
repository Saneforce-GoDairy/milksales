package com.saneforce.godairy.SFA_Activity;

import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;
import static com.saneforce.godairy.SFA_Activity.HAPApp.MRPCap;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.saneforce.godairy.Activity_Hap.AllowancCapture;
import com.saneforce.godairy.BuildConfig;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.Interface.OnImagePickListener;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.FilesAdapter;
import com.saneforce.godairy.SFA_Adapter.Print_Invoice_Adapter;
import com.saneforce.godairy.SFA_Adapter.QPS_Modal;
import com.saneforce.godairy.SFA_Model_Class.Product_Details_Modal;
import com.saneforce.godairy.common.FileUploadService;
import com.saneforce.godairy.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Print_Invoice_Activity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {
    Print_Invoice_Adapter mReportViewAdapter;
    RecyclerView printrecyclerview, rvReturnInv;
    Shared_Common_Pref sharedCommonPref;
    Common_Class common_class;
    List<Product_Details_Modal> Order_Outlet_Filter;
    TextView netamount, returnNetAmt, cashdiscount, gstLabel, gstrate, totalfreeqty, totalqty, totalitem, subtotal, invoicedate, retaileAddress, billnumber, retailername,
            retailerroute, back, tvOrderType, tvRetailorPhone, tvDistributorPh, tvDistributorName, tvOutstanding, tvPaidAmt, tvHeader, tvDistId,
            tvDistAdd, returngstLabel, returngstrate, returntotalqty, returntotalitem, returnsubtotal, tvDisGST, tvDisFSSAI, tvRetGST, tvRetFSSAI,baseAmt;
    ImageView ok, ivPrint;
    public static Print_Invoice_Activity mPrint_invoice_activity;
    CircularProgressButton btnInvoice;
    NumberFormat formatter = new DecimalFormat("##0.00");
    private String label, amt, dis_storeName = "", dis_address = "", dis_phone = "", storeName = "", address = "", phone = "";
    private ArrayList<Product_Details_Modal> taxList;
    private ArrayList<Product_Details_Modal> uomList;

    double cashDisc, subTotalVal,NetTotAmt, outstandAmt,tsubTotalVal;
    LinearLayout llDistCal, llRetailCal;
    RelativeLayout rlTax;
    String[] strLoc;
    final Handler handler = new Handler();
    public String TAG = "Print_Invoice_Activity";
    ImageView ivStockCapture;
    RecyclerView rvStockCapture;
    List<QPS_Modal> stockFileList = new ArrayList<>();
    String dis_gstn = "",dis_fssai="", ret_gstn = "",ret_fssai="",RetailCode="",PONo="";
    String sPMode="";
    Context context = this;

    Boolean Addinf=false;
    public static final String UserDetail = "MyPrefs";
    SharedPreferences UserDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            mPrint_invoice_activity = this;
            setContentView(R.layout.activity_print__invoice_);
            ButterKnife.inject(this);
            UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
            printrecyclerview = findViewById(R.id.printrecyclerview);
            rvReturnInv = findViewById(R.id.rvReturnInv);

            sharedCommonPref = new Shared_Common_Pref(Print_Invoice_Activity.this);
            sharedCommonPref.clear_pref(Constants.INVOICE_ORDERLIST);
            common_class = new Common_Class(this);
            netamount = findViewById(R.id.netamount);
            back = findViewById(R.id.back);
            cashdiscount = findViewById(R.id.cashdiscount);
            billnumber = findViewById(R.id.billnumber);
            totalfreeqty = findViewById(R.id.totalfreeqty);
            totalqty = findViewById(R.id.totalqty);
            totalitem = findViewById(R.id.totalitem);
            subtotal = findViewById(R.id.subtotal);
            invoicedate = findViewById(R.id.invoicedate);
            retaileAddress = findViewById(R.id.retaileAddress);
            retailername = findViewById(R.id.retailername);
            retailerroute = findViewById(R.id.retailerroute);
            ok = findViewById(R.id.ok);
            btnInvoice = findViewById(R.id.btnInvoice);
            tvDistributorName = findViewById(R.id.distributor_Name);
            tvDistributorPh = findViewById(R.id.distPhoneNum);
            tvOrderType = findViewById(R.id.tvTypeLabel);
            tvRetailorPhone = findViewById(R.id.retailePhoneNum);
            tvOutstanding = findViewById(R.id.tvOutstanding);
            tvPaidAmt = findViewById(R.id.tvPaidAmt);
            tvHeader = findViewById(R.id.tvHeader);
            gstLabel = findViewById(R.id.gstLabel);
            gstrate = findViewById(R.id.gstrate);
            tvDistAdd = findViewById(R.id.tvAdd);
            tvDisGST = findViewById(R.id.tvDIS_GST);
            tvDisFSSAI = findViewById(R.id.tvDIS_FSSAI);
            tvRetGST = findViewById(R.id.tvRet_GST);
            tvRetFSSAI = findViewById(R.id.tvRet_FSSAI);
            tvDistId = findViewById(R.id.tvDistId);
            llDistCal = findViewById(R.id.llDistCall);
            llRetailCal = findViewById(R.id.llRetailCal);
            returntotalqty = findViewById(R.id.returnTotalqty);
            returntotalitem = findViewById(R.id.returnTotalitem);
            returnsubtotal = findViewById(R.id.returnSubtotal);
            returngstLabel = findViewById(R.id.returnInvTax);
            returngstrate = findViewById(R.id.returnGstrate);
            returnNetAmt = findViewById(R.id.tvReturnAmt);
            ivStockCapture = findViewById(R.id.ivStockCapture);
            rvStockCapture = findViewById(R.id.rvStockFiles);
            baseAmt = findViewById(R.id.baseAmt);
            rlTax = findViewById(R.id.rlTax);

            RetailCode=sharedCommonPref.getvalue(Constants.Retailor_ERP_Code);
            retailername.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
            if(retailername.getText().toString().equalsIgnoreCase("")){
                retailername.setText("Customer");
            }
            tvDistributorName.setText(sharedCommonPref.getvalue(Constants.Distributor_name));
            ivPrint = findViewById(R.id.ivPrint);

            back.setOnClickListener(this);
            ok.setOnClickListener(this);
            ivPrint.setOnClickListener(this);
            btnInvoice.setOnClickListener(this);
            llDistCal.setOnClickListener(this);
            llRetailCal.setOnClickListener(this);
            ivStockCapture.setOnClickListener(this);


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

            sPMode = sharedCommonPref.getvalue(Constants.FLAG);

            if (sPMode.equalsIgnoreCase("Sales return")) {
                tvHeader.setText(sPMode);
                TextView referenceBillNumber = findViewById(R.id.referenceBillNumber);
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Map<String, String> params = new HashMap<>();
                params.put("axn", "cal_rem_amt_sales_return");
                params.put("invoice", Shared_Common_Pref.TransSlNo);
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
                                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                                    double InvAmt = jsonArray.getJSONObject(0).getDouble("InvAmt");
                                    double RetAmt = jsonArray.getJSONObject(0).getDouble("RetAmt");
                                    double RemAmt = jsonArray.getJSONObject(0).getDouble("RemAmt");
                                    String RefInv = jsonArray.getJSONObject(0).getString("RefInv");
                                    referenceBillNumber.setVisibility(View.VISIBLE);
                                    referenceBillNumber.setText("Reference Invoice:\n" + RefInv);
                                    netamount.setText(common_class.formatCurrency(RetAmt));
                                } else {
                                    Toast.makeText(context, "Error 1: Response Not Success", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "Error 2: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Error 3: Response Not Success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast.makeText(context, "Error 4: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                tvHeader.setText("Sales " + sPMode);
            }

            if (sPMode.equalsIgnoreCase("INVOICE")) {
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Map<String, String> params = new HashMap<>();
                params.put("axn", "cal_rem_amt_invoice");
                params.put("invoice", Shared_Common_Pref.TransSlNo);
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
                                    double InvTot = jsonObject.getDouble("InvTot");
                                    double RetOrdVal = jsonObject.getDouble("RetOrdVal");
                                    double RemAmt = jsonObject.getDouble("RemAmt");
                                    String RetInv = jsonObject.getString("RetInv");
                                    RelativeLayout rlRetAmt = findViewById(R.id.rlRetAmt);
                                    rlRetAmt.setVisibility(View.VISIBLE);
                                    RelativeLayout rlToPay = findViewById(R.id.rlToPay);
                                    rlToPay.setVisibility(View.VISIBLE);
                                    TextView retAmount = findViewById(R.id.returnAmount);
                                    retAmount.setText(common_class.formatCurrency(RetOrdVal));
                                    TextView toPayAmount = findViewById(R.id.toPayAmount);
                                    toPayAmount.setText(common_class.formatCurrency(RemAmt));
                                    TextView referenceBillNumber = findViewById(R.id.referenceInvNumber);
                                    referenceBillNumber.setVisibility(View.VISIBLE);
                                    referenceBillNumber.setText("Sales Return Invoice: " + RetInv);
                                    netamount.setText(common_class.formatCurrency(InvTot));
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "Error 2: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Error 3: Response Not Success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast.makeText(context, "Error 4: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            Log.d("Distributor_Id",sharedCommonPref.getvalue(Constants.Distributor_Id));
            if(sharedCommonPref.getvalue(Constants.Distributor_Id).equalsIgnoreCase("7951")){
                Addinf=true;
            }
            if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE) == Constants.CHECKIN_TYPE){
                JSONArray stkListData = new JSONArray(sharedCommonPref.getvalue(Constants.Distributor_List));
                for(int i=0;i<stkListData.length();i++){
                    JSONObject job= stkListData.getJSONObject(i);
                    String id =  String.valueOf(job.optInt("id" ));
                    if(sharedCommonPref.getvalue(Constants.Distributor_Id) == id)
                    {
                        dis_gstn = job.getString("GSTN");
                        dis_fssai = job.getString("FSSAI");
                    }
                }
            }
            else {
                //dis_gstn = sharedCommonPref.getvalue(Constants.Distributor_gst);
                //dis_fssai =  sharedCommonPref.getvalue(Constants.Distributor_fssai);
                dis_gstn = UserDetails.getString("GSTN",""); //sharedCommonPref.getvalue(Constants.Distributor_gst);
                dis_fssai =  UserDetails.getString("FSSAI","");//sharedCommonPref.getvalue(Constants.Distributor_fssai);
            }


                JSONArray retListData = new JSONArray(sharedCommonPref.getvalue(Constants.Retailer_OutletList));
                for(int i=0;i<retListData.length();i++) {
                    JSONObject job = retListData.getJSONObject(i);
                    String id = String.valueOf(job.optInt("id"));
                    if (sharedCommonPref.OutletCode.equals(id)) {
                        ret_gstn = job.getString("gst");
                        ret_fssai = job.getString("FssiNo");
                    }
                }





            tvDistributorPh.setText(sharedCommonPref.getvalue(Constants.Distributor_phone));
            tvRetailorPhone.setText(sharedCommonPref.getvalue(Constants.Retailor_PHNo));
if (tvRetailorPhone.getText().toString().equalsIgnoreCase("0")) tvRetailorPhone.setText("");
            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Distributor_phone)))
                llDistCal.setVisibility(View.GONE);
            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Retailor_PHNo)))
                llRetailCal.setVisibility(View.GONE);
            retailerroute.setText(sharedCommonPref.getvalue(Constants.Route_name));
            retaileAddress.setText(sharedCommonPref.getvalue(Constants.Retailor_Address));
            invoicedate.setText(getIntent().getStringExtra("Invoice_Date"));
            tvDistId.setText(sharedCommonPref.getvalue(Constants.DistributorERP));
            tvDistAdd.setText(sharedCommonPref.getvalue(Constants.DistributorAdd));

            PONo=getIntent().getStringExtra("PONumber");

            tvDisGST.setText(dis_gstn);
            tvDisFSSAI.setText(dis_fssai);

            tvRetGST.setText(ret_gstn);
            tvRetFSSAI.setText(ret_fssai);

            Log.v("gst_dist",sharedCommonPref.getvalue(Constants.DistributorGst));

            if (sharedCommonPref.getvalue(Constants.FLAG).equals("ORDER")) {
                storeName = retailername.getText().toString();
                address = retaileAddress.getText().toString();
                phone = "Mobile:" + tvRetailorPhone.getText().toString();
//                common_class.getDb_310Data(Constants.TAXList, this);
//                common_class.getProductDetails(this);
                //common_class.getDb_310Data(Constants.FreeSchemeDiscList, this);
                common_class.getDataFromApi(Constants.TodayOrderDetails_List, this, false);
                common_class.getDb_310Data(Constants.OUTSTANDING, this);
            }else if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("INDENT")) {
                findViewById(R.id.llCreateInvoice).setVisibility(View.GONE);

                prepareIndentDetails();

                storeName = retailername.getText().toString();
                address = retaileAddress.getText().toString();
                phone = "Mobile:" + tvRetailorPhone.getText().toString();

            }
            else if (sharedCommonPref.getvalue(Constants.FLAG).equals("Primary Order") || sharedCommonPref.getvalue(Constants.FLAG).equals("Secondary Order") || sharedCommonPref.getvalue(Constants.FLAG).equals("POS INVOICE") || (sharedCommonPref.getvalue(Constants.FLAG).equals("PROJECTION"))) {
                findViewById(R.id.llCreateInvoice).setVisibility(View.GONE);
                findViewById(R.id.llOutletParent).setVisibility(View.GONE);
                tvDistAdd.setVisibility(View.VISIBLE);
                //  tvDistId.setVisibility(View.VISIBLE);
                if (sharedCommonPref.getvalue(Constants.FLAG).equals("Primary Order"))
                    common_class.getDataFromApi(Constants.TodayPrimaryOrderDetails_List, this, false);
                else if (sharedCommonPref.getvalue(Constants.FLAG).equals("PROJECTION")) {
                    common_class.getDataFromApi(Constants.ProjectionOrderDetails_List, this, false);
                    findViewById(R.id.llUOM).setVisibility(View.GONE);
                    findViewById(R.id.llPrice).setVisibility(View.GONE);
                    findViewById(R.id.llTot).setVisibility(View.GONE);
                    findViewById(R.id.rlSubTot).setVisibility(View.GONE);
                    findViewById(R.id.rlNetAmt).setVisibility(View.GONE);
                    totalitem.setTypeface(Typeface.DEFAULT_BOLD);
                    totalqty.setTypeface(Typeface.DEFAULT_BOLD);

                    tvDistAdd.setVisibility(View.GONE);
                    tvDistributorPh.setVisibility(View.GONE);
                    findViewById(R.id.llDistCall).setVisibility(View.GONE);

                } else {
                    findViewById(R.id.tvWelcomeLabel).setVisibility(View.VISIBLE);
                    common_class.getDataFromApi(Constants.PosOrderDetails_List, this, false);
                }

                findViewById(R.id.llDelivery).setVisibility(View.GONE);
                dis_storeName = tvDistributorName.getText().toString();
                dis_address = tvDistAdd.getText().toString();
                dis_phone = "Mobile:" + tvDistributorPh.getText().toString();

            } else {
                findViewById(R.id.llCreateInvoice).setVisibility(View.GONE);
                storeName = retailername.getText().toString();
                address = retaileAddress.getText().toString();
                phone = "Mobile:" + tvRetailorPhone.getText().toString();

                if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("Return Invoice")) {
                    findViewById(R.id.cvReturnInvParent).setVisibility(View.VISIBLE);
                    findViewById(R.id.cvSalesParent).setVisibility(View.GONE);
                    findViewById(R.id.llCreateInvoice).setVisibility(View.VISIBLE);
                    btnInvoice.setText("CONFIRM");
                    orderInvoiceDetailData(sharedCommonPref.getvalue(Constants.SALES_RETURN));

                } else if (sharedCommonPref.getvalue(Constants.FLAG).equals("COMPLEMENTARY INVOICE")){
                    findViewById(R.id.tvWelcomeLabel).setVisibility(View.GONE);
                    common_class.getDataFromApi(Constants.ComplementaryOrderDetails_List, this, false);

                } else if (sharedCommonPref.getvalue(Constants.FLAG).equals("SALES RETURN")){
                    findViewById(R.id.tvWelcomeLabel).setVisibility(View.GONE);
                    common_class.getDataFromApi(Constants.SalesReturnDetailsList, this, false);

                } else {
                    common_class.getDataFromApi(Constants.TodayOrderDetails_List, this, false);
                    if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("INVOICE")) {
                        findViewById(R.id.cvPayDetails).setVisibility(View.VISIBLE);

                        common_class.getDb_310Data(Constants.OUTSTANDING, this);
                    }
                }

            }

            tvOrderType.setText(sharedCommonPref.getvalue(Constants.FLAG));
            cashDisc =0;
            if(getIntent().hasExtra("Discount_Amount")) {
                cashDisc = Double.parseDouble(getIntent().getStringExtra("Discount_Amount"));
            }
            stockFileList.add(new QPS_Modal("", "", ""));//purity

            if(Addinf ){
                ///For Brunei
                tvDisGST.setVisibility(View.GONE);
                tvDisFSSAI.setVisibility(View.GONE);
                tvRetGST.setVisibility(View.GONE);
                tvRetFSSAI.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }
    }

    private void prepareIndentDetails() {
        Context context = this;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "prepareIndentDetails");
        params.put("order_id", Shared_Common_Pref.TransSlNo);
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
                            String jsonArray = jsonObject.getJSONArray("response").toString();
                            orderInvoiceDetailData(jsonArray);
                            Log.e("status", "Response: " + jsonArray);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (sharedCommonPref.getvalue(Constants.FLAG).equals("POS INVOICE"))
                common_class.CommonIntentwithFinish(PosHistoryActivity.class);
            else
                finish();
        }
        return false;
    }
    public void ResetSubmitBtn(int resetMode) {
        common_class.ProgressdialogShow(0, "");
        long dely = 10;
        if (resetMode != 0) dely = 1000;
        if (resetMode == 1) {
            btnInvoice.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));
        } else {
            btnInvoice.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnInvoice.stopAnimation();
                btnInvoice.revertAnimation();
            }
        }, dely);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivStockCapture:
                if (stockFileList.get(0).getFileUrls() == null || stockFileList.get(0).getFileUrls().size() < sharedCommonPref.getIntValue(Constants.SALES_RETURN_FILECOUNT)) {
                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {

                            List<String> list = new ArrayList<>();
                            File file = new File(fullPath);
                            Uri contentUri = Uri.fromFile(file);

                            if (stockFileList.get(0).getFileUrls() != null && stockFileList.get(0).getFileUrls().size() > 0)
                                list = (stockFileList.get(0).getFileUrls());
                            list.add(contentUri.toString());
                            stockFileList.get(0).setFileUrls(list);

                            rvStockCapture.setAdapter(new FilesAdapter(stockFileList.get(0).getFileUrls(), R.layout.adapter_local_files_layout, Print_Invoice_Activity.this));
                        }
                    });
                    Intent intent = new Intent(Print_Invoice_Activity.this, AllowancCapture.class);
                    intent.putExtra("allowance", "TAClaim");
                    startActivity(intent);
                } else {
                    common_class.showMsg(Print_Invoice_Activity.this, "Limit Exceed...");
                }

                break;
            case R.id.back:
                if (sharedCommonPref.getvalue(Constants.FLAG).equals("POS INVOICE"))
                    common_class.CommonIntentwithFinish(PosHistoryActivity.class);
                else
                    finish();
                break;
            case R.id.ok:
                createPdf();
                break;
            case R.id.ivPrint:
                showPrinterList();
                break;

            case R.id.btnInvoice:
                if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("Return Invoice")) {
                    if (stockFileList.get(0).getFileUrls() == null || stockFileList.get(0).getFileUrls().size() == 0) {
                        common_class.showMsg(this, "Please take picture");
                        return;

                    }
                    if (btnInvoice.isAnimating()) return;
                    btnInvoice.startAnimation();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String sLoc = sharedCommonPref.getvalue("CurrLoc");
                            if (sLoc.equalsIgnoreCase("")) {
                                new LocationFinder(getApplication(), new LocationEvents() {
                                    @Override
                                    public void OnLocationRecived(Location location) {
                                        strLoc = (location.getLatitude() + ":" + location.getLongitude()).split(":");
                                        confirmReturnInv();
                                    }
                                });
                            } else {
                                strLoc = sLoc.split(":");
                                confirmReturnInv();
                            }
                        }
                    }, 500);
                } else {
                    Shared_Common_Pref.Invoicetoorder = "4";

//                    common_class.ProgressdialogShow(1, "Updating Matrial Details");
//                    common_class.getProductDetails(this, new OnLiveUpdateListener() {
//                        @Override
//                        public void onUpdate(String mode) {

                            common_class.CommonIntentwithFinish(Invoice_Category_Select.class);
                            common_class.ProgressdialogShow(0, "");
//                        }
//                    });
                }
                break;

            case R.id.llDistCall:
                common_class.showCalDialog(this, "Do you want to Call this Distributor?", sharedCommonPref.getvalue(Constants.Distributor_phone));
                break;
            case R.id.llRetailCal:
                common_class.showCalDialog(this, "Do you want to Call this Outlet?", sharedCommonPref.getvalue(Constants.Retailor_PHNo));
                break;
        }
    }
    private void confirmReturnInv() {
        if (common_class.isNetworkAvailable(this)) {
            AlertDialogBox.showDialog(Print_Invoice_Activity.this, HAPApp.Title, "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    try {


                        common_class.ProgressdialogShow(1, "");
                        JSONArray data = new JSONArray();
                        JSONObject ActivityData = new JSONObject();

                        JSONObject HeadItem = new JSONObject();
                        HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("Worktype_code", "");
                        HeadItem.put("Town_code", sharedCommonPref.getvalue(Constants.Route_Id));
                        HeadItem.put("dcr_activity_date", Common_Class.GetDate());
                        HeadItem.put("Daywise_Remarks", "");
                        HeadItem.put("UKey", Common_Class.GetEkey());
                        HeadItem.put("orderValue", formatter.format(subTotalVal));
                        HeadItem.put("DataSF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("AppVer", BuildConfig.VERSION_NAME);
                        ActivityData.put("Activity_Report_Head", HeadItem);

                        JSONObject OutletItem = new JSONObject();
                        OutletItem.put("Doc_Meet_Time", Common_Class.GetDate());
                        OutletItem.put("modified_time", Common_Class.GetDate());
                        OutletItem.put("stockist_code", sharedCommonPref.getvalue(Constants.Distributor_Id));
                        OutletItem.put("stockist_name", sharedCommonPref.getvalue(Constants.Distributor_name));
                        OutletItem.put("orderValue", formatter.format(subTotalVal));
                        OutletItem.put("CashDiscount", cashDisc);
                        OutletItem.put("NetAmount", formatter.format(NetTotAmt));
                        OutletItem.put("No_Of_items", returntotalitem.getText().toString());
                        OutletItem.put("Invoice_Flag", Shared_Common_Pref.Invoicetoorder);
                        OutletItem.put("TransSlNo", "");
                        OutletItem.put("doctor_code", Shared_Common_Pref.OutletCode);
                        OutletItem.put("doctor_name", Shared_Common_Pref.OutletName);
                        OutletItem.put("ordertype", "Return Invoice");
                        OutletItem.put("from", sharedCommonPref.getvalue(Constants.Distributor_Id));
                        OutletItem.put("to", Shared_Common_Pref.CUSTOMER_CODE);
                        OutletItem.put("distCode", Shared_Common_Pref.CUSTOMER_CODE);
                        OutletItem.put("customerCode", sharedCommonPref.getvalue(Constants.Distributor_Id));

                        if (strLoc.length > 0) {
                            OutletItem.put("Lat", strLoc[0]);
                            OutletItem.put("Long", strLoc[1]);
                        } else {
                            OutletItem.put("Lat", "");
                            OutletItem.put("Long", "");
                        }
                        JSONArray Order_Details = new JSONArray();
                        JSONArray totTaxArr = new JSONArray();

                        for (int z = 0; z < Order_Outlet_Filter.size(); z++) {
                            JSONObject ProdItem = new JSONObject();
                            ProdItem.put("product_Name", Order_Outlet_Filter.get(z).getName());
                            ProdItem.put("product_code", Order_Outlet_Filter.get(z).getId());
                            ProdItem.put("Product_Qty", Order_Outlet_Filter.get(z).getQty());
                            ProdItem.put("Product_RegularQty", Order_Outlet_Filter.get(z).getRegularQty());
                            ProdItem.put("Product_Total_Qty", Order_Outlet_Filter.get(z).getQty());
                            ProdItem.put("Product_Amount", Order_Outlet_Filter.get(z).getAmount());
                            ProdItem.put("Rate", String.format("%.2f", Order_Outlet_Filter.get(z).getRate()));

                            ProdItem.put("free", Order_Outlet_Filter.get(z).getFree());
                            ProdItem.put("dis", Order_Outlet_Filter.get(z).getDiscount());
                            ProdItem.put("dis_value", Order_Outlet_Filter.get(z).getDiscount_value());
                            ProdItem.put("Off_Pro_code", Order_Outlet_Filter.get(z).getOff_Pro_code());
                            ProdItem.put("Off_Pro_name", Order_Outlet_Filter.get(z).getOff_Pro_name());
                            ProdItem.put("Off_Pro_Unit", Order_Outlet_Filter.get(z).getOff_Pro_Unit());
                            ProdItem.put("Off_Scheme_Unit", Order_Outlet_Filter.get(z).getScheme());
                            ProdItem.put("discount_type", Order_Outlet_Filter.get(z).getDiscount_type());

                            JSONArray tax_Details = new JSONArray();


                            if (Order_Outlet_Filter.get(z).getProductDetailsModal() != null &&
                                    Order_Outlet_Filter.get(z).getProductDetailsModal().size() > 0) {

                                for (int i = 0; i < Order_Outlet_Filter.get(z).getProductDetailsModal().size(); i++) {
                                    JSONObject taxData = new JSONObject();

                                    String label = Order_Outlet_Filter.get(z).getProductDetailsModal().get(i).getTax_Type();
                                    Double amt = Order_Outlet_Filter.get(z).getProductDetailsModal().get(i).getTax_Amt();
                                    taxData.put("Tax_Id", Order_Outlet_Filter.get(z).getProductDetailsModal().get(i).getTax_Id());
                                    taxData.put("Tax_Val", Order_Outlet_Filter.get(z).getProductDetailsModal().get(i).getTax_Val());
                                    taxData.put("Tax_Type", label);
                                    taxData.put("Tax_Amt", formatter.format(amt));
                                    tax_Details.put(taxData);


                                }


                            }

                            ProdItem.put("TAX_details", tax_Details);

                            Order_Details.put(ProdItem);

                        }

                        for (int i = 0; i < taxList.size(); i++) {
                            JSONObject totTaxObj = new JSONObject();

                            totTaxObj.put("Tax_Type", taxList.get(i).getTax_Type());
                            totTaxObj.put("Tax_Amt", formatter.format(taxList.get(i).getTax_Amt()));
                            totTaxArr.put(totTaxObj);

                        }

                        OutletItem.put("TOT_TAX_details", totTaxArr);
                        ActivityData.put("Activity_Doctor_Report", OutletItem);
                        ActivityData.put("Order_Details", Order_Details);

                        JSONArray file_Details = new JSONArray();


                        for (int f = 0; f < stockFileList.get(0).getFileUrls().size(); f++) {
                            JSONObject ProdItem = new JSONObject();
                            File file = new File(stockFileList.get(0).getFileUrls().get(f));
                            ProdItem.put("SalesReturnImg", file.getName());
                            file_Details.put(ProdItem);
                        }


                        ActivityData.put("file_Details", file_Details);

                        data.put(ActivityData);

                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonObject> responseBodyCall = apiInterface.saveSalesReturn(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
                        responseBodyCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        common_class.ProgressdialogShow(0, "");
                                        Log.e("JSON_VALUES", response.body().toString());
                                        JSONObject jsonObjects = new JSONObject(response.body().toString());
                                        String san = jsonObjects.getString("success");
                                        Log.e("Success_Message", san);
                                        ResetSubmitBtn(1);
                                        if (san.equals("true")) {
                                            sharedCommonPref.clear_pref(Constants.LOC_INDENT_DATA);
                                            common_class.CommonIntentwithFinish(Invoice_History.class);
                                        }
                                        common_class.showMsg(Print_Invoice_Activity.this, jsonObjects.getString("Msg"));

                                    } catch (Exception e) {
                                        common_class.ProgressdialogShow(0, "");
                                        ResetSubmitBtn(2);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                common_class.ProgressdialogShow(0, "");
                                Log.e("SUBMIT_VALUE", "ERROR");
                                ResetSubmitBtn(2);
                            }
                        });


                        uploadStockFile();

                    } catch (Exception e) {
                        e.printStackTrace();
                        ResetSubmitBtn(2);
                    }
                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    ResetSubmitBtn(0);
                }
            });

        } else {
            Toast.makeText(this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
            ResetSubmitBtn(0);
        }
    }
    private void uploadStockFile() {
        for (int f = 0; f < stockFileList.get(0).getFileUrls().size(); f++) {
            String filePath = stockFileList.get(0).getFileUrls().get(f).replaceAll("file:/", "");
            File file = new File(filePath);
            Intent mIntent = new Intent(this, FileUploadService.class);
            mIntent.putExtra("mFilePath", filePath);
            mIntent.putExtra("SF", Shared_Common_Pref.Sf_Code);
            mIntent.putExtra("FileName", file.getName());
            mIntent.putExtra("Mode", "SalesReturnImg");
            FileUploadService.enqueueWork(this, mIntent);
        }


    }
    public static String repeat(String val, int count){

        if(count<1) return "";
        StringBuilder buf = new StringBuilder(val.length() * count);
        while (count-- > 0) {
            buf.append(val);
        }
        return buf.toString();
    }
    public void printBill() {
        try {
                Bitmap logo = Printama.getBitmapFromVector(this, R.drawable.product_logo);
                Printama.with(this).connect(printama -> {

                   // printama.printImage(Printama.CENTER, logo, 140);
                   // printama.addNewLine();
                String sMode=sharedCommonPref.getvalue(Constants.FLAG);
                String sHead = "";
                printama.setWideTallBold();
                 if( sMode.equals("PROJECTION") ){
                    sHead ="ORDER";
                 } else if(sMode.equals("Secondary Order") || sMode.equals("VANSALES")  || sMode.equals("POS INVOICE") || sMode.equals("INVOICE"))
                {
                    sHead =(Addinf)?"TEMPORARY INVOICE":"TAX INVOICE";
                }else if( sMode.equals("PROJECTION") ){
                    sHead = "PROJECTION";
                }else if( sMode.equalsIgnoreCase("Order") ){
                    sHead = "ORDER";
                }
                if(sHead!=""){
                    printama.setBold();
                    printama.printTextln(Printama.CENTER, sHead);
                    printama.addNewLine();
                }
                printama.setNormalText();
                printama.printTextln(" ");
                printama.setWideTallBold();
                printama.setTallBold();
                printama.printTextln(Printama.CENTER, tvDistributorName.getText().toString());
                if(Addinf) {
                    printama.setNormalText();
                    printama.setBold();
                    printama.printTextln(Printama.CENTER, "( RC00006194 )");
                }
                printama.addNewLine();
                if (tvDistAdd.getVisibility() == View.VISIBLE) {

                    printama.setNormalText();
                    printama.printTextln(Printama.LEFT, tvDistAdd.getText().toString());
                    printama.addNewLine(1);
                }
                if (tvDistributorPh.getVisibility() == View.VISIBLE){
                    printama.setNormalText();

                    printama.printTextln(Printama.LEFT, tvDistributorPh.getText().toString() + "               " + invoicedate.getText().toString());
                    printama.addNewLine(1);
                }

                printama.addNewLine(1);

                if (tvDisGST.getVisibility() == View.VISIBLE && ! tvDisGST.getText().toString().equalsIgnoreCase("")){
                    printama.setNormalText();
                    printama.printTextln(Printama.LEFT, "GST NO: " + tvDisGST.getText().toString());
                }

                    if(!(sMode.equals("Primary Order"))) {
                        printama.addNewLine(1);
                        printama.printDashedLine();
                        printama.addNewLine(1);
                        printama.setBold();
                        printama.printTextln(Printama.LEFT, "BILL TO :");
                        printama.printTextln(Printama.LEFT, retailername.getText().toString());
                        printama.setNormalText();
                        if(!(sMode.equals("POS INVOICE"))) {
                            printama.printTextln(Printama.LEFT, "Customer Code :" + RetailCode);
                            if (retaileAddress.getVisibility() == View.VISIBLE) {
                                String[] lines = Split(retaileAddress.getText().toString(), 90, retaileAddress.getText().toString().length());
                                for (int i = 0; i < lines.length; i++) {
                                    System.out.println("lines[" + i + "]: (len: " + lines[i].length() + ") : " + lines[i]);
                                    printama.printTextln(Printama.LEFT, lines[i]);
                                }
                            }
                            if (tvRetailorPhone.getVisibility() == View.VISIBLE) {
                                printama.printTextln(Printama.LEFT, "Mobile: " + tvRetailorPhone.getText().toString());
                            }

                            if (!tvRetGST.getText().toString().equalsIgnoreCase("")) {
                                printama.printTextln(Printama.LEFT, "GSTN : " + tvRetGST.getText().toString());
                            }
                            if (!tvRetFSSAI.getText().toString().equalsIgnoreCase("")) {
                            printama.printTextln(Printama.LEFT, "FSSAI : " + tvRetFSSAI.getText().toString());
                        }
                        }else{
                            printama.printTextln(Printama.LEFT, "Mobile: " + tvRetailorPhone.getText().toString());
                        }
                    }

                printama.setSmallText();
                printama.printTextln(" ");
                printama.setNormalText();
                printama.setBold();
                printama.printTextln(Printama.LEFT,billnumber.getText().toString());
                if(sMode.equals("Primary Order") || sMode.equals("Secondary Order") || sMode.equals("INVOICE")) {
                    printama.printTextln(Printama.LEFT, "PO No : " + PONo);
                }
//                printama.addNewLine();

                printama.addNewLine();
                printama.printDashedLine();
                printama.setNormalText();
                printama.setBold();

                String sCode="Item";
                String sqtyValue = "Qty";
                String sFqtyValue = "Free";
                String sPCS = "PCS";
                String srateValue = "Price";
                String samtValue = "Amount";

                String sRowTx= sCode + repeat(" ",19-sCode.length()) +repeat(" ",3-sqtyValue.length()) + sqtyValue
                        +repeat(" ",5-sFqtyValue.length()) + sFqtyValue+repeat(" ",4-sPCS.length())+ sPCS +repeat(" ",6-srateValue.length()) + srateValue +repeat(" ",9-samtValue.length()) + samtValue;
                printama.printTextln(sRowTx);
                //printama.printTextln("Item"+repeat(" ",15) + repeat(" ",7) + "Qty" +repeat(" ",8) + "Price" "" + "Total");
                printama.printDashedLine();
                printama.setNormalText();

                for (int i = 0; i < Order_Outlet_Filter.size(); i++) {

                    String[] lines = Split(Order_Outlet_Filter.get(i).getName().toString().trim(), 19, Order_Outlet_Filter.get(i).getName().toString().trim().length());

                    for (int j = 0; j < lines.length; j++) {
                        printama.setBold();
                        String qtyValue = "";
                        String FqtyValue = "";
                        String PCS = "";
                        String rateValue = "";
                        String amtValue = "";
                        String Code=lines[j];
                        if (j==0){
                            qtyValue = String.valueOf(Order_Outlet_Filter.get(i).getQty());
                            FqtyValue = String.valueOf(Order_Outlet_Filter.get(i).getFree());
                            Integer sDp=Integer.parseInt(new DecimalFormat("##0").format(Double.parseDouble(Order_Outlet_Filter.get(i).getConversionFactor())));
                            PCS = String.valueOf((Order_Outlet_Filter.get(i).getQty() * sDp));
                            rateValue = String.valueOf(formatter.format(Order_Outlet_Filter.get(i).getRate()));
                            amtValue = String.valueOf(formatter.format(Order_Outlet_Filter.get(i).getAmount()));
                        }
                        String RowTx= Code + repeat(" ",19-Code.length()) +repeat(" ",3-qtyValue.length()) + qtyValue
                                +repeat(" ",4-FqtyValue.length())+ FqtyValue +repeat(" ",3-PCS.length())+ PCS +repeat(" ",8-rateValue.length()) + rateValue +repeat(" ",9-amtValue.length()) + amtValue;
                        printama.printTextln(RowTx);
                    }
                    printama.setNormalText();
                    printama.printTextln(Order_Outlet_Filter.get(i).getHSNCode().toString());
                    printama.setSmallText();
                    printama.printTextln(" ");
                    printama.setNormalText();

                }

                printama.setNormalText();
                printama.printDashedLine();
                printama.addNewLine(2);

                String subTotal = "           " + formatter.format(subTotalVal);
                String tsubTotal = "           " + formatter.format(tsubTotalVal);
                String totItem = "           " + totalitem.getText().toString();
                String totqty = "           " + totalqty.getText().toString();
                String discount = "           " + cashDisc;
                String outstand = "           " + outstandAmt;

                printama.printText("Total" + "                            " + subTotal.substring(String.valueOf(formatter.format(subTotalVal)).length(), subTotal.length()));

                printama.printDashedLine();
                    printama.addNewLine();
                printama.printText("Taxable Amount" + "                      " + tsubTotal.substring(String.valueOf(formatter.format(tsubTotalVal)).length(), subTotal.length()));
                printama.addNewLine();
                printama.printText("Total Qty" + "                           " + totqty.substring(totalqty.getText().toString().length(), totqty.length()));
                printama.addNewLine();

                if (uomList != null) {
                    for (int i = 0; i < uomList.size(); i++) {
                        String uomQty = "           " + (int) uomList.get(i).getCnvQty();
                        printama.printText(uomList.get(i).getUOM_Nm() + "                         " + uomQty.substring(String.valueOf((int) uomList.get(i).getCnvQty()).toString().length(), uomQty.length()));
                        printama.addNewLine();

                    }

                }


                //  printama.printText("Gst Rate" + "                       " + gst.substring(gstrate.getText().toString().length(), gst.length()));
                for (int i = 0; i < taxList.size(); i++) {
                    String val = String.valueOf(formatter.format(taxList.get(i).getTax_Amt()));
                    String amt = "           " + val;
                    String type = taxList.get(i).getTax_Type() + "                               ";
                    printama.printText(type.substring(0, type.length() - taxList.get(i).getTax_Type().length()) + amt.substring(val.length(), amt.length()));
                    printama.addNewLine();

                }
//                if (tvOutstanding.getVisibility() == View.VISIBLE) {
//                    printama.printText("Outstanding" + "                    " + outstand.substring(String.valueOf(outstandAmt).length(),
//                            outstand.length()));
//                    printama.addNewLine();
//                }

                if (cashDisc > 0) {
                    printama.printText("Cash Discount" + "                       " + discount.substring(String.valueOf(cashDisc).length(), discount.length()));
                    printama.addNewLine();
                }

                printama.addNewLine(1.5);
                printama.setTallBold();
                String strAmount = "       " +CurrencySymbol+" "+ formatter.format(NetTotAmt);
                printama.printDashedLine();
//
                printama.printTextln("Net amount" + "                          " + strAmount.substring(String.valueOf(NetTotAmt).length(), strAmount.length()));
                printama.printDashedLine();
//                printama.printLine();
                printama.addNewLine(2);
                if(sMode.equals("Primary Order") || sMode.equals("Secondary Order") || sMode.equals("VANSALES")   || sMode.equals("INVOICE"))
                {
                    if(Addinf) {
                        printama.setBold();
                        printama.printTextln(" ");
                        printama.printTextln(Printama.LEFT, "For " + tvDistributorName.getText().toString());
                        printama.printTextln(" ");
                        printama.printTextln(" ");
                        printama.printDashedLine();
                        printama.printTextln(Printama.LEFT, "Authorized signature");

                        printama.printTextln(" ");
                        printama.printTextln(" ");
                        printama.setBold();
                        printama.printTextln(Printama.LEFT, "For " + retailername.getText().toString());
                        printama.printTextln(" ");
                        printama.printTextln(" ");
                        printama.printDashedLine();
                        printama.printTextln(Printama.LEFT, "Receiver's Signature & Stamp");
                        printama.printTextln(" ");
                        printama.printDashedLine();
                        printama.setNormalText();
                        printama.printTextln(Printama.LEFT, "Note :");
                        printama.printTextln(Printama.LEFT, "1.Cheques should be crossed and made payable");
                        printama.printTextln(Printama.LEFT, "  to \"" + tvDistributorName.getText().toString() + "\"");
                        printama.printTextln(Printama.LEFT, "2.All goods must be carefully checked before signing.");
                        printama.printTextln(Printama.LEFT, "3.Goods are not returnable, refundable or exchangeable upon acceptance.");
                        printama.printTextln(" ");
                        printama.printTextln(" ");
                        printama.printTextln(" ");
                        printama.printTextln(" ");
                        printama.addNewLine();
                    }
                }else{
                    printama.setBold();
                    printama.printTextln(Printama.CENTER, "Thank You! Visit Again");
                    printama.addNewLine();
                }

                printama.setLineSpacing(3);
                printama.printTextln(" ");
                printama.printTextln(" ");
                printama.feedPaper();
                printama.close();
            });
        } catch (Exception e) {
            Log.e("PRINT: ", e.getMessage());
        }
    }
    private void showPrinterList() {
        Printama.showPrinterList(this, R.color.dark_blue, printerName -> {
            Toast.makeText(this, printerName, Toast.LENGTH_SHORT).show();
            // TextView connectedTo = findViewById(R.id.tv_printer_info);
            String text = "Connected to : " + printerName;
            //connectedTo.setText(text);
            if (!printerName.contains("failed")) {
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//                findViewById(R.id.btn_printer_test).setVisibility(View.VISIBLE);
                //findViewById(R.id.btn_printer_test).setOnClickListener(v -> testPrinter());
            }
        });
    }
    private void getSavedPrinter() {
        BluetoothDevice connectedPrinter = Printama.with(this).getConnectedPrinter();
        if (connectedPrinter != null) {
            //  TextView connectedTo = findViewById(R.id.tv_printer_info);
            //   String text = "Connected to : " + connectedPrinter.getName();
            // connectedTo.setText(text);
        }
    }
    private void createPdf() {
        try {
            int hgt = 1000 + (Order_Outlet_Filter.size() * 40);

            // create a new document
            PdfDocument document = new PdfDocument();
            int widthSize = 650;
            // crate a page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(widthSize, hgt, 1).create();
            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            int Margin=10;
            int x = 10;
            int y = 30;
            float wdth=widthSize-10;
            Rect bounds = new Rect();
            String sMode=sharedCommonPref.getvalue(Constants.FLAG);
            paint.setColor(Color.BLACK);
            String sHead = sMode.toUpperCase();

            if(sMode.equalsIgnoreCase("Primary Order"))
            {
                sHead = "ORDER";
            }else if(sMode.equalsIgnoreCase("Secondary Order") || sMode.equalsIgnoreCase("VANSALES")  || sMode.equalsIgnoreCase("INVOICE"))
            {
                sHead = ((Addinf)?"TEMPORARY":"TAX")+" INVOICE";
            }
            else if(sMode.equalsIgnoreCase("POS INVOICE")){

                sHead = "COUNTER SALES INVOICE";
            }

            if(sHead!=""){
                paint.setTextSize(15);
                paint.getTextBounds(sHead, 0, sHead.length(), bounds);
                paint.setFakeBoldText(true);
                canvas.drawText(sHead, widthSize - (bounds.width() + Margin), y, paint);
            }
            paint.setTextSize(13);

            canvas.drawText(tvDistributorName.getText().toString(), x, y, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(12);
            paint.setTextAlign(Paint.Align.LEFT);

            y = y + 20;

            paint.setFakeBoldText(false);
            if (tvDistAdd.getVisibility() == View.VISIBLE) {
                paint.setColor(Color.DKGRAY);
                paint.setTextSize(12);

                String[] lines = Split(tvDistAdd.getText().toString(), 90, tvDistAdd.getText().toString().length());
                for (int i = 0; i < lines.length; i++) {
                    System.out.println("lines[" + i + "]: (len: " + lines[i].length() + ") : " + lines[i]);
                    canvas.drawText(lines[i], x, y, paint);
                    y = y + 20;

                }
            }
            if (tvDistributorPh.getVisibility() == View.VISIBLE) {
                canvas.drawText("Mobile: "+ tvDistributorPh.getText().toString(), x, y, paint);
                y = y + 20;
            }

            if(!tvDisGST.getText().toString().equalsIgnoreCase("")) {
                canvas.drawText("GSTN : " + tvDisGST.getText().toString(), x, y, paint);
                y = y + 20;
            }

            if(!tvDisFSSAI.getText().toString().equalsIgnoreCase("")) {
                canvas.drawText("FSSAI : " + tvDisFSSAI.getText().toString(), x, y, paint);
                y = y + 20;
            }
            if(!(sMode.equalsIgnoreCase("Primary Order")))
            {
                paint.setColor(Color.LTGRAY);
                paint.setStrokeWidth(1);
                canvas.drawLine(0, y, widthSize, y, paint);

                paint.setFakeBoldText(true);
                y = y + 20;
                paint.setColor(Color.BLACK);
                paint.setTextSize(13);
                canvas.drawText("BILL TO", x, y, paint);

                y = y + 18;
                canvas.drawText(retailername.getText().toString(), x, y, paint);
                if(!(sMode.equalsIgnoreCase("POS INVOICE")))
                {
                paint.setColor(Color.BLACK);
                paint.setTextSize(12);
                paint.setTextAlign(Paint.Align.LEFT);
                y = y + 18;
                canvas.drawText("Customer Code :" + RetailCode.toString(), x, y, paint);

                y = y + 20;
                paint.setFakeBoldText(false);
                if (retaileAddress.getVisibility() == View.VISIBLE) {
                    paint.setColor(Color.DKGRAY);
                    paint.setTextSize(12);

                    String[] lines = Split(retaileAddress.getText().toString(), 90, retaileAddress.getText().toString().length());
                    for (int i = 0; i < lines.length; i++) {
                        System.out.println("lines[" + i + "]: (len: " + lines[i].length() + ") : " + lines[i]);
                        canvas.drawText(lines[i], x, y, paint);
                        y = y + 20;

                    }
                }
                if (tvRetailorPhone.getVisibility() == View.VISIBLE) {
                    canvas.drawText("Mobile: "+ tvRetailorPhone.getText().toString(), x, y, paint);
                    y = y + 10;
                }

                if(!tvRetGST.getText().toString().equalsIgnoreCase("")) {
                    canvas.drawText("GSTN : " + tvRetGST.getText().toString(), x, y, paint);
                    y = y + 20;
                }
                if(!tvRetFSSAI.getText().toString().equalsIgnoreCase("")) {
                canvas.drawText("FSSAI : " + tvRetFSSAI.getText().toString(), x, y, paint);
                y = y + 20;
            }
                }else{
                    y = y + 20;
                    if (tvRetailorPhone.getVisibility() == View.VISIBLE) {
                        canvas.drawText("Mobile: "+ tvRetailorPhone.getText().toString(), x, y, paint);
                        y = y + 10;
                    }
                }
            }
//            paint.setColor(Color.LTGRAY);
//            paint.setStrokeWidth(30);
//            canvas.drawLine(0, y + 30, widthSize, y + 30, paint);


            y = y + 35;
            paint.setColor(Color.BLACK);
            paint.setTextSize(11);
            paint.setFakeBoldText(true);
            canvas.drawText("" + billnumber.getText().toString(), x, y, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("" + invoicedate.getText().toString(), wdth, y, paint);

            paint.setTextAlign(Paint.Align.LEFT);
            if(sMode.equals("Primary Order") || sMode.equals("Secondary Order") || sMode.equals("INVOICE")) {
                y = y + 20;
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.BLACK);
                paint.setTextSize(11);
                paint.setFakeBoldText(true);
                canvas.drawText("PO No : " + PONo, x, y, paint);
            }

            y = y + 25;
            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);


            String space = "    ";
            y = y + 20;
            paint.setColor(Color.BLACK);
            paint.setTextSize(13);
            String sText="Item";
            canvas.drawText("Item", x, y, paint);

            float xTot,xCGST,xSGST,xGST,xPr,xQt,xFQt,xHSN,xMRP,xPCS,xQtU;
            paint.setTextAlign(Paint.Align.RIGHT);
            sText="___Total";
            paint.getTextBounds(sText, 0, sText.length(), bounds);
            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xTot=wdth;
            wdth=wdth-bounds.width();
            wdth = wdth-10;

            sText="__Price";
            paint.getTextBounds(sText, 0, sText.length(), bounds);
            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xPr=wdth;
            wdth = wdth-bounds.width();
            wdth = wdth-10;

            sText="_PCS";
            paint.getTextBounds(sText, 0, sText.length(), bounds);
            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xPCS=wdth;
            wdth = wdth-bounds.width();
            wdth = wdth-3;

            sText="_Free";
            paint.getTextBounds(sText, 0, sText.length(), bounds);
            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xFQt=wdth;
            wdth = wdth-bounds.width();
            wdth = wdth-3;

            sText="_Qty";
            paint.getTextBounds(sText, 0, sText.length(), bounds);
            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xQt=wdth;
            wdth = wdth-bounds.width();
            wdth = wdth-3;

            sText="_UOM";
            paint.getTextBounds(sText, 0, sText.length(), bounds);
            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xQtU=wdth;
            wdth = wdth-bounds.width();
            wdth = wdth-3;
            xMRP = wdth;
            if(!sMode.equals("Primary Order")) {
                sText = "_" + MRPCap; // RRP -> MRP
                paint.getTextBounds(sText, 0, sText.length(), bounds);
                canvas.drawText(sText.replaceAll("_", ""), wdth, y, paint);
                xMRP = wdth;
                wdth = wdth - bounds.width();
                wdth = wdth - 2;
                paint.setTextAlign(Paint.Align.LEFT);
            }
            sText="Code__________________";
            paint.getTextBounds(sText, 0, sText.length(), bounds);
            wdth = wdth-bounds.width();
            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xHSN=wdth;
            wdth = wdth-2;

            y = y + 10;
            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);

            paint.setFakeBoldText(false);
            y = y + 20;
            JSONArray jFreeSmry=new JSONArray();String soffp="";
            for (int i = 0; i < Order_Outlet_Filter.size(); i++) {

                paint.setTextAlign(Paint.Align.LEFT);
                y = y + 5;
                paint.setColor(Color.DKGRAY);
                paint.setTextSize(12);
                float cy=y;
                String[] lines = Split(Order_Outlet_Filter.get(i).getName(), 30, Order_Outlet_Filter.get(i).getName().length());
                for (int j = 0; j < lines.length; j++) {
                    System.out.println("lines[" + j + "]: (len: " + lines[j].length() + ") : " + lines[j]);
                    canvas.drawText(lines[j], x, y, paint);
                    y = y + 20;

                }
                canvas.drawText("" + Order_Outlet_Filter.get(i).getHSNCode(), xHSN, cy, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                if(!sMode.equals("Primary Order")) {
                    canvas.drawText("" + formatter.format(Double.parseDouble(Order_Outlet_Filter.get(i).getMRP())), xMRP, cy, paint);
                }
                canvas.drawText("" + Order_Outlet_Filter.get(i).getUnitCode(), xQtU, cy, paint);
                canvas.drawText("" + Order_Outlet_Filter.get(i).getQty(), xQt, cy, paint);
                canvas.drawText("" + Order_Outlet_Filter.get(i).getFree(), xFQt, cy, paint);
                Integer sDp=Integer.parseInt(new DecimalFormat("##0").format(Double.parseDouble(Order_Outlet_Filter.get(i).getConversionFactor())));
                canvas.drawText(String.valueOf(Order_Outlet_Filter.get(i).getQty() * sDp), xPCS, cy, paint);
                if(Double.parseDouble(Order_Outlet_Filter.get(i).getFree())>0){
                    if((";"+soffp).indexOf(";"+Order_Outlet_Filter.get(i).getOff_Pro_name()+";")<0){
                        soffp=Order_Outlet_Filter.get(i).getOff_Pro_name()+";";
                        JSONObject jitm=new JSONObject();
                        jitm.put("OffName",Order_Outlet_Filter.get(i).getOff_Pro_name());
                        jitm.put("free",Order_Outlet_Filter.get(i).getFree());
                        jFreeSmry.put(jitm);
                    }
                    else{
                        for(int ilf=0;ilf<jFreeSmry.length();ilf++){
                            if(jFreeSmry.getJSONObject(ilf).getString("OffName")==Order_Outlet_Filter.get(i).getOff_Pro_name()){
                                double xfre=Double.parseDouble(jFreeSmry.getJSONObject(ilf).getString("free"));
                                double cfre=Double.parseDouble(Order_Outlet_Filter.get(i).getFree());
                                jFreeSmry.getJSONObject(ilf).put("free",String.valueOf(xfre+cfre));
                            }
                        }
                    }

                }
                canvas.drawText("" + formatter.format(Order_Outlet_Filter.get(i).getRate()), xPr, cy, paint);
                canvas.drawText("" + formatter.format(Order_Outlet_Filter.get(i).getAmount()), xTot, cy, paint);


            }


            paint.setTextAlign(Paint.Align.LEFT);
            y = y + 5;
            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);

            y = y + 20;
            paint.setColor(Color.GRAY);
            paint.setTextSize(12);
            canvas.drawText("PRICE DETAILS", x, y, paint);

            y = y +10;
            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);

            paint.setColor(Color.DKGRAY);

            y = y + 30;
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Total", x, y, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(subtotal.getText().toString(), xTot, y, paint);

            y = y +10;
            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);

            paint.setColor(Color.DKGRAY);

            y = y + 20;
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Total Item", x, y, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(totalitem.getText().toString(), xTot, y, paint);
            if(!sMode.equals("Primary Order")) {
                y = y + 20;
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("Total Qty", x, y, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(totalqty.getText().toString(), xTot, y, paint);
            }
//            if (uomList != null) {
//                for (int i = 0; i < uomList.size(); i++) {
//                    y = y + 20;
//                    paint.setTextAlign(Paint.Align.LEFT);
//                    canvas.drawText(uomList.get(i).getUOM_Nm(), x, y, paint);
//                    paint.setTextAlign(Paint.Align.RIGHT);
//                    canvas.drawText("" + (int) uomList.get(i).getCnvQty(), xTot, y, paint);
//                }
//
//            }

            y = y + 30;
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Taxable Amount", x, y, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(baseAmt.getText().toString(), xTot, y, paint);

//            y = y + 30;
//            canvas.drawText("Gst Rate", x, y, paint);
//            canvas.drawText(gstrate.getText().toString(), (widthSize / 2) + 150, y, paint);
//
            for (int i = 0; i < taxList.size(); i++) {
                y = y + 20;
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(taxList.get(i).getTax_Type(), x, y, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(CurrencySymbol+" " + formatter.format(taxList.get(i).getTax_Amt()), xTot, y, paint);

            }

            y = y + 20;

//            if (tvOutstanding.getVisibility() == View.VISIBLE) {
//                canvas.drawText("Outstanding", x, y, paint);
//                canvas.drawText(tvOutstanding.getText().toString(), (widthSize / 2) + 150, y, paint);
//                y = y + 30;
//            }

            if (cashDisc > 0) {
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("Cash Discount", x, y, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(cashdiscount.getText().toString(), xTot, y, paint);
                //netamount=netamount - Double.parseDouble(cashdiscount.getText().toString());
                y = y + 20;
            }

            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);

            paint.setTextSize(15);
            paint.setColor(Color.BLACK);
            y = y + 30;
            paint.setFakeBoldText(true);
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Net Amount", x, y, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(netamount.getText().toString(), xTot, y, paint);

            paint.setFakeBoldText(false);
            y = y + 20;
            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);
            if(jFreeSmry.length()>0) {
                y = y + 50;
                paint.setFakeBoldText(true);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.BLACK);
                canvas.drawText("Free Invoice Details", x, y, paint);

                paint.setFakeBoldText(false);
                y = y + 10;
                paint.setColor(Color.LTGRAY);
                paint.setStrokeWidth(1);
                canvas.drawLine(0, y, widthSize, y, paint);

                paint.setTextAlign(Paint.Align.LEFT);
                y = y + 20;
                paint.setColor(Color.DKGRAY);
                paint.setTextSize(12);
                float cy = y;
                for (int j = 0; j < jFreeSmry.length(); j++) {
                    canvas.drawText(jFreeSmry.getJSONObject(j).getString("OffName"), x, y, paint);
                    canvas.drawText(jFreeSmry.getJSONObject(j).getString("free"), widthSize - 50, y, paint);
                    y = y + 20;

                }
            }
            if(Addinf) {
                paint.setColor(Color.BLACK);
                y = y + 30;
                paint.setFakeBoldText(true);
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("For " + tvDistributorName.getText().toString(), x, y, paint);

                y = y + 60;
                paint.setFakeBoldText(true);
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("Authorized signature", x, y, paint);
                paint.setFakeBoldText(false);
                y = y + 20;
                paint.setColor(Color.LTGRAY);
                paint.setStrokeWidth(1);
                canvas.drawLine(0, y, widthSize, y, paint);
                y = y + 30;
                paint.setColor(Color.BLACK);
                paint.setFakeBoldText(true);
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("For " + retailername.getText().toString(), x, y, paint);

                y = y + 60;
                paint.setFakeBoldText(true);
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("Receiver's Signature & Stamp", x, y, paint);

                paint.setFakeBoldText(false);
                y = y + 20;
                paint.setColor(Color.LTGRAY);
                paint.setStrokeWidth(1);
                canvas.drawLine(0, y, widthSize, y, paint);

                paint.setColor(Color.BLACK);
                y = y + 30;
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("Note :", x, y, paint);
                y = y + 30;
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("1.Cheques should be crossed and made payable to \"" + tvDistributorName.getText().toString() + "\"", x, y, paint);
                y = y + 30;
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("2.All goods must be carefully checked before signing.", x, y, paint);
                y = y + 30;
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("3.Goods are not returnable, refundable or exchangeable upon acceptance.", x, y, paint);
            }
            y = y + 30;
            //paint.setColor(Color.parseColor("#008000"));
            //paint.setTextSize(15);

            //paint.setTextAlign(Paint.Align.CENTER);
            //canvas.drawText("Thank You! Visit Again", widthSize/2, y, paint);



            //canvas.drawt
            // finish the page
            document.finishPage(page);



// draw text on the graphics object of the page
            // Create Page 2
//        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
//        page = document.startPage(pageInfo);
//        canvas = page.getCanvas();
//        paint = new Paint();
//        paint.setColor(Color.BLUE);
//        canvas.drawCircle(100, 100, 100, paint);
//        document.finishPage(page);


            // write the document content
            String directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hap/";
            File file = new File(directory_path);

            deleteRecursive(file);

            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + System.currentTimeMillis() + "bill.pdf";
            File filePath = new File(targetPdf);


            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();

            // close the document
            document.close();


            Uri fileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", filePath);


            Intent intent = ShareCompat.IntentBuilder.from(this)
                    .setType("*/*")
                    .setStream(fileUri)
                    .setChooserTitle("Choose bar")
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(intent);
        } catch (Exception e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }
//    private void createPdf() {
//        try {
//            int hgt = 1000 + (Order_Outlet_Filter.size() * 40);
//
//            // create a new document
//            PdfDocument document = new PdfDocument();
//            int widthSize = 650;
//            // crate a page description
//            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(widthSize, hgt, 1).create();
//            // start a page
//            PdfDocument.Page page = document.startPage(pageInfo);
//            Canvas canvas = page.getCanvas();
//            Paint paint = new Paint();
//
//            int Margin=10;
//            int x = 10;
//            int y = 30;
//            float wdth=widthSize-10;
//            Rect bounds = new Rect();
//        String sMode=sharedCommonPref.getvalue(Constants.FLAG);
//            paint.setColor(Color.BLACK);
//            String sHead = "";
//
//            if(sMode.equals("Primary Order") || sMode.equals("Secondary Order") || sMode.equals("VANSALES")  || sMode.equals("POS INVOICE") || sMode.equals("INVOICE"))
//            {
//                sHead = "INVOICE";
//            }else if( sMode.equals("PROJECTION") ){
//                sHead = "PROJECTION";
//            }else if( sMode.equalsIgnoreCase("Order") ){
//                sHead = "ORDER";
//            }
//            if(sHead!=""){
//                paint.setTextSize(15);
//                paint.getTextBounds(sHead, 0, sHead.length(), bounds);
//                paint.setFakeBoldText(true);
//                canvas.drawText(sHead, widthSize - (bounds.width() + Margin), y, paint);
//            }
//            paint.setTextSize(13);
//
//            canvas.drawText(tvDistributorName.getText().toString(), x, y, paint);
//
//            paint.setColor(Color.BLACK);
//            paint.setTextSize(12);
//            paint.setTextAlign(Paint.Align.LEFT);
//
//            y = y + 20;
//
//            paint.setFakeBoldText(false);
//            if (tvDistAdd.getVisibility() == View.VISIBLE) {
//                paint.setColor(Color.DKGRAY);
//                paint.setTextSize(12);
//
//                String[] lines = Split(tvDistAdd.getText().toString(), 90, tvDistAdd.getText().toString().length());
//                for (int i = 0; i < lines.length; i++) {
//                    System.out.println("lines[" + i + "]: (len: " + lines[i].length() + ") : " + lines[i]);
//                    canvas.drawText(lines[i], x, y, paint);
//                    y = y + 20;
//
//                }
//            }
//            if (tvDistributorPh.getVisibility() == View.VISIBLE) {
//                canvas.drawText("Mobile: "+ tvDistributorPh.getText().toString(), x, y, paint);
//                y = y + 20;
//            }
//
//            if(!tvDisGST.getText().toString().equalsIgnoreCase("")) {
//                canvas.drawText("GSTN : " + tvDisGST.getText().toString(), x, y, paint);
//                y = y + 20;
//            }
//
//            if(!tvDisFSSAI.getText().toString().equalsIgnoreCase("")) {
//                canvas.drawText("FSSAI : " + tvDisFSSAI.getText().toString(), x, y, paint);
//                y = y + 20;
//            }
//            paint.setColor(Color.LTGRAY);
//            paint.setStrokeWidth(1);
//            canvas.drawLine(0, y, widthSize, y, paint);
//
//            paint.setFakeBoldText(true);
//            y = y + 20;
//            paint.setColor(Color.BLACK);
//            paint.setTextSize(13);
//            canvas.drawText("BILL TO", x, y, paint);
//
//            y = y + 18;
//            canvas.drawText(retailername.getText().toString(), x, y, paint);
//
//            paint.setColor(Color.BLACK);
//            paint.setTextSize(12);
//            paint.setTextAlign(Paint.Align.LEFT);
//
//            y = y + 20;
//
//            paint.setFakeBoldText(false);
//            if (retaileAddress.getVisibility() == View.VISIBLE) {
//                paint.setColor(Color.DKGRAY);
//                paint.setTextSize(12);
//
//                String[] lines = Split(retaileAddress.getText().toString(), 90, retaileAddress.getText().toString().length());
//                for (int i = 0; i < lines.length; i++) {
//                    System.out.println("lines[" + i + "]: (len: " + lines[i].length() + ") : " + lines[i]);
//                    canvas.drawText(lines[i], x, y, paint);
//                    y = y + 20;
//
//                }
//            }
//            if (tvRetailorPhone.getVisibility() == View.VISIBLE) {
//                canvas.drawText("Mobile: "+ tvRetailorPhone.getText().toString(), x, y, paint);
//                y = y + 10;
//            }
//
//            if(!tvRetGST.getText().toString().equalsIgnoreCase("")) {
//                canvas.drawText("GSTN : " + tvRetGST.getText().toString(), x, y, paint);
//                y = y + 20;
//            }
//            if(!tvRetFSSAI.getText().toString().equalsIgnoreCase("")) {
//                canvas.drawText("FSSAI : " + tvRetFSSAI.getText().toString(), x, y, paint);
//                y = y + 20;
//            }
////
////            paint.setColor(Color.LTGRAY);
////            paint.setStrokeWidth(30);
////            canvas.drawLine(0, y + 30, widthSize, y + 30, paint);
//
//
//            y = y + 35;
//            paint.setColor(Color.BLACK);
//            paint.setTextSize(11);
//            paint.setFakeBoldText(true);
//            canvas.drawText("" + billnumber.getText().toString(), x, y, paint);
//            paint.setTextAlign(Paint.Align.RIGHT);
//            canvas.drawText("" + invoicedate.getText().toString(), wdth, y, paint);
//
//            paint.setTextAlign(Paint.Align.LEFT);
//            y = y + 25;
//            paint.setColor(Color.LTGRAY);
//            paint.setStrokeWidth(1);
//            canvas.drawLine(0, y, widthSize, y, paint);
//
//
//            String space = "    ";
//            y = y + 20;
//            paint.setColor(Color.BLACK);
//            paint.setTextSize(13);
//            String sText="Item";
//            canvas.drawText("Item", x, y, paint);
//
//            float xTot,xCGST,xSGST,xGST,xPr,xQt,xHSN,xMRP,xPCS;
//            paint.setTextAlign(Paint.Align.RIGHT);
//            sText="___Total";
//            paint.getTextBounds(sText, 0, sText.length(), bounds);
//            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xTot=wdth;
//            wdth=wdth-bounds.width();
//            wdth = wdth-10;
//
//            sText="___CGST";
//            paint.getTextBounds(sText, 0, sText.length(), bounds);
//            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xCGST=wdth;
//            wdth = wdth-bounds.width();
//            wdth = wdth-10;
//
//            sText="___SGST";
//            paint.getTextBounds(sText, 0, sText.length(), bounds);
//            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xSGST=wdth;
//            wdth = wdth-bounds.width();
//            wdth = wdth-10;
//
//            sText="_GST";
//            paint.getTextBounds(sText, 0, sText.length(), bounds);
//            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xGST=wdth;
//            wdth = wdth-bounds.width();
//            wdth = wdth-10;
//
//            sText="__Price";
//            paint.getTextBounds(sText, 0, sText.length(), bounds);
//            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xPr=wdth;
//            wdth = wdth-bounds.width();
//            wdth = wdth-10;
//
//            sText="_PCS";
//            paint.getTextBounds(sText, 0, sText.length(), bounds);
//            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xPCS=wdth;
//            wdth = wdth-bounds.width();
//            wdth = wdth-3;
//
//            sText="_Qty";
//            paint.getTextBounds(sText, 0, sText.length(), bounds);
//            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xQt=wdth;
//            wdth = wdth-bounds.width();
//            wdth = wdth-3;
//
//            sText="_MRP";
//            paint.getTextBounds(sText, 0, sText.length(), bounds);
//            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xMRP=wdth;
//            wdth = wdth-bounds.width();
//            wdth = wdth-2;
//            paint.setTextAlign(Paint.Align.LEFT);
//
//            sText="HSN Code_";
//            paint.getTextBounds(sText, 0, sText.length(), bounds);
//            wdth = wdth-bounds.width();
//            canvas.drawText(sText.replaceAll("_",""), wdth, y, paint);xHSN=wdth;
//            wdth = wdth-2;
//
//            y = y + 10;
//            paint.setColor(Color.LTGRAY);
//            paint.setStrokeWidth(1);
//            canvas.drawLine(0, y, widthSize, y, paint);
//
//            paint.setFakeBoldText(false);
//            y = y + 20;
//            for (int i = 0; i < Order_Outlet_Filter.size(); i++) {
//
//                paint.setTextAlign(Paint.Align.LEFT);
//                y = y + 5;
//                paint.setColor(Color.DKGRAY);
//                paint.setTextSize(12);
//                float cy=y;
//                String[] lines = Split(Order_Outlet_Filter.get(i).getName(), 30, Order_Outlet_Filter.get(i).getName().length());
//                for (int j = 0; j < lines.length; j++) {
//                    System.out.println("lines[" + j + "]: (len: " + lines[j].length() + ") : " + lines[j]);
//                    canvas.drawText(lines[j], x, y, paint);
//                    y = y + 20;
//
//                }
//                canvas.drawText("" + Order_Outlet_Filter.get(i).getHSNCode(), xHSN, cy, paint);
//                paint.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText("" + formatter.format(Double.parseDouble(Order_Outlet_Filter.get(i).getMRP())), xMRP, cy, paint);
//                canvas.drawText("" + Order_Outlet_Filter.get(i).getQty(), xQt, cy, paint);
//                Integer sDp=Integer.parseInt(new DecimalFormat("##0").format(Double.parseDouble(Order_Outlet_Filter.get(i).getConversionFactor())));
//                canvas.drawText(String.valueOf(Order_Outlet_Filter.get(i).getQty() * sDp), xPCS, cy, paint);
//                canvas.drawText("" + formatter.format(Order_Outlet_Filter.get(i).getRate()), xPr, cy, paint);
//
//                canvas.drawText("" + formatter.format(Order_Outlet_Filter.get(i).getTaxPer()), xGST, cy, paint);
//                canvas.drawText("" + formatter.format(Order_Outlet_Filter.get(i).getPSGST()), xSGST, cy, paint);
//                canvas.drawText("" + formatter.format(Order_Outlet_Filter.get(i).getPCGST()), xCGST, cy, paint);
//                canvas.drawText("" + formatter.format(Order_Outlet_Filter.get(i).getAmount()), xTot, cy, paint);
//
//
//            }
//
//
//            paint.setTextAlign(Paint.Align.LEFT);
//            y = y + 5;
//            paint.setColor(Color.LTGRAY);
//            paint.setStrokeWidth(1);
//            canvas.drawLine(0, y, widthSize, y, paint);
//
//            y = y + 20;
//            paint.setColor(Color.GRAY);
//            paint.setTextSize(12);
//            canvas.drawText("PRICE DETAILS", x, y, paint);
//
//            y = y +10;
//            paint.setColor(Color.LTGRAY);
//            paint.setStrokeWidth(1);
//            canvas.drawLine(0, y, widthSize, y, paint);
//
//            paint.setColor(Color.DKGRAY);
//
//            y = y + 30;
//            paint.setTextAlign(Paint.Align.LEFT);
//            canvas.drawText("SubTotal", x, y, paint);
//            paint.setTextAlign(Paint.Align.RIGHT);
//            canvas.drawText(subtotal.getText().toString(), xTot, y, paint);
//
//            y = y + 20;
//            paint.setTextAlign(Paint.Align.LEFT);
//            canvas.drawText("Total Item", x, y, paint);
//            paint.setTextAlign(Paint.Align.RIGHT);
//            canvas.drawText(totalitem.getText().toString(), xTot, y, paint);
//            y = y + 20;
//            paint.setTextAlign(Paint.Align.LEFT);
//            canvas.drawText("Total Qty", x, y, paint);
//            paint.setTextAlign(Paint.Align.RIGHT);
//            canvas.drawText(totalqty.getText().toString(), xTot, y, paint);
//
//            if (uomList != null) {
//                for (int i = 0; i < uomList.size(); i++) {
//                    y = y + 20;
//                    paint.setTextAlign(Paint.Align.LEFT);
//                    canvas.drawText(uomList.get(i).getUOM_Nm(), x, y, paint);
//                    paint.setTextAlign(Paint.Align.RIGHT);
//                    canvas.drawText("" + (int) uomList.get(i).getCnvQty(), xTot, y, paint);
//                }
//
//            }
//
////            y = y + 30;
////            canvas.drawText("Gst Rate", x, y, paint);
////            canvas.drawText(gstrate.getText().toString(), (widthSize / 2) + 150, y, paint);
////
//            for (int i = 0; i < taxList.size(); i++) {
//                y = y + 20;
//                paint.setTextAlign(Paint.Align.LEFT);
//                canvas.drawText(taxList.get(i).getTax_Type(), x, y, paint);
//                paint.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText(CurrencySymbol+" " + formatter.format(taxList.get(i).getTax_Amt()), xTot, y, paint);
//
//            }
//
//            y = y + 20;
//
////            if (tvOutstanding.getVisibility() == View.VISIBLE) {
////                canvas.drawText("Outstanding", x, y, paint);
////                canvas.drawText(tvOutstanding.getText().toString(), (widthSize / 2) + 150, y, paint);
////                y = y + 30;
////            }
//
//            if (cashDisc > 0) {
//                paint.setTextAlign(Paint.Align.LEFT);
//                canvas.drawText("Cash Discount", x, y, paint);
//                paint.setTextAlign(Paint.Align.RIGHT);
//                canvas.drawText(cashdiscount.getText().toString(), xTot, y, paint);
//                y = y + 20;
//            }
//
//            paint.setColor(Color.LTGRAY);
//            paint.setStrokeWidth(1);
//            canvas.drawLine(0, y, widthSize, y, paint);
//
//            paint.setTextSize(15);
//            paint.setColor(Color.BLACK);
//            y = y + 30;
//            paint.setFakeBoldText(true);
//            paint.setTextAlign(Paint.Align.LEFT);
//            canvas.drawText("Net Amount", x, y, paint);
//            paint.setTextAlign(Paint.Align.RIGHT);
//            canvas.drawText(netamount.getText().toString(), xTot, y, paint);
//
//            paint.setFakeBoldText(false);
//            y = y + 20;
//            paint.setColor(Color.LTGRAY);
//            paint.setStrokeWidth(1);
//            canvas.drawLine(0, y, widthSize, y, paint);
//
//            y = y + 30;
//            paint.setColor(Color.parseColor("#008000"));
//            paint.setTextSize(15);
//
//
//            paint.setTextAlign(Paint.Align.CENTER);
//            canvas.drawText("Thank You! Visit Again", widthSize/2, y, paint);
//
//
//
//            //canvas.drawt
//            // finish the page
//            document.finishPage(page);
//
//
//
//// draw text on the graphics object of the page
//            // Create Page 2
////        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
////        page = document.startPage(pageInfo);
////        canvas = page.getCanvas();
////        paint = new Paint();
////        paint.setColor(Color.BLUE);
////        canvas.drawCircle(100, 100, 100, paint);
////        document.finishPage(page);
//
//
//            // write the document content
//            String directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hap/";
//            File file = new File(directory_path);
//
//            deleteRecursive(file);
//
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//            String targetPdf = directory_path + System.currentTimeMillis() + "bill.pdf";
//            File filePath = new File(targetPdf);
//
//
//            document.writeTo(new FileOutputStream(filePath));
//            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
//
//            // close the document
//            document.close();
//
//
//            Uri fileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", filePath);
//
//
//            Intent intent = ShareCompat.IntentBuilder.from(this)
//                    .setType("*/*")
//                    .setStream(fileUri)
//                    .setChooserTitle("Choose bar")
//                    .createChooserIntent()
//                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//            startActivity(intent);
//        } catch (Exception e) {
//            Log.e("main", "error " + e.toString());
//            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
//        }
//
//    }
    public static String[] Split(String text, int chunkSize, int maxLength) {
        char[] data = text.toCharArray();
        int len = Math.min(data.length, maxLength);
        String[] result = new String[(len + chunkSize - 1) / chunkSize];
        int linha = 0;
        for (int i = 0; i < len; i += chunkSize) {
            result[linha] = new String(data, i, Math.min(chunkSize, len - i));
            linha++;
        }
        return result;
    }
    void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();

    }
    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null && !apiDataResponse.equals("")) {
                switch (key) {
                    case Constants.TodayOrderDetails_List:
                        orderInvoiceDetailData(apiDataResponse);
                        break;
                    case Constants.ProjectionOrderDetails_List:
                        orderInvoiceDetailData(apiDataResponse);
                        break;
                    case Constants.TodayPrimaryOrderDetails_List:
                        Log.v("pri_res:", apiDataResponse);
                        orderInvoiceDetailData(apiDataResponse);
                        break;
                    case Constants.PosOrderDetails_List:
                        orderInvoiceDetailData(apiDataResponse);
                        break;
                    case Constants.ComplementaryOrderDetails_List:
                        orderInvoiceDetailData(apiDataResponse);
                        break;
                    case Constants.SalesReturnDetailsList:
                        orderInvoiceDetailData(apiDataResponse);
                        break;
                    case Constants.OUTSTANDING:
                        JSONObject jsonObject = new JSONObject(apiDataResponse);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                outstandAmt = Double.parseDouble(new DecimalFormat("##0.00").format(
                                        jsonArray.getJSONObject(i).getDouble("Outstanding")));

                            }

                        }

                        tvOutstanding.setText(CurrencySymbol+" " + formatter.format(outstandAmt));
                        break;
                }
            }
        } catch (Exception e) {

        }

    }
    void orderInvoiceDetailData(String response) {
        try {
            if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("POS INVOICE"))
                Shared_Common_Pref.TransSlNo = Shared_Common_Pref.TransSlNo.replace("HAPH", "");

            Order_Outlet_Filter = new ArrayList<>();
            Order_Outlet_Filter.clear();

            int total_qtytext = 0;
            double tcsVal = 0;
            subTotalVal = 0.00;

            if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("Projection")) {
                billnumber.setText(Shared_Common_Pref.TransSlNo);
            }
            else if (sPMode.equalsIgnoreCase("Primary Order")) {
                billnumber.setText("Order No: " + Shared_Common_Pref.TransSlNo);
            }else {
                billnumber.setText("Bill No: " + Shared_Common_Pref.TransSlNo);
            }

            taxList = new ArrayList<>();
            taxList.clear();

            if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("Return Invoice")) {
                JSONArray arr = new JSONArray(response);
                for (int a = 0; a < arr.length(); a++) {
                    List<Product_Details_Modal> pmTax = new ArrayList<>();
                    JSONObject obj = arr.getJSONObject(a);
                    total_qtytext += obj.getInt("Qty");
                    double amt = 0;

                    double taxAmt = 0.00, sTaxV = 0.0, SGSTAmt = 0.00, CGSTAmt = 0.00;

                    String taxRes = sharedCommonPref.getvalue(Constants.TAXList);
                    if (!Common_Class.isNullOrEmpty(taxRes)) {
                        JSONObject jsonObject = new JSONObject(taxRes);
                        JSONArray jsonArray = jsonObject.getJSONArray("Data");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (jsonObject1.getString("Product_Detail_Code").equals(obj.getString("PCode"))) {
                                if (jsonObject1.getDouble("Tax_Val") > 0) {
                                    double taxCal = (obj.getInt("Qty") * obj.getDouble("Price")) *
                                            ((jsonObject1.getDouble("Tax_Val") / 100));

                                    pmTax.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"),
                                            jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), taxCal));


                                    String label = jsonObject1.getString("Tax_Type");

                                    taxAmt += taxCal;
                                    sTaxV += jsonObject1.getDouble("Tax_Val");
                                    if (label.equalsIgnoreCase("SGST")) {
                                        SGSTAmt += taxCal;
                                    }
                                    if (label.equalsIgnoreCase("CGST")) {
                                        CGSTAmt += taxCal;
                                    }
                                    if (taxList.size() == 0) {
                                        taxList.add(new Product_Details_Modal(label, taxCal));
                                    } else {

                                        boolean isDuplicate = false;
                                        for (int totTax = 0; totTax < taxList.size(); totTax++) {
                                            if (taxList.get(totTax).getTax_Type().equals(label)) {
                                                double oldAmt = taxList.get(totTax).getTax_Amt();
                                                isDuplicate = true;
                                                taxList.set(totTax, new Product_Details_Modal(label, oldAmt + taxCal));

                                            }
                                        }

                                        if (!isDuplicate) {
                                            taxList.add(new Product_Details_Modal(label, taxCal));
                                        }
                                    }

                                }
                            }
                        }

                        amt = ((obj.getInt("Qty") * obj.getDouble("Price"))) + taxAmt;

                        //tsubTotalVal+=((obj.getInt("Qty") * obj.getDouble("Price")))- Double.parseDouble(obj.getString("discount_price")) ;
                        subTotalVal += amt;
                    }
                    Order_Outlet_Filter.add(new Product_Details_Modal(obj.getString("PCode"), obj.getString("PDetails"), obj.getString("MRP"), obj.getString("HSN_Code"), 1, "1",
                            "1", "5", "", 0, "0", obj.getDouble("Price"), obj.getString("PTR"),
                            obj.getInt("Qty"), obj.getInt("Qty"), amt, pmTax, "0", (taxAmt), sTaxV, SGSTAmt, CGSTAmt, obj.getString("ConversionFactor"), obj.getString("discount_price"), obj.getString("Offer_ProductCd"), obj.getString("Offer_ProductNm"), obj.getString("off_pro_unit")));


                }
                mReportViewAdapter = new Print_Invoice_Adapter(Print_Invoice_Activity.this, Order_Outlet_Filter, sharedCommonPref.getvalue(Constants.FLAG));
                rvReturnInv.setAdapter(mReportViewAdapter);

            } else {
                if (sharedCommonPref.getvalue(Constants.FLAG).equals("Primary Order")) {

                    JSONObject primObj = new JSONObject(response);

                    JSONArray orderArr = primObj.getJSONArray("OrderData");
                    JSONArray uomArr = primObj.getJSONArray("uom_details");

                    for (int i = 0; i < orderArr.length(); i++) {
                        JSONObject obj = orderArr.getJSONObject(i);
                        total_qtytext += obj.getInt("Quantity");
                        subTotalVal += (obj.getDouble("value"));
                        tsubTotalVal=obj.getDouble("SubTotal");

                        tcsVal = obj.getDouble("TCS");

                        String paidAmt = "0";
                        try {
                            paidAmt = obj.getString("PaidAmount");
                        } catch (Exception e) {
                            Log.e("jhbdreg", e.getMessage());
                        }

                        double taxAmt = 0.00, sTaxV = 0.0, SGSTAmt = 0.00, CGSTAmt = 0.00;
                        try {
                            JSONArray taxArr = obj.getJSONArray("TAX_details");
                            for (int tax = 0; tax < taxArr.length(); tax++) {
                                JSONObject taxObj = taxArr.getJSONObject(tax);
                                String label = taxObj.getString("Tax_Name");
                                Double amt = taxObj.getDouble("Tax_Amt");

                                //TaxPer +=taxObj.getDouble("Tax_Val");
                                taxAmt += amt;
                                sTaxV += taxObj.getDouble("Tax_Val");
                                if (label.equalsIgnoreCase("SGST")) {
                                    SGSTAmt += amt;
                                }
                                if (label.equalsIgnoreCase("CGST")) {
                                    CGSTAmt += amt;
                                }
                                if (taxList.size() == 0) {
                                    if (tcsVal > 0)
                                        taxList.add(new Product_Details_Modal("TCS", tcsVal));
                                    taxList.add(new Product_Details_Modal(label, amt));
                                } else {

                                    boolean isDuplicate = false;
                                    for (int totTax = 0; totTax < taxList.size(); totTax++) {
                                        if (taxList.get(totTax).getTax_Type().equals(label)) {
                                            double oldAmt = taxList.get(totTax).getTax_Amt();
                                            isDuplicate = true;
                                            taxList.set(totTax, new Product_Details_Modal(label, oldAmt + amt));

                                        }
                                    }

                                    if (!isDuplicate) {
                                        taxList.add(new Product_Details_Modal(label, amt));
                                    }
                                }

                            }
                        } catch (Exception e) {
                            Log.e("jhbdreg", e.getMessage());
                        }
                        Order_Outlet_Filter.add(new Product_Details_Modal(obj.getString("Product_Code"), obj.getString("Product_Name"), obj.optString("MRP"), obj.getString("HSN_Code"), 1, "1",
                                "1", "5", obj.getString("UOM"), 0, "0", obj.getDouble("Rate"), obj.optString("PTR"),
                                obj.getInt("Quantity"), obj.getInt("qty"), obj.getDouble("value"), taxList, paidAmt, (taxAmt), sTaxV, SGSTAmt, CGSTAmt, obj.getString("Conf_Fac"), obj.getString("discount_price"), obj.getString("Offer_ProductCd"), obj.getString("Offer_ProductNm"), obj.getString("off_pro_unit")));


                    }

                    if (taxList.size() == 0 && tcsVal > 0)
                        taxList.add(new Product_Details_Modal("TCS", tcsVal));

                    uomList = new ArrayList<>();

                    for (int i = 0; i < uomArr.length(); i++) {
                        JSONObject obj = uomArr.getJSONObject(i);
                        uomList.add(new Product_Details_Modal(obj.getInt("uom_qty"), obj.getString("uom_name")));
                    }

                    if (uomList != null && uomList.size() > 0) {
                        findViewById(R.id.rlUomParent).setVisibility(View.GONE);
                        TextView tvUOMName = findViewById(R.id.tvUomLabel);
                        TextView tvUomQty = findViewById(R.id.tvUomQty);
                        String uomName = "";
                        String uomQty = "";
                        for (int i = 0; i < uomList.size(); i++) {
                            uomName = uomName + uomList.get(i).getUOM_Nm() + "\n";
                            uomQty = uomQty + (int) uomList.get(i).getCnvQty() + "\n";
                        }


                        tvUOMName.setText("" + uomName);
                        tvUomQty.setText("" + uomQty);
                    }
                } else {
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        total_qtytext += obj.getInt("Quantity");
                        subTotalVal += (obj.getDouble("value"));

                        double taxAmt = 0.00, sTaxV = 0.0, SGSTAmt = 0.00, CGSTAmt = 0.00;
                        try {
                            JSONArray taxArr = obj.getJSONArray("TAX_details");
                            for (int tax = 0; tax < taxArr.length(); tax++) {
                                JSONObject taxObj = taxArr.getJSONObject(tax);
                                String label = taxObj.getString("Tax_Name");
                                Double amt = taxObj.getDouble("Tax_Amt");

                                sTaxV += taxObj.getDouble("Tax_Val");
                                taxAmt += amt;
                                if (label.equalsIgnoreCase("SGST")) {
                                    SGSTAmt += amt;
                                }
                                if (label.equalsIgnoreCase("CGST")) {
                                    CGSTAmt += amt;
                                }
                                if (taxList.size() == 0) {
                                    if (tcsVal > 0)
                                        taxList.add(new Product_Details_Modal("TCS", tcsVal));
                                    taxList.add(new Product_Details_Modal(label, amt));
                                } else {

                                    boolean isDuplicate = false;
                                    for (int totTax = 0; totTax < taxList.size(); totTax++) {
                                        if (taxList.get(totTax).getTax_Type().equals(label)) {
                                            double oldAmt = taxList.get(totTax).getTax_Amt();
                                            isDuplicate = true;
                                            taxList.set(totTax, new Product_Details_Modal(label, oldAmt + amt));

                                        }
                                    }

                                    if (!isDuplicate) {
                                        taxList.add(new Product_Details_Modal(label, amt));
                                    }
                                }

                            }
                        } catch (Exception e) {
                            Log.e("jhbdreg", e.getMessage());

                        }

                        if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("POS INVOICE")) {
                            Order_Outlet_Filter.add(new Product_Details_Modal(obj.getString("Product_Code"), obj.getString("Product_Name"), obj.getString("MRP"), obj.getString("HSN_Code"), 1, "1",
                                    "1", "5", obj.getString("UOM"), 0, "0", obj.getDouble("BillRate"), obj.getString("PTR"),
                                    obj.getInt("Quantity"), obj.getInt("qty"), obj.getDouble("value"), taxList, "0", (taxAmt), (sTaxV), (SGSTAmt), (CGSTAmt), obj.getString("ConversionFactor"), obj.getString("discount_price"), obj.getString("Offer_ProductCd"), obj.getString("Offer_ProductNm"), obj.getString("off_pro_unit")));
                        }else if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("Projection")) {
                            Order_Outlet_Filter.add(new Product_Details_Modal(obj.getString("Product_Code"), obj.getString("Product_Name"), 1, "1",
                                    "1", "5", obj.getString("UOM"), 0, "0", 0.0,
                                    obj.getInt("Quantity"), obj.getInt("qty"), obj.getDouble("value"), taxList, "0", (taxAmt)));
                        } else {
                            Order_Outlet_Filter.add(new Product_Details_Modal(obj.getString("Product_Code"), obj.getString("Product_Name"), obj.getString("MRP"), obj.getString("Bar_Code"), 1, "1",
                                    "1", "5", obj.getString("UOM"), 0, "0", obj.getDouble("BillRate"), obj.getString("PTR"),
                                    obj.getInt("Quantity"), obj.getInt("qty"), obj.getDouble("value"), taxList, "0", (taxAmt), (sTaxV), (SGSTAmt), (CGSTAmt), obj.getString("ConversionFactor"), obj.getString("discount_price"), obj.getString("Offer_ProductCd"), obj.getString("Offer_ProductNm"), obj.getString("off_pro_unit")));

                        }

                    }

                    if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("Projection")) {
                        try {
                            storeName = arr.getJSONObject(0).getString("plantName") + "(" + arr.getJSONObject(0).getInt("plantID") + ")";
                            tvDistributorName.setText("" + tvDistributorName.getText().toString());

                        } catch (Exception e) {
                            Log.e("jhbdreg", e.getMessage());

                        }

                    }
                }
                mReportViewAdapter = new Print_Invoice_Adapter(Print_Invoice_Activity.this, Order_Outlet_Filter, sharedCommonPref.getvalue(Constants.FLAG));
                printrecyclerview.setAdapter(mReportViewAdapter);


            }


            subTotalVal = Double.parseDouble(formatter.format(subTotalVal + tcsVal));

            totalqty.setText("" + String.valueOf(total_qtytext));
            totalitem.setText("" + Order_Outlet_Filter.size());
            baseAmt.setText(CurrencySymbol + " " + formatter.format(tsubTotalVal));
            subtotal.setText(CurrencySymbol + " " + formatter.format(subTotalVal));
            NetTotAmt = subTotalVal ;//- cashDisc;
            netamount.setText(CurrencySymbol + " " + formatter.format(NetTotAmt));


            returntotalqty.setText("" + String.valueOf(total_qtytext));
            returntotalitem.setText("" + Order_Outlet_Filter.size());
            returnsubtotal.setText(CurrencySymbol + " " + formatter.format(subTotalVal));
            returnNetAmt.setText(CurrencySymbol + " " + formatter.format(NetTotAmt));

            tvPaidAmt.setText(CurrencySymbol + " " + formatter.format(Double.parseDouble(Order_Outlet_Filter.get(0).getPaidAmount())));

            sharedCommonPref.save(Constants.INVOICE_ORDERLIST, response);


            cashdiscount.setText(CurrencySymbol + " " + formatter.format(cashDisc));
            //gstrate.setText(CurrencySymbol + " " + formatter.format(Double.parseDouble(getIntent().getStringExtra("NetAmount"))));

            label = "";
            amt = "";
            for (int i = 0; i < taxList.size(); i++) {
                label = label + taxList.get(i).getTax_Type() + "\n";
                amt = amt + CurrencySymbol + " " + String.valueOf(formatter.format(taxList.get(i).getTax_Amt())) + "\n";
            }
            if (taxList.size() < 0) {
                rlTax.setVisibility(View.GONE);
            }
            gstLabel.setText(label);
            gstrate.setText(amt);
            returngstLabel.setText(label);
            returngstrate.setText(amt);


        } catch (Exception e) {
            Log.e("jhbdreg", e.getMessage());
        }
    }
}