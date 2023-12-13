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
import com.saneforce.godairy.databinding.ActivityColletionCenterLocationBinding;

import java.io.File;

public class CollectionCenterLocationActivity extends AppCompatActivity {
    private ActivityColletionCenterLocationBinding binding;
    private String mCompanyName, mPlant, mSapCenterCode, mSapCenterName, mCenterAddress, mPotentialLpd;
    private String mNoOfFarmersEnrolled, mCompetitorLpdSinner1, mCompetitorLpdSinner2, mCompetitorLpdSinner3, mCompetitorLpdSinner4, mCompetitorLpdSinner5;
    private String mCompetitorLpdEdText1, mCompetitorLpdEdText2, mCompetitorLpdEdText3, mCompetitorLpdEdText4, mCompetitorLpdEdText5;
    private final Context context = this;
    private Bitmap bitmapCollectCenter;
    private File fileCollectCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityColletionCenterLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSpinnerArray();
        onClick();
    }

    private void initSpinnerArray() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.company_array, R.layout.custom_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompany.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.plant_array, R.layout.custom_spinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPlant.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.competitor_array, R.layout.custom_spinner);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompetitorLpd1.setAdapter(adapter3);

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.competitor_array, R.layout.custom_spinner);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompetitorLpd2.setAdapter(adapter4);

        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.competitor_array, R.layout.custom_spinner);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompetitorLpd3.setAdapter(adapter5);

        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this,
                R.array.competitor_array, R.layout.custom_spinner);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompetitorLpd4.setAdapter(adapter6);

        ArrayAdapter<CharSequence> adapter7 = ArrayAdapter.createFromResource(this,
                R.array.competitor_array, R.layout.custom_spinner);
        adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCompetitorLpd5.setAdapter(adapter7);
    }

    private void onClick() {

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

        binding.spinnerCompetitorLpd1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerCompetitorLpd1.getSelectedItem().toString();
                binding.txtCompetitorLpd1NotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        binding.spinnerCompetitorLpd2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerCompetitorLpd2.getSelectedItem().toString();
                binding.txtCompetitorLpd2NotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerCompetitorLpd3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerCompetitorLpd3.getSelectedItem().toString();
                binding.txtCompetitorLpd3NotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerCompetitorLpd4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerCompetitorLpd4.getSelectedItem().toString();
                binding.txtCompetitorLpd4NotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerCompetitorLpd5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerCompetitorLpd5.getSelectedItem().toString();
                binding.txtCompetitorLpd5NotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.back.setOnClickListener(view -> finish());

          /*
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
         */


        binding.cameraCollectCenter.setOnClickListener(view -> {
            binding.txtCollectCenterNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Collection center");
            intent.putExtra("camera_id", "5");
            startActivity(intent);
        });

        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                Toast.makeText(context, "valid", Toast.LENGTH_SHORT).show();
                saveNow();
            }
        });

        binding.imageViewCollectCenterLayout.setOnClickListener(view -> {
            String imagePath = getExternalFilesDir("/").getPath() + "/" + "procurement/" + "CC_123.jpg";

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("uri", imagePath);
            intent.putExtra("event_name", "Collection Center");
            startActivity(intent);
        });
    }

    private void saveNow() {

    }

    private boolean validateInputs() {
        mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
        mPlant = binding.spinnerPlant.getSelectedItem().toString();
        mSapCenterCode = binding.edSapCenterCode.getText().toString().trim();
        mSapCenterName = binding.edSapCenterName.getText().toString().trim();
        mCenterAddress = binding.edSapAddress.getText().toString().trim();
        mPotentialLpd = binding.edPotentialLpd.getText().toString().trim();
        mNoOfFarmersEnrolled = binding.edEnrolled.getText().toString().trim();

        mCompetitorLpdSinner1 = binding.spinnerCompetitorLpd1.getSelectedItem().toString();
        mCompetitorLpdEdText1 = binding.edCompetitorLpd1.getText().toString().trim();

        mCompetitorLpdSinner2 = binding.spinnerCompetitorLpd2.getSelectedItem().toString();
        mCompetitorLpdEdText2 = binding.edCompetitorLpd2.getText().toString().trim();

        mCompetitorLpdSinner3 = binding.spinnerCompetitorLpd3.getSelectedItem().toString();
        mCompetitorLpdEdText3 = binding.edCompetitorLpd3.getText().toString().trim();

        mCompetitorLpdSinner4 = binding.spinnerCompetitorLpd4.getSelectedItem().toString();
        mCompetitorLpdEdText4 = binding.edCompetitorLpd4.getText().toString().trim();

        mCompetitorLpdSinner5 = binding.spinnerCompetitorLpd5.getSelectedItem().toString();
        mCompetitorLpdEdText5 = binding.edCompetitorLpd5.getText().toString().trim();

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
        if (bitmapCollectCenter == null){
            binding.txtCollectCenterNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mSapCenterCode)){
            binding.edSapCenterCode.setError("Enter Sap Center Code");
            binding.edSapCenterCode.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mSapCenterName)){
            binding.edSapCenterName.setError("Enter Sap Center Name");
            binding.edSapCenterName.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCenterAddress)){
            binding.edSapAddress.setError("Enter Center Address");
            binding.edSapAddress.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mPotentialLpd)){
            binding.edPotentialLpd.setError("Enter Potential LPD");
            binding.edPotentialLpd.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfFarmersEnrolled)){
            binding.edEnrolled.setError("Enter Farmers Enrolled");
            binding.edEnrolled.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mCompetitorLpdSinner1)){
            ((TextView)binding.spinnerCompetitorLpd1.getSelectedView()).setError("Select Competitor LPD1");
            binding.spinnerCompetitorLpd1.getSelectedView().requestFocus();
            binding.txtCompetitorLpd1NotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCompetitorLpdEdText1)){
            binding.edCompetitorLpd1.setError("Enter Competitor LPD1");
            binding.edCompetitorLpd1.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mCompetitorLpdSinner2)){
            ((TextView)binding.spinnerCompetitorLpd2.getSelectedView()).setError("Select Competitor LPD1");
            binding.spinnerCompetitorLpd2.getSelectedView().requestFocus();
            binding.txtCompetitorLpd2NotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCompetitorLpdEdText2)){
            binding.edCompetitorLpd2.setError("Enter Competitor LPD1");
            binding.edCompetitorLpd2.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mCompetitorLpdSinner3)){
            ((TextView)binding.spinnerCompetitorLpd3.getSelectedView()).setError("Select Competitor LPD1");
            binding.spinnerCompetitorLpd3.getSelectedView().requestFocus();
            binding.txtCompetitorLpd3NotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCompetitorLpdEdText3)){
            binding.edCompetitorLpd3.setError("Enter Competitor LPD1");
            binding.edCompetitorLpd3.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mCompetitorLpdSinner4)){
            ((TextView)binding.spinnerCompetitorLpd4.getSelectedView()).setError("Select Competitor LPD1");
            binding.spinnerCompetitorLpd4.getSelectedView().requestFocus();
            binding.txtCompetitorLpd4NotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCompetitorLpdEdText4)){
            binding.edCompetitorLpd4.setError("Enter Competitor LPD1");
            binding.edCompetitorLpd4.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mCompetitorLpdSinner5)){
            ((TextView)binding.spinnerCompetitorLpd5.getSelectedView()).setError("Select Competitor LPD1");
            binding.spinnerCompetitorLpd5.getSelectedView().requestFocus();
            binding.txtCompetitorLpd5NotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCompetitorLpdEdText5)){
            binding.edCompetitorLpd5.setError("Enter Competitor LPD1");
            binding.edCompetitorLpd5.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Farmers meeting
        fileCollectCenter = new File(getExternalFilesDir(null), "/procurement/" + "CC_123.jpg");
        bitmapCollectCenter = BitmapFactory.decodeFile(fileCollectCenter.getAbsolutePath());

        if (bitmapCollectCenter != null){
            binding.imageViewCollectCenterLayout.setVisibility(View.VISIBLE);
            binding.imageCollectCenter.setImageBitmap(bitmapCollectCenter);
            binding.txtCollectCenterNotValid.setVisibility(View.GONE);
            binding.txtErrorFound.setVisibility(View.GONE);
        }
    }
}