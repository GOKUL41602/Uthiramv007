package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class UpdatePhoneNoOtpPage extends AppCompatActivity {

    FirebaseAuth fAuth;
    EditText phonenumber, codeEnter;
    Button nextBtn;
    ProgressBar progressBar;
    TextView state;
    CountryCodePicker codePicker;
    String verificationId, phoneNum, userName, rollNo, age, weight, address, district, pinCode, phoneNo, donorName;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean verificationProgress = false;

    private int count = 0;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone_no_otp_page);
        fAuth = FirebaseAuth.getInstance();
        phonenumber = findViewById(R.id.phone);
        userName = getIntent().getStringExtra("userName");
        donorName = getIntent().getStringExtra("donorName");
        rollNo = getIntent().getStringExtra("rollNo");
        age = getIntent().getStringExtra("age");
        weight = getIntent().getStringExtra("weight");
        address = getIntent().getStringExtra("address");
        district = getIntent().getStringExtra("district");
        pinCode = getIntent().getStringExtra("pinCode");
        phoneNo = getIntent().getStringExtra("phoneNo");
        codeEnter = findViewById(R.id.codeEnter);
        progressBar = findViewById(R.id.progressBar);
        nextBtn = findViewById(R.id.nextBtn);
        state = findViewById(R.id.state);
        codePicker = findViewById(R.id.ccp);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!verificationProgress) {
                    if (!phonenumber.getText().toString().isEmpty() && phonenumber.getText().toString().length() == 10) {
                        phoneNum = "+" + codePicker.getSelectedCountryCode() + phonenumber.getText().toString();
                        progressBar.setVisibility(View.VISIBLE);
                        state.setText("sending");
                        state.setVisibility(View.VISIBLE);
                        requestOTP(phoneNum);

                    } else {
                        phonenumber.setError("Phone number is not valid");
                    }
                } else {
                    if (count < 1) {
                        String userOTP = codeEnter.getText().toString();
                        if (!userOTP.isEmpty() && userOTP.length() == 6) {
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, userOTP);
                            verifyAuth(credential);

                        } else {
                            codeEnter.setError("Valid OTP is required");
                        }
                    } else {
                        Intent intent = new Intent(UpdatePhoneNoOtpPage.this, OtpPage.class);
                        count = 0;
                        Toast.makeText(UpdatePhoneNoOtpPage.this, "Authentication Failed Please Try Again!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void verifyAuth(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdatePhoneNoOtpPage.this, "Updated successful", Toast.LENGTH_SHORT).show();
                    updateDonorDetailsToDB();
                    Intent intent = new Intent(UpdatePhoneNoOtpPage.this, RequestBloodDonor.class);
                    intent.putExtra("userName", userName);
                    UpdatePhoneNoOtpPage.this.finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(UpdatePhoneNoOtpPage.this, "Updation Failed", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UpdatePhoneNoOtpPage.this, RequestBloodDonor.class);
        startActivity(intent);
        UpdatePhoneNoOtpPage.this.finish();
    }

    private void updateDonorDetailsToDB() {
        reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
        Query query = reference.orderByChild("rollNo").startAt(userName).endAt(userName + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reference.child(userName).child("donorName").setValue(donorName);
                    reference.child(userName).child("rollNo").setValue(userName);
                    reference.child(userName).child("age").setValue(age);
                    reference.child(userName).child("weight").setValue(weight);
                    reference.child(userName).child("pinCode").setValue(pinCode);
                    reference.child(userName).child("address").setValue(address);
                    reference.child(userName).child("phoneNo").setValue(phoneNum);
                    reference.child(userName).child("district").setValue(district);
                } else {
                    Toast.makeText(UpdatePhoneNoOtpPage.this, "User doesn't Exists !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void requestOTP(String phoneNum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.GONE);

                state.setVisibility(View.GONE);
                codeEnter.setVisibility(View.VISIBLE);
                verificationId = s;
                token = forceResendingToken;
                nextBtn.setText("Verify");
                verificationProgress = true;


            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(UpdatePhoneNoOtpPage.this, "cannot access account" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}