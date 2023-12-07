package com.saneforce.godairy.SFA_Activity;

import static com.saneforce.godairy.Common_Class.Common_Class.addquote;
import static com.saneforce.godairy.Common_Class.Constants.GroupFilter;
import static com.saneforce.godairy.Common_Class.Constants.Rout_List;
import static com.saneforce.godairy.SFA_Activity.HAPApp.CurrencySymbol;

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
import android.widget.Button;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Common_Model;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AdapterOnClick;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.Master_Interface;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.Interface.onListItemClick;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.PrimaryOrder_History_Adapter;
import com.saneforce.godairy.SFA_Adapter.RyclBrandListItemAdb;
import com.saneforce.godairy.databinding.ActivityTodayPrimorderHistoryBinding;
import com.saneforce.godairy.procurement.AITFormActivity;
import com.saneforce.godairy.procurement.AgronomistFormActivity;
import com.saneforce.godairy.procurement.ColletionCenterLocationActivity;
import com.saneforce.godairy.procurement.ExistingAgentVisitActivity;
import com.saneforce.godairy.procurement.MaintanenceIssuesFormActivity;
import com.saneforce.godairy.procurement.ProcurementAssetActivity;
import com.saneforce.godairy.procurement.QualityFormActivity;
import com.saneforce.godairy.procurement.VeterinaryDoctorsFormActivity;

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
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

public class TodayPrimOrdActivity extends AppCompatActivity implements Master_Interface, View.OnClickListener, UpdateResponseUI {
    private ActivityTodayPrimorderHistoryBinding binding;
    private final Context context = this;
    TextView tvStartDate, tvEndDate, distributor_text, route_text, tvGrandTot;;
    Common_Class common_class;
    PrimaryOrder_History_Adapter mReportViewAdapter;
    RecyclerView invoicerecyclerview, rvFilter;
    Shared_Common_Pref sharedCommonPref;
    public static TodayPrimOrdActivity mTdPriAct;
    public static String stDate = "", endDate = "";
    DatePickerDialog fromDatePickerDialog;
    String date = "";
    LinearLayout llDistributor, btnCmbRoute;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    Common_Model Model_Pojo;
    String groupType = "All";
    private Dialog noOrderDialog;
    private Dialog purposeOfVisitDialog;
    String mSelectedNoOrderReason;
    EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            binding = ActivityTodayPrimorderHistoryBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            mTdPriAct = this;
            sharedCommonPref = new Shared_Common_Pref(TodayPrimOrdActivity.this);
            common_class = new Common_Class(this);

            common_class.getDb_310Data(Constants.GroupFilter, this);

            tvStartDate = findViewById(R.id.tvStartDate);
            tvEndDate = findViewById(R.id.tvEndDate);
            invoicerecyclerview = (RecyclerView) findViewById(R.id.invoicerecyclerview);
            rvFilter = findViewById(R.id.rvPrimOrdFilter);
            distributor_text = findViewById(R.id.distributor_text);
            llDistributor = findViewById(R.id.llDistributor);
            btnCmbRoute = findViewById(R.id.btnCmbRoute);
            route_text = findViewById(R.id.route_text);
            tvGrandTot = findViewById(R.id.txtTotAmt);


            tvStartDate.setOnClickListener(this);
            tvEndDate.setOnClickListener(this);
            llDistributor.setOnClickListener(this);
            btnCmbRoute.setOnClickListener(this);

