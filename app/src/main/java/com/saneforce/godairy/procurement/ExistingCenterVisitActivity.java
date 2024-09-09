package com.saneforce.godairy.procurement;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityExistingCenterVisitBinding;
import java.util.Calendar;

public class ExistingCenterVisitActivity extends AppCompatActivity {
    private ActivityExistingCenterVisitBinding binding;
    private final Context context = this;
    private String mPouringActivity, mOpeningTime, mClosingTime, mNoOfFarmers, mVolume, mAvgFAT, mAvgSNF, mAvgRate;
    private String mNoOfCansLoad, mNoOfCansReturned, mCattleFeed, mOtherStock, mEchoMilkClActivity = "", mMachineCondition;
    private String mLoanFarmerIssue, mIssueFromFarmerSide, mAssetVerification, mRenameVillage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExistingCenterVisitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLoad();
        onClick();

        binding.openingTime.setFocusable(false);
        binding.closingTime.setFocusable(false);
    }

    @SuppressLint("SetTextI18n")
    private void onClick() {

        binding.openingTime.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(context, (timePicker, selectedHour, selectedMinute) -> binding.openingTime.setText( selectedHour + ":" + selectedMinute), hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });

        binding.closingTime.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(context, (timePicker, selectedHour, selectedMinute) -> binding.closingTime.setText( selectedHour + ":" + selectedMinute), hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });

        binding.back.setOnClickListener(view -> finish());
        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                saveNow();
            }
        });

        binding.milkClDaily.setOnClickListener(view -> {
            mEchoMilkClActivity = "Daily";
            binding.milkClDaily.setChecked(true);

            binding.milkClWeekly.setChecked(false);
            binding.milkClMonthly.setChecked(false);
        });

        binding.milkClWeekly.setOnClickListener(view -> {
            mEchoMilkClActivity = "Weekly";
            binding.milkClWeekly.setChecked(true);

            binding.milkClDaily.setChecked(false);
            binding.milkClMonthly.setChecked(false);
        });

        binding.milkClMonthly.setOnClickListener(view -> {
            mEchoMilkClActivity = "Monthly";
            binding.milkClMonthly.setChecked(true);

            binding.milkClDaily.setChecked(false);
            binding.milkClWeekly.setChecked(false);
        });
    }

    private void saveNow() {
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("pouring_act", mPouringActivity);
        serviceIntent.putExtra("opening_time", mOpeningTime);
        serviceIntent.putExtra("closing_time", mClosingTime);
        serviceIntent.putExtra("no_of_farmer", mNoOfFarmers);
        serviceIntent.putExtra("volume", mVolume);
        serviceIntent.putExtra("avg_fat", mAvgFAT);
        serviceIntent.putExtra("avg_snf", mAvgSNF);
        serviceIntent.putExtra("avg_rate", mAvgRate);
        serviceIntent.putExtra("cans_load", mNoOfCansLoad);
        serviceIntent.putExtra("cans_returned", mNoOfCansReturned);
        serviceIntent.putExtra("cattle_feed", mCattleFeed);
        serviceIntent.putExtra("other_stock", mOtherStock);
        serviceIntent.putExtra("echo_milk_clean_activity", mEchoMilkClActivity);
        serviceIntent.putExtra("machine_condition", mMachineCondition);
        serviceIntent.putExtra("loan_farmer_issue", mLoanFarmerIssue);
        serviceIntent.putExtra("issue_frm_farmer_side", mIssueFromFarmerSide);
        serviceIntent.putExtra("asset_verification", mAssetVerification);
        serviceIntent.putExtra("rename_village", mRenameVillage);
        serviceIntent.putExtra("active_flag", "1");
        serviceIntent.putExtra("upload_service_id", "11");
        ContextCompat.startForegroundService(this, serviceIntent);

        finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs() {
        mPouringActivity = binding.spinnerPouringActivity.getSelectedItem().toString();
        mOpeningTime = binding.openingTime.getText().toString();
        mClosingTime = binding.closingTime.getText().toString();
        mNoOfFarmers = binding.edNoFarmers.getText().toString();
        mVolume = binding.edVolume.getText().toString();
        mAvgFAT = binding.edAvgFat.getText().toString();
        mAvgSNF = binding.edAvgSnf.getText().toString();
        mAvgRate = binding.edAvgRate.getText().toString();
        mNoOfCansLoad = binding.edCansLoad.getText().toString();
        mNoOfCansReturned = binding.edCansReturned.getText().toString();
        mCattleFeed = binding.edCattleFeed.getText().toString();
        mOtherStock = binding.edOtherStock.getText().toString();
        mMachineCondition = binding.spinnerMachineCondition.getSelectedItem().toString();
        mLoanFarmerIssue = binding.edLoanFarIssue.getText().toString();
        mIssueFromFarmerSide = binding.edIssueFrmFarmerSide.getText().toString();
        mAssetVerification = binding.edAssetVerification.getText().toString();
        mRenameVillage = binding.edRenameVillage.getText().toString();
        return true;
    }

    private void initLoad() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.proc_pouring_activity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPouringActivity.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.proc_machine_condi, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerMachineCondition.setAdapter(adapter1);
    }
}