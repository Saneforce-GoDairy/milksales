package com.saneforce.godairy.Activity_Hap;



import static com.saneforce.godairy.Common_Class.Constants.Change_Password;
import static com.saneforce.godairy.SFA_Activity.HAPApp.printUsrLog;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.BuildConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.saneforce.godairy.Common_Class.Common_Class;
import com.saneforce.godairy.Common_Class.Constants;
import com.saneforce.godairy.Common_Class.Shared_Common_Pref;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Interface.UpdateResponseUI;
import com.saneforce.godairy.Model_Class.Model;
import com.saneforce.godairy.R;
import com.saneforce.godairy.universal.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ChangePasswordActivity extends AppCompatActivity implements UpdateResponseUI {

    private SharedPreferences UserDetails;
    public static final String UserDetail = "MyPrefs";
    private SharedPreferences.Editor editors;
    public static final String MyPREFERENCES = "MyPrefs";
    private EditText oldPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private Shared_Common_Pref mShared_common_pref;

    private TextView tvUserName,etOldPassword,etNewPassword,etConfirmPassword;

    private final Context context = this;

    private Button btn_update;
    private ImageView userImage;
    private String mProfileUrl;

    private Common_Class common_class;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        oldPasswordEditText = findViewById(R.id.et_old_password);
        newPasswordEditText = findViewById(R.id.et_new_password);
        confirmPasswordEditText = findViewById(R.id.et_confirm_password);
        tvUserName = findViewById(R.id.user_name);
        userImage = findViewById(R.id.user_image);
        Button updateButton = findViewById(R.id.btn_update);
        common_class = new Common_Class(this);
        String mProfileImage = UserDetails.getString("Profile", "");
        String sUName = UserDetails.getString("SfName", "");

        userImage.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
            intent.putExtra("ImageUrl", mProfileImage);
            startActivity(intent);

        });

        updateButton.setOnClickListener(view -> updatePassword());

        tvUserName.setText(UserDetails.getString("SfName", ""));
//        userImage.setImageBitmap(mProfileImage);
        mShared_common_pref = new Shared_Common_Pref(this);

        mProfileUrl = mShared_common_pref.getvalue("mProfile");
        Log.e("hgfhg", mProfileUrl);
        if (!com.saneforce.godairy.Common_Class.Common_Class.isNullOrEmpty(mProfileUrl)) {
            String[] image = mProfileUrl.split("/");
            if (image.length > 0 && image[(image.length - 1)].contains(".")) {
                loadImage(mProfileUrl);
            }
        }
    }

    private void loadImage(String mProfileUrl) {
        Glide.with(this.context)
                .load(mProfileUrl)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userImage);
    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null){
                switch (key){
                    case Change_Password:
                        JSONObject changepassword = new JSONObject(apiDataResponse);
                        JSONArray arr = changepassword.getJSONArray("Data");
                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
             Log.e("error", e.toString());
        }

    }

/*    private void updatePasswordApi(String response) {
        try {
//        JSONObject jsonObject = new JSONObject(response);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.changePassword(
                "change_Password",
                Shared_Common_Pref.Sf_Code,
                Shared_Common_Pref.mOldPassword,
                Shared_Common_Pref.mNewPasswpord);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res;
                    try {
                        res = response.body().string();
                        Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("error", e.toString());
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        } catch (Exception e) {
            Log.v("error", e.getMessage());
        }
    }*/
