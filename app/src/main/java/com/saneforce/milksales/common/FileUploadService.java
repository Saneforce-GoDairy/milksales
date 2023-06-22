package com.saneforce.milksales.common;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.saneforce.milksales.Common_Class.Util;
import com.saneforce.milksales.Interface.ApiClient;
import com.saneforce.milksales.Interface.ApiInterface;

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
    // Reference to the utility class
    static Util util;

    String mFilePath,mSF,FileName,Mode;
    /**
     * Unique job ID for this service.
     */
    public enum MIMEType {
        IMAGE("image/*"), VIDEO("video/*");
        public String value;

        MIMEType(String value) {
            this.value = value;
        }
    }
    private static final int JOB_ID = 102;
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

            final File file = new File(mFilePath);
           /* TransferObserver uploadObserver =
                    transferUtility.upload("happic","TAPhotos/" + FileName , file);

            uploadObserver.setTransferListener(new TransferListener() {

                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        Toast.makeText(getApplicationContext(), "Upload Completed!", Toast.LENGTH_SHORT).show();
                        sendOtherPhotos();
                    } else if (TransferState.FAILED == state) {

                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int) percentDonef;

                    //tvFileName.setText("ID:" + id + "|bytesCurrent: " + bytesCurrent + "|bytesTotal: " + bytesTotal + "|" + percentDone + "%");
                }

                @Override
                public void onError(int id, Exception ex) {
                    ex.printStackTrace();
                }

            });*/
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Flowable<Double> fileObservable = Flowable.create(emitter -> {
                apiInterface.onFileUpload(mSF,FileName,Mode,
                        createMultipartBody(mFilePath, emitter)).blockingGet();
                emitter.onComplete();
            }, BackpressureStrategy.LATEST);
            mDisposable = fileObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(progress -> onProgress(progress), throwable -> onErrors(throwable),
                        () -> onSuccess());
        }
        catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        mFilePath = intent.getStringExtra("mFilePath");
        mSF = intent.getStringExtra("SF");
        FileName=intent.getStringExtra("FileName");
        Mode=intent.getStringExtra("Mode");

        DatabaseHandler db=new DatabaseHandler(FileUploadService.this);
        db.addPhotoDetails(FileName.replaceAll(".jpg",""),mSF,Mode,FileName,mFilePath);

        UploadPhoto();
        /*
        if (mFilePath == null) {
            Log.e(TAG, "onHandleWork: Invalid file URI");
            return;
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Flowable<Double> fileObservable = Flowable.create(emitter -> {
            apiInterface.onFileUpload(mSF,FileName,Mode,
                    createMultipartBody(mFilePath, emitter)).blockingGet();
            emitter.onComplete();
        }, BackpressureStrategy.LATEST);
        mDisposable = fileObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(progress -> onProgress(progress), throwable -> onErrors(throwable),
                        () -> onSuccess());*/
    }
    private void onErrors(Throwable throwable) {
    //sendBroadcastMeaasge("Error in file upload " + throwable.getMessage());
        //if(throwable.getMessage().indexOf("No such file or directory")>-1){
            //sendBroadcastMeaasge(throwable.getMessage());

            sendOtherPhotos();
        //}else {
            //if(Err>3)UploadPhoto();
        //}

        //ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        /*apiInterface.sendUpldPhotoErrorMsg("send/photouplerr",throwable.getMessage())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });*/
        Log.e(TAG, "onErrors: ", throwable);
    }
    private void onProgress(Double progress) {
        //sendBroadcastMeaasge("Uploading in progress... " + (int) (100 * progress));
        Log.i(TAG, "onProgress: " + progress);
    }
    private void onSuccess() {
        sendOtherPhotos();

        sendBroadcastMeaasge("File uploading successful ");
        Log.i(TAG, "onSuccess: File Uploaded");

    }
public void sendOtherPhotos(){
    DatabaseHandler db=new DatabaseHandler(FileUploadService.this);
    db.deletePhotoDetails(FileName.replaceAll(".jpg",""));

    JSONArray pendingPhotos=db.getAllPendingPhotos();
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
     //   Intent localIntent = new Intent("my.own.broadcast");
     //   localIntent.putExtra("result", message);
     //   LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
    private RequestBody createRequestBodyFromFile(File file, String mimeType) {
        return RequestBody.create(MediaType.parse(mimeType), file);
    }
    private RequestBody createRequestBodyFromText(String mText) {
        return RequestBody.create(MediaType.parse("text/plain"), mText);
    }
    /**
     * return multi part body in format of FlowableEmitter
     */
    private MultipartBody.Part createMultipartBody(String filePath, FlowableEmitter<Double> emitter) {
        File file = new File(filePath);
        return MultipartBody.Part.createFormData("file", file.getName(),
                createCountingRequestBody(file, MIMEType.IMAGE.value, emitter));
    }
    private RequestBody createCountingRequestBody(File file, String mimeType,
                                                  FlowableEmitter<Double> emitter) {
        RequestBody requestBody = createRequestBodyFromFile(file, mimeType);
        return new CountingRequestBody(requestBody, (bytesWritten, contentLength) -> {
            double progress = (1.0 * bytesWritten) / contentLength;
            emitter.onNext(progress);
        });
    }
}
