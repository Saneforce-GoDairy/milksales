package com.saneforce.godairy.Activity_Hap;

import static com.saneforce.godairy.Common_Class.Common_Class.GetDateOnly;
import static com.saneforce.godairy.Common_Class.Common_Class.addquote;
import static com.saneforce.godairy.common.AppConstants.GET_JOINT_WORK_LIST;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Activity.AllowanceActivity;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.MVP.Main_Model;
import com.saneforce.godairy.Model_Class.ModeOfTravel;
import com.saneforce.godairy.Model_Class.Route_Master;
import com.saneforce.godairy.Model_Class.Tp_Dynamic_Modal;
import com.saneforce.godairy.R;
import com.saneforce.godairy.adapters.Joint_Work_Adapter;
import com.saneforce.godairy.common.DatabaseHandler;
import com.saneforce.godairy.databinding.ActivityMydayplanBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mydayplan_Activity extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface {
    private ActivityMydayplanBinding binding;
    private final Context context = this;
    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    Shared_Common_Pref sharedCommonPref;
    private SharedPreferences UserDetails;
    List<Common_Model> worktypelist = new ArrayList<>();
    List<Common_Model> Route_Masterlist = new ArrayList<>();
    List<Common_Model> FRoute_Master = new ArrayList<>();
    List<Common_Model> distributor_master = new ArrayList<>();
    List<Common_Model> getfieldforcehqlist = new ArrayList<>();
    List<Common_Model> ChillingCenter_List = new ArrayList<>();
    List<Common_Model> Shift_Typelist = new ArrayList<>();
    List<Common_Model> Jointworklistview = new ArrayList<>();
    List<Common_Model> Savejointwork = new ArrayList<>();
    List<ModeOfTravel> modelOfTravel;
    List<Common_Model> modelTravelType = new ArrayList<>();
    List<Common_Model> listOrderType = new ArrayList<>();
    DatePickerDialog DatePickerDialog;
    TimePickerDialog timePickerDialog;
    CustomListViewDialog customDialog;
    Dialog jointWorkDialog;
    Gson gson;
    Type userType;
    Common_Class common_class;
    EditText edt_remarks, empidedittext;
    TextView TextMode, TextToAddress, dailyAllowance;
    TextView worktype_text, distributor_text, route_text, text_tour_plancount, hq_text, shift_type, chilling_text, Remarkscaption, tourdate;
    String TpDate, worktype_id, Worktype_Button = "", Fieldworkflag = "", shifttypeid,  modeId = "", toId = "", startEnd = "";
    String STRCode = "", DriverNeed = "false", DriverMode = "", modeTypeVale = "", mode = "", modeVal = "";
    String StartedKM = "", FromKm = "", ToKm = "";
    Button submitbutton, GetEmpId;
    ProgressBar progressbar;
    Common_Model Model_Pojo;
    LinearLayout BusTo, jointwork_layout, hqlayout, shiftypelayout, Procrumentlayout, chillinglayout, MdeTraval, DailyAll, frmPlace;
    LinearLayout worktypelayout, distributors_layout, route_layout, linCheckdriver, Dynamictpview;
    RecyclerView jointwork_recycler, dynamicrecyclerview;
    CardView ModeTravel, card_Toplace, CardDailyAllowance;
    CheckBox driverAllowance;
    Common_Model mCommon_model_spinner;
    Joint_Work_Adapter adapter;
    Mydayplan_Activity.DynamicViewAdapter dynamicadapter;
    DatabaseHandler db;
    ArrayList<Tp_Dynamic_Modal> Tp_dynamicArraylist = new ArrayList<>();
    ArrayList<Tp_Dynamic_Modal> dynamicarray = new ArrayList<>();
    ArrayList<String> jointWorkNameList;
    ArrayList<String> jointWorkIdList;
    ArrayList<String> jointWorkDesigList;
    boolean ExpNeed = false;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListId;
    RecyclerView recyclerView;
    String commaseparatedlistName;
    String commaseparatedlistId;
    JointWorkSelectedAdapter jointWorkSelectedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMydayplanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initVariable();
        initOnClick();

        arrayList = new ArrayList<>();
        arrayListId = new ArrayList<>();

        gson = new Gson();
        db = new DatabaseHandler(context);
        UserDetails = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        sharedCommonPref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);

        new Handler().postDelayed(() -> {
            loadWorkTypes();
            getWorkTypes();
            Log.e("my_day_plan", "Handler : started now");
        },500);

        loadExtraField();
        initJointWorkSelectedRecyclerView();
        loadWorkTypes();
        getWorkTypes();
        Get_MydayPlan(com.saneforce.godairy.Common_Class.Common_Class.GetDateOnly());
        loadWorkTypes();
        getWorkTypes();
        Get_MydayPlan(com.saneforce.godairy.Common_Class.Common_Class.GetDateOnly());
        initSetup();
    }

    @SuppressLint("SetTextI18n")
    private void initSetup() {
        TpDate = com.saneforce.godairy.Common_Class.Common_Class.GetDateOnly();
        String[] TP_Dt = TpDate.split("-");
        tourdate.setText(TP_Dt[2] + "/" + TP_Dt[1] + "/" + TP_Dt[0]);
        text_tour_plancount.setText("0");

        distributors_layout.setVisibility(View.GONE);
        chillinglayout.setVisibility(View.GONE);
        hqlayout.setVisibility(View.GONE);
        shiftypelayout.setVisibility(View.GONE);
        route_layout.setVisibility(View.GONE);

        TpDate = GetDateOnly();
        binding.tourdate.setText(TP_Dt[2] + "/" + TP_Dt[1] + "/" + TP_Dt[0]);
        binding.myDayPlanDate.setText("(" + TP_Dt[2] + "/" + TP_Dt[1] + "/" + TP_Dt[0] + ")");
        binding.textTourPlancount.setText("0");


        distributors_layout.setVisibility(View.GONE);
        binding.chillinglayout.setVisibility(View.GONE);
        binding.hqlayout.setVisibility(View.GONE);
        binding.shiftypelayout.setVisibility(View.GONE);
        route_layout.setVisibility(View.GONE);

        String mProfileUrl = sharedCommonPref.getvalue("mProfile");

        if (!com.saneforce.godairy.Common_Class.Common_Class.isNullOrEmpty(mProfileUrl)) {
            String[] image = mProfileUrl.split("/");
            if (image.length > 0 && image[(image.length - 1)].contains(".")) {
                Glide.with(this.context)
                        .load(mProfileUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.pImage);
            }
        }
    }

    private void initJointWorkDialogFresh() {
        RecyclerView recyclerView = jointWorkDialog.findViewById(R.id.primaryChannelList);
        recyclerView.setEnabled(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(20);
        JointWorkAdapater jointWorkAdapater = new JointWorkAdapater(Mydayplan_Activity.this, jointWorkIdList, jointWorkNameList, jointWorkDesigList);
        recyclerView.setAdapter(jointWorkAdapater);
    }

    private void initJointWorkSelectedRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_jw);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void loadExtraField() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("axn", GET_JOINT_WORK_LIST); // axn
        params.put("sfCode", "MGR0201"); // sf code
        Call<ResponseBody> call = apiInterface.getUniversalData(params);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String result;
                    try {
                        assert response.body() != null;
                        result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);

                        if (jsonObject.getBoolean("success")) {
                            JSONArray array = jsonObject.getJSONArray("response");
                            jointWorkNameList = new ArrayList<>();
                            jointWorkIdList = new ArrayList<>();
                            jointWorkDesigList = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                jointWorkIdList.add(jsonObject1.getString("id"));
                                jointWorkNameList.add(jsonObject1.getString("name"));
                                jointWorkDesigList.add(jsonObject1.getString("desig"));
                            }
                        } else {
                            Log.e("extra_field_", "response error");
                        }
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    public class JointWorkAdapater extends RecyclerView.Adapter<JointWorkAdapater.ViewHolder> {
        private final Context context;
        ArrayList<String> id;
        ArrayList<String> name;
        ArrayList<String> desig;

        public JointWorkAdapater(Context context, ArrayList<String> id, ArrayList<String> name , ArrayList<String> desig){
            this.context = context;
            this.id = id;
            this.name = name;
            this.desig = desig;
        }

        @NonNull
        @Override
        public JointWorkAdapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(context).inflate(R.layout.model_joint_work, parent, false);
            return new JointWorkAdapater.ViewHolder(view);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onBindViewHolder(@NonNull JointWorkAdapater.ViewHolder holder, int position) {
            holder.name.setText(name.get(position));

            Glide
                    .with(context)
                    .load(R.drawable.joint_work_bg)
                    .placeholder(R.color.grey_50)
                    .into(holder.channelImage);

            String mName = name.get(position);

            holder.nameLetter.setText(mName.substring(0,1).toUpperCase());

            holder.radioButton.setChecked(false);

            holder.mainLayout.setOnClickListener(view -> {
            });

            holder.radioButton.setOnClickListener(view -> {
                boolean isChecked = ((CheckBox)view).isChecked();

                if (isChecked){
                    arrayList.add(name.get(position));
                    arrayListId.add(id.get(position));
                }else {
                    if (arrayListId.contains(id.get(position))){
                        arrayList.remove(name.get(position));
                        arrayListId.remove(id.get(position));

                        JointWorkSelectedAdapter jointWorkSelectedAdapter = new JointWorkSelectedAdapter(Mydayplan_Activity.this, arrayList);
                        recyclerView.setAdapter(jointWorkSelectedAdapter);
                        jointWorkSelectedAdapter.notifyDataSetChanged();

                        StringBuilder str = new StringBuilder();

                        for (String eachstring : arrayList) {
                            str.append(eachstring).append(",");
                        }
                        commaseparatedlistName = str.toString();

                        StringBuilder str2 = new StringBuilder();

                        for (String eachstring : arrayListId) {
                            str2.append(eachstring).append(",");
                        }
                        commaseparatedlistId = str2.toString();

                        if (commaseparatedlistId.equals("")){
                            arrayList.clear();
                            arrayListId.clear();
                            binding.jointWorkNameTemp.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return name.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private final TextView name, nameLetter;
            private final CardView mainLayout;
            private final CheckBox radioButton;
            private final ImageView channelImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                mainLayout = itemView.findViewById(R.id.main);
                radioButton = itemView.findViewById(R.id.radio_button);
                channelImage = itemView.findViewById(R.id.channel_image);
                nameLetter = itemView.findViewById(R.id.name_letter);
            }
        }
    }
    public void getWorkTypes() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SF", UserDetails.getString("Sfcode", ""));
            jsonObject.put("div", UserDetails.getString("Divcode", ""));
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            service.getDataArrayList("get/worktypes", jsonObject.toString()).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                    db.deleteMasterData("HAPWorkTypes");
                    db.addMasterData("HAPWorkTypes", response.body());
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

    @SuppressLint("NotifyDataSetChanged")
    private void initOnClick() {
        binding.GetEmpId.setOnClickListener(this);
        binding.submitbutton.setOnClickListener(this);
        worktypelayout.setOnClickListener(this);
        distributors_layout.setOnClickListener(this);
        route_layout.setOnClickListener(this);
        binding.shiftypelayout.setOnClickListener(this);
        binding.hqlayout.setOnClickListener(this);
        binding.cardToplace.setOnClickListener(this);
        binding.chillinglayout.setOnClickListener(this);
        binding.jointWorkExtraFieldLayout.setOnClickListener(v -> jointWorkDialog.show());

        binding.spinnerWorkType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String workType = binding.spinnerWorkType.getSelectedItem().toString();
                if (workType.equals("Joint Work")){
                    binding.jointWorkExtraFieldLayout.setVisibility(View.VISIBLE);
                    initJointWorkDialog3();
                    jointWorkSelectedAdapter = new JointWorkSelectedAdapter(Mydayplan_Activity.this, arrayList);
                    recyclerView.setAdapter(jointWorkSelectedAdapter);
                    jointWorkSelectedAdapter.notifyDataSetChanged();
                }else {
                    arrayList.clear();
                    binding.jointWorkExtraFieldLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                    jsonobj.put("worktype_name", addquote(worktype_text.getText().toString()));
                    jsonobj.put("Ekey", Common_Class.GetEkey());
                    jsonobj.put("objective", addquote(remarks));
                    jsonobj.put("Flag", addquote(Fieldworkflag));
                    jsonobj.put("Button_Access", Worktype_Button);
                    jsonobj.put("MOT", addquote(TextMode.getText().toString()));
                    jsonobj.put("DA_Type", addquote(dailyAllowance.getText().toString()));
                    jsonobj.put("Driver_Allow", addquote((driverAllowance.isChecked()) ? "1" : "0"));
                    jsonobj.put("From_Place", addquote(binding.busFrom.getText().toString()));
                    jsonobj.put("To_Place", addquote(TextToAddress.getText().toString()));
                    jsonobj.put("MOT_ID", addquote(modeId));
                    jsonobj.put("To_Place_ID", addquote(toId));
                    jsonobj.put("Mode_Travel_ID", addquote(startEnd));
                    jsonobj.put("worked_with", addquote(commaseparatedlistName));
                    jsonobj.put("jointWorkCode", addquote(commaseparatedlistId));
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
                                    sharedCommonPref.save("worktype", worktype_id);
                                } else if (ExpNeed) {
                                    Intent intent = new Intent(Mydayplan_Activity.this, AllowanceActivity.class);
                                    intent.putExtra("My_Day_Plan", "One");
                                    startActivity(intent);
                                    finish();
                                } else {
                                    common_class.CommonIntentwithFinish(Dashboard.class);
                                }
                                Toast.makeText(Mydayplan_Activity.this, "Day Plan Submitted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                            common_class.ProgressdialogShow(2, "Tour  plan");
                            Log.e("Reponse TAG", "onFailure : " + t);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

       ModeTravel.setOnClickListener(v -> {
           modelTravelType.clear();
           dynamicMode();
       });

        binding.selectBtn.setOnClickListener(v -> jointWorkDialog.show());

        binding.clearBtn.setOnClickListener(v -> {
            arrayList.clear();
            arrayListId.clear();

            initJointWorkDialogFresh();
            jointWorkSelectedAdapter.notifyDataSetChanged();
            binding.jointWorkNameTemp.setVisibility(View.VISIBLE);
        });

        CardDailyAllowance.setOnClickListener(v -> {
            listOrderType.clear();
            OrderType();
        });
    }

    public class JointWorkSelectedAdapter extends RecyclerView.Adapter<JointWorkSelectedAdapter.ViewHolder> {
        ArrayList<String> name;
        Context context;

        public JointWorkSelectedAdapter(Context context, ArrayList<String> name){
            this.context = context;
            this.name = name;
        }

        @NonNull
        @Override
        public JointWorkSelectedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joint_wrk_list_item, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onBindViewHolder(@NonNull JointWorkSelectedAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.nameText.setText(name.get(position));

            holder.nameText.setOnClickListener(v -> jointWorkDialog.show());

            holder.deleteText.setOnClickListener(v -> {
                if (arrayList.contains(name.get(position))) {
                    arrayList.remove(name.get(position));
                    JointWorkSelectedAdapter jointWorkSelectedAdapter = new JointWorkSelectedAdapter(Mydayplan_Activity.this, arrayList);
                    recyclerView.setAdapter(jointWorkSelectedAdapter);
                    jointWorkSelectedAdapter.notifyDataSetChanged();

                    // for name
                    StringBuilder str = new StringBuilder();
                    for (String eachstring : arrayList) {
                        str.append(eachstring).append(",");
                    }
                    commaseparatedlistName = str.toString();

                    // for id
                    StringBuilder str2 = new StringBuilder();
                    for (String eachstring : arrayListId) {
                        str2.append(eachstring).append(",");
                    }
                    commaseparatedlistId = str2.toString();

                    // clear entries
                    if (commaseparatedlistId.equals("")) {
                        arrayList.clear();
                        arrayListId.clear();
                        binding.jointWorkNameTemp.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return name.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView nameText;
            ImageView deleteText;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                nameText = itemView.findViewById(R.id.joint_work_name);
                deleteText = itemView.findViewById(R.id.delete_);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initJointWorkDialog3() {
        jointWorkDialog = new Dialog(Mydayplan_Activity.this);
        jointWorkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        jointWorkDialog.setContentView(R.layout.model_dialog_joint_work);
        Objects.requireNonNull(jointWorkDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RelativeLayout ok = jointWorkDialog.findViewById(R.id.ok_btn);
        ok.setEnabled(true);

        ok.setOnClickListener(v -> {
            binding.jointWorkNameTemp.setVisibility(View.GONE);
            jointWorkDialog.dismiss();
            JointWorkSelectedAdapter jointWorkSelectedAdapter = new JointWorkSelectedAdapter(Mydayplan_Activity.this, arrayList);
            recyclerView.setAdapter(jointWorkSelectedAdapter);
            jointWorkSelectedAdapter.notifyDataSetChanged();

            // for name
            StringBuilder str = new StringBuilder();
            for (String eachstring : arrayList) {
                str.append(eachstring).append(",");
            }
            String removeLastExtraComma = str.toString();
            commaseparatedlistName = removeLastExtraComma.replaceFirst(".$",""); // removed last extra comma


            // for id
            StringBuilder str2 = new StringBuilder();
            for (String eachstring : arrayListId) {
                str2.append(eachstring).append(",");
            }
            String removeLastExtraCommaInId = str2.toString();
            commaseparatedlistId = removeLastExtraCommaInId.replaceFirst(".$",""); // removed last extra comma


            if (commaseparatedlistId.equals("")){
                arrayList.clear();
                arrayListId.clear();
                binding.jointWorkNameTemp.setVisibility(View.VISIBLE);
            }
        });

        RecyclerView recyclerView = jointWorkDialog.findViewById(R.id.primaryChannelList);
        recyclerView.setEnabled(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(20);
        JointWorkAdapater jointWorkAdapater = new JointWorkAdapater(Mydayplan_Activity.this, jointWorkIdList, jointWorkNameList, jointWorkDesigList);
        recyclerView.setAdapter(jointWorkAdapater);
    }

    private void initVariable() {
        progressbar = findViewById(R.id.progressbar);
        edt_remarks = findViewById(R.id.edt_remarks);
        Dynamictpview = findViewById(R.id.Dynamictpview);
        dynamicrecyclerview = findViewById(R.id.dynamicrecyclerview);
        tourdate = findViewById(R.id.tourdate);
        route_text = findViewById(R.id.route_text);
        worktypelayout = findViewById(R.id.worktypelayout);
        distributors_layout = findViewById(R.id.distributors_layout);
        ModeTravel = findViewById(R.id.card_travel_mode);
        card_Toplace = findViewById(R.id.card_Toplace);
        Remarkscaption = findViewById(R.id.remarkscaption);
        chillinglayout = findViewById(R.id.chillinglayout);
        chilling_text = findViewById(R.id.chilling_text);
        Procrumentlayout = findViewById(R.id.Procrumentlayout);
        hqlayout = findViewById(R.id.hqlayout);
        shiftypelayout = findViewById(R.id.shiftypelayout);
        hq_text = findViewById(R.id.hq_text);
        shift_type = findViewById(R.id.shift_type);
        route_layout = findViewById(R.id.route_layout);
        submitbutton = findViewById(R.id.submitbutton);
        worktype_text = findViewById(R.id.worktype_text);
        distributor_text = findViewById(R.id.distributor_text);
        text_tour_plancount = findViewById(R.id.text_tour_plancount);
        jointwork_layout = findViewById(R.id.jointwork_layout);
        jointwork_recycler = findViewById(R.id.jointwork_recycler);
        jointwork_layout = findViewById(R.id.jointwork_layout);
        jointwork_recycler = findViewById(R.id.jointwork_recycler);
        MdeTraval = findViewById(R.id.mode_of_travel);
        DailyAll = findViewById(R.id.lin_daily);
        frmPlace = findViewById(R.id.lin_from);
        GetEmpId = findViewById(R.id.GetEmpId);
        empidedittext = findViewById(R.id.empidedittext);
        BusTo = findViewById(R.id.lin_to_place);
        TextMode = findViewById(R.id.txt_mode);
        TextToAddress = findViewById(R.id.edt_to);
        CardDailyAllowance = findViewById(R.id.card_daily_allowance);
        dailyAllowance = findViewById(R.id.text_daily_allowance);
        driverAllowance = findViewById(R.id.da_driver_allowance);
        linCheckdriver = findViewById(R.id.lin_check_driver);
        worktypelayout = findViewById(R.id.worktypelayout);
        distributors_layout = findViewById(R.id.distributors_layout);
        route_layout = findViewById(R.id.route_layout);

        GetEmpId.setOnClickListener(this);
        submitbutton.setOnClickListener(this);
        worktypelayout.setOnClickListener(this);
        distributors_layout.setOnClickListener(this);
        route_layout.setOnClickListener(this);
        shiftypelayout.setOnClickListener(this);
        hqlayout.setOnClickListener(this);
        card_Toplace.setOnClickListener(this);
        chillinglayout.setOnClickListener(this);
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
            loadWorkTypes();
            getWorkTypes();
            Get_MydayPlan(com.saneforce.godairy.Common_Class.Common_Class.GetDateOnly());
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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == -1) {
            worktype_text.setText(myDataset.get(position).getName());
            worktype_id = String.valueOf(myDataset.get(position).getId());
            Fieldworkflag = myDataset.get(position).getFlag();
            Worktype_Button = myDataset.get(position).getCheckouttime();
            ExpNeed = myDataset.get(position).getExpNeed();
            jointwork_layout.setVisibility(View.GONE);
            GetTp_Worktype_Fields(Worktype_Button);
        } else if (type == 7) {
            binding.busFrom.setText(myDataset.get(position).getName());
            shifttypeid = myDataset.get(position).getId();
        } else if (type == 102) {
            TextToAddress.setText(myDataset.get(position).getName());
            toId = myDataset.get(position).getId();
        } else if (type == 100) {
            TextMode.setText(myDataset.get(position).getName());
            DriverMode = myDataset.get(position).getCheckouttime();
            modeId = myDataset.get(position).getFlag();
            startEnd = myDataset.get(position).getId();
            if (startEnd.equals("0")) {
                mode = "11";
                FromKm = "";
                ToKm = "";
                StartedKM = "";
                binding.busFrom.setText("");
                TextToAddress.setText("");
            } else {
                mode = "12";
                FromKm = "";
                ToKm = "";
                StartedKM = "";
                binding.busFrom.setText("");
                TextToAddress.setText("");
            }
            if (DriverMode.equals("1")) {
                linCheckdriver.setVisibility(View.VISIBLE);
            } else {
                linCheckdriver.setVisibility(View.GONE);
            }
            DriverNeed = "";
            driverAllowance.setChecked(false);

        } else if (type == 10) {
            TextToAddress.setText(myDataset.get(position).getName());
        } else if (type == 101) {
            String TrTyp = myDataset.get(position).getName();
            dailyAllowance.setText(TrTyp);
            if (TrTyp.equals("HQ")) {
                BusTo.setVisibility(View.GONE);
            } else {
                BusTo.setVisibility(View.VISIBLE);
            }
            TextToAddress.setText("");
        } else {
            dynamicarray.get(type).setFilter_Value(myDataset.get(position).getName());
            dynamicarray.get(type).setFilter_Text(myDataset.get(position).getId());
            dynamicrecyclerview.setLayoutManager(new LinearLayoutManager(this));
            dynamicadapter = new Mydayplan_Activity.DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext());
            dynamicrecyclerview.setAdapter(dynamicadapter);
            dynamicadapter.notifyDataSetChanged();
            dynamicrecyclerview.setItemViewCacheSize(dynamicarray.size());
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitbutton:

                break;
            case R.id.worktypelayout:
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, worktypelist, -1);
                Window window = customDialog.getWindow();
                assert window != null;
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
            case R.id.chillinglayout:
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, ChillingCenter_List, 6);
                Window chillwindow = customDialog.getWindow();
                assert chillwindow != null;
                chillwindow.setGravity(Gravity.CENTER);
                chillwindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;

            case R.id.GetEmpId:
                if (empidedittext.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter the EMP_Id", Toast.LENGTH_SHORT).show();
                } else {
                    GetEmpList();
                }
                break;

            case R.id.card_Toplace:
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, getfieldforcehqlist, 102);
                Window chillwindowww = customDialog.getWindow();
                assert chillwindowww != null;
                chillwindowww.setGravity(Gravity.CENTER);
                chillwindowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
        }
    }

    public void loadWorkTypes() {
        db = new DatabaseHandler(this);
        try {
            JSONArray HAPLoca = db.getMasterData("HAPWorkTypes");
            List<String> list = new ArrayList<>();
            list.add("Select");
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

                    worktype_id = id;
                    worktypelist.add(item);

                    binding.spinnerWorkType.setPrompt(name);
                    list.add(name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerWorkType.setAdapter(adapter);
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
        assert window != null;
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
                switch (type) {
                    case "0":
                        String PlInv = jsonObject1.optString("Place_Involved");
                        boolean tExpNeed = (PlInv.equalsIgnoreCase("Y"));
                        Model_Pojo = new Common_Model(id, name, flag, ETabs, tExpNeed);
                        worktypelist.add(Model_Pojo);
                        break;
                    case "1":
                        distributor_master.add(Model_Pojo);
                        break;
                    case "2":
                        Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                        FRoute_Master.add(Model_Pojo);
                        Route_Masterlist.add(Model_Pojo);
                        break;
                    case "3":
                        break;
                    case "4":
                        String sid = jsonObject1.optString(("id"));
                        String Odflag = jsonObject1.optString("ODFlag");
                        Model_Pojo = new Common_Model(sid, name, Odflag);
                        getfieldforcehqlist.add(Model_Pojo);
                        break;
                    case "5":
                        Model_Pojo = new Common_Model(id, name, flag);
                        Shift_Typelist.add(Model_Pojo);
                        break;
                    default:
                        Model_Pojo = new Common_Model(id, name, flag);
                        ChillingCenter_List.add(Model_Pojo);
                        break;
                }
            }

            if (type.equals("3")) {
                jointwork_recycler.setLayoutManager(new LinearLayoutManager(this));
                jointwork_recycler.setLayoutManager(new LinearLayoutManager(this));
                jointwork_recycler.setAdapter(new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", (po, flag) -> {
                    Jointworklistview.get(po).setSelected(flag);
                    int jcount = 0;
                    for (int i = 0; Jointworklistview.size() > i; i++) {
                        if (Jointworklistview.get(i).isSelected()) {
                            jcount = jcount + 1;
                        }
                    }
                    text_tour_plancount.setText(String.valueOf(jcount));
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
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
        mCall.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                try {
                    common_class.ProgressdialogShow(2, "Tour Plan");
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    JSONArray jsoncc = jsonObject.getJSONArray("GettodayResult");
                    if (jsoncc.length() > 0) {
                        worktype_id = String.valueOf(jsoncc.getJSONObject(0).get("worktype_code"));
                        edt_remarks.setText(String.valueOf(jsoncc.getJSONObject(0).get("remarks")));
                        Fieldworkflag = String.valueOf(jsoncc.getJSONObject(0).get("Worktype_Flag"));
                        worktype_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("worktype_name")));
                        modeId = String.valueOf(jsoncc.getJSONObject(0).get("Mot_ID"));
                        STRCode = String.valueOf(jsoncc.getJSONObject(0).get("To_Place_ID"));
                        modeVal = String.valueOf(jsoncc.getJSONObject(0).get("Mode_Travel_Id"));
                        Worktype_Button = String.valueOf(jsoncc.getJSONObject(0).get("Button_Access"));
                        String Jointworkcode = String.valueOf(jsoncc.getJSONObject(0).get("JointworkCode"));
                        String JointWork_Name = String.valueOf(jsoncc.getJSONObject(0).get("JointWork_Name"));
                        String[] arrOfStr = Jointworkcode.split(",");
                        String[] arrOfname = JointWork_Name.split(",");

                        if (!Jointworkcode.equals("")) {
                            for (int ik = 0; arrOfStr.length > ik; ik++) {
                                Model_Pojo = new Common_Model(arrOfname[ik], arrOfStr[ik], false);
                                Jointworklistview.add(Model_Pojo);
                            }

                            if (Jointworklistview.size() > 0) {
                                jointwork_layout.setVisibility(View.VISIBLE);
                                text_tour_plancount.setText(String.valueOf(arrOfStr.length));
                                adapter = new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", (position, flag) -> {
                                    Jointworklistview.remove(position);
                                    text_tour_plancount.setText(String.valueOf(Jointworklistview.size()));
                                    adapter.notifyDataSetChanged();
                                });
                                jointwork_recycler.setAdapter(adapter);
                            }
                        }
                        if (modeVal.equals("0")) {
                            TextMode.setText(modeTypeVale);
                            TextMode.setText(String.valueOf(jsoncc.getJSONObject(0).get("MOT")));
                            binding.busFrom.setText(String.valueOf(jsoncc.getJSONObject(0).get("From_Place")));
                            TextToAddress.setText(String.valueOf(jsoncc.getJSONObject(0).get("To_Place")));
                            dailyAllowance.setText(String.valueOf(jsoncc.getJSONObject(0).get("DA_Type")));
                        } else {
                            TextMode.setText(modeTypeVale);
                            TextMode.setText(String.valueOf(jsoncc.getJSONObject(0).get("MOT")));
                            binding.busFrom.setText(String.valueOf(jsoncc.getJSONObject(0).get("From_Place")));
                            TextToAddress.setText(String.valueOf(jsoncc.getJSONObject(0).get("To_Place")));
                            if (jsoncc.getJSONObject(0).get("DA_Type").equals("HQ")) {
                                BusTo.setVisibility(View.GONE);
                            } else {
                                BusTo.setVisibility(View.VISIBLE);
                            }
                            dailyAllowance.setText(String.valueOf(jsoncc.getJSONObject(0).get("DA_Type")));
                        }
                        if (String.valueOf(jsoncc.getJSONObject(0).get("Driver_Allow")).equals("1")) {
                            linCheckdriver.setVisibility(View.VISIBLE);
                            driverAllowance.setChecked(true);
                        } else {
                            linCheckdriver.setVisibility(View.GONE);
                            driverAllowance.setChecked(false);
                        }

                        Tp_dynamicArraylist.clear();
                        JSONArray jsnArValue = jsonObject.getJSONArray("DynamicViews");
                        for (int i = 0; i < jsnArValue.length(); i++) {
                            JSONObject json_oo = jsnArValue.getJSONObject(i);
                            ArrayList<Common_Model> a_listt = new ArrayList<>();
                            json_oo.getJSONArray("inputs");
                            JSONArray jarray = json_oo.getJSONArray("inputs");
                            String[] txtArray = json_oo.getString("Fld_Src_Field").split(",");
                            if (jarray.length() > 0) {
                                for (int m = 0; m < jarray.length(); m++) {
                                    JSONObject jjss = jarray.getJSONObject(m);
                                    a_listt.add(new Common_Model(jjss.getString(txtArray[1]), jjss.getString(txtArray[0]), false));
                                }
                            }
                            Tp_dynamicArraylist.add(new Tp_Dynamic_Modal(json_oo.getString("Fld_ID"), json_oo.getString("Fld_Name"), "", json_oo.getString("Fld_Type"), json_oo.getString("Fld_Src_Name"), json_oo.getString("Fld_Src_Field"), json_oo.getInt("Fld_Length"), json_oo.getString("Fld_Symbol"), json_oo.getString("Fld_Mandatory"), json_oo.getString("Active_flag"), json_oo.getString("Control_id"), json_oo.getString("Target_Form"), json_oo.getString("Filter_Text"), json_oo.getString("Filter_Value"), json_oo.getString("Field_Col"), a_listt));
                        }
                        dynamicadapter = new Mydayplan_Activity.DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext());
                        dynamicrecyclerview.setAdapter(dynamicadapter);
                        dynamicadapter.notifyDataSetChanged();
                        dynamicrecyclerview.setItemViewCacheSize(jsnArValue.length());
                    }
                    common_class.ProgressdialogShow(2, "Tour plan");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                common_class.ProgressdialogShow(2, "Tour Plan");
            }
        });
    }

    public void GetEmpList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> Callto = apiInterface.getDataArrayList("get/Emp_IdName",
                Shared_Common_Pref.Div_Code,
                Shared_Common_Pref.Sf_Code, empidedittext.getText().toString(), "", "DateTime", null);
        Callto.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                JsonArray res = response.body();
                assert res != null;
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
                text_tour_plancount.setText(String.valueOf(Jointworklistview.size()));
                adapter = new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", (position, flag1) -> {
                    Jointworklistview.remove(position);
                    text_tour_plancount.setText(String.valueOf(Jointworklistview.size()));
                    adapter.notifyDataSetChanged();
                });
                jointwork_recycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
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
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
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
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, modelTravelType, 100);
                Window window = customDialog.getWindow();
                assert window != null;
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
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
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                try {
                    if (response.isSuccessful()) {
                        Tp_dynamicArraylist.clear();
                        JSONArray jsnArValue = new JSONArray(new Gson().toJson(response.body()));
                        for (int i = 0; i < jsnArValue.length(); i++) {
                            JSONObject json_oo = jsnArValue.getJSONObject(i);
                            ArrayList<Common_Model> a_listt = new ArrayList<>();
                            json_oo.getJSONArray("inputs");
                            JSONArray jarray = json_oo.getJSONArray("inputs");
                            String[] txtArray = json_oo.getString("Fld_Src_Field").split(",");
                            if (json_oo.getString("Fld_Symbol").equals("JW")) {
                                jointwork_layout.setVisibility(View.VISIBLE);
                            }
                            if (jarray.length() > 0) {
                                for (int m = 0; m < jarray.length(); m++) {
                                    JSONObject jjss = jarray.getJSONObject(m);
                                    a_listt.add(new Common_Model(jjss.getString(txtArray[1]), jjss.getString(txtArray[0]), false));
                                }
                            }
                            Tp_dynamicArraylist.add(new Tp_Dynamic_Modal(json_oo.getString("Fld_ID"), json_oo.getString("Fld_Name"), "", json_oo.getString("Fld_Type"), json_oo.getString("Fld_Src_Name"), json_oo.getString("Fld_Src_Field"), json_oo.getInt("Fld_Length"), json_oo.getString("Fld_Symbol"), json_oo.getString("Fld_Mandatory"), json_oo.getString("Active_flag"), json_oo.getString("Control_id"), json_oo.getString("Target_Form"), json_oo.getString("Filter_Text"), json_oo.getString("Filter_Value"), json_oo.getString("Field_Col"), a_listt));
                        }
                        dynamicadapter = new Mydayplan_Activity.DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext());
                        dynamicrecyclerview.setAdapter(dynamicadapter);
                        dynamicadapter.notifyDataSetChanged();
                        dynamicrecyclerview.setItemViewCacheSize(jsnArValue.length());
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {

            }
        });
    }

    public void openspinnerbox(int position, ArrayList<Common_Model> ArrayList) {
        customDialog = new CustomListViewDialog(this, ArrayList, position);
        Window windowww = customDialog.getWindow();
        assert windowww != null;
        windowww.setGravity(Gravity.CENTER);
        windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void timePicker(int position) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(Mydayplan_Activity.this, (timePicker, selectedHour, selectedMinute) -> {
            dynamicarray.get(position).setFilter_Value(selectedHour + ":" + selectedMinute);
            dynamicadapter = new DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext());
            dynamicrecyclerview.setAdapter(dynamicadapter);
            dynamicadapter.notifyDataSetChanged();
            dynamicrecyclerview.setItemViewCacheSize(dynamicarray.size());
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void datePicker(int position) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            int mnth = monthOfYear + 1;
            dynamicarray.get(position).setFilter_Value(dayOfMonth + "-" + mnth + "-" + year);
            dynamicadapter.notifyDataSetChanged();
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        DatePickerDialog.show();
    }

    public class DynamicViewAdapter extends RecyclerView.Adapter<DynamicViewAdapter.MyViewHolder> {
        private final int rowLayout;

        public DynamicViewAdapter(ArrayList<Tp_Dynamic_Modal> array, int rowLayout, Context context) {
            dynamicarray = array;
            this.rowLayout = rowLayout;
        }

        @NonNull
        @Override
        public DynamicViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new DynamicViewAdapter.MyViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull DynamicViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (!dynamicarray.get(position).getFld_Symbol().equals("JW")) {
                holder.tpcaptions.setVisibility(View.VISIBLE);
                holder.tpcaptions.setText(dynamicarray.get(position).getFld_Name());
                String titleCaptions = dynamicarray.get(position).getFld_Name();
                String setTextValues = dynamicarray.get(position).getFilter_Value();
                //noinspection IfCanBeSwitch
                if (dynamicarray.get(position).getControl_id().equals("1") || dynamicarray.get(position).getControl_id().equals("3") || dynamicarray.get(position).getControl_id().equals("18") || dynamicarray.get(position).getControl_id().equals("24") || dynamicarray.get(position).getControl_id().equals("24")) {
                    holder.edittextid.setHint("" + titleCaptions);
                    holder.edittextid.setVisibility(View.VISIBLE);
                    holder.edittextid.setText(setTextValues);
                    switch (dynamicarray.get(position).getControl_id()) {
                        case "1":
                            holder.edittextid.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                        case "3":
                            holder.edittextid.setInputType(InputType.TYPE_CLASS_NUMBER);
                            break;
                        case "18":
                            holder.edittextid.setInputType(InputType.TYPE_CLASS_PHONE);
                            break;
                        case "24":
                            holder.edittextid.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            break;
                        default:
                            Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
                            break;
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
                    holder.Textspinnerview.setHint("Select The " + titleCaptions);
                    holder.Textspinnerview.setText(setTextValues);
                    holder.Textspinnerview.setVisibility(View.VISIBLE);
                    holder.edittextid.setVisibility(View.GONE);
                    if (titleCaptions.equals("Distributor")) {
                        sharedCommonPref.save(Constants.Distributor_name, setTextValues);
                        sharedCommonPref.save(Constants.Distributor_Id, dynamicarray.get(position).getFilter_Text());
                        sharedCommonPref.save(Constants.TEMP_DISTRIBUTOR_ID, dynamicarray.get(position).getFilter_Text());
                        common_class.getDataFromApi(Constants.Retailer_OutletList, Mydayplan_Activity.this, false);
                    }
                } else if (dynamicarray.get(position).getControl_id().equals("10")) {
                    holder.radiogroup.setVisibility(View.VISIBLE);
                    holder.edittextid.setVisibility(View.GONE);
                    for (int ii = 0; ii < dynamicarray.get(position).getA_list().size(); ii++) {
                        RadioButton rbn = new RadioButton(getApplicationContext());
                        rbn.setId(ii);
                        rbn.setText("" + dynamicarray.get(position).getA_list().get(ii).getName());
                        if (dynamicarray.get(position).getA_list().get(ii).isSelected() || (dynamicarray.get(position).getFilter_Text() != null && !Objects.equals(dynamicarray.get(position).getFilter_Text(), "") && dynamicarray.get(position).getFilter_Text().equals(dynamicarray.get(position).getA_list().get(ii).getId()))) {
                            rbn.setChecked(true);
                        }
                        holder.radiogroup.addView(rbn);
                    }

                    holder.radiogroup.setOnCheckedChangeListener((group, checkedId) -> {
                        RadioButton checkedRadioButton = group.findViewById(checkedId);
                        boolean isChecked = checkedRadioButton.isChecked();
                        if (isChecked) {
                            dynamicarray.get(position).setFilter_Value(dynamicarray.get(position).getA_list().get(checkedId).getName());
                            dynamicarray.get(position).setFilter_Text(dynamicarray.get(position).getA_list().get(checkedId).getId());
                            dynamicarray.get(position).getA_list().get(checkedId).setSelected(true);
                        }
                    });
                }
                holder.Textspinnerview.setOnClickListener(v -> {
                    if (dynamicarray.get(position).getControl_id().equals("11")) {
                        timePicker(position);
                    } else if (dynamicarray.get(position).getControl_id().equals("8")) {
                        datePicker(position);
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
