package com.saneforce.milksales.Activity_Hap;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Common_Class.Common_Class;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.DistanceMeterWatcher;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.Model_Class.ModeOfTravel;
import com.saneforce.milksales.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaFuelEdit extends AppCompatActivity implements Master_Interface {
    EditText edtFrom, edtTo, edtPersonal, edtTraveled;
    String SLNO = "", MOT = "", MOTNm = "", starEd = "", DriverNeed = "false", startEnd = "", DriverMode = "", modeId = "";
    Shared_Common_Pref mShared_common_pref;
    Integer inEdtFrom, inEdtTo, intSum;
    CardView ModeTravel;
    List<Common_Model> modelTravelType = new ArrayList<>();
    CustomListViewDialog customDialog;
    Common_Model Model_Pojo;
    TextView TextMode;

    Gson gson;
    List<ModeOfTravel> modelOfTravel;
    Type userType;
    ImageView btnFuelclose;
    LinearLayout linCheckdriver;
    CheckBox driverAllowance;

    /*Choosing Dynamic Mode*/
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
                modelOfTravel = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < modelOfTravel.size(); i++) {
                    String id = String.valueOf(modelOfTravel.get(i).getStEndNeed());
                    String name = modelOfTravel.get(i).getName();
                    String modeId = String.valueOf(modelOfTravel.get(i).getId());
                    String driverMode = String.valueOf(modelOfTravel.get(i).getDriverNeed());
                    if (id.equalsIgnoreCase("1")) {
                        Model_Pojo = new Common_Model(id, name, modeId, driverMode);
                        modelTravelType.add(Model_Pojo);
                    }
                }
                customDialog = new CustomListViewDialog(TaFuelEdit.this, modelTravelType, 8);
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

    static DistanceMeterWatcher ReadingChanger;

    public static void onDistanceMeterWatcher(DistanceMeterWatcher mReadingChange) {
        ReadingChanger = mReadingChange;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ta_fuel_edit);
        SLNO = String.valueOf(getIntent().getSerializableExtra("SL_NO"));
        MOT = String.valueOf(getIntent().getSerializableExtra("MOT"));
        MOTNm = String.valueOf(getIntent().getSerializableExtra("MOTNm"));
        mShared_common_pref = new Shared_Common_Pref(this);
        edtFrom = findViewById(R.id.edt_from);
        edtTo = findViewById(R.id.edt_to);
        edtPersonal = findViewById(R.id.edt_pers);
        edtTraveled = findViewById(R.id.edt_travelled);
        driverAllowance = findViewById(R.id.da_driver_allowance);
        linCheckdriver = findViewById(R.id.lin_check_driver);
        btnFuelclose = findViewById(R.id.btnFuelclose);

        btnFuelclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextMode = findViewById(R.id.txt_mode);
        edtFrom.setText("" + getIntent().getSerializableExtra("Start"));
        edtTo.setText("" + getIntent().getSerializableExtra("End"));
        //  edtPersonal.setText("0");

        TextMode.setText(MOTNm);

        edtFrom.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, 99999999), new InputFilter.LengthFilter(10)});
        edtTo.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, 99999999), new InputFilter.LengthFilter(10)});

        gson = new Gson();
        inEdtFrom = Integer.valueOf(Common_Class.isNullOrEmpty(edtFrom.getText().toString()) ? "0" : edtFrom.getText().toString());
        inEdtTo = Integer.parseInt(Common_Class.isNullOrEmpty(edtTo.getText().toString()) ? "0" : edtTo.getText().toString());
        intSum = inEdtTo - inEdtFrom;
        Log.v("INT_SUM", String.valueOf(intSum));
        edtTraveled.setText("" + intSum);
        edtPersonal.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, intSum)});

        ModeTravel = findViewById(R.id.card_travel_mode);
        ModeTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelTravelType.clear();
                dynamicMode();
            }
        });
        edtFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

//                if (edtFrom.getText().toString().equalsIgnoreCase("")) edtFrom.setText("0");
//                if (edtTo.getText().toString().equalsIgnoreCase("")) edtTo.setText("0");
                    //  if (edtPersonal.getText().toString().equalsIgnoreCase("")) edtPersonal.setText("0");
                    inEdtFrom = Integer.valueOf(edtFrom.getText().toString().equalsIgnoreCase("") ? "0" : edtFrom.getText().toString());
                    int mxKm = inEdtFrom + Shared_Common_Pref.MaxKm;
                    edtTo.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, mxKm), new InputFilter.LengthFilter(10)});
                    edtTo.setText("");
                    // if (!edtTo.getText().toString().equals("")) {

                    inEdtTo = Integer.parseInt(edtTo.getText().toString().equalsIgnoreCase("0") ? "0" : edtTo.getText().toString());
                } catch (NumberFormatException ex) { // handle your exception

                }
                // }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
