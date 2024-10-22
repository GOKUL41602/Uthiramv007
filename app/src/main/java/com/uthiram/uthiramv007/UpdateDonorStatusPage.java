package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;

import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

public class UpdateDonorStatusPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private Button selectDateBtn, updateStatusBtn;

    private TextView dateText, showDate, statusTextView;

    private RadioButton available, unAvailable;

    private RelativeLayout relativeLayout;

    private String userName, statusText;

    private DatabaseReference reference;

    public String loginPath = "null";

    private String rollNoPath = "null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_donor_status_page);

        initializeViews();
        userName = getIntent().getStringExtra("userName");

        loadDonorDetails();


        drawerLayout = findViewById(R.id.updateDonorStatusPage_design_navigation_view);

        Toolbar toolbar = findViewById(R.id.updateDonorStatusPage_toolbar);

        NavigationView navigationView = findViewById(R.id.updateDonorStatusPage_nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A LAST DONATED DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        showDate.setText(materialDatePicker.getHeaderText());
                    }
                });

        selectDateBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });


        updateStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStatus();
                reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
                Query query = reference.orderByChild("rollNo").startAt(userName).endAt(userName + "\uf8ff");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (statusText != "Null") {
                                reference.child(userName).child("status").setValue(statusText);
                                reference.child(userName).child("lastDonatedDate").setValue(showDate.getText().toString());
                                Toast.makeText(UpdateDonorStatusPage.this, "Status Updated Successfully", Toast.LENGTH_SHORT).show();
                                loadDonorDetails();
                            } else {
                                Snackbar.make(relativeLayout, "Please Select Your Status !", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("RETRY", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        }).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    private void loadDonorDetails() {
        reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
        Query query = reference.orderByChild("rollNo").startAt(userName).endAt(userName + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String lastDonatedDateFromDB = snapshot.child(userName).child("lastDonatedDate").getValue(String.class);
                    String statusTextFromDB = snapshot.child(userName).child("status").getValue(String.class);

                    showDate.setText(lastDonatedDateFromDB);
                    statusTextView.setText("Current Status : " + statusTextFromDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getStatus() {
        if (available.isChecked()) {
            statusText = "Available";
        } else if (unAvailable.isChecked()) {
            statusText = "Unavailable";
        } else {
            statusText = "Null";
        }
    }

    private void initializeViews() {
        selectDateBtn = findViewById(R.id.updateDonorStatusPage_pickDateButton);
        dateText = findViewById(R.id.updateDonorStatusPage_lastDonatedDateText);
        showDate = findViewById(R.id.updateDonorStatusPage_lastDonatedDate);
        available = findViewById(R.id.updateDonorStatusPage_availableRadioBtn);
        unAvailable = findViewById(R.id.updateDonorStatusPage_unAvailableRadioBtn);
        updateStatusBtn = findViewById(R.id.updateDonorStatusPage_updateStatusBtn);
        statusTextView = findViewById(R.id.updateDonorStatusPage_statusTextView);
        relativeLayout = findViewById(R.id.updateDonorStatusPage_relLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent intent = new Intent(UpdateDonorStatusPage.this, RequestBloodDonor.class);
        startActivity(intent);
        UpdateDonorStatusPage.this.finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.donor_home:
                Intent intent0 = new Intent(UpdateDonorStatusPage.this, RequestBloodDonor.class);
                intent0.putExtra("userName", userName);
                startActivity(intent0);
                UpdateDonorStatusPage.this.finish();
                break;

            case R.id.view_requests:
                Intent intent = new Intent(UpdateDonorStatusPage.this, ViewRequests.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
                UpdateDonorStatusPage.this.finish();
                break;
            case R.id.edit_donor_profile:
                Intent intent1 = new Intent(UpdateDonorStatusPage.this, UpdateDonorDetailsPage.class);
                intent1.putExtra("userName", userName);
                startActivity(intent1);
                UpdateDonorStatusPage.this.finish();
                break;
            case R.id.edit_donor_status:
                Intent intent2 = new Intent(UpdateDonorStatusPage.this, UpdateDonorStatusPage.class);
                intent2.putExtra("userName", userName);
                startActivity(intent2);
                UpdateDonorStatusPage.this.finish();
                break;
            case R.id.donor_logout:loginPath = getExternalFilesDir("text").getAbsolutePath() + "/loginCredentials.txt";
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
                Intent intent3 = new Intent(UpdateDonorStatusPage.this, EmergencyRequests.class);
                startActivity(intent3);
                UpdateDonorStatusPage.this.finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}