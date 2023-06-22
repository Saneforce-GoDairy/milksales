package com.saneforce.milksales.SFA_Activity;

import static com.saneforce.milksales.SFA_Activity.HAPApp.CurrencySymbol;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.PayLedger_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;

public class PayLedgerActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {
    public TextView tvOutletName, tvStartDate, tvEndDate;
    DatePickerDialog fromDatePickerDialog;
    RecyclerView rvLedger;
    PayLedger_Adapter plAdapter;
    private Common_Class common_class;
    private String date;
    Shared_Common_Pref sharedCommonPref;
    public static String ledgerFDT = "", ledgerTDT = "";
    TextView tvGrandTot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_stmt);
        init();

        JsonObject jParam = new JsonObject();
        jParam.addProperty("FDate", ledgerFDT);
        jParam.addProperty("TDate", ledgerTDT);
        common_class.getDb_310Data(Constants.LEDGER, this, jParam);

        // tvOutletName.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
        tvOutletName.setText(sharedCommonPref.getvalue(Constants.Distributor_name));

    }

    public void init() {
        common_class = new Common_Class(this);
        sharedCommonPref = new Shared_Common_Pref(this);
        rvLedger = findViewById(R.id.rvLedger);
        tvOutletName = findViewById(R.id.retailername);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvGrandTot = findViewById(R.id.txtTotCBAmt);

        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);


        tvStartDate.setText(Common_Class.GetDatewothouttime());
        tvEndDate.setText(Common_Class.GetDatewothouttime());

        ledgerFDT = String.valueOf(tvStartDate.getText());
        ledgerTDT = String.valueOf(tvEndDate.getText());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStartDate:
                selectDate(1);
                break;
            case R.id.tvEndDate:
                selectDate(2);
                break;
        }
    }


    void selectDate(int val) {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(PayLedgerActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (common_class.checkDates(date, tvEndDate.getText().toString(), PayLedgerActivity.this) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        ledgerFDT = tvStartDate.getText().toString();
                        JsonObject jParam = new JsonObject();
                        jParam.addProperty("FDate", ledgerFDT);
                        jParam.addProperty("TDate", ledgerTDT);
                        common_class.getDb_310Data(Constants.LEDGER, PayLedgerActivity
                                .this, jParam);
                    } else
                        common_class.showMsg(PayLedgerActivity.this, "Please select valid date");
                } else {
                    if (common_class.checkDates(tvStartDate.getText().toString(), date, PayLedgerActivity.this) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        ledgerTDT = tvEndDate.getText().toString();

                        JsonObject jParam = new JsonObject();
                        jParam.addProperty("FDate", ledgerFDT);
                        jParam.addProperty("TDate", ledgerTDT);
                        common_class.getDb_310Data(Constants.LEDGER, PayLedgerActivity.this, jParam);

                    } else
                        common_class.showMsg(PayLedgerActivity.this, "Please select valid date");

                }


            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();


    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {
                switch (key) {
                    case Constants.LEDGER:
                        JSONArray legList = new JSONArray(apiDataResponse);
                        plAdapter = new PayLedger_Adapter(this, legList);
                        rvLedger.setAdapter(plAdapter);

                        double totAmt = 0;
                        for (int i = 0; i < legList.length(); i++) {
                            JSONObject obj = legList.getJSONObject(i);
                            totAmt += obj.getDouble("ClAmt");
                        }
                        tvGrandTot.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(totAmt));
                        break;
                }
            }

        } catch (Exception e) {
Log.v("LEDGER:",e.getMessage());
        }
    }
}
