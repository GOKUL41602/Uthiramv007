package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewDonorProfile extends AppCompatActivity {

    private TextView donorName, rollNo, age, address, district, deptName, bloodGroup, pinCode, phoneNo, status, lastDonatedDate, weight;

    private String userName, donorNameText, rollNoText, ageText, addressText, districtText, deptNameText, bloodGroupText, pinCodeText, phoneNoText, statusText, lastDonatedDateText, weightText;

    private Button backBtn;

    private RelativeLayout relativeLayout;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donor_profile);
        initializeViews();

        userName = getIntent().getStringExtra("userName");
        loadDonorDetails();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewDonorProfile.this, DonorHomePage.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
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
                    donorNameText = snapshot.child(userName).child("donorName").getValue(String.class);
                    rollNoText = userName;
                    ageText = snapshot.child(userName).child("age").getValue(String.class);
                    weightText = snapshot.child(userName).child("weight").getValue(String.class);
                    statusText = snapshot.child(userName).child("status").getValue(String.class);
                    addressText = snapshot.child(userName).child("address").getValue(String.class);
                    phoneNoText = snapshot.child(userName).child("phoneNo").getValue(String.class);
                    pinCodeText = snapshot.child(userName).child("pinCode").getValue(String.class);
                    districtText = snapshot.child(userName).child("district").getValue(String.class);
                    deptNameText = snapshot.child(userName).child("deptName").getValue(String.class);
                    bloodGroupText = snapshot.child(userName).child("bloodGroup").getValue(String.class);
                    lastDonatedDateText = snapshot.child(userName).child("lastDonatedDate").getValue(String.class);

                    donorName.setText(donorNameText);
                    rollNo.setText(rollNoText);
                    age.setText(ageText);
                    weight.setText(weightText);
                    status.setText(statusText);
                    phoneNo.setText(phoneNoText);
                    address.setText(addressText);
                    pinCode.setText(pinCodeText);
                    district.setText(districtText);
                    deptName.setText(deptNameText);
                    bloodGroup.setText(bloodGroupText);
                    lastDonatedDate.setText(lastDonatedDateText);

                } else {
                    Toast.makeText(ViewDonorProfile.this, "User Doesn't exists ! ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeViews() {
        donorName = findViewById(R.id.viewDonorProfile_donorName);
        rollNo = findViewById(R.id.viewDonorProfile_rollNo);
        age = findViewById(R.id.viewDonorProfile_age);
        address = findViewById(R.id.viewDonorProfile_address);
        district = findViewById(R.id.viewDonorProfile_district);
        deptName = findViewById(R.id.viewDonorProfile_dept);
        bloodGroup = findViewById(R.id.viewDonorProfile_blood);
        pinCode = findViewById(R.id.viewDonorProfile_pinCode);
        phoneNo = findViewById(R.id.viewDonorProfile_phoneNo);
        status = findViewById(R.id.viewDonorProfile_status);
        lastDonatedDate = findViewById(R.id.viewDonorProfile_lastDonatedDate);
        weight = findViewById(R.id.viewDonorProfile_weight);

        relativeLayout = findViewById(R.id.viewDonorProfile_relLayout);

        backBtn = findViewById(R.id.viewDonorProfile_backBtn);
    }
}