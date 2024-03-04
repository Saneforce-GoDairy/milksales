package com.saneforce.godairy.procurement.ska;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityExistingFarmerVisitBinding;

import java.io.IOException;

public class ExistingFarmerVisitActivity extends AppCompatActivity {
    private ActivityExistingFarmerVisitBinding binding;
    private LinearLayout startTV, stopTV, playTV, stopplayTV;
    private TextView statusTV;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    public static final String APP_DATA = "/procurement";
    String mRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExistingFarmerVisitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        statusTV = findViewById(R.id.idTVstatus);
        startTV = findViewById(R.id.btnRecord);
        stopTV = findViewById(R.id.btnStop);
        playTV = findViewById(R.id.btnPlay);
        stopplayTV = findViewById(R.id.btnStopPlay);

        onClick();
    }

    private void onClick() {
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
        ActivityCompat.requestPermissions(ExistingFarmerVisitActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
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