package com.milksales.godairy.SFA_Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.milksales.godairy.Common_Class.AlertDialogBox;
import com.milksales.godairy.Common_Class.Common_Class;
import com.milksales.godairy.Common_Class.Common_Model;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.Common_Class.Shared_Common_Pref;
import com.milksales.godairy.Interface.AlertBox;
import com.milksales.godairy.Interface.ApiClient;
import com.milksales.godairy.Interface.ApiInterface;
import com.milksales.godairy.Interface.Master_Interface;
import com.milksales.godairy.Interface.UpdateResponseUI;
import com.milksales.godairy.R;
import com.milksales.godairy.SFA_Adapter.QPS_Modal;
import com.milksales.godairy.common.DatabaseHandler;
import com.milksales.godairy.common.FileUploadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherBrandActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface, UpdateResponseUI {

    public static OtherBrandActivity otherBrandActivity;
    List<QPS_Modal> Getorder_Array_List;
    TextView tvOrder, tvQPS, tvPOP, tvCoolerInfo, tvAddBrand;
    OtherBrandAdapter otherBrandAdapter;
    Common_Class common_class;
    Shared_Common_Pref sharedCommonPref;
    private List<Common_Model> otherBrandList = new ArrayList<>();
    private int selectedPos = -1;
    private Type userTypeCompetitor;
    private TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_brand_layout);
        otherBrandActivity = this;

        findViewById(R.id.tvOtherBrand).setVisibility(View.GONE);


        common_class = new Common_Class(this);
        sharedCommonPref = new Shared_Common_Pref(this);
        common_class.getDataFromApi(Constants.Competitor_List, this, false);


        RecyclerView recyclerView = findViewById(R.id.orderrecyclerview);

        tvOrder = (TextView) findViewById(R.id.tvOrder);
        tvPOP = (TextView) findViewById(R.id.tvPOP);
        tvQPS = (TextView) findViewById(R.id.tvQPS);
        tvCoolerInfo = (TextView) findViewById(R.id.tvCoolerInfo);
        tvAddBrand = (TextView) findViewById(R.id.tvAddBrand);
        tvSubmit = (TextView) findViewById(R.id.btnSubmit);

        TextView tvRetailorName = findViewById(R.id.Category_Nametext);

        tvRetailorName.setText(Shared_Common_Pref.OutletName);


        tvOrder.setOnClickListener(this);
        tvQPS.setOnClickListener(this);
        tvPOP.setOnClickListener(this);
        tvCoolerInfo.setOnClickListener(this);
        tvAddBrand.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);


        Getorder_Array_List = new ArrayList<>();


        Getorder_Array_List.add(new QPS_Modal("", "", "", 0, 0, 0, ""));

        otherBrandAdapter = new OtherBrandAdapter(Getorder_Array_List, R.layout.other_brand_order_recyclerview,
                this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(otherBrandAdapter);
        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);

        tvCoolerInfo.setVisibility(View.GONE);

        if(Shared_Common_Pref.Freezer_Required.equalsIgnoreCase("yes"))
            tvCoolerInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        Common_Class common_class = new Common_Class(this);
        switch (v.getId()) {
            case R.id.tvOrder:
                common_class.commonDialog(this, Order_Category_Select.class, "Other Brand?");
                break;
            case R.id.tvQPS:
                common_class.commonDialog(this, QPSActivity.class, "Other Brand?");
                break;
            case R.id.tvPOP:
                common_class.commonDialog(this, POPActivity.class, "Other Brand?");
                break;
            case R.id.tvCoolerInfo:
                common_class.commonDialog(this, CoolerInfoActivity.class, "Other Brand?");
                break;

            case R.id.tvAddBrand:
                Getorder_Array_List.add(new QPS_Modal("", "", "", 0, 0, 0, ""));
                otherBrandAdapter.notifyData(Getorder_Array_List);
                break;
            case R.id.btnSubmit:
                SaveOrder();
                break;
        }
    }

    private void SaveOrder() {
        List<QPS_Modal> submitBrandList = new ArrayList<>();
        submitBrandList.clear();
        for (int i = 0; i < Getorder_Array_List.size(); i++) {

            if (Getorder_Array_List.get(i).getName().equals("")) {
                common_class.showMsg(this, "Other Brand name in position " + (i + 1) + " is blank.");
                return;
            } else if (Getorder_Array_List.get(i).getSku().equals("")) {
                common_class.showMsg(this, "SKU position " + (i + 1) + " is blank.");
                return;

            } else if (Getorder_Array_List.get(i).getAmount().equals("")) {
                common_class.showMsg(this, "No Amount in position " + (i + 1));
                return;

            } else if (Getorder_Array_List.get(i).getScheme().equals("")) {
                common_class.showMsg(this, "Scheme position " + (i + 1) + " is blank.");
                return;
            } else if (!Getorder_Array_List.get(i).getName().equals("") && !Getorder_Array_List.get(i).getSku().equals("") &&
                    Getorder_Array_List.get(i).getAmount() > 0 &&
                    !Getorder_Array_List.get(i).getScheme().equals("")) {
                submitBrandList.add(Getorder_Array_List.get(i));

            }
        }

        if (submitBrandList.size() > 0) {
            if (common_class.isNetworkAvailable(this)) {
                AlertDialogBox.showDialog(OtherBrandActivity.this, HAPApp.Title, "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
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
                            HeadItem.put("StkCode", sharedCommonPref.getvalue(Constants.Distributor_Id));
                            HeadItem.put("Datetime",Common_Class.GetDate());
                            ActivityData.put("Json_Head", HeadItem);

                            JSONArray Order_Details = new JSONArray();
                            for (int z = 0; z < submitBrandList.size(); z++) {
                                JSONObject ProdItem = new JSONObject();
                                ProdItem.put("id", submitBrandList.get(z).getP_id());
                                ProdItem.put("BrandNm", submitBrandList.get(z).getName());
                                ProdItem.put("BrandSKU", submitBrandList.get(z).getSku());
                                ProdItem.put("Qty", submitBrandList.get(z).getQty());
                                //  ProdItem.put("Product_RegularQty", Getorder_Array_List.get(z).getRegularQty());
                                ProdItem.put("Price", submitBrandList.get(z).getPrice());
                                ProdItem.put("Amt", submitBrandList.get(z).getAmount());
                                ProdItem.put("Sch", submitBrandList.get(z).getScheme());

                                JSONArray fileArr = new JSONArray();
                                if (submitBrandList.get(z).getFileUrls() != null &&
                                        submitBrandList.get(z).getFileUrls().size() > 0) {

                                    for (int i = 0; i < submitBrandList.get(z).getFileUrls().size(); i++) {
                                        JSONObject fileData = new JSONObject();
                                        File file = new File(submitBrandList.get(z).getFileUrls().get(i));
                                        fileData.put("ob_filename", file.getName());
                                        fileArr.put(fileData);

                                    }
                                }

                                ProdItem.put("file_Details", fileArr);
                                Order_Details.put(ProdItem);
                            }
                            ActivityData.put("Entry_Details", Order_Details);
                            data.put(ActivityData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonObject> responseBodyCall = apiInterface.saveOtherBrand(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
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
                                        common_class.showMsg(OtherBrandActivity.this, jsonObjects.getString("Msg"));

                                    } catch (Exception e) {

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.e("SUBMIT_VALUE", "ERROR");
                            }
                        });


                        for (int i = 0; i < submitBrandList.size(); i++) {
                            if (submitBrandList.get(i).getFileUrls() != null &&
                                    submitBrandList.get(i).getFileUrls().size() > 0) {

                                for (int j = 0; j < submitBrandList.get(i).getFileUrls().size(); j++) {
                                    String filePath = submitBrandList.get(i).getFileUrls().get(j).replaceAll("file:/","");
                                    File file = new File(filePath);
                                   // Uri contentUri = Uri.fromFile(file);

                                    Intent mIntent = new Intent(OtherBrandActivity.this, FileUploadService.class);
                                    mIntent.putExtra("mFilePath", filePath);
                                    mIntent.putExtra("SF", Shared_Common_Pref.Sf_Code);
                                    mIntent.putExtra("FileName", file.getName());
                                    mIntent.putExtra("Mode", "OB");
                                    FileUploadService.enqueueWork(OtherBrandActivity.this, mIntent);

                                }
                            }
                        }
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
            common_class.showMsg(this, "Other Brand is empty");

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            common_class.CommonIntentwithFinish(Invoice_History.class);
            return true;
        }
        return false;
    }

    public void showBrandDialog(int position) {
        selectedPos = position;
        common_class.showCommonDialog(otherBrandList, 501, this);
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        try {
            if (selectedPos >= 0) {
                common_class.dismissCommonDialog(type);
                Getorder_Array_List.set(selectedPos, new QPS_Modal(myDataset.get(position).getId(), myDataset.get(position).getName(), "", 0, 0, 0, ""));
                otherBrandAdapter.notifyData(Getorder_Array_List);

            }
        } catch (Exception e) {
            Log.v("OtherBrandDialog: ", e.getMessage());
        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {

        if (apiDataResponse != null && !apiDataResponse.equals("")) {
            DatabaseHandler db = new DatabaseHandler(OtherBrandActivity.otherBrandActivity);
            String Compititor_List = String.valueOf(db.getMasterData(Constants.Competitor_List));
            Gson gson = new Gson();
            userTypeCompetitor = new TypeToken<ArrayList<Common_Model>>() {
            }.getType();
            otherBrandList = gson.fromJson(Compititor_List, userTypeCompetitor);
        }

    }

}