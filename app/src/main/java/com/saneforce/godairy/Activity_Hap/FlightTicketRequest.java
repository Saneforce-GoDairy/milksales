package com.saneforce.godairy.Activity_Hap;

import static com.saneforce.godairy.Activity_Hap.Login.CheckInDetail;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Common_Class.AlertDialogBox;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.AlertBox;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.LocationEvents;
import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.HAPApp;
import com.saneforce.godairy.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightTicketRequest extends AppCompatActivity {
    RadioGroup radioGrp;
    RadioButton radioOne, radioRound, radoMor, radoEve, retradoMor, retradoEve;
    LinearLayout LinearReturn, LinearHome, TrvlrList;
    EditText FrmPlc, ToPlc, retFrmPlc, retToPlc, edtDepature, retedtDepature;
    TextView Name, addTrvlr;
    String tominYear = "", tominMonth = "", tominDay = "", maxTWoDate = "", SFName = "", TrvTyp = "ONE";
    SharedPreferences CheckInDetails, UserDetails, sharedpreferences;
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";
    CircularProgressButton buttonSave;
    JSONArray jTrvDets;
    Location clocation = null;
    final Handler handler = new Handler();
    ApiInterface apiInterface;
    String departureDt = "",dtReturn="";
    com.saneforce.godairy.Activity_Hap.Common_Class DT = new com.saneforce.godairy.Activity_Hap.Common_Class();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_ticket_request);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        sharedpreferences = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        radioGrp = findViewById(R.id.radio_ticket);
        radioOne = findViewById(R.id.radio_oneway);
        radioRound = findViewById(R.id.radio_twoway);

        FrmPlc = findViewById(R.id.from_place);
        ToPlc = findViewById(R.id.to_place);
        edtDepature = findViewById(R.id.edt_dep);
        radoMor = findViewById(R.id.radio_depMor);
        radoEve = findViewById(R.id.radio_depEve);

        retFrmPlc = findViewById(R.id.retfrom_place);
        retToPlc = findViewById(R.id.retto_place);
        retedtDepature = findViewById(R.id.edt_return);
        retradoMor = findViewById(R.id.radio_retdepMor);
        retradoEve = findViewById(R.id.radio_retdepEve);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        LinearReturn = findViewById(R.id.lin_return);
        TrvlrList = findViewById(R.id.TrvlrList);
        addTrvlr = findViewById(R.id.addTrvlr);
        LinearHome = findViewById(R.id.lin_name);
        Name = findViewById(R.id.txt_name);
        radioGrp.clearCheck();
        radioOne.setChecked(true);
        LinearReturn.setVisibility(View.GONE);
        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                Name.setText(Shared_Common_Pref.Sf_Name);
                if (null != rb && checkedId > -1) {
                    LinearHome.setVisibility(View.VISIBLE);
                    if (rb.getText().toString().equalsIgnoreCase("One way")) {
                        LinearReturn.setVisibility(View.GONE);
                    } else {
                        LinearReturn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        buttonSave = findViewById(R.id.save_button);
//        ImageView backView = findViewById(R.id.imag_back);
//        backView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnBackPressedDispatcher.onBackPressed();
//            }
//        });


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
                openHome();
            }
        });
        addTraveller();
        addTrvlr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTraveller();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSave.startAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!validate()) {
                            ResetSubmitBtn(0, buttonSave);
                            return;
                        }
                        AlertDialogBox.showDialog(FlightTicketRequest.this, HAPApp.Title, String.valueOf(Html.fromHtml("Do You Submit Flight Booking Request.")), "Yes", "No", false, new AlertBox() {
                            @Override
                            public void PositiveMethod(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                if (clocation != null) {
                                    submitData();
                                } else {
                                    new LocationFinder(getApplication(), new LocationEvents() {
                                        @Override
                                        public void OnLocationRecived(Location location) {
                                            clocation = location;
                                            submitData();
                                        }
                                    });
                                }

                            }

                            @Override
                            public void NegativeMethod(DialogInterface dialog, int id) {
                                ResetSubmitBtn(0, buttonSave);
                                dialog.dismiss();
                            }
                        });

                    }
                }, 100);
            }
        });
        new LocationFinder(getApplication(), new LocationEvents() {
            @Override
            public void OnLocationRecived(Location location) {
                clocation = location;
            }
        });
    }

    private void addTraveller() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 15, 15, 15);
        final View rowView = inflater.inflate(R.layout.travelleritem, null);
        TrvlrList.addView(rowView, layoutParams);
        ImageView btnDel = rowView.findViewById(R.id.btnDelete);
        RadioGroup radHAPOTH = rowView.findViewById(R.id.radio_HAPOth);
        LinearLayout linGetEmpId = rowView.findViewById(R.id.linGetEmpId);
        Button btnGetEmp = rowView.findViewById(R.id.btnGetEmp);

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveTraveller(v);
            }
        });
        btnGetEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetEmpDetails(v);
            }
        });
        radHAPOTH.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    linGetEmpId.setVisibility(View.GONE);
                    if (rb.getText().toString().equalsIgnoreCase("HAP")) {
                        linGetEmpId.setVisibility(View.VISIBLE);
                    } else {
                        linGetEmpId.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void RemoveTraveller(View v) {
        View pv = (View) v.getParent().getParent().getParent();
        TrvlrList.removeView(pv);
    }

    public void onGetEmpDetails(View v) {
        EditText edtGEmpid;
        View pv = (View) v.getParent().getParent();
        edtGEmpid = pv.findViewById(R.id.edtGEmpid);
        String sEmpID = String.valueOf(edtGEmpid.getText());


        Call<JsonArray> Callto = apiInterface.getDataArrayListA("get/EmpByID",
                UserDetails.getString("Divcode", ""),
                UserDetails.getString("Sfcode", ""), sEmpID, "", "", null);
        Callto.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray res = response.body();
                if (res.size() < 1) {
                    Toast.makeText(getApplicationContext(), "Emp Code Not Found !", Toast.LENGTH_LONG).show();
                    return;
                }


                JsonObject EmpDet = res.get(0).getAsJsonObject();
                if (EmpDet.has("Msg")) {
                    if (!EmpDet.get("Msg").getAsString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), EmpDet.get("Msg").getAsString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                EditText txtTrvName, txtTrvDept, txtTrvMob;

                txtTrvName = pv.findViewById(R.id.txTrvName);
                txtTrvDept = pv.findViewById(R.id.txCmpName);
                txtTrvMob = pv.findViewById(R.id.txTrvMobile);

                txtTrvName.setText(EmpDet.get("Name").getAsString());
                //txtJNDesig.setText(EmpDet.get("Desig").getAsString());
                txtTrvDept.setText(EmpDet.get("DeptName").getAsString());
                //txtJNHQ.setText(EmpDet.get("HQ").getAsString());
                txtTrvMob.setText(EmpDet.get("Mob").getAsString());
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("Error:", "Some Error" + t.getMessage());
            }
        });
    }

    public void openHome() {
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
        Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");

        if (CheckIn == true) {
            Intent Dashboard = new Intent(FlightTicketRequest.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }

    public void DepDtePker(View v) {
        hideKeyBoard();
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(FlightTicketRequest.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Date time=DT.GetCurrDateTime(FlightTicketRequest.this);
                        radoMor.setEnabled(true);
                        departureDt = "" + (year + "-" + ((monthOfYear<9)?"0":"")+(monthOfYear + 1) + "-" + ((dayOfMonth<10)?"0":"")+dayOfMonth);
                        if (DT.getDateWithFormat(time,"yyyy-MM-dd").equals(departureDt)){
                            if(c.get(Calendar.HOUR_OF_DAY)>12){
                                radoMor.setChecked(false);
                                radoEve.setChecked(true);
                                radoMor.setEnabled(false);
                            }
                        }
                        edtDepature.setText(((dayOfMonth<10)?"0":"")+dayOfMonth + "-" + ((monthOfYear<9)?"0":"")+(monthOfYear + 1) + "-" + year);
                        maxTWoDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        MaxMinDateTo(maxTWoDate);
                        dtReturn = "";
                        retedtDepature.setText("");

                    }
                }, mYear, mMonth, mDay);

        Calendar calendarmin = Calendar.getInstance();

        calendarmin.set(mYear, mMonth , mDay);
        datePickerDialog.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
        datePickerDialog.show();
    }

    public void RetunDtePker(View v) {
        hideKeyBoard();
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        if (!maxTWoDate.equalsIgnoreCase("")) {
            // date picker dialog
            DatePickerDialog picker = new DatePickerDialog(FlightTicketRequest.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            dtReturn = year + "-" + ((monthOfYear<9)?"0":"")+(monthOfYear + 1) + "-" + ((dayOfMonth<10)?"0":"")+dayOfMonth;
                            retedtDepature.setText( ((dayOfMonth<10)?"0":"")+dayOfMonth + "-" + ((monthOfYear<9)?"0":"")+(monthOfYear + 1) + "-" + year);
                            Date time=DT.GetCurrDateTime(FlightTicketRequest.this);

                            retradoMor.setEnabled(true);
                            if (DT.getDateWithFormat(time,"yyyy-MM-dd").equals(dtReturn)){
                                if(cldr.get(Calendar.HOUR_OF_DAY)>12){
                                    retradoMor.setChecked(false);
                                    retradoEve.setChecked(true);
                                    retradoMor.setEnabled(false);
                                }
                            }
                        }
                    }, year, month, day);
            Calendar calendarmin = Calendar.getInstance();

            calendarmin.set(Integer.parseInt(tominYear), Integer.parseInt(tominMonth) - 1, Integer.parseInt(tominDay));
            picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
            picker.show();
        } else {
            Toast.makeText(this, "Please choose depature date", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void MaxMinDateTo(String strMinDate) {
        String[] separated1 = strMinDate.split("-");
        separated1[0] = separated1[0].trim();
        separated1[1] = separated1[1].trim();
        separated1[2] = separated1[2].trim();
        tominYear = separated1[0];
        tominMonth = separated1[1];
        tominDay = separated1[2];
    }

    public Boolean validate() {
        TrvTyp = "ONE";
        if (FrmPlc.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(FlightTicketRequest.this, "Enter the From Place", Toast.LENGTH_LONG).show();
            return false;
        }
        if (ToPlc.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(FlightTicketRequest.this, "Enter the To Place", Toast.LENGTH_LONG).show();
            return false;
        }
        if (edtDepature.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(FlightTicketRequest.this, "Enter the Depature Date", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!(radoMor.isChecked() || radoEve.isChecked())) {
            Toast.makeText(FlightTicketRequest.this, "Enter the Time Session", Toast.LENGTH_LONG).show();
            return false;
        }
        if (radioRound.isChecked()) {
            TrvTyp = "TWO";
            if (retFrmPlc.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(FlightTicketRequest.this, "Enter the From Place", Toast.LENGTH_LONG).show();
                return false;
            }
            if (retToPlc.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(FlightTicketRequest.this, "Enter the To Place", Toast.LENGTH_LONG).show();
                return false;
            }
            if (retedtDepature.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(FlightTicketRequest.this, "Enter the Depature Date", Toast.LENGTH_LONG).show();
                return false;
            }
            if (!(retradoMor.isChecked() || retradoEve.isChecked())) {
                Toast.makeText(FlightTicketRequest.this, "Enter the Time Session", Toast.LENGTH_LONG).show();
                return false;
            }


        }
        jTrvDets = new JSONArray();
        for (int il = 0; il < TrvlrList.getChildCount(); il++) {
            try {
                LinearLayout LayChild = (LinearLayout) TrvlrList.getChildAt(il);
                RadioButton radHap = LayChild.findViewById(R.id.radio_HAP);
                RadioButton radOth = LayChild.findViewById(R.id.radio_OTH);
                EditText txTrvEmpid = LayChild.findViewById(R.id.edtGEmpid);
                EditText txTrvName = LayChild.findViewById(R.id.txTrvName);
                EditText txCmpName = LayChild.findViewById(R.id.txCmpName);
                EditText txMobile = LayChild.findViewById(R.id.txTrvMobile);
                EditText txRemark = LayChild.findViewById(R.id.txTrvRemarks);
                RadioButton radGenM = LayChild.findViewById(R.id.radio_TrvGenM);
                RadioButton radGenF = LayChild.findViewById(R.id.radio_TrvGenF);
                JSONObject nItem = new JSONObject();
                String CmpCat = "";
                if (radHap.isChecked())
                    CmpCat = "HAP";
                else if (radOth.isChecked())
                    CmpCat = "OTH";
                else {
                    Toast.makeText(FlightTicketRequest.this, "Enter the Company Type", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (txTrvName.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(FlightTicketRequest.this, "Enter the Traveller Name", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (txCmpName.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(FlightTicketRequest.this, "Enter the Company Name", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (txMobile.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(FlightTicketRequest.this, "Enter the Mobile No", Toast.LENGTH_LONG).show();
                    return false;
                }


                String TrvGen = "";
                if (radGenM.isChecked())
                    TrvGen = "M";
                else if (radGenF.isChecked())
                    TrvGen = "F";
                else {
                    Toast.makeText(FlightTicketRequest.this, "Select the Gender", Toast.LENGTH_LONG).show();
                    return false;
                }

               /* if (txRemark.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(FlightTicketRequest.this, "Enter the Remarks", Toast.LENGTH_LONG).show();
                    return false;
                }*/
                nItem.put("CmpTyp", CmpCat);
                nItem.put("EmpID", txTrvEmpid.getText().toString());
                nItem.put("TrvName", txTrvName.getText().toString());
                nItem.put("TrvCmp", txCmpName.getText().toString());
                nItem.put("CmpMob", txMobile.getText().toString());
                nItem.put("CmpGen", TrvGen);
                nItem.put("remarks", txRemark.getText().toString());

                jTrvDets.put(nItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void submitData() {
        JSONObject jObj = new JSONObject();
        try {
            TrvTyp = "ONE";
            if (radioRound.isChecked()) TrvTyp = "TWO";
            String TrvMorEve = "";
            if (radoMor.isChecked()) TrvMorEve = "M";
            if (radoEve.isChecked()) TrvMorEve = "E";

            String retTrvMorEve = "";
            if (retradoMor.isChecked()) retTrvMorEve = "M";
            if (retradoEve.isChecked()) retTrvMorEve = "E";

            jObj.put("SF", UserDetails.getString("Sfcode", ""));
            jObj.put("SFNm", UserDetails.getString("SfName", ""));
            jObj.put("TrvMdType", TrvTyp);
            jObj.put("TrvFrmPlc", FrmPlc.getText().toString());
            jObj.put("TrvToPlc", ToPlc.getText().toString());
            jObj.put("TrvDepDt", departureDt);
            jObj.put("TrvSes", TrvMorEve);

            jObj.put("retTrvFrmPlc", retFrmPlc.getText().toString());
            jObj.put("retTrvToPlc", retToPlc.getText().toString());
            jObj.put("retTrvDepDt", dtReturn);
            jObj.put("retTrvSes", retTrvMorEve);

            jObj.put("Traveller", jTrvDets);

            apiInterface.JsonSave("save/flightbook", jObj.toString()).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject Res = response.body();
                    String Msg = Res.get("Msg").getAsString();
                    if (!Msg.equalsIgnoreCase("")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(FlightTicketRequest.this)
                                .setTitle(HAPApp.Title)
                                .setMessage(Html.fromHtml(Msg))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Res.get("Success").getAsString().equalsIgnoreCase("true")) {
                                            openHome();
                                        }
                                    }
                                })
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    ResetSubmitBtn(2, buttonSave);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void ResetSubmitBtn(int resetMode, CircularProgressButton btnAnim) {
        long dely = 10;
        if (resetMode != 0) dely = 1000;
        if (resetMode == 1) {
            btnAnim.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));
        } else {
            btnAnim.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnAnim.stopAnimation();
                btnAnim.revertAnimation();
                btnAnim.setBackground(getDrawable(R.drawable.button_blueg));
            }
        }, dely);

    }
//    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
//            new OnBackPressedDispatcher(new Runnable() {
//                @Override
//                public void run() {
//                   finish();
//                }
//            });
//
//    @Override
//    public void onBackPressed() {
//
//    }

}