//                if (edtFrom.getText().toString().equalsIgnoreCase("")) edtFrom.setText("0");
//                if (edtTo.getText().toString().equalsIgnoreCase("")) edtTo.setText("0");
                    //  if (edtPersonal.getText().toString().equalsIgnoreCase("")) edtPersonal.setText("0");
                    inEdtFrom = Integer.valueOf(edtFrom.getText().toString().equalsIgnoreCase("") ? "0" : edtFrom.getText().toString());
                    inEdtTo = Integer.parseInt(edtTo.getText().toString().equalsIgnoreCase("") ? "0" : edtTo.getText().toString());
                    intSum = inEdtTo - inEdtFrom;
                    Log.v("INT_SUM", String.valueOf(intSum));
                    edtTraveled.setText("" + intSum);

                    edtPersonal.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, intSum)});
                } catch (Exception e) {

                }
            }
        });


        edtTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {


//                if (edtFrom.getText().toString().equalsIgnoreCase("")) edtFrom.setText("0");
//                if (edtTo.getText().toString().equalsIgnoreCase("")) edtTo.setText("0");
                    // if (edtPersonal.getText().toString().equalsIgnoreCase("")) edtPersonal.setText("0");
                    inEdtFrom = Integer.valueOf(edtFrom.getText().toString().equalsIgnoreCase("") ? "0" : edtFrom.getText().toString());
                    //  if (!edtTo.getText().toString().equals("")) {

                    try {
                        inEdtTo = Integer.parseInt(edtTo.getText().toString().equalsIgnoreCase("") ? "0" : edtTo.getText().toString());
                    } catch (NumberFormatException ex) { // handle your exception

                    }
                    //   }
                } catch (Exception e) {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
//                if (edtFrom.getText().toString().equalsIgnoreCase("")) edtFrom.setText("0");
//                if (edtTo.getText().toString().equalsIgnoreCase("")) edtTo.setText("0");
                    //    if (edtPersonal.getText().toString().equalsIgnoreCase("")) edtPersonal.setText("0");
                    inEdtFrom = Integer.valueOf(edtFrom.getText().toString().equalsIgnoreCase("") ? "0" : edtFrom.getText().toString());
                    inEdtTo = Integer.parseInt(edtTo.getText().toString().equalsIgnoreCase("") ? "0" : edtTo.getText().toString());
                    intSum = inEdtTo - inEdtFrom;
                    Log.v("INT_SUM", String.valueOf(intSum));
                    edtTraveled.setText("" + intSum);
                    edtPersonal.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, intSum)});
                } catch (Exception e) {

                }
            }
        });

        showDriveAllowance();
    }

    void showDriveAllowance() {
        if (TextMode.getText().toString().equalsIgnoreCase("Four Wheeler"))
            linCheckdriver.setVisibility(View.VISIBLE);
        else
            linCheckdriver.setVisibility(View.GONE);

    }

    public void UpdteAllowance(View v) throws JSONException {

//        if (edtFrom.getText().toString().equalsIgnoreCase("")) edtFrom.setText("0");
//        if (edtTo.getText().toString().equalsIgnoreCase("")) edtTo.setText("0");
        // if (edtPersonal.getText().toString().equalsIgnoreCase("")) edtPersonal.setText("0");

//        if (edtFrom.getText().toString() != null && !edtFrom.getText().toString().isEmpty() && !edtFrom.getText().toString().equals("null")) {
//            if (edtTo.getText().toString() != null && !edtTo.getText().toString().isEmpty() && !edtTo.getText().toString().equals("null")) {

        inEdtFrom = Integer.valueOf(com.saneforce.milksales.Activity_Hap.Common_Class.isNullOrEmpty(edtFrom.getText().toString()) ? "0" : edtFrom.getText().toString().equalsIgnoreCase("null") ? "0" : edtFrom.getText().toString());
        inEdtTo = Integer.valueOf(com.saneforce.milksales.Activity_Hap.Common_Class.isNullOrEmpty(edtTo.getText().toString()) ? "0" : edtTo.getText().toString().equalsIgnoreCase("null") ? "0" : edtTo.getText().toString());
        String drvAllw = (driverAllowance.isChecked() ? "true" : "false");
        if (inEdtFrom <= 0) {
            Toast.makeText(this, "Enter the Starting Km", Toast.LENGTH_SHORT).show();
            return;
        }
        if (inEdtTo <= 0) {
            Toast.makeText(this, "Enter the Ending Km", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((inEdtTo - inEdtFrom) >= (Shared_Common_Pref.MaxKm))
            Toast.makeText(this, "Traveled km should be less than Maximum Km", Toast.LENGTH_SHORT).show();
        else if (inEdtFrom < inEdtTo) {
            JSONObject jj = new JSONObject();
            jj.put("sl_no", SLNO);
            jj.put("mot", MOT);
            jj.put("motnm", MOTNm);
            jj.put("driverAllow", drvAllw);
            jj.put("sfCode", mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jj.put("startKm", edtFrom.getText().toString());
            jj.put("endKm", edtTo.getText().toString());
            jj.put("personalKm", edtPersonal.getText().toString().equalsIgnoreCase("") ? "0" : edtPersonal.getText().toString());
            Log.v("printing_allow", jj.toString());

            Call<JsonObject> Callto;
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Callto = apiInterface.upteAllowance(jj.toString());
            Callto.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject json = response.body();
                    Log.v("CHECKING", json.get("success").getAsString());
                    if (ReadingChanger != null) ReadingChanger.onKilometerChange(jj);
                    if (json.get("success").getAsString().equalsIgnoreCase("true")) {
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        } else {
            Toast.makeText(this, "Closing km should be greater than Start Km", Toast.LENGTH_SHORT).show();
        }

        // }


        // }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 8) {
            TextMode.setText(myDataset.get(position).getName());
            MOTNm = myDataset.get(position).getName();
            MOT = myDataset.get(position).getFlag();


            startEnd = myDataset.get(position).getId();
            DriverMode = myDataset.get(position).getCheckouttime();
            modeId = myDataset.get(position).getFlag();

            if (DriverMode.equals("1")) {
                linCheckdriver.setVisibility(View.VISIBLE);
            } else {
                linCheckdriver.setVisibility(View.GONE);
            }
            DriverNeed = "";
            driverAllowance.setChecked(false);

            showDriveAllowance();

        }
    }


}