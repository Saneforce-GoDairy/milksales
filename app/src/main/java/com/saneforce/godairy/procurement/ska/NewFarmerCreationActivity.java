package com.saneforce.godairy.procurement.ska;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityNewFarmerCreationBinding;
import com.saneforce.godairy.procurement.ImageViewActivity;
import com.saneforce.godairy.procurement.ProcurementCameraX;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NewFarmerCreationActivity extends AppCompatActivity {
    private ActivityNewFarmerCreationBinding binding;
    private final Context context = this;
    private String mName, mVillage, mCompetitor, mRemarksText = "";
    public String mType, mRemarksType = "";
    private Bitmap bitmap;
    private static String mFileName = null;
    private MediaRecorder mRecorder;
    public MediaPlayer mPlayer;
    private static int eTime = 0;
    public static int sTime = 0;
    private static int oTime = 0;
    public final Handler handler = new Handler();

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
           if (mPlayer != null){
               sTime = mPlayer.getCurrentPosition();

               handler.postDelayed(this, 100);
           }

            binding.txtStartTime.setText(String.format("%d min, %d sec", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) sTime)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)sTime)))}));
            binding.seekbar.setProgress(sTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewFarmerCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLoad();
        onClick();
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
         binding.buttonSave.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (validateInputs()) {
                     saveNow();
                 }
             }
         });

         binding.cameraCompetitors.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 binding.txtCompetitorsImageNotValid.setVisibility(View.GONE);
                 Intent intent = new Intent(context, ProcurementCameraX.class);
                 intent.putExtra("event_name", "Competitor");
                 intent.putExtra("camera_id", "16");
                 startActivity(intent);
             }
         });

         binding.imageViewCompetitorsLayout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(context, ImageViewActivity.class);
                 intent.putExtra("uri", getExternalFilesDir("/").getPath() + "/procurement/SKA_NEW_CMTR_123.jpg");
                 intent.putExtra("event_name", "Competitor");
                 startActivity(intent);
             }
         });

        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mType = binding.spinnerType.getSelectedItem().toString();
                binding.txtTypeNotValid.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.remarkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.remarkAudio.setChecked(false);
                binding.remarkType.setChecked(true);
                mRemarksType = "type";
                binding.edRemark.setVisibility(View.VISIBLE);
                binding.remarkAudioLayout.setVisibility(View.GONE);
            }
        });

        binding.remarkAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.remarkType.setChecked(false);
                binding.remarkAudio.setChecked(true);
                mRemarksType = "audio";
                binding.edRemark.setVisibility(View.GONE);
                binding.remarkAudioLayout.setVisibility(View.VISIBLE);
            }
        });

        binding.startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.chronometer.start();
                startRecording();
                binding.startRecord.setVisibility(View.GONE);
                binding.stop.setVisibility(View.VISIBLE);
            }
        });

        binding.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.stop.setVisibility(View.GONE);
                binding.play.setVisibility(View.VISIBLE);
                pauseRecording();
                binding.chronometer.stop();
                binding.chronometer.setVisibility(View.GONE);
                binding.seekbarContainer.setVisibility(View.VISIBLE);
                binding.seekbar.setVisibility(View.VISIBLE);
                binding.deleteVoiceNote.setVisibility(View.VISIBLE);
            }
        });

        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.play.setVisibility(View.GONE);
                binding.stopPlay.setVisibility(View.VISIBLE);
                playAudio();
            }
        });

        binding.stopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.stopPlay.setVisibility(View.GONE);
                binding.play.setVisibility(View.VISIBLE);
                pausePlaying();
            }
        });

        binding.deleteVoiceNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new File(getExternalFilesDir("/").getPath() + "/procurement/", "new_far_creation.mp3").delete()) {
                    Toast.makeText(context, "audio deleted", Toast.LENGTH_SHORT).show();
                   binding.play.setVisibility(View.GONE);
                   binding.deleteVoiceNote.setVisibility(View.GONE);
                   binding.startRecord.setVisibility(View.VISIBLE);
                   binding.stopPlay.setVisibility(View.GONE);
                    binding.seekbar.setVisibility(View.GONE);
                    binding.chronometer.setVisibility(View.VISIBLE);
                    binding.chronometer.setBase(SystemClock.elapsedRealtime());
                    binding.chronometer.stop();
                    binding.txtStartTime.setVisibility(View.GONE);
                    binding.txtSongTime.setVisibility(View.GONE);
                    return;
                }
                Toast.makeText(context, "unable to deleted", Toast.LENGTH_SHORT).show();
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void pausePlaying() {
        mPlayer.release();
        mPlayer = null;
        binding.status.setText("Recording Play Stopped");
    }

    private void playAudio() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mPlayer = mediaPlayer;
        try {
            mediaPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            binding.status.setText("Recording Started Playing");
            eTime = mPlayer.getDuration();
            sTime = mPlayer.getCurrentPosition();
            if (oTime == 0) {
                binding.seekbar.setMax(eTime);
                oTime = 1;
            }
            binding.txtSongTime.setText(String.format("%d min, %d sec", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) eTime)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) eTime)))}));
            binding.txtStartTime.setText(String.format("%d min, %d sec", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) sTime)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) sTime)))}));
            binding.seekbar.setProgress(sTime);
            handler.postDelayed(UpdateSongTime, 100);
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    private void pauseRecording() {
        try {
            mRecorder.stop();
        } catch (RuntimeException ignored) {
        }
        binding.status.setText("Recording Stopped");
    }

    private void startRecording() {
        if (CheckPermissions()) {
            mFileName = getExternalFilesDir("/").getPath() + "/procurement/";
            mFileName += "new_far_creation.mp3";
            MediaRecorder mediaRecorder = new MediaRecorder();
            mRecorder = mediaRecorder;
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(mFileName);
            try {
                mRecorder.prepare();
                mRecorder.start();
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }
            binding.status.setText("Recording Started");
            return;
        }
        RequestPermissions();
    }

    private boolean CheckPermissions() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.RECORD_AUDIO") == 0;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
    }

    private void saveNow() {
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("name", mName);
        serviceIntent.putExtra("village", mVillage);
        serviceIntent.putExtra("type", mType);
        serviceIntent.putExtra("competitor", mCompetitor);
        serviceIntent.putExtra("remarks_type", mRemarksType);
        serviceIntent.putExtra("remarks_text", mRemarksText);
        serviceIntent.putExtra("active_flag", "1");
        serviceIntent.putExtra("upload_service_id", "12");
        ContextCompat.startForegroundService(context, serviceIntent);
        finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs() {
        mName = binding.edFarmerName.getText().toString();
        mType = binding.spinnerType.getSelectedItem().toString();
        mVillage = binding.edFarmerVillage.getText().toString();
        mCompetitor = binding.edCompetitorsVillage.getText().toString();
        mRemarksText = binding.edRemark.getText().toString();

        if ("".equals(mName)) {
            binding.edFarmerName.setError("Empty field");
            binding.edFarmerName.requestFocus();
            return false;
        }
        if ("".equals(mVillage)) {
            binding.edFarmerVillage.setError("Empty field");
            binding.edFarmerVillage.requestFocus();
            return false;
        }
        if ("Select".equals(mType)) {
            ((TextView) binding.spinnerType.getSelectedView()).setError("Select type");
            binding.spinnerType.getSelectedView().requestFocus();
            binding.txtTypeNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCompetitor)) {
            binding.edCompetitorsVillage.setError("Empty field");
            binding.edCompetitorsVillage.requestFocus();
            return false;
        }
        if (bitmap == null) {
            binding.txtCompetitorsImageNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mRemarksType)) {
            Toast.makeText(context, "Please select remarks ", Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
    }

    private void initLoad() {
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context,
                R.array.farmer_creation_types_array, R.layout.custom_spinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerType.setAdapter(adapter2);
    }

    public void onResume() {
        super.onResume();
        Bitmap decodeFile = BitmapFactory.decodeFile(new File(getExternalFilesDir((String) null), "/procurement/SKA_NEW_CMTR_123.jpg").getAbsolutePath());
        bitmap = decodeFile;
        if (decodeFile != null) {
            binding.imageViewCompetitorsLayout.setVisibility(View.VISIBLE);
            binding.imageCompetitors.setImageBitmap(bitmap);
        }
    }
}