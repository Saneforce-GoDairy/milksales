package com.saneforce.godairy.SFA_Activity;

import static com.saneforce.godairy.Common_Class.Constants.GroupFilter;
import static com.saneforce.godairy.Common_Class.Constants.Rout_List;
import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.Model_Class.PrimaryNoOrderList;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.PrimaryOrder_History_Adapter;
import com.saneforce.godairy.SFA_Adapter.RyclBrandListItemAdb;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.common.LocationFinder;
import com.saneforce.godairy.databinding.ActivityTodayPrimorderHistoryBinding;
import com.saneforce.godairy.procurement.custom_form.CustomFormDashboardActivity;
import com.saneforce.godairy.procurement.custom_form.ReportHomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayPrimOrdActivity extends AppCompatActivity implements Master_Interface, View.OnClickListener, UpdateResponseUI {
    private ActivityTodayPrimorderHistoryBinding binding;
    private final Context context = this;
    private Common_Class common_class;
    private Shared_Common_Pref sharedCommonPref;
    ArrayList<String> noOrderVisitList;
    @SuppressLint("StaticFieldLeak")
    public static TodayPrimOrdActivity mTdPriAct;
    private String mSelectedNoOrderReason, distributerCode, mDistributerName, date = "", groupType = "All";
    private final List<Common_Model> FRoute_Master = new ArrayList<>();
    private Dialog noOrderDialog;
    private Dialog purposeOfVisitDialog;
    private EditText nameEditText;
    private int mERPCode;
    private double lat = 0, lng = 0;
    private ImageView ivToolbarHome;
    public static String stDate = "", endDate = "";
    private static final String TAG = "TodayPrimOrdActivity";
    private TextView tvStartDate, tvEndDate;
    private List<PrimaryNoOrderList> primaryNoOrderListsMain;
    private PrimaryNoOrderListAdapter primaryNoOrderListAdapter;
    private LinearLayout navbtns;
    AssistantClass assistantClass;
    JSONArray filterArr;
    PrimaryOrder_History_Adapter mReportViewAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityTodayPrimorderHistoryBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            assistantClass = new AssistantClass(context);
            mTdPriAct = this;
            sharedCommonPref = new Shared_Common_Pref(TodayPrimOrdActivity.this);
            common_class = new Common_Class(this);
            common_class.getDb_310Data(Constants.GroupFilter, this);
            iniVariable();
            intialize();
            onClickListener();
            noOrderVisitList = new ArrayList<>();
            common_class.gotoHomeScreen(this, ivToolbarHome);
            stDate = Common_Class.GetDatewothouttime();
            endDate = Common_Class.GetDatewothouttime();
            common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, this, false);
            binding.tvStartDate.setText(stDate);
            binding.tvEndDate.setText(endDate);
            primaryNoOrderListsMain = new ArrayList<>();
            navbtns=findViewById(R.id.navbtns);
            navbtns.setVisibility(View.VISIBLE);
            Shared_Common_Pref.OutletCode = "OutletCode";
            if(sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equalsIgnoreCase(Constants.DISTRIBUTER_TYPE)){
               navbtns.setVisibility(View.GONE);
           }
            getNoOrderTeplates();
    }

    private void getNoOrderTeplates() {
        Map<String, String> params = new HashMap<>();
        params.put("axn", "getPrimaryNoOrderTemplates");
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                JSONArray array = jsonObject.optJSONArray("response");
                if (array == null) {
                    array = new JSONArray();
                }
                for (int i = 0; i < array.length(); i++) {
                    noOrderVisitList.add(array.optJSONObject(i).optString("template"));
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void loadList() {
        Map<String, String> params = new HashMap<>();
        params.put("axn", "getNoOrders");
        params.put("erpCode", "" + mERPCode);
        params.put("from", stDate);
        params.put("to", endDate);
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray array = jsonObject.getJSONArray("response");
                    for (int i = 0; i < array.length(); i++) {
                        filterArr.put(array.getJSONObject(i));
                    }
                    mReportViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("makeApiCall", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void intialize() {
        if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
            binding.layoutDistributer.setEnabled(false);
            binding.btnCmbRoute.setVisibility(View.GONE);
            findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);
            binding.txtDistributor.setText("HI! " + sharedCommonPref.getvalue(Constants.Distributor_name, ""));
            distributerCode = sharedCommonPref.getvalue(Constants.Distributor_Id, "");
        } else {
            if (!sharedCommonPref.getvalue(Constants.Distributor_Id).equals("")) {
                common_class.getDb_310Data(Rout_List, this);
                binding.txtDistributor.setText(/*"Hi! " +*/ sharedCommonPref.getvalue(Constants.Distributor_name, ""));
                distributerCode = sharedCommonPref.getvalue(Constants.Distributor_Id, "");
            } else {
                binding.btnCmbRoute.setVisibility(View.GONE);
            }
        }

        String ds = sharedCommonPref.getvalue(Constants.Distributor_name, "");
        mERPCode = Integer.parseInt(distributerCode.replaceAll("[\\D]", ""));
        mDistributerName = ds.replaceAll("[0-9]","").replaceAll("[^a-zA-Z0-9\\s]", "");

        new LocationFinder(getApplication(), location -> {
            if (location != null) {
                try {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                } catch (Exception ignored) { }
            }
        });
    }

    private void onClickListener() {
        binding.orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, PrimaryOrderActivity.class);
            intent.putExtra("Mode", "order_view");
            startActivity(intent);
        });
        binding.marketingActivity.setOnClickListener(v -> {
            Intent intent = new Intent(context, CustomFormDashboardActivity.class);
            intent.putExtra("isPrimary", 1);
            startActivity(intent);
        });
        binding.marketingActivityView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReportHomeActivity.class);
            intent.putExtra("isPrimary", 1);
            startActivity(intent);
        });
        binding.noOrder.setOnClickListener(v -> {
            noOrderDialog = new Dialog(context);
            noOrderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            noOrderDialog.setContentView(R.layout.primary_order_no_dialog);
            noOrderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            noOrderDialog.setCancelable(false);
            noOrderDialog.show();

            RelativeLayout close = noOrderDialog.findViewById(R.id.close);
            RelativeLayout submitBtn = noOrderDialog.findViewById(R.id.submit_button);
            nameEditText = noOrderDialog.findViewById(R.id.name_edit_text);
            LinearLayout noOrderPurposeOfVisit = noOrderDialog.findViewById(R.id.purpose_of_visit);

            noOrderPurposeOfVisit.setOnClickListener(v13 -> {
                purposeOfVisitDialog = new Dialog(context);
                purposeOfVisitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                purposeOfVisitDialog.setContentView(R.layout.model_purpose_of_visit_dialog);
                purposeOfVisitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                purposeOfVisitDialog.setCancelable(true);
                purposeOfVisitDialog.show();

                RecyclerView recyclerView = purposeOfVisitDialog.findViewById(R.id.recycler_view);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(20);
                NoOrderPurposeOfVisitAdapter purposeOfVisitAdapter = new NoOrderPurposeOfVisitAdapter(context, noOrderVisitList);
                recyclerView.setAdapter(purposeOfVisitAdapter);
            });

            submitBtn.setOnClickListener(v12 -> {
                String mChannealName = nameEditText.getText().toString().trim();

                if ("". equals(mChannealName)){
                    nameEditText.requestFocus();
                    return;
                }
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


                String mDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String mTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String mTimeDate  = mDate +" "+mTime;

                String userInfo = "MyPrefs";
                SharedPreferences UserDetails = getSharedPreferences(userInfo, Context.MODE_PRIVATE);
                String mSFCode =UserDetails.getString("Sfcode","");

                Call<ResponseBody> call = apiInterface.primaryNoOrderReasonSubmit("save/no_order_reason",
                        mChannealName,
                        mSFCode,
                        String.valueOf(mERPCode),
                        mDistributerName,
                        String.valueOf(lat),
                        String.valueOf(lng),
                        mTimeDate);

                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            assert response.body() != null;
                            String res = null;
                            try {
                                res = response.body().string();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, TodayPrimOrdActivity.this, false);
                            noOrderDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        noOrderDialog.dismiss();
                    }
                });

            });
            close.setOnClickListener(v1 -> noOrderDialog.dismiss());
        });

        binding.tvStartDate.setOnClickListener(this);
        binding.tvEndDate.setOnClickListener(this);
        binding.layoutDistributer.setOnClickListener(this);
        binding.btnCmbRoute.setOnClickListener(this);
    }

    private void iniVariable() {
        ivToolbarHome = findViewById(R.id.toolbar_home);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);

    }

    public static class PrimaryNoOrderListAdapter extends  RecyclerView.Adapter<PrimaryNoOrderListAdapter.ViewHolder>{
        private List<PrimaryNoOrderList> primaryNoOrderListsA;
        private Context context;

        public PrimaryNoOrderListAdapter(Context context, List<PrimaryNoOrderList> primaryNoOrderListsA) {
            this.context = context;
            this.primaryNoOrderListsA = primaryNoOrderListsA;
        }

        @NonNull
        @Override
        public PrimaryNoOrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_no_order_reason_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PrimaryNoOrderListAdapter.ViewHolder holder, int position) {
            holder.text.setText(primaryNoOrderListsA.get(position).getId());
            holder.txtReason.setText(primaryNoOrderListsA.get(position).getReason());

            String upToNCharacters = primaryNoOrderListsA.get(position).getDateTime().substring(0, Math.min(primaryNoOrderListsA.get(position).getDateTime().length(), 10));

            holder.txtDate.setText(upToNCharacters);
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
            return primaryNoOrderListsA.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView text, txtReason, txtDate;
            CardView layout;

            public ViewHolder(View view) {
                super(view);
                text = view.findViewById(R.id.txt_name);
                layout = view.findViewById(R.id.layout);
                txtReason = view.findViewById(R.id.txt_reason);
                txtDate = view.findViewById(R.id.txt_date);
            }
        }
    }

    public class NoOrderPurposeOfVisitAdapter extends RecyclerView.Adapter<NoOrderPurposeOfVisitAdapter.ViewHolder> {
        ArrayList<String> exploreName;
        Context context;

        public NoOrderPurposeOfVisitAdapter(Context context, ArrayList<String> courseName) {
            this.context = context;
            this.exploreName = courseName;
        }

        @NonNull
        @Override
        public NoOrderPurposeOfVisitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_purpose_of_visit_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NoOrderPurposeOfVisitAdapter.ViewHolder holder, int position) {
            holder.text.setText(exploreName.get(position));
            holder.layout.setOnClickListener(v -> {
                mSelectedNoOrderReason = exploreName.get(position).toString();
                nameEditText.setText(mSelectedNoOrderReason);
                purposeOfVisitDialog.dismiss();
                noOrderDialog.show();
            });
        }

        @Override
        public int getItemCount() {
            return exploreName.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView text;
            LinearLayout layout;

            public ViewHolder(View view) {
                super(view);
                text = view.findViewById(R.id.txt_name);
                layout = view.findViewById(R.id.layout);
            }
        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog(type);
        switch (type) {
            case 2:
                groupType = "All";
                common_class.brandPos = 0;
                binding.txtRoute.setText("");
                sharedCommonPref.save(Constants.Route_name, "");
                sharedCommonPref.save(Constants.Route_Id, "");
                binding.txtDistributor.setText(myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Distributor_name, myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Distributor_Id, myDataset.get(position).getId());
                sharedCommonPref.save(Constants.DistributorERP, myDataset.get(position).getCont());
                sharedCommonPref.save(Constants.DivERP, myDataset.get(position).getDivERP());
                sharedCommonPref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());
                sharedCommonPref.save(Constants.Distributor_phone, myDataset.get(position).getPhone());
                sharedCommonPref.save(Constants.CusSubGrpErp, myDataset.get(position).getCusSubGrpErp());
                sharedCommonPref.save(Constants.DistributorGst, myDataset.get(position).getDisGst());
                sharedCommonPref.save(Constants.DistributorFSSAI, myDataset.get(position).getDisFssai());
                common_class.getDb_310Data(Constants.GroupFilter, this);
                common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, TodayPrimOrdActivity.this, false);
                common_class.getDb_310Data(Rout_List, this);
                break;

            case 3:
                binding.txtRoute.setText(myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Route_name, myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Route_Id, myDataset.get(position).getId());
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStartDate:
                selectDate(1);
                break;

            case R.id.tvEndDate:
                selectDate(2);
                break;

            case R.id.btnCmbRoute:
                if (FRoute_Master != null && FRoute_Master.size() > 1) {
                    common_class.showCommonDialog(FRoute_Master, 3, this);
                }
                break;

            case R.id.llDistributor:
                common_class.showCommonDialog(common_class.getDistList(), 2, this);
                break;
        }
    }

    void selectDate(int val) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(TodayPrimOrdActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (common_class.checkDates(date, tvEndDate.getText().toString(), TodayPrimOrdActivity.this) ||
                            tvEndDate.getText().toString().equals("")) {
                        tvStartDate.setText(date);
                        stDate = tvStartDate.getText().toString();
                        common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, TodayPrimOrdActivity.this, false);
                    } else
                        common_class.showMsg(TodayPrimOrdActivity.this, "Please select valid date");
                } else {
                    if (common_class.checkDates(tvStartDate.getText().toString(), date, TodayPrimOrdActivity.this) ||
                            tvStartDate.getText().toString().equals("")) {
                        tvEndDate.setText(date);
                        endDate = tvEndDate.getText().toString();
                        common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, TodayPrimOrdActivity.this, false);
                    } else
                        common_class.showMsg(TodayPrimOrdActivity.this, "Please select valid date");
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public void loadroute() {
        if (FRoute_Master.size() == 1) {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.INVISIBLE);
            binding.txtRoute.setText(FRoute_Master.get(0).getName());
            sharedCommonPref.save(Constants.Route_name, FRoute_Master.get(0).getName());
            sharedCommonPref.save(Constants.Route_Id, FRoute_Master.get(0).getId());
        } else {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {

            if (apiDataResponse != null && !apiDataResponse.equals("")) {

                switch (key) {
                    case Rout_List:
                        JSONArray routeArr = new JSONArray(apiDataResponse);
                        FRoute_Master.clear();
                        for (int i = 0; i < routeArr.length(); i++) {
                            JSONObject jsonObject1 = routeArr.getJSONObject(i);
                            String id = String.valueOf(jsonObject1.optInt("id"));
                            String name = jsonObject1.optString("name");
                            String flag = jsonObject1.optString("FWFlg");
                            new Common_Model(id, name, flag);
                            Common_Model model_Pojo;
                            model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                            FRoute_Master.add(model_Pojo);
                        }
                        loadroute();
                        break;

                    case GroupFilter:
                        Log.v(key, apiDataResponse);
                        JSONObject filterObj = new JSONObject(apiDataResponse);
                        JSONObject obj = new JSONObject();
                        obj.put("name", "All");
                        obj.put("GroupCode", "0");
                        JSONArray arr = new JSONArray();
                        arr.put(obj);

                        if (filterObj.getBoolean("success")) {
                            findViewById(R.id.cvPrimOrdFilter).setVisibility(View.VISIBLE);

                            for (int i = 0; i < filterObj.getJSONArray("Data").length(); i++)
                                arr.put(filterObj.getJSONArray("Data").getJSONObject(i));
                        }

                        binding.recyclerviewPrimaryOrderFilter.setAdapter(new RyclBrandListItemAdb(arr, this, item -> {
                            try {
                                groupType = item.getString("name");
                                setHistoryAdapter(new JSONArray(sharedCommonPref.getvalue(Constants.GetTodayPrimaryOrder_List)));
                            } catch (Exception e) {
                                Log.v(TAG, e.getMessage());
                            }
                        }));
                        break;

                    case Constants.GetTodayPrimaryOrder_List:
                        Log.v(TAG, apiDataResponse);
                        sharedCommonPref.save(Constants.GetTodayPrimaryOrder_List, apiDataResponse);
                        setHistoryAdapter(new JSONArray(apiDataResponse));
                        loadList();
                }
            }
        } catch (Exception e) {
            Log.v("Invoice History: ", e.getMessage());
        }
    }

    @SuppressLint("SetTextI18n")
    void setHistoryAdapter(JSONArray arr) {
        try {
            filterArr = new JSONArray();
            for (int i = 0; i < arr.length(); i++) {
                if (Common_Class.isNullOrEmpty(groupType) || groupType.equalsIgnoreCase("All") || groupType.equalsIgnoreCase(arr.getJSONObject(i).getString("category_type"))) {
                    filterArr.put(arr.getJSONObject(i));
                }
            }

            mReportViewAdapter = new PrimaryOrder_History_Adapter(TodayPrimOrdActivity.this, filterArr, new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    try {
                        JSONObject obj = filterArr.getJSONObject(position);
                        Shared_Common_Pref.TransSlNo = obj.getString("Trans_Sl_No");
                        Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
                        sharedCommonPref.save(Constants.FLAG, "Primary Order");
                        intent.putExtra("Mode", "order_view");
                        intent.putExtra("Order_Values", obj.getString("Order_Value"));
                        intent.putExtra("Invoice_Values", obj.getString("invoicevalues"));
                        //intent.putExtra("No_Of_Items", FilterOrderList.get(position).getNo_Of_items());
                        intent.putExtra("Invoice_Date", obj.getString("Order_Date"));
                        intent.putExtra("NetAmount", obj.getString("NetAmount"));
                        intent.putExtra("Discount_Amount", obj.getString("Discount_Amount"));
                        intent.putExtra("gstn", sharedCommonPref.getvalue(Constants.DistributorGst));
                        startActivity(intent);
                        overridePendingTransition(R.anim.in, R.anim.out);
                    } catch (Exception e) {
                        Log.v(TAG, e.getMessage());
                    }

                }

                @Override
                public void onEditOrder(String orderNo, String cutoff_time, String categoryType) {
                    AdapterOnClick.super.onEditOrder(orderNo, cutoff_time, categoryType);
                    try {
                        if (Common_Class.isNullOrEmpty(cutoff_time)) {
                            common_class.showMsg(TodayPrimOrdActivity.this, "Time UP...");
                        } else {
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            Date d1 = sdf.parse(Common_Class.GetTime());
                            Date d2 = sdf.parse(cutoff_time);
                            long elapsed = d2.getTime() - d1.getTime();
                            if (elapsed >= 0) {
                                sharedCommonPref.clear_pref(Constants.LOC_PRIMARY_DATA);
                                Intent intent = new Intent(TodayPrimOrdActivity.this, PrimaryOrderActivity.class);
                                intent.putExtra(Constants.ORDER_ID, orderNo);
                                intent.putExtra(Constants.CATEGORY_TYPE, categoryType);
                                intent.putExtra("Mode", "order_view");
                                Shared_Common_Pref.TransSlNo = orderNo;
                                startActivity(intent);
                                overridePendingTransition(R.anim.in, R.anim.out);

                            } else {
                                common_class.showMsg(TodayPrimOrdActivity.this, "Time UP...");
                            }
                        }
                    } catch (Exception e) {
                        Log.v("Edit Order ", e.getMessage());
                    }
                }
            });

            binding.recyclerViewInvoice.setAdapter(mReportViewAdapter);

            double totAmt = 0;
            for (int i = 0; i < filterArr.length(); i++) {
                JSONObject obj = filterArr.getJSONObject(i);
                totAmt += Double.parseDouble(obj.getString("Order_Value"));
            }

            if (totAmt > 0) {
                findViewById(R.id.cvTotParent).setVisibility(View.VISIBLE);
                binding.txtGrandTotal.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(totAmt));
            } else
                findViewById(R.id.cvTotParent).setVisibility(View.GONE);
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.in, R.anim.out);
            return true;
        }
        return false;
    }
}