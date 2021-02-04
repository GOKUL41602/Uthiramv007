package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UpdateDonorStatusPage extends AppCompatActivity {

    private Button selectDateBtn, backBtn, updateStatusBtn;

    private TextView dateText, showDate, statusTextView;

    private RadioButton available, unAvailable;

    private RelativeLayout relativeLayout;

    private String userName, statusText;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_donor_status_page);

        initializeViews();
        userName = getIntent().getStringExtra("userName");

        loadDonorDetails();

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


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateDonorStatusPage.this, DonorHomePage.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
                UpdateDonorStatusPage.this.finish();
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
                } else {

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
        backBtn = findViewById(R.id.updateDonorStatusPage_backBtn);
        available = findViewById(R.id.updateDonorStatusPage_availableRadioBtn);
        unAvailable = findViewById(R.id.updateDonorStatusPage_unAvailableRadioBtn);
        updateStatusBtn = findViewById(R.id.updateDonorStatusPage_updateStatusBtn);
        statusTextView = findViewById(R.id.updateDonorStatusPage_statusTextView);
        relativeLayout = findViewById(R.id.updateDonorStatusPage_relLayout);

    }
}