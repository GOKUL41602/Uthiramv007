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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static android.text.format.DateFormat.format;
import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

public class EditEmergencyRequest extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private RelativeLayout relativeLayout;

    private TextInputLayout patientName, unitsNeeded, hospitalName, patientPhoneNo, neededDate, neededTime;

    private Button selectDateBtn, selectTimeBtn, updateDonorBtn, cancelBtn;

    private TextView bloodGroup;

    private String patientNameText, unitsNeededText, hospitalNameText, patientPhoneNoText, neededDateText, neededTimeText;

    private String patientNameFromDB, unitsNeededFromDB, hospitalNameFromDB, patientPhoneNoFromDB, neededDateFromDB, neededTimeFromDB, bloodGroupFromDB;

    private String userName, key;

    private int t1minute, t1hour;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_emergency_request);

        key = getIntent().getStringExtra("key");
        Log.d("key",key);

        userName = getIntent().getStringExtra("userName");

        initializeViews();
        loadRequestDetails();


        drawerLayout = findViewById(R.id.design_navigation_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEmergencyRequest.this, RequestBloodDonor.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
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

        selectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(
                        EditEmergencyRequest.this, new TimePickerDialog.OnTimeSetListener() {
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

        updateDonorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeStrings();
                if (validatePatientName()) {
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
                    validatePatientName();
                }
            }
        });
    }

    private void loadRequestDetails() {
        reference = FirebaseDatabase.getInstance().getReference("EmergencyRequests");
        Query query = reference.orderByChild("key").startAt(key).endAt(key + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    patientNameFromDB = snapshot.child(key).child("patientName").getValue(String.class);
                    patientPhoneNoFromDB = snapshot.child(key).child("patientPhoneNo").getValue(String.class);
                    unitsNeededFromDB = snapshot.child(key).child("unitsNeeded").getValue(String.class);
                    neededTimeFromDB = snapshot.child(key).child("neededWithInTime").getValue(String.class);
                    neededDateFromDB = snapshot.child(key).child("neededWithInDate").getValue(String.class);
                    hospitalNameFromDB = snapshot.child(key).child("hospitalName").getValue(String.class);
                    bloodGroupFromDB = snapshot.child(key).child("bloodGroup").getValue(String.class);


                    patientName.getEditText().setText(patientNameFromDB);
                    unitsNeeded.getEditText().setText(unitsNeededFromDB);
                    patientPhoneNo.getEditText().setText(patientPhoneNoFromDB);
                    neededDate.getEditText().setText(neededDateFromDB);
                    neededTime.getEditText().setText(neededTimeFromDB);
                    hospitalName.getEditText().setText(hospitalNameFromDB);
                    bloodGroup.setText(bloodGroupFromDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadRequestDetails() {
        reference = FirebaseDatabase.getInstance().getReference("RequestDonorDto");
        RequestDonorDto requestDonorDto = new RequestDonorDto(userName, patientNameText, bloodGroupFromDB, unitsNeededText, hospitalNameText, patientPhoneNoText, neededDateText, neededTimeText, key);
        reference.child(userName).child(key).setValue(requestDonorDto);
        reference = FirebaseDatabase.getInstance().getReference("EmergencyRequests");
        RequestDonorDto requestDonorDto1 = new RequestDonorDto(userName, patientNameText, bloodGroupFromDB, unitsNeededText, hospitalNameText, patientPhoneNoText, neededDateText, neededTimeText, key);
        reference.child(key).setValue(requestDonorDto1);
        showSnack();
    }

    private void showSnack() {
        Snackbar.make(relativeLayout, "Request Blood Donor Successful", Snackbar.LENGTH_SHORT).show();
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
        bloodGroup = findViewById(R.id.editRequestBloodDonor_bloodGroup);

        patientName = findViewById(R.id.editRequestBloodDonor_patientName);

        patientPhoneNo = findViewById(R.id.editRequestBloodDonor_patientPhoneNo);

        unitsNeeded = findViewById(R.id.editRequestBloodDonor_unitsNeeded);

        hospitalName = findViewById(R.id.editRequestBloodDonor_hospitalName);

        neededDate = findViewById(R.id.editRequestBloodDonor_date);

        neededTime = findViewById(R.id.editRequestBloodDonor_time);

        selectDateBtn = findViewById(R.id.editRequestBloodDonor_selectDateBtn);

        selectTimeBtn = findViewById(R.id.editRequestBloodDonor_selectTimeBtn);

        updateDonorBtn = findViewById(R.id.editRequestBloodDonor_updateDonorBtn);

        cancelBtn = findViewById(R.id.editRequestBloodDonor_cancelBtn);

        relativeLayout = findViewById(R.id.editRequestBloodDonor_relLayout);

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
                Intent intent0 = new Intent(EditEmergencyRequest.this, RequestBloodDonor.class);
                intent0.putExtra("userName", userName);
                startActivity(intent0);
                break;
            case R.id.edit_donor_profile:
                Intent intent1 = new Intent(EditEmergencyRequest.this, UpdateDonorDetailsPage.class);
                intent1.putExtra("userName", userName);
                startActivity(intent1);
                break;
            case R.id.edit_donor_status:
                Intent intent2 = new Intent(EditEmergencyRequest.this, UpdateDonorStatusPage.class);
                intent2.putExtra("userName", userName);
                startActivity(intent2);
                break;
            case R.id.donor_logout:
                Intent intent3 = new Intent(EditEmergencyRequest.this, LoginPage.class);
                startActivity(intent3);
                EditEmergencyRequest.this.finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}