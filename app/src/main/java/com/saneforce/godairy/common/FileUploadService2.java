package com.saneforce.godairy.common;

import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_SUBMIT_AGENT_VISIT;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_SUBMIT_AGRONOMIST;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_SUBMIT_COLL_CENTER_LOCATION;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_SUBMIT_FARMER_CREATION;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_SUBMIT_VETERINARY;
import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_SUBMIT_AIT;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
import com.saneforce.godairy.R;
import com.saneforce.godairy.procurement.AITFormActivity;
import com.saneforce.godairy.procurement.AgronomistFormActivity;
import com.saneforce.godairy.procurement.CollectionCenterLocationActivity;
import com.saneforce.godairy.procurement.ExistingAgentVisitActivity;
import com.saneforce.godairy.procurement.FarmerCreationActivity;
import com.saneforce.godairy.procurement.VeterinaryDoctorsFormActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
                case 1:
                    procCollectionCenter(intent);
                    break;

                case 2:
                    procFarmerCreation(intent);
                    break;

                case 3:
                    procAgronomist(intent);
                    break;

                case 4:
                    procVeterinary(intent);
                    break;

                case 5:
                    procAIT(intent);
                    break;

                case 6:
                    procAgentVisit(intent);
                    break;

                default:
                    Toast.makeText(context, "service id is empty", Toast.LENGTH_SHORT).show();
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
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopForeground(true);
                }
            }, 5000);
        }

        // stopself
        return START_NOT_STICKY;
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

        ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);

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
                        throw new RuntimeException(e);
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

        ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);

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
                        throw new RuntimeException(e);
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

        String mProductType = intent.getStringExtra("product_type");
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

        ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcVeterinary(PROCUREMENT_SUBMIT_VETERINARY,
                                                                    mCompany,
                                                                    mPlant,
                mCenterName,
                mFarmerName,
                mServiceType,
                imagePart1,
                mProductType,
                mSeedSale,
                mMineralMixture,
                mFodderSetts,
                mCattleFeed,
                mTeatDip,
                mEvm,
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
                        throw new RuntimeException(e);
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

        ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);

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
                        throw new RuntimeException(e);
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
        String mFarmerAddress = intent.getStringExtra("farmer_address");
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

        ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);

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

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String res;
                    showUploadCompleteNotification();
                    stopForeground(true);
                    try {
                        res = response.body().string();
                        Toast.makeText(context, "Farmer creation form submit success", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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

        ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);

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
                        throw new RuntimeException(e);
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
