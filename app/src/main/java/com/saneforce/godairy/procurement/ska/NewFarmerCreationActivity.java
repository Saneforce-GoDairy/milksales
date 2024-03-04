package com.saneforce.godairy.procurement.ska;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saneforce.godairy.Activity_Hap.MainActivity;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityNewFarmerCreationBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;
import com.saneforce.godairy.procurement.ProcurementCameraX;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NewFarmerCreationActivity extends AppCompatActivity {
    private String DIR ;
    private ActivityNewFarmerCreationBinding binding;
    private final Context context = this;
    private String mName, mVillage, mType, mCompetitor, mRemarks;
    private Bitmap bitmap;

    private LinearLayout startTV, stopTV, playTV, stopplayTV;
    private TextView statusTV;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    public static final String APP_DATA = "/procurement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewFarmerCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        statusTV = findViewById(R.id.idTVstatus);
        startTV = findViewById(R.id.btnRecord);
        stopTV = findViewById(R.id.btnStop);
        playTV = findViewById(R.id.btnPlay);
        stopplayTV = findViewById(R.id.btnStopPlay);

        initLoad();
        onClick();

        startTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start recording method will start the recording of audio.
                startRecording();
            }
        });
        stopTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pause Recording method will pause the recording of audio.
                pauseRecording();

            }
        });
        playTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // play audio method will play the audio which we have recorded
                playAudio();
            }
        });
        stopplayTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pause play method will pause the play of audio
                pausePlaying();
            }
        });
    }

    private void onClick() {
           /*
           Camera access id

           1, AgronomistFormActivity
              Farmers meeting = 1
              CSR Activity    = 2
              Fodder Development Ac = 3

           2, AITFormActivity
              breed = 4

           3, CollectionCenterLocationActivity
              Collection center image = 5

           4, VeterinaryDoctorsFormActivity
              Type of image image = 6
              Emergency treatment/EVM Treatment (Breed) = 7

            5, QualityFormActivity
               Quality fat = 8
               Quality snf = 9
               No of vehicle received with hoods = 10
               No of vehicle received without hoods = 11
               Awareness program = 12

            6, FarmerCreationActivity
               Farmer image = 13

            7, MaintenanceIssueActivity
               Type of repair image = 14

            8, MaintenanceRegularActivity
               DG Set Running Hrs, After Last Services = 15

            9, New farmer creation ska
               Competitors = 16
         */

        binding.cameraCompetitors.setOnClickListener(v -> {
            binding.txtCompetitorsImageNotValid.setVisibility(GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Competitor");
            intent.putExtra("camera_id", "16");
            startActivity(intent);
        });

        binding.imageViewCompetitorsLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "SKA_NEW_CMTR_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Competitor");
            startActivity(intent);
        });


        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                saveNow();
            }
        });

        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mType = binding.spinnerType.getSelectedItem().toString();
                binding.txtTypeNotValid.setVisibility(GONE);
             //   binding.txtErrorFound.setVisibility(GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.remarkType.setOnClickListener(v -> {
        //    Toast.makeText(context, "type", Toast.LENGTH_SHORT).show();
            binding.remarkAudio.setChecked(false);
            binding.remarkType.setChecked(true);
            mRemarks = "type";
            binding.edRemark.setVisibility(View.VISIBLE);

            binding.remarkAudioLayout.setVisibility(View.GONE);
        });

        binding.remarkAudio.setOnClickListener(v -> {
       //     Toast.makeText(context, "Record audio", Toast.LENGTH_SHORT).show();
            binding.remarkType.setChecked(false);
            binding.remarkAudio.setChecked(true);
            mRemarks = "audio";
            binding.edRemark.setVisibility(View.GONE);

            binding.remarkAudioLayout.setVisibility(View.VISIBLE);
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void saveNow() {

    }

    private boolean validateInputs() {
        mName = binding.edFarmerName.getText().toString();
        mVillage = binding.edFarmerVillage.getText().toString();
        mCompetitor = binding.edCompetitorsVillage.getText().toString();

        if ("".equals(mName)){
            binding.edFarmerName.setError("Empty field");
            binding.edFarmerName.requestFocus();
       ////     binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mVillage)){
            binding.edFarmerVillage.setError("Empty field");
            binding.edFarmerVillage.requestFocus();
      //      binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mType)){
            ((TextView)binding.spinnerType.getSelectedView()).setError("Select type");
            binding.spinnerType.getSelectedView().requestFocus();
            binding.txtTypeNotValid.setVisibility(View.VISIBLE);
       //     binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCompetitor)){
            binding.edCompetitorsVillage.setError("Empty field");
            binding.edCompetitorsVillage.requestFocus();
       //     binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mRemarks)){
            Toast.makeText(context, "Please select ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initLoad() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.farmer_creation_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerType.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        File file = new File(getExternalFilesDir(null), "/procurement/" + "SKA_NEW_CMTR_123.jpg");
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        if (bitmap != null){
            binding.imageViewCompetitorsLayout.setVisibility(View.VISIBLE);
            binding.imageCompetitors.setImageBitmap(bitmap);
         //   binding.txtErrorFound.setVisibility(GONE);
        }
    }

    private void startRecording() {
        // check permission method is used to check that the user has granted permission to record nd store the audio.
        if (CheckPermissions()) {
            //setbackgroundcolor method will change the background color of text view.
            stopTV.setBackgroundColor(getResources().getColor(R.color.stop_reco));
//            startTV.setBackgroundColor(getResources().getColor(R.color.gray));
//            playTV.setBackgroundColor(getResources().getColor(R.color.gray));
//            stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));
            //we are here initializing our filename variable with the path of the recorded audio file.
//            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//            mFileName += "/AudioRecording.3gp";
            mFileName = getExternalFilesDir("/").getPath() + "/" + "procurement/";
            mFileName += "AudioRecording.3gp";
            //below method is used to initialize the media recorder clss
            mRecorder = new MediaRecorder();
            //below method is used to set the audio source which we are using a mic.
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //below method is used to set the output format of the audio.
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //below method is used to set the audio encoder for our recorded audio.
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //below method is used to set the output file location for our recorded audio
            mRecorder.setOutputFile(mFileName);
            try {
                //below mwthod will prepare our audio recorder class
                mRecorder.prepare();
                mRecorder.start();
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }
            // start method will start the audio recording.

            statusTV.setText("Recording Started");
        } else {
            //if audio recording permissions are not granted by user below method will ask for runtime permission for mic and storage.
            RequestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // this method is called when user will grant the permission for audio recording.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        //this method is used to check permission
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        // this method is used to request the permission for audio recording and storage.
        ActivityCompat.requestPermissions(NewFarmerCreationActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }


    public void playAudio() {
//        stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
//        startTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
//        playTV.setBackgroundColor(getResources().getColor(R.color.gray));
//        stopplayTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        //for playing our recorded audio we are using media player class.
        mPlayer = new MediaPlayer();
        try {
            //below method is used to set the data source which will be our file name
            mPlayer.setDataSource(mFileName);
            //below method will prepare our media player
            mPlayer.prepare();
            //below method will start our media player.
            mPlayer.start();
            statusTV.setText("Recording Started Playing");
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }


    }

    public void pauseRecording()  {
        stopTV.setBackgroundColor(getResources().getColor(R.color.white));
//        startTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
//        playTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
//        stopplayTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
        //below method will stop the audio recording.
        try{
            mRecorder.stop();
        }catch(RuntimeException ex){
            //Ignore
        }

        statusTV.setText("Recording Stopped");

    }

    public void pausePlaying() {
        //this method will release the media player class and pause the playing of our recorded audio.
        mPlayer.release();
        mPlayer = null;
//        stopTV.setBackgroundColor(getResources().getColor(R.color.gray));
//        startTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
//        playTV.setBackgroundColor(getResources().getColor(R.color.purple_200));
//        stopplayTV.setBackgroundColor(getResources().getColor(R.color.gray));
        statusTV.setText("Recording Play Stopped");

    }
}