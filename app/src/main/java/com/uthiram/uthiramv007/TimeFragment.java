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
    int min;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
         min = c.get(Calendar.MINUTE);


        return new TimePickerDialog(getActivity(), this, hour, min, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        RequestBloodDonor activity = (RequestBloodDonor) getActivity();

        String minu = String.valueOf(min);
        if (minu.length() == 1) {
            minu = "0" + minu;
        }

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);

        String am_pm = "";
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";

        activity.processTimePicker(hourOfDay, minute, c);
    }
}