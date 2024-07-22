package com.saneforce.godairy.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.github.barteksc.pdfviewer.PDFView;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.PdfDocumentAdapter;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.assistantClass.Base64ToFileConverter;
import com.saneforce.godairy.databinding.ActivityViewInvoiceBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewInvoiceActivity extends AppCompatActivity {
    ActivityViewInvoiceBinding binding;
    Common_Class common_class;
    AssistantClass assistantClass;
    Context context = this;
    String OrderNo = "", InvNo = "";
    private File file = null;
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewInvoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        common_class = new Common_Class(this);
        assistantClass = new AssistantClass(context);

        OrderNo = getIntent().getStringExtra("OrderNo");
        InvNo = getIntent().getStringExtra("InvNo");

        title = "Invoice: " + InvNo;
        binding.toolbar.title.setText(title);
        binding.toolbar.share.setVisibility(View.VISIBLE);
        binding.toolbar.home.setVisibility(View.VISIBLE);
        binding.toolbar.home.setImageResource(R.drawable.ic_round_print_24);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.share.setOnClickListener(v -> ShareFile());
        binding.toolbar.home.setOnClickListener(v -> PrintFile());

        getBase64Data();
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
        printManager.print(InvNo, printDocumentAdapter, printAttributes);
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
            File newFile = new File(directory_path + "Invoice - " + InvNo + ".pdf");
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
            shareFile(fileUri);
        } catch (Exception e) {
            assistantClass.log(e.getLocalizedMessage());
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void shareFile(Uri fileUri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share File"));
    }

    private void getBase64Data() {
        String data = String.format("{\"OrderNo\":\"%s\",\"InvNo\":\"%s\"}", OrderNo, InvNo);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        service.updateAllowance("get/invoicedetail", data).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String base64Data = response.body().string();
                        file = Base64ToFileConverter.convert(base64Data);
                        binding.pdfView.fromFile(file).load();
                    } catch (IOException e) {
                        assistantClass.showAlertDialogWithDismiss(e.getLocalizedMessage());
                    }
                }
                binding.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                assistantClass.showAlertDialogWithDismiss(t.getLocalizedMessage());
            }
        });
    }
}