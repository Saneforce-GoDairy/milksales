package com.saneforce.milksales.SFA_Activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.github.barteksc.pdfviewer.PDFView;
import com.saneforce.milksales.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChallanActivity extends AppCompatActivity {
    ImageView toolbarHome;
    PDFView pdfView;
    Context context = this;

    Paint paint = new Paint();
    Canvas canvas;

    final int pageWidth = 595, pageHeight = 842;
    final float xMin = 20, xMax = pageWidth - 20;
    final float x = 40;
    float y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challan);

        toolbarHome = findViewById(R.id.toolbar_home);
        pdfView = findViewById(R.id.pdfView);

        createChallanPDF();
    }

    private void createChallanPDF() {
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

        y += 30;
        drawTitleWithCenterAlign("Lactalis India Pvt Ltd", (float) pageWidth / 2, y);

        y += 20;
        drawHorizontalLine(xMin, y, xMax, y);

        // ------------------------------------------------------------------------- Receipt No

        y += 30;
        drawText("Receipt No: ", x, y);
        float temp1 = paint.measureText("Receipt No: ");

        String docId = "9876543210";
        drawTitle(docId, x + temp1 + 10, y);
        float temp2 = paint.measureText(docId);

        drawHorizontalLine(x + temp1 + 5, y + 5, x + temp1 + 15 + temp2, y + 5);

        String date = "23/08/2023";
        drawTextWithRightAlign(date, xMax - 25, y);
        float temp = paint.measureText(date);
        drawHorizontalLine(xMax - 20, y + 5, xMax - 20 - temp - 5 - 5, y + 5);

        drawTextWithRightAlign("Date: ", xMax - 20 - temp - 15, y);

        // ------------------------------------------------------------------------- Axis Bank, Branch

        y += 40;
        String data = "Axis Bank, Branch: ";
        drawText(data, x, y);
        temp = paint.measureText(data);

        String branchName = "My Branch";
        drawText(branchName, x + temp + 15, y);
        temp2 = paint.measureText(branchName);
        drawRectangle(x + temp + 5, y - 20, x + temp + 15 + temp2 + 10, y + 10);

        y += 25;
        drawHorizontalLine(xMin, y, xMax, y);

        // ------------------------------------------------------------------------- Employee ID

        y += 40;
        drawText("Employee ID: ", x, y);
        String empId = "Employee ID";
        drawText(empId, x + 110, y);
        drawRectangle(x + 100, y - 20, x + 350, y + 10);

        // ------------------------------------------------------------------------- Employee Name

        y += 40;
        drawText("Employee Name: ", x, y);
        String empName = "Employee Name";
        drawText(empName, x + 110, y);
        drawRectangle(x + 100, y - 20, x + 350, y + 10);

        // ------------------------------------------------------------------------- Branch ID

        y += 40;
        drawText("Branch ID: ", x, y);
        String branchId = "Branch ID";
        drawText(branchId, x + 110, y);
        drawRectangle(x + 100, y - 20, x + 350, y + 10);

        // ------------------------------------------------------------------------- Branch Name

        y += 40;
        drawText("Branch Name: ", x, y);
        String bName = "Branch Name";
        drawText(bName, x + 110, y);
        drawRectangle(x + 100, y - 20, x + 350, y + 10);

        // -------------------------------------------------------------------------

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


        drawHorizontalLine(x, tableStart, x, tableStart + 140);
        drawHorizontalLine(xMax - 20, tableStart, xMax - 20, tableStart + 140);
        drawHorizontalLine(amtColStart, tableStart, amtColStart, tableStart + 140);
        drawHorizontalLine(totColStart, tableStart, totColStart, tableStart + 140);

        y = tableStart + 140;
        drawHorizontalLine(x, y, xMax - 20, y);

        // ------------------------------------------------------------------------- Amount in words

        y += 25;
        drawText("Amount in words: ", x, y);
        float amountSize = paint.measureText("Amount in words: ");
        drawText("Five hundred rupees only", x + amountSize + 5, y);

        // ------------------------------------------------------------------------- Depositor’s Sign

        y += 35;
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
        drawHorizontalLine(x, y, xMax - 20, y);

        drawHorizontalLine(x, table2Start, x, table2Start + 80);
        drawHorizontalLine(xMax - 20, table2Start, xMax - 20, table2Start + 80);
        drawHorizontalLine(bankStampColStart, table2Start, bankStampColStart, table2Start + 80);

        y = table2Start + 80;
        drawHorizontalLine(x, y, xMax - 20, y);

        // ------------------------------------------------------------------------- For Bank Use

        y += 20;
        drawHorizontalLine(xMin, y, xMax, y);

        y += 20;
        drawTitleWithCenterAlign("For Bank Use", bankStampColStart, y);

        y += 25;
        drawText("Received Rs. _____________  (Rs. ______________________________________________________________________ )", x, y);

        y += 25;
        drawText("on _______________ 2023", x, y);





        pdfDocument.finishPage(page);
        File pdfFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "test.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/files/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + System.currentTimeMillis() + "MyChallan.pdf";
        File filePath = new File(targetPdf);
        try {
            pdfDocument.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
        Uri fileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", filePath);
        pdfView.fromUri(fileUri).load();

        //Intent intent = ShareCompat.IntentBuilder.from(this).setType("*/*").setStream(fileUri).setChooserTitle("Choose bar").createChooserIntent().addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //startActivity(intent);
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
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        canvas.drawText(string, x, y, paint);
    }

    private void drawTextWithRightAlign(String string, float x, float y) {
        paint.reset();
        paint.setTextSize(12);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(string, x, y, paint);
    }

    private void drawTextWithCenterAlign(String string, float x, float y) {
        paint.reset();
        paint.setTextSize(12);
        paint.setColor(Color.BLACK);
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