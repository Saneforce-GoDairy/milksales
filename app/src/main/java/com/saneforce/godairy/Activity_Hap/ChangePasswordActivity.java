package com.saneforce.godairy.Activity_Hap;

import static com.saneforce.godairy.R.id.designation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private SharedPreferences UserDetails;
    public static final String UserDetail = "MyPrefs";
    public static final String MY_PREFERENCES = "MyPrefs";
    private EditText edOldPassword, edNewPassword, edConfirmPassword;
    private String oldPassword, newPassword, conFirmPassword;
    private Button btnUpdate;
    ImageView user_image;
    private final Context context = this;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edOldPassword = findViewById(R.id.et_old_password);
        edNewPassword = findViewById(R.id.et_new_password);
        edConfirmPassword = findViewById(R.id.et_confirm_password);
        Button updateButton = findViewById(R.id.btn_update);
        user_image = findViewById(R.id.user_image);

        SharedPreferences userDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        TextView txtUserName = findViewById(R.id.user_name);
        TextView  tvdesignation = findViewById(designation);
        String sUName = userDetails.getString("SfName","");
        String SFDesig = userDetails.getString("SFDesig","");

        txtUserName.setText(sUName);
        tvdesignation.setText(SFDesig);

        user_image.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ProfileActivity.class);
            startActivity(intent);
        });

        updateButton.setOnClickListener(v -> {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            oldPassword = edOldPassword.getText().toString();
            newPassword = edNewPassword.getText().toString();
            conFirmPassword = edConfirmPassword.getText().toString();

            if ("".equals(oldPassword)) {
                edOldPassword.setError("Enter Old Password");
                edOldPassword.requestFocus();
                return;
            }
            if ("".equals(newPassword)) {
                edNewPassword.setError("Please enter a valid new password. It must be at least 6 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
                edNewPassword.requestFocus();
                return;
            }
            if ("".equals(conFirmPassword)) {
                edConfirmPassword.setError("Enter Confirm Password");
                edConfirmPassword.requestFocus();
                return;
            }
            if (newPassword.equals(conFirmPassword)) {
                updatePassword();
                return;
            }
            Toast.makeText(context, "New Password  and confirm password does not match", Toast.LENGTH_SHORT).show();
        });
    }

    private void updatePassword() {
        UserDetails = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String sfCode = UserDetails.getString("Sfcode", "");
        String divCode = UserDetails.getString("Divcode", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.changePassword("change_password",
                sfCode,
                divCode,
                oldPassword,
                newPassword);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String mResponse;
                    try {
                        mResponse = response.body().string();

                        JSONObject object = new JSONObject(mResponse);

                        String status = object.getString("response");
                        String message = object.getString("msg");

                        if (Boolean.getBoolean(status)) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    }

