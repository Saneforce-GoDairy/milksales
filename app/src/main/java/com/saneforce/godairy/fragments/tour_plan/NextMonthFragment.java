package com.saneforce.godairy.fragments.tour_plan;

import static com.saneforce.godairy.Common_Class.Common_Class.addquote;
import static com.saneforce.godairy.common.AppConstants.GET_JOINT_WORK_LIST;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Activity_Hap.CustomListViewDialog;
import com.saneforce.godairy.Activity_Hap.Tp_Calander;
import com.saneforce.godairy.Activity_Hap.Tp_Mydayplan;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.Joint_Work_Listner;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.MVP.Main_Model;
import com.saneforce.godairy.Model_Class.ModeOfTravel;
import com.saneforce.godairy.Model_Class.Route_Master;
import com.saneforce.godairy.Model_Class.Tp_Dynamic_Modal;
import com.saneforce.godairy.Model_Class.Tp_View_Master;
import com.saneforce.godairy.R;
import com.saneforce.godairy.adapters.Joint_Work_Adapter;
import com.saneforce.godairy.adapters.TourPlanExploreAdapter;
import com.saneforce.godairy.common.DatabaseHandler;
import com.saneforce.godairy.databinding.CalendarItemBinding;
import com.saneforce.godairy.databinding.FragmentNextMonthBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NextMonthFragment extends Fragment implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface {
    private FragmentNextMonthBinding binding;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
    int SelectedMonth;
    // get current month / next month
    int CM, CY;
    int NM;
    private Common_Class common_class;
    private Type userType;
    private Gson gson;
    private List<Tp_View_Master> Tp_View_Master = new ArrayList<>();
    private int month, year;
    private GridCellAdapter adapterGrid;
    private ProgressDialog progressDialog;
    private Calendar _calendar;
    private RecyclerView.Adapter adapter2;
    private RecyclerView.Adapter adapter3;

    // tp_myday_plan
    private final List<Common_Model> worktypelist = new ArrayList<>();
    private final List<Common_Model> Route_Masterlist = new ArrayList<>();
    private final List<Common_Model> FRoute_Master = new ArrayList<>();
    private LinearLayout worktypelayout, distributors_layout, route_layout;
    private final List<Common_Model> distributor_master = new ArrayList<>();
    private final List<Common_Model> getfieldforcehqlist = new ArrayList<>();
    private final List<Common_Model> ChillingCenter_List = new ArrayList<>();
    private final List<Common_Model> Shift_Typelist = new ArrayList<>();
    private final List<Common_Model> Jointworklistview = new ArrayList<>();
    private List<Common_Model> Savejointwork = new ArrayList<>();
    private Main_Model.presenter presenter;
    private android.app.DatePickerDialog DatePickerDialog;
    private TimePickerDialog timePickerDialog;
    private final ArrayList<Tp_Dynamic_Modal> Tp_dynamicArraylist = new ArrayList<>();
    private EditText edt_remarks, empidedittext;
    private Shared_Common_Pref shared_common_pref;
    private String worktype_id, Worktype_Button = "", Fieldworkflag = "", hqid, shifttypeid, Chilling_Id;
    private Button submitbutton, GetEmpId;
    private CustomListViewDialog customDialog;
    private ProgressBar progressbar;
    boolean ExpNeed = false;
    private TextView Sf_name, distributor_text, route_text, hq_text, shift_type, chilling_text, Remarkscaption;
    private TextView tourdate, txtOthPlc;
    private Common_Model Model_Pojo;
    private LinearLayout jointwork_layout, hqlayout, shiftypelayout, Procrumentlayout, chillinglayout;
    private RecyclerView jointwork_recycler;
    private CardView ModeTravel, card_Toplace, CardDailyAllowance, card_from;
    private EditText reason;
    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    private String STRCode = "";
    private final String DM = "";
    private String DriverNeed = "false";
    private String DriverMode = "";
    private final String modeTypeVale = "";
    private String mode = "";
    private final String imageURI = "";
    private String modeVal = "";
    private String StartedKM = "";
    private String FromKm = "";
    private String ToKm = "";
    private final String Fare = "";
    private final String strDailyAllowance = "";
    private final String strDriverAllowance = "";
    private final String StToEnd = "";
    private final String StrID = "";
    private ArrayList<String> travelTypeList;
    private List<ModeOfTravel> modelOfTravel;
    private final List<Common_Model> modelTravelType = new ArrayList<>();
    private LinearLayout  vwExpTravel;
    private final List<Common_Model> listOrderType = new ArrayList<>();
    private Common_Model mCommon_model_spinner;
    private String modeId = "", toId = "", startEnd = "";
    private ImageView backView;
    private LinearLayout MdeTraval, DailyAll, frmPlace, ToPlace, Approvereject, rejectonly;
    int jcountglobal = 0;
    private Joint_Work_Adapter adapter;
    private LinearLayout Dynamictpview;
    private ArrayList<Tp_Dynamic_Modal> dynamicarray = new ArrayList<>();
    private DynamicViewAdapter dynamicadapter;
    private Button tpapprovebutton, tp_rejectsave, tpreject;
    private DatabaseHandler db;
    private final List<String> list = new ArrayList<>();
    private Context context ;

    private View bottomSheetView;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior bottomSheetBehavior;
    Spinner spinner;
    TextView bottomSheetDate;
    Button bottomSheetSubmit;
    EditText bottomSheetRemarks;
    String finalTourDate;

    Dialog jointWorkDialog;
    TextView jointWorkName;
    ArrayList<String> jointWorkNameList;
    ArrayList<String> jointWorkIdList;
    ArrayList<String> jointWorkDesigList;
    LinearLayout jointWorkExtraFieldLayout;
    String jointWorkSelectedEmployeeId, jointWorkSelectedEmployeeName, jointWorkSelectedEmployeeDesig;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNextMonthBinding.inflate(inflater, container, false);

        common_class = new Common_Class(getActivity());
        gson = new Gson();

        initProgressbar();
        getCurrentMonthAndNextMonth(); // CM , NM
        getCurrentYear(); // yyyy

        SelectedMonth = NM;

        progressDialog.show();
        GetTp_List(); // Load tour plan

        shared_common_pref = new Shared_Common_Pref(requireContext());  // tp_plan
        initBottomSheetDialog();
        initOnClickTpPlan();
        binding.textTourPlancount.setText("0");
        loadWorkTypes();
        loadExtraField();

        if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
            bottomSheetSubmit.setText("Submit");
            bottomSheetSubmit.setVisibility(View.VISIBLE);
            binding.SfName.setVisibility(View.GONE);
            binding.Approvereject.setVisibility(View.GONE);
        } else {
            bottomSheetSubmit.setText("Edit");
            bottomSheetSubmit.setVisibility(View.GONE);
            binding.SfName.setText("" + Shared_Common_Pref.Tp_Sf_name);
            binding.SfName.setVisibility(View.VISIBLE);
            binding.Approvereject.setVisibility(View.VISIBLE);

        }
        return binding.getRoot();
    }

      private void initJointWorkDialog() {
        jointWorkDialog = new Dialog(requireContext());
        jointWorkDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        jointWorkDialog.setContentView(R.layout.model_dialog_joint_work);
        jointWorkDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        jointWorkDialog.show();

        RecyclerView recyclerView = jointWorkDialog.findViewById(R.id.primaryChannelList);
        recyclerView.setEnabled(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(20);
        JointWorkAdapater jointWorkAdapater = new JointWorkAdapater(requireContext(), jointWorkIdList, jointWorkNameList, jointWorkDesigList);
        recyclerView.setAdapter(jointWorkAdapater);

    }

    public class JointWorkAdapater extends RecyclerView.Adapter<JointWorkAdapater.ViewHolder> {
        private Context context;
        private Activity activity;
        ArrayList  id;
        ArrayList  name;
        ArrayList  desig;

        public JointWorkAdapater(Context context, ArrayList  id, ArrayList  name , ArrayList  desig){
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

        @Override
        public void onBindViewHolder(@NonNull JointWorkAdapater.ViewHolder holder, int position) {
            holder.name.setText((String) name.get(position));

            Glide
                    .with(context)
                    .load(R.drawable.joint_work_bg)
                    .placeholder(R.color.grey_50)
                    .into(holder.channelImage);

            String mName = (String) name.get(position);

            holder.nameLetter.setText(mName.substring(0,1).toUpperCase());

            /*
              [{"id":"TRMUMGR0009",
              "name":"Ramesh qc-GENERAL SE",
              "desig":"GENERALSE"]}
             */

            holder.mainLayout.setOnClickListener(view -> {
                Toast.makeText(context, "sucess", Toast.LENGTH_SHORT).show();
                holder.radioButton.setEnabled(true);
                holder.radioButton.setChecked(true);
                jointWorkName.setText((String) name.get(position));
                jointWorkExtraFieldLayout.setVisibility(View.VISIBLE);
                jointWorkDialog.dismiss();

                 jointWorkSelectedEmployeeId = (String) id.get(position);
                 jointWorkSelectedEmployeeName = (String) name.get(position);
                 jointWorkSelectedEmployeeDesig = (String) desig.get(position);

                Log.e("jw__", jointWorkSelectedEmployeeId + jointWorkSelectedEmployeeName + jointWorkSelectedEmployeeDesig);
            });

            holder.radioButton.setOnClickListener(view -> {
                Toast.makeText(context, "sucess", Toast.LENGTH_SHORT).show();
                holder.radioButton.setEnabled(true);

            });
        }

        @Override
        public int getItemCount() {
            return name.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView name, nameLetter;
            private CardView mainLayout;
            private RadioButton radioButton;
            private ImageView channelImage;

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

    private void loadExtraField() {
       ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("axn", GET_JOINT_WORK_LIST);
        params.put("sfCode", "MGR0201");
        Call<ResponseBody> call = apiInterface.getUniversalData(params);

       call.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               if (response.isSuccessful()){
                   String result = null;
                   try {
                       assert response.body() != null;
                       result = response.body().string();
                       JSONObject jsonObject = new JSONObject(result);

                       if (jsonObject.getBoolean("success")){
                           JSONArray array = jsonObject.getJSONArray("response");

                           jointWorkNameList = new ArrayList<>();
                           jointWorkIdList = new ArrayList<>();
                           jointWorkDesigList = new ArrayList<>();
                           //catList.add("Select");
                           for (int i =0; i<array.length(); i++){
                               JSONObject jsonObject1 = array.getJSONObject(i);
                               jointWorkIdList.add(jsonObject1.getString("id"));
                               jointWorkNameList.add(jsonObject1.getString("name"));
                               jointWorkDesigList.add(jsonObject1.getString("desig"));
                           }

//                           ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, catList);
//                           adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                           extraSpinner.setAdapter(adapter);

                           String debug = "";
                           Log.e("extra_field_", String.valueOf(array));
                       }else {
                           Log.e("extra_field_", "response error");
                       }
                   } catch (JSONException | IOException e) {
                       throw new RuntimeException(e);
                   }
               }
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {

           }
       });
    }

    private void initBottomSheetDialog() {
        bottomSheetView = getLayoutInflater().inflate(R.layout.bottomsheetdialog_tp_plan, null);
        bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());

        bottomSheetDate = bottomSheetDialog.findViewById(R.id.plan_date);
        bottomSheetSubmit = bottomSheetDialog.findViewById(R.id.submit);
        bottomSheetRemarks = bottomSheetDialog.findViewById(R.id.remarks);
        spinner = bottomSheetDialog.findViewById(R.id.spinner);

            jointWorkName = bottomSheetDialog.findViewById(R.id.joint_work_name);
        jointWorkExtraFieldLayout = bottomSheetDialog.findViewById(R.id.extra_field);

        assert jointWorkExtraFieldLayout != null;
        jointWorkExtraFieldLayout.setOnClickListener(v -> jointWorkDialog.show());

        assert spinner != null;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String workType = spinner.getSelectedItem().toString();

                if (workType.equals("Joint Work")){
                    assert jointWorkExtraFieldLayout != null;
                    jointWorkExtraFieldLayout.setVisibility(View.GONE);
                    initJointWorkDialog();
                }else {
                    assert jointWorkExtraFieldLayout != null;
                    jointWorkExtraFieldLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void loadWorkTypes() {
        db = new DatabaseHandler(requireContext());
        String id = null;
        String name = null;
        list.add("Select");
        try {
            JSONArray HAPLoca = db.getMasterData("HAPWorkTypes");
            if (HAPLoca != null) {
                for (int li = 0; li < HAPLoca.length(); li++) {
                    JSONObject jItem = HAPLoca.getJSONObject(li);
                     id = String.valueOf(jItem.optInt("id"));
                     name = jItem.optString("name");
                    String flag = jItem.optString("FWFlg");
                    String ETabs = jItem.optString("ETabs");
                    String PlInv = jItem.optString("Place_Involved");
                    boolean tExpNeed = (PlInv.equalsIgnoreCase("Y"));
                    Common_Model item = new Common_Model(id, name, flag, ETabs, tExpNeed);
                    worktypelist.add(item);

                    worktype_id = id;
                    spinner.setPrompt(name);
                    list.add(name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initOnClickTpPlan(){
        bottomSheetSubmit.setOnClickListener(v -> Tp_Submit("1"));
    }

    private void Tp_Submit(String Submit_Flag) {
        int workTypeIdPosition = spinner.getSelectedItemPosition();

        String check = String.valueOf(workTypeIdPosition);

        if(check.equals("0")){
            Toast.makeText(requireContext(), "Work Type Not Selected", Toast.LENGTH_SHORT).show();
            return;
        }
        String workType = spinner.getSelectedItem().toString();

        if (workType.isEmpty() || workType.equals("Select")){
            Toast.makeText(requireContext(), "Select work type", Toast.LENGTH_SHORT).show();
            return;
        }

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
        Calendar c = Calendar.getInstance();
        String Dcr_Dste = new SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(new Date());
        JSONArray jsonarr = new JSONArray();
        JSONObject jsonarrplan = new JSONObject();
        String remarks = bottomSheetRemarks.getText().toString();
        try {
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("worktype_code", addquote(String.valueOf(workTypeIdPosition)));
            jsonobj.put("Tour_Date", addquote(finalTourDate));
            jsonobj.put("worktype_name",  addquote(workType));
            jsonobj.put("Ekey", Common_Class.GetEkey());
            jsonobj.put("objective",  addquote(remarks));
            jsonobj.put("Flag", addquote(Fieldworkflag));
            jsonobj.put("Button_Access", Worktype_Button);
            jsonobj.put("MOT", addquote(binding.txtMode.getText().toString()));
            jsonobj.put("DA_Type", addquote(binding.textDailyAllowance.getText().toString()));
            jsonobj.put("Driver_Allow", addquote((binding.daDriverAllowance.isChecked()) ? "1" : "0"));
            jsonobj.put("From_Place", addquote(binding.edtFrm.getText().toString()));
            if (toId.equalsIgnoreCase("-1")) {
                jsonobj.put("To_Place", txtOthPlc.getText());
            } else {
                jsonobj.put("To_Place", addquote(binding.edtTo.getText().toString()));
            }
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
            jsonarrplan.put("Tour_Plan", jsonobj);
            jsonarrplan.put("Tp_DynamicValues", personarray);
            jsonarr.put(jsonarrplan);
            String Sf_Code;
            if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
                Sf_Code = Shared_Common_Pref.Sf_Code;
            } else {
                Sf_Code = Shared_Common_Pref.Tp_SFCode;
            }
            Map<String, String> QueryString = new HashMap<>();
            QueryString.put("sfCode", Sf_Code);
            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
            QueryString.put("State_Code", Shared_Common_Pref.StateCode);
            QueryString.put("Approval_MGR", Shared_Common_Pref.Tp_Approvalflag); // Todo: Tp_Approvalflag
            QueryString.put("desig", shared_common_pref.getvalue(Shared_Common_Pref.SF_DESIG));
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<Object> Callto = apiInterface.Tb_Mydayplannew(QueryString, jsonarr.toString());
            Callto.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    common_class.ProgressdialogShow(2, "Tour  plan");
                    if (response.code() == 200 || response.code() == 201) {
                        common_class.GetTP_Result("TourPlanSubmit", "", common_class.getintentValues("SubmitMonth"), common_class.getintentValues("TourYear"));

                        if (Submit_Flag.equals("1")) {
                            SendtpApproval("NTPApproval", 1);
                        } else {
                            common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", String.valueOf(common_class.getintentValues("TourMonth")));
                            Toast.makeText(requireContext(), "Work Plan Submitted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    common_class.ProgressdialogShow(2, "Tour  plan");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void SendtpApproval(String Name, int flag) {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "dcr/save");
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.Div_Code);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("Start_date", finalTourDate);
        QueryString.put("Mr_Sfcode", Shared_Common_Pref.Tp_SFCode);
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        try {
            sp.put("Sf_Code", Shared_Common_Pref.Tp_SFCode);
            if (flag == 2) {
                sp.put("reason", addquote(reason.getText().toString()));
            }
            jsonObject.put(Name, sp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
        mCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", String.valueOf(common_class.getintentValues("TourMonth")));
                if (flag == 1) {
                    Toast.makeText(requireContext(), "TP Approved  Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "TP Rejected  Successfully", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public boolean vali() {
        if (binding.worktypeText.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(requireContext(), "select the Work Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (int i = 0; i < dynamicarray.size(); i++) {
            if (dynamicarray.get(i).getFilter_Value() != null && dynamicarray.get(i).getFilter_Value().equals("") && dynamicarray.get(i).getFld_Mandatory().equals("1")) {
                if (dynamicarray.get(i).getFld_Symbol().equals("JW")) {
                    if (Jointworklistview.size() == 0) {
                        Toast.makeText(requireContext(), "Required Field " + dynamicarray.get(i).getFld_Name(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(requireContext(), "Required Field " + dynamicarray.get(i).getFld_Name(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        if (ExpNeed) {
            String sMsg = "";
            if (binding.txtMode.getText().toString().equalsIgnoreCase("")) {
                sMsg = "Select the Mode of travel";
            }
            if (binding.textDailyAllowance.getText().toString().equalsIgnoreCase("")) {
                sMsg = "Select the DA Type";
            }
            if (toId.equalsIgnoreCase("-1") && txtOthPlc.getText().toString().equalsIgnoreCase("")) {
                sMsg = "Enter the To Other Place";
            }
            if (!sMsg.equalsIgnoreCase("")) {
                Toast.makeText(requireContext(), sMsg, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void Get_MydayPlan(String tourDate) {
        String Sf_Code = "";
        if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
            Sf_Code = Shared_Common_Pref.Sf_Code;
        } else {
            Sf_Code = Shared_Common_Pref.Tp_SFCode;
        }
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "Get/Tp_dayplan");
        QueryString.put("Sf_code", Sf_Code);
        QueryString.put("Date", tourDate);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
        mCall.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    JSONArray jsoncc = jsonObject.getJSONArray("GettodayResult");

                    // hide by prasanth
                    //     jointwork_layout.setVisibility(View.GONE);
                    //    Jointworklistview.clear();
                    if (jsoncc.length() > 0) {
                        worktype_id = String.valueOf(jsoncc.getJSONObject(0).get("worktype_code"));
                        bottomSheetRemarks.setText(String.valueOf(jsoncc.getJSONObject(0).get("remarks")));
                        Fieldworkflag = String.valueOf(jsoncc.getJSONObject(0).get("Worktype_Flag"));
                        binding.worktypeText.setText(String.valueOf(jsoncc.getJSONObject(0).get("worktype_name")));

                        for (int i = 0; i < worktypelist.size(); i++) {
                            if (worktypelist.get(i).getId().equals(worktype_id)) {
                                spinner.setSelection(i + 1);
                                break;
                            }
                        }

                        modeId = String.valueOf(jsoncc.getJSONObject(0).get("Mot_ID"));
                        STRCode = String.valueOf(jsoncc.getJSONObject(0).get("To_Place_ID"));
                        toId = String.valueOf(jsoncc.getJSONObject(0).get("To_Place_ID"));
                        modeVal = String.valueOf(jsoncc.getJSONObject(0).get("Mode_Travel_Id"));
                        ExpNeed = false;
                        for (int ij = 0; ij < worktypelist.size(); ij++) {
                            if (worktype_id.equalsIgnoreCase(worktypelist.get(ij).getId())) {
                                ExpNeed = worktypelist.get(ij).getExpNeed();
                            }
                        }

                        // hide by prasanth
//                        vwExpTravel.setVisibility(View.VISIBLE);
//                        if (ExpNeed == false) {
//                            vwExpTravel.setVisibility(View.GONE);
//                        }
                        String Jointworkcode = String.valueOf(jsoncc.getJSONObject(0).get("JointworkCode"));
                        String JointWork_Name = String.valueOf(jsoncc.getJSONObject(0).get("JointWork_Name"));
                        String[] arrOfStr = Jointworkcode.split(",");
                        String[] arrOfname = JointWork_Name.split(",");

                        if (!Jointworkcode.equals("")) {
                            Jointworklistview.clear();
                            for (int ik = 0; arrOfStr.length > ik; ik++) {
                                Model_Pojo = new Common_Model(arrOfname[ik], arrOfStr[ik], false);
                                Jointworklistview.add(Model_Pojo);
                            }
                            if (Jointworklistview.size() > 0) {
                                jointwork_layout.setVisibility(View.VISIBLE);
                                binding.textTourPlancount.setText(String.valueOf(arrOfStr.length));
                                adapter = new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, requireContext(), "10", new Joint_Work_Listner() {
                                    @Override
                                    public void onIntentClick(int position, boolean flag) {
                                        Jointworklistview.remove(position);
                                        binding.textTourPlancount.setText(String.valueOf(Jointworklistview.size()));
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                jointwork_recycler.setAdapter(adapter);
                            }
                        }

                        binding.txtMode.setText(modeTypeVale);
                        binding.txtMode.setText(String.valueOf(jsoncc.getJSONObject(0).get("MOT")));
                        binding.edtFrm.setText(String.valueOf(jsoncc.getJSONObject(0).get("From_Place")));
                        binding.edtTo.setText(String.valueOf(jsoncc.getJSONObject(0).get("To_Place")));
                        binding.textDailyAllowance.setText(String.valueOf(jsoncc.getJSONObject(0).get("DA_Type")));
                        binding.CardOthPlc.setVisibility(View.GONE);

                        if (toId.equalsIgnoreCase("-1")) {
                            binding.CardOthPlc.setVisibility(View.VISIBLE);
                            binding.edtTo.setText("Other Location");
                            txtOthPlc.setText(String.valueOf(jsoncc.getJSONObject(0).get("To_Place")));
                        }
                        if (jsoncc.getJSONObject(0).get("DA_Type").equals("HQ")) {
                            binding.linToPlace.setVisibility(View.GONE);
                        } else {
                            binding.linToPlace.setVisibility(View.VISIBLE);
                        }
                        if (String.valueOf(jsoncc.getJSONObject(0).get("Driver_Allow")).equals("1")) {
                            binding.linCheckDriver.setVisibility(View.VISIBLE);
                            binding.daDriverAllowance.setChecked(true);
                        } else {
                            binding.linCheckDriver.setVisibility(View.GONE);
                            binding.daDriverAllowance.setChecked(false);
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

                        dynamicadapter = new DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, requireContext(), -1);
                        binding.dynamicrecyclerview.setAdapter(dynamicadapter);
                        dynamicadapter.notifyDataSetChanged();
                        //new Tp_Mydayplan.DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext(), 0).notifyDataSetChanged();
                        binding.dynamicrecyclerview.setItemViewCacheSize(jsnArValue.length());
                        if (String.valueOf(jsoncc.getJSONObject(0).get("submit_status")).equals("3")) {
                            submitbutton.setVisibility(View.GONE);
                        }
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

    @Override
    public void onClick(View v) {

    }

    public class DynamicViewAdapter extends RecyclerView.Adapter<DynamicViewAdapter.MyViewHolder> {
        private final int rowLayout;
        private final Context context;
        AdapterOnClick mAdapterOnClick;
        private final int Categorycolor;

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

        public DynamicViewAdapter(ArrayList<Tp_Dynamic_Modal> array, int rowLayout, Context context, int Categorycolor) {
            dynamicarray = array;
            this.rowLayout = rowLayout;
            this.context = context;
            this.Categorycolor = Categorycolor;
        }

        @Override
        public DynamicViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new DynamicViewAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DynamicViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (!dynamicarray.get(position).getFld_Symbol().equals("JW")) {
                holder.tpcaptions.setVisibility(View.VISIBLE);
                holder.tpcaptions.setText(dynamicarray.get(position).getFld_Name());
                String titlecaptions = dynamicarray.get(position).getFld_Name();
                String SEttextvalues = dynamicarray.get(position).getFilter_Value();
                if (dynamicarray.get(position).getControl_id().equals("1") || dynamicarray.get(position).getControl_id().equals("2") || dynamicarray.get(position).getControl_id().equals("3") || dynamicarray.get(position).getControl_id().equals("18") || dynamicarray.get(position).getControl_id().equals("24") || dynamicarray.get(position).getControl_id().equals("24")) {
                    holder.edittextid.setHint("" + titlecaptions);
                    holder.edittextid.setVisibility(View.VISIBLE);
                    holder.edittextid.setText(SEttextvalues);
                    if (dynamicarray.get(position).getControl_id().equals("1")) {
                        holder.edittextid.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else if (dynamicarray.get(position).getControl_id().equals("2")) {
                        holder.edittextid.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
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
                } else if (dynamicarray.get(position).getControl_id().equals("10")) {
                    holder.radiogroup.setVisibility(View.VISIBLE);
                    holder.edittextid.setVisibility(View.GONE);
                    for (int ii = 0; ii < dynamicarray.get(position).getA_list().size(); ii++) {
                        RadioButton rbn = new RadioButton(requireContext());
                        rbn.setId(ii);
                        rbn.setText("" + dynamicarray.get(position).getA_list().get(ii).getName());
                        if (dynamicarray.get(position).getA_list().get(ii).isSelected() || (dynamicarray.get(position).getFilter_Text() != null && dynamicarray.get(position).getFilter_Text() != "" && dynamicarray.get(position).getFilter_Text().equals(dynamicarray.get(position).getA_list().get(ii).getId()))) {
                            rbn.setChecked(true);
                        }
                        holder.radiogroup.addView(rbn);
                    }

                    holder.radiogroup.setOnCheckedChangeListener((group, checkedId) -> {
                        RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
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
                        timePicker(position, dynamicarray.get(position).getA_list());
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
    }

     public void openspinnerbox(int position, ArrayList<Common_Model> ArrayList) {
        customDialog = new CustomListViewDialog(getActivity(), ArrayList, position);
        Window windowww = customDialog.getWindow();
        windowww.setGravity(Gravity.CENTER);
        windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }

    public void timePicker(int position, ArrayList<Common_Model> ArrayList) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(requireContext(), (timePicker, selectedHour, selectedMinute) -> {
            dynamicarray.get(position).setFilter_Value(selectedHour + ":" + selectedMinute);
            dynamicadapter = new DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, requireContext(), -1);
            binding.dynamicrecyclerview.setAdapter(dynamicadapter);
            dynamicadapter.notifyDataSetChanged();
            binding.dynamicrecyclerview.setItemViewCacheSize(dynamicarray.size());
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void datePicker(int position) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog = new DatePickerDialog(requireContext(), (view, year, monthOfYear, dayOfMonth) -> {
            int mnth = monthOfYear + 1;
            dynamicarray.get(position).setFilter_Value(dayOfMonth + "-" + mnth + "-" + year);
            dynamicadapter.notifyDataSetChanged();
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        DatePickerDialog.show();
    }

      @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void setDataToRoute(ArrayList<Route_Master> noticeArrayList) {
        //Log.e("ROUTE_MASTER", String.valueOf(noticeArrayList.size()));
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
            //Get_MydayPlan(common_class.getintentValues("TourDate"));
        } else {
            GetJsonData(new Gson().toJson(noticeArrayList), "6");
            common_class.ProgressdialogShow(1, "Day plan");
            Get_MydayPlan(finalTourDate);
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
            binding.worktypeText.setText(myDataset.get(position).getName());
            worktype_id = String.valueOf(myDataset.get(position).getId());
            Fieldworkflag = myDataset.get(position).getFlag();
            Worktype_Button = myDataset.get(position).getCheckouttime();
            ExpNeed = myDataset.get(position).getExpNeed();
            jointwork_layout.setVisibility(View.GONE);
            Jointworklistview.clear();
            GetTp_Worktype_Fields(Worktype_Button);
            vwExpTravel.setVisibility(View.VISIBLE);
            if (!ExpNeed) {
                vwExpTravel.setVisibility(View.GONE);
            }
        } else if (type == 7) {
            binding.edtFrm.setText(myDataset.get(position).getName());
            shifttypeid = myDataset.get(position).getId();
        } else if (type == 102) {
            binding.edtTo.setText(myDataset.get(position).getName());
            toId = myDataset.get(position).getId();
            binding.CardOthPlc.setVisibility(View.GONE);
            if (toId.equalsIgnoreCase("-1")) {
                binding.CardOthPlc.setVisibility(View.VISIBLE);
            }
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
                binding.edtFrm.setText("");
                binding.edtTo.setText("");
            } else {
                mode = "12";
                FromKm = "";
                ToKm = "";
                StartedKM = "";
                binding.edtFrm.setText("");
                binding.edtTo.setText("");
            }
            if (DriverMode.equals("1")) {
                binding.linCheckDriver.setVisibility(View.VISIBLE);
            } else {
                binding.linCheckDriver.setVisibility(View.GONE);
            }
            DriverNeed = "";
            binding.daDriverAllowance.setChecked(false);
        } else if (type == 10) {
            binding.edtTo.setText(myDataset.get(position).getName());
        } else if (type == 101) {
            String TrTyp = myDataset.get(position).getName();
            binding.textDailyAllowance.setText(TrTyp);
            if (TrTyp.equals("HQ")) {
                binding.edtFrm.setVisibility(View.GONE);
            } else {
                binding.edtFrm.setVisibility(View.VISIBLE);
            }
            binding.edtTo.setText("");
        } else {
            dynamicarray.get(type).setFilter_Value(myDataset.get(position).getName());
            dynamicarray.get(type).setFilter_Text(myDataset.get(position).getId());
            dynamicadapter = new DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, requireContext(), -1);
            binding.dynamicrecyclerview.setAdapter(dynamicadapter);
            dynamicadapter.notifyDataSetChanged();
            binding.dynamicrecyclerview.setItemViewCacheSize(dynamicarray.size());
        }
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

            if (type.equalsIgnoreCase("4")) {
                Model_Pojo = new Common_Model("-1", "Other Location", "");
                getfieldforcehqlist.add(Model_Pojo);
            }

            if (type.equals("3")) {
                jointwork_recycler.setAdapter(new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, requireContext(), "10", new Joint_Work_Listner() {
                    @Override
                    public void onIntentClick(int po, boolean flag) {
                        Jointworklistview.get(po).setSelected(flag);
                        int jcount = 0;
                        for (int i = 0; Jointworklistview.size() > i; i++) {
                            if (Jointworklistview.get(i).isSelected()) {
                                jcount = jcount + 1;
                            }
                        }
                        binding.textTourPlancount.setText(String.valueOf(jcount));
                    }
                }));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetTp_Worktype_Fields(String wflag) {
        common_class.ProgressdialogShow(1, "Tour plan PJP");
        String Sf_Code;
        if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
            Sf_Code = Shared_Common_Pref.Sf_Code;
        } else {
            Sf_Code = Shared_Common_Pref.Tp_SFCode;
        }
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "get/worktypefields");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Sf_Code);
        QueryString.put("rSF", Sf_Code);
        QueryString.put("Worktype_Code", wflag);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GettpWorktypeFields(QueryString);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    if (response.isSuccessful()) {
                        String jsonData = null;
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
                                    jointwork_layout.setVisibility(View.VISIBLE);
                                }
                                // Toast.makeText(Tp_Mydayplan.this, "Fld_Src_Field", Toast.LENGTH_SHORT).show();
                                if (jarray != null && jarray.length() > 0) {
                                    for (int m = 0; m < jarray.length(); m++) {
                                        JSONObject jjss = jarray.getJSONObject(m);
                                        a_listt.add(new Common_Model(jjss.getString(txtArray[1]), jjss.getString(txtArray[0]), false));
                                    }
                                }
                            }
                            Tp_dynamicArraylist.add(new Tp_Dynamic_Modal(json_oo.getString("Fld_ID"), json_oo.getString("Fld_Name"), "", json_oo.getString("Fld_Type"), json_oo.getString("Fld_Src_Name"), json_oo.getString("Fld_Src_Field"), json_oo.getInt("Fld_Length"), json_oo.getString("Fld_Symbol"), json_oo.getString("Fld_Mandatory"), json_oo.getString("Active_flag"), json_oo.getString("Control_id"), json_oo.getString("Target_Form"), json_oo.getString("Filter_Text"), json_oo.getString("Filter_Value"), json_oo.getString("Field_Col"), a_listt));
                        }
                        dynamicadapter = new DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, requireContext(), -1);
                        binding.dynamicrecyclerview.setAdapter(dynamicadapter);
                        dynamicadapter.notifyDataSetChanged();
                        //new Tp_Mydayplan.DynamicViewAdapter(Tp_dynamicArraylist, R.layout.tp_dynamic_layout, getApplicationContext(), 0).notifyDataSetChanged();
                        binding.dynamicrecyclerview.setItemViewCacheSize(jsnArValue.length());
                        common_class.ProgressdialogShow(2, "Tour plan PJP");
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                common_class.ProgressdialogShow(2, "Tour plan PJP");
            }
        });
    }

    private void getCurrentYear() {
        _calendar = Calendar.getInstance(Locale.getDefault());
        if (SelectedMonth == 12 || SelectedMonth == 0) {
            SelectedMonth = 0;
            if (SelectedMonth == 12) {
                year = _calendar.get(Calendar.YEAR) + 1;
            } else {
                year = _calendar.get(Calendar.YEAR);
            }
        } else {
            year = _calendar.get(Calendar.YEAR);
        }
    }

    private void getCurrentMonthAndNextMonth() {
        Calendar cal = Calendar.getInstance();
        CM = cal.get(Calendar.MONTH);
        CY = cal.get(Calendar.YEAR);
        NM = cal.get(Calendar.MONTH) + 1;
        String currrentmonth = common_class.GetMonthname(CM) + "   " + CY;
        String nextmonth = "";
        if (CM == 11) {
            CY = CY + 1;
            nextmonth = common_class.GetMonthname(NM) + "   " + CY;
        } else
            nextmonth = common_class.GetMonthname(NM) + "   " + CY;
    }

    public void GetTp_List() {
            int SM = SelectedMonth + 1;
            String Tp_Object = "{\"tableName\":\"vwTourPlan\",\"coloumns\":\"[\\\"date\\\",\\\"remarks\\\",\\\"worktype_code\\\",\\\"worktype_name\\\",\\\"RouteCode\\\",\\\"RouteName\\\",\\\"Worked_with_Code\\\",\\\"Worked_with_Name\\\",\\\"JointWork_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            String Sf_Code = "";
            if (Shared_Common_Pref.Tp_Approvalflag.equals("0")) {
                Sf_Code = Shared_Common_Pref.Sf_Code;
            } else {
                Sf_Code = Shared_Common_Pref.Tp_SFCode;
            }

            Call<Object> mCall = apiInterface.GettpRespnse(Shared_Common_Pref.Div_Code, Sf_Code, Sf_Code, Shared_Common_Pref.StateCode, String.valueOf(SM), String.valueOf(year), Tp_Object);
            mCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    progressDialog.dismiss();

                    userType = new TypeToken<ArrayList<Tp_View_Master>>() {
                    }.getType();
                    //------------ Server response
                    Tp_View_Master = gson.fromJson(new Gson().toJson(response.body()), userType);
                    month = SelectedMonth + 1;

                    //------------- Gridview
                    adapterGrid = new GridCellAdapter(getContext(), R.id.date, month, year, (ArrayList<Tp_View_Master>) Tp_View_Master);
                    binding.gridcalander.setAdapter(adapterGrid);
                    adapterGrid.notifyDataSetChanged();

                    //-------------- RecyclerView
                    adapter2 = new TourPlanCalanderAdapter(getActivity(), R.id.date, SelectedMonth + 1, year, (ArrayList<com.saneforce.godairy.Model_Class.Tp_View_Master>) Tp_View_Master);
                    binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
                    binding.recyclerView.setHasFixedSize(true);
                    binding.recyclerView.setItemViewCacheSize(20);
                    binding.recyclerView.setAdapter(adapter2);
                    adapter2.notifyDataSetChanged();

                    //--------------- Tour plan RecyclerView Plan explore view
                    adapter3 = new TourPlanExploreAdapter(getActivity(), SelectedMonth + 1, year,(ArrayList<com.saneforce.godairy.Model_Class.Tp_View_Master>) Tp_View_Master);
                    binding.recyclerviewExplore.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerviewExplore.addItemDecoration(new DividerItemDecoration(requireContext(), 0));
                    binding.recyclerviewExplore.setHasFixedSize(true);
                    binding.recyclerviewExplore.setItemViewCacheSize(20);
                    binding.recyclerviewExplore.setAdapter(adapter3);
                    adapter3.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
    }

    public class TourPlanCalanderAdapter extends RecyclerView.Adapter<TourPlanCalanderAdapter.ViewHolder>{
        private final Context context;
        private final List<String> list;
        private ArrayList<Tp_View_Master> tpViewMasterArrayList;
        private static final int DAY_OFFSET = 1;
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private TextView num_events_per_day;
        private String  curentDateString;
        private Calendar selectedDate;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private SimpleDateFormat df;
        int selectedPosition = -1;
        int lastSelectedPosition = -1;

        public TourPlanCalanderAdapter(Context context, int textViewResourceId, int month, int year, ArrayList<Tp_View_Master> tpViewMasterArrayList) {
            this.context = context;
            this.tpViewMasterArrayList = tpViewMasterArrayList;
            this.list = new ArrayList<>();

            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            selectedDate = (Calendar) calendar.clone();
            df = new SimpleDateFormat("MMM");
            curentDateString = df.format(selectedDate.getTime());

            printMonth(month, year);
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        @NonNull
        @Override
        public TourPlanCalanderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CalendarItemBinding binding = CalendarItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull TourPlanCalanderAdapter.ViewHolder holder, int position) {
            holder.binding.getRoot().setOnClickListener(v -> {
                lastSelectedPosition = selectedPosition;
                selectedPosition = holder.getBindingAdapterPosition();
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);

                String[] day_color1 = list.get(position).split("-");
                String theday1 = day_color1[0];
                String themonth1 = day_color1[2];
                String theyear1 = day_color1[3];
                int month = SelectedMonth + 1;
                String TourMonth = theyear1 + "-" + month + "-" + theday1;
                finalTourDate = TourMonth;
                Get_MydayPlan(TourMonth);

                bottomSheetDialog.show();
                bottomSheetDate.setText(TourMonth);
                bottomSheetRemarks.setText(null);
                spinner.setSelection(0);
              //  common_class.CommonIntentwithoutFinishputextratwo(Tp_Mydayplan.class, "TourDate", TourMonth, "TourMonth", String.valueOf(month - 1));
            });

            if (selectedPosition == holder.getBindingAdapterPosition()) {
                final int sdk = Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.mDateLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg) );
                } else {
                    holder.mDateLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_enabled_bg));
                }
                holder.mDate.setTextColor(Color.WHITE);
            } else {
                final int sdk = Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.mDateLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.tp_month_disabled) );
                } else {
                    holder.mDateLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.tp_month_disabled));
                }
                holder.mDate.setTextColor(Color.BLACK);
            }

            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    //  num_events_per_day = view.findViewById(R.id.num_events_per_day);
                    Integer numEvents = eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            // Set the Day GridCell
            holder.mDate.setText(theday);
            holder.mDate.setTag(theday + "-" + themonth + "-" + theyear);
            if (day_color[1].equals("GREY")) {
                holder.mDate.setTextColor(Color.LTGRAY);
                holder.mDate.setEnabled(false);
            }
            if (day_color[1].equals("GREEN")) {
                holder.mTourPlanCircle.setVisibility(View.VISIBLE);
            }
            if (day_color[1].equals("BLUE")) {
                holder.mDate.setTextColor(getResources().getColor(R.color.Pending_yellow));
            }

            if (tpViewMasterArrayList != null){
                for (int i = 0; tpViewMasterArrayList.size() > i; i++ ){}
            }else {
                Toast.makeText(context, "Empty dp arraylist", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public int getItemViewType(int position){
            return position;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private CalendarItemBinding binding;
            private TextView mDate;
            private RelativeLayout mDateLayout;
            private CardView mTourPlanCircle;

            public ViewHolder(CalendarItemBinding binding)
            {
                super(binding.getRoot());
                this.binding = binding;

                mDate = binding.date;
                mDateLayout = binding.dateLayout;
                mTourPlanCircle = binding.color;
            }
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        public String CheckTp_View(int a) {
            String bflag = "0";
            if (Tp_View_Master != null) {


                for (int i = 0; Tp_View_Master.size() > i; i++) {
                    if (a == Tp_View_Master.get(i).getDayofcout()) {
                        Log.v("SUBMIT_STATUS", String.valueOf(Tp_View_Master.get(i).getSubmitStatus() + "DAY" + Tp_View_Master.get(i).getDayofcout()));
                        if (String.valueOf(Tp_View_Master.get(i).getSubmitStatus()).equals("3")) {
                            bflag = "3";
                        } else {
                            bflag = "1";
                        }
                    }
                }
            }
            return bflag;
        }

        @SuppressLint("WrongConstant")
        private void printMonth(int mm, int yy) {
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);
            // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
                ++daysInMonth;
            }

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                if (CheckTp_View(i).equals("1") || CheckTp_View(i).equals("3")) {
                    Log.e("getCurrentDayOfMonth", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth) + "DATE " + getCurrentDayOfMonth() + "-" + getMonthAsString(currentMonth) + "=" + yy);
                    if (CheckTp_View(i).equals("1")) {
                        Log.e("PENDING_COLOR", CheckTp_View(i));
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    } else {
                        Log.e("APPROVED_COLOR", CheckTp_View(i));
                        list.add(String.valueOf(i) + "-GREEN" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                   /* if (getMonthAsString(currentMonth).equals(curentDateString)) {
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                        Log.d("getCurrentDayOfMonth11", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth));
                    } else {
                        list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }*/
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }
                Log.e("DAY_of_month", String.valueOf(list.get(i - 1)));
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                list.add(i + 1 + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
            }
            for (int i = 0; i < list.size(); i++) {
                Log.e("DAYCOLOR", String.valueOf(list.get(i)));
                Log.e("Days_In_A month", String.valueOf(daysInMonth));
            }
        }

        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            return map;
        }
    }

    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private final Context _context;
        private final List<String> list;
        private ArrayList<Tp_View_Master> Tp_View_Master;
        private static final int DAY_OFFSET = 1;
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private TextView gridcell;
        private ImageView iv_icon;
        private TextView num_events_per_day;
        private String  curentDateString;
        private Calendar selectedDate;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private SimpleDateFormat df;

        public GridCellAdapter(Context context, int textViewResourceId, int month, int year, ArrayList<Tp_View_Master> Tp_View_Master) {
            super();
            this._context = context;
            this.Tp_View_Master = Tp_View_Master;

            this.list = new ArrayList<>();

            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            selectedDate = (Calendar) calendar.clone();
            df = new SimpleDateFormat("MMM");
            curentDateString = df.format(selectedDate.getTime());

            printMonth(month, year);

            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @SuppressLint("WrongConstant")
        private void printMonth(int mm, int yy) {
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);
            // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
                ++daysInMonth;
            }

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                if (CheckTp_View(i).equals("1") || CheckTp_View(i).equals("3")) {
                    Log.e("getCurrentDayOfMonth", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth) + "DATE " + getCurrentDayOfMonth() + "-" + getMonthAsString(currentMonth) + "=" + yy);
                    if (CheckTp_View(i).equals("1")) {
                        Log.e("PENDING_COLOR", CheckTp_View(i));
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    } else {
                        Log.e("APPROVED_COLOR", CheckTp_View(i));
                        list.add(String.valueOf(i) + "-GREEN" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                   /* if (getMonthAsString(currentMonth).equals(curentDateString)) {
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                        Log.d("getCurrentDayOfMonth11", String.valueOf(i) + "-BLUE" + "-" + curentDateString + "-" + yy + "  " + getMonthAsString(currentMonth));
                    } else {
                        list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }*/
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }

                Log.e("DAY_of_month", String.valueOf(list.get(i - 1)));
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
            }
            for (int i = 0; i < list.size(); i++) {
                Log.e("DAYCOLOR", String.valueOf(list.get(i)));
                Log.e("Days_In_A month", String.valueOf(daysInMonth));
            }
        }

        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.calendar_item, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = row.findViewById(R.id.date);
            iv_icon = row.findViewById(R.id.tp_date_icon);


            // for spacing
            String[] day_color = list.get(position).split("-");

            Log.e("THE_DAY_COLOR", String.valueOf(day_color[0]));
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    num_events_per_day = row.findViewById(R.id.num_events_per_day);
                    Integer numEvents = eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.e("ALL_DATE", theday + "-" + themonth + "-" + theyear + day_color[1]);
            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(Color.LTGRAY);
                gridcell.setEnabled(false);
            }
            if (day_color[1].equals("GREEN")) {
                gridcell.setTextColor(getResources().getColor(R.color.subExpHeader));
            }
            if (day_color[1].equals("BLUE")) {
                gridcell.setTextColor(getResources().getColor(R.color.Pending_yellow));
            }
            int in = 0;
            gridcell.setOnClickListener(v -> {
                String[] day_color1 = list.get(position).split("-");
                String theday1 = day_color1[0];
                String themonth1 = day_color1[2];
                String theyear1 = day_color1[3];
                int month = SelectedMonth + 1;
                String TourMonth = theyear1 + "-" + month + "-" + theday1;
                common_class.CommonIntentwithoutFinishputextratwo(Tp_Mydayplan.class, "TourDate", TourMonth, "TourMonth", String.valueOf(month - 1));

            });
            return row;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();
            try {
                dateFormatter.parse(date_month_year);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        public String CheckTp_View(int a) {
            String bflag = "0";
            if (Tp_View_Master != null) {
                for (int i = 0; Tp_View_Master.size() > i; i++) {
                    if (a == Tp_View_Master.get(i).getDayofcout()) {
                        Log.v("SUBMIT_STATUS", String.valueOf(Tp_View_Master.get(i).getSubmitStatus() + "DAY" + Tp_View_Master.get(i).getDayofcout()));
                        if (String.valueOf(Tp_View_Master.get(i).getSubmitStatus()).equals("3")) {
                            bflag = "3";
                        } else {
                            bflag = "1";
                        }
                    }
                }
            }
            return bflag;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }

    private void initProgressbar() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading.......");
        progressDialog.setTitle("Tour Plan");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
    }

}