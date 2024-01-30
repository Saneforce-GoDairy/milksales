package com.saneforce.godairy.Activity_Hap;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.SFA_Adapter.AdapterDayReport;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityDayReportBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Day_Report_Activity extends AppCompatActivity {
    private ActivityDayReportBinding binding;

    Context context = this;
    Common_Class common_class;
    AssistantClass assistantClass;

    JSONArray hierarchyArray, reportArray;
    String id = "", date = "";
    AdapterDayReport adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDayReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        common_class = new Common_Class(this);
        assistantClass = new AssistantClass(context);
        hierarchyArray = new JSONArray();
        reportArray = new JSONArray();

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Day Report");
        common_class.gotoHomeScreen(context, binding.toolbar.home);

        binding.selectDate.setOnClickListener(v -> assistantClass.showDatePickerDialog(0, Calendar.getInstance().getTimeInMillis(), (_date, dateForDB) -> {
            date = _date;
            binding.selectDate.setText(_date);
            getReport();
        }));
        binding.selectFieldForce.setOnClickListener(v -> assistantClass.showDropdown("Select Employee", hierarchyArray, object -> {
            binding.selectFieldForce.setText(object.optString("sfName"));
            id = object.optString("id");
            getReport();
        }));
        binding.viewSummary.setOnClickListener(v -> {});

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.selectDate.setText(assistantClass.getTime("dd/MM/yyyy"));

        getFieldForceList();
    }

    private void getReport() {
        if (id.isEmpty() || date.isEmpty()) {
            return;
        }
        assistantClass.showProgressDialog("Fetching Report...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "getDayReport");
        params.put("sfCode", id);
        params.put("date", assistantClass.formatDateToDB(date));
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                assistantClass.dismissProgressDialog();
                reportArray = jsonObject.optJSONArray("response");
                assignData();
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithDismiss(error);
            }
        });
    }

    private void assignData() {
        if (reportArray == null) {
            return;
        }
        adapter = new AdapterDayReport(context, reportArray);
        binding.recyclerView.setAdapter(adapter);
    }

    private void getFieldForceList() {
        assistantClass.showProgressDialog("Fetching My Team...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "getDownlineHierarchy");
        params.put("date", assistantClass.formatDateToDB(binding.selectDate.getText().toString()));
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                assistantClass.dismissProgressDialog();
                hierarchyArray = jsonObject.optJSONArray("response");
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithDismiss(error);
            }
        });
    }

    /*private void initBottomSheet() {
        sheetDialog = new BottomSheetDialog(Day_Report_Activity.this, R.style.AppBottomSheetDialogTheme);
        sheetDialog.getBehavior().toString();
        sheetDialog.setContentView(R.layout.day_bottom_sheet);
        sheetDialog.show();
    }*/
}



