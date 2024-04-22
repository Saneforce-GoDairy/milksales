package com.saneforce.godairy.SFA_Activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.SFA_Adapter.InvoiceListAdapter;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityVwInvoicesBinding;

import org.json.JSONArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vwInvoices extends AppCompatActivity {
    ActivityVwInvoicesBinding binding;
    Common_Class common_class;
    AssistantClass assistantClass;
    Context context = this;
    String poNo = "", salNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVwInvoicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        assistantClass = new AssistantClass(context);
        common_class = new Common_Class(this);

        poNo = getIntent().getStringExtra("PONO");
        salNo = getIntent().getStringExtra("salno");

        binding.txtIndentNo.setText(poNo);
        binding.txtPONo.setText(salNo);

        getInvoiceList();
    }

    private void getInvoiceList() {
        assistantClass.showProgressDialog("Getting invoice list...", false);
        String data = String.format("{\"OrderNo\":\"%s\"}", salNo);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        service.updateAllowance("get/invoiceslist", data).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String data = response.body().string();
                        JSONArray array = new JSONArray(data);
                        AssignData(array);
                        assistantClass.log("Invoice Response: " + data);
                    } catch (Exception e) {
                        assistantClass.showAlertDialogWithDismiss(e.getLocalizedMessage());
                    }
                }
                assistantClass.dismissProgressDialog();

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithDismiss(t.getLocalizedMessage());
            }
        });
    }

    private void AssignData(JSONArray array) {
        if (array == null) {
            array = new JSONArray();
        }
        InvoiceListAdapter adapter = new InvoiceListAdapter(context, array);
        binding.rvPrimary.setLayoutManager(new LinearLayoutManager(context));
        binding.rvPrimary.setAdapter(adapter);
        if (array.length() == 0) {
            assistantClass.showAlertDialogWithFinish("Invoice list is empty");
        }
    }
}