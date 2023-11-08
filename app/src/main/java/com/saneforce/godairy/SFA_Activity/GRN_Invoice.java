package com.saneforce.godairy.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.GRN_Print_Invoice_Adapter;
import com.saneforce.godairy.SFA_Adapter.QPS_Modal;
import com.saneforce.godairy.SFA_Model_Class.OutletReport_View_Modal;
import com.saneforce.godairy.SFA_Model_Class.Product_Details_Modal;
import com.saneforce.godairy.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.ButterKnife;

public class GRN_Invoice extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {

    GRN_Print_Invoice_Adapter mReportViewAdapter;
    RecyclerView printrecyclerview, rvReturnInv;
    Shared_Common_Pref sharedCommonPref;
    Common_Class common_class;
    List<com.saneforce.godairy.SFA_Model_Class.OutletReport_View_Modal> OutletReport_View_Modal = new ArrayList<>();
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();
    TextView netamount, returnNetAmt, cashdiscount, gstLabel, gstrate, totalfreeqty, totalqty, totalitem, subtotal, invoicedate, retaileAddress, billnumber, retailername,
            retailerroute, back, tvOrderType, tvRetailorPhone, tvDistributorPh, tvDistributorName, tvOutstanding, tvPaidAmt, tvHeader, tvDistId,
            tvDistAdd, returngstLabel, returngstrate, returntotalqty, returntotalitem, returnsubtotal, tvDisGST, tvDisFSSAI, tvRetGST, tvRetFSSAI;
    ImageView ok, ivPrint;
    public static GRN_Invoice mPrint_invoice_activity;
    CircularProgressButton btnInvoice;
    NumberFormat formatter = new DecimalFormat("##0.00");
    private String label, amt, dis_storeName = "", dis_address = "", dis_phone = "", storeName = "", address = "", phone = "";
    private ArrayList<Product_Details_Modal> taxList;
    private ArrayList<Product_Details_Modal> uomList;
    DatabaseHandler db;
    double cashDisc, subTotalVal, outstandAmt;
    LinearLayout llDistCal, llRetailCal, llDisGst, llDisFssai;
    String[] strLoc;
    final Handler handler = new Handler();
    public String TAG = "Print_Invoice_Activity";
    ImageView ivStockCapture;
    RecyclerView rvStockCapture;
    List<QPS_Modal> stockFileList = new ArrayList<>();
    Type userType;
    Gson gson;
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrint_invoice_activity = this;
        setContentView(R.layout.activity_grn_invoice);
        ButterKnife.inject(this);
        printrecyclerview = findViewById(R.id.grnprintrecyclerview);
        rvReturnInv = findViewById(R.id.rvReturnInv);

        db = new DatabaseHandler(this);
        gson = new Gson();
        sharedCommonPref = new Shared_Common_Pref(GRN_Invoice.this);
        common_class = new Common_Class(this);

        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        common_class.getProductDetails(this);
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

        llDisGst = findViewById(R.id.disGstLay);
        llDisFssai = findViewById(R.id.disFssaiLay);

        retailername.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
        tvDistributorName.setText(sharedCommonPref.getvalue(Constants.Distributor_name));
        ivPrint = findViewById(R.id.ivPrint);

        LinearLayoutManager shrtgridlayManager = new LinearLayoutManager(this);
        shrtgridlayManager.setOrientation(LinearLayoutManager.VERTICAL);
        printrecyclerview.setLayoutManager(shrtgridlayManager);

        back.setOnClickListener(this);
        ok.setOnClickListener(this);
        ivPrint.setOnClickListener(this);
        btnInvoice.setOnClickListener(this);
        llDistCal.setOnClickListener(this);
        llRetailCal.setOnClickListener(this);
        ivStockCapture.setOnClickListener(this);

        common_class.getDataFromApi(Constants.GetGrn_Pending_List, this, false);

        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);

        billnumber.setText("Order-" + getIntent().getStringExtra("Invoice_No"));
        tvOrderType.setText(sharedCommonPref.getvalue(Constants.FLAG));
        tvHeader.setText("Sales " + sharedCommonPref.getvalue(Constants.FLAG));

        tvDistributorPh.setText(sharedCommonPref.getvalue(Constants.Distributor_phone));
        tvRetailorPhone.setText(sharedCommonPref.getvalue(Constants.Retailor_PHNo));

        if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Distributor_phone)))
            llDistCal.setVisibility(View.GONE);
        if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Retailor_PHNo)))
            llRetailCal.setVisibility(View.GONE);
        retailerroute.setText(sharedCommonPref.getvalue(Constants.Route_name));
        retaileAddress.setText(sharedCommonPref.getvalue(Constants.Retailor_Address));
        invoicedate.setText(getIntent().getStringExtra("Invoice_Date"));
        tvDistId.setText(sharedCommonPref.getvalue(Constants.DistributorERP));
        tvDistAdd.setText(sharedCommonPref.getvalue(Constants.DistributorAdd));

        subtotal.setText("₹" + getIntent().getStringExtra("NetAmount"));
        netamount.setText("₹" + getIntent().getStringExtra("NetTotal"));
        gstrate.setText("₹" + getIntent().getStringExtra("TaxAmount"));
//        totalqty.setText(getIntent().getStringExtra("TotQnty"));

        tvDisGST.setText(sharedCommonPref.getvalue(Constants.DistributorGst));
        tvDisFSSAI.setText(sharedCommonPref.getvalue(Constants.DistributorFSSAI));

        if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.DistributorGst)))
            llDisGst.setVisibility(View.GONE);
        if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.DistributorFSSAI)))
            llDisFssai.setVisibility(View.GONE);

        Log.v("gst_dist", sharedCommonPref.getvalue(Constants.DistributorGst));



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            return true;
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

//            case R.id.ok:
//                createPdf();
//                break;
//            case R.id.ivPrint:
//                showPrinterList();
//                break;

            case R.id.llDistCall:
                common_class.showCalDialog(this, "Do you want to Call this Distributor?", sharedCommonPref.getvalue(Constants.Distributor_phone));
                break;
            case R.id.llRetailCal:
                common_class.showCalDialog(this, "Do you want to Call this Outlet?", sharedCommonPref.getvalue(Constants.Retailor_PHNo));
                break;
        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {

            if (apiDataResponse != null && !apiDataResponse.equals("")) {
                switch (key) {
                    case Constants.GetGrn_Pending_List:
//                        apiCall(apiDataResponse);
                        FilterOrderList.clear();
                        userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
                        }.getType();
                        OutletReport_View_Modal = gson.fromJson(apiDataResponse, userType);
                        if (OutletReport_View_Modal != null && OutletReport_View_Modal.size() > 0) {
                            for (OutletReport_View_Modal filterlist : OutletReport_View_Modal) {
                                FilterOrderList.add(filterlist);
                            }
                        }

                        int total_qtytext = 0;
                        JSONArray arr = new JSONArray(apiDataResponse);
                        for (int a = 0; a < arr.length(); a++) {
                            JSONObject obj = arr.getJSONObject(a);
                            total_qtytext += obj.getInt("billed_qty");
                            totalqty.setText("" + String.valueOf(total_qtytext));

                            Log.e("totalQTY",totalqty.toString());

                        }

                        totalitem.setText("" + OutletReport_View_Modal.size());

                        mReportViewAdapter = new GRN_Print_Invoice_Adapter(GRN_Invoice.this, FilterOrderList);
                        printrecyclerview.setAdapter(mReportViewAdapter);
                        break;

                }
            }
        } catch (Exception e) {

            Log.e("erw", e.toString());
        }

    }


}