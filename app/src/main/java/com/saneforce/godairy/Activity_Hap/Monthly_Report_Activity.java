package com.saneforce.godairy.Activity_Hap;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.AdapterDayReport;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityDayReportBinding;
import com.saneforce.godairy.databinding.ActivityMonthlyReportBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class Monthly_Report_Activity extends AppCompatActivity {
    Context context = this;
    Common_Class common_class;
    AssistantClass assistantClass;
    JSONArray hierarchyArray, reportArray;
    String id = "";
    int month = 0, year = 0, currentYear, currentMonth;
    AdapterDayReport adapter;
    double distVisitedCount = 0, retVisitedCount = 0, distOrderTaken = 0, retOrderTaken = 0, distInvoicedCount = 0, retInvoicedCount = 0, distOrderedAmt = 0, retOrderedAmt = 0, distInvoicedAmt = 0, retInvoicedAmt = 0;
    private ActivityMonthlyReportBinding binding;
    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMonthlyReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        common_class = new Common_Class(this);
        assistantClass = new AssistantClass(context);
        hierarchyArray = new JSONArray();
        reportArray = new JSONArray();

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Monthly Report");
        common_class.gotoHomeScreen(context, binding.toolbar.home);

        final Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        month = currentMonth;
        year = currentYear;

        binding.selectDate.setOnClickListener(v -> showMonthPicker());

        binding.selectFieldForce.setOnClickListener(v -> assistantClass.showDropdown("Select Employee", hierarchyArray, object -> {
            binding.selectFieldForce.setText(object.optString("sfName"));
            id = object.optString("id");
            getReport();
        }));

        binding.summaryView.setOnClickListener(v -> showBottomSheet());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        getFieldForceList();

        bottomSheetDialog = new BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme);
        bottomSheetView = View.inflate(context, R.layout.day_bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        TextView tv_view_summery = bottomSheetView.findViewById(R.id.tv_view_summery);
        tv_view_summery.setOnClickListener(v -> bottomSheetDialog.dismiss());

        id = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("Sfcode", "");
        binding.selectFieldForce.setText(getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("SfName", ""));

        setDate();
    }

    private void showMonthPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_month_picker, null, false);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        NumberPicker monthPicker, yearPicker;
        monthPicker = view.findViewById(R.id.monthPicker);
        yearPicker = view.findViewById(R.id.yearPicker);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(currentMonth + 1);

        yearPicker.setMinValue(currentYear - 1);
        yearPicker.setMaxValue(currentYear);
        yearPicker.setValue(currentYear);

        TextView cancel, ok;
        cancel = view.findViewById(R.id.cancel);
        ok = view.findViewById(R.id.ok);
        cancel.setOnClickListener(view1 -> dialog.dismiss());
        ok.setOnClickListener(view1 -> {
            dialog.dismiss();
            month = (monthPicker.getValue() - 1);
            year = yearPicker.getValue();
            setDate();
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void setDate() {
        String MMM = getResources().getStringArray(R.array.months_array)[month];
        binding.selectDate.setText(MMM + " " + year);
        getReport();
    }

    private void showBottomSheet() {
        bottomSheetDialog.show();
    }

    private void getReport() {
        if (id.isEmpty()) {
            return;
        }
        assistantClass.showProgressDialog("Fetching Report...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "getMonthReport");
        params.put("sfCode", id);
        params.put("month", String.valueOf(month + 1));
        params.put("year", String.valueOf(year));
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
            reportArray = new JSONArray();
        }
        adapter = new AdapterDayReport(context, reportArray);
        binding.recyclerView.setAdapter(adapter);
        Executors.newSingleThreadExecutor().execute(() -> {
            distVisitedCount = 0;
            retVisitedCount = 0;
            distOrderTaken = 0;
            retOrderTaken = 0;
            distInvoicedCount = 0;
            retInvoicedCount = 0;
            distOrderedAmt = 0;
            retOrderedAmt = 0;
            distInvoicedAmt = 0;
            retInvoicedAmt = 0;
            for (int i = 0; i < reportArray.length(); i++) {
                distVisitedCount += reportArray.optJSONObject(i).optDouble("DistVisitedCount");
                retVisitedCount += reportArray.optJSONObject(i).optDouble("RetVisitedCount");

                distOrderTaken += reportArray.optJSONObject(i).optDouble("DistOrderCount");
                retOrderTaken += reportArray.optJSONObject(i).optDouble("RetOrderCount");
                distInvoicedCount += reportArray.optJSONObject(i).optDouble("DistInvoiceCount");
                retInvoicedCount += reportArray.optJSONObject(i).optDouble("RetInvoiceCount");

                distOrderedAmt += reportArray.optJSONObject(i).optDouble("DistOrderAmt");
                retOrderedAmt += reportArray.optJSONObject(i).optDouble("RetOrderAmt");
                distInvoicedAmt += reportArray.optJSONObject(i).optDouble("DistInvoiceAmt");
                retInvoicedAmt += reportArray.optJSONObject(i).optDouble("RetInvoiceAmt");

                runOnUiThread(() -> {
                    TextView visited_distributor = bottomSheetView.findViewById(R.id.visited_distributor);
                    TextView visited_outlet = bottomSheetView.findViewById(R.id.visited_outlet);
                    TextView distOrderCount = bottomSheetView.findViewById(R.id.distOrderCount);
                    TextView retOrderCount = bottomSheetView.findViewById(R.id.retOrderCount);
                    TextView count_distributor = bottomSheetView.findViewById(R.id.count_distributor);
                    TextView count_outlet = bottomSheetView.findViewById(R.id.count_outlet);
                    TextView ordered_distributor = bottomSheetView.findViewById(R.id.ordered_distributor);
                    TextView ordered_outlet = bottomSheetView.findViewById(R.id.ordered_outlet);
                    TextView invoiced_distributor = bottomSheetView.findViewById(R.id.invoiced_distributor);
                    TextView invoiced_outlet = bottomSheetView.findViewById(R.id.invoiced_outlet);

                    visited_distributor.setText(new DecimalFormat("00").format(distVisitedCount));
                    visited_outlet.setText(new DecimalFormat("00").format(retVisitedCount));
                    distOrderCount.setText(new DecimalFormat("00").format(distOrderTaken));
                    retOrderCount.setText(new DecimalFormat("00").format(retOrderTaken));
                    count_distributor.setText(new DecimalFormat("00").format(distInvoicedCount));
                    count_outlet.setText(new DecimalFormat("00").format(retInvoicedCount));
                    ordered_distributor.setText(new DecimalFormat("00").format(distOrderedAmt));
                    ordered_outlet.setText(new DecimalFormat("00").format(retOrderedAmt));
                    invoiced_distributor.setText(new DecimalFormat("00").format(distInvoicedAmt));
                    invoiced_outlet.setText(new DecimalFormat("00").format(retInvoicedAmt));
                });
            }
        });
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
}



