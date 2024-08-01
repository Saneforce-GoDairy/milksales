package com.saneforce.godairy.procurement;

import static com.saneforce.godairy.procurement.AppConstants.MAS_GET_CUSTOMERS;
import static com.saneforce.godairy.procurement.AppConstants.MAS_GET_STATES;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.Procurement;
import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityMilkCollEntryBinding;
import com.saneforce.godairy.procurement.adapter.SelectionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MilkCollEntryActivity extends AppCompatActivity implements SelectionAdapter.OnClickInterface{
    private ActivityMilkCollEntryBinding binding;
    private final Context context = this;
    private final String TAG = "MilkCollEntryActivity";
    private String mNoOfCans, mMilkWeight, mMilkToatlQty, mMilkSample, mFat, mSnf;
    private String mCustomerName, mCustomerNo, mDate, mSession, mMilkType;
    private String mClr, mMilkRate, mTotalMilkAmt;
    private List<Procurement> selectionsLists;
    private int mSelect = 0;
    private ApiInterface apiInterface;
    private String mSelectedName;
    private String mSelectedCode;
    private Calendar calendar;
    private int year, month, day;
    int datePickerId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMilkCollEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        onClick();
        initSpinner();

        selectionsLists = new ArrayList<>();;
        binding.edCustomerSel.setFocusable(false);
        binding.edDate.setFocusable(false);
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
            }
        });

        binding.edCustomerSel.setOnClickListener(v -> {
            binding.title.setText("Select Customer");
            loadCustomer();
            binding.scrollView1.setVisibility(View.GONE);
            binding.selectionCon.setVisibility(View.VISIBLE);
            mSelect = 1;
        });

        binding.back.setOnClickListener(v -> finish());

        binding.edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerId = 1;
                setDate();
            }
        });
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
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = (arg0, arg1, arg2, arg3) -> {
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
        Date date;
        try {
            date = dateFormat.parse(selectedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (datePickerId == 0){
            binding.edDate.setText(dateFormat.format(date));
        }else {
            binding.edDate.setText(dateFormat.format(date));
        }
    }


    private void loadCustomer() {
        Call<ResponseBody> call =
                apiInterface.getCustomers(MAS_GET_CUSTOMERS);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    binding.shimmerLayout.setVisibility(View.GONE);
                    String customersList = "";

                    try {
                        customersList = response.body().string();
                        JSONObject jsonObject = new JSONObject(customersList);
                        boolean mRecords = jsonObject.getBoolean("status");

                        if (mRecords){
                            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                Procurement customer = new Procurement();
                                JSONObject object = jsonArrayData.getJSONObject(i);
                                customer.setSelectionCode(object.getString("Customer_Code"));
                                customer.setSelectionName(object.getString("Customer_Name"));
                                selectionsLists.add(customer);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.recyclerView.setLayoutManager(linearLayoutManager);
                            binding.recyclerView.setHasFixedSize(true);
                            binding.recyclerView.setItemViewCacheSize(20);
                            SelectionAdapter selectionAdapter = new SelectionAdapter(0,selectionsLists, context);
                            binding.recyclerView.setAdapter(selectionAdapter);
                            selectionAdapter.notifyDataSetChanged();
                        }
                    } catch (IOException | JSONException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClickInterface(Intent intent) {
        String requestId = intent.getStringExtra("request_id");

        int mRequestId = Integer.parseInt(requestId);

        if (mRequestId == 0) {
            mSelectedName = intent.getStringExtra("selection_name");
            mSelectedCode = intent.getStringExtra("selection_code");
            binding.edCustomerSel.setText(mSelectedName);
        }

        if (mRequestId == 1) {
            mSelectedName = intent.getStringExtra("selection_name");
            binding.edCustomerSel.setText(mSelectedName);
        }

        if (mSelect == 1){
            binding.scrollView1.setVisibility(View.VISIBLE);
            binding.title.setText("Milk Collection Entry");
            binding.selectionCon.setVisibility(View.GONE);
            mSelect = 0;
        }
    }

    private void saveNow() {
        Toast.makeText(context, "Valid", Toast.LENGTH_SHORT).show();
        /*
        String mActiveFlag = "1";
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
         serviceIntent.putExtra("cans", mNoOfCans);
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
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
         */
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
}