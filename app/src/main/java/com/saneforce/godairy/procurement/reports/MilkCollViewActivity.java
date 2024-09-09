package com.saneforce.godairy.procurement.reports;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.godairy.databinding.ActivityMilkCollViewBinding;

public class MilkCollViewActivity extends AppCompatActivity {
    private ActivityMilkCollViewBinding binding;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMilkCollViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String customerName = getIntent().getStringExtra("customer_name");
        String customerNo = getIntent().getStringExtra("customer_no");
        String date = getIntent().getStringExtra("date");
        String session = getIntent().getStringExtra("session");
        String milkType = getIntent().getStringExtra("milk_type");
        String cans = getIntent().getStringExtra("cans");
        String milkWeight = getIntent().getStringExtra("milk_weight");
        String totalMilkQty = getIntent().getStringExtra("total_milk_qty");
        String sampleNo = getIntent().getStringExtra("milk_sample_no");
        String fat = getIntent().getStringExtra("fat");
        String snf = getIntent().getStringExtra("snf");
        String clr = getIntent().getStringExtra("clr");
        String milkRate = getIntent().getStringExtra("milk_rate");
        String totalAmount = getIntent().getStringExtra("total_amount");
        String collectionEntryDate = getIntent().getStringExtra("coll_entry_date");

        if (!customerName.isEmpty()) {
            binding.customerName.setText(customerName);
        }

        if (customerNo != null) {
            binding.customerNo.setText(customerNo);
        }

        if (date != null) {
            binding.date.setText(date);
        }

        if (session != null) {
            binding.session.setText(session);
        }

        if (milkType != null) {
            binding.milkType.setText(milkType);
        }

        if (cans != null) {
            binding.noOfCans.setText(cans);
        }

        if (milkWeight != null) {
            binding.milkWeight.setText(milkWeight);
        }
        if (totalMilkQty != null) {
            binding.totalMilkQuantity.setText(totalMilkQty);
        }
        if (sampleNo != null) {
            binding.milkSampleNo.setText(sampleNo);
        }
        if (fat != null) {
            binding.fat.setText(fat);
        }
        if (snf != null) {
            binding.snf.setText(snf);
        }
        if (clr != null) {
            binding.clr.setText(clr);
        }
        if (milkRate != null) {
            binding.milkRate.setText(milkRate);
        }
        if (totalAmount != null) {
            binding.totalAmount.setText(totalAmount);
        }
        if (collectionEntryDate != null) {
            binding.collDate.setText(collectionEntryDate);
        }
    }
}