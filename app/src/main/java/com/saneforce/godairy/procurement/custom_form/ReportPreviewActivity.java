package com.saneforce.godairy.procurement.custom_form;

import static android.view.View.GONE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityCustomFormReportPreviewBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;
import com.saneforce.godairy.procurement.custom_form.model.dynamicDataModel;
import com.saneforce.godairy.universal.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportPreviewActivity extends AppCompatActivity {
    private ActivityCustomFormReportPreviewBinding binding;
    private final Context context = this;
    String moduleId;
    String entryId;
    String sfCode;
    SharedPreferences UserDetails;
    public static final String MY_PREFERENCES = "MyPrefs";
    private String DIV_CODE;
    ArrayList<dynamicDataModel> master_list;
    ArrayList<dynamicDataModel> group_list;
    LinearLayout dynamic_data_layout;
    private Shared_Common_Pref shared_common_pref;
    String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomFormReportPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        master_list = new ArrayList<>();
        group_list=new ArrayList<>();

        dynamic_data_layout=findViewById(R.id.ll_dynamic_data);
        dynamic_data_layout.setVisibility(View.GONE);

        moduleId = getIntent().getStringExtra("moduleId");
        entryId = getIntent().getStringExtra("entryId");
        sfCode = getIntent().getStringExtra("sfCode");

        UserDetails = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        baseUrl = UserDetails.getString("base_url", "");

        getCustomDataDetails(moduleId,entryId);

        binding.back.setOnClickListener(v -> finish());

    }

    private void getCustomDataDetails(String moduleId, String entryId) {
        UserDetails = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        DIV_CODE = UserDetails.getString("Divcode", "");

        Map<String, Object> fieldParams = new HashMap<>();
        fieldParams.put("data", sfCode);
        fieldParams.put("moduleId", moduleId);
        fieldParams.put("divcode",DIV_CODE);
        fieldParams.put("entryId",entryId);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("axn", "get/customformdatadetail");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getCustomFormDataPreview(queryParams, fieldParams);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.shimmerLayout.setVisibility(GONE);
                try {
                    String res = response.body().string();

                    if(res!=null && !res.equals("")){

                        String cleanLine1 = res.replace("₹", "");

                        String cleanLine2 = cleanLine1.replace("?", "");

                        JSONObject jsonObject1 = new JSONObject(cleanLine2);
                        JSONArray array = new JSONArray(jsonObject1.getString("customGrp"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("Dynamic_View"));
                        for (int m = 0; m < jsonArray.length(); m++) {
                            JSONObject jsonObjecta = jsonArray.getJSONObject(m);
                            String typeMaster="CM,RM,SSM,SMM";

                            if (jsonObjecta.getString("Fld_Src_Name") != null && !jsonObjecta.getString("Fld_Src_Name").equalsIgnoreCase("null")&&jsonObjecta.getString("Fld_Src_Field") != null && !jsonObjecta.getString("Fld_Src_Field").equalsIgnoreCase("null")) {
                                if(!jsonObjecta.getString("Fld_Src_Name").equals("")&&!jsonObjecta.getString("Fld_Src_Field").equals("")&&typeMaster.contains(jsonObjecta.getString("Fld_Type"))) {
                                    dynamicDataModel model = new dynamicDataModel(jsonObjecta.getString("Fld_Src_Name"), jsonObjecta.getString("Fld_Src_Field"));
                                    master_list.add(model);
                                }
                            }
                        }

                        if(master_list.size()>0) {

                            for (int k = 0; k < master_list.size(); k++) {
                                getDataFromMasterss(master_list.get(k).getFldSrcName(), master_list.get(k).getFldSrcFld(), k, array, jsonArray);
                            }
                        }else {
                            loadData(array, jsonArray);
                        }
                        binding.tvNoData.setVisibility(View.GONE);
                    }else{
                        binding.tvNoData.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getDataFromMasterss(String fldSrcName, String fldSrcFld, int k, JSONArray array, JSONArray jsonArray) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("axn", "get/custom_form_masterData");
        queryParams.put("divisionCode", Constant.DIVISION_CODE);
        queryParams.put("State_Code", Constant.STATE_CODE);
        queryParams.put("masterName", fldSrcName);
        queryParams.put("fieldName", fldSrcFld);
        queryParams.put("sfCode", sfCode);

        ApiInterface apiClient = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiClient.getCustomFormMater(queryParams);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    ResponseBody res = response.body();
                    if (res != null && !res.equals("")) {
                        String responseBody = null;
                        try {
                            responseBody = res.string();
                            Constant.getInstance().setValue(responseBody, fldSrcName);

                            if (master_list.size() == k + 1) {
                                loadData(array, jsonArray);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadData(JSONArray array,JSONArray jsonArray2) {
        try {
            dynamic_data_layout.setVisibility(View.VISIBLE);
            if(array.length()>0){
                for (int jk = 0; jk < array.length(); jk++) {
                    JSONObject jsonObject2 = array.getJSONObject(jk);
                    int grpId = jsonObject2.getInt("FieldGroupId");
                    String grpName = jsonObject2.getString("FGroupName");
                    String fgTableName = jsonObject2.getString("FGTableName");
                    LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    int vertical_margin = 10, Horizontal_margin = 15;
                    int vertical_marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, vertical_margin, getResources()
                                    .getDisplayMetrics());
                    int Horizontal_marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, Horizontal_margin, getResources()
                                    .getDisplayMetrics());
                    paramss.setMargins(Horizontal_marginInDp, vertical_marginInDp, Horizontal_marginInDp, vertical_marginInDp);
                    TextView textView30 = new TextView(this);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView30.setTextColor(getResources().getColor(R.color.purple_500, null));
                    }
                    textView30.setTextSize(18);
                    textView30.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    textView30.setPadding((int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                    .getDisplayMetrics()), 0, 0, 0);

                    textView30.setText(grpName);
                    dynamic_data_layout.addView(textView30);
                    textView30.setVisibility(GONE);

                    dynamicDataModel dataModels = new dynamicDataModel();
                    dataModels.setFldGrpId(grpId);
                    dataModels.setFldGrpName(grpName);
                    dataModels.setGrpTableName(fgTableName);
                    group_list.add(dataModels);

                    if ((jsonArray2.length() > 0)) {
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            JSONObject jsonObject = jsonArray2.getJSONObject(i);

                            int fieldGroupId = 0;
                            if (jsonObject.getString("FieldGroupId") != null && !jsonObject.getString("FieldGroupId").equalsIgnoreCase("null"))
                                fieldGroupId = jsonObject.getInt("FieldGroupId");
                            if (grpId == fieldGroupId) {
                                String Heading_Label = jsonObject.getString("Field_Name");
                                String Type_to_Add = jsonObject.getString("Fld_Type");
                                String Module_Id = jsonObject.getString("ModuleId");
                                String Symbol_Currency = jsonObject.getString("Fld_Symbol");
                                String Column_Store = jsonObject.getString("Field_Col");
                                String Src_Name = jsonObject.getString("Fld_Src_Name");
                                String Src_Field = jsonObject.getString("Fld_Src_Field");
                                int Text_Length = jsonObject.getInt("Fld_Length");
                                int mandate = jsonObject.getInt("Mandate");
                                int flag = jsonObject.getInt("flag");
                                String data_value = jsonObject.getString("data_value");
                                String tableName = jsonObject.getString("FGTableName");
                                textView30.setVisibility(View.VISIBLE);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                int vertical_margin1 = 5, Horizontal_margin1 = 0;
                                int vertical_marginInDp1 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, vertical_margin1, getResources()
                                                .getDisplayMetrics());
                                int Horizontal_marginInDp1 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, Horizontal_margin1, getResources()
                                                .getDisplayMetrics());

                                params.setMargins(Horizontal_marginInDp1, vertical_marginInDp1, Horizontal_marginInDp1, vertical_marginInDp1);

                                if ((Type_to_Add.contains("TA")) || (Type_to_Add.equals("N")) || (Type_to_Add.equals("TAS")) || (Type_to_Add.equals("NP")) || (Type_to_Add.equals("TAM"))) {
                                    LinearLayout layout = new LinearLayout(getApplicationContext());
                                    TextView textView = new TextView(getApplicationContext());
                                    TextView textView1 = new TextView(getApplicationContext());
                                    layout.setOrientation(LinearLayout.HORIZONTAL);
                                    layout.setLayoutParams(params);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );

                                    textView.setLayoutParams(param);
                                    textView1.setLayoutParams(param);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.START);
                                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    textView.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    textView.setText(Heading_Label + ": ");
                                    textView1.setTextSize(15);
                                    textView1.setGravity(Gravity.START);
                                    textView1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                                    textView1.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    if (!data_value.equals("")) {
                                        textView1.setText("" + data_value);
                                    } else {
                                        textView1.setText("-");
                                    }

                                    layout.addView(textView);
                                    layout.addView(textView1);
                                    dynamic_data_layout.addView(layout);
                                }

                                else if (Type_to_Add.equals("DR")) {
                                    TextView textView = new TextView(getApplicationContext());
                                    TextView textView1 = new TextView(getApplicationContext());
                                    LinearLayout layout = new LinearLayout(getApplicationContext());

                                    layout.setOrientation(LinearLayout.HORIZONTAL);
                                    layout.setLayoutParams(params);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    textView.setLayoutParams(param);
                                    textView1.setLayoutParams(param);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.START);
                                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    textView.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    textView.setText(Heading_Label + ": ");
                                    textView1.setTextSize(15);
                                    textView1.setGravity(Gravity.START);
                                    textView1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                                    textView1.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    if (!data_value.equals("")) {
                                        textView1.setText(data_value);
                                    } else {
                                        textView1.setText("-");
                                    }
                                    layout.addView(textView);
                                    layout.addView(textView1);
                                    dynamic_data_layout.addView(layout);
                                }
                                else if (Type_to_Add.equals("D")) {
                                    TextView textView = new TextView(getApplicationContext());
                                    TextView textView1 = new TextView(getApplicationContext());
                                    LinearLayout layout = new LinearLayout(getApplicationContext());
                                    layout.setOrientation(LinearLayout.HORIZONTAL);
                                    layout.setLayoutParams(params);
                                    layout.setLayoutParams(params);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    textView.setLayoutParams(param);
                                    textView1.setLayoutParams(param);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.START);
                                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    textView.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    textView.setText(Heading_Label + ": ");
                                    textView1.setTextSize(15);
                                    textView1.setGravity(Gravity.START);
                                    textView1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                                    textView1.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    if (!data_value.equals("")) {
                                        textView1.setText(data_value);
                                    } else {
                                        textView1.setText("-");
                                    }

                                    layout.addView(textView);
                                    layout.addView(textView1);

                                    dynamic_data_layout.addView(layout);
                                }
                                //spinner for selection or options and data required
                                else if (Type_to_Add.equals("SSO") || Type_to_Add.equals("SSM")) {
                                    TextView textView = new TextView(getApplicationContext());
                                    TextView textView1 = new TextView(getApplicationContext());
                                    LinearLayout layout = new LinearLayout(getApplicationContext());

                                    layout.setOrientation(LinearLayout.HORIZONTAL);
                                    layout.setLayoutParams(params);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    textView.setLayoutParams(param);
                                    textView1.setLayoutParams(param);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.START);
                                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    textView.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    textView.setText(Heading_Label + ": ");
                                    textView1.setTextSize(15);
                                    textView1.setGravity(Gravity.START);
                                    textView1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                                    textView1.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);
                                    if(Type_to_Add.equals("SSM")){
                                        HashMap<String,String> data = getHasMapList(Src_Name, Src_Field);
                                        if (!data_value.equals("")) {
                                            textView1.setText(data.get(data_value));
                                        } else {
                                            textView1.setText("-");
                                        }
                                    }else {

                                        if (!data_value.equals("")) {
                                            textView1.setText(data_value);
                                        } else {
                                            textView1.setText("-");
                                        }
                                    }
                                    layout.addView(textView);
                                    layout.addView(textView1);
                                    dynamic_data_layout.addView(layout);
                                }
                                //Currency edittext for ND,currency
                                else if (Type_to_Add.equals("NC")) {
                                    TextView textView = new TextView(getApplicationContext());
                                    TextView textView1 = new TextView(getApplicationContext());
                                    LinearLayout layout = new LinearLayout(getApplicationContext());

                                    layout.setOrientation(LinearLayout.HORIZONTAL);

                                    layout.setLayoutParams(params);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    textView.setLayoutParams(param);
                                    textView1.setLayoutParams(param);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.START);
                                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    textView.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    textView.setText(Heading_Label + ": ");
                                    textView1.setTextSize(15);
                                    textView1.setGravity(Gravity.START);
                                    textView1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                                    textView1.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    if (!data_value.equals("")) {
                                        textView1.setText(data_value);
                                    } else {
                                        textView1.setText("-");
                                    }
                                    layout.addView(textView);
                                    layout.addView(textView1);
                                    dynamic_data_layout.addView(layout);
                                }
                                //DateTime Picker for DateTimeRange
                                else if (Type_to_Add.equals("TR")) {
                                    TextView textView = new TextView(getApplicationContext());
                                    TextView textView1 = new TextView(getApplicationContext());
                                    LinearLayout layout = new LinearLayout(getApplicationContext());

                                    layout.setOrientation(LinearLayout.HORIZONTAL);
                                    // layout.setGravity(Gravity.CENTER);
                                    // layout.setWeightSum(2);
                                    layout.setLayoutParams(params);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    textView.setLayoutParams(param);
                                    textView1.setLayoutParams(param);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.START);
                                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    textView.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    textView.setText(Heading_Label + ": ");
                                    textView1.setTextSize(15);
                                    textView1.setGravity(Gravity.START);
                                    textView1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                                    textView1.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    if (!data_value.equals("")) {
                                        textView1.setText(data_value);
                                    } else {
                                        textView1.setText("-");
                                    }
                                    layout.addView(textView);
                                    layout.addView(textView1);
                                    dynamic_data_layout.addView(layout);
                                }
                                else if (Type_to_Add.equals("T")) {

                                    TextView textView = new TextView(getApplicationContext());
                                    TextView textView1 = new TextView(getApplicationContext());
                                    LinearLayout layout = new LinearLayout(getApplicationContext());

                                    layout.setOrientation(LinearLayout.HORIZONTAL);

                                    layout.setLayoutParams(params);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    textView.setLayoutParams(param);
                                    textView1.setLayoutParams(param);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.START);
                                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    textView.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    textView.setText(Heading_Label + ": ");
                                    textView1.setTextSize(15);
                                    textView1.setGravity(Gravity.START);
                                    textView1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                                    textView1.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    if (!data_value.equals("")) {
                                        textView1.setText(data_value);
                                    } else {
                                        textView1.setText("-");
                                    }
                                    layout.addView(textView);
                                    layout.addView(textView1);
                                    dynamic_data_layout.addView(layout);
                                }
                                //radio group for options and data required
                                else if (Type_to_Add.equals("RO") || Type_to_Add.equals("RM")) {
                                    TextView textView = new TextView(getApplicationContext());
                                    TextView textView1 = new TextView(getApplicationContext());
                                    LinearLayout layout = new LinearLayout(getApplicationContext());

                                    layout.setOrientation(LinearLayout.HORIZONTAL);

                                    layout.setLayoutParams(params);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    textView.setLayoutParams(param);
                                    textView1.setLayoutParams(param);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.START);
                                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    textView.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    textView.setText(Heading_Label + ": ");
                                    textView1.setTextSize(15);
                                    textView1.setGravity(Gravity.START);
                                    textView1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                                    textView1.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    if(Type_to_Add.equals("RM")) {
                                        HashMap<String, String> data = getHasMapList(Src_Name, Src_Field);
                                        if (!data_value.equals("")) {
                                            textView1.setText(data.get(data_value));
                                        } else {
                                            textView1.setText("-");
                                        }
                                    }else {
                                        if (!data_value.equals("")) {
                                            textView1.setText(data_value);
                                        } else {
                                            textView1.setText("-");
                                        }
                                    }
                                    layout.addView(textView);
                                    layout.addView(textView1);
                                    dynamic_data_layout.addView(layout);
                                }
                                //checkbox for CheckBox and data required
                                else if (Type_to_Add.equals("CO") || Type_to_Add.equals("CM") || Type_to_Add.equals("SMO") || Type_to_Add.equals("SMM")) {
                                    TextView textView = new TextView(getApplicationContext());
                                    TextView textView1 = new TextView(getApplicationContext());
                                    LinearLayout layout = new LinearLayout(getApplicationContext());

                                    layout.setOrientation(LinearLayout.HORIZONTAL);

                                    layout.setLayoutParams(params);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    textView.setLayoutParams(param);
                                    textView1.setLayoutParams(param);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.START);
                                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    textView.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);

                                    textView.setText(Heading_Label + ": ");
                                    textView1.setTextSize(15);
                                    textView1.setGravity(Gravity.START);
                                    textView1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                                    textView1.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);
                                    if(Type_to_Add.equals("CM")||Type_to_Add.equals("SMM")){
                                        String[] srcSplit;
                                        ArrayList<String> strList =new ArrayList<>();
                                        String showData="";
                                        HashMap<String, String> data = getHasMapList(Src_Name, Src_Field);
                                        if (!data_value.equals("")) {
                                            srcSplit = data_value.split(",");
                                            strList = new ArrayList<String>(Arrays.asList(srcSplit));
                                            for (int j = 0; strList.size() > j; j++) {
                                                showData=showData+data.get(strList.get(j))+",";
                                            }
                                            textView1.setText(showData);
                                        } else {
                                            textView1.setText("-");
                                        }
                                    }else {
                                        if (!data_value.equals("")) {
                                            textView1.setText(data_value);
                                        } else {
                                            textView1.setText("-");
                                        }
                                    }
                                    layout.addView(textView);
                                    layout.addView(textView1);
                                    dynamic_data_layout.addView(layout);
                                } else if (Type_to_Add.equals("FSC") || Type_to_Add.equals("FC") || Type_to_Add.equals("FS")) {
                                    TextView textView = new TextView(getApplicationContext());
                                    ImageView imageView = new ImageView(getApplicationContext());
                                    CardView layout = new CardView(getApplicationContext());
                                    // layout.setOrientation(LinearLayout.HORIZONTAL);

                                    // for cardview
                                    CardView.LayoutParams paramsCardView = new CardView.LayoutParams(
                                            100,
                                            100);

                                    layout.setRadius(10);
                                    paramsCardView.setMargins(35, 0, 0, 0);

                                    layout.setLayoutParams(paramsCardView);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                                    // for image view
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                                    imageView.setLayoutParams(layoutParams);
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                    textView.setLayoutParams(param);
                                    imageView.setLayoutParams(layoutParams);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.START);
                                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    textView.setPadding((int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                    .getDisplayMetrics()), 0, 0, 0);
                                    imageView.setImageResource(R.drawable.image_placeholder);

                                    try{
                                        String[] names = data_value.split("\\.");
                                        String extension = names[names.length - 1];
                                        final File file = File.createTempFile(data_value, extension);
                                        com.saneforce.godairy.Common_Class.Util util = new com.saneforce.godairy.Common_Class.Util();
                                        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref((Activity) context);
                                        String companyCode = shared_common_pref.getvalue("company_code");

                                        if (companyCode.isEmpty()) {
                                            Toast.makeText(context, "Company code invalid", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        TransferUtility transferUtility = util.getTransferUtility(context);
                                        TransferObserver downloadObserver = transferUtility.download("godairy",companyCode + "/" + "Procurement" + "/" + data_value, file);
                                        downloadObserver.setTransferListener(new TransferListener() {
                                            @Override
                                            public void onStateChanged(int id, TransferState state) {
                                                if (TransferState.COMPLETED == state) {
                                                    Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                                                    Glide.with(context)
                                                            .load(bmp)
                                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                            .placeholder(R.drawable.error_image1)
                                                            .into(imageView);

                                                    imageView.setOnClickListener(v -> {
                                                        String imageUrl = file.getAbsolutePath();
                                                        Intent intent = new Intent(context, ImageViewActivity.class);
                                                        intent.putExtra("access_id", "2"); // 1 for url ( without access for URI storage image ) 2 for amazon s3
                                                        intent.putExtra("event_name", "Custom Form"); // This is url ( not URI )
                                                        intent.putExtra("s3", imageUrl); // url not URI
                                                        context.startActivity(intent);
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                                                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                                                int percentDone = (int) percentDonef;
                                            }

                                            @Override
                                            public void onError(int id, Exception ex) {
                                                Log.e("download__", ex.getMessage());
                                                imageView.setImageResource(R.drawable.error_image1);
                                            }
                                        });
                                    }
                                    catch (Exception e){
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        imageView.setImageResource(R.drawable.error_image1);
                                    }

                                    layout.addView(textView);
                                    layout.addView(imageView);
                                    dynamic_data_layout.addView(layout);
                                }
                            }
                        }
                    }

                    dynamic_data_layout.setVisibility(View.VISIBLE);
                    binding.tvNoData.setVisibility(GONE);
                }
            }else{
                binding.tvNoData.setVisibility(View.VISIBLE);
                dynamic_data_layout.setVisibility(GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String,String> getHasMapList(String srcName,String SrcFieldName) {
        List<String> ss = new ArrayList<>();
        String combinedNames = "";
        String listResponse = Constant.getInstance().getValue(srcName, "");
        String[] split = SrcFieldName.split(",");
        String field_name = split[1];
        String field_key=split[0];
        HashMap<String,String> mapData = new HashMap<>();
        if (listResponse != null && !listResponse.equals("")) {
            try {
                JSONArray jsonArrayy = new JSONArray(listResponse);
                for (int ii = 0; ii < jsonArrayy.length(); ii++) {
                    JSONObject jsonObjectt = jsonArrayy.getJSONObject(ii);
                    if (jsonObjectt.has(field_name)&&jsonObjectt.has(field_key)) {
                        mapData.put(jsonObjectt.getString(field_key),jsonObjectt.getString(field_name));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("ssfdf","data:"+mapData);
        return mapData;
    }
}