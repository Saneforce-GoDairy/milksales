package com.saneforce.milksales.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;
import com.saneforce.milksales.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CateenToken extends AppCompatActivity {
    public static final String UserDetail = "MyPrefs";
    private static final String TAG = "MYQR";
    SharedPreferences UserDetails;
    ImageView qrImage, btnProfile, imgProfile, imgHome;
    TextView txtHQName, txtSFName, txtEmpID, btnCanteen;

    String imagePath, CheckInfo = "CheckInDetail";
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cateen_token);
        qrImage = findViewById(R.id.imgQR);
        txtHQName = findViewById(R.id.HQName);
        txtSFName = findViewById(R.id.empName);
        txtEmpID = findViewById(R.id.empcode);
        imgHome = findViewById(R.id.toolbar_home);


        btnCanteen = findViewById(R.id.btnCanteen);
        btnProfile = findViewById(R.id.btnProfile);
        imgProfile = findViewById(R.id.imgProf);

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                SharedPreferences CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
//                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
//                if (CheckIn == true) {
//                    Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
//                    Dashboard.putExtra("Mode", "CIN");
//                    startActivity(Dashboard);
//                } else
//                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });

        btnCanteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent frmCanteen = new Intent(CateenToken.this, foodExp.class);
                startActivity(frmCanteen);
                finish();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent frmCanteen = new Intent(CateenToken.this, ImageCapture.class);
                frmCanteen.putExtra("Mode", "PF");
                startActivity(frmCanteen);
                //finish();
            }
        });
//        ImageCapture.setOnImagePickListener(new OnImagePickListener() {
//            @Override
//            public void OnImagePick(Bitmap image, String FileName) {
//                imgProfile.setImageBitmap(image);
//                SharedPreferences.Editor editor = UserDetails.edit();
//                editor.putString("Profile", String.valueOf(FileName));
//                editor.apply();
//            }
//        });
        imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/sfProfile.jpg";
        file = new File(imagePath);
        try {
            Uri uri = Uri.fromFile(file);
            imgProfile.setImageURI(uri);
        } catch (Exception e) {
        }
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        String HQCode = UserDetails.getString("SFHQCode", "1000");
        String EmpID = UserDetails.getString("EmpId", "");

        String sHQName = UserDetails.getString("SFHQ", "");
        String sSFNm = UserDetails.getString("SfName", "");

        txtHQName.setText(sHQName);
        txtSFName.setText(sSFNm);
        txtEmpID.setText(EmpID);
        String inputValue = EmpID;//HQCode+EmpID;
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);

        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);

            String ProfPath = UserDetails.getString("ProfPath", "");
            String URL = UserDetails.getString("Profile", "");
            if (!(ProfPath + URL).equalsIgnoreCase("")) {
                new ImageDownloader().execute(ProfPath + URL);
            }
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }
    }

    private void save(Bitmap bm) throws IOException {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        OutputStream outputStream = null;
        outputStream = new FileOutputStream(file);
        outputStream.write(byteArray);
        outputStream.close();
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                  // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                  // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            Log.i("Async-Example", "onPreExecute Called");

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Log.i("Async-Example", "onPostExecute Called");
            try {
                if (result != null) save(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgProfile.setImageBitmap(result);
        }
    }
}