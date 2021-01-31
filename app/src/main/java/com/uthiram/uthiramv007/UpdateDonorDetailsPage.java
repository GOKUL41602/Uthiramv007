package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.util.ArrayList;

public class UpdateDonorDetailsPage extends AppCompatActivity {

    private TextView bloodGroup, phoneNo;

    private TextInputLayout name, rollNo, age, weight, address, pinCode;

    private Spinner district;

    private RelativeLayout relativeLayout;

    private String userName, nameText, ageText, rollNoText, weightText, addressText, pinCodeText, phoneNoText, districtText, bloodGroupText;

    private DatabaseReference reference;

    private Button updateBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_donor_details_page);
        userName = getIntent().getStringExtra("userName");
        initializeViews();
        loadDonorDetails();

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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeStrings();
                if (validateDonorName()) {
                    if (validateRollNo()) {
                        if (validateAge()) {
                            if (validateAddress()) {
                                if (validatePinCode()) {
                                    if (validateWeight()) {
                                        showSnackBar();
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
                    validateDonorName();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateDonorDetailsPage.this, DonorHomePage.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

    }

    private void showSnackBar() {
        Snackbar.make(relativeLayout, "Verify Phone Number", Snackbar.LENGTH_INDEFINITE).setAction("VERIFY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateDonorDetailsPage.this, UpdatePhoneNoOtpPage.class);
                intent.putExtra("phoneNo", phoneNoText);
                intent.putExtra("userName", rollNoText);
                startActivity(intent);
            }
        }).show();
    }

    private void loadDonorDetails() {
        reference = FirebaseDatabase.getInstance().getReference("DonorsDto");
        Query query = reference.orderByChild("rollNo").startAt(userName).endAt(userName + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nameText = snapshot.child(userName).child("donorName").getValue(String.class);
                    ageText = snapshot.child(userName).child("age").getValue(String.class);
                    rollNoText = userName;
                    weightText = snapshot.child(userName).child("weight").getValue(String.class);
                    addressText = snapshot.child(userName).child("address").getValue(String.class);
                    pinCodeText = snapshot.child(userName).child("pinCode").getValue(String.class);
                    phoneNoText = snapshot.child(userName).child("phoneNo").getValue(String.class);
                    districtText = snapshot.child(userName).child("district").getValue(String.class);
                    bloodGroupText = snapshot.child(userName).child("bloodGroup").getValue(String.class);

                    name.getEditText().setText(nameText);
                    age.getEditText().setText(ageText);
                    rollNo.getEditText().setText(rollNoText);
                    weight.getEditText().setText(weightText);
                    address.getEditText().setText(addressText);
                    pinCode.getEditText().setText(pinCodeText);
                    phoneNo.setText(phoneNoText);
                    bloodGroup.setText(bloodGroupText);
                } else {
                    Toast.makeText(UpdateDonorDetailsPage.this, "User doesn't exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeStrings() {
        nameText = name.getEditText().getText().toString().trim();
        ageText = age.getEditText().getText().toString().trim();
        rollNoText = rollNo.getEditText().getText().toString().trim();
        weightText = weight.getEditText().getText().toString().trim();
        addressText = address.getEditText().getText().toString().trim();
        pinCodeText = pinCode.getEditText().getText().toString().trim();

    }

    private void initializeViews() {
        name = findViewById(R.id.updateDonorDetailsPage_name);
        rollNo = findViewById(R.id.updateDonorDetailsPage_rollNo);
        age = findViewById(R.id.updateDonorDetailsPage_age);
        weight = findViewById(R.id.updateDonorDetailsPage_weight);
        address = findViewById(R.id.updateDonorDetailsPage_address);
        pinCode = findViewById(R.id.updateDonorDetailsPage_pinCode);
        phoneNo = findViewById(R.id.updateDonorDetailsPage_phoneNo);
        bloodGroup = findViewById(R.id.updateDonorDetailsPage_bloodGroup);
        district = findViewById(R.id.updateDonorDetailsPage_district);

        updateBtn = findViewById(R.id.updateDonorDetailsPage_updateBtn);
        cancelBtn = findViewById(R.id.updateDonorDetailsPage_cancelBtn);

        relativeLayout = findViewById(R.id.updateDonorDetailsPage_relLayout);
    }

    private boolean validateDonorName() {
        if (nameText.equals("")) {
            name.setError("Please Enter Donor Name");
            name.requestFocus();
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateRollNo() {
        if (rollNoText.equals("")) {
            rollNo.setError("Please Enter Roll No");
            rollNo.requestFocus();
            return false;
        } else {
            rollNo.setError(null);
            rollNo.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateAge() {
        int ageInfo = Integer.parseInt(ageText);
        if (ageText.equals("") || ageInfo < 18) {
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
        if (addressText.equals("")) {
            address.setError("Please Enter Address");
            address.requestFocus();
            return false;
        } else {
            address.setError(null);
            address.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePinCode() {

        if (pinCodeText.equals("") || pinCodeText.length() != 6) {
            pinCode.setError("Please Enter Valid Pin Code");
            pinCode.requestFocus();
            return false;
        } else {
            pinCode.setError(null);
            pinCode.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateWeight() {
        int weightInfo = Integer.parseInt(weightText);
        if (weightText.equals("") || weightInfo < 50) {
            weight.setError("Please Enter Valid weight");
            weight.requestFocus();
            return false;
        } else {
            weight.setError(null);
            weight.setErrorEnabled(false);
            return true;
        }
    }
}