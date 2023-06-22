package com.saneforce.milksales.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.milksales.R;
import com.saneforce.milksales.common.SANGPSTracker;

public class Block_Information extends AppCompatActivity {
    Button openDateSetting;
    TextView lblMsg,lblInfo;
    String mMode = "",sMsg="",sHead= "";

    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_information);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Bundle params = getIntent().getExtras();
        mMode = "";sMsg="";sHead= "";
        if(params!=null) {
             mMode = params.getString("Mode", "");
             sMsg = params.getString("Msg", "");
             sHead = params.getString("Head", "");
        }
        lblMsg=findViewById(R.id.txtMsg);
        lblInfo=findViewById(R.id.txt_info);
        openDateSetting = (Button) findViewById(R.id.openDateTime);

        if(!sMsg.equalsIgnoreCase("")) lblMsg.setText(Html.fromHtml(sMsg));
        if(!sHead.equalsIgnoreCase("")) lblInfo.setText(Html.fromHtml(sHead));
        if(mMode.equalsIgnoreCase("FGPS")){
            openDateSetting.setText("Exit Application");
        }
        openDateSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMode.equalsIgnoreCase("FGPS")){
                    SharedPreferences.Editor editor = UserDetails.edit();
                    editor.putBoolean("Login", false);
                    editor.apply();
                    Intent playIntent = new Intent(Block_Information.this, SANGPSTracker.class);
                    stopService(playIntent);
                    finishAffinity();
                }else {
                    Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                    startActivity(intent);
                }
            }
        });
    }
}