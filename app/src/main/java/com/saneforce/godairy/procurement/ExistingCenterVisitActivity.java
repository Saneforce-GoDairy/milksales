package com.saneforce.godairy.procurement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityExistingCenterVisitBinding;

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
    }

    private void onClick() {
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
        // 5
        mPouringActivity = binding.spinnerPouringActivity.getSelectedItem().toString();
        mOpeningTime = binding.openingTime.getText().toString();
        mClosingTime = binding.closingTime.getText().toString();
        mNoOfFarmers = binding.edNoFarmers.getText().toString();
        mVolume = binding.edVolume.getText().toString();

        // 5
        mAvgFAT = binding.edAvgFat.getText().toString();
        mAvgSNF = binding.edAvgSnf.getText().toString();
        mAvgRate = binding.edAvgRate.getText().toString();
        mNoOfCansLoad = binding.edCansLoad.getText().toString();
        mNoOfCansReturned = binding.edCansReturned.getText().toString();

        // 5
        mCattleFeed = binding.edCattleFeed.getText().toString();
        mOtherStock = binding.edOtherStock.getText().toString();
        mMachineCondition = binding.spinnerMachineCondition.getSelectedItem().toString();
        mLoanFarmerIssue = binding.edLoanFarIssue.getText().toString();
        mIssueFromFarmerSide = binding.edIssueFrmFarmerSide.getText().toString();

        // 2
        mAssetVerification = binding.edAssetVerification.getText().toString();
        mRenameVillage = binding.edRenameVillage.getText().toString();

        if ("Select".equals(mPouringActivity)){
            ((TextView)binding.spinnerPouringActivity.getSelectedView()).setError("Select Pouring Activity");
            binding.spinnerPouringActivity.getSelectedView().requestFocus();
            binding.txtPouringNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mOpeningTime)){
            binding.openingTime.setError("Enter opening time");
            binding.openingTime.requestFocus();
            return false;
        }
        if ("".equals(mClosingTime)){
            binding.closingTime.setError("Enter closing time");
            binding.closingTime.requestFocus();
            return false;
        }
        if ("".equals(mNoOfFarmers)){
            binding.edNoFarmers.setError("Please enter");
            binding.edNoFarmers.requestFocus();
            return false;
        }
        if ("".equals(mVolume)){
            binding.edVolume.setError("Enter volume");
            binding.edVolume.requestFocus();
            return false;
        }
        if ("".equals(mAvgFAT)){
            binding.edAvgFat.setError("Enter Avg FAT");
            binding.edAvgFat.requestFocus();
            return false;
        }
        if ("".equals(mAvgSNF)){
            binding.edAvgSnf.setError("Enter Avg SNF");
            binding.edAvgSnf.requestFocus();
            return false;
        }
        if ("".equals(mAvgRate)){
            binding.edAvgRate.setError("Enter Avg Rate");
            binding.edAvgRate.requestFocus();
            return false;
        }
        if ("".equals(mNoOfCansLoad)){
            binding.edCansLoad.setError("Enter cans load");
            binding.edCansLoad.requestFocus();
            return false;
        }
        if ("".equals(mNoOfCansReturned)){
            binding.edCansReturned.setError("Enter returned cans");
            binding.edCansReturned.requestFocus();
            return false;
        }
        if ("".equals(mCattleFeed)){
            binding.edCattleFeed.setError("Enter cattle feed");
            binding.edCattleFeed.requestFocus();
            return false;
        }
        if ("".equals(mOtherStock)){
            binding.edOtherStock.setError("Enter other stock");
            binding.edOtherStock.requestFocus();
            return false;
        }
        if ("".equals(mEchoMilkClActivity)){
            Toast.makeText(context, "Select echo milk clean activity", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("Select".equals(mMachineCondition)){
            ((TextView)binding.spinnerMachineCondition.getSelectedView()).setError("Select machine condition");
            binding.spinnerMachineCondition.getSelectedView().requestFocus();
            binding.txtMachineConditionNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mLoanFarmerIssue)){
            binding.edLoanFarIssue.setError("Enter loan farmer issue");
            binding.edLoanFarIssue.requestFocus();
            return false;
        }
        if ("".equals(mIssueFromFarmerSide)){
            binding.edIssueFrmFarmerSide.setError("Enter issue from farmer side");
            binding.edIssueFrmFarmerSide.requestFocus();
            return false;
        }
        if ("".equals(mAssetVerification)){
            binding.edAssetVerification.setError("Enter asset verification");
            binding.edAssetVerification.requestFocus();
            return false;
        }
        if ("".equals(mRenameVillage)){
            binding.edRenameVillage.setError("Enter rename village");
            binding.edRenameVillage.requestFocus();
            return false;
        }
        return true;
    }

    private void initLoad() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.farmer_creation_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPouringActivity.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.farmer_creation_types_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerMachineCondition.setAdapter(adapter1);
    }
}