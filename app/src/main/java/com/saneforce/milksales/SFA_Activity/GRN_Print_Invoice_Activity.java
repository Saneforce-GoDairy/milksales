package com.saneforce.milksales.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.GRN_History_Print_Invoice_Adapter;
import com.saneforce.milksales.SFA_Adapter.QPS_Modal;
import com.saneforce.milksales.SFA_Model_Class.OutletReport_View_Modal;
import com.saneforce.milksales.SFA_Model_Class.Product_Details_Modal;
import com.saneforce.milksales.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.ButterKnife;

public class GRN_Print_Invoice_Activity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {

    GRN_History_Print_Invoice_Adapter mReportViewAdapter;
    RecyclerView printrecyclerview, rvReturnInv;
    Shared_Common_Pref sharedCommonPref;
    Common_Class common_class;
    List<OutletReport_View_Modal> OutletReport_View_Modal = new ArrayList<>();
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();
    TextView netamount, returnNetAmt, cashdiscount, gstLabel, gstrate, totalfreeqty, totalqty, totalitem, subtotal, invoicedate, retaileAddress, billnumber, retailername,
            retailerroute, back, tvOrderType, tvRetailorPhone, tvDistributorPh, tvDistributorName, tvOutstanding, tvPaidAmt, tvHeader, tvDistId,
            tvDistAdd, returngstLabel, returngstrate, returntotalqty, returntotalitem, returnsubtotal, tvDisGST, tvDisFSSAI, tvRetGST, tvRetFSSAI;
    ImageView ok, ivPrint;
    public static GRN_Print_Invoice_Activity mPrint_invoice_activity;
    CircularProgressButton btnInvoice;
    NumberFormat formatter = new DecimalFormat("##0.00");
    private String label, amt, dis_storeName = "", dis_address = "", dis_phone = "", storeName = "", address = "", phone = "";
    private ArrayList<Product_Details_Modal> taxList;
    private ArrayList<Product_Details_Modal> uomList;
    DatabaseHandler db;
    double cashDisc, subTotalVal, outstandAmt;
    LinearLayout llDistCal, llRetailCal,llDisGst,llDisFssai;
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
//        try {
            super.onCreate(savedInstanceState);
            mPrint_invoice_activity = this;
            setContentView(R.layout.activity_grn_print_invoice);
            ButterKnife.inject(this);
            printrecyclerview = findViewById(R.id.grnprintrecyclerview);
            rvReturnInv = findViewById(R.id.rvReturnInv);

            db = new DatabaseHandler(this);
            gson = new Gson();
            sharedCommonPref = new Shared_Common_Pref(GRN_Print_Invoice_Activity.this);
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

            llDisGst=findViewById(R.id.disGstLay);
            llDisFssai=findViewById(R.id.disFssaiLay);

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

            common_class.getDataFromApi(Constants.GetGrn_OrderDetails, this, false);

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

            subtotal.setText("₹" + getIntent().getStringExtra("Net_Tot_Amount"));
            netamount.setText("₹" + getIntent().getStringExtra("NetAmount"));
        gstrate.setText("₹" + getIntent().getStringExtra("NetTax"));



        tvDisGST.setText(sharedCommonPref.getvalue(Constants.DistributorGst));
            tvDisFSSAI.setText(sharedCommonPref.getvalue(Constants.DistributorFSSAI));

            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.DistributorGst)))
                llDisGst.setVisibility(View.GONE);
            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.DistributorFSSAI)))
                llDisFssai.setVisibility(View.GONE);

            Log.v("gst_dist",sharedCommonPref.getvalue(Constants.DistributorGst));



