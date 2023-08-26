package com.saneforce.milksales.SFA_Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.CurrencyConverter;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChallanActivity extends AppCompatActivity {

    TextView invoiceNumberTV, crDateTV, stockistNameTV, amountTV, statusTV;

    final int pageWidth = 595, pageHeight = 842;
    final float xMin = 20, xMax = pageWidth - 20;
    final float x = 40;

    ImageView toolbarHome, print, share;

    Context context = this;

    Paint paint;
    Canvas canvas;
    Common_Class common_class;

    double amount = 0;
    String invoice = "", DocNo = "", todayDate = "", customerCode = "", customerName = "", ERP_CODE = "", PAN = "", VERSION = "";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challan);

        toolbarHome = findViewById(R.id.toolbar_home);
        print = findViewById(R.id.print);
        share = findViewById(R.id.share);
        invoiceNumberTV = findViewById(R.id.invoiceNumberTV);
        crDateTV = findViewById(R.id.crDateTV);
        stockistNameTV = findViewById(R.id.stockistNameTV);
        amountTV = findViewById(R.id.amountTV);
        statusTV = findViewById(R.id.statusTV);

        common_class = new Common_Class(this);
        invoice = getIntent().getStringExtra("invoice");

        print.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setMessage("Select challan type");
            builder.setPositiveButton("Bank Copy", (dialog, which) -> {
                VERSION = "Bank Copy (To be retained by Axis Bank Collecting Branch)";
                createChallanPDF("print");
            });
            builder.setNegativeButton("Customer Copy", (dialog, which) -> {
                VERSION = "Customer Copy (To be submitted by the Applicant to the Lactalis India Pvt Ltd)";
                createChallanPDF("print");
            });
            builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
        share.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setMessage("Select challan type");
            builder.setPositiveButton("Bank Copy", (dialog, which) -> {
                VERSION = "Bank Copy (To be retained by Axis Bank Collecting Branch)";
                createChallanPDF("share");
            });
            builder.setNegativeButton("Customer Copy", (dialog, which) -> {
                VERSION = "Customer Copy (To be submitted by the Applicant to the Lactalis India Pvt Ltd)";
                createChallanPDF("share");
            });
            builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });

        getChallanData();
    }

    private void getChallanData() {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Creating challan");
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_trans_info");
        params.put("invoice", invoice);
        Call<ResponseBody> call = apiInterface.getUniversalData(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() == null) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Response is Null", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("success")) {
                            JSONObject object = jsonObject.getJSONObject("response");
                            DocNo = object.getString("DocNo");
                            todayDate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                            customerCode = object.getString("OutletId");
                            customerName = object.getString("userName");
                            amount = object.getDouble("CashAmt");
                            ERP_CODE = object.getString("ERP_Code");
                            PAN = object.getString("Pan");
                            ShowStatus(object);
                        } else {
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error while parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Response Not Success", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void ShowStatus(JSONObject jsonObject) {
        try {
            invoiceNumberTV.setText(invoice);
            crDateTV.setText(jsonObject.getString("Date"));
            stockistNameTV.setText(customerName);
            amountTV.setText(common_class.formatCurrency(amount));
            if (amount == jsonObject.getDouble("DepositedAmt")) {
                statusTV.setText("PAID");
            } else {
                statusTV.setText("PENDING");
            }
        } catch (JSONException ignored) { }
    }

    private void createChallanPDF(String mode) {
        float y = 0;
        paint = new Paint();
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        canvas = page.getCanvas();

        // ------------------------------------------------------------------------- Page Border

        y += 20;
        drawHorizontalLine(xMin, y, xMax, y);
        drawHorizontalLine(xMin, pageHeight - 20, xMax, pageHeight - 20);
        drawHorizontalLine(xMin, y, xMin, pageHeight - 20);
        drawHorizontalLine(xMax, y, xMax, pageHeight - 20);

        // ------------------------------------------------------------------------- Heading

        y += 25;
        drawBIGTitleWithCenterAlign("Lactalis India Pvt Ltd", (float) pageWidth / 2, y);

        y += 20;
        drawTextWithCenterAlign(VERSION, (float) pageWidth / 2, y);

        y += 15;
        drawHorizontalLine(xMin, y, xMax, y);

        // ------------------------------------------------------------------------- Receipt No

        y += 30;
        drawText("Receipt No: ", x, y);
        float temp1 = paint.measureText("Receipt No: ");

        drawTitle(DocNo, x + temp1 + 10, y);
        float temp2 = paint.measureText(DocNo);

        drawHorizontalLine(x + temp1 + 5, y + 5, x + temp1 + 15 + temp2, y + 5);

        drawTextWithRightAlign(todayDate, xMax - 25, y);
        float temp = paint.measureText(todayDate);
        drawHorizontalLine(xMax - 20, y + 5, xMax - 20 - temp - 5 - 5, y + 5);

        drawTextWithRightAlign("Date: ", xMax - 20 - temp - 15, y);

        // ------------------------------------------------------------------------- Axis Bank, Branch

        y += 40;
        String data = "Axis Bank, Branch: ";
        drawText(data, x, y);
        temp = paint.measureText(data);

        drawRectangle(x + temp + 5, y - 20, x + 350, y + 10);

        y += 25;
        drawHorizontalLine(xMin, y, xMax, y);

        // ------------------------------------------------------------------------- Customer Code

        y += 30;
        drawText("Customer Code: ", x, y);
        drawText(customerCode, x + 110, y);
        drawRectangle(x + 100, y - 20, x + 350, y + 10);

        // ------------------------------------------------------------------------- Customer Name

        y += 40;
        drawText("Customer Name: ", x, y);
        drawText(customerName, x + 110, y);
        drawRectangle(x + 100, y - 20, x + 350, y + 10);

        // ------------------------------------------------------------------------- ERP Code

        y += 40;
        drawText("ERP Code: ", x, y);
        drawText(ERP_CODE, x + 110, y);
        drawRectangle(x + 100, y - 20, x + 350, y + 10);

        // ------------------------------------------------------------------------- PAN

        y += 40;
        drawText("PAN: ", x, y);
        drawText(PAN, x + 110, y);
        drawRectangle(x + 100, y - 20, x + 350, y + 10);

        // ------------------------------------------------------------------------- Denomination

        y += 30;
        drawHorizontalLine(xMin, y, xMax, y);

        y += 20;
        float tableStart = y;
        drawHorizontalLine(x, y, xMax - 20, y);

        float amtColStart = xMax - 20 - 10 - 100 - 10;
        float totColStart = amtColStart - 10 - 100 - 10;

        y += 20;
        drawText("Denomination", x + 10, y);
        drawTextWithCenterAlign("Amount", amtColStart + 60, y);
        drawTextWithCenterAlign("Total (Rs)", totColStart + 60, y);

        y += 10;
        drawHorizontalLine(x, y, xMax - 20, y);

        drawHorizontalLine(x, tableStart, x, tableStart + 100);
        drawHorizontalLine(xMax - 20, tableStart, xMax - 20, tableStart + 100);
        drawHorizontalLine(amtColStart, tableStart, amtColStart, tableStart + 100);
        drawHorizontalLine(totColStart, tableStart, totColStart, tableStart + 100);

        y = tableStart + 100;
        drawHorizontalLine(x, y, xMax - 20, y);

        // ------------------------------------------------------------------------- Amount in words

        y += 25;
        String amtInWords = CurrencyConverter.convert(amount);
        String[] line = Split(amtInWords, 90, amtInWords.length());
        for (String s : line) {
            drawText(s, x, y);
            y = y + 20;
        }

        // ------------------------------------------------------------------------- Depositor’s Sign

        y += 15;
        drawText("Depositor’s Sign: ", x, y);
        float signSize = paint.measureText("Depositor’s Sign: ");
        drawHorizontalLine(x + signSize + 5, y + 5, x + signSize + 5 + 200, y + 5);

        // ------------------------------------------------------------------------- Mode of Payment (√)

        y += 20;
        float table2Start = y;
        drawHorizontalLine(x, y, xMax - 20, y);

        float bankStampColStart = (float) pageWidth / 2;

        y += 20;
        drawTitle("Mode of Payment (√)", x + 10, y);
        drawTitle("Bank Stamp", bankStampColStart + 10, y);

        y += 10;
        float modeStart = y;
        drawHorizontalLine(x, y, xMax - 20, y);

        y += 18;
        drawText("Cash", x + 10, y);

        y += 10;
        drawHorizontalLine(x, y, bankStampColStart, y);

        y += 18;
        drawText("Demand Draft", x + 10, y);

        y += 10;
        drawHorizontalLine(x, y, xMax - 20, y);

        drawHorizontalLine(x, table2Start, x, y);
        drawHorizontalLine(x + 150, modeStart, x + 150, y);
        drawHorizontalLine(xMax - 20, table2Start, xMax - 20, y);
        drawHorizontalLine(bankStampColStart, table2Start, bankStampColStart, y);

        // ------------------------------------------------------------------------- For Bank Use

        y += 20;
        drawHorizontalLine(xMin, y, xMax, y);

        y += 20;
        drawTitleWithCenterAlign("For Bank Use", bankStampColStart, y);

        y += 25;
        String amt = "Received: " + common_class.formatCurrency(amount) + " (" + CurrencyConverter.convert(amount) + ") on " + new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime()) + ".";
        String[] lines = Split(amt, 90, amt.length());
        for (String s : lines) {
            drawText(s, x, y);
            y = y + 20;
        }

        // ------------------------------------------------------------------------- For Bank Use

        y += 15;
        drawText("Cashier: ", x, y);
        temp1 = paint.measureText("Cashier: ");
        float cashierEnd = x + temp1 + 15 + 150;
        drawHorizontalLine(x + temp1 + 5, y + 5, cashierEnd, y + 5);
        drawText("Cashier’s Scroll No: ", cashierEnd + 10, y);
        temp = paint.measureText("Cashier’s Scroll No: ");
        drawHorizontalLine(cashierEnd + 10 + 5 + temp, y + 5, cashierEnd + 10 + 160 + temp, y + 5);

        // ------------------------------------------------------------------------- Note

        y += 25;
        drawBoldText("Note: To be accepted at the designated Axis Bank Branch", x, y);

        // ------------------------------------------------------------------------- End of PDF

        pdfDocument.finishPage(page);

        String directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/files/challan/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        File filePath = new File(directory_path + "MyChallan.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();

        Uri fileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", filePath);

        if (mode.equals("share")) {
            Intent intent = ShareCompat.IntentBuilder.from(this).setType("*/*").setStream(fileUri).setChooserTitle("Choose bar").createChooserIntent().addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else if (mode.equals("print")) {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(this, fileUri);
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            String jobName = getString(R.string.app_name) + " Document";
            PrintAttributes.Builder builder = new PrintAttributes.Builder();
            builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
            PrintAttributes printAttributes = builder.build();
            printManager.print(jobName, printDocumentAdapter, printAttributes);
        }
    }

    private void drawRectangle(float left, float top, float right, float bottom) {
        paint.reset();
        paint.setStrokeWidth(1);
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    private void drawTitle(String string, float x, float y) {
        paint.reset();
        paint.setTextSize(14);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setFakeBoldText(true);
        canvas.drawText(string, x, y, paint);
    }

    private void drawBIGTitleWithCenterAlign(String string, float x, float y) {
        paint.reset();
        paint.setTextSize(18);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(true);
        canvas.drawText(string, x, y, paint);
    }

    private void drawTitleWithCenterAlign(String string, float x, float y) {
        paint.reset();
        paint.setTextSize(14);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(true);
        canvas.drawText(string, x, y, paint);
    }

    private void drawText(String string, float x, float y) {
        paint.reset();
        paint.setTextSize(12);
        paint.setFakeBoldText(false);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        canvas.drawText(string, x, y, paint);
    }

    private void drawBoldText(String string, float x, float y) {
        paint.reset();
        paint.setTextSize(12);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        canvas.drawText(string, x, y, paint);
    }

    private void drawTextWithRightAlign(String string, float x, float y) {
        paint.reset();
        paint.setTextSize(12);
        paint.setColor(Color.BLACK);
        paint.setFakeBoldText(false);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(string, x, y, paint);
    }

    private void drawTextWithCenterAlign(String string, float x, float y) {
        paint.reset();
        paint.setTextSize(12);
        paint.setColor(Color.BLACK);
        paint.setFakeBoldText(false);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(string, x, y, paint);
    }

    private void drawHorizontalLine(float startX, float startY, float stopX, float stopY) {
        paint.reset();
        paint.setStrokeWidth(1);
        paint.setColor(Color.LTGRAY);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }
}