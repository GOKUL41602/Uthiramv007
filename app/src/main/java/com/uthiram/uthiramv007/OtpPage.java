package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class OtpPage extends AppCompatActivity {
    FirebaseAuth fAuth;
    EditText phonenumber, codeEnter;
    Button nextBtn;
    ProgressBar progressBar;
    TextView state;
    CountryCodePicker codePicker;
    String verificationId, phoneNum;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean verificationProgress = false;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);
        fAuth = FirebaseAuth.getInstance();
        phonenumber = findViewById(R.id.phone);
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
                        Toast.makeText(OtpPage.this, "Redirecting to Browser to verify Phone Number", Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(OtpPage.this, OtpPage.class);
                        count = 0;
                        Toast.makeText(OtpPage.this, "Authentication Failed Please Try Again!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(OtpPage.this, "Authentication is successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OtpPage.this, RegisterPage.class);
                    intent.putExtra("phoneNo", phoneNum);
                    startActivity(intent);
                    OtpPage.this.finish();
                } else {
                    Toast.makeText(OtpPage.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    count++;
                }
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
                Toast.makeText(OtpPage.this, "cannot access account" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                state.setVisibility(View.GONE);
            }
        });
    }
}
