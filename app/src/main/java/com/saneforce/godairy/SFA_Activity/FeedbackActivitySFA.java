package com.saneforce.godairy.SFA_Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Interface.APIResult;
import com.saneforce.godairy.Interface.AlertDialogClickListener;
import com.saneforce.godairy.assistantClass.AssistantClass;
import com.saneforce.godairy.databinding.ActivityFeedbackSfaBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivitySFA extends AppCompatActivity {
    ActivityFeedbackSfaBinding binding;
    AssistantClass assistantClass;
    Common_Class common_class;
    Context context = this;
    String title = "Feedback Form";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedbackSfaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        assistantClass = new AssistantClass(context);
        common_class = new Common_Class(this);

        binding.toolbar.back.setOnClickListener(view -> onBackPressed());
        binding.toolbar.title.setText(title);
        common_class.gotoHomeScreen(context, binding.toolbar.home);

        binding.submit.setOnClickListener(v -> assistantClass.showAlertDialog("", "Are you sure you want to submit?", true, "Yes", "No", new AlertDialogClickListener() {
            @Override
            public void onPositiveButtonClick(DialogInterface dialog) {
                dialog.dismiss();
                int deliveryCheckedId = binding.deliveryGroup.getCheckedRadioButtonId();
                int issueCheckedId = binding.issueGroup.getCheckedRadioButtonId();
                int damageCheckedId = binding.damageGroup.getCheckedRadioButtonId();
                String feedbackText = binding.feedback.getText().toString().trim();
                String deliveryStatus = getCheckedRadioButtonText(deliveryCheckedId);
                String issueStatus = getCheckedRadioButtonText(issueCheckedId);
                String damageStatus = getCheckedRadioButtonText(damageCheckedId);
                submitFeedback(deliveryStatus, issueStatus, damageStatus, feedbackText);
            }

            @Override
            public void onNegativeButtonClick(DialogInterface dialog) {
                dialog.dismiss();
            }
        }));
    }

    private String getCheckedRadioButtonText(int checkedRadioButtonId) {
        RadioButton radioButton = findViewById(checkedRadioButtonId);
        if (radioButton != null) {
            return radioButton.getText().toString();
        } else {
            return " - ";
        }
    }

    private void submitFeedback(String deliveryStatus, String issueStatus, String damageStatus, String feedbackText) {
        assistantClass.showProgressDialog("Submitting...", false);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "submit_feedback_form");
        params.put("deliveryStatus", deliveryStatus);
        params.put("issueStatus", issueStatus);
        params.put("damageStatus", damageStatus);
        params.put("feedbackText", feedbackText);
        assistantClass.makeApiCall(params, "", new APIResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithFinish(jsonObject.optString("msg"));
            }

            @Override
            public void onFailure(String error) {
                assistantClass.dismissProgressDialog();
                assistantClass.showAlertDialogWithDismiss(error);
            }
        });
    }
}
