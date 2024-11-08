package com.saneforce.godairy.procurement.custom_form;

import static android.view.View.GONE;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_CUSTOM_FORM_MODULE_LIST;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityCustomFormDashboardBinding;
import com.saneforce.godairy.procurement.custom_form.adapter.ModuleAdapter;
import com.saneforce.godairy.procurement.custom_form.model.ModuleList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomFormDashboardActivity extends AppCompatActivity {
    private ActivityCustomFormDashboardBinding binding;
    public static final String APP_DATA = "/procurement";
    private final Context context = this;
    private ApiInterface apiInterface;
    private List<ModuleList> moduleArrayList;
    int isPrimary = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomFormDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("isPrimary")) {
            isPrimary = getIntent().getIntExtra("isPrimary", 0);
        }

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        moduleArrayList = new ArrayList();

        loadCustomFormModule();

        onClick();
    }

    private void onClick() {
        binding.back.setOnClickListener(v -> finish());
    }

    private void loadCustomFormModule() {
        Call<ResponseBody> call = apiInterface.getProcCustomFormModule(PROCUREMENT_GET_CUSTOM_FORM_MODULE_LIST, isPrimary);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                String hi = "";
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(GONE);
                    String moduleList;
                    try {
                        moduleList = response.body().string();
                        Log.e("res_", moduleList);
                        JSONArray jsonArray = new JSONArray(moduleList);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ModuleList moduleListModel = new ModuleList();
                            moduleListModel.setModuleName(jsonObject.getString("ModuleName"));
                            moduleListModel.setModuleId(jsonObject.getString("ModuleId"));
                            moduleArrayList.add(moduleListModel);
                        }

                        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                        binding.recyclerView.setHasFixedSize(true);
                        binding.recyclerView.setItemViewCacheSize(20);
                        ModuleAdapter moduleAdapter = new ModuleAdapter(context, moduleArrayList, isPrimary);
                        binding.recyclerView.setAdapter(moduleAdapter);
                    } catch (IOException | JSONException e) {
                        // throw new RuntimeException(e);
                        showError();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                showError();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showError() {
        binding.shimmerLayout.setVisibility(GONE);
        binding.recyclerView.setVisibility(GONE);
        binding.nullError.setVisibility(View.VISIBLE);
        binding.message.setText(R.string.something_went_wrong);
    }
}