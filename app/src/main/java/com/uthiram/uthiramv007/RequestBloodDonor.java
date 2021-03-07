package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.text.format.DateFormat.format;
import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

public class RequestBloodDonor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private TextView textView;

    private RelativeLayout relativeLayout, timeLayout;

    private TimePicker timePicker;

    private TextInputLayout patientName, unitsNeeded, hospitalName, patientPhoneNo, neededDate, neededTime;

    private Button selectDateBtn, selectTimeBtn, requestDonorBtn, cancelBtn,okBtn;

    private Spinner bloodGroup;

    private String patientNameText, unitsNeededText, hospitalNameText, patientPhoneNoText, neededDateText, neededTimeText, bloodGroupText;

    private String userName, currentTime, currentDate, date, phoneNo, userNameCred;

    private int t1minute = 0, t1hour = 0;

    private DatabaseReference reference;

    private Date date1, date2;

    private boolean global = false;

    private String loginPath = "null",time = "", currentTimeForText,rollNoPath = "null", strHrsToShow;

    int hour, min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood_donor);
        initializeViews();

        currentTimeForText = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
        textView.setText(currentTimeForText);

        userName = getIntent().getStringExtra("userName");

        phoneNo = getIntent().getStringExtra("phoneNo");

        currentDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

        ArrayList<String> bloodGroupList = new ArrayList<>();
        bloodGroupList.add("Blood Group");
        bloodGroupList.add("O+");
        bloodGroupList.add("O-");
        bloodGroupList.add("A+");
        bloodGroupList.add("A-");
        bloodGroupList.add("B+");
        bloodGroupList.add("B-");
        bloodGroupList.add("AB+");
        bloodGroupList.add("AB-");

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(RequestBloodDonor.this, android.R.layout.simple_dropdown_item_1line, bloodGroupList);
        bloodGroup.setAdapter(bloodAdapter);
        bloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroupText = bloodGroupList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT LAST DATE FOR DONATION");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                neededDate.getEditText().setText(materialDatePicker.getHeaderText());
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestBloodDonor.this, RequestBloodDonor.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
                RequestBloodDonor.this.finish();
            }
        });

        selectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
                timeLayout.setVisibility(View.VISIBLE);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectTime();
                        neededTime.getEditText().setText(time);
                        relativeLayout.setVisibility(View.VISIBLE);
                        timeLayout.setVisibility(View.GONE);
                    }
                });
                neededTime.getEditText().setText(textView.getText().toString());
            }
        });

        selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });
        drawerLayout = findViewById(R.id.requestBloodDonor_design_navigation_view);

        Toolbar toolbar = findViewById(R.id.requestBloodDonor_toolbar);

        NavigationView navigationView = findViewById(R.id.requestBloodDonor_nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        requestDonorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeStrings();
                if (neededDateText.length() == 10) {
                    date = String.format("0%s-%s-%s", neededDateText.substring(0, 1), neededDateText.substring(2, 5), neededDateText.substring(6, 10));
                } else if (neededDateText.length() == 11) {
                    date = String.format("%s-%s-%s", neededDateText.substring(0, 2), neededDateText.substring(3, 6), neededDateText.substring(7, 11));
                }
                neededDate.getEditText().setText(date);
                if (validatePatientName()) {
                    if (validateBloodGroup()) {
                        if (validateUnitsNeeded()) {
                            if (validateHospitalName()) {
                                if (validatePatientPhoneNo()) {
                                    if (validateDate()) {
                                        if (validateTime()) {
                                            if (verifyDate()) {
                                                if (global) {
                                                    uploadRequestDetails();
                                                } else {
                                                    Toast.makeText(RequestBloodDonor.this, "Select Proper Date And Time", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                verifyDate();
                                            }
                                        } else {
                                            validateTime();
                                        }
                                    } else {
                                        validateDate();
                                    }
                                } else {
                                    validatePatientPhoneNo();
                                }
                            } else {
                                validateHospitalName();
                            }
                        } else {
                            validateUnitsNeeded();
                        }
                    } else {
                        validateBloodGroup();
                    }
                } else {
                    validatePatientName();
                }
            }
        });
    }


    private void selectTime() {
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

                time = strHrsToShow + ":" + minu + " " + am_pm;
                textView.setText(time);
                time = textView.getText().toString();
                neededTime.getEditText().setText(textView.getText().toString());

            }
        });
    }

    private void uploadRequestDetails() {

        reference = FirebaseDatabase.getInstance().getReference("RequestDonorDto");
        String key = reference.push().getKey();
        RequestDonorDto requestDonorDto = new RequestDonorDto(userName, patientNameText, bloodGroupText, unitsNeededText, hospitalNameText, patientPhoneNoText, neededDateText, neededTimeText, key);
        reference.child(userName).child(key).setValue(requestDonorDto);
        reference = FirebaseDatabase.getInstance().getReference("EmergencyRequests");
        RequestDonorDto requestDonorDto1 = new RequestDonorDto(userName, patientNameText, bloodGroupText, unitsNeededText, hospitalNameText, patientPhoneNoText, neededDateText, neededTimeText, key);
        reference.child(key).setValue(requestDonorDto1);
        Toast.makeText(this, "Request Blood Donor Successful", Toast.LENGTH_SHORT).show();
        showSnack();
    }

    private void showSnack() {

        Intent intent = new Intent(RequestBloodDonor.this, ViewRequests.class);
        intent.putExtra("userName", userName);
        startActivity(intent);
        RequestBloodDonor.this.finish();
    }

    private boolean validatePatientName() {
        if (patientNameText.isEmpty() || patientNameText.length() <= 2) {
            patientName.setError("Please Enter Patient Name");
            patientName.requestFocus();
            return false;
        } else {
            patientName.setError(null);
            patientName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePatientPhoneNo() {
        if (patientPhoneNoText.length() != 10) {
            patientPhoneNo.setError("Please Enter Valid Phone No");
            patientPhoneNo.requestFocus();
            return false;
        } else {
            patientPhoneNo.setError(null);
            patientPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUnitsNeeded() {
        if (unitsNeededText.isEmpty()) {
            unitsNeeded.setError("Please Enter Units Needed");
            unitsNeeded.requestFocus();
            return false;
        } else {
            unitsNeeded.setError(null);
            unitsNeeded.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateHospitalName() {
        if (hospitalNameText.isEmpty()) {
            hospitalName.setError("Please Enter Hospital Name");
            hospitalName.requestFocus();
            return false;
        } else {
            hospitalName.setError(null);
            hospitalName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateBloodGroup() {
        if (bloodGroupText.equals("Blood Group")) {
            Toast.makeText(this, "Please Select Blood Group", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateDate() {
        if (neededDateText.isEmpty()) {
            neededDate.setError("Please Enter WithNeeded Date");
            neededDate.requestFocus();
            return false;
        } else {
            neededDate.setError(null);
            neededDate.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateTime() {
        if (neededTimeText.isEmpty()) {
            neededTime.setError("Please Enter WithNeeded Time");
            neededTime.requestFocus();
            return false;
        } else {
            neededTime.setError(null);
            neededTime.setErrorEnabled(false);
            return true;
        }
    }

    private void initializeStrings() {
        patientNameText = patientName.getEditText().getText().toString().trim();
        patientPhoneNoText = patientPhoneNo.getEditText().getText().toString().trim();
        unitsNeededText = unitsNeeded.getEditText().getText().toString().trim();
        hospitalNameText = hospitalName.getEditText().getText().toString().trim();
        neededDateText = neededDate.getEditText().getText().toString().trim();
        neededTimeText = neededTime.getEditText().getText().toString().trim();
    }

    private void initializeViews() {

        textView = findViewById(R.id.timeHideText);

        timeLayout = findViewById(R.id.timePickerRelLayout);

        okBtn = findViewById(R.id.timeOkBtn);

        timePicker = findViewById(R.id.timePicker);

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

        relativeLayout = findViewById(R.id.requestBloodDonor_relLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent intent = new Intent(RequestBloodDonor.this, EmergencyRequests.class);
        startActivity(intent);
        RequestBloodDonor.this.finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.view_requests:
                Intent intent = new Intent(RequestBloodDonor.this, ViewRequests.class);
                intent.putExtra("userName", userName);
                RequestBloodDonor.this.finish();
                startActivity(intent);
                break;
            case R.id.donor_home:
                Intent intent0 = new Intent(RequestBloodDonor.this, RequestBloodDonor.class);
                intent0.putExtra("userName", userName);
                RequestBloodDonor.this.finish();
                startActivity(intent0);
                break;
            case R.id.edit_donor_profile:
                Intent intent1 = new Intent(RequestBloodDonor.this, UpdateDonorDetailsPage.class);
                intent1.putExtra("userName", userName);
                intent1.putExtra("phoneNo", phoneNo);
                RequestBloodDonor.this.finish();
                startActivity(intent1);
                break;
            case R.id.edit_donor_status:
                Intent intent2 = new Intent(RequestBloodDonor.this, UpdateDonorStatusPage.class);
                intent2.putExtra("userName", userName);
                RequestBloodDonor.this.finish();
                startActivity(intent2);
                break;
            case R.id.donor_logout:
                loginPath = getExternalFilesDir("text").getAbsolutePath() + "/loginCredentials.txt";
                try {
                    FileWriter fw = new FileWriter(loginPath);
                    fw.write("");
                    fw.flush();
                    fw.close();
                } catch (Exception e) {
                    System.out.println(e);
                }

                rollNoPath = getExternalFilesDir("text").getAbsolutePath() + "/rollNo.txt";

                File file = new File(rollNoPath);
                try {
                    FileWriter fw = new FileWriter(rollNoPath);
                    fw.write("");
                    fw.flush();
                    fw.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                Intent intent3 = new Intent(RequestBloodDonor.this, EmergencyRequests.class);
                startActivity(intent3);
                RequestBloodDonor.this.finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isTimeWith_in_Interval(String startTime, String endTime) {
        boolean isBetween = false;
        try {
            Date time1 = new SimpleDateFormat("h:mm a").parse(startTime);

            Date time2 = new SimpleDateFormat("h:mm a").parse(endTime);

            if (time2.after(time1)) {
                isBetween = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }


    public boolean isDate(String startDate, String endDate) {
        boolean isBetween = false;
        try {
            Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(startDate);

            Date date2 = new SimpleDateFormat("dd-MMM-yyyy").parse(endDate);

            if (date2.after(date1)) {
                isBetween = true;
                global = true;
            } else {
                if (date2.equals(date1)) {
                    isBetween = true;
                    global = verifyTime(isBetween);

                } else {
                    isBetween = false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }

    private boolean verifyTime(boolean isBetween) {
        boolean bool = false;
        if (isBetween) {
            if (isTimeWith_in_Interval(currentTime, neededTimeText)) {
                neededTime.setErrorEnabled(false);
                neededTime.setError(null);
                bool = true;
            } else {
                neededTime.setError("Choose Valid Time");
                bool = false;
            }
        } else {
            Toast.makeText(this, "Select Proper Date ", Toast.LENGTH_SHORT).show();
        }
        return bool;
    }

    private boolean verifyDate() {
        if (isDate(currentDate, date)) {
            neededDate.setError(null);
            neededDate.setErrorEnabled(false);
            return true;
        } else {
            neededDate.setError("Choose Valid Date");
            return false;
        }
    }
}