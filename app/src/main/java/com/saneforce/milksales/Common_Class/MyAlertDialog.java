package com.saneforce.milksales.Common_Class;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.saneforce.milksales.Interface.AlertBox;

public class MyAlertDialog {
    AlertDialog.Builder builder;
    Context context;

    public MyAlertDialog(Context context) {
        this.context = context;
    }

    public void show(String title, String message, boolean isCancelable, String positiveButton, String negativeButton, AlertBox listener) {
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(isCancelable);
        if (!title.isEmpty()) {
            builder.setTitle(title);
        }
        if (!message.isEmpty()) {
            builder.setMessage(message);
        }
        if (!positiveButton.isEmpty()) {
            builder.setPositiveButton(positiveButton, listener::PositiveMethod);
        }
        if (!negativeButton.isEmpty()) {
            builder.setNegativeButton(negativeButton, listener::NegativeMethod);
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
