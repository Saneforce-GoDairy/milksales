package com.saneforce.godairy.Activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.PdfDocumentAdapter;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityMyPdfViewerBinding;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class MyPDFViewer extends AppCompatActivity {
    ActivityMyPdfViewerBinding binding;
    String axn = "", day = "", month = "", year = "", sfCode = "";
    Context context = this;
    Common_Class common_class;
    AssistantClass assistantClass;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyPdfViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        common_class = new Common_Class(context);
        assistantClass = new AssistantClass(context);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.share.setVisibility(View.VISIBLE);
        binding.toolbar.home.setVisibility(View.VISIBLE);
        binding.toolbar.home.setImageResource(R.drawable.ic_round_print_24);
        binding.toolbar.share.setOnClickListener(v -> ShareFile());
        binding.toolbar.home.setOnClickListener(v -> PrintFile());

        if (getIntent().hasExtra("title")) {
            String title = getIntent().getStringExtra("title");
            binding.toolbar.title.setText(title);
            binding.toolbar.title.setSelected(true);
        }
        if (getIntent().hasExtra("axn")) {
            axn = getIntent().getStringExtra("axn");
        }
        if (getIntent().hasExtra("day")) {
            day = getIntent().getStringExtra("day");
        }
        if (getIntent().hasExtra("month")) {
            month = getIntent().getStringExtra("month");
        }
        if (getIntent().hasExtra("year")) {
            year = getIntent().getStringExtra("year");
        }
        if (getIntent().hasExtra("sfCode")) {
            sfCode = getIntent().getStringExtra("sfCode");
        }

        downloadPDF();
    }

    private void downloadPDF() {
        assistantClass.showProgressDialog("Preparing...", false);
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String pdfUrl = ApiClient.BASE_URL + "MyPHP.php?axn=" + axn + "&sfCode=" + sfCode + "&month=" + month + "&year=" + year + "&day=" + day;
                URL url = new URL(pdfUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/reports/");
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    file = new File(folder, assistantClass.getTime("HHmmssSSS") + ".pdf");
                    try {
                        file.createNewFile();
                        OutputStream outputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        inputStream.close();
                        outputStream.close();
                        runOnUiThread(() -> {
                            binding.pdfView.fromFile(file).load();
                            assistantClass.dismissProgressDialog();
                            if (file.length() < 10) {
                                assistantClass.showAlertDialogWithFinish("No data available...");
                            }
                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            assistantClass.dismissProgressDialog();
                            assistantClass.showAlertDialogWithFinish(e.getMessage());
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        assistantClass.dismissProgressDialog();
                        assistantClass.showAlertDialogWithFinish("Invalid file path");
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    assistantClass.dismissProgressDialog();
                    assistantClass.showAlertDialogWithFinish(e.getMessage());
                });
            }
        });
    }

    private void PrintFile() {
        if (file == null) {
            Toast.makeText(context, "Invalid file", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = Uri.fromFile(file);
        PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(this, uri);
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
        PrintAttributes printAttributes = builder.build();
        printManager.print(assistantClass.getTime("HHmmssSSS"), printDocumentAdapter, printAttributes);
    }

    private void ShareFile() {
        if (file == null) {
            Toast.makeText(context, "Invalid file", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/files/invoices/";
            File path = new File(directory_path);
            if (!path.exists()) {
                path.mkdirs();
            }
            File newFile = new File(directory_path + "Report_" + sfCode + "_" + day + "_" + month + "_" + year + ".pdf");
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            Uri fileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", newFile);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share File"));
        } catch (Exception e) {
            assistantClass.log(e.getLocalizedMessage());
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}