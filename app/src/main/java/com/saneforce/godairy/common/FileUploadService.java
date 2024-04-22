package com.saneforce.godairy.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.saneforce.godairy.Common_Class.Util;
import com.saneforce.godairy.Interface.ApiClient;
import com.saneforce.godairy.Interface.ApiInterface;
import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.MediaType;

public class FileUploadService extends JobIntentService {
    private static final String TAG = "FileUploadService: ";
    Disposable mDisposable;
    static TransferUtility transferUtility;
    static Util util;
    String mFilePath,mSF,FileName,Mode;
    public static final String MyPREFERENCES = "MyPrefs";
    private static final int JOB_ID = 102;

    public enum MIMEType {
        IMAGE("image/*"), VIDEO("video/*");
        public final String value;

        MIMEType(String value) {
            this.value = value;
        }
    }

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, FileUploadService.class, JOB_ID, intent);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        util = new Util();
        transferUtility = util.getTransferUtility(this);
    }
    private void UploadPhoto(){
        try{
            if (mFilePath == null) {
                Log.e(TAG, "onHandleWork: Invalid file URI");
                return;
            }

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            @SuppressLint("CheckResult") Flowable<Double> fileObservable = Flowable.create(emitter -> {
                apiInterface.onFileUpload(mSF,FileName,Mode,
                        createMultipartBody(mFilePath, emitter)).blockingGet();
                emitter.onComplete();
            }, BackpressureStrategy.LATEST);
            mDisposable = fileObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onProgress, this::onErrors,
                        this::onSuccess);
        }
        catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

        if (Mode.equals("PROF" )){
          updateProfileSession();
        }
        if (Mode.equals("PF")){
            updateProfileSession();
        }
    }

    private void updateProfileSession() {
        String mBasePath = "https://admin.godairy.in/SalesForce_Profile_Img/" + FileName;
        SharedPreferences userDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor userEditor = userDetails.edit();
        userEditor.putString("Profile", mBasePath);
        userEditor.apply();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        mFilePath = intent.getStringExtra("mFilePath");
        mSF = intent.getStringExtra("SF");
        FileName=intent.getStringExtra("FileName");
        Mode=intent.getStringExtra("Mode");

        try (DatabaseHandler db = new DatabaseHandler(FileUploadService.this)) {
            db.addPhotoDetails(FileName.replaceAll(".jpg", ""), mSF, Mode, FileName, mFilePath);
        }
        UploadPhoto();
    }
    private void onErrors(Throwable throwable) {
            sendOtherPhotos();
        Log.e(TAG, "onErrors: ", throwable);
    }
    private void onProgress(Double progress) {
        //sendBroadcastMeaasge("Uploading in progress... " + (int) (100 * progress));
        Log.i(TAG, "onProgress: " + progress);
    }
    private void onSuccess() {
        sendOtherPhotos();
        sendBroadcastMeaasge("File uploading successful ");
    }
public void sendOtherPhotos(){
    JSONArray pendingPhotos;
    try (DatabaseHandler db = new DatabaseHandler(FileUploadService.this)) {
        db.deletePhotoDetails(FileName.replaceAll(".jpg", ""));
        pendingPhotos = db.getAllPendingPhotos();
    }
    if(pendingPhotos.length()>0){
        try {
            JSONObject itm=pendingPhotos.getJSONObject(0);
            mFilePath=itm.getString("FileURI");
            mSF=itm.getString("SFCode");
            FileName= itm.getString("FileName");
            Mode= itm.getString("Mode");
            UploadPhoto();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
    public void sendBroadcastMeaasge(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    @NonNull
    @Contract("_, _ -> new")
    private RequestBody createRequestBodyFromFile(File file, String mimeType) {
        return RequestBody.create(MediaType.parse(mimeType), file);
    }

    @NonNull
    private MultipartBody.Part createMultipartBody(String filePath, FlowableEmitter<Double> emitter) {
        File file = new File(filePath);
        return MultipartBody.Part.createFormData("file", file.getName(),
                createCountingRequestBody(file, MIMEType.IMAGE.value, emitter));
    }

    @NonNull
    private RequestBody createCountingRequestBody(File file, String mimeType,
                                                  FlowableEmitter<Double> emitter) {
        RequestBody requestBody = createRequestBodyFromFile(file, mimeType);
        return new CountingRequestBody(requestBody, (bytesWritten, contentLength) -> {
            double progress = (1.0 * bytesWritten) / contentLength;
            emitter.onNext(progress);
        });
    }
}
