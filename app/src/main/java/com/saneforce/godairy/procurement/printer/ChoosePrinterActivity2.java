package com.saneforce.godairy.procurement.printer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saneforce.godairy.R;

import java.util.ArrayList;
import java.util.Set;

public class ChoosePrinterActivity2 extends AppCompatActivity {
    private Set<BluetoothDevice> bondedDevices;
    private String mPrinterName;
    private Button saveButton;
    private Button testButton;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_printer2);

        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null && !defaultAdapter.getBondedDevices().isEmpty()) {
            bondedDevices = defaultAdapter.getBondedDevices();
        } else {
            finishWithError();
        }
    }

    private void hideToolbar() {
        if (getActionBar() != null) {
            getActionBar().hide();
        }
    }

    private void finishWithError() {
        Intent intent = new Intent();
        intent.putExtra("printama", "failed to connect printer");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (bondedDevices == null) {
            finishWithError();
        } else {
            testButton = findViewById(R.id.btn_test_printer);
            testButton.setOnClickListener(v -> testPrinter());
            saveButton = findViewById(R.id.btn_save_printer);
            saveButton.setOnClickListener(v -> savePrinter());
            mPrinterName = Pref2.getString(Pref2.SAVED_DEVICE);
            toggleButtons();

            RecyclerView rvDeviceList = findViewById(R.id.rv_device_list);
            rvDeviceList.setLayoutManager(new LinearLayoutManager(this));
            ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>(bondedDevices);
            DeviceListAdapter2 adapter = new DeviceListAdapter2(bluetoothDevices, mPrinterName);
            rvDeviceList.setAdapter(adapter);
            adapter.setOnConnectPrinter(printerName -> {
                this.mPrinterName = printerName;
                toggleButtons();
            });
        }
    }

    private void testPrinter() {
        Printama2.with(this, mPrinterName).printTest();
    }

    private void toggleButtons() {
        if (mPrinterName != null) {
            testButton.setBackgroundColor(Color.GREEN);
            saveButton.setBackgroundColor(Color.GREEN);
        } else {
            testButton.setBackgroundColor(Color.GRAY);
            saveButton.setBackgroundColor(Color.GRAY);
        }
    }

    private void savePrinter() {
        Pref2.setString(Pref2.SAVED_DEVICE, mPrinterName);
        Intent intent = new Intent();
        intent.putExtra("printama", mPrinterName);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}