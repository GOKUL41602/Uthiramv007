package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TimeActivity extends AppCompatActivity {

    private TimePicker timePicker;
    int hour, min;
    Button okBtn, cancelBtn;
    String strHrsToShow="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        timePicker = findViewById(R.id.timePicker);
        okBtn = findViewById(R.id.timeOkBtn);
        cancelBtn = findViewById(R.id.timeCancelBtn);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;
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

                 strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";

                Toast.makeText(TimeActivity.this, strHrsToShow + ":" + minu + " " + am_pm, Toast.LENGTH_SHORT).show();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeActivity.this, RequestBloodDonor.class);
                intent.putExtra("time",strHrsToShow);
                startActivity(intent);
            }
        });
    }
}