package com.saneforce.godairy.SFA_Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.SFA_Adapter.AdapterPaymentHistory;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityPaymentHistoryBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaymentHistory extends AppCompatActivity {
    ActivityPaymentHistoryBinding binding;

    String title = "Payment History";

    Context context = this;
    AssistantClass assistantClass;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;

    AdapterPaymentHistory adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        assistantClass = new AssistantClass(context);
        common_class = new Common_Class(context);
        shared_common_pref = new Shared_Common_Pref(this);

        common_class.gotoHomeScreen(context, binding.toolbar.home);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText(title);

        String currentDate = assistantClass.getTime("dd/MM/yyyy");
        binding.datePicker.startDate.setText(currentDate);
        binding.datePicker.endDate.setText(currentDate);

        binding.datePicker.startDate.setOnClickListener(v -> {
            assistantClass.showDatePickerDialog(0, Calendar.getInstance().getTimeInMillis(), (date, dateForDB) -> {
                binding.datePicker.startDate.setText(date);
                binding.datePicker.endDate.setText("");
            });
        });

        binding.datePicker.endDate.setOnClickListener(v -> {
            String fromDate = binding.datePicker.startDate.getText().toString();
            if (fromDate.isEmpty()) {
                Toast.makeText(context, "Please select start date first", Toast.LENGTH_SHORT).show();
                return;
            }
            long from = 0;
            try {
                Date fromDATE = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(fromDate);
                if (fromDATE != null) {
                    from = fromDATE.getTime();
                }
            } catch (ParseException ignored) {
            }
            assistantClass.showDatePickerDialog(from, Calendar.getInstance().getTimeInMillis(), (date, dateForDB) -> {
                binding.datePicker.endDate.setText(date);
                fetchData();
            });
        });

        fetchData();
    }

    private void fetchData() {
        binding.info.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        String from = binding.datePicker.startDate.getText().toString();
        String to = binding.datePicker.endDate.getText().toString();
        if (from.isEmpty()) {
            Toast.makeText(context, "Please select start date", Toast.LENGTH_SHORT).show();
        } else if (to.isEmpty()) {
            Toast.makeText(context, "Please select end date", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("axn", "get_payment_history");
            params.put("from", assistantClass.formatDateToDB(from));
            params.put("to", assistantClass.formatDateToDB(to));
            params.put("stockistCode", shared_common_pref.getvalue(Constants.Distributor_Id));
            assistantClass.makeApiCall(params, "", new APIResult() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    binding.progressBar.setVisibility(View.GONE);
                    JSONArray myArray = jsonObject.optJSONArray("response");
                    if (myArray != null) {
                        assignData(myArray);
                    }
                }

                @Override
                public void onFailure(String error) {
                    binding.progressBar.setVisibility(View.GONE);
                    assistantClass.showAlertDialogWithDismiss(error);
                }
            });
        }
    }

    private void assignData(JSONArray response) {
        if (response.length() == 0) {
            binding.info.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
        adapter = new AdapterPaymentHistory(context, response);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setAdapter(adapter);
    }
}
