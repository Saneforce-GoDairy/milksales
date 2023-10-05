package com.saneforce.milksales.Common_Class;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.saneforce.milksales.Interface.AlertBox;

public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId != -1) {
                MyAlertDialog.show(context,"", "File downloaded successfully...", false, "Okay", "", new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }
}

