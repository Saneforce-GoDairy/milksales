package com.saneforce.godairy.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityPaymentResultBinding;

public class PaymentResult extends AppCompatActivity {
    ActivityPaymentResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String status = getIntent().getStringExtra("status");
        String transactionId = getIntent().getStringExtra("transactionId");
        String transactionAmount = getIntent().getStringExtra("transactionAmount");
        String transactionDate = getIntent().getStringExtra("transactionDate");

        binding.status.setText(status);
        binding.transactionId.setText(transactionId);
        binding.transactionAmount.setText(transactionAmount);
        binding.transactionDate.setText(transactionDate);

        if (status.equalsIgnoreCase("success")) {
            binding.paymentSuccessLL.setVisibility(View.VISIBLE);
            binding.paymentFailedLL.setVisibility(View.GONE);
        } else {
            binding.paymentSuccessLL.setVisibility(View.GONE);
            binding.paymentFailedLL.setVisibility(View.VISIBLE);
        }

        binding.close.setOnClickListener(v -> finish());
    }
}