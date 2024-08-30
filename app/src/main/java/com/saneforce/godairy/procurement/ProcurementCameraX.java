package com.saneforce.godairy.procurement;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
//noinspection ExifInterface
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.common.util.concurrent.ListenableFuture;
import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityProcurementCameraXBinding;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class ProcurementCameraX extends AppCompatActivity {
    private ActivityProcurementCameraXBinding binding;
    private final Context context = this;
    private Camera camera;
    private String imageName, DIR;
    private File file;
    private Bitmap bitmap;

    int cameraFacing = CameraSelector.LENS_FACING_FRONT;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result){
                // startCamera(cameraFacing);
                startCamera(1);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProcurementCameraXBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cameraPermission();
        DIR = getExternalFilesDir("/").getPath() + "/" + "procurement/";
        createDirectory();
        onClick();

        String eventName = getIntent().getStringExtra("event_name");

        if (eventName != null){
            binding.eventName.setText(eventName);
        }
    }

    private void onClick() {
        binding.back.setOnClickListener(v -> finish());
        binding.buttonSwitchCam.setOnClickListener(v -> {
            if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
            } else {
                cameraFacing = CameraSelector.LENS_FACING_BACK;
            }
            startCamera(cameraFacing);
        });

        binding.cameraxRightControls.setOnClickListener(v -> {
            startCamera(cameraFacing);
            binding.imageView.setVisibility(View.GONE);
            binding.cameraxRightControls.setVisibility(View.GONE);

            binding.cameraxLeftControls.setVisibility(View.VISIBLE);
            binding.cameraPreview.setVisibility(View.VISIBLE);
            binding.cameraxClickLayout.setVisibility(View.VISIBLE);
            binding.captureButton.setVisibility(View.VISIBLE);
        });
    }

    private void  createDirectory() {
        File dir = getExternalFilesDir("/procurement");
        if(!dir.exists()) {
            if (!dir.mkdir()) {
                Toast.makeText(getApplicationContext(), "The folder " + dir.getPath() + "was not created", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else {
            startCamera(cameraFacing);
        }
    }

    public void startCamera(int cameraFacing) {
        int aspectRatio = aspectRatio(binding.cameraPreview.getWidth(), binding.cameraPreview.getHeight());
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);

        listenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider) listenableFuture.get();

                Preview preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).build();

                androidx.camera.core.ImageCapture imageCapture = new androidx.camera.core.ImageCapture.Builder()
                        .setCaptureMode(androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(getWindowManager()
                                .getDefaultDisplay().getRotation()).build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                cameraProvider.unbindAll();

                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

                binding.captureButton.setOnClickListener(view -> {
                    takePicture(imageCapture);
                });

                binding.buttonFlash.setOnClickListener(view -> setFlashIcon(camera));

                preview.setSurfaceProvider(binding.cameraPreview.getSurfaceProvider());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    public void takePicture(androidx.camera.core.ImageCapture imageCapture) {

        String cameraEventId = getIntent().getStringExtra("camera_id");

        switch (Integer.parseInt(cameraEventId)){

            case 25:
                imageName = "CENP_123" + ".jpg";
                file = new File(DIR, imageName);
                break;


            case 24:
                imageName = "FAMC2_123" + ".jpg";
                file = new File(DIR, imageName);
                break;

            case 23:
                agentCreat();
                break;

            case 22:
                maintenanceAsPerBook();
                break;

            case 21:
                maintenanceRegNoHrs();
                break;

            case 20:
                maintenanceWeighSca();
                break;

            case 19:
                maintenanceMotorIs();
                break;

            case 18:
                maintenanceSMBS();
                break;

            case 17:
                maintenanceBoardIs();
                break;

            case 16:
                newFarCreatCompetitor(); // ska client
                break;

            case 15:
                maintenanceRegular();
                break;

            case 14:
                maintenanceRepair();
                break;

            case 13:
                farmerCreation();
                break;

            case 12:
                qualityAws();
                break;

            case 11:
                qualityReceivedNofVehicleWithout();
                break;

            case 10:
                qualityReceivedNofVehicle();
                break;

            case 9:
                qualityFormSNF();
                break;

            case 8:
                qualityFormFat();
                break;

            case 7:
                VeteriDocFormEVM();
                break;

            case 6:
                VeteriDocFormTypeOfSer();
                break;

            case 5:
                collectionCenter();
                break;

            case 4:
                nameOfBreed();
                break;

            case 3:
                fodderDevAcres();
                break;

            case 2:
                csrActivityImage();
                break;

            case 1:
                farmersMeetingImage();
                break;

        }

        androidx.camera.core.ImageCapture.OutputFileOptions outputFileOptions = new androidx.camera.core.ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new androidx.camera.core.ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {
                    file = new File(DIR, imageName);
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    // automatic screen orientation require Android 13 above API 33
                    // using Exif method
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(file.getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                    Bitmap bmRotated = rotateBitmap(bitmap, orientation);

                    binding.imageView.setImageBitmap(bmRotated);
                    binding.cameraxLeftControls.setVisibility(View.GONE);
                    binding.cameraxClickLayout.setVisibility(View.GONE);

                    binding.imageView.setVisibility(View.VISIBLE);;
                    binding.cameraxRightControls.setVisibility(View.VISIBLE);
                    binding.cameraPreview.setVisibility(View.INVISIBLE);
                });
                startCamera(cameraFacing);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(() -> Log.e("image_capture", "Captured image save error :" + exception.getMessage()));
                startCamera(cameraFacing);
            }
        });
    }

    private void agentCreat() {
        imageName = "AGENT_CREAT_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void maintenanceAsPerBook() {
        imageName = "MAIN_RE_AS_PER_BOOK_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void maintenanceRegNoHrs() {
        imageName = "MAIN_RE_NOHRS_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void maintenanceWeighSca() {
        imageName = "MAIN_WEIGH_SC_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void maintenanceMotorIs() {
        imageName = "MAIN_MOTOR_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void maintenanceSMBS() {
        imageName = "MAIN_SMBS_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void maintenanceBoardIs() {
        imageName = "MAIN_IS_BOARD_IS" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void newFarCreatCompetitor() {
        imageName = "SKA_NEW_CMTR_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void maintenanceRegular() {
        imageName = "MAIN_REGU_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void maintenanceRepair() {
        imageName = "MAIN_RE_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void farmerCreation() {
        imageName = "FAMC_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void qualityAws() {
        imageName = "QUA_AWS_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void qualityReceivedNofVehicleWithout() {
        imageName = "QUA_RNV_WITHOUT_HOODS_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void qualityReceivedNofVehicle() {
        imageName = "QUA_RNV_WITH_HOODS_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void qualityFormSNF() {
        imageName = "QUA_SNF_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void qualityFormFat() {
        imageName = "QUA_FAT_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void VeteriDocFormEVM() {
        imageName = "VET_EVM_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void VeteriDocFormTypeOfSer() {
        imageName = "VET_TOS_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void collectionCenter() {
        imageName = "CC_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void nameOfBreed() {
        imageName = "NOB_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void fodderDevAcres() {
        imageName = "FDA_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void csrActivityImage() {
        imageName = "CSR_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    private void farmersMeetingImage() {
        imageName = "FAR_123" + ".jpg";
        file = new File(DIR, imageName);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    private void setFlashIcon(Camera camera) {
        if (camera.getCameraInfo().hasFlashUnit()) {
            if (camera.getCameraInfo().getTorchState().getValue() == 0) {
                camera.getCameraControl().enableTorch(true);
                binding.buttonFlash.setImageResource(R.drawable.camera_flash);
            } else {
                camera.getCameraControl().enableTorch(false);
                binding.buttonFlash.setImageResource(R.drawable.camera_flash);
            }
        } else {
            runOnUiThread(() -> Toast.makeText(context, "Flash is not available currently", Toast.LENGTH_SHORT).show());
        }
    }
}