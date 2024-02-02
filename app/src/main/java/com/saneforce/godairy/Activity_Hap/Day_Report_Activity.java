package com.saneforce.godairy.Activity_Hap;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.AdapterDayReport;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityDayReportBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class Day_Report_Activity extends AppCompatActivity {
    Context context = this;
    Common_Class common_class;
    AssistantClass assistantClass;
    JSONArray hierarchyArray, reportArray;
    String id = "", date = "";
    AdapterDayReport adapter;
    double distVisitedCount = 0, retVisitedCount = 0, distOrderTaken = 0, retOrderTaken = 0, distInvoicedCount = 0, retInvoicedCount = 0, distOrderedAmt = 0, retOrderedAmt = 0, distInvoicedAmt = 0, retInvoicedAmt = 0;
    private ActivityDayReportBinding binding;
    BottomSheetDialog bottomSheetDialog;
    View bottomSheetView;

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
        binding.viewSummary.setOnClickListener(v -> showBottomSheet());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        date = assistantClass.getTime("dd/MM/yyyy");
        binding.selectDate.setText(date);

        getFieldForceList();

        bottomSheetDialog = new BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme);
        bottomSheetView = View.inflate(context, R.layout.day_bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        TextView tv_view_summery = bottomSheetView.findViewById(R.id.tv_view_summery);
        tv_view_summery.setOnClickListener(v -> bottomSheetDialog.dismiss());
    }

    private void showBottomSheet() {
        bottomSheetDialog.show();
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
        assistantClass.showProgressDialog("Fetching My Team", false);
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



