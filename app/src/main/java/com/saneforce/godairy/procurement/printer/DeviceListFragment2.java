package com.saneforce.godairy.procurement.printer;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.saneforce.godairy.R;
import com.saneforce.godairy.procurement.MilkCollEntryActivity;

import java.util.ArrayList;
import java.util.Set;

public class DeviceListFragment2 extends DialogFragment {

    private Printama2.OnConnectPrinter onConnectPrinter;
    private Set<BluetoothDevice> bondedDevices;
    private String mPrinterName;
    private Button saveButton;
    private Button testButton;
    private int inactiveColor;
    private int activeColor;
    private int paperSize=80;
    private final String TAG = "MilkCollEntryActivity_";

    public DeviceListFragment2() {
        // Required empty public constructor
    }

    public static DeviceListFragment2 newInstance() {
        DeviceListFragment2 fragment = new DeviceListFragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_list, container, false);
    }

    public void setOnConnectPrinter(Printama2.OnConnectPrinter onConnectPrinter) {
        this.onConnectPrinter = onConnectPrinter;
    }

    public void setDeviceList(Set<BluetoothDevice> bondedDevices) {
        this.bondedDevices = bondedDevices;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        testButton = view.findViewById(R.id.btn_test_printer);
        testButton.setOnClickListener(v -> testPrinter());
        saveButton = view.findViewById(R.id.btn_save_printer);
        saveButton.setOnClickListener(v -> savePrinter());
        mPrinterName = Pref2.getString(Pref2.SAVED_DEVICE);

        RecyclerView rvDeviceList = view.findViewById(R.id.rv_device_list);
        rvDeviceList.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>(bondedDevices);
        DeviceListAdapter2 adapter = new DeviceListAdapter2(bluetoothDevices, mPrinterName);
        rvDeviceList.setAdapter(adapter);
        adapter.setOnConnectPrinter(printerName -> {
            this.mPrinterName = printerName;
            toggleButtons();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setColor();
        toggleButtons();
    }

    private void setColor() {
        if (getContext() != null) {
            if (this.activeColor == 0) {
                this.activeColor = ContextCompat.getColor(getContext(), R.color.call_preview_text2);
            }
            if (this.inactiveColor == 0) {
                this.inactiveColor = ContextCompat.getColor(getContext(), R.color.mdtp_light_gray);
            }
        }

    }

    private void testPrinter() {
        try {
            if( MilkCollEntryActivity.milkCollEntryActivity != null) {
                MilkCollEntryActivity.milkCollEntryActivity.printBill();
            }else{
                CallPreviewActivity2.mCallPreviewActivity2.printBill();
            }
        }catch (Exception e){
            Log.e(TAG, "Error! DeviceListFragment2.java: " + e.getMessage());
        }


        dismiss();

    }

    private void toggleButtons() {
        if (getContext() != null) {
            if (mPrinterName != null) {
                testButton.setBackgroundColor(activeColor);
                saveButton.setBackgroundColor(activeColor);
            } else {
                testButton.setBackgroundColor(inactiveColor);
                saveButton.setBackgroundColor(inactiveColor);
            }
        }
    }

    private void savePrinter() {
        Pref2.setString(Pref2.SAVED_DEVICE, mPrinterName);
        if (onConnectPrinter != null) {
            onConnectPrinter.onConnectPrinter(mPrinterName);
        }
        //   dismiss();
    }

    public void setColorTheme(int activeColor, int inactiveColor) {
        if (activeColor != 0) {
            this.activeColor = activeColor;
        }
        if (inactiveColor != 0) {
            this.inactiveColor = inactiveColor;
        }
    }

    public void setPaperSize(int size){
        this.paperSize=size;
    }



}
