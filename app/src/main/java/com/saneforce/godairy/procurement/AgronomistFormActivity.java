package com.saneforce.godairy.procurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.saneforce.godairy.R;
import com.saneforce.godairy.databinding.ActivityAgronomistFormBinding;

import java.io.File;

public class AgronomistFormActivity extends AppCompatActivity {
    private ActivityAgronomistFormBinding binding;
    private String mCompanyName, mPlant, mCenterName, mFarmerCodeName, mTypeOfProduct, mTeatTipCup, mTypeOfService, mFodderDev;
    private String mNoOfFarmersEnrolled, mNoOfFarmersInducted;
    private final Context context = this;
    private File fileFormersMeeting, fileCSRActivity, fileFodderDevAcres;
    private Bitmap bitmapFormersMeeting, bitmapCSRActivity , bitmapFodderDevAcres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgronomistFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSpinnerArray();
        onClick();
    }

    private void onClick() {
        binding.back.setOnClickListener(view -> {
            finish();
        });
    }

    private void initSpinnerArray() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.company_array, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompany.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.plant_array, R.layout.custom_spinner);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPlant.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.type_of_product_array, R.layout.custom_spinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfProduct.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.typs_of_service_array, R.layout.custom_spinner);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfService.setAdapter(adapter3);

        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                Toast.makeText(AgronomistFormActivity.this, "valid save", Toast.LENGTH_SHORT).show();
                saveNow();
            }
        });

        binding.spinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
                binding.txtCompanyNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        binding.spinnerPlant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerPlant.getSelectedItem().toString();
                binding.txtPlantNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        binding.spinnerTypeOfProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTypeOfProduct = binding.spinnerTypeOfProduct.getSelectedItem().toString();
                binding.txtTypeOfProNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerTypeOfService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTypeOfProduct = binding.spinnerTypeOfService.getSelectedItem().toString();
                binding.txtTypeOfServiceNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        /*
           Camera access id

           Farmers meeting = 1
           CSR Activity    = 2
           Fodder Development Ac = 3
         */

        binding.cameraFarmersMeeting.setOnClickListener(view -> {
            binding.txtFarmersImageNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Farmers meeting");
            intent.putExtra("camera_id", "1");
            startActivity(intent);
        });

        binding.cameraCsrActivity.setOnClickListener(view -> {
            binding.txtCsrImageNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "CSR Activity");
            intent.putExtra("camera_id", "2");
            startActivity(intent);
        });

        binding.cameraFoder.setOnClickListener(view -> {
            binding.txtFodderImageNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Fodder Development Acres");
            intent.putExtra("camera_id", "3");
            startActivity(intent);
        });

        binding.imageViewFormersMeetingLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "FAR_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Formers Meeting");
            startActivity(intent);
        });

        binding.imageViewCsrActivityLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "CSR_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "CSR Activity");
            startActivity(intent);
        });

        binding.imageViewFoderLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "FDA_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Fodder Development Acres");
            startActivity(intent);
        });
    }

    private boolean validateInputs() {
        mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
        mPlant = binding.spinnerPlant.getSelectedItem().toString();
        mCenterName = binding.edCenterName.getText().toString().trim();
        mFarmerCodeName = binding.edFarmerCodeName.getText().toString().trim();
        mTypeOfProduct = binding.spinnerTypeOfProduct.getSelectedItem().toString();
        mTeatTipCup = binding.edTeatTipCup.getText().toString().trim();
        mTypeOfService = binding.spinnerTypeOfService.getSelectedItem().toString();
        mFodderDev = binding.edFodderDevelopmentAcres.getText().toString();
        mNoOfFarmersEnrolled = binding.edNoOfFarmersEnrolled.getText().toString().trim();
        mNoOfFarmersInducted = binding.edNoOfFarmersInducted.getText().toString().trim();

        if ("Select".equals(mCompanyName)){
            ((TextView)binding.spinnerCompany.getSelectedView()).setError("Select company");
            binding.spinnerCompany.getSelectedView().requestFocus();
            binding.txtCompanyNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mPlant)){
            ((TextView)binding.spinnerPlant.getSelectedView()).setError("Select plant");
            binding.spinnerPlant.getSelectedView().requestFocus();
            binding.txtPlantNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCenterName)){
            binding.edCenterName.setError("Empty field");
            binding.edCenterName.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mFarmerCodeName)){
            binding.edFarmerCodeName.setError("Empty field");
            binding.edFarmerCodeName.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mTypeOfProduct)){
            ((TextView)binding.spinnerTypeOfProduct.getSelectedView()).setError("Select");
            binding.spinnerTypeOfProduct.getSelectedView().requestFocus();
            binding.txtTypeOfProNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mTeatTipCup)){
            binding.edTeatTipCup.setError("Empty field");
            binding.edTeatTipCup.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mTypeOfService)){
            ((TextView)binding.spinnerTypeOfService.getSelectedView()).setError("Select");
            binding.spinnerTypeOfService.getSelectedView().requestFocus();
            binding.txtTypeOfServiceNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapFormersMeeting == null){
            binding.txtFarmersImageNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapCSRActivity == null){
            binding.txtCsrImageNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mFodderDev)){
            binding.edFodderDevelopmentAcres.setError("Empty field");
            binding.edFodderDevelopmentAcres.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapFodderDevAcres == null){
            binding.txtFodderImageNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfFarmersEnrolled)){
            binding.edNoOfFarmersEnrolled.setError("Empty field");
            binding.edNoOfFarmersEnrolled.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfFarmersInducted)){
            binding.edNoOfFarmersInducted.setError("Empty field");
            binding.edNoOfFarmersInducted.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private void saveNow() {
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Farmers meeting
        fileFormersMeeting = new File(getExternalFilesDir(null), "/procurement/" +"FAR_123.jpg");
        bitmapFormersMeeting = BitmapFactory.decodeFile(fileFormersMeeting.getAbsolutePath());

        // CSR Activity
        fileCSRActivity = new File(getExternalFilesDir(null), "/procurement/" +"CSR_123.jpg");
        bitmapCSRActivity = BitmapFactory.decodeFile(fileCSRActivity.getAbsolutePath());

        // Fodder dev acres
        fileFodderDevAcres = new File(getExternalFilesDir(null), "/procurement/" +"FDA_123.jpg");
        bitmapFodderDevAcres = BitmapFactory.decodeFile(fileFodderDevAcres.getAbsolutePath());

        if (bitmapFormersMeeting != null){
            binding.imageViewFormersMeetingLayout.setVisibility(View.VISIBLE);
            binding.imageFarmersMeeting.setImageBitmap(bitmapFormersMeeting);
        }

        if (bitmapCSRActivity != null){
            binding.imageViewCsrActivityLayout.setVisibility(View.VISIBLE);
            binding.imageCsrActivity.setImageBitmap(bitmapCSRActivity);
        }

        if (bitmapFodderDevAcres != null){
            binding.imageViewFoderLayout.setVisibility(View.VISIBLE);
            binding.imageFoder.setImageBitmap(bitmapFodderDevAcres);
        }
    }
}