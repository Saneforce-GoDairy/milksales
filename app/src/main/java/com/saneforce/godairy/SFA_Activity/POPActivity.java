package com.saneforce.godairy.SFA_Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.POPStatusAdapter;
import com.saneforce.godairy.SFA_Adapter.PopAddAdapter;
import com.saneforce.godairy.SFA_Model_Class.Product_Details_Modal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POPActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface, UpdateResponseUI {

    TextView tvViewStatus;
    Button btnSubmit;
    TextView tvOrder, tvOtherBrand, tvQPS, tvCoolerInfo, tvBookingDate;

    RecyclerView rvQps;

    POPStatusAdapter popStatusAdapter;
    Common_Class common_class;

    RecyclerView rvPopAdd;

    PopAddAdapter popAddAdapter;

    ImageView ivAdd;
    private DatePickerDialog fromDatePickerDialog;

    List<Product_Details_Modal> popAddList = new ArrayList<>();
    private ArrayList<Common_Model> popMaterialList = new ArrayList<>();
    public static POPActivity popActivity;
    private int selectedPos;
    Shared_Common_Pref shared_common_pref;
    EditText etRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_layout);
        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);
        popActivity = this;

        btnSubmit = findViewById(R.id.btnSubmit);
        tvViewStatus = findViewById(R.id.tvPOPViewStatus);
        etRemarks = findViewById(R.id.etRemarks);

        tvOrder = (TextView) findViewById(R.id.tvOrder);
        tvQPS = (TextView) findViewById(R.id.tvQPS);
        tvOtherBrand = (TextView) findViewById(R.id.tvOtherBrand);
        tvCoolerInfo = (TextView) findViewById(R.id.tvCoolerInfo);
        rvQps = (RecyclerView) findViewById(R.id.rvPOP);
        rvPopAdd = (RecyclerView) findViewById(R.id.rvPOPAdd);
        ivAdd = (ImageView) findViewById(R.id.ivAddPOP);
        tvBookingDate = (TextView) findViewById(R.id.tvBookingDate);

        TextView tvRetailorName = findViewById(R.id.Category_Nametext);

        tvRetailorName.setText(Shared_Common_Pref.OutletName);


        tvOrder.setOnClickListener(this);
        tvOtherBrand.setOnClickListener(this);
        tvQPS.setOnClickListener(this);
        tvCoolerInfo.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvBookingDate.setOnClickListener(this);
        tvViewStatus.setOnClickListener(this);

        findViewById(R.id.tvPOP).setVisibility(View.GONE);


        popAddList.add(new Product_Details_Modal("", "", "", 0, ""));

        popAddAdapter = new PopAddAdapter(popAddList, R.layout.pop_add_recyclerview,
                this);

        rvPopAdd.setAdapter(popAddAdapter);


        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);

        tvCoolerInfo.setVisibility(View.GONE);

        if (Shared_Common_Pref.Freezer_Required.equalsIgnoreCase("yes"))
            tvCoolerInfo.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (tvViewStatus.getVisibility() == View.VISIBLE) {
                common_class.CommonIntentwithFinish(Invoice_History.class);
            } else {
                tvViewStatus.setVisibility(View.VISIBLE);
                findViewById(R.id.rlBookingDate).setVisibility(View.VISIBLE);
                findViewById(R.id.llPOPStatus).setVisibility(View.VISIBLE);
                findViewById(R.id.llPOPRequestStatus).setVisibility(View.GONE);
                ivAdd.setVisibility(View.VISIBLE);
                // btnSubmit.setText("Completed");
                // btnSubmit.setVisibility(View.VISIBLE);
                findViewById(R.id.btnSubmit).setVisibility(View.VISIBLE);
            }

            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPOPViewStatus:

                common_class.getDb_310Data(Constants.POP_ENTRY_STATUS, this);
                break;
            case R.id.tvBookingDate:
                Calendar newCalendar = Calendar.getInstance();
                fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int mnth = monthOfYear + 1;

                        tvBookingDate.setText("" + year + "-" + mnth + "-" + dayOfMonth);

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.show();
                break;
            case R.id.tvOrder:
                common_class.commonDialog(this, Order_Category_Select.class, "POP?");
                break;
            case R.id.tvOtherBrand:
                common_class.commonDialog(this, OtherBrandActivity.class, "POP?");
                break;
            case R.id.tvQPS:
                common_class.commonDialog(this, QPSActivity.class, "POP?");
                break;
            case R.id.tvCoolerInfo:
                common_class.commonDialog(this, CoolerInfoActivity.class, "POP?");
                break;
            case R.id.ivAddPOP:
                popAddList.add(new Product_Details_Modal("", "", "", 0, ""));
                popAddAdapter.notifyData(popAddList);

                break;
            case R.id.btnSubmit:
                if (tvBookingDate.getText().toString().equals(""))
                    common_class.showMsg(this, "Enter Date");
                else
                    SaveOrder();
                break;
        }
    }

    private void SaveOrder() {

        List<Product_Details_Modal> submitPOPList = new ArrayList<>();
        submitPOPList.clear();

        for (int i = 0; i < popAddList.size(); i++) {
            if (Common_Class.isNullOrEmpty(popAddList.get(i).getName())) {
                common_class.showMsg(this, "Name in position " + (i + 1) + " is blank.");
                return;
            } else if (popAddList.get(i).getQty() <= 0) {
                common_class.showMsg(this, "Qty in position " + (i + 1) + " is zero.");
                return;
            } else if (Common_Class.isNullOrEmpty(popAddList.get(i).getUOM())) {
                common_class.showMsg(this, "UOM in position " + (i + 1) + " is blank.");
                return;
            } else {
                submitPOPList.add(popAddList.get(i));
            }
        }

        if (submitPOPList.size() > 0) {
            if (common_class.isNetworkAvailable(this)) {

                AlertDialogBox.showDialog(POPActivity.this, HAPApp.Title, "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {

                        JSONArray data = new JSONArray();
                        JSONObject ActivityData = new JSONObject();

                        try {
                            JSONObject HeadItem = new JSONObject();
                            HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                            HeadItem.put("divCode", Shared_Common_Pref.Div_Code);
                            HeadItem.put("CustCode", Shared_Common_Pref.OutletCode);
                            HeadItem.put("CustName", Shared_Common_Pref.OutletName);
                            HeadItem.put("StkCode", shared_common_pref.getvalue(Constants.Distributor_Id));
                            HeadItem.put("Datetime", Common_Class.GetDate());
                            HeadItem.put("date", tvBookingDate.getText().toString());
                            HeadItem.put("remarks", etRemarks.getText().toString());

                            ActivityData.put("Json_Head", HeadItem);


                            JSONArray Order_Details = new JSONArray();
                            for (int z = 0; z < submitPOPList.size(); z++) {
                                JSONObject ProdItem = new JSONObject();
                                ProdItem.put("id", submitPOPList.get(z).getId());
                                ProdItem.put("material", submitPOPList.get(z).getName());
                                // ProdItem.put("date", submitPOPList.get(z).getBookingDate());
                                ProdItem.put("Qty", submitPOPList.get(z).getQty());
                                ProdItem.put("uom", submitPOPList.get(z).getUOM());

                                Order_Details.put(ProdItem);
                            }
                            ActivityData.put("Entry_Details", Order_Details);
                            data.put(ActivityData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonObject> responseBodyCall = apiInterface.savePOP(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
                        responseBodyCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        Log.e("JSON_VALUES", response.body().toString());
                                        JSONObject jsonObjects = new JSONObject(response.body().toString());
                                        if (jsonObjects.getString("success").equals("true")) {
                                            startActivity(new Intent(getApplicationContext(), Invoice_History.class));
                                            finish();
                                        }
                                        common_class.showMsg(POPActivity.this, jsonObjects.getString("Msg"));

                                    } catch (Exception e) {

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.e("SUBMIT_VALUE", "ERROR");
                            }
                        });

                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            } else {
                Toast.makeText(this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        } else {
            common_class.showMsg(this, "POP is empty");
        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        if (selectedPos >= 0) {
            common_class.dismissCommonDialog(type);
            popAddList.set(selectedPos, new Product_Details_Modal(myDataset.get(position).getId(), myDataset.get(position).getName(),
                    "", 0, myDataset.get(position).getFlag()));
            popAddAdapter.notifyData(popAddList);

        }
    }

    public void showBrandDialog(int position) {
        selectedPos = position;
        if (popMaterialList.size() == 0)
            common_class.getDb_310Data(Constants.POP_MATERIAL, this);
        else
            common_class.showCommonDialog(popMaterialList, 1, this);
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {

        try {
            switch (key) {
                case Constants.POP_ENTRY_STATUS:
                    JSONObject jsonObject = new JSONObject(apiDataResponse);
                    if (jsonObject.getBoolean("success")) {
                        tvViewStatus.setVisibility(View.GONE);
                        findViewById(R.id.rlBookingDate).setVisibility(View.GONE);
                        findViewById(R.id.llPOPStatus).setVisibility(View.GONE);
                        findViewById(R.id.llPOPRequestStatus).setVisibility(View.VISIBLE);
                        ivAdd.setVisibility(View.GONE);
                        findViewById(R.id.btnSubmit).setVisibility(View.GONE);
                        popStatusAdapter = new POPStatusAdapter(this, jsonObject.getJSONArray("Data"));
                        rvQps.setAdapter(popStatusAdapter);
                    } else {
                        common_class.showMsg(POPActivity.this, jsonObject.getString("Msg"));
                    }
                    break;
                case Constants.POP_MATERIAL:
                    JSONObject materialObj = new JSONObject(apiDataResponse);
                    JSONArray jsonArray = materialObj.getJSONArray("Data");
                    popMaterialList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        popMaterialList.add(new Common_Model(jsonObject1.getString("POP_Code"), jsonObject1.getString("POP_Name"),
                                jsonObject1.getString("POP_UOM")));
                    }

                    common_class.showCommonDialog(popMaterialList, 1, this);
                    break;
            }
        } catch (Exception e) {

        }

    }
}