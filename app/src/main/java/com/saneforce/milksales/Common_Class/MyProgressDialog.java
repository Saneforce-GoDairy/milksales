package com.saneforce.milksales.Common_Class;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressDialog {
    ProgressDialog progressDialog;

    public MyProgressDialog(Context context) {
        this.progressDialog = new ProgressDialog(context);
    }

    public void show(String title, String message, boolean isCancelable) {
        progressDialog.setCancelable(isCancelable);
        if (!title.isEmpty()) {
            progressDialog.setTitle(title);
        }
        if (!message.isEmpty()) {
            progressDialog.setMessage(message);
        }
        progressDialog.show();
    }

    public void dismiss() {
        progressDialog.dismiss();
    }

}
