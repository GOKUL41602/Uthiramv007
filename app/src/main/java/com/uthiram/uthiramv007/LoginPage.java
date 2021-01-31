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

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {

    private TextInputLayout userName, password;

    private Button loginBtn, cancelBtn;

    private TextView registerBtn;

    private String userNameText, passwordText, userNameFromDB, passwordFromDB;

    private DatabaseReference reference;

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        initializeViews();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeStrings();
                if (validateUserName()) {
                    if (validatePassword()) {
                        verifyUserId();
                    } else {
                        validatePassword();
                    }
                } else {
                    validateUserName();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, OtpPage.class);
                startActivity(intent);
            }
        });
    }

    private void initializeStrings() {
        userNameText = userName.getEditText().getText().toString().trim();
        passwordText = password.getEditText().getText().toString().trim();
    }

    private boolean validateUserName() {
        if (userNameText.equals("") && userNameText.length() != 8) {
            userName.setError("Enter Valid UserName");
            userName.requestFocus();
            return false;
        } else {
            userName.setError(null);
            userName.setErrorEnabled(false);
            return true;
        }
    }

    private void verifyUserId() {
        reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
        Query query = reference.orderByChild("rollNo").startAt(userNameText).endAt(userNameText + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    passwordFromDB = snapshot.child(userNameText).child("password").getValue(String.class);
                    if (passwordText.equals(passwordFromDB)) {
                        Intent intent = new Intent(LoginPage.this, DonorHomePage.class);
                        startActivity(intent);
                    } else {
                        password.setError("Incorrect Password");
                        password.requestFocus();
                    }
                } else {
                    userName.setError("UserName doesn't Exists");
                    userName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean validatePassword() {
        if (passwordText.equals("") && passwordText.length() != 6) {
            password.setError("Enter Valid Password");
            password.requestFocus();
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private void initializeViews() {
        userName = findViewById(R.id.loginPage_userName);
        password = findViewById(R.id.loginPage_password);

        loginBtn = findViewById(R.id.loginPage_loginBtn);
        cancelBtn = findViewById(R.id.loginPage_cancelBtn);
        registerBtn = findViewById(R.id.loginPage_registerBtn);

        relativeLayout = findViewById(R.id.loginPage_relLayout);

        reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
    }

}