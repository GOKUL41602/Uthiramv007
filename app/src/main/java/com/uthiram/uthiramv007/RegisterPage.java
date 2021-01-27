package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterPage extends AppCompatActivity {

    private TextInputLayout name, rollNo, age, phoneNo, address, pinCode, weight, createPassword, confirmPassword;
    private String nameText, rollNoText, ageText, phoneNoText, addressText, pinCodeText, weightText, createPasswordText, confirmPasswordText;
    private Spinner bloodGroup, deptName;
    private Button verifyBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        initializeViews();

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeStrings();
                if (validateName()) {
                    if (validateRollNo()) {
                        if (validateAge()) {
                            if (validatePhoneNo()) {
                                if (validateAddress()) {
                                    if (validatePinCode()) {
                                        if (validateWeight()) {
                                            if (validateCreatePassword()) {
                                                if (validateConfirmPassword()) {
                                                    if (verifyPassword()) {
                                                        if (validateBloodGroup()) {
                                                            if (validateDeptName()) {
                                                                Intent intent = new Intent(RegisterPage.this, OtpPage.class);
                                                                intent.putExtra("phoneNo", phoneNoText);
                                                                startActivity(intent);
                                                            } else {
                                                                validateDeptName();
                                                            }
                                                        } else {
                                                            validateBloodGroup();
                                                        }
                                                    } else {
                                                        verifyPassword();
                                                    }
                                                } else {
                                                    validateConfirmPassword();
                                                }
                                            } else {
                                                validateCreatePassword();
                                            }
                                        } else {
                                            validateWeight();
                                        }
                                    } else {
                                        validatePinCode();
                                    }
                                } else {
                                    validateAddress();
                                }
                            } else {
                                validatePhoneNo();
                            }
                        } else {
                            validateAge();
                        }
                    } else {
                        validateRollNo();
                    }
                } else {
                    validateName();
                }
            }
        });
    }

    private boolean validateName() {
        if (nameText.equals("") || nameText.length() < 3) {
            name.setError("Enter Valid Name");
            name.requestFocus();
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateRollNo() {
        if (rollNoText.equals("") || rollNoText.length() < 3) {
            rollNo.setError("Enter Valid Roll No");
            rollNo.requestFocus();
            return false;
        } else {
            rollNo.setError(null);
            rollNo.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateAge() {
        if (ageText.equals("") || ageText.length() != 2) {
            age.setError("Enter Valid Age");
            age.requestFocus();
            return false;
        } else {
            age.setError(null);
            age.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateAddress() {
        if (addressText.equals("") || addressText.length() <= 3) {
            address.setError("Enter Valid City/Village");
            address.requestFocus();
            return false;
        } else {
            address.setError(null);
            address.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhoneNo() {
        if (phoneNoText.equals("") || phoneNoText.length() != 10) {
            phoneNo.setError("Enter Valid Phone No");
            phoneNo.requestFocus();
            return false;
        } else {
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateBloodGroup() {
        if (bloodGroup.equals("Blood Group")) {
            Toast.makeText(this, "Please Select Blood Group", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateDeptName() {
        if (deptName.equals("Dept Name")) {
            Toast.makeText(this, "Please Select Department Name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePinCode() {
        if (pinCodeText.equals("") || pinCodeText.length() != 6) {
            pinCode.setError("Enter Valid PinCode");
            pinCode.requestFocus();
            return false;
        } else {
            pinCode.setError(null);
            pinCode.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateWeight() {
        int weightValue = Integer.parseInt(weightText);
        if (weightText.equals("") && weightValue <= 50) {
            weight.setError("Enter Valid Weight");
            weight.requestFocus();
            return false;
        } else {
            weight.setError(null);
            weight.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateCreatePassword() {
        if (createPasswordText.equals("") && createPasswordText.length() <= 6) {
            createPassword.setError("Enter Valid 6 Password");
            createPassword.requestFocus();
            return false;
        } else {
            createPassword.setError(null);
            createPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        if (confirmPasswordText.equals("") && confirmPasswordText.length() <= 6) {
            confirmPassword.setError("Enter Valid 6 Password");
            confirmPassword.requestFocus();
            return false;
        } else {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean verifyPassword() {
        if (createPasswordText.equals(confirmPasswordText)) {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        } else {
            confirmPassword.setError("Password doesn't Match");
            confirmPassword.requestFocus();
            return false;
        }
    }

    private void initializeStrings() {
        nameText = name.getEditText().getText().toString().trim();
        rollNoText = rollNo.getEditText().getText().toString().trim();
        ageText = age.getEditText().getText().toString().trim();
        phoneNoText = phoneNo.getEditText().getText().toString().trim();
        addressText = address.getEditText().getText().toString().trim();
        pinCodeText = pinCode.getEditText().getText().toString().trim();
        weightText = weight.getEditText().getText().toString().trim();
        createPasswordText = createPassword.getEditText().getText().toString().trim();
        confirmPasswordText = confirmPassword.getEditText().getText().toString().trim();
    }

    private void initializeViews() {
        name = findViewById(R.id.registerPage_donorName);
        rollNo = findViewById(R.id.registerPage_rollNo);
        age = findViewById(R.id.registerPage_age);
        phoneNo = findViewById(R.id.registerPage_phoneNo);
        address = findViewById(R.id.registerPage_address);
        pinCode = findViewById(R.id.registerPage_pinCode);
        weight = findViewById(R.id.registerPage_weight);
        createPassword = findViewById(R.id.registerPage_createPassword);
        confirmPassword = findViewById(R.id.registerPage_confirmPassword);

        bloodGroup = findViewById(R.id.registerPage_bloodGroup);
        deptName = findViewById(R.id.registerPage_deptName);

        verifyBtn = findViewById(R.id.registerPage_verifyBtn);
        cancelBtn = findViewById(R.id.registerPage_cancelBtn);
    }
}