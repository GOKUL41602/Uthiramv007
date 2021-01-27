package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginPage extends AppCompatActivity {

    private TextInputLayout userName, password;

    private Button loginBtn, cancelBtn;

    private TextView registerBtn;

    private String userNameText, passwordText,userNameFromDB,passwordFromDB;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        initializeViews();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeStrings();
            }
        });
    }

    private void initializeStrings() {
        userNameText = userName.getEditText().getText().toString().trim();
        passwordText = password.getEditText().getText().toString().trim();
    }

    private boolean validateUserName() {
        if (userNameText.equals("") || userNameText.length() != 8) {
            userName.setError("Enter Valid UserName");
            userName.requestFocus();
            return true;
        } else {
            userName.setError(null);
            userName.setErrorEnabled(false);
            return false;
        }
    }

    private void verifyUserId() {

    }

    private boolean validatePassword() {
        if (passwordText.equals("") || userNameText.length() != 6) {
            password.setError("Enter Valid Password");
            password.requestFocus();
            return true;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return false;
        }
    }

    private void initializeViews() {
        userName = findViewById(R.id.loginPage_userName);
        password = findViewById(R.id.loginPage_password);

        loginBtn = findViewById(R.id.loginPage_loginBtn);
        cancelBtn = findViewById(R.id.loginPage_cancelBtn);
        registerBtn = findViewById(R.id.loginPage_registerBtn);

        reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
    }

}