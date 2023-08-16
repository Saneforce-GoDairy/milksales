package com.saneforce.milksales.Activity_Hap;

import static com.saneforce.milksales.Common_Class.Common_Class.GetDateOnly;
import static com.saneforce.milksales.Common_Class.Common_Class.addquote;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity.AllowanceActivity;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Constants;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.Joint_Work_Listner;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.MVP.Main_Model;
import com.saneforce.milksales.Model_Class.ModeOfTravel;
import com.saneforce.milksales.Model_Class.Route_Master;
import com.saneforce.milksales.Model_Class.Tp_Dynamic_Modal;
import com.saneforce.milksales.R;
import com.saneforce.milksales.adapters.Joint_Work_Adapter;
import com.saneforce.milksales.common.DatabaseHandler;
import com.saneforce.milksales.databinding.ActivityMydayplanBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDayPlanActivity extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface {
    private ActivityMydayplanBinding binding;
    private final Context context = this;
    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(() -> startActivity(new Intent(getApplicationContext(), Dashboard.class)));

    List<Common_Model> worktypelist = new ArrayList<>();
    List<Common_Model> Route_Masterlist = new ArrayList<>();
    List<Common_Model> FRoute_Master = new ArrayList<>();
    LinearLayout worktypelayout, distributors_layout, route_layout;
    List<Common_Model> distributor_master = new ArrayList<>();
    List<Common_Model> getfieldforcehqlist = new ArrayList<>();
    List<Common_Model> ChillingCenter_List = new ArrayList<>();
    List<Common_Model> Shift_Typelist = new ArrayList<>();
    List<Common_Model> Jointworklistview = new ArrayList<>();
    List<Common_Model> Savejointwork = new ArrayList<>();
    ArrayList<Tp_Dynamic_Modal> dynamicarray = new ArrayList<>();

    ArrayList<Tp_Dynamic_Modal> Tp_dynamicArraylist = new ArrayList<>();
    Type userType;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    Common_Model Model_Pojo;
    List<ModeOfTravel> modelOfTravel;
    List<Common_Model> modelTravelType = new ArrayList<>();
    List<Common_Model> listOrderType = new ArrayList<>();
    Common_Model mCommon_model_spinner;
    Shared_Common_Pref sharedCommonPref;

    Joint_Work_Adapter adapter;
    MyDayPlanActivity.DynamicViewAdapter dynamicadapter;

    CustomListViewDialog customDialog;
    DatePickerDialog DatePickerDialog;
    TimePickerDialog timePickerDialog;

    String TpDate, worktype_id = "", Worktype_Button = "",  Fieldworkflag = "", shifttypeid ,  modeVal = "", StartedKM = "";
    String STRCode = "";
    String DriverNeed = "false";
    String DriverMode = "";
    String modeTypeVale = "";
    String mode = "";
    String FromKm = "";
    String ToKm = "";
    String modeId = "", toId = "", startEnd = "";

    public static final String MY_PREFERENCES = "MyPrefs";
    SharedPreferences UserDetails;

    boolean ExpNeed = false;
    DatabaseHandler databaseHandler;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMydayplanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        databaseHandler = new DatabaseHandler(context);
        UserDetails = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        initVariable();
        initOnClick();

        sharedCommonPref = new Shared_Common_Pref(this);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);

        binding.dynamicRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        TpDate = GetDateOnly();
        String[] TP_Dt = TpDate.split("-");
        binding.tourdate.setText(TP_Dt[2] + "/" + TP_Dt[1] + "/" + TP_Dt[0]);
        binding.myDayPlanDate.setText("(" + TP_Dt[2] + "/" + TP_Dt[1] + "/" + TP_Dt[0] + ")");
        binding.textTourPlanCount.setText("0");

        loadWorkTypes();
        Get_MydayPlan(GetDateOnly());
        binding.jointWorkRecycler.setLayoutManager(new LinearLayoutManager(this));

        getWorkTypes();

        distributors_layout.setVisibility(View.GONE);
        binding.chillingLayout.setVisibility(View.GONE);
        binding.hqLayout.setVisibility(View.GONE);
        binding.shiftTypeLayout.setVisibility(View.GONE);
        route_layout.setVisibility(View.GONE);
    }



    public void getWorkTypes() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SF", UserDetails.getString("Sfcode", ""));
            jsonObject.put("div", UserDetails.getString("Divcode", ""));
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            service.getDataArrayList("get/worktypes", jsonObject.toString()).enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                    databaseHandler.deleteMasterData("HAPWorkTypes");
                    databaseHandler.addMasterData("HAPWorkTypes", response.body());
                }

                @Override
                public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
                    Log.e("work_types", t.toString());
                }
            });
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initOnClick() {
        binding.getEmpIdBtn.setOnClickListener(this);
        binding.submitButton.setOnClickListener(this);
        worktypelayout.setOnClickListener(this);
        distributors_layout.setOnClickListener(this);
        route_layout.setOnClickListener(this);
        binding.shiftTypeLayout.setOnClickListener(this);
        binding.hqLayout.setOnClickListener(this);
        binding.cardToPlace.setOnClickListener(this);
        binding.chillingLayout.setOnClickListener(this);

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(v -> mOnBackPressedDispatcher.onBackPressed());
        binding.cardTravelMode.setOnClickListener(v -> {
            modelTravelType.clear();
            dynamicMode();
        });

        binding.cardDailyAllowance.setOnClickListener(v -> {
            listOrderType.clear();
            OrderType();
        });

        binding.submitButton1.setOnClickListener(v -> {
            if (vali()) {
                Savejointwork = Jointworklistview;

                String jointwork = "";
                String jointworkname = "";
                for (int ii = 0; ii < Savejointwork.size(); ii++) {
                    if (ii != 0) {
                        jointwork = jointwork.concat(",");
                        jointworkname = jointworkname.concat(",");
                    }

                    jointwork = jointwork.concat(Savejointwork.get(ii).getId());
                    jointworkname = jointworkname.concat(Savejointwork.get(ii).getName());
                }

                common_class.ProgressdialogShow(1, "Tour  plan");

                JSONArray jsonarr = new JSONArray();
                JSONObject jsonarrplan = new JSONObject();
                String remarks = binding.editRemarks1.getText().toString();
                try {
                    JSONObject jsonobj = new JSONObject();
                    jsonobj.put("worktype_code", addquote(worktype_id));
                    jsonobj.put("dcr_activity_date", addquote(TpDate));
                    jsonobj.put("worktype_name", addquote(binding.worktypeText.getText().toString()));
                    jsonobj.put("Ekey", Common_Class.GetEkey());
                    jsonobj.put("objective", addquote(remarks));
                    jsonobj.put("Flag", addquote(Fieldworkflag));
                    jsonobj.put("Button_Access", Worktype_Button);
                    jsonobj.put("MOT", addquote(binding.txtMode.getText().toString()));
                    jsonobj.put("DA_Type", addquote(binding.textDailyAllowance.getText().toString()));
                    jsonobj.put("Driver_Allow", addquote((binding.dailyDriverAllowance.isChecked()) ? "1" : "0"));
                    jsonobj.put("From_Place", addquote(binding.editBusForm.getText().toString()));
                    jsonobj.put("To_Place", addquote(binding.editToAddress.getText().toString()));
                    jsonobj.put("MOT_ID", addquote(modeId));
                    jsonobj.put("To_Place_ID", addquote(toId));
                    jsonobj.put("Mode_Travel_ID", addquote(startEnd));
                    jsonobj.put("worked_with", addquote(jointworkname));
                    jsonobj.put("jointWorkCode", addquote(jointwork));
                    JSONArray personarray = new JSONArray();
                    JSONObject ProductJson_Object;
                    for (int z = 0; z < dynamicarray.size(); z++) {
                        ProductJson_Object = new JSONObject();
                        try {
                            ProductJson_Object.put("Fld_ID", dynamicarray.get(z).getFld_ID());
                            ProductJson_Object.put("Fld_Name", dynamicarray.get(z).getFld_Name());
                            ProductJson_Object.put("Fld_Type", dynamicarray.get(z).getFld_Type());
                            ProductJson_Object.put("Fld_Src_Name", dynamicarray.get(z).getFld_Src_Name());
                            ProductJson_Object.put("Fld_Src_Field", dynamicarray.get(z).getFld_Src_Field());
                            ProductJson_Object.put("Fld_Length", dynamicarray.get(z).getFld_Length());
                            ProductJson_Object.put("Fld_Symbol", dynamicarray.get(z).getFld_Symbol());
                            ProductJson_Object.put("Fld_Mandatory", dynamicarray.get(z).getFld_Mandatory());
                            ProductJson_Object.put("Active_flag", dynamicarray.get(z).getActive_flag());
                            ProductJson_Object.put("Control_id", dynamicarray.get(z).getControl_id());
                            ProductJson_Object.put("Target_Form", dynamicarray.get(z).getTarget_Form());
                            ProductJson_Object.put("Filter_Text", dynamicarray.get(z).getFilter_Text());
                            ProductJson_Object.put("Filter_Value", dynamicarray.get(z).getFilter_Value());
                            ProductJson_Object.put("Field_Col", dynamicarray.get(z).getField_Col());
                            if (dynamicarray.get(z).getFld_Symbol().equals("D")) {
                                jsonobj.put("Worked_with_Code", dynamicarray.get(z).getFilter_Text());
                                jsonobj.put("Worked_with_Name", dynamicarray.get(z).getFilter_Value());
                            } else if (dynamicarray.get(z).getFld_Symbol().equals("R")) {
                                jsonobj.put("RouteCode", dynamicarray.get(z).getFilter_Text());
                                jsonobj.put("RouteName", dynamicarray.get(z).getFilter_Value());
                            }
                            personarray.put(ProductJson_Object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    jsonarrplan.put("Tp_Dayplan", jsonobj);
                    jsonarrplan.put("Tp_DynamicValues", personarray);
                    jsonarr.put(jsonarrplan);
                    Map<String, String> QueryString = new HashMap<>();
                    QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
                    QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
                    QueryString.put("State_Code", Shared_Common_Pref.StateCode);
                    QueryString.put("desig", "MGR");
                    QueryString.put("axn", "save/dayplandynamic");

                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<Object> Callto = apiInterface.Tb_Mydayplannew(QueryString, jsonarr.toString());
                    Callto.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                            common_class.ProgressdialogShow(2, "Tour  plan");
                            if (response.code() == 200 || response.code() == 201) {
                                if (worktype_id.equalsIgnoreCase("43")) {
                                    common_class.CommonIntentwithFinish(Dashboard.class);
                                    shared_common_pref.save("worktype", worktype_id);
                                } else if (ExpNeed) {
                                    Intent intent = new Intent(context, AllowanceActivity.class);
                                    intent.putExtra("My_Day_Plan", "One");
                                    startActivity(intent);
                                    finish();
                                } else {
                                      // Previous method submit success return Dashboard  home.
                                      // common_class.CommonIntentwithFinish(Dashboard.class);

                                       // New code as per design
                                       Intent intent = new Intent(context, CheckInActivity2.class);
                                       intent.putExtra("My_Day_Plan", "One");
                                       startActivity(intent);
                                       finish();
                                }
                                Toast.makeText(context, "Day Plan Submitted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            common_class.ProgressdialogShow(2, "Tour  plan");
                            Log.e("Reponse TAG", "onFailure : " + t);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initVariable() {
        worktypelayout = findViewById(R.id.worktypelayout);
        distributors_layout = findViewById(R.id.distributors_layout);
        route_layout = findViewById(R.id.route_layout);
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void setDataToRoute(ArrayList<Route_Master> noticeArrayList) {
    }

    @Override
    public void setDataToRouteObject(Object noticeArrayList, int position) {
        if (position == 0) {
            GetJsonData(new Gson().toJson(noticeArrayList), "0");
        } else if (position == 1) {
            GetJsonData(new Gson().toJson(noticeArrayList), "1");
        } else if (position == 2) {
            GetJsonData(new Gson().toJson(noticeArrayList), "2");
        } else if (position == 3) {
            GetJsonData(new Gson().toJson(noticeArrayList), "3");
        } else if (position == 4) {
            GetJsonData(new Gson().toJson(noticeArrayList), "4");
        } else if (position == 5) {
            GetJsonData(new Gson().toJson(noticeArrayList), "5");
        } else {
            GetJsonData(new Gson().toJson(noticeArrayList), "6");
            common_class.ProgressdialogShow(1, "Day plan");
            Get_MydayPlan(GetDateOnly());
        }
    }

    public void loadroute(String id) {
        if (Common_Class.isNullOrEmpty(String.valueOf(id))) {
            Toast.makeText(this, "Select Franchise", Toast.LENGTH_SHORT).show();
        }

        FRoute_Master.clear();
        for (int i = 0; i < Route_Masterlist.size(); i++) {
            if (Route_Masterlist.get(i).getFlag().toLowerCase().trim().replaceAll("\\s", "").contains(id.toLowerCase().trim().replaceAll("\\s", ""))) {
                FRoute_Master.add(new Common_Model(Route_Masterlist.get(i).getId(), Route_Masterlist.get(i).getName(), Route_Masterlist.get(i).getFlag()));
            }
        }
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == -1) {
            binding.worktypeText.setText(myDataset.get(position).getName());
            worktype_id = String.valueOf(myDataset.get(position).getId());
            Fieldworkflag = myDataset.get(position).getFlag();
            Worktype_Button = myDataset.get(position).getCheckouttime();
            ExpNeed = myDataset.get(position).getExpNeed();
            binding.jointWorkLayout.setVisibility(View.GONE);
            GetTp_Worktype_Fields(Worktype_Button);
        } else if (type == 7) {
            binding.editBusForm.setText(myDataset.get(position).getName());
            shifttypeid = myDataset.get(position).getId();
        } else if (type == 102) {
            binding.editToAddress.setText(myDataset.get(position).getName());
            toId = myDataset.get(position).getId();
        } else if (type == 100) {
            binding.txtMode.setText(myDataset.get(position).getName());
            DriverMode = myDataset.get(position).getCheckouttime();
            modeId = myDataset.get(position).getFlag();
            startEnd = myDataset.get(position).getId();
            if (startEnd.equals("0")) {
                mode = "11";
                FromKm = "";
                ToKm = "";
                StartedKM = "";
                binding.editBusForm.setText("");
                binding.editToAddress.setText("");
            } else {
                mode = "12";
                FromKm = "";
                ToKm = "";
                StartedKM = "";
                binding.editBusForm.setText("");
                binding.editToAddress.setText("");
            }
            if (DriverMode.equals("1")) {
                binding.linCheckDriver.setVisibility(View.VISIBLE);
            } else {
                binding.linCheckDriver.setVisibility(View.GONE);
            }
            DriverNeed = "";
            binding.dailyDriverAllowance.setChecked(false);
        } else if (type == 10) {
            binding.editToAddress.setText(myDataset.get(position).getName());
        } else if (type == 101) {
            String TrTyp = myDataset.get(position).getName();
            binding.textDailyAllowance.setText(TrTyp);
            if (TrTyp.equals("HQ")) {
                binding.busTo.setVisibility(View.GONE);
            } else {
                binding.busTo.setVisibility(View.VISIBLE);
            }
            binding.editToAddress.setText("");
        } else {
            dynamicarray.get(type).setFilter_Value(myDataset.get(position).getName());
            dynamicarray.get(type).setFilter_Text(myDataset.get(position).getId());
            dynamicadapter = new MyDayPlanActivity.DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext(), -1);
            binding.dynamicRecyclerview.setAdapter(dynamicadapter);
            dynamicadapter.notifyDataSetChanged();
            binding.dynamicRecyclerview.setItemViewCacheSize(dynamicarray.size());
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitbutton:
                if (vali()) {
                    Savejointwork = Jointworklistview;

                    String jointwork = "";
                    String jointworkname = "";
                    for (int ii = 0; ii < Savejointwork.size(); ii++) {
                        if (ii != 0) {
                            jointwork = jointwork.concat(",");
                            jointworkname = jointworkname.concat(",");
                        }

                        jointwork = jointwork.concat(Savejointwork.get(ii).getId());
                        jointworkname = jointworkname.concat(Savejointwork.get(ii).getName());
                    }

                    common_class.ProgressdialogShow(1, "Tour  plan");

                    JSONArray jsonarr = new JSONArray();
                    JSONObject jsonarrplan = new JSONObject();
                    String remarks = binding.editRemarks.getText().toString();
                    try {
                        JSONObject jsonobj = new JSONObject();
                        jsonobj.put("worktype_code", addquote(worktype_id));
                        jsonobj.put("dcr_activity_date", addquote(TpDate));
                        jsonobj.put("worktype_name", addquote(binding.worktypeText.getText().toString()));
                        jsonobj.put("Ekey", Common_Class.GetEkey());
                        jsonobj.put("objective", addquote(remarks));
                        jsonobj.put("Flag", addquote(Fieldworkflag));
                        jsonobj.put("Button_Access", Worktype_Button);
                        jsonobj.put("MOT", addquote(binding.txtMode.getText().toString()));
                        jsonobj.put("DA_Type", addquote(binding.textDailyAllowance.getText().toString()));
                        jsonobj.put("Driver_Allow", addquote((binding.dailyDriverAllowance.isChecked()) ? "1" : "0"));
                        jsonobj.put("From_Place", addquote(binding.editBusForm.getText().toString()));
                        jsonobj.put("To_Place", addquote(binding.editToAddress.getText().toString()));
                        jsonobj.put("MOT_ID", addquote(modeId));
                        jsonobj.put("To_Place_ID", addquote(toId));
                        jsonobj.put("Mode_Travel_ID", addquote(startEnd));
                        jsonobj.put("worked_with", addquote(jointworkname));
                        jsonobj.put("jointWorkCode", addquote(jointwork));
                        JSONArray personarray = new JSONArray();
                        JSONObject ProductJson_Object;
                        for (int z = 0; z < dynamicarray.size(); z++) {
                            ProductJson_Object = new JSONObject();
                            try {
                                ProductJson_Object.put("Fld_ID", dynamicarray.get(z).getFld_ID());
                                ProductJson_Object.put("Fld_Name", dynamicarray.get(z).getFld_Name());
                                ProductJson_Object.put("Fld_Type", dynamicarray.get(z).getFld_Type());
                                ProductJson_Object.put("Fld_Src_Name", dynamicarray.get(z).getFld_Src_Name());
                                ProductJson_Object.put("Fld_Src_Field", dynamicarray.get(z).getFld_Src_Field());
                                ProductJson_Object.put("Fld_Length", dynamicarray.get(z).getFld_Length());
                                ProductJson_Object.put("Fld_Symbol", dynamicarray.get(z).getFld_Symbol());
                                ProductJson_Object.put("Fld_Mandatory", dynamicarray.get(z).getFld_Mandatory());
                                ProductJson_Object.put("Active_flag", dynamicarray.get(z).getActive_flag());
                                ProductJson_Object.put("Control_id", dynamicarray.get(z).getControl_id());
                                ProductJson_Object.put("Target_Form", dynamicarray.get(z).getTarget_Form());
                                ProductJson_Object.put("Filter_Text", dynamicarray.get(z).getFilter_Text());
                                ProductJson_Object.put("Filter_Value", dynamicarray.get(z).getFilter_Value());
                                ProductJson_Object.put("Field_Col", dynamicarray.get(z).getField_Col());
                                if (dynamicarray.get(z).getFld_Symbol().equals("D")) {
                                    jsonobj.put("Worked_with_Code", dynamicarray.get(z).getFilter_Text());
                                    jsonobj.put("Worked_with_Name", dynamicarray.get(z).getFilter_Value());
                                } else if (dynamicarray.get(z).getFld_Symbol().equals("R")) {
                                    jsonobj.put("RouteCode", dynamicarray.get(z).getFilter_Text());
                                    jsonobj.put("RouteName", dynamicarray.get(z).getFilter_Value());
                                }
                                personarray.put(ProductJson_Object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        jsonarrplan.put("Tp_Dayplan", jsonobj);
                        jsonarrplan.put("Tp_DynamicValues", personarray);
                        jsonarr.put(jsonarrplan);
                        Map<String, String> QueryString = new HashMap<>();
                        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
                        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
                        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
                        QueryString.put("desig", "MGR");
                        QueryString.put("axn", "save/dayplandynamic");

                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<Object> Callto = apiInterface.Tb_Mydayplannew(QueryString, jsonarr.toString());
                        Callto.enqueue(new Callback<>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                common_class.ProgressdialogShow(2, "Tour  plan");
                                if (response.code() == 200 || response.code() == 201) {
                                    if (worktype_id.equalsIgnoreCase("43")) {
                                        common_class.CommonIntentwithFinish(Dashboard.class);
                                        shared_common_pref.save("worktype", worktype_id);

                                    } else if (ExpNeed) {
                                        Intent intent = new Intent(context, AllowanceActivity.class);
                                        intent.putExtra("My_Day_Plan", "One");
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        common_class.CommonIntentwithFinish(Dashboard.class);
                                    }
                                    Toast.makeText(context, "Day Plan Submitted Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                common_class.ProgressdialogShow(2, "Tour  plan");
                                Log.e("Reponse TAG", "onFailure : " + t);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.worktypelayout:
                customDialog = new CustomListViewDialog(this, worktypelist, -1);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;

            case R.id.chilling_layout:
                customDialog = new CustomListViewDialog(this, ChillingCenter_List, 6);
                Window chillwindow = customDialog.getWindow();
                chillwindow.setGravity(Gravity.CENTER);
                chillwindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;

            case R.id.get_emp_id_btn:
                if (binding.empIdEditText.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter the EMP_Id", Toast.LENGTH_SHORT).show();
                } else {
                    GetEmpList();
                }
                break;

            case R.id.card_to_place:
                customDialog = new CustomListViewDialog(this, getfieldforcehqlist, 102);
                Window chillwindowww = customDialog.getWindow();
                chillwindowww.setGravity(Gravity.CENTER);
                chillwindowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
        }
    }

    public void loadWorkTypes() {
        databaseHandler = new DatabaseHandler(this);
        try {
            JSONArray HAPLoca = databaseHandler.getMasterData("HAPWorkTypes");
            if (HAPLoca != null) {
                for (int li = 0; li < HAPLoca.length(); li++) {
                    JSONObject jItem = HAPLoca.getJSONObject(li);
                    String id = String.valueOf(jItem.optInt("id"));
                    String name = jItem.optString("name");
                    String flag = jItem.optString("FWFlg");
                    String ETabs = jItem.optString("ETabs");
                    String PlInv = jItem.optString("Place_Involved");
                    boolean tExpNeed = (PlInv.equalsIgnoreCase("Y"));
                    Common_Model item = new Common_Model(id, name, flag, ETabs, tExpNeed);
                    worktypelist.add(item);

                    binding.spinnerWorkType.setPrompt(name);

                    List<String> list = new ArrayList<>();
                    list.add("Select");
                    list.add(name);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerWorkType.setAdapter(adapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void OrderType() {
        ArrayList<String> travelTypeList = new ArrayList<>();
        travelTypeList.add("HQ");
        travelTypeList.add("EXQ");
        travelTypeList.add("Out Station");

        for (int i = 0; i < travelTypeList.size(); i++) {
            String id = String.valueOf(travelTypeList.get(i));
            String name = travelTypeList.get(i);
            mCommon_model_spinner = new Common_Model(id, name, "flag");
            listOrderType.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(this, listOrderType, 101);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }

    public boolean vali() {

        String workType = binding.spinnerWorkType.getSelectedItem().toString();

        if (workType.isEmpty() || workType.equals("Select")){
            toastMessage("Select work type");
            return false;
        }

        for (int i = 0; i < dynamicarray.size(); i++) {
            if (dynamicarray.get(i).getFilter_Value() != null && dynamicarray.get(i).getFilter_Value().equals("") && dynamicarray.get(i).getFld_Mandatory().equals("1")) {
                if (dynamicarray.get(i).getFld_Symbol().equals("JW")) {
                    if (Jointworklistview.size() == 0) {
                        toastMessage("\"Required Field\" + \"\\t\\t\" + dynamicarray.get(i).getFld_Name()");
                        return false;
                    }
                } else {
                    toastMessage("Required Field" + "\t\t" + dynamicarray.get(i).getFld_Name());
                    return false;
                }
            }
        }
        return true;
    }

    private void toastMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

    }

    private void GetJsonData(String jsonResponse, String type) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("FWFlg");
                String ETabs = jsonObject1.optString("ETabs");
                Model_Pojo = new Common_Model(id, name, flag);
                if (type.equals("0")) {
                    String PlInv = jsonObject1.optString("Place_Involved");
                    boolean tExpNeed = (PlInv.equalsIgnoreCase("Y"));
                    Model_Pojo = new Common_Model(id, name, flag, ETabs, tExpNeed);
                    worktypelist.add(Model_Pojo);
                } else if (type.equals("1")) {
                    distributor_master.add(Model_Pojo);
                } else if (type.equals("2")) {
                    Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                    FRoute_Master.add(Model_Pojo);
                    Route_Masterlist.add(Model_Pojo);
                } else if (type.equals("3")) {
                      /*  Model_Pojo = new Common_Model(name + "-" + jsonObject1.optString("desig"), id, false);
                        Jointworklistview.add(Model_Pojo);*/
                } else if (type.equals("4")) {
                    String sid = jsonObject1.optString(("id"));
                    String Odflag = jsonObject1.optString("ODFlag");
                    Model_Pojo = new Common_Model(sid, name, Odflag);
                    getfieldforcehqlist.add(Model_Pojo);
                } else if (type.equals("5")) {
                    Model_Pojo = new Common_Model(id, name, flag);
                    Shift_Typelist.add(Model_Pojo);
                } else {
                    Model_Pojo = new Common_Model(id, name, flag);
                    ChillingCenter_List.add(Model_Pojo);
                }

            }

            if (type.equals("3")) {
                binding.jointWorkRecycler.setAdapter(new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", new Joint_Work_Listner() {
                    @Override
                    public void onIntentClick(int po, boolean flag) {
                        Jointworklistview.get(po).setSelected(flag);
                        int jcount = 0;
                        for (int i = 0; Jointworklistview.size() > i; i++) {
                            if (Jointworklistview.get(i).isSelected()) {
                                jcount = jcount + 1;
                            }

                        }
                        binding.textTourPlanCount.setText(String.valueOf(jcount));
                    }
                }));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Get_MydayPlan(String tourDate) {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "Get/Tp_dayplan");
        QueryString.put("Sf_code", Shared_Common_Pref.Sf_Code);
        QueryString.put("Date", tourDate);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    common_class.ProgressdialogShow(2, "Tour Plan");
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    JSONArray jsoncc = jsonObject.getJSONArray("GettodayResult");

                    if (jsoncc.length() > 0) {
                        worktype_id = String.valueOf(jsoncc.getJSONObject(0).get("worktype_code"));
                        binding.editRemarks.setText(String.valueOf(jsoncc.getJSONObject(0).get("remarks")));
                        Fieldworkflag = String.valueOf(jsoncc.getJSONObject(0).get("Worktype_Flag"));
                        binding.worktypeText.setText(String.valueOf(jsoncc.getJSONObject(0).get("worktype_name")));
                        binding.spinnerWorkType.setPrompt(String.valueOf(jsoncc.getJSONObject(0).get("worktype_name")));
                        modeId = String.valueOf(jsoncc.getJSONObject(0).get("Mot_ID"));
                        STRCode = String.valueOf(jsoncc.getJSONObject(0).get("To_Place_ID"));
                        modeVal = String.valueOf(jsoncc.getJSONObject(0).get("Mode_Travel_Id"));
                        Worktype_Button = String.valueOf(jsoncc.getJSONObject(0).get("Button_Access"));
                        String Jointworkcode = String.valueOf(jsoncc.getJSONObject(0).get("JointworkCode"));
                        String JointWork_Name = String.valueOf(jsoncc.getJSONObject(0).get("JointWork_Name"));
                        String[] arrOfStr = Jointworkcode.split(",");
                        String[] arrOfname = JointWork_Name.split(",");

                        if (!Jointworkcode.equals("")) {
                            //Model_Pojo = new Common_Model(arrOfStr.get("Sf_Name").getAsString() + "-" + EmpDet.get("sf_Designation_Short_Name").getAsString(), EmpDet.get("Sf_Code").getAsString(), false);
                            for (int ik = 0; arrOfStr.length > ik; ik++) {
                                Model_Pojo = new Common_Model(arrOfname[ik], arrOfStr[ik], false);
                                Jointworklistview.add(Model_Pojo);
                            }

                            if (Jointworklistview.size() > 0) {
                                binding.jointWorkLayout.setVisibility(View.VISIBLE);
                                binding.textTourPlanCount.setText(String.valueOf(arrOfStr.length));
                                adapter = new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", new Joint_Work_Listner() {
                                    @Override
                                    public void onIntentClick(int position, boolean flag) {
                                        Jointworklistview.remove(position);
                                        binding.textTourPlanCount.setText(String.valueOf(Jointworklistview.size()));
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                binding.jointWorkRecycler.setAdapter(adapter);
                            }
                        }
                        if (modeVal.equals("0")) {
                            binding.txtMode.setText(modeTypeVale);
                            binding.txtMode.setText(String.valueOf(jsoncc.getJSONObject(0).get("MOT")));
                            binding.editBusForm.setText(String.valueOf(jsoncc.getJSONObject(0).get("From_Place")));
                            binding.editToAddress.setText(String.valueOf(jsoncc.getJSONObject(0).get("To_Place")));
                            binding.textDailyAllowance.setText(String.valueOf(jsoncc.getJSONObject(0).get("DA_Type")));
                        } else {
                            binding.txtMode.setText(modeTypeVale);
                            binding.txtMode.setText(String.valueOf(jsoncc.getJSONObject(0).get("MOT")));
                            binding.editBusForm.setText(String.valueOf(jsoncc.getJSONObject(0).get("From_Place")));
                            binding.editToAddress.setText(String.valueOf(jsoncc.getJSONObject(0).get("To_Place")));
                            if (jsoncc.getJSONObject(0).get("DA_Type").equals("HQ")) {
                                binding.busTo.setVisibility(View.GONE);
                            } else {
                                binding.busTo.setVisibility(View.VISIBLE);
                            }
                            binding.textDailyAllowance.setText(String.valueOf(jsoncc.getJSONObject(0).get("DA_Type")));
                        }
                        if (String.valueOf(jsoncc.getJSONObject(0).get("Driver_Allow")).equals("1")) {
                            binding.linCheckDriver.setVisibility(View.VISIBLE);
                            binding.dailyDriverAllowance.setChecked(true);
                        } else {
                            binding.linCheckDriver.setVisibility(View.GONE);
                            binding.dailyDriverAllowance.setChecked(false);
                        }

                        Tp_dynamicArraylist.clear();

                        JSONArray jsnArValue = jsonObject.getJSONArray("DynamicViews");

                        for (int i = 0; i < jsnArValue.length(); i++) {
                            JSONObject json_oo = jsnArValue.getJSONObject(i);

                            ArrayList<Common_Model> a_listt = new ArrayList<>();
                            ArrayList<Common_Model> a_list = new ArrayList<>();
                            if (json_oo.getJSONArray("inputs") != null) {
                                JSONArray jarray = json_oo.getJSONArray("inputs");
                                a_listt.clear();
                                String[] txtArray = json_oo.getString("Fld_Src_Field").split(",");
                                if (jarray != null && jarray.length() > 0) {
                                    for (int m = 0; m < jarray.length(); m++) {
                                        JSONObject jjss = jarray.getJSONObject(m);
                                        a_listt.add(new Common_Model(jjss.getString(txtArray[1]), jjss.getString(txtArray[0]), false));
                                    }
                                }
                            }
                            Tp_dynamicArraylist.add(new Tp_Dynamic_Modal(json_oo.getString("Fld_ID"), json_oo.getString("Fld_Name"), "", json_oo.getString("Fld_Type"), json_oo.getString("Fld_Src_Name"), json_oo.getString("Fld_Src_Field"), json_oo.getInt("Fld_Length"), json_oo.getString("Fld_Symbol"), json_oo.getString("Fld_Mandatory"), json_oo.getString("Active_flag"), json_oo.getString("Control_id"), json_oo.getString("Target_Form"), json_oo.getString("Filter_Text"), json_oo.getString("Filter_Value"), json_oo.getString("Field_Col"), a_listt));
                        }
                        dynamicadapter = new MyDayPlanActivity.DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext(), -1);
                        binding.dynamicRecyclerview.setAdapter(dynamicadapter);
                        dynamicadapter.notifyDataSetChanged();
                        binding.dynamicRecyclerview.setItemViewCacheSize(jsnArValue.length());

           /*             if (String.valueOf(jsoncc.getJSONObject(0).get("submit_status")).equals("3")) {
                            submitbutton.setVisibility(View.GONE);
                        }*/

                    } else {
                        Toast.makeText(context, "Tour Plan not Done", Toast.LENGTH_SHORT).show();
                    }
                    common_class.ProgressdialogShow(2, "Tour plan");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                common_class.ProgressdialogShow(2, "Tour Plan");
            }
        });
    }

    public void GetEmpList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> Callto = apiInterface.getDataArrayList("get/Emp_IdName",
                Shared_Common_Pref.Div_Code,
                Shared_Common_Pref.Sf_Code, binding.empIdEditText.getText().toString(), "", "DateTime", null);
        Callto.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray res = response.body();
                if (res.size() < 1) {
                    Toast.makeText(getApplicationContext(), "Emp Code  Not Found!", Toast.LENGTH_LONG).show();
                    return;
                }
                JsonObject EmpDet = res.get(0).getAsJsonObject();
                Common_Model Model_Pojo = new Common_Model(EmpDet.get("Sf_Name").getAsString() + "-" + EmpDet.get("sf_Designation_Short_Name").getAsString(), EmpDet.get("Sf_Code").getAsString(), false);

                boolean flag = CheckContains(Jointworklistview, EmpDet.get("Sf_Code").getAsString());
                if (flag) {
                    Toast.makeText(getApplicationContext(), "Already Added SF Name!", Toast.LENGTH_LONG).show();
                } else {
                    Jointworklistview.add(Model_Pojo);
                }
                binding.textTourPlanCount.setText(String.valueOf(Jointworklistview.size()));
                adapter = new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", new Joint_Work_Listner() {
                    @Override
                    public void onIntentClick(int position, boolean flag) {
                        Jointworklistview.remove(position);
                        binding.textTourPlanCount.setText(String.valueOf(Jointworklistview.size()));
                        adapter.notifyDataSetChanged();
                    }
                });
                binding.jointWorkRecycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("Error:", "Some Error" + t.getMessage());
            }
        });
    }

    private boolean CheckContains(List<Common_Model> jointworklistview, String Sf_Code) {
        boolean flag = false;
        for (int i = 0; jointworklistview.size() > i; i++) {
            if (jointworklistview.get(i).getId().equals(Sf_Code)) {
                flag = true;
            }
        }
        return flag;
    }

    public void dynamicMode() {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        String commonLeaveType = "{\"tableName\":\"getmodeoftravel\",\"coloumns\":\"[\\\"id\\\",\\\"name\\\",\\\"Leave_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetRouteObjects(QueryString, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                userType = new TypeToken<ArrayList<ModeOfTravel>>() {
                }.getType();
                Gson gson = new Gson();
                modelOfTravel = gson.fromJson(new Gson().toJson(response.body()), userType);

                for (int i = 0; i < modelOfTravel.size(); i++) {
                    String id = String.valueOf(modelOfTravel.get(i).getStEndNeed());
                    String name = modelOfTravel.get(i).getName();
                    String modeId = String.valueOf(modelOfTravel.get(i).getId());
                    String driverMode = String.valueOf(modelOfTravel.get(i).getDriverNeed());
                    Model_Pojo = new Common_Model(id, name, modeId, driverMode);
                    modelTravelType.add(Model_Pojo);
                }
                customDialog = new CustomListViewDialog(MyDayPlanActivity.this, modelTravelType, 100);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }

    public void GetTp_Worktype_Fields(String wflag) {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "get/worktypefields");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("Worktype_Code", wflag);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        Call<Object> call = service.GettpWorktypeFields(QueryString);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    if (response.isSuccessful()) {
                        Tp_dynamicArraylist.clear();
                        JSONArray jsnArValue = new JSONArray(new Gson().toJson(response.body()));

                        for (int i = 0; i < jsnArValue.length(); i++) {
                            JSONObject json_oo = jsnArValue.getJSONObject(i);

                            ArrayList<Common_Model> a_listt = new ArrayList<>();
                            ArrayList<Common_Model> a_list = new ArrayList<>();

                            if (json_oo.getJSONArray("inputs") != null) {
                                JSONArray jarray = json_oo.getJSONArray("inputs");
                                a_listt.clear();

                                String[] txtArray = json_oo.getString("Fld_Src_Field").split(",");
                                if (json_oo.getString("Fld_Symbol").equals("JW")) {
                                    binding.jointWorkLayout.setVisibility(View.VISIBLE);
                                }

                                if (jarray != null && jarray.length() > 0) {
                                    for (int m = 0; m < jarray.length(); m++) {
                                        JSONObject jjss = jarray.getJSONObject(m);
                                        a_listt.add(new Common_Model(jjss.getString(txtArray[1]), jjss.getString(txtArray[0]), false));
                                    }
                                }
                            }
                            Tp_dynamicArraylist.add(new Tp_Dynamic_Modal(json_oo.getString("Fld_ID"), json_oo.getString("Fld_Name"), "", json_oo.getString("Fld_Type"), json_oo.getString("Fld_Src_Name"), json_oo.getString("Fld_Src_Field"), json_oo.getInt("Fld_Length"), json_oo.getString("Fld_Symbol"), json_oo.getString("Fld_Mandatory"), json_oo.getString("Active_flag"), json_oo.getString("Control_id"), json_oo.getString("Target_Form"), json_oo.getString("Filter_Text"), json_oo.getString("Filter_Value"), json_oo.getString("Field_Col"), a_listt));
                        }
                        dynamicadapter = new MyDayPlanActivity.DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext(), -1);
                        binding.dynamicRecyclerview.setAdapter(dynamicadapter);
                        dynamicadapter.notifyDataSetChanged();
                        binding.dynamicRecyclerview.setItemViewCacheSize(jsnArValue.length());
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    public void openspinnerbox(int position, ArrayList<Common_Model> ArrayList) {
        customDialog = new CustomListViewDialog(MyDayPlanActivity.this, ArrayList, position);
        Window windowww = customDialog.getWindow();
        windowww.setGravity(Gravity.CENTER);
        windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }

    public void timePicker(int position, ArrayList<Common_Model> ArrayList) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(context, (timePicker, selectedHour, selectedMinute) -> {
            //eReminderTime.setText( selectedHour + ":" + selectedMinute);
            dynamicarray.get(position).setFilter_Value(selectedHour + ":" + selectedMinute);
            dynamicadapter = new DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext(), -1);
            binding.dynamicRecyclerview.setAdapter(dynamicadapter);
            dynamicadapter.notifyDataSetChanged();
            binding.dynamicRecyclerview.setItemViewCacheSize(dynamicarray.size());
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void datePicker(int position, ArrayList<Common_Model> ArrayList) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            int mnth = monthOfYear + 1;
            dynamicarray.get(position).setFilter_Value(dayOfMonth + "-" + mnth + "-" + year);
            dynamicadapter.notifyDataSetChanged();
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        DatePickerDialog.show();
    }

    public class DynamicViewAdapter extends RecyclerView.Adapter<MyDayPlanActivity.DynamicViewAdapter.MyViewHolder> {
        private final int rowLayout;
        private final Context context;
        private final int Categorycolor;

        public DynamicViewAdapter(ArrayList<Tp_Dynamic_Modal> array, int rowLayout, Context context, int Categorycolor) {
            dynamicarray = array;
            this.rowLayout = rowLayout;
            this.context = context;
            this.Categorycolor = Categorycolor;
        }

        @Override
        public MyDayPlanActivity.DynamicViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new MyDayPlanActivity.DynamicViewAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyDayPlanActivity.DynamicViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (!dynamicarray.get(position).getFld_Symbol().equals("JW")) {
                holder.tpcaptions.setVisibility(View.VISIBLE);
                holder.tpcaptions.setText(dynamicarray.get(position).getFld_Name());
                String titlecaptions = dynamicarray.get(position).getFld_Name();
                String SEttextvalues = dynamicarray.get(position).getFilter_Value();
                if (dynamicarray.get(position).getControl_id().equals("1") || dynamicarray.get(position).getControl_id().equals("3") || dynamicarray.get(position).getControl_id().equals("18") || dynamicarray.get(position).getControl_id().equals("24") || dynamicarray.get(position).getControl_id().equals("24")) {
                    holder.edittextid.setHint("" + titlecaptions);
                    holder.edittextid.setVisibility(View.VISIBLE);
                    holder.edittextid.setText(SEttextvalues);
                    if (dynamicarray.get(position).getControl_id().equals("1")) {
                        holder.edittextid.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else if (dynamicarray.get(position).getControl_id().equals("3")) {
                        holder.edittextid.setInputType(InputType.TYPE_CLASS_NUMBER);
                    } else if (dynamicarray.get(position).getControl_id().equals("18")) {
                        holder.edittextid.setInputType(InputType.TYPE_CLASS_PHONE);
                    } else if (dynamicarray.get(position).getControl_id().equals("24")) {
                        holder.edittextid.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    } else {
                        holder.edittextid.setInputType(InputType.TYPE_CLASS_TEXT);
                    }

                    int maxLength = dynamicarray.get(position).getFld_Length();
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                    holder.edittextid.setFilters(FilterArray);
                    holder.edittextid.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence charSequence, int start,
                                                  int before, int count) {
                            if (!charSequence.toString().equals("")) {
                                dynamicarray.get(position).setFilter_Value(charSequence.toString());
                            } else {
                                dynamicarray.get(position).setFilter_Value("");
                            }
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (!s.toString().equals("")) {
                                dynamicarray.get(position).setFilter_Value(s.toString());
                            } else {
                                dynamicarray.get(position).setFilter_Value("");
                            }
                        }
                    });
                } else if (dynamicarray.get(position).getControl_id().equals("7") || dynamicarray.get(position).getControl_id().equals("8") || dynamicarray.get(position).getControl_id().equals("11")) {
                    holder.Textspinnerview.setHint("Select The " + titlecaptions);
                    holder.Textspinnerview.setText(SEttextvalues);
                    holder.Textspinnerview.setVisibility(View.VISIBLE);
                    holder.edittextid.setVisibility(View.GONE);
                    if (titlecaptions.equals("Distributor")) {
                        shared_common_pref.save(Constants.Distributor_name, SEttextvalues);
                        shared_common_pref.save(Constants.Distributor_Id, dynamicarray.get(position).getFilter_Text());
                        shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, dynamicarray.get(position).getFilter_Text());
                        common_class.getDataFromApi(Constants.Retailer_OutletList, MyDayPlanActivity.this, false);
                    }
                } else if (dynamicarray.get(position).getControl_id().equals("10")) {
                    holder.radiogroup.setVisibility(View.VISIBLE);
                    holder.edittextid.setVisibility(View.GONE);
                    for (int ii = 0; ii < dynamicarray.get(position).getA_list().size(); ii++) {
                        RadioButton rbn = new RadioButton(getApplicationContext());
                        rbn.setId(ii);
                        rbn.setText("" + dynamicarray.get(position).getA_list().get(ii).getName());
                        if (dynamicarray.get(position).getA_list().get(ii).isSelected() || (dynamicarray.get(position).getFilter_Text() != null && dynamicarray.get(position).getFilter_Text() != "" && dynamicarray.get(position).getFilter_Text().equals(dynamicarray.get(position).getA_list().get(ii).getId()))) {
                            rbn.setChecked(true);
                        }
                        holder.radiogroup.addView(rbn);
                    }

                    holder.radiogroup.setOnCheckedChangeListener((group, checkedId) -> {
                        // This will get the radiobutton that has changed in its check state
                        RadioButton checkedRadioButton = group.findViewById(checkedId);
                        // This puts the value (true/false) into the variable
                        boolean isChecked = checkedRadioButton.isChecked();
                        // If the radiobutton that has changed in check state is now checked...
                        if (isChecked) {
                            dynamicarray.get(position).setFilter_Value(dynamicarray.get(position).getA_list().get(checkedId).getName());
                            dynamicarray.get(position).setFilter_Text(dynamicarray.get(position).getA_list().get(checkedId).getId());
                            dynamicarray.get(position).getA_list().get(checkedId).setSelected(true);
                        }
                    });
                }
                holder.Textspinnerview.setOnClickListener(v -> {
                    if (dynamicarray.get(position).getControl_id().equals("11")) {
                        timePicker(position, dynamicarray.get(position).getA_list());
                    } else if (dynamicarray.get(position).getControl_id().equals("8")) {
                        datePicker(position, dynamicarray.get(position).getA_list());
                    } else
                        openspinnerbox(position, dynamicarray.get(position).getA_list());
                });
            }
        }

        @Override
        public int getItemCount() {
            return dynamicarray.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tpcaptions, Textspinnerview;
            EditText edittextid;
            RadioGroup radiogroup;
            LinearLayout worktypelayout;

            public MyViewHolder(View view) {
                super(view);
                tpcaptions = view.findViewById(R.id.tpcaptions);
                Textspinnerview = view.findViewById(R.id.Textspinnerview);
                edittextid = view.findViewById(R.id.edittextid);
                worktypelayout = view.findViewById(R.id.worktypelayout);
                radiogroup = view.findViewById(R.id.radiogroup);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWorkTypes();
    }
}
