package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
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

public class ForgetPasswordOtpVerifyPage extends AppCompatActivity {

    private TextInputLayout phoneNo, otp;

    private RelativeLayout relativeLayout;

    private String phoneNum, verificationId, userName, password;

    private CountryCodePicker ccp;

    private Button verifyBtn, backBtn;

    private ProgressBar progressBar;

    private TextView sendText;

    private FirebaseAuth fAuth;

    private PhoneAuthProvider.ForceResendingToken token;

    private Boolean verificationProgress = false;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_otp_verify_page);
        initializeViews();
        verifyBtn.setText("Send Otp");
        password = getIntent().getStringExtra("pass");
        userName = getIntent().getStringExtra("user");
        Log.d("User : ", userName);
        Log.d("Pass :", password);
        fAuth = FirebaseAuth.getInstance();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPasswordOtpVerifyPage.this, ForgetPasswordPage.class);
                startActivity(intent);
                ForgetPasswordOtpVerifyPage.this.finish();
            }
        });
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verificationProgress) {
                    if (phoneNo.getEditText().getText().toString().isEmpty() || phoneNo.getEditText().getText().toString().length() == 10) {
                        phoneNo.setError(null);
                        phoneNo.setErrorEnabled(false);
                        phoneNum = "+" + ccp.getSelectedCountryCode() + phoneNo.getEditText().getText().toString();
                        progressBar.setVisibility(View.VISIBLE);
                        sendText.setText("sending");
                        sendText.setVisibility(View.VISIBLE);
                        requestOtp(phoneNum);
                    } else {
                        phoneNo.setError("Enter Valid 10 Digit Phone Number");
                        phoneNo.requestFocus();
                    }
                } else {
                    if (count < 2) {
                        if (!otp.getEditText().getText().toString().isEmpty() && otp.getEditText().getText().toString().length() == 6) {
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp.getEditText().getText().toString());
                            verifyOtp(credential);
                        } else {
                            Toast.makeText(ForgetPasswordOtpVerifyPage.this, "Valid OTP is Required", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(ForgetPasswordOtpVerifyPage.this, OtpPage.class);
                        count = 0;
                        Toast.makeText(ForgetPasswordOtpVerifyPage.this, "Authentication Failed Please Try Again!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void updatePasswordToDB() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
        Query query = reference.orderByChild("rollNo").startAt(userName).endAt(userName + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reference.child(userName).child("password").setValue(password);
                    Toast.makeText(ForgetPasswordOtpVerifyPage.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void verifyOtp(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgetPasswordOtpVerifyPage.this, "Authentication is successful", Toast.LENGTH_SHORT).show();
                    updatePasswordToDB();
                    Snackbar.make(relativeLayout, "Password changed Successfully", Snackbar.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ForgetPasswordOtpVerifyPage.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestOtp(String phoneNum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.GONE);

                sendText.setVisibility(View.GONE);
                otp.setVisibility(View.VISIBLE);
                verificationId = s;
                token = forceResendingToken;
                verifyBtn.setText("Verify");
                verificationProgress = true;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(ForgetPasswordOtpVerifyPage.this, "cannot access account" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        });
    }

    private void initializeViews() {
        phoneNo = findViewById(R.id.forgetPasswordOtpVerifyPage_phoneNo);
        otp = findViewById(R.id.forgetPasswordOtpVerifyPage_otp);

        relativeLayout = findViewById(R.id.forgetPasswordOtpVerifyPage_relLayout);

        ccp = findViewById(R.id.forgetPasswordOtpVerifyPage_ccp);

        verifyBtn = findViewById(R.id.forgetPasswordOtpVerifyPage_verifyBtn);
        backBtn = findViewById(R.id.forgetPasswordOtpVerifyPage_backBtn);

        progressBar = findViewById(R.id.forgetPasswordOtpVerifyPage_progressBar);

        sendText = findViewById(R.id.forgetPasswordOtpVerifyPage_sendText);
    }
}