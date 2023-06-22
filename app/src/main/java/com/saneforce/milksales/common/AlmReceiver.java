package com.saneforce.milksales.common;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.res.Resources;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String sID=intent.getExtras().getString("ID");
        String stitle=intent.getExtras().getString("Title");
        String msg=intent.getExtras().getString("Message");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "AlarmID_"+sID;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //@SuppressLint("WrongConstant")
            NotificationChannel notificationChannel
                    = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Shift Time Notifications", NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            notificationChannel.setDescription("Shift Time Notifications Channel");
            notificationChannel.enableLights(true);
            //notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(getIconResId(context)) // notification icon
                //.setTicker("Tutorialspoint")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(stitle)
                .setContentText(msg)
                .setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());




/*

        //Toast.makeText(context, "Alarm Called...", Toast.LENGTH_LONG).show();
NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(context,"Checkin_01")
            .setSmallIcon(getIconResId(context)) // notification icon
            .setContentTitle("Notification!") // title for notification
            .setContentText("Hello word") // message for notification
            .setAutoCancel(true); // clear notification after click
//Intent intent = new Intent(this, MainActivity.class);
PendingIntent pi = PendingIntent.getActivity(context,0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
mBuilder.setContentIntent(pi);
NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
mNotificationManager.notify(0, mBuilder.build());*/

        /*String stitle=intent.getExtras().getString("Title");
        String msg=intent.getExtras().getString("Message");

        createNotification("1231",stitle,msg,context,intent);*/
        //Context context = this.cordova.getActivity().getApplicationContext();
       /*  Notification.Builder mBuilder = new Notification.Builder(context);
    String stitle=intent.getExtras().getString("Title");
    String msg=intent.getExtras().getString("Message");
        mBuilder.setSmallIcon(getIconResId(context));
        mBuilder.setContentTitle(stitle);
        mBuilder.setContentText(msg);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        //.setFullScreenIntent(fullScreenPendingIntent, true);

        NotificationManager mNotificationManager =

            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());*/
    }
    private int getIconResId(Context context) {
        //Context context = this.cordova.getActivity().getApplicationContext();
        Resources res   = context.getResources();
        String pkgName  = context.getPackageName();
        String icon     = "icon";

        // cordova-android 6 uses mipmaps
        int resId = getIconResId(res, icon, "mipmap", pkgName);
        if (resId == 0) {
            resId = getIconResId(res, icon, "drawable", pkgName);
        }

        return resId;
    }

    private int getIconResId(Resources res, String icon,
                             String type, String pkgName) {

        int resId = res.getIdentifier(icon, type, pkgName);

        if (resId == 0) {
            resId = res.getIdentifier("ic_launcher", type, pkgName);
        }

        return resId;
    }
  /*  private NotificationManager notifManager;
    public void createNotification(String id, String title, String aMessage, Context context, Intent intent) {
        final int NOTIFY_ID = 0; // ID of notification
        //String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
        //String title = context.getString(R.string.default_notification_channel_title); // Default Channel

        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            //intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                   .setSmallIcon(getIconResId(context))  // required
                   .setContentText(aMessage) // required
                   .setDefaults(Notification.DEFAULT_ALL)
                   .setAutoCancel(true)
                   .setContentIntent(pendingIntent)
                   .setTicker(aMessage)
                   .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(context, id);
            //intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                   .setSmallIcon(getIconResId(context))
                   //.setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                   .setContentText(aMessage) // required
                   .setDefaults(Notification.DEFAULT_ALL)
                   .setAutoCancel(true)
                   .setContentIntent(pendingIntent)
                   .setTicker(aMessage)
                   .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                   .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }*/
}
