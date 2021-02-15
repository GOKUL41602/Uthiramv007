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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.text.format.DateFormat.format;
import static com.uthiram.uthiramv007.R.string.navigation_draw_open;
import static java.text.DateFormat.*;

public class RequestBloodDonor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private RelativeLayout relativeLayout;

    private TextInputLayout patientName, unitsNeeded, hospitalName, patientPhoneNo, neededDate, neededTime;

    private Button selectDateBtn, selectTimeBtn, requestDonorBtn, cancelBtn;

    private Spinner bloodGroup;

    private String patientNameText, unitsNeededText, hospitalNameText, patientPhoneNoText, neededDateText, neededTimeText, bloodGroupText;

    private String userName;

    private int t1minute, t1hour;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood_donor);
        initializeViews();

        userName = getIntent().getStringExtra("userName");


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
        drawerLayout = findViewById(R.id.requestBloodDonor_design_navigation_view);

        Toolbar toolbar = findViewById(R.id.toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        requestDonorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeStrings();
                if (validatePatientName()) {
                    if (validateBloodGroup()) {
                        if (validateUnitsNeeded()) {
                            if (validateHospitalName()) {
                                if (validatePatientPhoneNo()) {
                                    if (validateDate()) {
                                        if (validateTime()) {
                                            uploadRequestDetails();
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

    private void uploadRequestDetails() {
        reference = FirebaseDatabase.getInstance().getReference("RequestDonorDto");
        String key = reference.push().getKey();
        RequestDonorDto requestDonorDto = new RequestDonorDto(userName, patientNameText, bloodGroupText, unitsNeededText, hospitalNameText, patientPhoneNoText, neededDateText, neededTimeText, key);
        reference.child(userName).child(key).setValue(requestDonorDto);
        reference = FirebaseDatabase.getInstance().getReference("EmergencyRequests");
        RequestDonorDto requestDonorDto1 = new RequestDonorDto(userName, patientNameText, bloodGroupText, unitsNeededText, hospitalNameText, patientPhoneNoText, neededDateText, neededTimeText, key);
        reference.child(key).setValue(requestDonorDto1);
        showSnack();
    }

    private void showSnack() {
        Snackbar.make(relativeLayout, "Request Blood Donor Successful", Snackbar.LENGTH_SHORT).show();
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.view_requests:
                Intent intent = new Intent(RequestBloodDonor.this, ViewRequests.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
                break;
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