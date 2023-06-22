package com.saneforce.milksales.SFA_Activity;

import static com.saneforce.milksales.SFA_Activity.HAPApp.CurrencySymbol;

import android.app.DatePickerDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.PayModeAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {

    TextView tvRemainAmt, etDate, tvRetailorName, tvOutStandAmt, tvOutstandDate, tvRetailorPhone, retaileAddress;
    CircularProgressButton btnSubmit;
    EditText etRefNo, etAmtRec;
    private DatePickerDialog fromDatePickerDialog;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;

    RecyclerView rvPayMode;
    private List<Common_Model> payList = new ArrayList<>();
    PayModeAdapter payModeAdapter;
    NumberFormat formatter = new DecimalFormat("##0.00");

    Double outstandAmt;
    public String payModeLabel = "cash";
    public static PaymentActivity paymentActivity;
    final Handler handler = new Handler();
    LinearLayout llCalMob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        paymentActivity = this;
        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);

        init();


        tvRetailorName.setText(shared_common_pref.getvalue(Constants.Retailor_Name_ERP_Code));
        if (Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.Retailor_PHNo)))
            llCalMob.setVisibility(View.GONE);
        else
            tvRetailorPhone.setText(shared_common_pref.getvalue(Constants.Retailor_PHNo));
        retaileAddress.setText(shared_common_pref.getvalue(Constants.Retailor_Address));

        etAmtRec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double enterAmt = 0;
                    if (!s.toString().equals(""))
                        enterAmt = Double.parseDouble(s.toString());

                    tvRemainAmt.setText(CurrencySymbol+" " + formatter.format(outstandAmt - enterAmt));


                } catch (Exception e) {
                    Log.e("paymentAct:etAmtRec ", e.getMessage());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        common_class.getDb_310Data(Constants.OUTSTANDING, this);
        common_class.getDb_310Data(Constants.PAYMODES, this);


        tvOutstandDate.setText("Previous Outstanding as on " + Common_Class.GetDatewothouttime());

    }


    void init() {
        tvRemainAmt = findViewById(R.id.tvPayRemainAmt);
        btnSubmit = findViewById(R.id.btnPaySubmit);
        etDate = findViewById(R.id.etPayDate);
        etRefNo = findViewById(R.id.etPayRefNo);
        etAmtRec = findViewById(R.id.etPayRecAmt);
        tvRetailorName = findViewById(R.id.retailername);
        rvPayMode = findViewById(R.id.rvPayMode);
        tvOutStandAmt = findViewById(R.id.tvPayOutStandAmt);
        tvOutstandDate = findViewById(R.id.tvOutstandLabel);
        retaileAddress = findViewById(R.id.retaileAddress);
        tvRetailorPhone = findViewById(R.id.retailePhoneNum);
        llCalMob = findViewById(R.id.btnCallMob);
        llCalMob.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);
        etDate.setOnClickListener(this);

    }

    private void submitPayData() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                JSONObject HeadItem = new JSONObject();

                HeadItem.put("retailorCode", Shared_Common_Pref.OutletCode);
                HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
                HeadItem.put("divCode", Shared_Common_Pref.Div_Code);
                HeadItem.put("distributorCode", shared_common_pref.getvalue(Constants.Distributor_Id));

                HeadItem.put("outstandingAmt", outstandAmt);
                HeadItem.put("payMode", payModeLabel);

                HeadItem.put("RefNo", etRefNo.getText().toString());
                HeadItem.put("dateOfPay", etDate.getText().toString());
                HeadItem.put("amtReceived", etAmtRec.getText().toString());
                HeadItem.put("remainOutstand", tvRemainAmt.getText().toString());
                HeadItem.put("outstandDate", Common_Class.GetDatewothouttime());


                Call<ResponseBody> call = service.submitPayData(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }

                                JSONObject jsonObject = new JSONObject(is.toString());

                                if (jsonObject.getBoolean("success")) {
                                    ResetSubmitBtn(1);
                                    finish();
                                }
                                common_class.showMsg(PaymentActivity.this, jsonObject.getString("Msg"));

                            }

                        } catch (Exception e) {
                            ResetSubmitBtn(2);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>", t.toString());
                        ResetSubmitBtn(2);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                ResetSubmitBtn(0);
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());

            ResetSubmitBtn(0);

        }
    }

    public void ResetSubmitBtn(int resetMode) {
        common_class.ProgressdialogShow(0, "");
        long dely = 3;
        if (resetMode != 0) dely = 1000;
        if (resetMode == 1) {
            btnSubmit.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));
        } else {
            btnSubmit.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnSubmit.stopAnimation();
                btnSubmit.revertAnimation();
            }
        }, dely);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPaySubmit:

                btnSubmit.startAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*if (!payModeAdapter.isModeSelected()) {
                            common_class.showMsg(this, "Please select any Payment Mode");
                        } else */
                        if (etDate.getText().toString().equals("")) {
                            common_class.showMsg(PaymentActivity.this, "Please enter the Date of payment");
                            ResetSubmitBtn(0);
                        } else if (etAmtRec.getText().toString().equals("")) {
                            common_class.showMsg(PaymentActivity.this, "Please enter the Amount Received");
                            ResetSubmitBtn(0);
                        } else {
                            submitPayData();
                        }
                    }
                }, 100);

                break;

            case R.id.etPayDate:
                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(PaymentActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;

                        etDate.setText("" + year + "/" + month + "/" + dayOfMonth);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();


                break;

            case R.id.btnCallMob:
                common_class.showCalDialog(PaymentActivity.this, "Do you want to Call this Outlet?", tvRetailorPhone.getText().toString().replaceAll(",", ""));
                break;


        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            JSONObject jsonObject = new JSONObject(apiDataResponse);


            switch (key) {
                case Constants.OUTSTANDING:


                    if (jsonObject.getBoolean("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            outstandAmt = jsonArray.getJSONObject(i).getDouble("Outstanding");
                            if (outstandAmt < 0) outstandAmt = Math.abs(outstandAmt);
                            else outstandAmt = 0 - outstandAmt;
                            tvOutStandAmt.setText(CurrencySymbol+" " + formatter.format(jsonArray.getJSONObject(i).getDouble("Outstanding")));

                        }

                    } else {
                        outstandAmt = 0.00;
                        tvOutStandAmt.setText(CurrencySymbol+" " + 0.00);
                    }
                    break;
                case Constants.PAYMODES:

                    payList.clear();
                    if (jsonObject.getBoolean("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataObj = jsonArray.getJSONObject(i);
                            payList.add(new Common_Model(dataObj.getString("Name"), dataObj.getString("Code")));
                        }

                    }

                    payModeAdapter = new PayModeAdapter(payList, R.layout.adapter_paymode_layout, PaymentActivity.this);
                    rvPayMode.setAdapter(payModeAdapter);

                    break;

            }
        } catch (Exception e) {

        }
    }
}