package com.saneforce.godairy.Common_Class;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressDialog {
    static ProgressDialog progressDialog;

    public static void show(Context context, String title, String message, boolean isCancelable) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setCancelable(isCancelable);
        if (!title.isEmpty()) {
            progressDialog.setTitle(title);
        }
        if (!message.isEmpty()) {
            progressDialog.setMessage(message);
        }
        progressDialog.show();
    }

    public static void dismiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
