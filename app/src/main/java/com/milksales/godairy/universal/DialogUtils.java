package com.milksales.godairy.universal;

import static android.content.Context.POWER_SERVICE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.google.gson.JsonObject;
import com.milksales.godairy.Common_Class.Constants;
import com.milksales.godairy.SFA_Activity.HAPApp;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.milksales.godairy.R;

import okhttp3.ResponseBody;

public class DialogUtils {

    private static final String TAG = DialogUtils.class.getSimpleName();
    public static Dialog dialog;
    public static Dialog permissionDialog;

    public static Dialog getDialogInstance(Context context){
        if (dialog!=null)
            return dialog;
        else
            dialog = new Dialog(context);

        return dialog;
    }
    public static void showDialog(){
        if(dialog!=null && !dialog.isShowing())
            dialog.show();
    }
    public static void cancelDialog(){
        if(dialog!=null && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }

    public static void showLocationPermissionDialog(Context context){
        if(!HAPApp.getApplication().isAppInForeground)
            return;

        if(permissionDialog ==null)
            permissionDialog = new Dialog(context);

        if(permissionDialog.isShowing())
            return;

        permissionDialog.setContentView(R.layout.dialog_background_location);

        CheckBox checkBox = permissionDialog.findViewById(R.id.cb_dont_show);
        Button btnCancel = permissionDialog.findViewById(R.id.btn_cancel);
        Button btnConfirm = permissionDialog.findViewById(R.id.btn_confirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked())
                    Constant.getInstance().setValue(true, Constants.DONT_SHOW_LOCATION_PERMISSION_DIALOG);

                permissionDialog.dismiss();

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionDialog.dismiss();

                if (!PermissionUtils.hasGivenBackgroundPermission(HAPApp.getApplication())) {
                        PermissionUtils.checkLocation(HAPApp.getActiveActivity());
                }
                if (checkBox.isChecked())
                    Constant.getInstance().setValue(true, Constants.DONT_SHOW_LOCATION_PERMISSION_DIALOG);

            }
        });

        try {
            if(HAPApp.getApplication().isAppInForeground)
                permissionDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
