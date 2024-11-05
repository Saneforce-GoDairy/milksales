package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.procurement.AppConstants.GET_RATE_CARD_PRICE;
import static com.saneforce.godairy.procurement.AppConstants.MAS_GET_CUSTOMERS;

import static java.lang.String.format;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.Procurement;
import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityMilkCollEntryBinding;
import com.saneforce.godairy.procurement.adapter.SelectionAdapter;
import com.saneforce.godairy.procurement.printer.Printama2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  MilkCollEntryActivity extends AppCompatActivity implements SelectionAdapter.OnClickInterface{
    private ActivityMilkCollEntryBinding binding;
    private final Context context = this;
    private final String TAG = "MilkCollEntryActivity_";
    private String mNoOfCans, mMilkWeight, mMilkToatlQty, mMilkSample, mFat, mSnf;
    private String mCustomerName, mCustomerNo, mDate, mSession, mMilkType;
    private String mClr, mMilkRate, mTotalMilkAmt;
    @SuppressLint("StaticFieldLeak")
    public static MilkCollEntryActivity milkCollEntryActivity;
    private List<Procurement> selectionsLists;
    private int mSelect = 0;
    private ApiInterface apiInterface;
    public int year, month, day;
    int datePickerId = 0;
    private Dialog printDialog;
    private int paperSize = 80;
    private final String mDate2 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    private final String mTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    private final String mTimeDate  = mDate2 +" "+mTime;
    String mSelectedStateCode;
    double milkTotalQuantity;
    String RateCardPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMilkCollEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        milkCollEntryActivity = this;

        onClick();
        initSpinner();
        initPrintDialog();

        selectionsLists = new ArrayList<>();
        binding.edCustomerSel.setFocusable(false);
        binding.edDate.setFocusable(false);

        binding.edMilkRate.setFocusable(false);
    }

    private void initPrintDialog() {
        printDialog = new Dialog(context);
        printDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        printDialog.setContentView(R.layout.model_print_dialog);
        printDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        printDialog.setCancelable(true);

        Button print = printDialog.findViewById(R.id.print);
        Button ignore = printDialog.findViewById(R.id.ignore);

        print.setOnClickListener(v -> printInvoice());
        ignore.setOnClickListener(v -> ignoreNow());
    }

    private void ignoreNow() {
        saveNow();
    }

    private void printInvoice() {
        try {
            if (!isBluetoothAvailable()) {
                Toast.makeText(getApplicationContext(), R.string.check_bluetooth_connection,Toast.LENGTH_SHORT).show();
            }else if (isBluetoothAvailable()&&ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(MilkCollEntryActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
            } else {
                showPrinterSettingDialog();
            }
        } catch (Exception e) {
            showToast(getString(R.string.bluetooth_error));
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message , Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NonConstantResourceId")
    private void showPrinterSettingDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.model_dialog_printer_size);
        Button print = dialog.findViewById(R.id.print);
        RelativeLayout close =dialog.findViewById(R.id.close);

        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        RadioButton radioButton2 = dialog.findViewById(R.id.rb_size_80);
        radioButton2.setChecked(true);
        paperSize = 80;

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            dialog.findViewById(checkedId);

            switch(checkedId) {
                case R.id.rb_size_58:
                    paperSize=58;
                    break;

                case R.id.rb_size_102:
                    paperSize=102;
                    break;

                default:
                    paperSize=80;
            }
        });

        print.setOnClickListener(view -> {
            dialog.cancel();
            Log.e(TAG, "Selected" + paperSize);
            showPrinterList(paperSize);
        });
        close.setOnClickListener(view -> dialog.cancel());

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    private void showPrinterList(int size) {
        Printama2.showPrinterList(this, R.color.blue_1,size, printerName -> {
            Toast.makeText(context, printerName, Toast.LENGTH_SHORT).show();
            String text = "Connected to : " + printerName;
            if (!printerName.contains("failed")) {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.session_array, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSession.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.milk_type_array, R.layout.custom_spinner);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerMilkType.setAdapter(adapter1);
    }

    private void onClick() {
        binding.save.setOnClickListener(v -> {
            if (validateInputs()) {
                 saveNow();
                printDialog.show();
            }
        });

        binding.edCustomerSel.setOnClickListener(v -> {
            binding.title.setText(R.string.select_customer);
            loadCustomer();
            binding.scrollView1.setVisibility(View.GONE);
            binding.selectionCon.setVisibility(View.VISIBLE);
            mSelect = 1;
        });

        binding.back.setOnClickListener(v -> finish());

        binding.edDate.setOnClickListener(v -> {
            datePickerId = 1;
            setDate();
        });

        binding.edMilkWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String milkWeight = binding.edMilkWeight.getText().toString();

                if (!milkWeight.isEmpty()){
                    milkTotalQuantity = Integer.parseInt(milkWeight) * 1.03;

                    binding.edMilkToalQty.setText(milkTotalQuantity+"");
                    updateTotalAmount(RateCardPrice);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.spinnerMilkType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mMilkType = binding.spinnerMilkType.getSelectedItem().toString();

                if (!mMilkType.isEmpty()){
                    if (!mMilkType.equals("Select")){

                        String mMilkTypeFinal = "";
                        if (mMilkType.equals("Cow Milk")){
                            mMilkTypeFinal = "C";
                        }
                        if (mMilkType.equals("Buffalo Milk")){
                            mMilkTypeFinal = "B";
                        }

                        loadRateCard(mSelectedStateCode, mMilkTypeFinal);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.back2.setOnClickListener(view -> finish());

    }

    public void setDate()
    {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        // TODO Auto-generated method stub
        if (id == 999)
        {
            final Calendar calendar = Calendar.getInstance();
            int YEAR = calendar.get(Calendar.YEAR);
            int MONTH = calendar.get(Calendar.MONTH);
            int DAY = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, myDateListener, YEAR, MONTH, DAY);
        }
        return null;
    }

    private final DatePickerDialog.OnDateSetListener myDateListener = (arg0, arg1, arg2, arg3) -> {
        // TODO Auto-generated method stub
        // arg1 = year
        // arg2 = month
        // arg3 = day
        showDate(arg1, arg2+1, arg3);
    };

    private void showDate(int year, int month, int day)
    {
        String selectedDate = day+"/"+month+"/"+year;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        try {
            date = dateFormat.parse(selectedDate);
        } catch (ParseException e) {
            // throw new RuntimeException(e);
            Toast.makeText(context, "Date Picker Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        binding.edDate.setText(dateFormat.format(date));
    }

    private void loadCustomer() {
        if (selectionsLists != null){
            selectionsLists.clear();
        }

        Call<ResponseBody> call =
                apiInterface.getCustomers(MAS_GET_CUSTOMERS);

        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    binding.shimmerLayout.setVisibility(View.GONE);
                    String customersList;
                    try {
                        customersList = response.body().string();
                        JSONObject jsonObject = new JSONObject(customersList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords) {
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                Procurement customer = new Procurement();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                customer.setSelectionCode(object.getString("Customer_Code"));
                                customer.setSelectionName(object.getString("Customer_Name"));
                                customer.setStateName(object.getString("State_Name"));
                                selectionsLists.add(customer);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            SelectionAdapter selectionAdapter = new SelectionAdapter(0, selectionsLists, context);
                            binding.recyclerView.setAdapter(selectionAdapter);
                            selectionAdapter.notifyDataSetChanged();
                        }
                    } catch (IOException | JSONException e) {
                        showToast(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    @Override
    public void onClickInterface(Intent intent) {
        String requestId = intent.getStringExtra("request_id");
        int mRequestId = Integer.parseInt(requestId);
        String mSelectedName;

        if (mRequestId == 0) {
            mSelectedName = intent.getStringExtra("selection_name");
            mSelectedStateCode = intent.getStringExtra("selection_code");
            Log.e(TAG, "Selected State Code = " + mSelectedStateCode);
            binding.edCustomerSel.setText(mSelectedName);

            String mSelectedStateName = intent.getStringExtra("state_name");
            if (!mSelectedStateName.isEmpty()){
                binding.stateTextCon.setVisibility(View.VISIBLE);
                binding.stateText.setText(mSelectedStateName);

            //    loadRateCard(mSelectedStateCode);
            }
        }

        if (mRequestId == 1) {
            mSelectedName = intent.getStringExtra("selection_name");
            binding.edCustomerSel.setText(mSelectedName);
        }

        if (mSelect == 1){
            binding.scrollView1.setVisibility(View.VISIBLE);
            binding.title.setText(R.string.milk_collection_entry);
            binding.selectionCon.setVisibility(View.GONE);
            mSelect = 0;
        }
    }

    private void loadRateCard(String stateCode, String mMilkType) {
        Log.e(TAG, "Selected Milk Type Short = " + mMilkType);
        Call<ResponseBody> call =
                apiInterface.getRateCardPrice(GET_RATE_CARD_PRICE,
                                              stateCode,
                                              mMilkType);

        Log.e(TAG, "Post data = \n " + "State Code = " + mSelectedStateCode + "Milk Type = " + mMilkType);
       call.enqueue(new Callback<>() {
           @Override
           public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
               if (response.isSuccessful()) {
                   if (response.body() != null) {
                       try {
                           String customersList = response.body().string();
                           Log.e(TAG, "Received Server Response = " + customersList);
                           JSONObject jsonObject = new JSONObject(customersList);
                           boolean mRecords = jsonObject.getBoolean("status");
                           if (mRecords) {
                               JSONArray jsonArrayData = jsonObject.getJSONArray("data");

                               if (jsonArrayData.length() == 0){
                                   binding.scrollView1.setVisibility(View.GONE);
                                   binding.rateCardError.setVisibility(View.VISIBLE);
                                   return;
                               }

                               for (int i = 0; i < jsonArrayData.length(); i++) {
                                   JSONObject object = jsonArrayData.getJSONObject(0);
                                   RateCardPrice = object.getString("Price");
                               }
                               binding.edMilkRate.setText(RateCardPrice);
                               Log.e(TAG, "Received Rate Card Price = " + RateCardPrice);
                               updateTotalAmount(RateCardPrice);
                           }
                       } catch (IOException | JSONException e) {
                           showToast(e.getMessage());
                       }
                   }
               }
           }

           @Override
           public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
               binding.scrollView1.setVisibility(View.GONE);
               binding.rateCardError.setVisibility(View.VISIBLE);
               Toast.makeText(context, "Error! " + t.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void updateTotalAmount(String price) {
        double totalAmount = Double.parseDouble(price) * milkTotalQuantity;
        binding.edTotalMilkAmount.setText(format("%.2f", totalAmount));

        Log.e(TAG, "Final Total Amount 'Received Rate Card Price * Milk Quantity' = " + totalAmount);
    }

    private void saveNow() {
        String mActiveFlag = "1";
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("session", mSession);
        serviceIntent.putExtra("milk_type", mMilkType);
        serviceIntent.putExtra("cans", mNoOfCans);
        serviceIntent.putExtra("customer_name", mCustomerName);
        serviceIntent.putExtra("customer_no", mCustomerNo);
        serviceIntent.putExtra("coll_entry_date", mDate);
        serviceIntent.putExtra("milk_weight", mMilkWeight);
        serviceIntent.putExtra("total_milk_qty", mMilkToatlQty);
        serviceIntent.putExtra("milk_sample_no", mMilkSample);
        serviceIntent.putExtra("fat", mFat);
        serviceIntent.putExtra("snf", mSnf);
        serviceIntent.putExtra("clr", mClr);
        serviceIntent.putExtra("milk_rate", mMilkRate);
        serviceIntent.putExtra("total_milk_amt", mTotalMilkAmt);
        serviceIntent.putExtra("active_flag", mActiveFlag);
        serviceIntent.putExtra("upload_service_id", "16");
        ContextCompat.startForegroundService(this, serviceIntent);
        finish();
        showToast(getString(R.string.form_submit_started));
    }

    private boolean validateInputs() {
        mCustomerName = binding.edCustomerSel.getText().toString();
        mCustomerNo = binding.edCustomerNo.getText().toString();
        mDate = binding.edDate.getText().toString();
        mSession = binding.spinnerSession.getSelectedItem().toString();
        mMilkType = binding.spinnerMilkType.getSelectedItem().toString();
        mNoOfCans = binding.edCans.getText().toString();
        mMilkWeight = binding.edMilkWeight.getText().toString();
        mMilkToatlQty = binding.edMilkToalQty.getText().toString();
        mMilkSample = binding.edMilkSampleNo.getText().toString();
        mFat = binding.edFat.getText().toString();
        mSnf = binding.edSnf.getText().toString();
        mClr = binding.edClr.getText().toString();
        mMilkRate = binding.edMilkRate.getText().toString();
        mTotalMilkAmt = binding.edTotalMilkAmount.getText().toString();

        if (mCustomerName.isEmpty()){
            Toast.makeText(context, "Select Customer", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mCustomerNo.isEmpty()){
            binding.edCustomerNo.requestFocus();
            binding.edCustomerNo.setError("Required");
            return false;
        }
        if (mDate.isEmpty()){
            Toast.makeText(context, "Select Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("Select".equals(mSession)){
            Toast.makeText(context, "Select Session", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("Select".equals(mMilkType)){
            Toast.makeText(context, "Select Milk Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mNoOfCans.isEmpty()){
            binding.edCans.requestFocus();
            binding.edCans.setError("Required");
            return false;
        }
        if (mMilkWeight.isEmpty()){
            binding.edMilkWeight.requestFocus();
            binding.edMilkWeight.setError("Required");
            return false;
        }
        if (mMilkToatlQty.isEmpty()){
            binding.edMilkToalQty.requestFocus();
            binding.edMilkToalQty.setError("Required");
            return false;
        }
        if (mMilkSample.isEmpty()){
            binding.edMilkSampleNo.requestFocus();
            binding.edMilkSampleNo.setError("Required");
            return false;
        }
        if (mFat.isEmpty()){
            binding.edFat.requestFocus();
            binding.edFat.setError("Required");
            return false;
        }
        if (mSnf.isEmpty()){
            binding.edSnf.requestFocus();
            binding.edSnf.setError("Required");
            return false;
        }
        if (mClr.isEmpty()){
            binding.edClr.requestFocus();
            binding.edClr.setError("Required");
            return false;
        }
        if (mMilkRate.isEmpty()){
            binding.edMilkRate.requestFocus();
            binding.edMilkRate.setError("Required");
            return false;
        }
        if (mTotalMilkAmt.isEmpty()){
            binding.edTotalMilkAmount.requestFocus();
            binding.edTotalMilkAmount.setError("Required");
            return false;
        }
        return true;
    }

    public void printBill() {
        try {
            Printama2.with(context, paperSize).connect(printama -> {

                printama.setWideTallBold();
                printama.setTallBold();
                printama.printTextln(Printama2.CENTER, "ENTRY SLIP");
                printama.addNewLine();
                printama.setNormalText();
                printama.printTextln(Printama2.LEFT, "Customer ID :" + "TESTID77722");
                printama.addNewLine();
                printama.setNormalText();
                printama.printTextln(Printama2.LEFT,"Customer Name : "+ mCustomerName);
                printama.setBold();
                printama.printTextln(Printama2.LEFT, "Date : " + mTimeDate);
                printama.printTextln(Printama2.LEFT, "Milk : " + mMilkToatlQty);
                printama.printTextln(Printama2.LEFT, "Fat : " + mFat);
                printama.printTextln(Printama2.LEFT, "SNF : " + mSnf);
                printama.printTextln(Printama2.LEFT, "Rate : " + mMilkRate);
                printama.printTextln(Printama2.LEFT, "Amount : " + mTotalMilkAmt);
                printama.addNewLine();
                printama.printTextln(Printama2.CENTER, "Thank you!");
                printama.setBold();
                printama.setLineSpacing(5);
                printama.feedPaper();
                printama.close();
            });
            saveNow();
        } catch (Exception e) {
            Log.e(TAG, "Error! printBill : " + e.getMessage());
        }
    }

    public boolean isBluetoothAvailable(){
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null
                && bluetoothAdapter.isEnabled()
                && bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON;
    }
}