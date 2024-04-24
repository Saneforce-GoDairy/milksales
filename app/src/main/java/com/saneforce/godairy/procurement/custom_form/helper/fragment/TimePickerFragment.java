package com.saneforce.godairy.procurement.custom_form.helper.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import com.saneforce.godairy.R;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    LinearLayout parentLayout;

    public TimePickerFragment() {
    }

    public void setParentLayout(LinearLayout parentLayout) {
        this.parentLayout = parentLayout;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    public void onTimeSet(TimePicker view, int hour, int minute) {
        TextView textView = (TextView) parentLayout.findViewWithTag("time");
        textView.setText(getString(R.string.bf_chosen_time, hour + ":" + minute));
    }
}
