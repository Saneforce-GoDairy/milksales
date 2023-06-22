package com.saneforce.milksales.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.saneforce.milksales.Activity.Util.ImageFilePath;
import com.saneforce.milksales.Common_Class.CameraPermission;
import com.saneforce.milksales.Common_Class.Common_Model;
import com.saneforce.milksales.Common_Class.Shared_Common_Pref;
import com.saneforce.milksales.Common_Class.Common_Class;

import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;
import com.saneforce.milksales.Interface.Master_Interface;
import com.saneforce.milksales.Interface.OnImagePickListener;
import com.saneforce.milksales.Model_Class.ModeOfTravel;
import com.saneforce.milksales.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaExceptionEntry extends AppCompatActivity implements View.OnClickListener, Master_Interface {

    EditText cardDate, edtActual, edtEarly, edtAmt;
    DatePickerDialog picker;
    String minDate, minYear, minMonth, minDay, fullPath = "", finalPath = "", filePath = "", imgUrl = "", imageConvert = "";
    ArrayList<String> travelTypeList;
    ArrayList<String> trainTypeList;
    Common_Model mCommon_model_spinner;
    List<Common_Model> listOrderType = new ArrayList<>();
    List<Common_Model> ModeOfTravel = new ArrayList<>();
    List<Common_Model> ToModeOfTravel = new ArrayList<>();
    List<Common_Model> TrainTypeModel = new ArrayList<>();
    CustomListViewDialog customDialog;
    CardView expType, expModetype, trainType, toModeType;
    TextView typeText, TxtActl, TxtErly, txtRmks, txtTotalAmt;
    LinearLayout dataVisiblity, modeTravel, trainAllowance, llCapture;
    ImageView imgAttach, ivCaptureImg;
    Dialog dialog;
    Uri outputFileUri;
    Shared_Common_Pref mShared_common_pref;
    TextView txtMode, txtTrain, txtToTravel;
    Gson gson;
    List<ModeOfTravel> modelOfTravels;
    List<ModeOfTravel> TomodelOfTravels;
    Type userType;
    String IdFrom, IdTo, NameFrom, NameTo;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_da_exception_entry);

        common_class = new Common_Class(this);
        gson = new Gson();
        getTool();
        MaxMinDate();
        ModeOfType();
        cardDate = findViewById(R.id.choose_date);
        expType = findViewById(R.id.exp_card_type);
        ivCaptureImg = findViewById(R.id.capture_img);
        expModetype = findViewById(R.id.exp_mode_type);
        TxtActl = findViewById(R.id.actual_timer);
        TxtErly = findViewById(R.id.early_timer);
        typeText = findViewById(R.id.txt_crd_tpe);
        edtActual = findViewById(R.id.choose_time);
        edtEarly = findViewById(R.id.early_time);
        edtAmt = findViewById(R.id.edt_amt);
        txtTotalAmt = findViewById(R.id.total_amount);
        imgAttach = findViewById(R.id.da_exp_att);
        dataVisiblity = findViewById(R.id.linear_view);
        modeTravel = findViewById(R.id.mode_linear);
        trainType = findViewById(R.id.exp_train_type);
        txtMode = findViewById(R.id.txt_mode_tpe);
        txtTrain = findViewById(R.id.txt_train_tpe);
        trainAllowance = findViewById(R.id.train_linear);
        toModeType = findViewById(R.id.exp_to_mode_type);
        txtToTravel = findViewById(R.id.txt_to_mode_tpe);
        txtRmks = findViewById(R.id.remarks);
        llCapture = findViewById(R.id.llCapture);
        expType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOrderType.clear();
                OrderType();
            }
        });
        expModetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(DaExceptionEntry.this, ModeOfTravel, 8);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });
        trainType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainTypeModel.clear();
                TrainType();
            }
        });
        toModeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(DaExceptionEntry.this, ToModeOfTravel, 9);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });

        mShared_common_pref = new Shared_Common_Pref(this);
        cardDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog.

                picker = new DatePickerDialog(DaExceptionEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cardDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                Calendar calendarmin = Calendar.getInstance();
                calendarmin.set(Integer.parseInt(minYear), Integer.parseInt(minMonth) - 1, Integer.parseInt(minDay));
                picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
                picker.show();

            }

        });
        edtActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DaExceptionEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = String.format("%02d", (selectedHour));
                        String min = String.format("%02d", (selectedMinute));


                        edtActual.setText(hour + ":" + min);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        edtEarly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DaExceptionEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = String.format("%02d", (selectedHour));
                        String min = String.format("%02d", (selectedMinute));

                        edtEarly.setText(hour + ":" + min);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPermission cameraPermission = new CameraPermission(DaExceptionEntry.this, getApplicationContext());
                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                } else {
                    popupCapture();

                }
            }
        });
        edtAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtTotalAmt.setText("Rs. " + edtAmt.getText().toString() + ".00");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        llCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraPermission cameraPermission = new CameraPermission(DaExceptionEntry.this, getApplicationContext());

                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                    Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                } else {
                    Log.v("PERMISSION", "PERMISSION");

                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImageURIPick(Bitmap bitmap, String FileName, String fullPath) {

                            MultipartBody.Part imgg = convertimg("file", fullPath);
                            HashMap<String, RequestBody> values = field(Shared_Common_Pref.Sf_Code);
                            Log.v("PATH_IMAGE_imgg", String.valueOf(imgg));
                            sendImageToServer(values, imgg, bitmap, fullPath);

                        }
                    });
                    Intent intent = new Intent(DaExceptionEntry.this, AllowancCapture.class);
                    intent.putExtra("allowance", "One");
                    startActivity(intent);
                }
            }
        });

        ivCaptureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                intent.putExtra("ImageUrl", imageConvert);
                startActivity(intent);
            }
        });


    }


    public void popupCapture() {
        dialog = new Dialog(DaExceptionEntry.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_capture);
        dialog.show();
        TextView upload = dialog.findViewById(R.id.upload);
        TextView camera = dialog.findViewById(R.id.camera);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMultiImage();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureFile();
            }
        });
    }

    public void selectMultiImage() {
        dialog.dismiss();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);

    }

    public void captureFile() {
        dialog.dismiss();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputFileUri = FileProvider.getUriForFile(DaExceptionEntry.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), Shared_Common_Pref.Sf_Code + "_" + System.currentTimeMillis() + ".jpeg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 1);

    }

    public void MaxMinDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println("Current_DATE_FORMAT" + formatter.format(date));

        String strMinDate = formatter.format(date);
        minDate = strMinDate;
        /*Min Date*/
        String[] separated1 = minDate.split("-");
        separated1[0] = separated1[0].trim();
        separated1[1] = separated1[1].trim();
        separated1[2] = separated1[2].trim();

        minYear = separated1[0];
        minMonth = separated1[1];
        minDay = separated1[2];
    }

    public void OrderType() {
        travelTypeList = new ArrayList<>();
        travelTypeList.add("TRAVEL EARLY CHECK-IN");
        travelTypeList.add("TRAVEL LATE CHECK-OUT");
        travelTypeList.add("TRAVEL EXCEPTION");

        for (int i = 0; i < travelTypeList.size(); i++) {
            String id = String.valueOf(travelTypeList.get(i));
            String name = travelTypeList.get(i);
            mCommon_model_spinner = new Common_Model(id, name, "flag");
            listOrderType.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(DaExceptionEntry.this, listOrderType, 101);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }

    public void TrainType() {
        trainTypeList = new ArrayList<>();
        trainTypeList.add("SS");
        trainTypeList.add("SL");
        trainTypeList.add("1Class AC");
        trainTypeList.add("2Class AC");
        trainTypeList.add("3Class AC");

        for (int i = 0; i < trainTypeList.size(); i++) {
            String id = String.valueOf(trainTypeList.get(i));
            String name = trainTypeList.get(i);
            mCommon_model_spinner = new Common_Model(id, name, "flag");
            TrainTypeModel.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(DaExceptionEntry.this, TrainTypeModel, 102);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();

    }

    public void ModeOfType() {
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "get/exceptravel");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        String commonLeaveType = "{\"tableName\":\"getmodeoftravel\",\"coloumns\":\"[\\\"id\\\",\\\"name\\\",\\\"Leave_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetExceptionRoutes(QueryString, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                userType = new TypeToken<ArrayList<com.saneforce.milksales.Model_Class.ModeOfTravel>>() {
                }.getType();
                modelOfTravels = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < modelOfTravels.size(); i++) {
                    String id = String.valueOf(modelOfTravels.get(i).getStEndNeed());
                    String name = modelOfTravels.get(i).getName();
                    String modeId = String.valueOf(modelOfTravels.get(i).getId());
                    String driverMode = String.valueOf(modelOfTravels.get(i).getDriverNeed());
                    Integer ModeTyp = modelOfTravels.get(i).getEligible();
                    Log.v("Name_of_mode_travel", name);
                    mCommon_model_spinner = new Common_Model(id, name, modeId, driverMode);
                    if (ModeTyp == 1)
                        ModeOfTravel.add(mCommon_model_spinner);
                    else
                        ToModeOfTravel.add(mCommon_model_spinner);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }

    public void getTool() {
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
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    DaExceptionEntry.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        edtAmt.setText("");

        if (type == 101) {
            NameFrom = "";
            NameTo = " ";
            txtMode.setText("");
            txtToTravel.setText("");
            cardDate.setText("");
            typeText.setText(myDataset.get(position).getName());
            if (typeText.getText().toString().equals("TRAVEL EARLY CHECK-IN")) {
                TxtActl.setText("Actual Check-in Time");
                TxtErly.setText("Early Check-in Time");
                dataVisiblity.setVisibility(View.VISIBLE);
                modeTravel.setVisibility(View.GONE);
            } else if (typeText.getText().toString().equalsIgnoreCase("TRAVEL LATE CHECK-OUT")) {
                TxtActl.setText("Actual Check-out Time");
                TxtErly.setText("Late Check-out Time");
                dataVisiblity.setVisibility(View.VISIBLE);
                modeTravel.setVisibility(View.GONE);
            } else {
                dataVisiblity.setVisibility(View.GONE);
                modeTravel.setVisibility(View.VISIBLE);
            }
        } else if (type == 8) {
            txtMode.setText(myDataset.get(position).getName());
            IdFrom = myDataset.get(position).getFlag();
            NameFrom = myDataset.get(position).getName();
        } else if (type == 102) {
            txtTrain.setText(myDataset.get(position).getName());
        } else if (type == 9) {
            txtToTravel.setText(myDataset.get(position).getName());
            IdTo = myDataset.get(position).getFlag();
            NameTo = myDataset.get(position).getName();
        }
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {

            if (resultCode == RESULT_OK) {
                if (requestCode == 2) {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            // display your images
                            ImageFilePath filepath = new ImageFilePath();
                            fullPath = filepath.getPath(DaExceptionEntry.this, mClipData.getItemAt(i).getUri());

                            Log.v("KARTHIC_DA_IMAGE_1", fullPath);
                            getMulipart(fullPath);
                        }
                    } else if (data.getData() != null) {
                        Uri item = data.getData();
                        ImageFilePath filepath = new ImageFilePath();
                        fullPath = filepath.getPath(DaExceptionEntry.this, item);
                        Log.v("KARTHIC_DA_IMAGE_2", fullPath);
                        getMulipart(fullPath);
                    }
                }
            }
        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            finalPath = "/storage/emulated/0";
            filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));
            Log.v("KARTHIC_DA_IMAGE_3", filePath);

            getMulipart(filePath);
        }
    }


    public void getMulipart(String path) {
        Log.v("PATH_IMAGE", path);
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field(Shared_Common_Pref.Sf_Code);
        Log.v("PATH_IMAGE_imgg", String.valueOf(imgg));
        //sendImageToServer(values, imgg);
    }

    public HashMap<String, RequestBody> field(String val) {
        HashMap<String, RequestBody> xx = new HashMap<String, RequestBody>();
        xx.put("data", createFromString(val));
        return xx;
    }

    private RequestBody createFromString(String txt) {
        return RequestBody.create(MultipartBody.FORM, txt);
    }


    private void sendImageToServer(HashMap<String, RequestBody> values, MultipartBody.Part imgg, Bitmap bitmap, String path) {
        common_class.ProgressdialogShow(1, "");

        Call<ResponseBody> Callto;
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Callto = apiService.uploadProcPic(values, imgg);

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());

                try {
                    if (response.isSuccessful()) {
                        common_class.ProgressdialogShow(0, "");


                        Log.v("print_upload_file_true", "ggg" + response);
                        JSONObject jb = null;
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("request_data_upload", String.valueOf(jsonData));
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getBoolean("success")) {
                            common_class.showMsg(DaExceptionEntry.this, "File uploading successful ");

                            Log.v("printing_dynamic_cou", js.getString("url"));
                            imgUrl = js.getString("url");
                            imageConvert = path;

                            ivCaptureImg.setVisibility(View.VISIBLE);

                            ivCaptureImg.setImageBitmap(bitmap);
                        } else {
                            common_class.ProgressdialogShow(0, "");
                            common_class.showMsg(DaExceptionEntry.this, "Failed.Try Again...");

                        }
                    }
                } catch (Exception e) {
                    common_class.ProgressdialogShow(0, "");
                    common_class.showMsg(DaExceptionEntry.this, "Failed.Try Again...");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("print_failure", "ggg" + t.getMessage());
                common_class.ProgressdialogShow(0, "");
                common_class.showMsg(DaExceptionEntry.this, t.getMessage() + "Try Again...");

            }
        });

    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        try {
            if (!TextUtils.isEmpty(path)) {

                File file = new File(path);
                if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(new File(path));
                else
                    file = new File(path);
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
                yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
            }
        } catch (Exception e) {
        }
        return yy;
    }

    public void DaException(View v) {
        if (validateTAExcep()) daExpen();
    }

    public boolean validateTAExcep() {
        if (typeText.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(DaExceptionEntry.this, "Select the exception type", Toast.LENGTH_LONG).show();
            return false;
        }
        if (typeText.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(DaExceptionEntry.this, "Select the exception type", Toast.LENGTH_LONG).show();
            return false;
        }
        if (txtRmks.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(DaExceptionEntry.this, "Enter the Remarks", Toast.LENGTH_LONG).show();
            return false;
        }
        if (imgUrl.equalsIgnoreCase("")) {
            Toast.makeText(DaExceptionEntry.this, "Please attach file", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void daExpen() {

        Log.v("SF_CODE", Shared_Common_Pref.Sf_Code);
        Log.v("DIVISION_CODE", Shared_Common_Pref.Div_Code);

        JSONObject json = new JSONObject();
        try {
            json.put("da_type", typeText.getText().toString());
            json.put("sfCode", mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            json.put("division_code", mShared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
            json.put("da_date", cardDate.getText().toString());
            json.put("da_actua_time", edtActual.getText().toString());
            json.put("da_early_time", edtEarly.getText().toString());
            json.put("da_amt", edtAmt.getText().toString());
            json.put("da_img", imgUrl);
            json.put("FMOT", IdFrom);
            json.put("TMOT", IdTo);
            json.put("FMOTName", NameFrom);
            json.put("TMOTName", NameTo);
            json.put("Rmks", txtRmks.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<JsonObject> Callto;
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Callto = apiService.daExpen(json.toString());

        Log.v("Resquest_DAException", Callto.request().toString());
        Callto.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                JsonObject jsonObject = response.body();
                Log.v("RESPONSE_ORDER", jsonObject.toString());
                if (jsonObject.get("success").toString().equalsIgnoreCase("true")) {
                    finish();
                }


            }


            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.v("print_failure", "ggg" + t.getMessage());
            }
        });

    }


}