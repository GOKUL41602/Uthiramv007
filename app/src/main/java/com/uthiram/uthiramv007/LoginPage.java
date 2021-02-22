package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

import java.io.FileWriter;

import static androidx.browser.trusted.sharing.ShareTarget.FileFormField.KEY_NAME;

public class LoginPage extends AppCompatActivity {

    private TextInputLayout userName, password;

    private Button loginBtn, forgetPasswordBtn;

    private ProgressBar progressBar;

    private TextView registerBtn;

    private String userNameText, passwordText, userNameFromDB, passwordFromDB, phoneNoFromDB, phoneFromDBForAutoLogin;

    public String rollNo = "null";

    private String rollNoPath = "null";

    private DatabaseReference reference;

    private RelativeLayout relativeLayout;

    private String loginPath = "null";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        initializeViews();

        rollNo = getIntent().getStringExtra("rollNo");

        if (rollNo.length() == 8) {
            checkForRollNoInDB();

        } else {
            progressBar.setVisibility(View.GONE);
            forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initializeStrings();
                    if (validateUserName()) {
                        checkUserName();
                    } else {
                        validateUserName();
                    }
                }
            });
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initializeStrings();
                    if (validateUserName()) {
                        if (validatePassword()) {
                            progressBar.setVisibility(View.VISIBLE);
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

    }

    private void checkForRollNoInDB() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
        Query query = reference.orderByChild("rollNo").startAt(rollNo).endAt(rollNo + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phoneFromDBForAutoLogin = snapshot.child(rollNo).child("phoneNo").getValue(String.class);
                    Intent intent = new Intent(LoginPage.this, RequestBloodDonor.class);
                    intent.putExtra("userName", rollNo);
                    intent.putExtra("phoneNo", phoneFromDBForAutoLogin);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(LoginPage.this, LoginPage.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginPage.this, EmergencyRequests.class);
        startActivity(intent);
        LoginPage.this.finish();
    }

    private void initializeStrings() {
        userNameText = userName.getEditText().getText().toString().trim();
        passwordText = password.getEditText().getText().toString().trim();
    }

    private void checkUserName() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
        Query query = reference.orderByChild("rollNo").startAt(userNameText).endAt(userNameText + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userName.setError(null);
                    userName.setErrorEnabled(false);
                    passwordFromDB = snapshot.child(userNameText).child("password").getValue(String.class);
                    phoneNoFromDB = snapshot.child(userNameText).child("phoneNo").getValue(String.class);
                    Intent intent = new Intent(LoginPage.this, ForgetPasswordPage.class);
                    intent.putExtra("userName", userNameText);
                    intent.putExtra("phoneNo", phoneNoFromDB);
                    startActivity(intent);
                } else {
                    userName.setError("User Name Doesn't match");
                    userName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    phoneNoFromDB = snapshot.child(userNameText).child("phoneNo").getValue(String.class);
                    if (passwordText.equals(passwordFromDB)) {
                        loginPath = getExternalFilesDir("text").getAbsolutePath() + "/loginCredentials.txt";
                        try {
                            FileWriter fw = new FileWriter(loginPath);
                            fw.write("1");
                            fw.close();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        rollNoPath = getExternalFilesDir("text").getAbsolutePath() + "/rollNo.txt";
                        try {
                            FileWriter fw = new FileWriter(rollNoPath);
                            fw.write(userNameText);
                            fw.close();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        Intent intent = new Intent(LoginPage.this, RequestBloodDonor.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("userName", userNameText);
                        intent.putExtra("phoneNo", phoneNoFromDB);
                        startActivity(intent);
                    } else {

                        password.setError("Incorrect Password");
                        password.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    userName.setError("UserName doesn't Exists");
                    userName.requestFocus();
                    progressBar.setVisibility(View.GONE);
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
        progressBar = findViewById(R.id.loginPage_progressbar);
        userName = findViewById(R.id.loginPage_userName);
        password = findViewById(R.id.loginPage_password);
        loginBtn = findViewById(R.id.loginPage_loginBtn);
        registerBtn = findViewById(R.id.loginPage_registerBtn);
        forgetPasswordBtn = findViewById(R.id.loginPage_forgetPasswordBtn);
        relativeLayout = findViewById(R.id.loginPage_relLayout);
        reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
    }
}