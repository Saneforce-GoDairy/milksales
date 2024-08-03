package com.saneforce.godairy.procurement.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.saneforce.godairy.R;
import com.saneforce.godairy.SFA_Activity.Printama;

public class CallPreviewActivity2 extends AppCompatActivity {
    public static CallPreviewActivity2 mCallPreviewActivity2;
    private final Context context = this;
    private int paperSize = 80;
    private final String TAG = "MilkCollEntryActivity_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_preview2);
        mCallPreviewActivity2 = this;
    }

    public void printBill() {
        try {

            Bitmap logo = Printama2.getBitmapFromVector(this, R.drawable.godairy_logo_jpeg);
            Printama2.with(context, paperSize).connect(printama -> {

                printama.setWideTallBold();
                printama.setTallBold();
                printama.printTextln(Printama.CENTER, "Godairy");
                printama.addNewLine();
                printama.setNormalText();
                printama.printTextln(Printama.LEFT, "Prasanth");
                printama.setNormalText();
                printama.printTextln(Printama.LEFT,"Mob No : "+ "8940570614");
                printama.setBold();
                if(paperSize==80||paperSize==102) {
                    printama.printLine();
                }else{
                    printama.printSmallLine();
                }
                printama.addNewLine();
                if(paperSize==80||paperSize==102) {
                    printama.printLine();
                }else{
                    printama.printSmallLine();
                }

                printama.setBold();
                printama.addNewLine();
                printama.setLineSpacing(5);
                printama.feedPaper();
                printama.close();
            });
        } catch (Exception e) {
            Log.e(TAG, "Error CallPreviewActivity2 - PrintBill : " + e.getMessage());
        }
    }
}