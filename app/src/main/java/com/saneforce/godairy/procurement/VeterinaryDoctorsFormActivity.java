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
import com.saneforce.godairy.databinding.ActivityVeterinaryDoctorsFormBinding;

import java.io.File;

public class VeterinaryDoctorsFormActivity extends AppCompatActivity {
    private ActivityVeterinaryDoctorsFormBinding binding;
    private final Context context = this;
    private String mCompanyName, mPlant, mCenterName, mFarmerName, mTypeOfService, mTypeOfProduct, mSeedSales;
    private String mMinaralMixture, mFodderSales, mCattleOrder, mTeatTipCup, mEmergencyEvm, mTypesOfCases;
    private String mFamilyFarmDev, mNoOfFarmersEnrolled, mNoOfFarmersInducted;
    private Bitmap bitmapTypeOfSer, bitmapEVM;
    private File fileTypeOfSer, fileEVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVeterinaryDoctorsFormBinding.inflate(getLayoutInflater());
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
                R.array.veterinary_type_of_service_array, R.layout.custom_spinner);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfService.setAdapter(adapter3);

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.veterinary_type_of_product_array, R.layout.custom_spinner);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfProduct.setAdapter(adapter4);

        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.veterinary_evm_array, R.layout.custom_spinner);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerEmerEvm.setAdapter(adapter5);

        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this,
                R.array.veterinary_type_of_case_array, R.layout.custom_spinner);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTypeOfCases.setAdapter(adapter5);


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
         */


        binding.cameraTypeOfService.setOnClickListener(view -> {
            binding.txtImgTypeOfServiceNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Type of service");
            intent.putExtra("camera_id", "6");
            startActivity(intent);
        });

        binding.cameraEmerEvm.setOnClickListener(view -> {
            binding.txtImgTypeOfServiceNotValid.setVisibility(View.GONE);
            Intent intent = new Intent(context, ProcurementCameraX.class);
            intent.putExtra("event_name", "Emergency treatment/EVM");
            intent.putExtra("camera_id", "7");
            startActivity(intent);
        });


        binding.buttonSave.setOnClickListener(view -> {
            if (validateInputs()) {
                Toast.makeText(context, "valid", Toast.LENGTH_SHORT).show();
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

        binding.spinnerTypeOfService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerTypeOfService.getSelectedItem().toString();
                binding.txtTypeOfServiceNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerTypeOfProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerTypeOfProduct.getSelectedItem().toString();
                binding.txtTypeOfProNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerEmerEvm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerEmerEvm.getSelectedItem().toString();
                binding.txtEmerEvmNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        binding.spinnerTypeOfCases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlant = binding.spinnerTypeOfCases.getSelectedItem().toString();
                binding.txtTypeOfCasesNotValid.setVisibility(View.GONE);
                binding.txtErrorFound.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        binding.back.setOnClickListener(view -> finish());
    }

    private void saveNow() {

    }

    private boolean validateInputs() {
        mCompanyName = binding.spinnerCompany.getSelectedItem().toString();
        mPlant = binding.spinnerPlant.getSelectedItem().toString();
        mCenterName = binding.edCenterNameVisit.getText().toString().trim();
        mFarmerName = binding.edFarmerCode.getText().toString().trim();
        mTypeOfService = binding.spinnerTypeOfService.getSelectedItem().toString();
        mTypeOfProduct = binding.spinnerTypeOfProduct.getSelectedItem().toString();

        mSeedSales = binding.edSeedSale.getText().toString().trim();
        mMinaralMixture = binding.edMineralMixture.getText().toString().trim();

        mFodderSales = binding.edFodderSettsSale.getText().toString().trim();
        mCattleOrder = binding.edCattleFeedOrder.getText().toString().trim();

        mTeatTipCup = binding.edTeatDipCup.getText().toString().trim();
        mEmergencyEvm = binding.spinnerEmerEvm.getSelectedItem().toString();

        mTypesOfCases = binding.spinnerTypeOfCases.getSelectedItem().toString();
        mFamilyFarmDev = binding.edFamilyFarmDev.getText().toString().trim();

        mNoOfFarmersEnrolled = binding.edNoOfFarmerEnrolled.getText().toString().trim();
        mNoOfFarmersInducted = binding.edNoOfFarmerInducted.getText().toString().trim();


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
            binding.edCenterNameVisit.setError("Enter center name");
            binding.edCenterNameVisit.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mFarmerName)){
            binding.edFarmerCode.setError("Enter farmer name");
            binding.edFarmerCode.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mTypeOfService)){
            ((TextView)binding.spinnerTypeOfService.getSelectedView()).setError("Select type of service");
            binding.spinnerTypeOfService.getSelectedView().requestFocus();
            binding.txtTypeOfServiceNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapTypeOfSer == null){
            binding.txtImgTypeOfServiceNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mTypeOfProduct)){
            ((TextView)binding.spinnerTypeOfProduct.getSelectedView()).setError("Select type of product");
            binding.spinnerTypeOfProduct.getSelectedView().requestFocus();
            binding.txtTypeOfProNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mSeedSales)){
            binding.edSeedSale.setError("Enter seed sale");
            binding.edSeedSale.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mMinaralMixture)){
            binding.edMineralMixture.setError("Enter mineral mixture");
            binding.edMineralMixture.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mFodderSales)){
            binding.edFodderSettsSale.setError("Enter fodder setts sales");
            binding.edFodderSettsSale.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mCattleOrder)){
            binding.edCattleFeedOrder.setError("Enter cattle feed order");
            binding.edCattleFeedOrder.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mTeatTipCup)){
            binding.edTeatDipCup.setError("Enter teat dip cup & solution");
            binding.edTeatDipCup.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mEmergencyEvm)){
            ((TextView)binding.spinnerEmerEvm.getSelectedView()).setError("Select emergency treatment/EVM");
            binding.spinnerEmerEvm.getSelectedView().requestFocus();
            binding.txtEmerEvmNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if (bitmapEVM == null){
            binding.txtImgEmerEvmNotValid.setVisibility(View.VISIBLE);
            return false;
        }
        if ("Select".equals(mTypesOfCases)){
            ((TextView)binding.spinnerTypeOfCases.getSelectedView()).setError("Select type of cases");
            binding.spinnerTypeOfCases.getSelectedView().requestFocus();
            binding.txtTypeOfCasesNotValid.setVisibility(View.VISIBLE);
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mFamilyFarmDev)){
            binding.edFamilyFarmDev.setError("Enter family farm development");
            binding.edFamilyFarmDev.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfFarmersEnrolled)){
            binding.edNoOfFarmerEnrolled.setError("Enter no of farmers enrolled");
            binding.edNoOfFarmerEnrolled.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        if ("".equals(mNoOfFarmersInducted)){
            binding.edNoOfFarmerInducted.setError("Enter no of farmers inducted");
            binding.edNoOfFarmerInducted.requestFocus();
            binding.txtErrorFound.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        fileTypeOfSer = new File(getExternalFilesDir(null), "/procurement/" + "VET_TOS_123.jpg");
        bitmapTypeOfSer = BitmapFactory.decodeFile(fileTypeOfSer.getAbsolutePath());

        fileEVM = new File(getExternalFilesDir(null), "/procurement/" + "VET_EVM_123.jpg");
        bitmapEVM = BitmapFactory.decodeFile(fileEVM.getAbsolutePath());

        if (bitmapTypeOfSer != null){
            binding.imageViewTypeOfServiceLayout.setVisibility(View.VISIBLE);
            binding.imageTypeOfService.setImageBitmap(bitmapTypeOfSer);
            binding.txtImgTypeOfServiceNotValid.setVisibility(View.GONE);
        }

        if (bitmapEVM != null){
            binding.imageViewEmerEvmLayout.setVisibility(View.VISIBLE);
            binding.imageEmerEvm.setImageBitmap(bitmapEVM);
            binding.txtImgEmerEvmNotValid.setVisibility(View.GONE);
        }
    }
}