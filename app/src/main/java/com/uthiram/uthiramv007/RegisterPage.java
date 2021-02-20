package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterPage extends AppCompatActivity {

    private TextInputLayout name, rollNo, age, address, pinCode, weight, createPassword, confirmPassword;
    private String nameText, rollNoText, ageText, phoneNoText, addressText, pinCodeText, deptNameText, districtText, bloodGroupText, weightText, createPasswordText, confirmPasswordText, lastDonatedDate, status, districtName;
    private Spinner bloodGroup, deptName, district;
    private Button verifyBtn, cancelBtn;
    private RelativeLayout relativeLayout;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        initializeViews();
        phoneNoText = getIntent().getStringExtra("phoneNo");

        ArrayList<String> bloodsList = new ArrayList<>();
        bloodsList.add("Blood Group");
        bloodsList.add("B+");
        bloodsList.add("A+");
        bloodsList.add("AB-");
        bloodsList.add("AB+");
        bloodsList.add("O-");
        bloodsList.add("O+");

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(RegisterPage.this, android.R.layout.simple_spinner_dropdown_item, bloodsList);
        bloodGroup.setAdapter(bloodAdapter);
        bloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroupText = bloodsList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayList<String> deptNameList = new ArrayList<>();

        deptNameList.add("Dept Name");
        deptNameList.add("IT");
        deptNameList.add("CSE");
        deptNameList.add("FT");
        deptNameList.add("EEE");
        deptNameList.add("ME");
        deptNameList.add("AU");
        deptNameList.add("AE");
        deptNameList.add("BT");
        deptNameList.add("CE");
        deptNameList.add("ECE");
        deptNameList.add("EIE");
        deptNameList.add("ISE");
        deptNameList.add("MC");
        deptNameList.add("TT");
        deptNameList.add("MCA");
        deptNameList.add("MBA");

        ArrayAdapter<String> deptNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, deptNameList);
        deptName.setAdapter(deptNameAdapter);
        deptName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deptNameText = deptNameList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayList<String> districtName = new ArrayList<>();
        districtName.add("District Name");
        districtName.add("Ariyalur");
        districtName.add("Chengalpet");
        districtName.add("Chennai");
        districtName.add("Coimbatore");
        districtName.add("Cuddalore");
        districtName.add("Dharmapuri");
        districtName.add("Dindigul");
        districtName.add("Erode");
        districtName.add("Kallakurichi");
        districtName.add("Kancheepuram");
        districtName.add("Karur");
        districtName.add("Krishnagiri");
        districtName.add("Madurai");
        districtName.add("Nagapattinam");
        districtName.add("Kanyakumari");
        districtName.add("Namakkal");
        districtName.add("Perambalur");
        districtName.add("Pudukottai");
        districtName.add("Ramanathapuram");
        districtName.add("Ranipet");
        districtName.add("Salem");
        districtName.add("Sivagangai");
        districtName.add("Tenkasi");
        districtName.add("Thanjavur");
        districtName.add("Theni");
        districtName.add("Thiruvallur");
        districtName.add("Thiruvarur");
        districtName.add("Tuticorin");
        districtName.add("Trichirappalli");
        districtName.add("Thirunelveli");
        districtName.add("Tirupathur");
        districtName.add("Tiruppur");
        districtName.add("Thiruvannamalai");
        districtName.add("The Nilgiris");
        districtName.add("Vellore");
        districtName.add("Viluppuram");
        districtName.add("Virudhunagar");
        districtName.add("Other...");

        ArrayAdapter<String> districtNameAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, districtName);
        district.setAdapter(districtNameAdapter);
        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districtText = districtName.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeStrings();
                if (validateName()) {
                    if (validateRollNo()) {
                        if (validateAge()) {
                            if (validateAddress()) {
                                if (validatePinCode()) {
                                    if (validateWeight()) {
                                        if (validateCreatePassword()) {
                                            if (validateConfirmPassword()) {
                                                if (verifyPassword()) {
                                                    if (verifyAge()) {
                                                        if (verifyWeight()) {
                                                            if (verifyPinCode()) {
                                                                if (validateDept()) {
                                                                    if (validateBloodGroup()) {
                                                                        if (validateDistrict()) {
                                                                            checkUserName();
                                                                        } else {
                                                                            validateDistrict();
                                                                        }
                                                                    } else {
                                                                        validateBloodGroup();
                                                                    }
                                                                } else {
                                                                    validateDept();
                                                                }
                                                            } else {
                                                                verifyPinCode();
                                                            }
                                                        } else {
                                                            verifyWeight();
                                                        }
                                                    } else {
                                                        verifyAge();
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

    private boolean validateDistrict() {
        if (districtText.equals("District Name")) {
            Snackbar.make(relativeLayout, "Please Select District Name", Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateBloodGroup() {
        if (bloodGroupText.equals("Blood Group")) {
            Snackbar.make(relativeLayout, "Please Select Blood Group", Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateDept() {
        if (deptNameText.equals("Dept Name")) {
            Snackbar.make(relativeLayout, "Please Select Department Name", Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void checkUserName() {
        reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
        Query query = reference.orderByChild("rollNo").startAt(rollNoText).endAt(rollNoText + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    rollNo.setError("RollNo Already Exists!");
                    rollNo.requestFocus();
                } else {
                    reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
                    status = "Null";
                    lastDonatedDate = "Null";
                    DonorsDto donorsDto = new DonorsDto(nameText, rollNoText, ageText, bloodGroupText, phoneNoText, addressText, districtText, pinCodeText, weightText, confirmPasswordText, status, deptNameText, lastDonatedDate);
                    reference.child(rollNoText).setValue(donorsDto);
                    showSnackBar();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showSnackBar() {
        Snackbar.make(relativeLayout, "Registration Successful", Snackbar.LENGTH_INDEFINITE)
                .setAction("LOGIN", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                        startActivity(intent);
                    }
                }).show();
    }


    private boolean validateName() {
        if (nameText.equals("") || nameText.length() <= 3) {
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
        if (rollNoText.equals("") || rollNoText.length() != 8) {
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
        if (ageText.equals("")) {
            age.setError("Enter Valid Age");
            age.requestFocus();
            return false;
        } else {
            age.setError(null);
            age.setErrorEnabled(false);
            return true;
        }
    }

    private boolean verifyAge() {
        int Age = Integer.parseInt(ageText);
        if (Age < 19) {
            age.setError("Please Enter Valid Age");
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


    private boolean validatePinCode() {
        if (pinCodeText.equals("")) {
            pinCode.setError("Enter Valid PinCode");
            pinCode.requestFocus();
            return false;
        } else {
            pinCode.setError(null);
            pinCode.setErrorEnabled(false);
            return true;
        }
    }

    private boolean verifyPinCode() {
        if (pinCodeText.length() != 6) {
            pinCode.setError("Enter Valid Pin Code");
            pinCode.requestFocus();
            return false;
        } else {
            pinCode.setError(null);
            pinCode.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateWeight() {

        if (weightText.equals("")) {
            weight.setError("Enter Valid Weight");
            weight.requestFocus();
            return false;
        } else {
            weight.setError(null);
            weight.setErrorEnabled(false);
            return true;
        }
    }

    private boolean verifyWeight() {
        int Weight = Integer.parseInt(weightText);
        if (Weight >= 50) {
            weight.setErrorEnabled(false);
            weight.setError(null);
            return true;
        } else {
            weight.setError("Please Enter Valid Weight");
            return false;
        }
    }

    private boolean validateCreatePassword() {
        if (createPasswordText.equals("") || createPasswordText.length() < 6) {
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
        if (confirmPasswordText.equals("") || confirmPasswordText.length() < 6) {
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

        address = findViewById(R.id.registerPage_address);
        pinCode = findViewById(R.id.registerPage_pinCode);
        weight = findViewById(R.id.registerPage_weight);
        createPassword = findViewById(R.id.registerPage_createPassword);
        confirmPassword = findViewById(R.id.registerPage_confirmPassword);

        bloodGroup = findViewById(R.id.registerPage_bloodGroup);
        deptName = findViewById(R.id.registerPage_deptName);
        district = findViewById(R.id.registerPage_district);

        verifyBtn = findViewById(R.id.registerPage_verifyBtn);
        cancelBtn = findViewById(R.id.registerPage_cancelBtn);
        relativeLayout = findViewById(R.id.registerPage_relLayout);
    }
}