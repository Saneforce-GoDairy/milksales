package com.saneforce.milksales.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.saneforce.milksales.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChallanActivity extends AppCompatActivity {
    ImageView toolbarHome;
    PDFView pdfView;
    Context context = this;

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
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(842,595,  1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(12);
        canvas.drawText("Hello, this is a sample PDF created using Android's PdfDocument class!", 50, 50, paint);

        pdfDocument.finishPage(page);

        // Save the PDF to a file
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
        Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();

        // close the document
        pdfDocument.close();


        Uri fileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", filePath);

        pdfView.fromUri(fileUri).load();

        //Intent intent = ShareCompat.IntentBuilder.from(this).setType("*/*").setStream(fileUri).setChooserTitle("Choose bar").createChooserIntent().addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //startActivity(intent);
    }
}