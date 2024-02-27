package com.saneforce.godairy.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.assistantClass.Base64ToFileConverter;
import com.saneforce.godairy.databinding.ActivityViewInvoiceBinding;

import java.io.File;
import java.io.IOException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewInvoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        common_class = new Common_Class(this);
        assistantClass = new AssistantClass(context);

        OrderNo = getIntent().getStringExtra("OrderNo");
        InvNo = getIntent().getStringExtra("InvNo");

        String title = "Invoice: " + InvNo;
        binding.toolbar.title.setText(title);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        common_class.gotoHomeScreen(context, binding.toolbar.home);

        getBase64Data();
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
                        File file = Base64ToFileConverter.convert(base64Data);
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