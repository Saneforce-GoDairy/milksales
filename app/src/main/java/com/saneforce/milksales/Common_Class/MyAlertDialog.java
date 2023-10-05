package com.saneforce.milksales.Common_Class;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.saneforce.milksales.Interface.AlertBox;

public class MyAlertDialog {

    public static void show(Context context, String title, String message, boolean isCancelable, String positiveButton, String negativeButton, AlertBox listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