//        } catch (Exception e) {
//            Log.v(TAG, e.getMessage());
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

                finish();
                return  true;
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

            case R.id.ok:
                createPdf();
                break;
            case R.id.ivPrint:
                showPrinterList();
                break;

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
                    case Constants.GetGrn_OrderDetails:
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
                        Double gross=0.0;
                        int tax=0;
                        JSONArray arr = new JSONArray(apiDataResponse);
                        for (int a = 0; a < arr.length(); a++) {
                            JSONObject obj = arr.getJSONObject(a);
                            total_qtytext += obj.getInt("POQTY");
//                            gross += obj.getDouble("Gross_Value");
//                            tax += obj.getInt("Tax");
                            totalqty.setText("" + String.valueOf(total_qtytext));
//                            subtotal.setText("₹" + String.valueOf(gross));
//                            gstrate.setText("₹" + String.valueOf(tax));
//                            netamount.setText("₹" + (gross + tax));
                            Log.e("totalQTY",totalqty.toString());

                        }

                        totalitem.setText("" + OutletReport_View_Modal.size());

                        mReportViewAdapter = new GRN_History_Print_Invoice_Adapter(GRN_Print_Invoice_Activity.this, FilterOrderList);
                        printrecyclerview.setAdapter(mReportViewAdapter);
                        break;

                }
            }
        } catch (Exception e) {

            Log.e("erw", e.toString());
        }

    }


    private void createPdf() {
        try {
            int hgt = 1000 + (OutletReport_View_Modal.size() * 40);

            // create a new document
            PdfDocument document = new PdfDocument();
            int widthSize = 600;
            // crate a page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(widthSize, hgt, 1).create();
            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();


            int x = 10;
            int y = 30;

            paint.setColor(Color.BLACK);
            paint.setTextSize(13);

            canvas.drawText(tvDistributorName.getText().toString(), x, y, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(12);
            paint.setTextAlign(Paint.Align.LEFT);

            y = y + 20;

            if (tvDistAdd.getVisibility() == View.VISIBLE) {
                canvas.drawText("Address :", x, y, paint);
                paint.setColor(Color.DKGRAY);
                paint.setTextSize(12);
                y = y + 20;

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

            canvas.drawText("GST No: "+ tvDisGST.getText().toString(), x, y, paint);
            y = y + 20;

            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);

            y = y + 20;
            paint.setColor(Color.BLACK);
            paint.setTextSize(13);
            canvas.drawText("BILL TO", x, y, paint);

            y = y + 18;
            canvas.drawText(retailername.getText().toString(), x, y, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(12);
            paint.setTextAlign(Paint.Align.LEFT);

            y = y + 20;

            if (retaileAddress.getVisibility() == View.VISIBLE) {
                canvas.drawText("Address :", x, y, paint);
                paint.setColor(Color.DKGRAY);
                paint.setTextSize(12);
                y = y + 20;

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
//
//            paint.setColor(Color.LTGRAY);
//            paint.setStrokeWidth(30);
//            canvas.drawLine(0, y + 30, widthSize, y + 30, paint);


            y = y + 35;
            paint.setColor(Color.BLACK);
            paint.setTextSize(11);
            canvas.drawText("" + billnumber.getText().toString(), x, y, paint);
            canvas.drawText("" + invoicedate.getText().toString(), (widthSize / 2) + 140, y, paint);

            y = y + 25;
            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);


            String space = "    ";
            y = y + 20;
            paint.setColor(Color.BLACK);
            paint.setTextSize(13);

            canvas.drawText("Item", x, y, paint);
            canvas.drawText("Qty", (widthSize / 2) + 60, y, paint);
            canvas.drawText("Price", (widthSize / 2) + 110, y, paint);
            canvas.drawText("GST", (widthSize / 2) + 170, y, paint);
            canvas.drawText("Total", (widthSize / 2) + 220, y, paint);


            y = y + 10;
            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);

            for (int i = 0; i < OutletReport_View_Modal.size(); i++) {

                y = y + 20;
                paint.setColor(Color.DKGRAY);
                paint.setTextSize(12);


                String name = OutletReport_View_Modal.get(i).getProductName() + "                               ";
                name = name.substring(0, name.length() - String.valueOf(OutletReport_View_Modal.get(i).getProdDetails()).length());


                String qtyValue = String.valueOf(OutletReport_View_Modal.get(i).getProdQnty());
                String qty = "     " + qtyValue;
                qty = qty.substring(qtyValue.length(), qty.length());

                String rateValue = String.valueOf(formatter.format(OutletReport_View_Modal.get(i).getProdPrice()));
                String rate = "               " + rateValue;
                rate = (rate.substring(rateValue.length(), rate.length()));

                String amtValue = String.valueOf(formatter.format(OutletReport_View_Modal.get(i).getProdTotal()));
                String amt = "             " + amtValue;
                amt = (amt.substring(amtValue.length(), amt.length()));

                Log.e("Values length: ", "item: " + name.length() + " qty: " + qty.length() + " rate: " + rate.length() + " amt : " + amt.length());

                // canvas.drawText(name + qty + rate + amt, x, y, paint);


                canvas.drawText("" + OutletReport_View_Modal.get(i).getProdDetails(), x, y, paint);
                canvas.drawText("" + OutletReport_View_Modal.get(i).getProdQnty(), (widthSize / 2) + 60, y, paint);
                canvas.drawText("" + formatter.format(OutletReport_View_Modal.get(i).getProdPrice()), (widthSize / 2) + 100, y, paint);
                canvas.drawText("" + formatter.format(OutletReport_View_Modal.get(i).getTax_amount()), (widthSize / 2) + 170, y, paint);
                canvas.drawText("" + formatter.format(OutletReport_View_Modal.get(i).getProdTotal()), (widthSize / 2) + 230, y, paint);


            }


            y = y + 20;
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
            canvas.drawText("SubTotal", x, y, paint);
            canvas.drawText(subtotal.getText().toString(), (widthSize / 2) + 220, y, paint);

            y = y + 20;
            canvas.drawText("Total Item", x, y, paint);
            canvas.drawText(totalitem.getText().toString(), (widthSize / 2) + 220, y, paint);
            y = y + 20;
            canvas.drawText("Total Qty", x, y, paint);
            canvas.drawText(totalqty.getText().toString(), (widthSize / 2) + 220, y, paint);

            if (uomList != null) {
                for (int i = 0; i < uomList.size(); i++) {
                    y = y + 20;
                    canvas.drawText(uomList.get(i).getUOM_Nm(), x, y, paint);
                    canvas.drawText("" + (int) uomList.get(i).getCnvQty(), (widthSize / 2) + 220, y, paint);
                }

            }

//            y = y + 30;
//            canvas.drawText("Gst Rate", x, y, paint);
//            canvas.drawText(gstrate.getText().toString(), (widthSize / 2) + 150, y, paint);
//
            for (int i = 0; i < taxList.size(); i++) {
                y = y + 20;
                canvas.drawText(taxList.get(i).getTax_Type(), x, y, paint);
                canvas.drawText("₹" + formatter.format(taxList.get(i).getTax_Amt()), (widthSize / 2) + 220, y, paint);

            }

            y = y + 20;

//            if (tvOutstanding.getVisibility() == View.VISIBLE) {
//                canvas.drawText("Outstanding", x, y, paint);
//                canvas.drawText(tvOutstanding.getText().toString(), (widthSize / 2) + 150, y, paint);
//                y = y + 30;
//            }

            if (cashDisc > 0) {
                canvas.drawText("Cash Discount", x, y, paint);
                canvas.drawText(cashdiscount.getText().toString(), (widthSize / 2) + 220, y, paint);
                y = y + 20;
            }

            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);

            paint.setTextSize(15);
            paint.setColor(Color.BLACK);
            y = y + 30;
            canvas.drawText("Net Amount", x, y, paint);
            canvas.drawText(netamount.getText().toString(), (widthSize / 2) + 210, y, paint);

            y = y + 20;
            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);

            y = y + 30;
            paint.setColor(Color.parseColor("#008000"));
            paint.setTextSize(15);


            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Thank You! Visit Again", widthSize/2, y, paint);



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

    public void printBill() {
        try {
            Bitmap logo = Printama.getBitmapFromVector(this, R.drawable.product_logo);
            Printama.with(this).connect(printama -> {

                printama.printImage(Printama.CENTER, logo, 140);
                printama.addNewLine();


                printama.setWideTallBold();
                printama.setTallBold();
                printama.printTextln(Printama.CENTER, tvDistributorName.getText().toString());
                printama.addNewLine();
                printama.setBold();
                if (tvDistAdd.getVisibility() == View.VISIBLE) {
                    printama.printTextln(Printama.LEFT, "Address: ");
                    printama.setNormalText();

                    printama.printTextln(Printama.LEFT, tvDistAdd.getText().toString());
                }

                printama.addNewLine(1);

                if (tvDistributorPh.getVisibility() == View.VISIBLE){
                    printama.printTextln(Printama.LEFT, "Mobile: ");
                    printama.setNormalText();

                    printama.printTextln(Printama.LEFT, tvDistributorPh.getText().toString() + "                  " + invoicedate.getText().toString());
                }

                printama.addNewLine(1);

                if (tvDisGST.getVisibility() == View.VISIBLE){
                    printama.setNormalText();

                    printama.printTextln(Printama.LEFT, "GST NO: " + tvDisGST.getText().toString());
                }

                if (tvDisFSSAI.getVisibility() == View.VISIBLE){
                    printama.setNormalText();

                    printama.printTextln(Printama.LEFT, "FSSAI NO: " + tvDisFSSAI.getText().toString());
                }

                printama.addNewLine(1);
                printama.setBold();
                printama.printText(billnumber.getText().toString());
//                printama.addNewLine();

                printama.addNewLine();
                printama.printLine();
                printama.addNewLine(2);
                printama.setBold();
                printama.printTextln(" Item       " + "     Qty" + "      Price" + "     Total");
                printama.printLine();
                printama.addNewLine(2);



                for (int i = 0; i < OutletReport_View_Modal.size(); i++) {
                    printama.setBold();
                    printama.printTextln(OutletReport_View_Modal.get(i).getProdDetails().toString().trim());
                    printama.addNewLine(1);

                    printama.setNormalText();

                    String name = OutletReport_View_Modal.get(i).getProdDetails() + "           ";
                    name = name.substring(0, name.length() - String.valueOf(OutletReport_View_Modal.get(i).getProductName()).length());


                    String qtyValue = String.valueOf(OutletReport_View_Modal.get(i).getProdQnty());
                    String qty = "        " + qtyValue;
                    qty = qty.substring(qtyValue.length(), qty.length());

                    String rateValue = String.valueOf(formatter.format(OutletReport_View_Modal.get(i).getProdPrice()));
                    String rate = "            " + rateValue;
                    rate = (rate.substring(rateValue.length(), rate.length()));

                    String amtValue = String.valueOf(formatter.format(OutletReport_View_Modal.get(i).getProdTotal()));
                    String amt = "           " + amtValue;
                    amt = (amt.substring(amtValue.length(), amt.length()));

                    printama.printTextln(name + qty +
                            rate + amt);

                    printama.addNewLine();

                }

                printama.printLine();
                printama.addNewLine(2);

                String subTotal = "           " + formatter.format(subTotalVal);
                String totItem = "           " + totalitem.getText().toString();
                String totqty = "           " + totalqty.getText().toString();
                String discount = "           " + cashDisc;
                String outstand = "           " + outstandAmt;

                printama.printText("SubTotal" + "                       " + subTotal.substring(String.valueOf(formatter.format(subTotalVal)).length(), subTotal.length()));
                printama.addNewLine();
                printama.printText("Total Item" + "                     " + totItem.substring(totalitem.getText().toString().length(), totItem.length()));
                printama.addNewLine();
                printama.printText("Total Qty" + "                      " + totqty.substring(totalqty.getText().toString().length(), totqty.length()));
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
                    printama.printText("Cash Discount" + "                  " + discount.substring(String.valueOf(cashDisc).length(), discount.length()));
                    printama.addNewLine();
                    printama.printLine();
                }

                printama.addNewLine(1.5);
                printama.setTallBold();
                String strAmount = "           " + formatter.format(subTotalVal);

                printama.printText("Net amount" + "                     " + strAmount.substring(String.valueOf(subTotalVal).length(), strAmount.length()));
                printama.addNewLine(1);
//                printama.printLine();
                printama.addNewLine(2);

                printama.setBold();
                printama.printTextln(Printama.CENTER, "Thank You! Visit Again");
                printama.addNewLine();


                printama.setLineSpacing(3);
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

    void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();

    }

}