/*public void changePsw(){
    JsonArray jsonArray = new JsonArray();
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("username",tvUserName.getText().toString());
    jsonObject.addProperty("oldpassword",etOldPassword.getText().toString());
    jsonObject.addProperty("newpassword",etNewPassword.getText().toString());
    jsonObject.addProperty("confirmpassword",etConfirmPassword.getText().toString());

    JsonObject jsonObject1 = new JsonObject();
    jsonObject1.add("ChangePassword",jsonObject);
    jsonArray.add(jsonObject1);
    printUsrLog("MyProfileFragment","jsonArray:"+jsonArray);



    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
 */
    /*   Call<ResponseBody> call = request.saveJSONArray(toRequestBody(jsonArray),"dcr/save",*//*
    Call<ResponseBody> call = apiInterface.changePassword(
            Constant.DIVISION_CODE,
            Constant.SF_CODE,
            Constant.STATE_CODE,
            Constant.DESIG);
    call.enqueue(new Callback<ResponseBody>() {

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            try {
                String responseBody = response.body().string();

                printUsrLog("MyProfileFragment", "Change password responseBody:" + responseBody);
                if (responseBody != null) {
                    try {
                        JSONObject jsonArray = new JSONObject(responseBody);

                        if (jsonArray.getBoolean("success")) {
                            Toast.makeText(context, "Password Change Successfully", Toast.LENGTH_SHORT).show();
//                            logout();
                        } else {
                            Toast.makeText(context, "Response : null", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            call.cancel();
            Toast.makeText(context, "Exception 2 " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    });
}*/


    @Override
    public void onErrorData(String msg) {
        UpdateResponseUI.super.onErrorData(msg);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public class Adapter extends RecyclerView.Adapter<Dashboard.Adapter.ViewHolder> {
        ArrayList exploreImage, exploreName;
        Context context;

        public Adapter(Context context, ArrayList courseImg, ArrayList courseName) {
            this.context = context;
            this.exploreImage = courseImg;
            this.exploreName = courseName;
        }

        @NonNull
        @Override
        public Dashboard.Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull Dashboard.Adapter.ViewHolder holder, int position) {
            int res = (int) exploreImage.get(position);
            holder.images.setImageResource(res);
            holder.text.setText((String) exploreName.get(position));
        }
        @Override
        public int getItemCount() {
            return exploreImage.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView images;
            TextView text;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                images = itemView.findViewById(R.id.image);
                text = itemView.findViewById(R.id.name);
            }
        }
    }
 /*   public void changePsw(){
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username",tvUserName.getText().toString());
        jsonObject.addProperty("oldpassword",etOldPassword.getText().toString());
        jsonObject.addProperty("newpassword",etNewPassword.getText().toString());
        jsonObject.addProperty("confirmpassword",etConfirmPassword.getText().toString());

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.add("ChangePassword",jsonObject);
        jsonArray.add(jsonObject1);
        printUsrLog("MyProfileFragment","jsonArray:"+jsonArray);



        ApiInterface request = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = request.saveJSONArray(toRequestBody(jsonArray),"dcr/save",
                Constant.DIVISION_CODE,
                Constant.SF_CODE,
                Constant.STATE_CODE,
                Constant.DESIG);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseBody = response.body().string();

                    printUsrLog("MyProfileFragment", "Change password responseBody:" + responseBody);
                    if (responseBody != null) {
                        try {
                            JSONObject jsonArray = new JSONObject(responseBody);

                            if (jsonArray.getBoolean("success")) {
                                Toast.makeText(getContext(), "Password Change Successfully", Toast.LENGTH_SHORT).show();
                                logout();
                            } else {
                                Toast.makeText(getContext(), "Response : null", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Exception 2 " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }*/


       private void updatePasswordApi(String oldPassword,String newpassword) {
           ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);
           Call<ResponseBody> call = apiInterface.changePassword(
                   "Change_Password",
                   Constants.Sf_Code,
                   oldPassword,
                   newpassword,
                   Constants.div_Code);
           call.enqueue(new Callback<>() {
               @Override
               public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                   if (response.isSuccessful()) {
                       String res;
                       try {
                           res = response.body().string();
                           Log.e("login", "res " + response);
                           Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                       } catch (IOException e) {
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
  private void updatePassword(){
        String oldPassword = oldPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

   /*   editor = UserDetails.edit();
      editor.putString("old_password",oldPassword);
      editor.putString("new_password",newPassword);
      editor.commit();
      editor.apply();*/

        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            showAlertDialog("Fields Required", "Some field are empty");

        }else if (!isValidOldPassword(oldPassword)) {
            showAlertDialog("Invalid Old Password", "Please enter a valid old password.");

        }else if (!isValidPassword(newPassword)) {
            showAlertDialog("Invalid New Password", "Please enter a valid new password. It must be at least 6 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");

        } else if (!newPassword.equals(confirmPassword)) {
            showAlertDialog("Password Mismatch", "New password and confirm password do not match.");
//            Toast.makeText(this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();

        }else if (newPassword.equals(confirmPassword)) {


            updatePasswordApi(oldPassword,newPassword);

        }


        // Your code to update the password goes here

        // Show success message
//      Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();




        // Clear the input fields

    }


    // Api function
//    private  void ChangePassword

    private boolean isValidOldPassword(String oldPassword) {
        return !TextUtils.isEmpty(oldPassword) && oldPassword.length() >= 6;
    }


    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!,%,&,@#$,^,?~]).{6,12}$";
        return Pattern.matches(passwordPattern, password);
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

}

