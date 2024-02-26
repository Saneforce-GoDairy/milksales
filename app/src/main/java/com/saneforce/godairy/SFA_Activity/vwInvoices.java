package com.saneforce.godairy.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.saneforce.godairy.R;

public class vwInvoices extends AppCompatActivity {
    TextView txtPONo,txtIndentNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vw_invoices);
        Intent i=getIntent();
        txtIndentNo=findViewById(R.id.txtIndentNo);
        txtIndentNo.setText(i.getExtras().getString("PONO"));
        txtPONo=findViewById(R.id.txtPONo);
        txtPONo.setText(i.getExtras().getString("salno"));
    }
}