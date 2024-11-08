package com.saneforce.godairy.Common_Class;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;

import com.saneforce.godairy.Interface.AlertBox;


public class AlertDialogBox {
    public static void showDialog(Context context, String title, String message, String positiveBtnCaption, String negativeBtnCaption, boolean isCancelable, final AlertBox target) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(Html.fromHtml( message)).setCancelable(false).setPositiveButton(positiveBtnCaption, (dialog, id) -> target.PositiveMethod(dialog, id)).setNegativeButton(negativeBtnCaption, (dialog, id) -> target.NegativeMethod(dialog, id));
        AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.show();
        if (isCancelable) {
            alert.setOnCancelListener(arg0 -> target.NegativeMethod(null, 0));
        }
    }

}
