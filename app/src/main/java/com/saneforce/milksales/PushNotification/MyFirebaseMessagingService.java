package com.saneforce.milksales.PushNotification;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.saneforce.milksales.Activity_Hap.Login;
import com.saneforce.milksales.Interface.OnLiveUpdateListener;
import com.saneforce.milksales.SFA_Activity.HAPApp;

import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    static OnLiveUpdateListener liveUpdateListener;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;


        Log.v("MESSAGE_DATA", remoteMessage.getData().toString());
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject();
                handleDataMessage(remoteMessage.getData());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message

            if(message.equalsIgnoreCase("reloadSale")){
                Log.d("ActiveActivity",HAPApp.activeActivity.getClass().toString());
                if(HAPApp.activeActivity.getClass().toString().equalsIgnoreCase("Dashboard_Route")){
                    Log.d("ActiveActivity","Dashboard_Route");
                    liveUpdateListener.onUpdate(message);
                }
            }else {
                Log.e("PUSH_NOtificaiton", message);
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                // play notification sound
       /*     NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();*/
            }
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(Map<String, String> data) {
        Log.e(TAG, "push json: " + data);

        try {

            String title = data.get("title");
            String message = data.get("text");
/*            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");*/

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
/*            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);*/

            if(message.equalsIgnoreCase("reloadSale")) {
                if (HAPApp.activeActivity.getClass().getSimpleName().equalsIgnoreCase("Dashboard_Route")) {
                    Log.d("ActiveActivity", "Dashboard_Route");
                    Log.d("ActiveActivity", "Dashboard_Route");
                    liveUpdateListener.onUpdate(message);
                }
            }else{
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Log.d("ActiveActivity", HAPApp.activeActivity.getClass().toString());
                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                    showNotificationMessage(getApplicationContext(), title, message, "timestamp", pushNotification);
                    // play notification sound
               /* NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();*/
                } else {
                    // app is in background, show the notification in notification tray
                    Intent resultIntent = new Intent(getApplicationContext(), Login.class);
                    resultIntent.putExtra("message", message);
                    showNotificationMessage(getApplicationContext(), title, message, "timestamp", resultIntent);
                    // check for image attachment
               /* if (TextUtils.isEmpty(imageUrl)) {

                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }*/
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    public static void setOnLiveUpdateListener(OnLiveUpdateListener mliveUpdateListener){
        liveUpdateListener=mliveUpdateListener;
    }
}
