package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class ForgetPasswordPage extends AppCompatActivity {

    private TextInputLayout newPassword, confirmPassword;

    private String newPasswordText, confirmPasswordText;

    private Button changePasswordBtn;

    private RelativeLayout relativeLayout;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_page);
        initializeViews();
        userName = getIntent().getStringExtra("userName");
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeStrings();
                if (validateNewPassword()) {
                    if (validateConfirmPassword()) {
                        if (verifyPassword()) {
                            showSnackBar();
                        } else {
                            verifyPassword();
                        }
                    } else {
                        validateConfirmPassword();
                    }
                } else {
                    validateNewPassword();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ForgetPasswordPage.this, LoginPage.class);
        startActivity(intent);
        ForgetPasswordPage.this.finish();
    }

    private void showSnackBar() {
        Snackbar.make(relativeLayout, "Verify PhoneNo for Changing Password", Snackbar.LENGTH_INDEFINITE)
                .setAction("VERIFY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ForgetPasswordPage.this, ForgetPasswordOtpVerifyPage.class);
                        intent.putExtra("pass", confirmPasswordText);
                        intent.putExtra("user", userName);
                        startActivity(intent);
                    }
                }).show();
    }

    private void initializeStrings() {
        newPasswordText = newPassword.getEditText().getText().toString().trim();
        confirmPasswordText = confirmPassword.getEditText().getText().toString().trim();
    }

    private boolean validateNewPassword() {
        if (newPasswordText.equals("") || newPasswordText.length() < 6) {
            newPassword.setError("Enter Valid 6 Digit Password");
            newPassword.requestFocus();
            return false;
        } else {
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        if (confirmPasswordText.equals("") || confirmPasswordText.length() < 6) {
            confirmPassword.setError("Enter Valid 6 Digit Password");
            confirmPassword.requestFocus();
            return false;
        } else {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean verifyPassword() {
        if (newPasswordText.equals(confirmPasswordText)) {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        } else {
            confirmPassword.setError("Password doesn't Match");
            confirmPassword.requestFocus();
            return false;
        }
    }

    private void initializeViews() {
        newPassword = findViewById(R.id.forgetPasswordPage_newPassword);
        confirmPassword = findViewById(R.id.forgetPasswordPage_confirmPassword);
        changePasswordBtn = findViewById(R.id.forgetPasswordPage_changePasswordBtn);
        relativeLayout = findViewById(R.id.forgetPasswordPage_relLayout);
    }
}