            stDate = Common_Class.GetDatewothouttime();
            endDate = Common_Class.GetDatewothouttime();
            tvStartDate.setText(stDate);
            tvEndDate.setText(endDate);

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);
            common_class.getDataFromApi(Constants.GetTodayPrimaryOrder_List, this, false);

            int didts = 0;
            if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
                llDistributor.setEnabled(false);
                btnCmbRoute.setVisibility(View.GONE);
                findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);
                distributor_text.setText("HI! " + sharedCommonPref.getvalue(Constants.Distributor_name, ""));
                String nMstring = sharedCommonPref.getvalue(Constants.Distributor_name, "");
                 didts = Integer.parseInt(nMstring.replaceAll("[\\D]", ""));
            } else {
                if (!sharedCommonPref.getvalue(Constants.Distributor_Id).equals("")) {
                    common_class.getDb_310Data(Rout_List, this);
                    distributor_text.setText(/*"Hi! " +*/ sharedCommonPref.getvalue(Constants.Distributor_name, ""));
                    String nMstring = sharedCommonPref.getvalue(Constants.Distributor_name, "");
                     didts = Integer.parseInt(nMstring.replaceAll("[\\D]", ""));
                } else {
                    btnCmbRoute.setVisibility(View.GONE);
                }
            }

            int dd = didts;

        } catch (Exception e) {

        }

        binding.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PrimaryOrderActivity.class);
                intent.putExtra("Mode", "order_view");
                startActivity(intent);
            }
        });

        binding.noOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noOrderDialog = new Dialog(context);
                noOrderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                noOrderDialog.setContentView(R.layout.primary_order_no_dialog);
                noOrderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                noOrderDialog.setCancelable(false);
                noOrderDialog.show();

                RelativeLayout close = noOrderDialog.findViewById(R.id.close);
                RelativeLayout submitBtn = noOrderDialog.findViewById(R.id.submit_button);
                nameEditText = noOrderDialog.findViewById(R.id.name_edit_text);
                LinearLayout purposeOfVisit = noOrderDialog.findViewById(R.id.purpose_of_visit);

                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // todo
                    }
                });

                purposeOfVisit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ArrayList purposeOfVisit = new ArrayList(Arrays.asList(
                                "Retail work for volume increase",
                                "Other brand conversion",
                                "Complaint Resolving",
                                "Merchandising",
                                "Sampling" ,
                                "Event participation",
                                "Training" ,
                                "Collection Center Location"
                        ));

                      //  noOrderDialog.dismiss();

                        purposeOfVisitDialog = new Dialog(context);
                        purposeOfVisitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        purposeOfVisitDialog.setContentView(R.layout.model_purpose_of_visit_dialog);
                        purposeOfVisitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        purposeOfVisitDialog.setCancelable(false);
                        purposeOfVisitDialog.show();

                        RecyclerView recyclerView = purposeOfVisitDialog.findViewById(R.id.recycler_view);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setItemViewCacheSize(20);
                        PurposeOfVisitAdapter purposeOfVisitAdapter = new PurposeOfVisitAdapter(context, purposeOfVisit);
                        recyclerView.setAdapter(purposeOfVisitAdapter);
                    }
                });

                submitBtn.setOnClickListener(v12 -> {
                    String mChannealName = nameEditText.getText().toString().trim();

                    if ("". equals(mChannealName)){
                        nameEditText.requestFocus();
                        return;
                    }
                    ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);

                    String userInfo = "MyPrefs";
                    SharedPreferences UserDetails = getSharedPreferences(userInfo, Context.MODE_PRIVATE);

                    String mDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String mTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    String mTimeDate  = mDate +" "+mTime;

                    String mSFCode =UserDetails.getString("Sfcode","");


                    Call<ResponseBody> call = apiInterface.primaryNoOrderReasonSubmit("save/no_order_reason", mSelectedNoOrderReason, mSFCode, mTimeDate);

                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                assert response.body() != null;
                                String res = null;
                                try {
                                    res = response.body().string();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                // Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "sucess", Toast.LENGTH_SHORT).show();
                                noOrderDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            noOrderDialog.dismiss();
                        }
                    });

                });
                close.setOnClickListener(v1 -> noOrderDialog.dismiss());
            }
        });
    }

    public class PurposeOfVisitAdapter extends RecyclerView.Adapter<PurposeOfVisitAdapter.ViewHolder> {
        ArrayList exploreImage, exploreName;
        Context context;

        public PurposeOfVisitAdapter(Context context, ArrayList courseName) {
            this.context = context;
            this.exploreName = courseName;
        }

        @NonNull
        @Override
        public PurposeOfVisitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_purpose_of_visit_item, parent, false);
            PurposeOfVisitAdapter.ViewHolder viewHolder = new PurposeOfVisitAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PurposeOfVisitAdapter.ViewHolder holder, int position) {
            holder.text.setText((String) exploreName.get(position));

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
                route_text.setText("");
                sharedCommonPref.save(Constants.Route_name, "");
                sharedCommonPref.save(Constants.Route_Id, "");
                // btnCmbRoute.setVisibility(View.VISIBLE);
                distributor_text.setText(myDataset.get(position).getName());
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
                //common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);

                break;
            case 3:
                route_text.setText(myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Route_name, myDataset.get(position).getName());
                sharedCommonPref.save(Constants.Route_Id, myDataset.get(position).getId());
                //common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);

                break;
        }
    }

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
        fromDatePickerDialog = new DatePickerDialog(TodayPrimOrdActivity.this, new DatePickerDialog.OnDateSetListener() {

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
            route_text.setText(FRoute_Master.get(0).getName());
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
                            Model_Pojo = new Common_Model(id, name, flag);
                            Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                            FRoute_Master.add(Model_Pojo);

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

                        rvFilter.setAdapter(new RyclBrandListItemAdb(arr, this, new onListItemClick() {

                            @Override

                            public void onItemClick(JSONObject item) {
                                try {
                                    groupType = item.getString("name");
                                    setHistoryAdapter(new JSONArray(sharedCommonPref.getvalue(Constants.GetTodayPrimaryOrder_List)));

                                } catch (Exception e) {
                                    Log.v("primHist:", e.getMessage());
                                }
                            }

                        }));

                        break;

                    case Constants.GetTodayPrimaryOrder_List:
                        Log.v("TodayPrim", apiDataResponse);
                        sharedCommonPref.save(Constants.GetTodayPrimaryOrder_List, apiDataResponse);
                        setHistoryAdapter(new JSONArray(apiDataResponse));


                }

            }
        } catch (Exception e) {
            Log.v("Invoice History: ", e.getMessage());

        }
    }


    void setHistoryAdapter(JSONArray arr) {
        try {
            JSONArray filterArr = new JSONArray();
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
                        intent.putExtra("gstn",sharedCommonPref.getvalue(Constants.DistributorGst));
                        startActivity(intent);
                        overridePendingTransition(R.anim.in, R.anim.out);
                    } catch (Exception e) {
                        Log.v("primary:", e.getMessage());
                    }

                }
            });
            invoicerecyclerview.setAdapter(mReportViewAdapter);

            double totAmt = 0;
            for (int i = 0; i < filterArr.length(); i++) {
                JSONObject obj = filterArr.getJSONObject(i);
                totAmt += Double.parseDouble(obj.getString("Order_Value"));
            }

            if (totAmt > 0) {
                findViewById(R.id.cvTotParent).setVisibility(View.VISIBLE);
                tvGrandTot.setText(CurrencySymbol+" " + new DecimalFormat("##0.00").format(totAmt));
            } else
                findViewById(R.id.cvTotParent).setVisibility(View.GONE);
        } catch (Exception e) {
            Log.v("primHistAda:", e.getMessage());
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (sharedCommonPref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE))
//                finish();
//            else
//                common_class.CommonIntentwithFinish(FPPrimaryOrderActivity.class);
//

            finish();
            overridePendingTransition(R.anim.in, R.anim.out);
            return true;
        }
        return false;
    }

    public void updateData(String orderNo, String cutoff_time, String categoryType) {
        try {
            if (Common_Class.isNullOrEmpty(cutoff_time)) {
                common_class.showMsg(this, "Time UP...");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date d1 = sdf.parse(Common_Class.GetTime());
                Date d2 = sdf.parse(cutoff_time);
                long elapsed = d2.getTime() - d1.getTime();
                if (elapsed >= 0) {
                    sharedCommonPref.clear_pref(Constants.LOC_PRIMARY_DATA);
                    Intent intent = new Intent(this, PrimaryOrderActivity.class);
                    intent.putExtra(Constants.ORDER_ID, orderNo);
                    intent.putExtra(Constants.CATEGORY_TYPE, categoryType);
                    intent.putExtra("Mode", "order_view");
                    Shared_Common_Pref.TransSlNo = orderNo;
                    startActivity(intent);
                    overridePendingTransition(R.anim.in, R.anim.out);

                } else {
                    common_class.showMsg(this, "Time UP...");
                }
            }
        } catch (Exception e) {
            Log.v("TDPrimActivity:Edit:", e.getMessage());
        }

    }
}