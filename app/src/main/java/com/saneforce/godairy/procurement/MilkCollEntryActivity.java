package com.saneforce.godairy.procurement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityMilkCollEntryBinding;

public class MilkCollEntryActivity extends AppCompatActivity {
    private ActivityMilkCollEntryBinding binding;
    private final Context context = this;
    private final String TAG = "MilkCollEntryActivity";
    private String mNoOfCans, mMilkWeight, mMilkToatlQty, mMilkSample, mFat, mSnf;
    private String mClr, mMilkRate, mTotalMilkAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMilkCollEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClick();
    }

    private void onClick() {
        binding.save.setOnClickListener(v -> {
            if (validateInputs()) {
                saveNow();
            }
        });

        binding.back.setOnClickListener(v -> finish());
    }

    private void saveNow() {
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
    }

    private boolean validateInputs() {
        mNoOfCans = binding.edCans.getText().toString();
        mMilkWeight = binding.edMilkWeight.getText().toString();
        mMilkToatlQty = binding.edMilkToalQty.getText().toString();

        mMilkSample = binding.edMilkSampleNo.getText().toString();
        mFat = binding.edFat.getText().toString();
        mSnf = binding.edSnf.getText().toString();

        mClr = binding.edClr.getText().toString();
        mMilkRate = binding.edMilkRate.getText().toString();
        mTotalMilkAmt = binding.edTotalMilkAmount.getText().toString();

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
        return true;
    }
}