package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;

import static android.text.format.DateFormat.format;
import static com.uthiram.uthiramv007.R.string.navigation_draw_open;
import static java.text.DateFormat.*;

public class RequestBloodDonor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private TextInputLayout patientName, unitsNeeded, hospitalName, patientPhoneNo, neededDate, neededTime;

    private Button selectDateBtn, selectTimeBtn, requestDonorBtn, cancelBtn;

    private Spinner bloodGroup;

    private String patientNameText, unitsNeededText, hospitalNameText, patientPhoneNoText, neededDateText, neededTimeText, bloodGroupText;

    private String userName;

    private int t1minute, t1hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood_donor);
        initializeViews();

        userName = getIntent().getStringExtra("userName");

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT LAST DATE FOR DONATION");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                neededDate.getEditText().setText(materialDatePicker.getHeaderText());
            }
        });


        selectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(
                        RequestBloodDonor.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        t1hour = hourOfDay;
                        t1minute = minute;
                        Calendar calender = Calendar.getInstance();
                        calender.set(0, 0, 0, t1hour, t1minute);
                        neededTime.getEditText().setText(format("hh:mm aa", calender));
                    }
                }, 12, 0, false
                );

                timePicker.updateTime(t1hour, t1minute);
                timePicker.show();
            }

        });

        selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });
        drawerLayout = findViewById(R.id.design_navigation_view);

        Toolbar toolbar = findViewById(R.id.toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
    }


    private void initializeViews() {
        bloodGroup = findViewById(R.id.requestBloodDonor_bloodGroup);

        patientName = findViewById(R.id.requestBloodDonor_patientName);

        patientPhoneNo = findViewById(R.id.requestBloodDonor_patientPhoneNo);

        unitsNeeded = findViewById(R.id.requestBloodDonor_unitsNeeded);

        hospitalName = findViewById(R.id.requestBloodDonor_hospitalName);

        neededDate = findViewById(R.id.requestBloodDonor_date);

        neededTime = findViewById(R.id.requestBloodDonor_time);

        selectDateBtn = findViewById(R.id.requestBloodDonor_selectDateBtn);

        selectTimeBtn = findViewById(R.id.requestBloodDonor_selectTimeBtn);

        requestDonorBtn = findViewById(R.id.requestBloodDonor_requestDonorBtn);

        cancelBtn = findViewById(R.id.requestBloodDonor_cancelBtn);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.donor_home:
                Intent intent0 = new Intent(RequestBloodDonor.this, RequestBloodDonor.class);
                intent0.putExtra("userName", userName);
                startActivity(intent0);
                break;
            case R.id.edit_donor_profile:
                Intent intent1 = new Intent(RequestBloodDonor.this, UpdateDonorDetailsPage.class);
                intent1.putExtra("userName", userName);
                startActivity(intent1);
                break;
            case R.id.edit_donor_status:
                Intent intent2 = new Intent(RequestBloodDonor.this, UpdateDonorStatusPage.class);
                intent2.putExtra("userName", userName);
                startActivity(intent2);
                break;
            case R.id.donor_logout:
                Intent intent3 = new Intent(RequestBloodDonor.this, LoginPage.class);
                startActivity(intent3);
                RequestBloodDonor.this.finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}