package com.saneforce.godairy.Activity_Hap;

import static com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE;
import static com.saneforce.godairy.SFA_Activity.HAPApp.printUsrLog;
import static com.saneforce.godairy.common.AppConstants.INTENT_PROCUREMENT_MODE;
import static com.saneforce.godairy.common.AppConstants.INTENT_PROCUREMENT_USER_DOC_MODE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Activity.AllowanceActivity;
import com.saneforce.godairy.Activity.AllowanceActivityTwo;
import com.saneforce.godairy.Activity.TAClaimActivity;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.SFA_Activity.MapDirectionActivity;
import com.saneforce.godairy.common.DatabaseHandler;
import com.saneforce.godairy.common.SANGPSTracker;
import com.saneforce.godairy.databinding.ActivityDashboardBinding;
import com.saneforce.godairy.fragments.GateInOutFragment;
import com.saneforce.godairy.fragments.MonthlyFragment;
import com.saneforce.godairy.fragments.TodayFragment;
import com.saneforce.godairy.procurement.ProcurementHome;
import com.saneforce.godairy.universal.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    private ActivityDashboardBinding binding;
    private final Context context = this;
    public static final String CheckInDetail = "CheckInDetail";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String mypreference = "mypref";
    private String onDuty = "", ClosingDate = "", sSFType = "";
    private Integer ClosingKm = 0;
    private SharedPreferences.Editor editors;
    private SharedPreferences CheckInDetails, UserDetails, sharedpreferences;
    private Common_Class common_class;
    private Shared_Common_Pref shared_common_pref;
    private final com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();
    private DatabaseHandler db;
    private String mProfileUrl;

    private ImageView userImage;

    ArrayList<Integer> exploreImage = new ArrayList<>(Arrays.asList(
            R.drawable.request_status_ic,
            R.drawable.file_invoice_doller_ic,
            R.drawable.users_gear_ic,
            R.drawable.gate_ic,
            R.drawable.gate_out_ic,
            R.drawable.calendar_pjp));

    ArrayList<String> exploreName = new ArrayList<>(Arrays.asList(
            "Request & status",
            "TA & Claim",
            "Activity",
            "Gate IN",
            "Gate OUT",
            "PJP"));
    TextView update_text;
    LinearLayout lnupdate_text;
    private AppUpdateManager appUpdateManager;
    public  static final int APP_UPDATE=100;
    int update_available = 0;
    int version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view  = binding.getRoot();
        setContentView(view);

        onClick2();
        loadFragment();

        db = new DatabaseHandler(context);

        userImage = findViewById(R.id.user_image);

        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Get_MydayPlan(1, "check/mydayplan");
        getHapLocations();
        getHAPWorkTypes();
        shared_common_pref = new Shared_Common_Pref(context);

        // Integer type = (UserDetails.getInt("CheckCount", 0));

        common_class = new Common_Class(context);

        String eMail = UserDetails.getString("email", "");
        String sSFName = UserDetails.getString("SfName", "");
        String SFDesig = UserDetails.getString("SFDesig", "");
        sSFType = UserDetails.getString("Sf_Type", "");
        int OTFlg = UserDetails.getInt("OTFlg", 0);
        String mProfileImage = UserDetails.getString("Profile", "");

        binding.userName.setText(sSFName);
        binding.headQuarters.setText(SFDesig);
        binding.lblEmail.setText(eMail);

        int mCount = 0;

        for (int i = 0; i<mProfileImage.length(); i++){
            if(mProfileImage.charAt(i) != ' ')
                mCount++;
        }

        new Thread(() -> {
            try {
                URLConnection connection = new URL(mProfileImage).openConnection();
                String contentType = connection.getHeaderField("Content-Type");
                boolean image = contentType.startsWith("image/");
                if (image){
                    runOnUiThread(() -> {
                        loadImage(mProfileImage);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        binding.userImage.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
            intent.putExtra("ImageUrl", mProfileImage);
            startActivity(intent);

        });

        ImageView btMyQR = findViewById(R.id.myQR);
        binding.linMydayPlan.setVisibility(View.GONE);

        Log.e("Dashboard", "sSFType :" + sSFType);


        if (sSFType.equals("1")) {
            binding.linMydayPlan.setVisibility(View.VISIBLE);
            binding.linHolidayWorking.setVisibility(View.GONE);
            binding.linCheckIn.setVisibility(View.GONE);
        }

        Button linRequstStaus = (findViewById(R.id.lin_request_status));
        Button linReport = (findViewById(R.id.lin_report));
        Button linOnDuty = (findViewById(R.id.lin_onduty));
        Button linSFA = findViewById(R.id.lin_sfa);

        linSFA.setVisibility(View.GONE);

        linOnDuty.setVisibility(View.GONE);
        if (sSFType.equals("0"))
            linOnDuty.setVisibility(View.VISIBLE);
        else {
            linSFA.setVisibility(View.VISIBLE);
            binding.linRecheckIn.setVisibility(View.VISIBLE);
        }

        if (linOnDuty.getVisibility() == View.VISIBLE) {
            binding.linCheckIn.setVisibility(View.VISIBLE);
            binding.linHolidayWorking.setVisibility(View.VISIBLE);
        } else {
            binding.linCheckIn.setVisibility(View.GONE);
        }

        Button linTaClaim = (findViewById(R.id.lin_ta_claim));
        Button linExtShift = (findViewById(R.id.lin_extenden_shift));
        linExtShift.setVisibility(View.GONE);
        if (OTFlg == 1) linExtShift.setVisibility(View.VISIBLE);
        Button linTourPlan = (findViewById(R.id.lin_tour_plan));
        linTourPlan.setVisibility(View.GONE);
        if (sSFType.equals("1")) linTourPlan.setVisibility(View.VISIBLE);
        Button linExit = (findViewById(R.id.lin_exit));

        if (UserDetails.getInt("CheckCount", 0) <= 0) {
            binding.approval.setVisibility(View.INVISIBLE);
        } else {
            exploreName.add("Approvals");
            exploreImage.add(R.drawable.canteen_scan_ic);
            binding.approval.setVisibility(View.VISIBLE);
        }

        loadExploreGrid();

        btMyQR.setOnClickListener(v -> {
            Intent intent = new Intent(context, CateenToken.class);
            startActivity(intent);
        });
        userImage.setOnClickListener(view1 -> {
            Intent intent = new Intent(context,ProfileActivity.class);
            startActivity(intent);
        });

        update_text = findViewById(R.id.update_available);
        lnupdate_text = findViewById(R.id.updateAvailable);
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        checkUpdates();
        update_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo result) {

                        if (result.updateAvailability() == UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                            try {
                                appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, Dashboard.this, APP_UPDATE);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }else
                            lnupdate_text.setVisibility(View.GONE);
                    }
                });
            }
        });
        binding.linMydayPlan.setOnClickListener(this);
        binding.linCheckIn.setOnClickListener(this);
        linRequstStaus.setOnClickListener(this);
        linReport.setOnClickListener(this);
        linOnDuty.setOnClickListener(this);
        binding.approval.setOnClickListener(this);
        linTaClaim.setOnClickListener(this);
        linExtShift.setOnClickListener(this);
        linTourPlan.setOnClickListener(this);
        binding.linHolidayWorking.setOnClickListener(this);
        linExit.setOnClickListener(this);
        binding.linRecheckIn.setOnClickListener(this);
        linSFA.setOnClickListener(this);
        getcountdetails();
        updateFlxlayout();

        Log.v("wrkType:",shared_common_pref.getvalue("worktype", ""));
        if (shared_common_pref.getvalue("worktype", "").equalsIgnoreCase("43")) {
            binding.linCheckIn.setVisibility(View.GONE);
            binding.linRecheckIn.setVisibility(View.GONE);
        }
    }

    private void checkUpdates() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {

                if (result.updateAvailability() == UPDATE_AVAILABLE ){

                    version = result.availableVersionCode();
                    printUsrLog("Version", String.valueOf(version));

                    lnupdate_text.setVisibility(View.VISIBLE);

                    /*if (Constant.getInstance().getSetup(StringConstants.IS_FORCE_UPDATE, 0, new DBController(MainActivity.this)) == version) {
                        try {
                            if(result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                                appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, Dashboard.this, APP_UPDATE);
                            }else{
                                appUpdateManager.startUpdateFlowForResult(result, AppUpdateType.FLEXIBLE, Dashboard.this, APP_UPDATE);
                            }
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    } else {
                        update_available = 1;
                    }
                    */
                }else
                    lnupdate_text.setVisibility(View.GONE);
            }
        });
    }
    private void loadImage(String mProfileImage) {
        Glide.with(this.context)
                .load(mProfileImage)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.userImage);
    }

    private void loadExploreGrid() {
        binding.exploreMore.setLayoutManager(new GridLayoutManager(context, 3));
        binding.exploreMore.setHasFixedSize(true);
        binding.exploreMore.setItemViewCacheSize(20);
        Adapter adapter6 = new Adapter(context, exploreImage, exploreName);
        binding.exploreMore.setAdapter(adapter6);
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        ArrayList exploreImage, exploreName;
        Context context;

        public Adapter(Context context, ArrayList courseImg, ArrayList courseName) {
            this.context = context;
            this.exploreImage = courseImg;
            this.exploreName = courseName;
        }

        @NonNull
        @Override
        public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_dash_explore_more_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
            int res = (int) exploreImage.get(position);
            holder.images.setImageResource(res);
            holder.text.setText((String) exploreName.get(position));

            holder.layout.setOnClickListener(v -> {
                switch (position){
                    case 0:
                        startActivity(new Intent(context, Leave_Dashboard.class));
                        break;

                    case 1:
                        Shared_Common_Pref.TravelAllowance = 0;
                        startActivity(new Intent(context, TAClaimActivity.class));
                        break;

                    case 2:
                        if(getIntent().hasExtra("Mode")){
                            String mMode = getIntent().getStringExtra("Mode");

                            if (mMode.equals(INTENT_PROCUREMENT_MODE)){
                                Intent intent = new Intent(context, ProcurementHome.class);
                                intent.putExtra("proc_user", getIntent().getStringExtra("proc_user"));
                                startActivity(intent);
                                return;
                            }
                        }
                        startActivity(new Intent(context, SFA_Activity.class));
                        break;

                    case 3:
                        Intent intent = new Intent(context, QRCodeScanner.class);
                        intent.putExtra("Name", "GateIn");
                        startActivity(intent);
                        break;

                    case 4:
                        Intent intent1 = new Intent(context, QRCodeScanner.class);
                        intent1.putExtra("Name", "GateOut");
                        startActivity(intent1);
                        break;

                    case 5:
                        Shared_Common_Pref.Tp_Approvalflag = "0";
                        Intent intent5 = new Intent(context, Tp_Calander.class);
                        startActivity(intent5);
                        break;

                    case 6:
                        Shared_Common_Pref.TravelAllowance = 1;
                        startActivity(new Intent(context, Approvals.class));
                        break;
                }
            });
        }

        @Override
        public int getItemCount() {
            return exploreImage.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView images;
            TextView text;
            RelativeLayout layout;

            public ViewHolder(View view) {
                super(view);
                images = view.findViewById(R.id.image);
                text = view.findViewById(R.id.name);
                layout = view.findViewById(R.id.layout);
            }
        }
    }

    private void onClick2() {
        binding.linCheckIn.setOnClickListener(v -> {
            int val = UserDetails.getInt("checkRadius", 0);
            Log.v("CHECKIN:", "" + val);
            if (/*sSFType.equals("0")*/UserDetails.getInt("checkRadius", 0) == 1) {
                String[] latlongs = UserDetails.getString("HOLocation", "").split(":");
                Intent intent = new Intent(context, MapDirectionActivity.class);
                intent.putExtra(Constants.DEST_LAT, latlongs[0]);
                intent.putExtra(Constants.DEST_LNG, latlongs[1]);
                intent.putExtra(Constants.DEST_NAME, "HOLocation");
                intent.putExtra(Constants.NEW_OUTLET, "checkin");
                startActivity(intent);
            } else {
                String ETime = CheckInDetails.getString("CINEnd", "");
                if (!ETime.equalsIgnoreCase("")) {
                    String CutOFFDt = CheckInDetails.getString("ShiftCutOff", "0");
                    String SftId = CheckInDetails.getString("Shift_Selected_Id", "0");
                    if (DT.GetCurrDateTime(context).getTime() >= DT.getDate(CutOFFDt).getTime() || SftId.equals("0")) {
                        ETime = "";
                    }
                }
                if (!ETime.equalsIgnoreCase("")) {
                    Intent takePhoto = new Intent(context, CameraxActivity.class);
                    takePhoto.putExtra("Mode", "CIN");
                    takePhoto.putExtra("ShiftId", CheckInDetails.getString("Shift_Selected_Id", ""));
                    takePhoto.putExtra("ShiftName", CheckInDetails.getString("Shift_Name", ""));
                    takePhoto.putExtra("On_Duty_Flag", CheckInDetails.getString("On_Duty_Flag", "0"));
                    takePhoto.putExtra("On_Duty_Flag", CheckInDetails.getString("On_Duty_Flag", "0"));
                    takePhoto.putExtra("ShiftStart", CheckInDetails.getString("ShiftStart", "0"));
                    takePhoto.putExtra("ShiftEnd", CheckInDetails.getString("ShiftEnd", "0"));
                    takePhoto.putExtra("ShiftCutOff", CheckInDetails.getString("ShiftCutOff", "0"));
                    startActivity(takePhoto);
                } else {
                    Intent i = new Intent(context, Checkin.class);
                    startActivity(i);
                }

            }
        });

        binding.linMydayPlan.setOnClickListener(v -> {
            if (ClosingKm == 1) {
                Intent closingIntet = new Intent(context, AllowanceActivityTwo.class);
                closingIntet.putExtra("Cls_con", "cls");
                closingIntet.putExtra("Cls_dte", ClosingDate);
                startActivity(closingIntet);
                finish();
            } else {
                startActivity(new Intent(context, Mydayplan_Activity.class));
            }
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(binding.tabLayout.getTabAt(position)).select();
            }
        });

        binding.leaveRequestStatus.setOnClickListener(v -> startActivity(new Intent(context, Leave_Dashboard.class)));
        binding.taClaim.setOnClickListener(v -> {
            Shared_Common_Pref.TravelAllowance = 0;
            startActivity(new Intent(context, TAClaimActivity.class)); //Travel_Allowance
        });
        binding.sfa.setOnClickListener(v -> {
            startActivity(new Intent(context, SFA_Activity.class));
        });

        binding.gateIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, QRCodeScanner.class);
            intent.putExtra("Name", "GateIn");
            startActivity(intent);
        });

        binding.gateOut.setOnClickListener(v -> {
            Intent intent = new Intent(this, QRCodeScanner.class);
            intent.putExtra("Name", "GateOut");
            startActivity(intent);
        });

        binding.notification.setOnClickListener(v -> Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show());

        binding.logout.setOnClickListener(v -> {
                    common_class.clearLocData(Dashboard.this);
                    shared_common_pref.clear_pref(Constants.DB_TWO_GET_MREPORTS);
                    shared_common_pref.clear_pref(Constants.DB_TWO_GET_DYREPORTS);
                    shared_common_pref.clear_pref(Constants.DB_TWO_GET_NOTIFY);
                    shared_common_pref.clear_pref(Constants.LOGIN_DATA);
                    finishAffinity();
                    Intent Dashboard = new Intent(context, Login.class);
                    startActivity(Dashboard);
        });

        binding.pjp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shared_Common_Pref.Tp_Approvalflag = "0";
                Intent intent = new Intent(context, Tp_Calander.class);
                startActivity(intent);
            }
        });
    }

    private void loadFragment() {
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(this);
        binding.viewPager.setAdapter(myViewPagerAdapter);
    }

    public static class MyViewPagerAdapter  extends FragmentStateAdapter {
        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 1:
                    return new MonthlyFragment();
                case 2:
                    return new GateInOutFragment();
                default:
                    return new TodayFragment();
            }
        }
        @Override
        public int getItemCount() {
            return 3;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_check_in:
                  String[] latlongs = UserDetails.getString("HOLocation", "").split(":");
                     Intent intent = new Intent(context, MapDirectionActivity.class);
                     intent.putExtra(Constants.DEST_LAT, latlongs[0]);
                     intent.putExtra(Constants.DEST_LNG, latlongs[1]);
                     intent.putExtra(Constants.DEST_NAME, "HOLocation");
                     intent.putExtra(Constants.NEW_OUTLET, "checkin");

                     intent.putExtra("Mode", "CIN");
                     intent.putExtra("ShiftId", CheckInDetails.getString("Shift_Selected_Id", ""));
                     intent.putExtra("ShiftName", CheckInDetails.getString("Shift_Name", ""));
                     intent.putExtra("On_Duty_Flag", CheckInDetails.getString("On_Duty_Flag", "0"));
                     intent.putExtra("ShiftStart", CheckInDetails.getString("ShiftStart", "0"));
                     intent.putExtra("ShiftEnd", CheckInDetails.getString("ShiftEnd", "0"));
                     intent.putExtra("ShiftCutOff", CheckInDetails.getString("ShiftCutOff", "0"));
                  startActivity(intent);
                break;

            case R.id.lin_request_status:
                startActivity(new Intent(context, Leave_Dashboard.class));
                break;

            case R.id.lin_ta_claim:
                Shared_Common_Pref.TravelAllowance = 0;
                startActivity(new Intent(context, TAClaimActivity.class)); //Travel_Allowance
                break;

            case R.id.lin_report:
                Intent Dashboard = new Intent(context, Dashboard_Two.class);
                Dashboard.putExtra("Mode", "RPT");
                startActivity(Dashboard);
                break;

            case R.id.approval:
                Shared_Common_Pref.TravelAllowance = 1;
                startActivity(new Intent(context, Approvals.class));
                break;

            case R.id.lin_myday_plan:
                if (ClosingKm == 1) {
                    Intent closingIntet = new Intent(context, AllowanceActivityTwo.class);
                    closingIntet.putExtra("Cls_con", "cls");
                    closingIntet.putExtra("Cls_dte", ClosingDate);
                    startActivity(closingIntet);
                    finish();
                } else {
                    startActivity(new Intent(context, Mydayplan_Activity.class));
                }
                break;

            case R.id.lin_RecheckIn:
                Intent recall = new Intent(context, AllowanceActivity.class);
                recall.putExtra("Recall", "Recall");
                startActivity(recall);
                break;

            case R.id.lin_tour_plan:
                Shared_Common_Pref.Tp_Approvalflag = "0";
                startActivity(new Intent(context, Tp_Month_Select.class));
                break;

            case R.id.lin_holiday_working:
                AlertDialogBox.showDialog(context, HAPApp.Title, "Are you sure want to Check-in with Hoilday Entry", "YES", "NO", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        common_class.CommonIntentwithoutFinishputextra(Checkin.class, "Mode", "holidayentry");
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                break;

            case R.id.lin_onduty:
                Intent oDutyInt = new Intent(context, On_Duty_Activity.class);
                oDutyInt.putExtra("Onduty", onDuty);
                startActivity(oDutyInt);
                break;

            case R.id.lin_sfa:
                startActivity(new Intent(context, SFA_Activity.class));
                break;

            case R.id.lin_exit:
                shared_common_pref.clear_pref(Constants.LOGIN_DATA);
                SharedPreferences.Editor editor = UserDetails.edit();
                editor.putBoolean("Login", false);
                editor.apply();
                CheckInDetails.edit().clear().apply();
                Intent playIntent = new Intent(context, SANGPSTracker.class);
                stopService(playIntent);
                finishAffinity();
                break;

            case R.id.lin_extenden_shift:
                validateExtened("ValidateExtended");
                break;

            default:
                break;
        }
    }

    public void updateFlxlayout() {
        FlexboxLayout flexboxLayout = findViewById(R.id.flxlayut);
        View flxlastChild = null;
        int flg = 0;
        Log.d("TagName_FlexCount", String.valueOf(flexboxLayout.getChildCount()));
        for (int il = 0; il < flexboxLayout.getChildCount(); il++) {
            if (flexboxLayout.getChildAt(il).getVisibility() == View.VISIBLE) {
                flxlastChild = flexboxLayout.getChildAt(il);
                if (flg == 1)
                    flg = 0;
                else
                    flg = 1;
                FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) flxlastChild.getLayoutParams();
                Log.d("TagName", flxlastChild + " - " + lp.getFlexBasisPercent() + "-" + flg);
                lp.setFlexBasisPercent(0.47f);
                flxlastChild.setLayoutParams(lp);
            }
        }
        if (flg == 1) {
            FlexboxLayout.LayoutParams lp = (FlexboxLayout.LayoutParams) flxlastChild.getLayoutParams();
            lp.setFlexBasisPercent(100);
            flxlastChild.setLayoutParams(lp);
        }
    }

    private void validateExtened(String Name) {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", Name);
        QueryString.put("Sf_code", UserDetails.getString("Sfcode", ""));
        QueryString.put("Date", Common_Class.GetDate());
        QueryString.put("divisionCode", UserDetails.getString("Divcode", ""));
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
        mCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String Msg = jsonObject.getString("msg");
                    if (!Msg.equals("")) {
                        AlertDialogBox.showDialog(context, HAPApp.Title, Msg, "OK", "", false, new AlertBox() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }

                            @Override
                            public void NegativeMethod(DialogInterface dialog, int id) {

                            }
                        });
                    } else {
                        AlertDialogBox.showDialog(context, HAPApp.Title, "Do you want to check-in with Extended Shift?", "YES", "NO", false, new AlertBox() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                common_class.CommonIntentwithoutFinishputextra(Checkin.class, "Mode", "extended");
                            }

                            @Override
                            public void NegativeMethod(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        // Toast.makeText(Dashboard.this, "Send To Checkin", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

            }
        });
    }

    public void getHAPWorkTypes() {
        JSONObject jParam = new JSONObject();
        try {
            jParam.put("SF", UserDetails.getString("Sfcode", "")); // MGR0201
            jParam.put("div", UserDetails.getString("Divcode", "")); // 1
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
            service.getDataArrayList("get/worktypes", jParam.toString()).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                    db.deleteMasterData("HAPWorkTypes");
                    db.addMasterData("HAPWorkTypes", response.body());
                }

                @Override
                public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getHapLocations() {
        String commonLeaveType = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> GetHAPLocation = service.GetHAPLocation(UserDetails.getString("Divcode", ""), UserDetails.getString("Sfcode", ""), commonLeaveType);
        GetHAPLocation.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                db.deleteMasterData("HAPLocations");
                db.addMasterData("HAPLocations", response.body());
            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
            }
        });
    }

    private void Get_MydayPlan(int flag, String Name) {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", Name);
        QueryString.put("Sf_code", UserDetails.getString("Sfcode", ""));
        QueryString.put("Date", Common_Class.GetDate());
        QueryString.put("divisionCode", UserDetails.getString("Divcode", ""));
        QueryString.put("desig", "MGR");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, "[]");

        mCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    ClosingKm = Integer.valueOf(jsonObject.getString("CheckEndKM"));
                    ClosingDate = jsonObject.getString("CheckEndDT");
                    /* *********  Missing KM Auto Asking ******* */
                    if (ClosingKm == 1) {
                        Intent closingIntet = new Intent(context, AllowanceActivityTwo.class);
                        closingIntet.putExtra("Cls_con", "cls");
                        closingIntet.putExtra("Cls_dte", ClosingDate);
                        startActivity(closingIntet);
                        finish();
                        return;
                    }

                    onDuty = jsonObject.getString("CheckOnduty");
                    sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                    editors = sharedpreferences.edit();
                    editors.putString("Onduty", onDuty);
                    editors.putString("ShiftDuty", jsonObject.getString("Todaycheckin_Flag"));
                    editors.commit();

                    binding.linCheckIn.setVisibility(View.VISIBLE);
                    binding.linHolidayWorking.setVisibility(View.VISIBLE);
                    if (flag == 1 && sSFType.equals("1")) {
                        JSONArray jsoncc = jsonObject.getJSONArray("Checkdayplan");
                        if (jsoncc.length() > 0) {
                            if (jsoncc.getJSONObject(0).getInt("Cnt") < 1) {
                                Intent intent = new Intent(context, AllowanceActivity.class);
                                intent.putExtra("My_Day_Plan", "One");
                                startActivity(intent);
                            } else {
                                binding.linMydayPlan.setVisibility(View.GONE);
                                if (jsoncc.getJSONObject(0).getString("wtype").equalsIgnoreCase("43")) {
                                    binding.linCheckIn.setVisibility(View.GONE);
                                    binding.linRecheckIn.setVisibility(View.GONE);

                                } else {
                                    binding.linCheckIn.setVisibility(View.VISIBLE);
                                }
                                binding.linHolidayWorking.setVisibility(View.VISIBLE);
                                updateFlxlayout();
                            }
                        } else {
                            binding.linCheckIn.setVisibility(View.GONE);
                            binding.linHolidayWorking.setVisibility(View.GONE);
                            binding.linMydayPlan.setVisibility(View.VISIBLE);
                            updateFlxlayout();
                        }
                    } else {
                        String Msg = jsonObject.getString("msg");
                        if (!Msg.equals("")) {
                            AlertDialogBox.showDialog(context, HAPApp.Title, Msg, "OK", "", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {

                                }
                            });
                        } else {
                            AlertDialogBox.showDialog(context, HAPApp.Title, Msg, "YES", "NO", false, new AlertBox() {
                                @Override
                                public void PositiveMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    common_class.CommonIntentwithoutFinishputextra(Checkin.class, "Mode", "extended");
                                }

                                @Override
                                public void NegativeMethod(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            // Toast.makeText(Dashboard.this, "Send To Checkin", Toast.LENGTH_SHORT).show();
                        }
                    }
                    updateFlxlayout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("MDPError", Objects.requireNonNull(t.getMessage()));
            }
        });
    }


    public void getcountdetails() {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "ViewAllCount");
        QueryString.put("sfCode", UserDetails.getString("Sfcode", ""));
        QueryString.put("State_Code", UserDetails.getString("State_Code", ""));
        QueryString.put("divisionCode", UserDetails.getString("Divcode", ""));
        QueryString.put("rSF", UserDetails.getString("Sfcode", ""));
        QueryString.put("desig", "MGR");
        String commonworktype = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, commonworktype);

        mCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    Log.e("TOTAl_COUNT", String.valueOf(Integer.parseInt(jsonObject.getString("leave")) + Integer.parseInt(jsonObject.getString("Permission")) + Integer.parseInt(jsonObject.getString("vwOnduty")) + Integer.parseInt(jsonObject.getString("vwmissedpunch")) + Integer.parseInt(jsonObject.getString("TountPlanCount")) + Integer.parseInt(jsonObject.getString("vwExtended"))));
                    Shared_Common_Pref.TotalCountApproval = jsonObject.getInt("leave") + jsonObject.getInt("Permission") +
                            jsonObject.getInt("vwOnduty") + jsonObject.getInt("vwmissedpunch") +
                            jsonObject.getInt("vwExtended") + jsonObject.getInt("TountPlanCount") +
                            jsonObject.getInt("FlightAppr") +
                            jsonObject.getInt("HolidayCount") + jsonObject.getInt("DeviationC") +
                            jsonObject.getInt("CancelLeave") + jsonObject.getInt("ExpList");
                    binding.approvalcount.setText(String.valueOf(Shared_Common_Pref.TotalCountApproval));
                    binding.approvalcount.setVisibility(View.GONE);
                    if (Shared_Common_Pref.TotalCountApproval > 0)
                        binding.approvalcount.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                common_class.ProgressdialogShow(2, "");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        if (CheckIn) {
            Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
            Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
            Shared_Common_Pref.Reporting_Sf_Code = UserDetails.getString("Reporting_To_SF", "");
            Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
            Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");
            Shared_Common_Pref.SF_Mobile = UserDetails.getString("SF_Mobile", "");

            String ActStarted = shared_common_pref.getvalue("ActivityStart");
            if (ActStarted.equalsIgnoreCase("true")) {
                Intent aIntent;
                String sDeptType = UserDetails.getString("DeptType", "");
                if (sDeptType.equalsIgnoreCase("1")) {
                    //   aIntent = new Intent(Dashboard.this, ProcurementDashboardActivity.class);
                    aIntent = (new Intent(getApplicationContext(), SFA_Activity.class));

                } else {
                    Shared_Common_Pref.Sync_Flag = "0";
                    aIntent = new Intent(context, SFA_Activity.class);
                }
                startActivity(aIntent);
                finish();
            } else {
                Intent Dashboard = new Intent(context, Dashboard_Two.class);
                Dashboard.putExtra("Mode", "CIN");
                startActivity(Dashboard);
                finish();
            }
        }
    }
}

//        binding.menuBar.setOnClickListener(v -> {
//            PopupMenu popup = new PopupMenu(context, binding.menuBar);
//            popup.inflate(R.menu.month_plan);
//            popup.setOnMenuItemClickListener(item -> {
//                switch (item.getItemId()) {
//                    case R.id.tour_plan:
//                        Intent intent = new Intent(context, Tp_Month_Select.class);
//                        startActivity(intent);
//                        break;
//
//                    case R.id.logout:
//                        common_class.clearLocData(Dashboard.this);
//                        shared_common_pref.clear_pref(Constants.DB_TWO_GET_MREPORTS);
//                        shared_common_pref.clear_pref(Constants.DB_TWO_GET_DYREPORTS);
//                        shared_common_pref.clear_pref(Constants.DB_TWO_GET_NOTIFY);
//                        shared_common_pref.clear_pref(Constants.LOGIN_DATA);
//                        finishAffinity();
//
//                        Intent Dashboard = new Intent(context, Login.class);
//                        startActivity(Dashboard);
//                        break;
//                }
//                return false;
//            });
//            popup.show();
//        });
