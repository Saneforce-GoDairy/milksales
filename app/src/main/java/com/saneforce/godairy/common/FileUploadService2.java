package com.saneforce.godairy.common;

import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_MAINTENANCE_REGULAR;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SAVE_AGENT;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SAVE_FARMER_CEATION;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SAVE_MILK_COLLECTION;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SUBMIT_AGENT_VISIT;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SUBMIT_AGRONOMIST;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SUBMIT_ASSET;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SUBMIT_COLL_CENTER_LOCATION;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SUBMIT_EXISTING_CENTER_VISIT;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SUBMIT_EXISTING_FARMER_VISIT_SKA;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SUBMIT_FARMER_CREATION;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SUBMIT_FARMER_CREATION_SKA;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SUBMIT_MAINTENANCE;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SUBMIT_QUALITY;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SUBMIT_VETERINARY;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_SUBMIT_AIT;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_UPDATE_AGENT;
import static com.saneforce.godairy.procurement.AppConstants.PROCUREMENT_UPDATE_FARMER;
import static com.saneforce.godairy.procurement.AppConstants.PROC_COLL_CENTER_CR2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.Model_Class.Procurement;
import com.saneforce.godairy.R;
import com.saneforce.godairy.procurement.AITFormActivity;
import com.saneforce.godairy.procurement.AgentUpdateActivity;
import com.saneforce.godairy.procurement.AgronomistFormActivity;
import com.saneforce.godairy.procurement.CollectionCenterLocationActivity;
import com.saneforce.godairy.procurement.ExistingAgentVisitActivity;
import com.saneforce.godairy.procurement.ExistingCenterVisitActivity;
import com.saneforce.godairy.procurement.FarmerCreationActivity;
import com.saneforce.godairy.procurement.FarmerUpdateActivity;
import com.saneforce.godairy.procurement.MaintanenceIssuesFormActivity;
import com.saneforce.godairy.procurement.MaintanenceRegularActivity;
import com.saneforce.godairy.procurement.ProcurementAssetActivity;
import com.saneforce.godairy.procurement.QualityFormActivity;
import com.saneforce.godairy.procurement.VeterinaryDoctorsFormActivity;
import com.saneforce.godairy.procurement.ska.ExistingFarmerVisitActivity;
import com.saneforce.godairy.procurement.ska.NewFarmerCreationActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploadService2 extends Service {
    private long totalFileUploaded = 0;
    private long totalFileLength = 0;
    private FileUploadService2.FileUploaderCallback fileUploaderCallback;
    public static final String CHANNEL_ID = "ForegroundSrviceChannel";
    Context context = this;
    Notification notification;

    // upload date
    String mDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    String mTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    String mTimeDate  = mDate +" "+mTime;

    @Override
    public void onCreate() {
        createNotificationChannel();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do heavy work on background thread

        String service_id = intent.getStringExtra("upload_service_id");

        if (service_id != null){
            switch (Integer.parseInt(service_id)){
                case 19:
                    newCollCenterCrea(intent);
                    break;
                case 18:
                    farmerUpdate(intent);
                    break;

                case 17:
                    agentUpdation(intent);
                    break;

                case 16:
                    milkCollEntry(intent);
                    break;

                case 15:
                    farmerCreation2(intent);
                    break;

                case 14:
                    agentCreation(intent);
                    break;

                case 13:
                    procExistingFarmerSka(intent);
                    break;

                case 12:
                    procFarmerCreationSka(intent);
                    break;

                case 11:
                    procExistingCenterVisit(intent);
                    break;

                case 10:
                    procMaintenanceRegular(intent);
                    break;

                case 9:
                    procAsset(intent);
                    break;

                case 8:
                    procMaintenanceIssue(intent);
                    break;

                case 7:
                    procQuality(intent);
                    break;

                case 6:
                    procAgentVisit(intent);
                    break;

                case 5:
                    procAIT(intent);
                    break;

                case 4:
                    procVeterinary(intent);
                    break;

                case 3:
                    procAgronomist(intent);
                    break;

                case 2:
                    procFarmerCreation(intent);
                    break;

                case 1:
                    procCollectionCenter(intent);
                    break;

                default:
                    Toast.makeText(context, "Error! form submitting. service id is empty", Toast.LENGTH_SHORT).show();
                    break;
            }

        }else {
            //  Intent notificationIntent = new Intent(this, CollectionCenterLocationActivity.class);
            Intent Intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , Intent, PendingIntent.FLAG_IMMUTABLE);

            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setOnlyAlertOnce(true)
                    .setContentTitle("File uploading error")
                    .setContentText("Service id is missing")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);
            Toast.makeText(context, "Service id is empty", Toast.LENGTH_SHORT).show();

            Handler handler = new Handler();
            handler.postDelayed(() -> stopForeground(true), 5000);
        }

        // stopself
        return START_NOT_STICKY;
    }

    private void newCollCenterCrea(Intent intent) {
        String mCenterName = intent.getStringExtra("center_name");
        String mState = intent.getStringExtra("state");
        String mDistrict = intent.getStringExtra("district");
        String mPlant = intent.getStringExtra("plant");
        String mAddr1 = intent.getStringExtra("addr1");
        String mAddr2 = intent.getStringExtra("addr2");
        String mAddr3 = intent.getStringExtra("addr3");

        String mOwnerName = intent.getStringExtra("owner_name");
        String mOwnerAddr1 = intent.getStringExtra("owner_addr1");
        String mOwnerPinCode = intent.getStringExtra("owner_pincode");

        String mMobile = intent.getStringExtra("mobile");
        String mEmail = intent.getStringExtra("email");

        String mBusinessAddr = mAddr1 + " " + mAddr2 + " " + mAddr3;

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";

        File file = new File(dir, "CENP_123" + ".jpg");
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",file.getName(),progressRequestBody);

        Intent notificationIntent = new Intent(this, FarmerUpdateActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.proCollCenterCr(PROC_COLL_CENTER_CR2,
                imagePart,
                mCenterName,
                mState,
                mDistrict,
                mPlant,
                mBusinessAddr,
                mOwnerName,
                mOwnerAddr1,
                mOwnerPinCode,
                mMobile,
                mEmail);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        String string = response.body().string();
                        Toast.makeText(context, "form update success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        //   throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FileUploadService2.this.stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void farmerUpdate(Intent intent) {
        String mId = intent.getStringExtra("id");
        String mFarmerName = intent.getStringExtra("farmer_name");
        String mState = intent.getStringExtra("state");
        String mDistrict = intent.getStringExtra("district");

        String mTown = intent.getStringExtra("town");
        String mCollCenter = intent.getStringExtra("coll_center");
        String mFaCategory = intent.getStringExtra("fa_category");

        String mAddr = intent.getStringExtra("addr");
        String mPinCode = intent.getStringExtra("pin_code");

        String mCity = intent.getStringExtra("city");
        String mMobile = intent.getStringExtra("mobile_no");

        String mEmail = intent.getStringExtra("email");
        String mIncentiveAmt = intent.getStringExtra("incentive_amt");
        String mCartageAmt = intent.getStringExtra("cartage_amt");

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";

        File file = new File(dir, "FAMC2_123" + ".jpg");
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",file.getName(),progressRequestBody);

        Intent notificationIntent = new Intent(this, FarmerUpdateActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.updateProFarmer(PROCUREMENT_UPDATE_FARMER,
                imagePart,
                mId,
                mFarmerName,
                mState,
                mDistrict,
                mTown,
                mCollCenter,
                mFaCategory,
                mAddr,
                mPinCode,
                mCity,
                mMobile,
                mEmail,
                mIncentiveAmt,
                mCartageAmt);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        String string = response.body().string();
                        Toast.makeText(context, "form update success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        //   throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                FileUploadService2.this.stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agentUpdation(Intent intent) {
        String mId = intent.getStringExtra("id");
        String mAgentName = intent.getStringExtra("agent_name");
        String mState = intent.getStringExtra("state");
        String mDistrict = intent.getStringExtra("district");
        String mTown = intent.getStringExtra("town");
        String mCollCenter = intent.getStringExtra("coll_center");
        String mAgentCategory = intent.getStringExtra("ag_category");
        String mCompany = intent.getStringExtra("company");
        String mAddress = intent.getStringExtra("addr");
        String mPinCode = intent.getStringExtra("pin_code");
        String mCity = intent.getStringExtra("city");
        String mMobileNo = intent.getStringExtra("mobile_no");
        String mEmail = intent.getStringExtra("email");
        String mIncentiveAmt = intent.getStringExtra("incentive_amt");
        String mCartageAmt = intent.getStringExtra("cartage_amt");

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";

        File file = new File(dir, "AGENT_CREAT_123" + ".jpg");
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",file.getName(),progressRequestBody);

        Intent notificationIntent = new Intent(this, AgentUpdateActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.updateProAgent(PROCUREMENT_UPDATE_AGENT,
                mId,
                imagePart,
                mAgentName,
                mState,
                mDistrict,
                mTown,
                mCollCenter,
                mAgentCategory,
                mCompany,
                mAddress,
                mPinCode,
                mCity,
                mMobileNo,
                mEmail,
                mIncentiveAmt,
                mCartageAmt);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        String string = response.body().string();
                        Toast.makeText(context, "form update success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        //   throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                FileUploadService2.this.stopForeground(true);
                Toast.makeText(FileUploadService2.this.context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void milkCollEntry(Intent intent) {
        String mActiveFlag = intent.getStringExtra("active_flag");
        String mSession = intent.getStringExtra("session");
        String mMilkType = intent.getStringExtra("milk_type");
        String mCustomerName = intent.getStringExtra("customer_name");
        String mCustomerNo = intent.getStringExtra("customer_no");
        String mMilkCollEntryDate = intent.getStringExtra("coll_entry_date");
        String mCans = intent.getStringExtra("cans");
        String mMilkWeight = intent.getStringExtra("milk_weight");
        String mTotalMilkQty = intent.getStringExtra("total_milk_qty");
        String mMilkSampleNo = intent.getStringExtra("milk_sample_no");
        String mFat = intent.getStringExtra("fat");
        String mSnf = intent.getStringExtra("snf");
        String mCLR = intent.getStringExtra("clr");
        String mMilkRate = intent.getStringExtra("milk_rate");
        String mTotalMilkAmount = intent.getStringExtra("total_milk_amt");

        Intent notificationIntent = new Intent(this, NewFarmerCreationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.saveProcMilkCollEntry(PROCUREMENT_SAVE_MILK_COLLECTION,
                mSession,
                mMilkType,
                mCustomerName,
                mCustomerNo,
                mCans,
                mMilkWeight,
                mTotalMilkQty,
                mMilkSampleNo,
                mFat,
                mSnf,
                mCLR,
                mMilkRate,
                mTotalMilkAmount,
                mTimeDate,
                mActiveFlag,
                mMilkCollEntryDate);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        String string = response.body().string();
                        Toast.makeText(context, "form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        //   throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                FileUploadService2.this.stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void farmerCreation2(Intent intent) {
        String mFarmerName = intent.getStringExtra("farmer_name");
        String mState = intent.getStringExtra("state");
        String mDistrict = intent.getStringExtra("district");
        String mTown = intent.getStringExtra("town");

        String mCollCenter = intent.getStringExtra("coll_center");
        String mFarmerCategory = intent.getStringExtra("fa_category");
        String mAddress = intent.getStringExtra("addr");
        String mPinCode = intent.getStringExtra("pin_code");

        String mCity = intent.getStringExtra("city");
        String mMobileNo = intent.getStringExtra("mobile_no");
        String mEmail = intent.getStringExtra("email");
        String mIncentiveAmt = intent.getStringExtra("incentive_amt");
        String mCartageAmt = intent.getStringExtra("cartage_amt");

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";

        File file = new File(dir, "FAMC2_123" + ".jpg");
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",file.getName(),progressRequestBody);

        Intent notificationIntent = new Intent(this, NewFarmerCreationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.saveProFarmerCrea2(PROCUREMENT_SAVE_FARMER_CEATION,
                imagePart,
                mFarmerName,
                mState,
                mDistrict,
                mTown,
                mCollCenter,
                mFarmerCategory,
                mAddress,
                mPinCode,
                mCity,
                mMobileNo,
                mEmail,
                mIncentiveAmt,
                mCartageAmt);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        String string = response.body().string();
                        Toast.makeText(context, "form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        //   throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                FileUploadService2.this.stopForeground(true);
                Toast.makeText(FileUploadService2.this.context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void agentCreation(Intent intent) {
        String mAgentName = intent.getStringExtra("agent_name");
        String mState = intent.getStringExtra("state");
        String mDistrict = intent.getStringExtra("district");
        String mTown = intent.getStringExtra("town");
        String mCollCenter = intent.getStringExtra("coll_center");
        String mAgentCategory = intent.getStringExtra("ag_category");
        String mCompany = intent.getStringExtra("company");
        String mAddress = intent.getStringExtra("addr");
        String mPinCode = intent.getStringExtra("pin_code");
        String mCity = intent.getStringExtra("city");
        String mMobileNo = intent.getStringExtra("mobile_no");
        String mEmail = intent.getStringExtra("email");
        String mIncentiveAmt = intent.getStringExtra("incentive_amt");
        String mCartageAmt = intent.getStringExtra("cartage_amt");

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";

        File file = new File(dir, "AGENT_CREAT_123" + ".jpg");
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",file.getName(),progressRequestBody);

        Intent notificationIntent = new Intent(this, NewFarmerCreationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.saveProAgent(PROCUREMENT_SAVE_AGENT,
                imagePart,
                mAgentName,
                mState,
                mDistrict,
                mTown,
                mCollCenter,
                mAgentCategory,
                mCompany,
                mAddress,
                mPinCode,
                mCity,
                mMobileNo,
                mEmail,
                mIncentiveAmt,
                mCartageAmt);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        String string = response.body().string();
                        Toast.makeText(context, "form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                     //   throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                FileUploadService2.this.stopForeground(true);
                Toast.makeText(FileUploadService2.this.context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void procExistingFarmerSka(Intent intent) {
        String mCustomer = intent.getStringExtra("customer");
        String mCustomerDetails = intent.getStringExtra("customer_d");
        String mPurposeOfVisit = intent.getStringExtra("purpose_of_visit");
        String mPrice = intent.getStringExtra("price");
        String mAsset = intent.getStringExtra("asset");
        String mCans = intent.getStringExtra("cans");
        String mRemarksType = intent.getStringExtra("remarks_type");
        String mRemarksText = intent.getStringExtra("remarks_text");
        String mActiveFlag = intent.getStringExtra("active_flag");

        /************************************************************************
         * For test purpose image uploading
         */

        String dir = getExternalFilesDir("/").getPath() + "/procurement/";

        // image
        File file_image = new File(dir, "SKA_NEW_CMTR_123" + ".jpg");
        ProgressRequestBody progressRequestBodyRepair = new ProgressRequestBody(file_image);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",file_image.getName(),progressRequestBodyRepair);

        /* *********************************************************************** */

        // audio
        File file_audio = new File(dir, "new_far_creation" + ".mp3");
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(file_audio);
        MultipartBody.Part audioPart = MultipartBody.Part.createFormData("audio",file_audio.getName(), progressRequestBody);


        Intent notificationIntent = new Intent(this, ExistingFarmerVisitActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcExistingFarmerVisitSka(PROCUREMENT_SUBMIT_EXISTING_FARMER_VISIT_SKA,
                audioPart,
                imagePart,
                mCustomer,
                mCustomerDetails,
                mPurposeOfVisit,
                mPrice,
                mAsset,
                mCans,
                mRemarksType,
                mRemarksText,
                mActiveFlag,
                mTimeDate);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        String string = response.body().string();
                        Toast.makeText(context, "form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                FileUploadService2.this.stopForeground(true);
                Toast.makeText(FileUploadService2.this.context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void procFarmerCreationSka(Intent intent) {
        String mName = intent.getStringExtra("name");
        String mVillage = intent.getStringExtra("village");
        String mType = intent.getStringExtra("type");
        String mCompetitor = intent.getStringExtra("competitor");
        String mRemarksType = intent.getStringExtra("remarks_type");
        String mRemarksText = intent.getStringExtra("remarks_text");
        String mActiveFlag = intent.getStringExtra("active_flag");

        String dir = getExternalFilesDir("/").getPath() + "/procurement/";

        // image
        File file_image = new File(dir, "SKA_NEW_CMTR_123" + ".jpg");
        ProgressRequestBody progressRequestBodyRepair = new ProgressRequestBody(file_image);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",file_image.getName(),progressRequestBodyRepair);

        // audio
        File file_audio = new File(dir, "new_far_creation" + ".mp3");
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(file_audio);
        MultipartBody.Part audioPart = MultipartBody.Part.createFormData("audio",file_audio.getName(), progressRequestBody);

        Intent notificationIntent = new Intent(this, NewFarmerCreationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.submitProcFarmerCreationSka(PROCUREMENT_SUBMIT_FARMER_CREATION_SKA,
                audioPart,
                imagePart,
                mName,
                mVillage,
                mType,
                mCompetitor,
                mRemarksType,
                mRemarksText,
                mActiveFlag,
                mTimeDate);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        String string = response.body().string();
                        Toast.makeText(context, "form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FileUploadService2.this.stopForeground(true);
                Toast.makeText(FileUploadService2.this.context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void procExistingCenterVisit(Intent intent) {
        String mPouringActivity = intent.getStringExtra("pouring_act");
        String mOpeningTime = intent.getStringExtra("opening_time");
        String mClosingTime = intent.getStringExtra("closing_time");
        String mNoOfFarmers = intent.getStringExtra("no_of_farmer");
        String mVolume = intent.getStringExtra("volume");

        String mAvgFAT = intent.getStringExtra("avg_fat");
        String mAvgSNF = intent.getStringExtra("avg_snf");
        String mAvgRate = intent.getStringExtra("avg_rate");
        String mNoOfCansLoad = intent.getStringExtra("cans_load");
        String mNoOfCansReturned = intent.getStringExtra("cans_returned");

        String mCattleFeed = intent.getStringExtra("cattle_feed");
        String mOtherStock = intent.getStringExtra("other_stock");
        String mEchoMilkClActivity = intent.getStringExtra("echo_milk_clean_activity");
        String mMachineCondition = intent.getStringExtra("machine_condition");
        String mLoanFarmerIssue = intent.getStringExtra("loan_farmer_issue");

        String mIssueFromFarmerSide = intent.getStringExtra("issue_frm_farmer_side");
        String mAssetVerification = intent.getStringExtra("asset_verification");
        String mRenameVillage = intent.getStringExtra("rename_village");
        String mActiveFlag = intent.getStringExtra("active_flag");

        Intent notificationIntent = new Intent(this, ExistingCenterVisitActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcExistingCenterVist(PROCUREMENT_SUBMIT_EXISTING_CENTER_VISIT,
                mPouringActivity,
                mOpeningTime,
                mClosingTime,
                mNoOfFarmers,
                mVolume,
                mAvgFAT,
                mAvgSNF,
                mAvgRate,
                mNoOfCansLoad,
                mNoOfCansReturned,
                mCattleFeed,
                mOtherStock,
                mEchoMilkClActivity,
                mMachineCondition,
                mLoanFarmerIssue,
                mIssueFromFarmerSide,
                mAssetVerification,
                mRenameVillage,
                mActiveFlag,
                mTimeDate);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res;
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        res = response.body().string();
                        Toast.makeText(context, "form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void procMaintenanceRegular(Intent intent) {
        String mCompany = intent.getStringExtra("company");
        String mPlant = intent.getStringExtra("plant");

        // bmc
        String mBmcNoHrsRunning = intent.getStringExtra("bmc_hrs_run");
        String mBmcVolumeCollect = intent.getStringExtra("bmc_volume_coll");
        String mCcNoHrsRunning = intent.getStringExtra("cc_hrs_running");
        String mCcVolumeCollect = intent.getStringExtra("cc_volume_coll");
        String mIBTRunningHrs = intent.getStringExtra("ibt_running_hrs");
        String mDgSetRunnings = intent.getStringExtra("dg_set_running");
        String mPowerFactor = intent.getStringExtra("power_factor");
        String mPipeline = intent.getStringExtra("pipeline_condition");
        String mLeakages = intent.getStringExtra("leakage");
        String mScale = intent.getStringExtra("scale");
        String mFuelConsStockPerBook = intent.getStringExtra("per_book");
        String mFuelConsStockPerPhysical = intent.getStringExtra("physical");
        String mETP = intent.getStringExtra("etp");
        String mHotWater = intent.getStringExtra("hot_water");
        String mFactoryLicenceIns = intent.getStringExtra("factory_license_ins");
        String mActiveFlag = intent.getStringExtra("active_flag");

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";

        // Maintenance regular image
        File file_image_repair = new File(dir, "MAIN_REGU_123" + ".jpg");
        ProgressRequestBody progressRequestBodyRepair = new ProgressRequestBody(file_image_repair);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",file_image_repair.getName(),progressRequestBodyRepair);

        File file_image_no_hrs = new File(dir, "MAIN_RE_NOHRS_123" + ".jpg");
        ProgressRequestBody progressRequestBodyNoHrs = new ProgressRequestBody(file_image_no_hrs);
        MultipartBody.Part imagePart1 = MultipartBody.Part.createFormData("image1",file_image_no_hrs.getName(),progressRequestBodyNoHrs);

        File file_image_as_per_book = new File(dir, "MAIN_RE_AS_PER_BOOK_123" + ".jpg");
        ProgressRequestBody progressRequestBodyAsPerBook = new ProgressRequestBody(file_image_as_per_book);
        MultipartBody.Part imagePart2 = MultipartBody.Part.createFormData("image2",file_image_as_per_book.getName(),progressRequestBodyAsPerBook);


        Intent notificationIntent = new Intent(this, MaintanenceRegularActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcMaintenRegular(PROCUREMENT_MAINTENANCE_REGULAR,
                mCompany,
                mPlant,
                mBmcNoHrsRunning,
                mBmcVolumeCollect,
                mCcNoHrsRunning,
                mCcVolumeCollect,
                mIBTRunningHrs,
                mDgSetRunnings,
                imagePart,
                mPowerFactor,
                mPipeline,
                mLeakages,
                mScale,
                mFuelConsStockPerBook,
                mFuelConsStockPerPhysical,
                mETP,
                mHotWater,
                mFactoryLicenceIns,
                mActiveFlag,
                mTimeDate,
                imagePart1,
                imagePart2);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res;
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        res = response.body().string();
                        Toast.makeText(context, "Maintenance regular form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void procAsset(Intent intent) {
        String mCompany = intent.getStringExtra("company");
        String mPlant = intent.getStringExtra("plant");
        String mAssetType = intent.getStringExtra("asset_type");
        String mComments = intent.getStringExtra("comments");
        String mActiveFlag = intent.getStringExtra("active_flag");

        Intent notificationIntent = new Intent(this, ProcurementAssetActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcAsset(PROCUREMENT_SUBMIT_ASSET,
                mCompany,
                mPlant,
                mAssetType,
                mComments,
                mActiveFlag,
                mTimeDate);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res;
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        res = response.body().string();
                        Toast.makeText(context, "Asset form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void procMaintenanceIssue(Intent intent) {
        String mCompany = intent.getStringExtra("company");
        String mPlant = intent.getStringExtra("plant");
        String mEquipment = intent.getStringExtra("equipment");
        String mRepairType = intent.getStringExtra("repair_type");
        String mActiveFlag = intent.getStringExtra("active_flag");
        String mOthers = intent.getStringExtra("others");

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";
        MultipartBody.Part imagePart1 = null;

        File fileSensorIssue = new File(dir, "MAIN_RE_123" + ".jpg");
        Bitmap bitmap1 = BitmapFactory.decodeFile(fileSensorIssue.getAbsolutePath());
        if (bitmap1 != null){
            ProgressRequestBody progressRequestBodyRepair = new ProgressRequestBody(fileSensorIssue);
            imagePart1 = MultipartBody.Part.createFormData("image1", fileSensorIssue.getName(), progressRequestBodyRepair);
        }

        File fileBoardImage = new File(dir, "MAIN_IS_BOARD_IS" + ".jpg");
        Bitmap bitmap2 = BitmapFactory.decodeFile(fileBoardImage.getAbsolutePath());
        if (bitmap2 != null) {
            ProgressRequestBody progressRequestBodyBoardIssue = new ProgressRequestBody(fileBoardImage);
            imagePart1 = MultipartBody.Part.createFormData("image1", fileBoardImage.getName(), progressRequestBodyBoardIssue);
        }

        File fileSMPS = new File(dir, "MAIN_SMBS_123" + ".jpg");
        Bitmap bitmap3 = BitmapFactory.decodeFile(fileSMPS.getAbsolutePath());
        if (bitmap3 != null){
            ProgressRequestBody progressRequestBodySMBS = new ProgressRequestBody(fileSMPS);
            imagePart1 = MultipartBody.Part.createFormData("image1",fileSMPS.getName(),progressRequestBodySMBS);
        }

        File fileMotor = new File(dir, "MAIN_MOTOR_123" + ".jpg");
        Bitmap bitmap4 = BitmapFactory.decodeFile(fileMotor.getAbsolutePath());
        if (bitmap4 != null){
            ProgressRequestBody progressRequestBodyMotors = new ProgressRequestBody(fileMotor);
            imagePart1 = MultipartBody.Part.createFormData("image1",fileMotor.getName(),progressRequestBodyMotors);
        }

        File file_scale = new File(dir, "MAIN_WEIGH_SC_123" + ".jpg");
        Bitmap bitmap5 = BitmapFactory.decodeFile(fileMotor.getAbsolutePath());
        if (bitmap5 != null){
            ProgressRequestBody progressRequestBodyScale = new ProgressRequestBody(file_scale);
            imagePart1 = MultipartBody.Part.createFormData("image1",file_scale.getName(),progressRequestBodyScale);
        }

        Intent notificationIntent = new Intent(this, MaintanenceIssuesFormActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);


        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcMaintenance(PROCUREMENT_SUBMIT_MAINTENANCE,
                mCompany,
                mPlant,
                mEquipment,
                mRepairType,
                mActiveFlag,
                mTimeDate,
                imagePart1,
                mOthers);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res;
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        res = response.body().string();
                        Toast.makeText(context, "Maintenance form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void procQuality(Intent intent) {
        String mCompany = intent.getStringExtra("company");
        String mPlant = intent.getStringExtra("plant");
        String mMassBalance = intent.getStringExtra("mass_balance");
        String mMilkColl = intent.getStringExtra("milk_collection");
        String mMbrt = intent.getStringExtra("mbrt");
        String mRejection = intent.getStringExtra("rejection");
        String mSplCleaning = intent.getStringExtra("spl_cleaning");
        String mCleaningEff = intent.getStringExtra("cleaning_efficiency");
        String mVehicleWithHood = intent.getStringExtra("vehicle_with_hood");
        String mVehicleWithoutHood = intent.getStringExtra("vehicle_without_hood");
        String mChemicals = intent.getStringExtra("chemicals");
        String mStocks = intent.getStringExtra("stock");
        String mMilk = intent.getStringExtra("milk");
        String mAwsProgram = intent.getStringExtra("awareness_program");
        String mNoOfFat = intent.getStringExtra("no_of_fat");
        String mNoOfSnf = intent.getStringExtra("no_of_snf");
        String mNoOfWeight = intent.getStringExtra("no_of_weight");
        String mActiveFlag = intent.getStringExtra("active_flag");

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";

        // Quality fat image
        File file_image_fat = new File(dir, "QUA_FAT_123" + ".jpg");
        ProgressRequestBody progressRequestBodyFat = new ProgressRequestBody(file_image_fat);
        MultipartBody.Part imagePart1 = MultipartBody.Part.createFormData("image1",file_image_fat.getName(),progressRequestBodyFat);

        // Quality snf image
        File file_image_snf = new File(dir, "QUA_SNF_123" + ".jpg");
        ProgressRequestBody progressRequestBodySnf = new ProgressRequestBody(file_image_snf);
        MultipartBody.Part imagePart2 = MultipartBody.Part.createFormData("image2",file_image_snf.getName(),progressRequestBodySnf);

        // With hood image
        File file_image_withhood = new File(dir, "QUA_RNV_WITH_HOODS_123" + ".jpg");
        ProgressRequestBody progressRequestBodyWithHood = new ProgressRequestBody(file_image_withhood);
        MultipartBody.Part imagePart3 = MultipartBody.Part.createFormData("image3",file_image_withhood.getName(),progressRequestBodyWithHood);

        // Without hood image
        File file_image_withouthood = new File(dir, "QUA_RNV_WITHOUT_HOODS_123" + ".jpg");
        ProgressRequestBody progressRequestBodyWithOutHood = new ProgressRequestBody(file_image_withouthood);
        MultipartBody.Part imagePart4 = MultipartBody.Part.createFormData("image4",file_image_withouthood.getName(),progressRequestBodyWithOutHood);

        Intent notificationIntent = new Intent(this, QualityFormActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcQuality(PROCUREMENT_SUBMIT_QUALITY,
                mCompany,
                mPlant,
                mMassBalance,
                mMilkColl,
                imagePart1,
                imagePart2,
                mMbrt,
                mRejection,
                mSplCleaning,
                mCleaningEff,
                mVehicleWithHood,
                imagePart3,
                mVehicleWithoutHood,
                imagePart4,
                mChemicals,
                mStocks,
                mMilk,
                mAwsProgram,
                mNoOfFat,
                mNoOfSnf,
                mNoOfWeight,
                mActiveFlag,
                mTimeDate);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res;
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        res = response.body().string();
                        Toast.makeText(context, "Quality form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void procAgentVisit(Intent intent) {
        String mAgentVisit = intent.getStringExtra("visit_agent");
        String mCompany = intent.getStringExtra("company");
        String mTotalMilkAvai = intent.getStringExtra("total_milk_available");
        String mOurCompLtrs = intent.getStringExtra("our_company_ltrs");
        String mCompetitorRate = intent.getStringExtra("competitor_rate");
        String mCompanyRate = intent.getStringExtra("our_company_rate");
        String mDemand = intent.getStringExtra("demand");
        String mSupplyStartDate = intent.getStringExtra("supply_start_dt");
        String mActiveFlag = intent.getStringExtra("active_flag");

        Intent notificationIntent = new Intent(this, ExistingAgentVisitActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcAgentVisit(PROCUREMENT_SUBMIT_AGENT_VISIT,
                mAgentVisit,
                mCompany,
                mTotalMilkAvai,
                mOurCompLtrs,
                mCompetitorRate,
                mCompanyRate,
                mDemand,
                mSupplyStartDate,
                mActiveFlag,
                mTimeDate);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res;
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        res = response.body().string();
                        Toast.makeText(context, "Agent visit form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void procAIT(Intent intent) {
        String mCompany = intent.getStringExtra("company");
        String mPlant = intent.getStringExtra("plant");
        String mCenterName = intent.getStringExtra("center_name");
        String mFarmerCode = intent.getStringExtra("farmer_name_code");
        String mBreedName = intent.getStringExtra("breed_name");

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";

        // Breed image
        File file_image_service_type = new File(dir, "NOB_123" + ".jpg");
        ProgressRequestBody progressRequestBodyServiceType = new ProgressRequestBody(file_image_service_type);
        MultipartBody.Part imagePart1 = MultipartBody.Part.createFormData("image1",file_image_service_type.getName(),progressRequestBodyServiceType);

        String mServiceAI = intent.getStringExtra("service_type_ai");
        String mNoOfBullNos = intent.getStringExtra("service_type2");
        String mPdVerification = intent.getStringExtra("pd_verification");
        String mCalfbirthVeri = intent.getStringExtra("calfbirth_verification");
        String mMineralMix = intent.getStringExtra("mineral_mixture_kg");
        String mSeedSales = intent.getStringExtra("seed_sales");
        String mActiveFlag = intent.getStringExtra("active_flag");

        Intent notificationIntent = new Intent(this, AITFormActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcAIT(PROCUREMENT_SUBMIT_AIT,
                mCompany,
                mPlant,
                mCenterName,
                mFarmerCode,
                mBreedName,
                imagePart1,
                mServiceAI,
                mNoOfBullNos,
                mPdVerification,
                mCalfbirthVeri,
                mMineralMix,
                mSeedSales,
                mActiveFlag,
                mTimeDate);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res;
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        res = response.body().string();
                        Toast.makeText(context, "AIT form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void procVeterinary(Intent intent) {
        String mCompany = intent.getStringExtra("company");
        String mPlant = intent.getStringExtra("plant");
        String mCenterName = intent.getStringExtra("center_name");
        String mFarmerName = intent.getStringExtra("farmer_name");
        String mServiceType = intent.getStringExtra("service_type");

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";

        // Service type image
        File file_image_service_type = new File(dir, "VET_TOS_123" + ".jpg");
        ProgressRequestBody progressRequestBodyServiceType = new ProgressRequestBody(file_image_service_type);
        MultipartBody.Part imagePart1 = MultipartBody.Part.createFormData("image1",file_image_service_type.getName(),progressRequestBodyServiceType);

        // EVM image
        File file_image_evm = new File(dir, "VET_EVM_123" + ".jpg");
        ProgressRequestBody progressRequestBodyEVM = new ProgressRequestBody(file_image_evm);
        MultipartBody.Part imagePart2 = MultipartBody.Part.createFormData("image2",file_image_evm.getName(),progressRequestBodyEVM);

        String mSeedSale = intent.getStringExtra("seed_sale");
        String mMineralMixture = intent.getStringExtra("mineral_mixture");
        String mFodderSetts = intent.getStringExtra("fodder_setts_sale_kg");
        String mCattleFeed = intent.getStringExtra("cattle_feed_order_kg");
        String mTeatDip = intent.getStringExtra("teat_dip_cup");
        String mEvm = intent.getStringExtra("evm_treatment");
        String mCaseType = intent.getStringExtra("case_type");
        String mFarmerCount = intent.getStringExtra("identified_farmer_count");
        String mFarmerEnrolled = intent.getStringExtra("farmer_enrolled");
        String mFarmerInducted = intent.getStringExtra("farmer_inducted");
        String mActiveFlag = intent.getStringExtra("active_flag");

        Intent notificationIntent = new Intent(this, VeterinaryDoctorsFormActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcVeterinary(PROCUREMENT_SUBMIT_VETERINARY,
                mCompany,
                mPlant,
                mCenterName,
                mFarmerName,
                mServiceType,
                imagePart1,
                mSeedSale,
                mMineralMixture,
                mFodderSetts,
                mCattleFeed,
                mTeatDip,
                mEvm,
                imagePart2,
                mCaseType,
                mFarmerCount,
                mFarmerEnrolled,
                mFarmerInducted,
                mActiveFlag,
                mTimeDate);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res;
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        res = response.body().string();
                        Toast.makeText(context, "Veterinary form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void procAgronomist(Intent intent) {
        String mCompany = intent.getStringExtra("company");
        String mPlant = intent.getStringExtra("plant");
        String mCenterName = intent.getStringExtra("center_name");
        String mFarmerName = intent.getStringExtra("farmer_name");
        String mProductType = intent.getStringExtra("product_type");
        String mTeatDip = intent.getStringExtra("teat_dip");
        String mServiceType = intent.getStringExtra("service_type");

        String mFarmerEnrolled = intent.getStringExtra("farmer_enrolled");
        String mFarmerInducted = intent.getStringExtra("farmer_inducted");

        String mActiveFlag = intent.getStringExtra("active_flag");

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";

        // Farmer meeting image
        File file_image_farmer_meeting = new File(dir, "FAR_123" + ".jpg");
        ProgressRequestBody progressRequestBodyFarmerMeeting = new ProgressRequestBody(file_image_farmer_meeting);
        MultipartBody.Part imagePart1 = MultipartBody.Part.createFormData("image1",file_image_farmer_meeting.getName(),progressRequestBodyFarmerMeeting);

        // CSR image
        File file_image_csr = new File(dir, "CSR_123" + ".jpg");
        ProgressRequestBody progressRequestBodyCSR = new ProgressRequestBody(file_image_csr);
        MultipartBody.Part imagePart2 = MultipartBody.Part.createFormData("image2",file_image_csr.getName(),progressRequestBodyCSR);

        // Fodder image
        File file_image_fodder = new File(dir, "FDA_123" + ".jpg");
        ProgressRequestBody progressRequestBodyFodder = new ProgressRequestBody(file_image_fodder);
        MultipartBody.Part imagePart3 = MultipartBody.Part.createFormData("image3",file_image_fodder.getName(),progressRequestBodyFodder);

        String mFodderDevAcres = intent.getStringExtra("fodder_dev_acres");

        Intent notificationIntent = new Intent(this, AgronomistFormActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcAgronomist(
                PROCUREMENT_SUBMIT_AGRONOMIST,
                mCompany,
                mPlant,
                mCenterName,
                mFarmerName,
                mProductType,
                mTeatDip,
                mServiceType,
                imagePart1,
                imagePart2,
                mFodderDevAcres,
                imagePart3,
                mFarmerEnrolled,
                mFarmerInducted,
                mActiveFlag,
                mTimeDate);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res;
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        res = response.body().string();
                        Toast.makeText(context, "Farmer agronomist form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void procFarmerCreation(Intent intent) {
        String mCenter = intent.getStringExtra("center");
        String mFarmerGategory = intent.getStringExtra("farmer_gategory");
        String mFarmerName = intent.getStringExtra("farmer_name");
        String mFarmerAddress = intent.getStringExtra("farmer_addr");
        String mPhoneNumber = intent.getStringExtra("phone_number");
        String mPinCode = intent.getStringExtra("pin_code");
        String mBuffaloTotal = intent.getStringExtra("buffalo_total");
        String mCowTotal = intent.getStringExtra("cow_total");
        String mCowMilkAvailable = intent.getStringExtra("cow_available_ltrs");
        String mBuffaloMilkAvailable = intent.getStringExtra("buffalo_available_ltrs");
        String mMilkSupplyCompany = intent.getStringExtra("milk_supply_company");
        String mInterestedSupply = intent.getStringExtra("interested_supply");
        String mActiveFlag = intent.getStringExtra("active_flag");

        Intent notificationIntent = new Intent(this, FarmerCreationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";
        File file_image = new File(dir, "FAMC_123" + ".jpg");

        ProgressRequestBody progressRequestBody = new ProgressRequestBody(file_image);
        MultipartBody.Part thumbnailPart = MultipartBody.Part.createFormData("image",file_image.getName(),progressRequestBody);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcFarmerCreation(
                PROCUREMENT_SUBMIT_FARMER_CREATION,
                mCenter,
                mFarmerGategory,
                mFarmerName,
                mFarmerAddress,
                mPhoneNumber,
                mPinCode,
                mCowTotal,
                mBuffaloTotal,
                mCowMilkAvailable,
                mBuffaloMilkAvailable,
                mMilkSupplyCompany,
                mInterestedSupply,
                mActiveFlag,
                mTimeDate,
                thumbnailPart);

        Log.e("rr__", "sssss = 1");
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e("rr__", "sssss = 2");
                    String res;
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        res = response.body().string();
                        Toast.makeText(context, "Farmer creation form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void procCollectionCenter(Intent intent) {
        String mCompany = intent.getStringExtra("company");
        String mPlant = intent.getStringExtra("plant");
        String mCenterCode = intent.getStringExtra("center_code");
        String mCenterName = intent.getStringExtra("center_name");
        String mCenterAddr = intent.getStringExtra("center_addr");
        String mPotentialLpd = intent.getStringExtra("potential_lpd");
        String mFarmersEnrolled = intent.getStringExtra("farmers_enrolled");
        String mCompetitorLpd = intent.getStringExtra("competitor_lpd");
        String mCompetitorLpd1 = intent.getStringExtra("competitor_lpd1");
        String mActiveFlag = intent.getStringExtra("active_flag");

        Intent notificationIntent = new Intent(this, CollectionCenterLocationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Procurement uploading")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";
        File file_image = new File(dir, "CC_123" + ".jpg");

        ProgressRequestBody progressRequestBody = new ProgressRequestBody(file_image);
        MultipartBody.Part thumbnailPart = MultipartBody.Part.createFormData("image",file_image.getName(),progressRequestBody);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcCollectionCenterLo(PROCUREMENT_SUBMIT_COLL_CENTER_LOCATION,
                mCompany,
                mPlant,
                mCenterCode,
                mCenterName,
                mCenterAddr,
                mPotentialLpd,
                mFarmersEnrolled,
                mCompetitorLpd,
                mCompetitorLpd1,
                mActiveFlag,
                mTimeDate,
                thumbnailPart);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res;
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        res = response.body().string();
                        Toast.makeText(context, "Collection form submitted success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        Toast.makeText(context, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                stopForeground(true);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class ProgressRequestBody extends RequestBody {
        private File mFile;

        private static final int DEFAULT_BUFFER_SIZE = 2048;

        public ProgressRequestBody(final File file){
            mFile = file;
        }

        @Override
        public MediaType contentType() {
            return MediaType.parse("video/*");
        }

        @Override
        public long contentLength() throws IOException {
            return mFile.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            long fileLenth = mFile.length();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream in = new FileInputStream(mFile);
            long uploaded = 0;

            try {
                int read;
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = in.read(buffer)) != -1){

                    // update progress on UI thread
                    handler.post(new ProgressUpdater(uploaded, fileLenth));
                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            }finally {
                in.close();
            }
        }
    }

    private class ProgressUpdater implements Runnable{
        private long mUploaded;
        private long mTotal;

        public ProgressUpdater(long uploaded, long total){
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            int current_percent = (int) (100 * mUploaded/mTotal);
            int total_percent = (int) (100 * (totalFileUploaded + mUploaded));
            // fileUploaderCallback.onProgressUpdate(current_percent, total_percent,uploadIndex+1 );
            Log.e("upload_" , String.valueOf(current_percent));

            // Notification progressbar
            updateNotification(current_percent);
        }
    }

    private void updateNotification(int progress) {
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setContentTitle("File uploading service")
                .setContentText("Uploading: " + progress + "%")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();

        startForeground(1, notification);
    }

    private void showUploadCompleteNotification() {
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Upload Complete")
                .setContentText("Upload has been completed successfully.")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
        }
    }

    public interface FileUploaderCallback{
        void onProgressUpdate(int currentpercent, int totalpercent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
