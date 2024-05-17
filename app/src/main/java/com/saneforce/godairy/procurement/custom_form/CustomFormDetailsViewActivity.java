package com.saneforce.godairy.procurement.custom_form;

import static android.view.View.GONE;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_GET_CUSTOM_FORM_FIELD_LIST;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SAVE_CUSTOM_FORM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.saneforce.godairy.Activity.AllowanceActivity;
import com.saneforce.godairy.Activity.Util.SelectionModel;
import com.saneforce.godairy.Activity_Hap.AllowancCapture;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.Interface.OnImagePickListener;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Model_Class.TimeUtils;
import com.saneforce.godairy.common.LocationFinder;
import com.saneforce.godairy.databinding.ActivityCustomFormDetailsViewBinding;
import com.saneforce.godairy.procurement.custom_form.model.DynamicField;
import com.saneforce.godairy.universal.Constant;
import com.saneforce.godairy.universal.PermissionUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomFormDetailsViewActivity extends AppCompatActivity {
    private ActivityCustomFormDetailsViewBinding binding;
    private final Context context = this;
    private ApiInterface apiInterface;
    private static final String TAG = "CustomFormDetails_";
    private String mModuleId = "";
    private ArrayList<DynamicField> master_list;
    private ArrayList<DynamicField> group_list;
    private ArrayList<DynamicField> store_list;
    private Boolean isDateShow = false;
    private DatePickerDialog datePickerDialog;
    private Boolean isDateclicked =false;
    private Boolean isFromDateEmpty =true;
    private Boolean isToDateEmpty =true;
    private Boolean isTimeShow = false;
    private Boolean isTimeclicked =false;
    private Boolean isFromTimeEmpty =true;
    private Boolean isToTimeEmpty =true;
    private Map<Integer, ImageView> imageViewMap;
    private Map<Integer, TextView> textViewMap;
    private String picturePathFinal1;
    String mEkey = "";
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    String sf_code , div_code , disign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomFormDetailsViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        master_list=new ArrayList<>();
        group_list=new ArrayList<>();
        store_list = new ArrayList<>();
        imageViewMap = new HashMap<>();
        textViewMap = new HashMap<>();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String moduleName = getIntent().getStringExtra("title");
        binding.moduleName.setText(moduleName);

        mModuleId = getIntent().getStringExtra("moduleId");

        if (mModuleId != null){
            if (mModuleId.isEmpty() | mModuleId.equals("")){
                return;
            }
            loadFieldData(mModuleId);
        }

        mEkey =Constant.SF_CODE + "-" + TimeUtils.getTimeStamp(TimeUtils.getCurrentTime(TimeUtils.FORMAT), TimeUtils.FORMAT);

        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sf_code = Shared_Common_Pref.Sf_Code;
        div_code = UserDetails.getString("Divcode", ""); // DesigNm
        disign = UserDetails.getString("DesigNm", "");

        onClick();
    }

    private void onClick() {
        binding.submit.setOnClickListener(view -> {
            if (validateInputs()) {
                saveDynamicData();
            }
        });

        binding.back.setOnClickListener(v -> finish());
    }

    private boolean validateInputs() {                      
        for (int i = 0; i < store_list.size(); i++) {
            if (store_list.get(i).getData() == null) {
                showError2();
                return false;
            }
            if ((store_list.get(i).getData().trim().isEmpty()) && (!isFromDateEmpty || !isToDateEmpty || store_list.get(i).getMandatory() == 1)) {
                showError2();
                return false;
            }
            if (isDateclicked && isDateShow && (isFromDateEmpty)) {
                showError2();
                return false;
            }
            if (isTimeclicked && isTimeShow && (isFromTimeEmpty || isToTimeEmpty)) {
                showError2();
                return false;
            }
        }
        return true;
    }

    private void showError2() {
        binding.errorContainer.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.errorContainer.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void saveDynamicData() {
        JsonArray commonDynamicJsonArray = new JsonArray();
        JsonObject commonDynamicData = new JsonObject();

        commonDynamicData.addProperty("sf_code", sf_code );
        commonDynamicData.addProperty("eKey", mEkey);

        JsonObject commonDynamicJsonObject = new JsonObject();
        commonDynamicJsonObject.add("common_dynamic_data", commonDynamicData);
        commonDynamicJsonArray.add(commonDynamicJsonObject);
        // Common dynamic json data start --> over
        // Dynamic data details start
        JsonArray dynamicDataDetailJsonArray = new JsonArray();
        try {
            for(int i=0; group_list.size()>i;i++) {
                JsonObject dynamicDataDetailsMainJsonObject = new JsonObject();

                int grpId=group_list.get(i).getFldGrpId();
                String fieldGroupTableNm=group_list.get(i).getGrpTableName();
                JsonArray jArray = new JsonArray();
                for (int j=0;store_list.size()>j;j++) {
                    JsonObject jGroup = new JsonObject();

                    DynamicField model = store_list.get(j);

                        try {
                            jGroup.addProperty("column_name", model.getColumn());
                            jGroup.addProperty("data_value", model.getData());
                             jGroup.addProperty("table_name", model.getGrpTableName());
                            jArray.add(jGroup);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                }
                dynamicDataDetailsMainJsonObject.addProperty("groupId",grpId);
                dynamicDataDetailsMainJsonObject.addProperty("grpTableName",fieldGroupTableNm);
                dynamicDataDetailsMainJsonObject.add("itemdetail",jArray);
                dynamicDataDetailJsonArray .add(dynamicDataDetailsMainJsonObject);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.add("dynamic_data_detail", dynamicDataDetailJsonArray);
        commonDynamicJsonArray.add(jsonObject3);

        if (Constant.isNetworkAvailable(getApplicationContext())) {
            ApiInterface request = ApiClient.getClient().create(ApiInterface.class);

            RequestBody mJsonArrayPart = RequestBody.create(MediaType.parse("multipart/form-data"), commonDynamicJsonArray.toString());

            Call<ResponseBody> call = request.save1JSONArray(
                    PROCUREMENT_SAVE_CUSTOM_FORM,
                    mJsonArrayPart,
                    div_code,
                    sf_code);

            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    try {
                        String res = response.body().string();
                        Log.e("res__", res);

                        if (res != null) {
                            JSONObject jsonObject = new JSONObject(res);

                            boolean isSuccess = jsonObject.getBoolean("success");
                            if (isSuccess) {
                                Toast.makeText(context, "Form Submit Success", Toast.LENGTH_LONG).show();
                                finish();
                            } else
                                Toast.makeText(getApplicationContext(), "Response : null", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException | IOException ex) {
                        ex.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Exception error " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failure : " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFieldData(String mModuleId) {
        Call<ResponseBody> call = apiInterface.getProcCustomFormFieldLists(PROCUREMENT_GET_CUSTOM_FORM_FIELD_LIST, String.valueOf(mModuleId));

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(GONE);
                    String mResponse;
                    try {
                        mResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(mResponse);

                        JSONArray groupJsonArray = new JSONArray(jsonObject.getString( "customGrp"));
                        JSONArray fieldJsonArray = new JSONArray(jsonObject.getString("customData"));

                        for (int i = 0; i<fieldJsonArray.length(); i++){
                            JSONObject jsonObject1 =fieldJsonArray.getJSONObject(i);
                            String typeMaster="CM,RM,SSM,SMM";

                            if (jsonObject1.getString("Fld_Src_Name") != null && !jsonObject1.getString("Fld_Src_Name").equalsIgnoreCase("null")&&jsonObject1.getString("Fld_Src_Field") != null && !jsonObject1.getString("Fld_Src_Field").equalsIgnoreCase("null")) {
                                if(!jsonObject1.getString("Fld_Src_Name").equals("")&&!jsonObject1.getString("Fld_Src_Field").equals("")&&typeMaster.contains(jsonObject1.getString("Fld_Type"))) {
                                    DynamicField dynamicField = new DynamicField(jsonObject1.getString("Fld_Src_Name"), jsonObject1.getString("Fld_Src_Field"));
                                    master_list.add(dynamicField);
                                }
                            }
                        }
                        if(master_list.size()>0) {
                            for (int k = 0; k < master_list.size(); k++) {
                             //   getDataFromMasterss(master_list.get(k).getFldSrcName(), master_list.get(k).getFldSrcFld(), k, groupJsonArray, fieldJsonArray);
                            }
                        }else{
                            loadData(groupJsonArray,fieldJsonArray);
                        }
                    } catch (IOException | JSONException e) {
                        // throw new RuntimeException(e);
                        showError();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                showError();
            }
        });
    }

    private void showError() {
        binding.shimmerLayout.setVisibility(GONE);
        binding.linearLayout.setVisibility(GONE);
        binding.nullError.setVisibility(View.VISIBLE);
        binding.message.setText("Something went wrong!");
    }

    private void loadData(JSONArray groupJsonArray, JSONArray fieldJsonArray) {
        try {
        if(groupJsonArray.length()>0) {
            for (int i = 0; i < groupJsonArray.length(); i++) {
                JSONObject jsonObject2 = groupJsonArray.getJSONObject(i);

                int grpId = jsonObject2.getInt("FieldGroupId");
                String grpName = jsonObject2.getString("FGroupName");
                String fgTableName = jsonObject2.getString("FGTableName");

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
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

                params.setMargins(Horizontal_marginInDp, vertical_marginInDp, Horizontal_marginInDp, vertical_marginInDp);
                TextView textView30 = new TextView(getApplicationContext());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView30.setTextColor(getResources().getColor(R.color.primaryDark, null));
                }
                Typeface typeface = ResourcesCompat.getFont(this, R.font.ubuntu_bold);
                textView30.setTypeface(typeface);

                textView30.setTextSize(18);
                textView30.setPadding((int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                .getDisplayMetrics()), 10, 0, 0);

                textView30.setText(grpName);
                binding.linearLayout.addView(textView30);
                textView30.setVisibility(View.GONE);

                DynamicField dataModels = new DynamicField();
                dataModels.setFldGrpId(grpId);
                dataModels.setFldGrpName(grpName);
                dataModels.setGrpTableName(fgTableName);
                group_list.add(dataModels);

                if (fieldJsonArray.length() > 0) {
                    binding.noRecordsLayout.setVisibility(View.GONE);
                    for (int j = 0; j < fieldJsonArray.length(); j++) {
                        JSONObject jsonObject = fieldJsonArray.getJSONObject(j);
                        int fieldGroupId = 0;
                        if (jsonObject.getString("FieldGroupId") != null && !jsonObject.getString("FieldGroupId").equalsIgnoreCase("null"))
                            fieldGroupId = jsonObject.getInt("FieldGroupId");

                        if (grpId == fieldGroupId) {
                            textView30.setVisibility(View.VISIBLE);
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
                            String tableName = jsonObject.getString("FGTableName");
                            binding.submit.setVisibility(View.VISIBLE);

                            //edittext for text/phone number
                            if ((Type_to_Add.contains("TA")) || (Type_to_Add.equals("N")) || (Type_to_Add.equals("NP")) || (Type_to_Add.equals("TAS"))||(Type_to_Add.equals("TAM"))) {

                                CardView card = new CardView(getApplicationContext());
                                TextView textView = new TextView(getApplicationContext());
                                EditText editText = new EditText(getApplicationContext());
                                card.setLayoutParams(params);
                                card.setRadius(10);
                                card.setCardBackgroundColor(Color.WHITE);
                                card.setMaxCardElevation(15);
                                card.setCardElevation(10);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setTextColor(getResources().getColor(R.color.grey_500, null));
                                }
                                Typeface typeface1 = ResourcesCompat.getFont(this, R.font.ubuntu);
                                textView.setTypeface(typeface1);
                                textView.setTextSize(15);
                                textView.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), 10, 0, 0);
                                String text = Heading_Label + "<font color='red'> *</font>";
                                if (mandate == 1) {
                                    textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                } else {
                                    textView.setText(Heading_Label);
                                }
                                editText.setHint("Type");
                                if (Type_to_Add.equals("N") || Type_to_Add.equals("NP")) {
                                    editText.setSingleLine(true);
                                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Text_Length)});
                                    editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                }
                                else if (Type_to_Add.equals("TAS")) {
                                    editText.setSingleLine(true);
                                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Text_Length)});
                                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                                }
                                else {
                                    editText.setSingleLine(false);
                                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Text_Length)});
                                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                }
                                editText.setTextSize(14);
                                editText.setTextColor(Color.BLACK);
                                editText.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()));
                                editText.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_common));
                                DynamicField dataModel = new DynamicField();
                                dataModel.setColumn(Column_Store);
                                dataModel.setFldGrpId(fieldGroupId);
                                dataModel.setFldType(Type_to_Add);
                                dataModel.setMandatory(mandate);
                                dataModel.setGrpTableName(tableName);
                                editText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {
                                        dataModel.setData(editable.toString());
                                    }
                                });
                                card.addView(editText);
                                store_list.add(dataModel);
                                binding.linearLayout.addView(textView);
                                binding.linearLayout.addView(card);

                            }
                            //textview for label and data required
                            else if (Type_to_Add.equals("L")) {
                                TextView textView = new TextView(getApplicationContext());
                                textView.setTextColor(Color.BLACK);
                                textView.setTextSize(15);
                                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                textView.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), 0, 0, 0);
                                String text = Heading_Label + "<font color='red'> *</font>";
                                if (mandate == 1) {
                                    textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                } else {
                                    textView.setText(Heading_Label);
                                }

                                DynamicField dataModel = new DynamicField();
                                dataModel.setColumn(Column_Store);
                                dataModel.setMandatory(mandate);
                                dataModel.setFldGrpId(fieldGroupId);
                                dataModel.setFldType(Type_to_Add);
                                dataModel.setGrpTableName(tableName);
                                store_list.add(dataModel);
                                binding.linearLayout.addView(textView);
                            }
                            //Date Picker for date range/date
                            else if (Type_to_Add.equals("DR")) {
                                TextView textView = new TextView(getApplicationContext());
                                isDateShow=true;
                                CardView card = new CardView(getApplicationContext());
                                LinearLayout layout = new LinearLayout(getApplicationContext());
                                TextView From_Date = new TextView(getApplicationContext());
                                TextView To_Date = new TextView(getApplicationContext());
                                TextView between = new TextView(getApplicationContext());
                                DynamicField dataModel = new DynamicField();
                                card.setLayoutParams(params);
                                card.setRadius(5);
                                card.setCardBackgroundColor(Color.WHITE);
                                card.setMaxCardElevation(15);
                                card.setCardElevation(10);
                                layout.setOrientation(LinearLayout.HORIZONTAL);
                                layout.setGravity(Gravity.CENTER);
                                layout.setWeightSum(3);
                                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        1
                                );
                                From_Date.setLayoutParams(param1);
                                between.setLayoutParams(param1);
                                To_Date.setLayoutParams(param1);

                                textView.setTextColor(Color.BLACK);
                                textView.setTextSize(15);
                                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                textView.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), 0, 0, 0);
                                String text = Heading_Label + "<font color='red'> *</font>";
                                if (mandate == 1) {
                                    textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                } else {
                                    textView.setText(Heading_Label);
                                }

                                From_Date.setHint("Select From Date");
                                From_Date.setGravity(Gravity.CENTER);
                                From_Date.setTextSize(14);
                                From_Date.setTextColor(Color.BLACK);
                                From_Date.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()));
                                From_Date.setBackgroundColor(Color.TRANSPARENT);
                                From_Date.setOnClickListener(view -> {
                                    int day, month, year;
                                    if (!From_Date.getText().toString().equals("")) {
                                        String[] dateArray = From_Date.getText().toString().split("/");
                                        day = Integer.parseInt(dateArray[0]);
                                        month = Integer.parseInt(dateArray[1]) - 1;
                                        year = Integer.parseInt(dateArray[2]);
                                    } else {
                                        Calendar c = Calendar.getInstance();

                                        day = c.get(Calendar.DAY_OF_MONTH);
                                        month = c.get(Calendar.MONTH);
                                        year = c.get(Calendar.YEAR);
                                    }

                                    datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            String _year = String.valueOf(year);
                                            String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                                            String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                            String _pickedDate = year + "-" + _month + "-" + _date;
                                            From_Date.setText((_date + "/" + _month + "/" + _year));
                                            isDateclicked = true;
                                            isFromDateEmpty = false;
                                            dataModel.setData(From_Date.getText().toString() + "to" + To_Date.getText().toString());
                                        }
                                    }, year, month, day);
                                    datePickerDialog.getDatePicker().getMaxDate();
                                    datePickerDialog.show();


                                });
                                To_Date.setHint("Select To Date");
                                To_Date.setGravity(Gravity.CENTER);
                                To_Date.setTextSize(14);
                                To_Date.setTextColor(Color.BLACK);
                                To_Date.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()));
                                To_Date.setBackgroundColor(Color.TRANSPARENT);
                                To_Date.setOnClickListener(view -> {
                                    int day, month, year;
                                    if (!To_Date.getText().toString().equals("")) {
                                        String[] dateArray = To_Date.getText().toString().split("/");
                                        day = Integer.parseInt(dateArray[0]);
                                        month = Integer.parseInt(dateArray[1]) - 1;
                                        year = Integer.parseInt(dateArray[2]);
                                    } else {
                                        Calendar c = Calendar.getInstance();
                                        day = c.get(Calendar.DAY_OF_MONTH);
                                        month = c.get(Calendar.MONTH);
                                        year = c.get(Calendar.YEAR);
                                    }

                                    datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            String _year = String.valueOf(year);
                                            String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                                            String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                            String _pickedDate = year + "-" + _month + "-" + _date;
                                            To_Date.setText((_date + "/" + _month + "/" + _year));
                                            isToDateEmpty = false;
                                            isDateclicked=true;
                                            dataModel.setData(From_Date.getText().toString() + "to" + To_Date.getText().toString());
                                        }
                                    }, year, month, day);
                                    datePickerDialog.getDatePicker().getMaxDate();
                                    datePickerDialog.show();
                                });

                                between.setText("to");
                                between.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                between.setGravity(Gravity.CENTER);
                                between.setTextSize(14);
                                between.setTextColor(Color.BLACK);
                                dataModel.setMandatory(mandate);
                                dataModel.setColumn(Column_Store);
                                dataModel.setFldGrpId(fieldGroupId);
                                dataModel.setFldType(Type_to_Add);
                                dataModel.setGrpTableName(tableName);
                                store_list.add(dataModel);
                                layout.addView(From_Date);
                                layout.addView(between);
                                layout.addView(To_Date);
                                // Put the TextView inside CardView
                                card.addView(layout);
                                binding.linearLayout.addView(textView);
                                binding.linearLayout.addView(card);
                            }
                            else if (Type_to_Add.equals("D")) {
                                TextView textView = new TextView(getApplicationContext());
                                CardView card = new CardView(getApplicationContext());
                                LinearLayout layout = new LinearLayout(getApplicationContext());
                                TextView From_Date = new TextView(getApplicationContext());
                                DynamicField dataModel = new DynamicField();
                                card.setLayoutParams(params);
                                card.setRadius(10);
                                card.setCardBackgroundColor(Color.WHITE);
                                card.setMaxCardElevation(15);
                                card.setCardElevation(10);
                                layout.setOrientation(LinearLayout.HORIZONTAL);
                                layout.setGravity(Gravity.CENTER);
                                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        1
                                );
                                From_Date.setLayoutParams(param1);
                                textView.setTextColor(Color.BLACK);
                                textView.setTextSize(15);
                                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                textView.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), 0, 0, 0);
                                String text = Heading_Label + "<font color='red'> *</font>";
                                if (mandate == 1) {
                                    textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                } else {
                                    textView.setText(Heading_Label);
                                }
                                From_Date.setHint("Select Date");
                                From_Date.setGravity(Gravity.START);
                                From_Date.setTextSize(14);
                                From_Date.setTextColor(Color.BLACK);
                                From_Date.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()));
                                From_Date.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_common));
                                From_Date.setOnClickListener(view -> {
                                    int day, month, year;
                                    if (!From_Date.getText().toString().equals("")) {
                                        String[] dateArray = From_Date.getText().toString().split("/");
                                        day = Integer.parseInt(dateArray[0]);
                                        month = Integer.parseInt(dateArray[1]) - 1;
                                        year = Integer.parseInt(dateArray[2]);
                                    } else {
                                        Calendar c = Calendar.getInstance();

                                        day = c.get(Calendar.DAY_OF_MONTH);
                                        month = c.get(Calendar.MONTH);
                                        year = c.get(Calendar.YEAR);
                                    }

                                    datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            String _year = String.valueOf(year);
                                            String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                                            String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                            String _pickedDate = year + "-" + _month + "-" + _date;

                                            From_Date.setText((_date + "/" + _month + "/" + _year));
                                            dataModel.setData(From_Date.getText().toString());
                                        }
                                    }, year, month, day);
                                    datePickerDialog.getDatePicker().getMaxDate();
                                    datePickerDialog.show();
                                });
                                dataModel.setMandatory(mandate);
                                dataModel.setColumn(Column_Store);
                                dataModel.setFldGrpId(fieldGroupId);
                                dataModel.setFldType(Type_to_Add);
                                dataModel.setGrpTableName(tableName);
                                store_list.add(dataModel);
                                layout.addView(From_Date);
                                card.addView(layout);
                                binding.linearLayout.addView(textView);
                                binding.linearLayout.addView(card);
                            }
                            //spinner for selection or options and data required
                            else if (Type_to_Add.equals("SSO") || Type_to_Add.equals("SSM")) {
                                CardView card = new CardView(getApplicationContext());
                                TextView textView = new TextView(getApplicationContext());
                                //need to change this to the length of getting value from the server
                                Spinner selection_Spinner = new Spinner(getApplicationContext());
                                DynamicField dataModel = new DynamicField();
                                card.setLayoutParams(params);
                                card.setRadius(5);
                                card.setCardBackgroundColor(Color.WHITE);
                                card.setMaxCardElevation(15);
                                card.setCardElevation(10);
                                textView.setTextColor(Color.BLACK);
                                textView.setTextSize(15);
                                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                textView.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), 0, 0, 0);
                                String text = Heading_Label + "<font color='red'> *</font>";
                                if (mandate == 1) {
                                    textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                } else {
                                    textView.setText(Heading_Label);
                                }
                                ArrayList<String> strList = new ArrayList<String>();
                                String[] srcSplit;
                                String ss = "";
                                List<SelectionModel> model=new ArrayList<>();
                                SelectionModel[] selectionModels = new SelectionModel[0];
                                if (Type_to_Add.equals("SSM")) {
                                    selectionModels = getListWithModel(Src_Name, Src_Field);
                                } else {
                                    srcSplit = Src_Field.split(",");
                                    strList = new ArrayList<String>(Arrays.asList(srcSplit));
                                    selectionModels = new SelectionModel[strList.size()+1];
                                    selectionModels[0] = new SelectionModel();
                                    selectionModels[0].setName("Select Data");
                                    selectionModels[0].setId("0");
                                    for (int k=0;k<strList.size();k++){
                                        int pos=k+1;
                                        selectionModels[pos] = new SelectionModel();
                                        selectionModels[pos].setName(strList.get(k).toString());
                                        selectionModels[pos].setId(""+(pos));

                                    }
                                }
                                SpinAdapter arrayAdapter = new SpinAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,selectionModels);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                selection_Spinner.setAdapter(arrayAdapter);
                                selection_Spinner.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()));
                                selection_Spinner.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_common));
                                dataModel.setMandatory(mandate);
                                dataModel.setColumn(Column_Store);
                                dataModel.setFldGrpId(fieldGroupId);
                                dataModel.setGrpTableName(tableName);
                                dataModel.setFldType(Type_to_Add);
                                selection_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        if (selection_Spinner.getSelectedItemPosition() > 0) {
                                            SelectionModel mod = arrayAdapter.getItem(i);
                                            if(Type_to_Add.equals("SSM")){
                                                dataModel.setData(mod.getId());
                                            }else {
                                                dataModel.setData(mod.getName());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                        dataModel.setData("");
                                    }
                                });
                                store_list.add(dataModel);
                                // Put the TextView inside CardView
                                card.addView(selection_Spinner);
                                binding.linearLayout.addView(textView);
                                binding.linearLayout.addView(card);
                            }
                            //Currency edittext for ND,currency
                            else if (Type_to_Add.equals("NC")) {
                                TextView textView = new TextView(getApplicationContext());

                                CardView card = new CardView(getApplicationContext());
                                LinearLayout layout = new LinearLayout(getApplicationContext());
                                TextView currency_Symbol = new TextView(getApplicationContext());
                                LinearLayout symbol_Layout = new LinearLayout(getApplicationContext());
                                EditText enter_Amount = new EditText(getApplicationContext());
                                card.setLayoutParams(params);
                                card.setRadius(5);
                                card.setCardBackgroundColor(Color.WHITE);
                                card.setMaxCardElevation(15);
                                card.setCardElevation(10);


                                layout.setOrientation(LinearLayout.HORIZONTAL);
                                layout.setGravity(Gravity.CENTER);
                                layout.setWeightSum(2);
                                LinearLayout.LayoutParams symbol_param = new LinearLayout.LayoutParams(
                                        0,
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        0.3f
                                );

                                LinearLayout.LayoutParams amount_param = new LinearLayout.LayoutParams(
                                        0,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        1.7f
                                );

                                symbol_Layout.setLayoutParams(symbol_param);
                                symbol_Layout.setGravity(Gravity.CENTER);
                                symbol_Layout.setBackgroundColor(Color.GRAY);
                                DynamicField dataModel = new DynamicField();
                                dataModel.setColumn(Column_Store);
                                dataModel.setMandatory(mandate);
                                dataModel.setFldGrpId(fieldGroupId);
                                dataModel.setFldType(Type_to_Add);
                                dataModel.setGrpTableName(tableName);
                                enter_Amount.setLayoutParams(amount_param);
                                enter_Amount.setTextSize(14);
                                enter_Amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                enter_Amount.setHint("Enter the amount");
                                enter_Amount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Text_Length)});
                                enter_Amount.setBackgroundColor(Color.TRANSPARENT);
                                enter_Amount.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()));
                                enter_Amount.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {
                                        dataModel.setData(currency_Symbol.getText().toString() + editable);
                                    }
                                });
                                currency_Symbol.setText(Symbol_Currency);
                                currency_Symbol.setTextColor(Color.WHITE);
                                currency_Symbol.setTextSize(16);
                                currency_Symbol.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                                textView.setTextColor(Color.BLACK);
                                textView.setTextSize(15);
                                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                textView.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), 0, 0, 0);

                                String text = Heading_Label + "<font color='red'> *</font>";
                                if (mandate == 1) {
                                    textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                } else {
                                    textView.setText(Heading_Label);
                                }

                                symbol_Layout.addView(currency_Symbol);
                                layout.addView(symbol_Layout);
                                layout.addView(enter_Amount);
                                store_list.add(dataModel);
                                // Put the TextView inside CardView
                                card.addView(layout);
                                binding.linearLayout.addView(textView);
                                binding.linearLayout.addView(card);
                            }
                            //DateTime Picker for DateTimeRange
                            else if (Type_to_Add.equals("TR")) {
                                isTimeShow = true;
                                TextView textView = new TextView(getApplicationContext());
                                CardView card = new CardView(getApplicationContext());
                                LinearLayout layout = new LinearLayout(getApplicationContext());
                                TextView From_Date = new TextView(getApplicationContext());
                                TextView To_Date = new TextView(getApplicationContext());
                                TextView between = new TextView(getApplicationContext());
                                DynamicField dataModel = new DynamicField();
                                card.setLayoutParams(params);
                                card.setRadius(5);
                                card.setCardBackgroundColor(Color.WHITE);
                                card.setMaxCardElevation(15);
                                card.setCardElevation(10);

                                layout.setOrientation(LinearLayout.HORIZONTAL);
                                layout.setGravity(Gravity.CENTER);
                                layout.setWeightSum(3);
                                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        1
                                );
                                From_Date.setLayoutParams(param1);
                                between.setLayoutParams(param1);
                                To_Date.setLayoutParams(param1);

                                textView.setTextColor(Color.BLACK);
                                textView.setTextSize(15);
                                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                textView.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), 0, 0, 0);

                                String text = Heading_Label + "<font color='red'> *</font>";
                                if (mandate == 1) {
                                    textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                } else {
                                    textView.setText(Heading_Label);
                                }
                                From_Date.setHint("Select From Time");
                                From_Date.setGravity(Gravity.CENTER);
                                From_Date.setTextSize(14);
                                From_Date.setTextColor(Color.BLACK);
                                From_Date.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()));
                                From_Date.setBackgroundColor(Color.TRANSPARENT);
                                From_Date.setOnClickListener(view -> {
                                    int hours =0, minutes=0;
                                    if (!From_Date.getText().toString().equals("")) {
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                                            Date date = sdf.parse(From_Date.getText().toString());
                                            hours = date.getHours();
                                            minutes = date.getMinutes();
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Calendar c = Calendar.getInstance();
                                        hours = c.get(Calendar.HOUR_OF_DAY);
                                        minutes =c.get(Calendar.MINUTE);
                                    }

                                    TimePickerDialog dpd = new TimePickerDialog(context, (timePicker, hourOfDay1, minute1) -> {
                                        Time time = new Time(hourOfDay1, minute1, 0);
                                        SimpleDateFormat simpleDateFormat = new
                                                SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                        String s = simpleDateFormat.format(time);
                                        From_Date.setText(( s));
                                        isTimeclicked = true;
                                        isFromTimeEmpty = false;
                                        dataModel.setData(From_Date.getText().toString() + "to" + To_Date.getText().toString());
                                    },hours,minutes , false);
                                    dpd.show();
                                });
                                To_Date.setHint("Select To Time");
                                To_Date.setGravity(Gravity.CENTER);
                                To_Date.setTextSize(14);
                                To_Date.setTextColor(Color.BLACK);
                                To_Date.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()));
                                To_Date.setBackgroundColor(Color.TRANSPARENT);
                                To_Date.setOnClickListener(view -> {
                                    int hours =0, minutes=0;
                                    if (!To_Date.getText().toString().equals("")) {
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                                            Date date = sdf.parse(To_Date.getText().toString());
                                            hours = date.getHours();
                                            minutes = date.getMinutes();
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Calendar c = Calendar.getInstance();
                                        hours = c.get(Calendar.HOUR_OF_DAY);
                                        minutes =c.get(Calendar.MINUTE);
                                    }
                                        TimePickerDialog dpd = new TimePickerDialog(context, (timePicker, hourOfDay1, minute1) -> {
                                        Time time = new Time(hourOfDay1, minute1, 0);
                                        SimpleDateFormat simpleDateFormat = new
                                                SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                        String s = simpleDateFormat.format(time);
                                        To_Date.setText(( s));
                                        isTimeclicked=true;
                                        isToTimeEmpty = false;
                                        dataModel.setData(From_Date.getText().toString() + "to" + To_Date.getText().toString());
                                    },hours,minutes , false);
                                    dpd.show();
                                });

                                between.setText("to");
                                between.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                between.setGravity(Gravity.CENTER);
                                between.setTextSize(14);
                                between.setTextColor(Color.BLACK);

                                dataModel.setColumn(Column_Store);
                                dataModel.setMandatory(mandate);
                                dataModel.setFldGrpId(fieldGroupId);
                                dataModel.setFldType(Type_to_Add);
                                dataModel.setGrpTableName(tableName);
                                store_list.add(dataModel);
                                layout.addView(From_Date);
                                layout.addView(between);
                                layout.addView(To_Date);
                                card.addView(layout);
                                binding.linearLayout.addView(textView);
                                binding.linearLayout.addView(card);
                            }
                            else if (Type_to_Add.equals("T")) {
                                TextView textView = new TextView(getApplicationContext());
                                CardView card = new CardView(getApplicationContext());
                                LinearLayout layout = new LinearLayout(getApplicationContext());
                                TextView From_Date = new TextView(getApplicationContext());
                                DynamicField dataModel = new DynamicField();

                                card.setLayoutParams(params);
                                card.setRadius(5);
                                card.setCardBackgroundColor(Color.WHITE);
                                card.setMaxCardElevation(15);
                                card.setCardElevation(10);
                                layout.setOrientation(LinearLayout.HORIZONTAL);
                                layout.setGravity(Gravity.CENTER);
                                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        1
                                );
                                From_Date.setLayoutParams(param1);
                                textView.setTextColor(Color.BLACK);
                                textView.setTextSize(15);
                                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                textView.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), 0, 0, 0);
                                String text = Heading_Label + "<font color='red'> *</font>";
                                if (mandate == 1) {
                                    textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                } else {
                                    textView.setText(Heading_Label);
                                }

                                From_Date.setHint("Select Time");
                                From_Date.setGravity(Gravity.START);
                                From_Date.setTextSize(14);
                                From_Date.setTextColor(Color.BLACK);
                                From_Date.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
                                                .getDisplayMetrics()), (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 15, getResources()
                                                .getDisplayMetrics()));
                                From_Date.setBackgroundColor(Color.TRANSPARENT);
                                From_Date.setOnClickListener(view -> {
                                    int hours =0, minutes=0;
                                    if (!From_Date.getText().toString().equals("")) {
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                                            Date date = sdf.parse(From_Date.getText().toString());
                                            hours = date.getHours();
                                            minutes = date.getMinutes();
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Calendar c = Calendar.getInstance();
                                        hours = c.get(Calendar.HOUR_OF_DAY);
                                        minutes =c.get(Calendar.MINUTE);
                                    }
                                        TimePickerDialog dpd = new TimePickerDialog(context, (timePicker, hourOfDay1, minute1) -> {
                                        Time time = new Time(hourOfDay1, minute1, 0);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                        String s = simpleDateFormat.format(time);
                                        From_Date.setText(( s));
                                        dataModel.setData(From_Date.getText().toString());
                                    },hours,minutes , false);
                                    dpd.show();
                                });

                                dataModel.setColumn(Column_Store);
                                dataModel.setMandatory(mandate);
                                dataModel.setFldGrpId(fieldGroupId);
                                dataModel.setFldType(Type_to_Add);
                                dataModel.setGrpTableName(tableName);
                                store_list.add(dataModel);
                                layout.addView(From_Date);
                                card.addView(layout);
                                binding.linearLayout.addView(textView);
                                binding.linearLayout.addView(card);
                            }
                            //radio group for options and data required
                            else if (Type_to_Add.equals("RO") || Type_to_Add.equals("RM")) {
                                ArrayList<String> all_Data = new ArrayList<>();
                                CardView card = new CardView(getApplicationContext());
                                TextView textView = new TextView(getApplicationContext());
                                RadioGroup radioGroup = new RadioGroup(getApplicationContext());
                                DynamicField dataModel = new DynamicField();
                                radioGroup.setLayoutParams(params);
                                card.setLayoutParams(params);
                                card.setRadius(5);
                                card.setCardBackgroundColor(Color.WHITE);
                                card.setMaxCardElevation(15);
                                card.setCardElevation(10);
                                textView.setTextColor(Color.BLACK);
                                textView.setTextSize(15);
                                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                textView.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), 0, 0, 0);
                                String text = Heading_Label + "<font color='red'> *</font>";
                                if (mandate == 1) {
                                    textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                } else {
                                    textView.setText(Heading_Label);
                                }
                                radioGroup.setOrientation(LinearLayout.VERTICAL);
                                ArrayList<String> strList = new ArrayList<String>();
                                String[] srcSplit;
                                List<SelectionModel> model=new ArrayList<>();
                                if (Type_to_Add.equals("RM")) {
                                    model = getListWithCode(Src_Name,Src_Field);
                                } else {
                                    srcSplit = Src_Field.split(",");
                                    strList = new ArrayList<String>(Arrays.asList(srcSplit));
                                    for (int k=0;k<strList.size();k++){
                                        SelectionModel selectionModel=new SelectionModel();
                                        selectionModel.setName(strList.get(k));
                                        selectionModel.setId(""+(k+1));
                                        model.add(selectionModel);
                                    }
                                }
                                RadioButton[] rb = new RadioButton[model.size()];
                                for (int a = 0; a < model.size(); a++) {
                                    rb[a] = new RadioButton(getApplicationContext());

                                    if(Type_to_Add.equalsIgnoreCase("RM")) {
                                        rb[a].setText(model.get(a).getName() + "-" + model.get(a).getId());
                                    }else{
                                        rb[a].setText(model.get(a).getName());
                                    }
                                    radioGroup.addView(rb[a]);
                                }
                                radioGroup.setOnCheckedChangeListener((radioGroup1, i12) -> {
                                    try {
                                        RadioButton radioButton = radioGroup1.findViewById(i12);
                                        if ((Type_to_Add.equals("RM"))) {
                                            String data=radioButton.getText().toString();
                                            String[] splitData = data.split("-");
                                            String code = splitData[1];
                                            dataModel.setData(code);
                                        } else {
                                            dataModel.setData(radioButton.getText().toString());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                                dataModel.setColumn(Column_Store);
                                dataModel.setMandatory(mandate);
                                dataModel.setFldGrpId(fieldGroupId);
                                dataModel.setFldType(Type_to_Add);
                                dataModel.setGrpTableName(tableName);
                                store_list.add(dataModel);
                                card.addView(radioGroup);
                                binding.linearLayout.addView(textView);
                                binding.linearLayout.addView(card);
                            }
                            //checkbox for CheckBox and data required
                            else if (Type_to_Add.equals("CO") || Type_to_Add.equals("CM") || Type_to_Add.equals("SMO") || Type_to_Add.equals("SMM")) {
                                CardView card = new CardView(getApplicationContext());
                                TextView textView = new TextView(getApplicationContext());
                                LinearLayout checkBocContainer = new LinearLayout(getApplicationContext());
                                checkBocContainer.setLayoutParams(params);
                                checkBocContainer.setOrientation(LinearLayout.VERTICAL);
                                card.setLayoutParams(params);
                                card.setRadius(5);
                                card.setCardBackgroundColor(Color.WHITE);
                                card.setMaxCardElevation(15);
                                card.setCardElevation(10);
                                textView.setTextColor(Color.BLACK);
                                textView.setTextSize(15);
                                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                textView.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), 0, 0, 0);
                                //textView.setLayoutParams(params);
                                String text = Heading_Label + "<font color='red'> *</font>";
                                if (mandate == 1) {
                                    textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                } else {
                                    textView.setText(Heading_Label);
                                }
                                DynamicField dataModel = new DynamicField();
                                ArrayList<String> strList = new ArrayList<String>();
                                String[] srcSplit;
                                List<SelectionModel> model=new ArrayList<>();
                                if (Type_to_Add.equals("CM") || Type_to_Add.equals("SMM")) {
                                    model=getListWithCode(Src_Name,Src_Field);
                                } else {
                                    srcSplit = Src_Field.split(",");
                                    strList = new ArrayList<String>(Arrays.asList(srcSplit));
                                    for (int k=0;k<strList.size();k++){
                                        SelectionModel selectionModel=new SelectionModel();
                                        selectionModel.setName(strList.get(k));
                                        selectionModel.setId(""+k+1);
                                        model.add(selectionModel);
                                    }
                                }

                                for (int b = 0; model.size() > b; b++) {
                                    CheckBox checkBox = new CheckBox(getApplicationContext());
                                    checkBox.setText(model.get(b).getName());
                                    checkBox.setId(b);
                                    checkBox.setTag("" + i);
                                    List<SelectionModel> finalModel = model;
                                    checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                                        int selectPos=compoundButton.getId();
                                        if (isChecked) {
                                            if(Type_to_Add.equals("CM") || Type_to_Add.equals("SMM")){
                                                if ((dataModel.getData() == null) || (dataModel.getData().equals(""))) {

                                                    dataModel.setData(finalModel.get(selectPos).getId());
                                                } else {
                                                    dataModel.setData(dataModel.getData() + "," +finalModel.get(selectPos).getId());
                                                }
                                            }else {
                                                if ((dataModel.getData() == null) || (dataModel.getData().equals(""))) {
                                                    dataModel.setData(compoundButton.getText().toString());
                                                } else {
                                                    dataModel.setData(dataModel.getData() + "," + compoundButton.getText().toString());
                                                }
                                            }

                                            for (int h = 0; h < store_list.size(); h++) {
                                                if (store_list.get(h).getTag() != null) {
                                                    String tag = compoundButton.getTag().toString();
                                                    if (tag.equals(store_list.get(h).getTag())) {
                                                        store_list.set(h, dataModel);
                                                    }
                                                }
                                            }
                                        } else {
                                            int index = store_list.indexOf(dataModel);
                                            String data = store_list.get(index).getData().toString();

                                            if(Type_to_Add.equals("CM") || Type_to_Add.equals("SMM")) {
                                                String name=finalModel.get(selectPos).getId();
                                                if (data.contains("," + name + ",")) {
                                                    String new_data = data.replace("," +name, "");
                                                    dataModel.setData(new_data);
                                                } else if (data.contains("," + name)) {
                                                    String new_data = data.replace("," + name, "");
                                                    dataModel.setData(new_data);
                                                } else if (data.contains(name + ",")) {
                                                    String new_data = data.replace(name + ",", "");
                                                    dataModel.setData(new_data);
                                                } else {
                                                    String new_data = data.replace(name, "");
                                                    dataModel.setData(new_data);
                                                }
                                            }else {
                                                if (data.contains("," + compoundButton.getText() + ",")) {
                                                    String new_data = data.replace("," + compoundButton.getText() , "");
                                                    dataModel.setData(new_data);
                                                } else if (data.contains("," + compoundButton.getText())) {
                                                    String new_data = data.replace("," + compoundButton.getText(), "");
                                                    dataModel.setData(new_data);
                                                } else if (data.contains(compoundButton.getText() + ",")) {
                                                    String new_data = data.replace(compoundButton.getText() + ",", "");
                                                    dataModel.setData(new_data);
                                                } else {
                                                    String new_data = data.replace(compoundButton.getText(), "");
                                                    dataModel.setData(new_data);
                                                }
                                            }
                                            store_list.set(index, dataModel);
                                        }
                                    });
                                    checkBocContainer.addView(checkBox);
                                }
                                dataModel.setMandatory(mandate);
                                dataModel.setTag(String.valueOf(i));
                                dataModel.setColumn(Column_Store);
                                dataModel.setFldGrpId(fieldGroupId);
                                dataModel.setGrpTableName(tableName);
                                dataModel.setFldType(Type_to_Add);
                                store_list.add(dataModel);
                                card.addView(checkBocContainer);
                                binding.linearLayout.addView(textView);
                                binding.linearLayout.addView(card);
                            }
                            else if (Type_to_Add.equals("FSC") || Type_to_Add.equals("FC") || Type_to_Add.equals("FS")) {
                                CardView card = new CardView(getApplicationContext());
                                TextView textView = new TextView(getApplicationContext());
                                LinearLayout layout = new LinearLayout(getApplicationContext());
                                layout.setOrientation(LinearLayout.HORIZONTAL);
                                layout.setGravity(Gravity.CENTER);
                                layout.setWeightSum(2);
                                ImageView imageview = new ImageView(getApplicationContext());
                                TextView tvFileName = new TextView(getApplicationContext());

                                LinearLayout.LayoutParams param1 = new LinearLayout
                                        .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                int vertical_margin1 = 15, Horizontal_margin1 = 15;
                                int vertical_marginInDp1 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, vertical_margin1, getResources()
                                                .getDisplayMetrics());
                                int Horizontal_marginInDp1 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, Horizontal_margin1, getResources()
                                                .getDisplayMetrics());
                                param1.setMargins(Horizontal_marginInDp1, vertical_marginInDp1, Horizontal_marginInDp1, vertical_marginInDp1);
                                // Add image path from drawable folder.
                                LinearLayout.LayoutParams text_param = new LinearLayout.LayoutParams(
                                        0,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        1.8f
                                );

                                LinearLayout.LayoutParams image_param = new LinearLayout.LayoutParams(
                                        0,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        0.2f
                                );

                                card.setRadius(5);
                                card.setCardBackgroundColor(Color.WHITE);
                                card.setMaxCardElevation(15);
                                card.setCardElevation(10);
                                card.setLayoutParams(text_param);

                                tvFileName.setTextColor(Color.BLACK);
                                tvFileName.setTextSize(15);
                                tvFileName.setGravity(Gravity.START);
                                tvFileName.setLayoutParams(param1);
                                card.addView(tvFileName);
                                layout.addView(card);
                                imageview.setImageResource(R.drawable.ic_upload_file);
                                imageview.setLayoutParams(image_param);
                                layout.addView(imageview);
                                layout.setLayoutParams(param1);
                                textView.setTextColor(Color.BLACK);
                                textView.setTextSize(15);
                                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                textView.setPadding((int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP, 18, getResources()
                                                .getDisplayMetrics()), 0, 0, 0);
                                String text = Heading_Label + "<font color='red'> *</font>";
                                if (mandate == 1) {
                                    textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                } else {
                                    textView.setText(Heading_Label);
                                }
                                String key = String.valueOf(i) + String.valueOf(i);
                                imageview.setOnClickListener(view -> {
                                    if (Constant.isNetworkAvailable(getApplicationContext()))
                                        if (Type_to_Add.equals("FC")) {
                                            captureFile(Integer.parseInt(key));
                                        } else if (Type_to_Add.equals("FS")) {
                                            imageChooser(Integer.parseInt(key));
                                        } else {
                                            checkPermission(999999, Integer.parseInt(key));
                                        }
                                    else
                                        Toast.makeText(getApplicationContext(), "Please check the internet connectivity", Toast.LENGTH_SHORT).show();
                                });
                                imageViewMap.put(Integer.parseInt(key), imageview);
                                textViewMap.put(Integer.parseInt(key), tvFileName);
                                DynamicField dataModel = new DynamicField();
                                dataModel.setColumn(Column_Store);
                                dataModel.setMandatory(mandate);
                                dataModel.setFldGrpId(fieldGroupId);
                                dataModel.setFldType(Type_to_Add);
                                dataModel.setGrpTableName(tableName);
                                dataModel.setImgKey(key);
                                store_list.add(dataModel);
                                binding.linearLayout.addView(textView);
                                binding.linearLayout.addView(layout);
                            }
                        }
                    }
                }
            }
        }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public SelectionModel[]  getListWithModel(String srcName,String SrcFieldName) {
        SelectionModel[] ss=null;
        String combinedNames = "";
        String listResponse = Constant.getInstance().getValue(srcName, "");
        String[] split = SrcFieldName.split(",");
        String field_code = split[0];
        String field_name  = split[1];
        if (listResponse != null && !listResponse.equals("")) {
            try {
                JSONArray jsonArrayy = new JSONArray(listResponse);
                ss=  new SelectionModel[jsonArrayy.length()+1];
                ss[0] = new SelectionModel();
                ss[0].setName("Select Data");
                ss[0].setId("0");

                for (int ii = 1; ii <=jsonArrayy.length(); ii++) {
                    ss[ii] = new SelectionModel();
                    JSONObject jsonObjectt = jsonArrayy.getJSONObject(ii-1);

                    if (jsonObjectt.has(field_name)) {
                        ss[ii].setName(jsonObjectt.getString(field_name));
                    }
                    if (jsonObjectt.has(field_code)) {
                        ss[ii].setId(jsonObjectt.getString(field_code));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ss;
    }

    public class SpinAdapter extends ArrayAdapter<SelectionModel> {
        private Context context;
        private SelectionModel[] values;

        public SpinAdapter(Context context, int textViewResourceId,
                           SelectionModel[] values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount(){
            return values.length;
        }

        @Override
        public SelectionModel getItem(int position){
            return values[position];
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(values[position].getName());
            return label;
        }
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(values[position].getName());
            return label;
        }
    }

    public List<SelectionModel>  getListWithCode(String srcName, String SrcFieldName) {
        List<SelectionModel> ss = new ArrayList<>();
        String listResponse = Constant.getInstance().getValue(srcName, "");
        String[] split = SrcFieldName.split(",");
        String field_code = split[0];
        String field_name  = split[1];
        if (listResponse != null && !listResponse.equals("")) {
            try {
                JSONArray jsonArrayy = new JSONArray(listResponse);
                for (int ii = 0; ii < jsonArrayy.length(); ii++) {
                    SelectionModel selectionModel=new SelectionModel();
                    JSONObject jsonObjectt = jsonArrayy.getJSONObject(ii);

                    if (jsonObjectt.has(field_name)) {
                        selectionModel.setName(jsonObjectt.getString(field_name));
                    }
                    if (jsonObjectt.has(field_code)) {
                        selectionModel.setId(jsonObjectt.getString(field_code));
                    }
                    ss.add(selectionModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ss;
    }

    public void captureFile(Integer reqCode) {
        AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
            @Override
            public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                if(imageViewMap.containsKey(reqCode)){
                    File file = new File(fullPath);

                    picturePathFinal1=file.getAbsolutePath();
                    ImageView imageViewToModify = imageViewMap.get(reqCode);
                    TextView textViewToModify=textViewMap.get(reqCode);
                    textViewToModify.setVisibility(View.VISIBLE);
                    if (imageViewToModify != null) {
                        // imageViewToModify.setImageURI(Uri.parse(picturePathFinal1));
                        //  imageViewToModify.setVisibility(View.GONE);
                        textViewToModify.setText(Constant.SF_CODE+"_"+file.getName());
                    }
                    for (int i=0;i<store_list.size();i++){
                        String reqId=reqCode==0?"00":String.valueOf(reqCode);
                        if(store_list.get(i).getImgKey()!=null&&store_list.get(i).getImgKey().equals(reqId)){
                            store_list.get(i).setData(Constant.SF_CODE+"_"+file.getName());
                        }
                    }
                    uploadImageFile(picturePathFinal1);
                }
            }
        });
        Intent intent = new Intent(getApplicationContext(), AllowancCapture.class);
        intent.putExtra("allowance", "TAClaim");
        startActivity(intent);
    }

    private void imageChooser(int dynamicId) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes = {"application/pdf", "image/*"};
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, dynamicId);
    }

    public void uploadImageFile(String filePath){
 /*
        File file = new File(filePath);
        if (filePath.contains(".png") || filePath.contains(".jpg") || filePath.contains(".jpeg")) {
            try {
                file = new Compressor(getApplicationContext()).compressToFile(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            file = new File(filePath);
        try {
            String folderNm=Constant.getInstance().getSetup(StringConstants.LOGO_NAME, "", dbController)+"_"+"CustomForm";
            boolean folderExists = getS3Client(getApplicationContext()).doesObjectExist("happic", folderNm+"/");


            if(folderExists) {
                fileUpload(folderNm,file);
            }else{
                byte[] emptyContent = new byte[0];
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(0);
                getS3Client(getApplicationContext()).putObject("happic", folderNm+"/", new ByteArrayInputStream(emptyContent), metadata);
                fileUpload(folderNm,file);
            }
        }
        catch (Exception e){

        }
        */
    }

    public void checkPermission(int position,int dynamicId){
        Dexter.withContext(getApplicationContext())
                .withPermissions(PermissionUtils.getCameraStoragePermissionList())
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report)
                    {
                        if(report.areAllPermissionsGranted()){
                            if(position==999999)
                                showDynamicImageChooserDialog(position,dynamicId);
                            else
                                captureFile(99999999);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).check();
    }

    private void showDynamicImageChooserDialog(int position,int dynamicId){
        // currentExpensePosition = position;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_upload_image);

        ImageButton ibClose = dialog.findViewById(R.id.ib_close);
        ImageButton ibCamera = dialog.findViewById(R.id.ib_camera);
        ImageButton ibGallery = dialog.findViewById(R.id.ib_gallery);

        ibClose.setOnClickListener(view -> dialog.dismiss());

        ibCamera.setOnClickListener(view -> {
            dialog.dismiss();
            // callCamera();
            captureFile(dynamicId);
        });

        ibGallery.setOnClickListener(view -> {
            dialog.dismiss();
            imageChooser(dynamicId);
        });
        dialog.show();
    }
}