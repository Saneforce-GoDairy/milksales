package com.saneforce.godairy.Status_Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saneforce.godairy.Activity_Hap.Dashboard;
import com.saneforce.godairy.Activity_Hap.ERT;
import com.saneforce.godairy.Activity_Hap.Help_Activity;
import com.saneforce.godairy.Activity_Hap.PayslipFtp;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Adapter.CommonAdapter;
import com.saneforce.godairy.SFA_Model_Class.CommonModel;
import com.saneforce.godairy.Status_Adapter.WeekOff_Status_Adapter;
import com.saneforce.godairy.Status_Model_Class.WeekOff_Status_Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeekOff_Status_Activity extends AppCompatActivity {
    List<WeekOff_Status_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView;
    Type userType;
    Common_Class common_class;
    Intent i;
    String AMOD = "0";

    TextView tvStartDate, tvEndDate;
    ImageView showMore;
    Context context = this;
    ArrayList<CommonModel> list;
    DatePickerDialog fromDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_off__status_);

        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });
        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        showMore = findViewById(R.id.showMore);

        approvalList = new ArrayList<>();

        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PayslipFtp.class));
            }
        });


        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));

            }
        });
        recyclerView = findViewById(R.id.ondutystatus);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        i = getIntent();
        AMOD = i.getExtras().getString("AMod");
        gson = new Gson();

        list = new ArrayList<>();
        list.add(new CommonModel("Last 7 days"));
        list.add(new CommonModel("Last 30 days"));

        showMore.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            CommonAdapter adapter = new CommonAdapter(context, list);
            recyclerView1.setAdapter(adapter);
            TextView title = view.findViewById(R.id.title);
            title.setText("Select Quick Dates");
            TextView close = view.findViewById(R.id.close);
            AlertDialog dialog = builder.create();
            adapter.setItemSelect(name -> {
                Calendar calendar = Calendar.getInstance();
                if (name.contains("7")) {
                    calendar.add(Calendar.DATE, -7);
                    Date last7thDate = calendar.getTime();
                    tvStartDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(last7thDate));
                    tvEndDate.setText(Common_Class.GetDatewothouttime());
                } else if (name.contains("30")) {
                    calendar.add(Calendar.DATE, -30);
                    Date last7thDate = calendar.getTime();
                    tvStartDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(last7thDate));
                    tvEndDate.setText(Common_Class.GetDatewothouttime());
                }
                dialog.dismiss();
                getWeekOffStatus();
            });
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });

        tvStartDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        tvEndDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        tvStartDate.setOnClickListener(v -> showDatePickerDialog(0, tvStartDate));
        tvEndDate.setOnClickListener(v -> showDatePickerDialog(1, tvEndDate));
        getWeekOffStatus();
    }

    private void getWeekOffStatus() {
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_week_off_status");
        params.put("sfCode", Shared_Common_Pref.Sf_Code);
        params.put("tvStartDate", tvStartDate.getText().toString().trim());
        params.put("tvEndDate", tvEndDate.getText().toString().trim());
        Common_Class.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONArray array = jsonObject.getJSONArray("response");
                    approvalList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject myObject = array.getJSONObject(i);
                        String WrkTyp = myObject.getString("WrkTyp");
                        String wkDate = myObject.getString("wkDate");
                        String wkDt = myObject.getString("wkDt");
                        String DtNm = myObject.getString("DtNm");
                        String Rmks = myObject.getString("Rmks");
                        String sbmtOn = myObject.getString("sbmtOn");
                        approvalList.add(new WeekOff_Status_Model(WrkTyp, wkDate, wkDt, DtNm, Rmks, sbmtOn));
                    }
                    recyclerView.setAdapter(new WeekOff_Status_Adapter(approvalList, R.layout.weeklyoff_status_listitem, getApplicationContext(), AMOD));
                } catch (Exception e) {
                    Log.e("makeApiCall", e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(String error) {
            }
        });
    }

    private void showDatePickerDialog(int pos, TextView tv) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            int month = monthOfYear + 1;
            String date = ("" + year + "-" + month + "-" + dayOfMonth);
            if (common_class.checkDates(pos == 0 ? date : tvStartDate.getText().toString(), pos == 1 ? date : tvEndDate.getText().toString(), WeekOff_Status_Activity.this)) {
                tv.setText(date);
                getWeekOffStatus();
            } else {
                Toast.makeText(context, "Please select valid date", Toast.LENGTH_SHORT).show();
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }
}