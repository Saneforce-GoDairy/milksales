package com.saneforce.godairy.common;

import static com.saneforce.godairy.common.AppConstants.PROCUREMENT_POST_COLL_CENTER_LOCATION;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import com.saneforce.godairy.R;
import com.saneforce.godairy.procurement.CollectionCenterLocationActivity;

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

    @Override
    public void onCreate() {
        createNotificationChannel();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     // do heavy work on background thread
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

        // upload date
        String mDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String mTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String mTimeDate  = mDate +" "+mTime;

        String dir = getExternalFilesDir("/").getPath() + "/" + "procurement/";
        File file_image = new File(dir, "CC_123" + ".jpg");

        ProgressRequestBody progressRequestBody = new ProgressRequestBody(file_image);
        MultipartBody.Part thumbnailPart = MultipartBody.Part.createFormData("image",file_image.getName(),progressRequestBody);

        ApiInterface apiInterface = ApiClient.getClientThirumala().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.submitProcCollectionCenterLo(PROCUREMENT_POST_COLL_CENTER_LOCATION,
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
                        Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
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
        // stopself
        return START_NOT_STICKY;
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
