package com.uthiram.uthiramv007;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    Calendar c = Calendar.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, min, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        RequestBloodDonor activity = (RequestBloodDonor) getActivity();
        activity.processTimePicker(hourOfDay, minute, c);
    }
}