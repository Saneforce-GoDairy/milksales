package com.saneforce.godairy.procurement.ska;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saneforce.godairy.R;
import com.saneforce.godairy.common.FileUploadService2;
import com.saneforce.godairy.databinding.ActivityExistingFarmerVisitBinding;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ExistingFarmerVisitActivity extends AppCompatActivity {
    private ActivityExistingFarmerVisitBinding binding;
    private final Context context = this;
    private String mCustomer , mCustomerDetails, mPurposeOfVisit, mPrice , mAsset, mCanes, mRemarksType = "" , mRemarksText = "";
    private static String mFileName = null;
    private static int eTime = 0;
    private static int oTime = 0;
    public static int sTime = 0;
    private final int SELECT_PICTURE = 200;
    private Bitmap bitmap;
    public final Handler handler = new Handler();
    private MediaRecorder mRecorder;
    private Uri selectedImageUri;
    public MediaPlayer mPlayer;

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
        binding = ActivityExistingFarmerVisitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClick();
        initLoad();
    }

    private void initLoad() {
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context,
                R.array.farmer_creation_types_array, R.layout.custom_spinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCustomer.setAdapter(adapter2);
    }

    private void onClick() {
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    saveNow();
                }
            }
        });

        binding.startRecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
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

        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imageChooser();
            }
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
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 200) {
            Uri data2 = data.getData();
            selectedImageUri = data2;
            if (data2 != null) {
                binding.imagePurposeVisitLayout.setVisibility(View.VISIBLE);
                binding.imagePurposeVisit.setImageURI(selectedImageUri);
            }
        }
    }

    private void saveNow() {
        Intent serviceIntent = new Intent(this, FileUploadService2.class);
        serviceIntent.putExtra("customer", mCustomer);
        serviceIntent.putExtra("customer_d", mCustomerDetails);
        serviceIntent.putExtra("purpose_of_visit", mPurposeOfVisit);
        serviceIntent.putExtra("price", mPrice);
        serviceIntent.putExtra("asset", mAsset);
        serviceIntent.putExtra("cans", mCanes);
        serviceIntent.putExtra("remarks_type", mRemarksType);
        serviceIntent.putExtra("remarks_text", mRemarksText);

        serviceIntent.putExtra("active_flag", "1");
        serviceIntent.putExtra("upload_service_id", "13");
        ContextCompat.startForegroundService(context, serviceIntent);
        finish();
        Toast.makeText(context, "form submit started", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInputs() {
        mCustomer = binding.spinnerCustomer.getSelectedItem().toString();
        mCustomerDetails = binding.edCusDetails.getText().toString();
        mPurposeOfVisit = binding.edPurposeOfVisit.getText().toString();
        mPrice = binding.price.getText().toString();
        mAsset = binding.asset.getText().toString();
        mCanes = binding.cans.getText().toString();
        mRemarksText = binding.edRemark.getText().toString();
        
        if ("Select".equals(mCustomer)) {
            ((TextView) binding.spinnerCustomer.getSelectedView()).setError("Select type");
            binding.spinnerCustomer.getSelectedView().requestFocus();
            Toast.makeText(context, "Select customer", Toast.LENGTH_SHORT).show();
            return false;
        }  
        if ("".equals(mCustomerDetails)) {
            binding.edCusDetails.setError("Empty field");
            binding.edCusDetails.requestFocus();
            return false;
        }
        if ("".equals(mPurposeOfVisit)) {
            binding.edPurposeOfVisit.setError("Empty field");
            binding.edPurposeOfVisit.requestFocus();
            return false;
        }
        if (selectedImageUri == null) {
            Toast.makeText(context, "pick purpose of visit picture", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(mPrice)) {
            binding.price.setError("Empty field");
            binding.price.requestFocus();
            return false;
        }
        if ("".equals(mAsset)) {
            binding.asset.setError("Empty field");
            binding.asset.requestFocus();
            return false;
        }
        if ("".equals(mCanes)) {
            binding.cans.setError("Empty field");
            binding.cans.requestFocus();
            return false;
        }
        if ("".equals(mRemarksType)) {
            Toast.makeText(context, "Please select remarks ", Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
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

    private void pauseRecording() {
        try {
            mRecorder.stop();
        } catch (RuntimeException ignored) {
        }
        binding.status.setText("Recording Stopped");
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

    private void pausePlaying() {
        mPlayer.release();
        mPlayer = null;
        binding.status.setText("Recording Play Stopped");
    }
}