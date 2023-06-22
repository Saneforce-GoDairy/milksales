package com.saneforce.milksales.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.UpdateResponseUI;
import com.saneforce.milksales.R;
import com.saneforce.milksales.SFA_Adapter.GrnPendingAdapter;
import com.saneforce.milksales.SFA_Model_Class.OutletReport_View_Modal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrnPendingActivity extends AppCompatActivity implements UpdateResponseUI {

    RecyclerView recyclerView;
    List<OutletReport_View_Modal> OutletReport_View_Modal = new ArrayList<>();
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();
    Type userType;
    Gson gson;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences UserDetails;
    Common_Class common_class;
    GrnPendingAdapter mReportViewAdapter;
    CircularProgressButton save;
    String SF_code = "", div = "";
    Shared_Common_Pref sharedCommonPref;
    CircularProgressButton btnSave,btnCancel;
    public static TextView tvDispatchDate,challanNo,poNo;
    EditText receivedLoc,receivedBy,authorizedBy,remarks;
    TextView total;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grn_pending);

        gson = new Gson();
        sharedCommonPref = new Shared_Common_Pref(GrnPendingActivity.this);

        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");

        save = findViewById(R.id.btngrnSave);
        recyclerView = findViewById(R.id.rvGrnList);
        common_class = new Common_Class(this);

        total = findViewById(R.id.totalNetValue);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        common_class.getDataFromApi(Constants.GetGrn_Pending_List, this, false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDialog();
                //submitData();
            }
        });
    }

    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            switch (key) {
                case Constants.GetGrn_Pending_List:
                    Log.v("grnPendingList", apiDataResponse);

                    GetJsonData(apiDataResponse);

                    break;

            }
        } catch (Exception e) {

        }
    }


    private void GetJsonData(String jsonResponse) {

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String pname = jsonObject1.optString("Product_Name");
                String pcode = jsonObject1.optString("Product_code");
                String pmrp = jsonObject1.optString("MRP");
                String pmanfdate = jsonObject1.optString("manufactor_date");
                String pexpdate = jsonObject1.optString("exp_date");
                String pbilledqty = jsonObject1.optString("billed_qty");
                String pbatchno = jsonObject1.optString("sap_batch_no");
                String pbillingdate = jsonObject1.optString("Billing_Date");
                String pImage = jsonObject1.optString("Product_Image");
                String pUom = jsonObject1.optString("Unit_code");
                String pUomName = jsonObject1.optString("product_unit");
                Double pTax = Double.valueOf(jsonObject1.optString("total_tax_val"));

                Log.v("jsonarray123",jsonArray.toString());

                FilterOrderList.add(new OutletReport_View_Modal(pname,pcode,pmrp,pmanfdate,pexpdate,pbilledqty,pbatchno,pbillingdate,pImage,pUom,pUomName,pTax ));
            }

            mReportViewAdapter = new GrnPendingAdapter(GrnPendingActivity.this, FilterOrderList);
            recyclerView.setAdapter(mReportViewAdapter);
            mReportViewAdapter.notifyDataSetChanged();

        } catch (Exception e) {
        }

    }

    private void submitData() {
        JSONObject jObj = new JSONObject();


        try {
            jObj.put("SFCode",SF_code);
            jObj.put("divCode",div);
            jObj.put("grnDate",getIntent().getStringExtra("grnDate"));
            jObj.put("dispatchDate",tvDispatchDate.getText().toString());
            jObj.put("suppCode","0");
            jObj.put("suppName",sharedCommonPref.getvalue(Constants.Distributor_name));
            jObj.put("pono",getIntent().getStringExtra("billingID")+" - "+"("+getIntent().getStringExtra("salesID")+")");
            jObj.put("challanNo",getIntent().getStringExtra("salesID"));
            jObj.put("receivedLocation",receivedLoc.getText().toString());
            jObj.put("receivedBy",receivedBy.getText().toString());
            jObj.put("authorizedBy",authorizedBy.getText().toString());
            jObj.put("subDivCode","0");
            jObj.put("remarks",remarks.getText().toString());


            JSONArray jArr=new JSONArray();
            for (int i = 0; i < FilterOrderList.size(); i++) {
                JSONObject obj1 = new JSONObject();
                obj1.put("prodCode",FilterOrderList.get(i).getProductCode());
                obj1.put("prodName",FilterOrderList.get(i).getProductName());
                obj1.put("prodUnit",FilterOrderList.get(i).getPunit());
                obj1.put("prodUnitName",FilterOrderList.get(i).getuName());
                obj1.put("batchNo",FilterOrderList.get(i).getBatchNo());
                obj1.put("Ordered_qnty",FilterOrderList.get(i).getBilledQty());
                obj1.put("price",FilterOrderList.get(i).getMrp());
                obj1.put("Damaged_Qnty",FilterOrderList.get(i).getDamaged());
                obj1.put("manufDate",FilterOrderList.get(i).getManufDate());
                obj1.put("tax",FilterOrderList.get(i).getTaxVal());

                jArr.put(obj1);
            }
            jObj.accumulate("GRNEntryData" , jArr);


            Log.d("savehjj","ghkj"+jObj.toString());
            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Log.v("api",apiInterface.toString());
            Call<JsonObject> responseBodyCall =apiInterface.GRNSave(SF_code,div, jObj.toString());
            Log.v("divcodepos",SF_code+"   "+div );

            responseBodyCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            Log.e("JSON_VALUES", response.body().toString());
                            Toast.makeText(GrnPendingActivity.this, "GRN submitted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (Exception e) {
                            Log.v("error", e.toString());
                        }
                    } else {
                        Log.v("error_text", "Failed");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.v("errormsg", t.toString());
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            return true;
        }
        return false;
    }

    public void createNewDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View v = getLayoutInflater().inflate(R.layout.grn_popup,null);
        btnSave=v.findViewById(R.id.btngrnPopSubmit);
        btnCancel=v.findViewById(R.id.btngrnPopCancel);
        tvDispatchDate=v.findViewById(R.id.dispatchDate);
        challanNo=v.findViewById(R.id.challanNo);
        poNo=v.findViewById(R.id.poNo);
        receivedBy=v.findViewById(R.id.edtRBy);
        receivedLoc=v.findViewById(R.id.edtRLoc);
        authorizedBy=v.findViewById(R.id.edtABy);
        remarks=v.findViewById(R.id.edtRemarks);


        dialogBuilder.setView(v);
        dialog=dialogBuilder.create();
        dialog.show();

        tvDispatchDate.setText(Common_Class.GetDatewothouttime());
        challanNo.setText(getIntent().getStringExtra("salesID"));
        poNo.setText(getIntent().getStringExtra("billingID")+" - "+"("+getIntent().getStringExtra("salesID")+")");


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               submitData();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GrnPendingActivity.this,"GRN Cancelled",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        tvDispatchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(GrnPendingActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvDispatchDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });
    }


}