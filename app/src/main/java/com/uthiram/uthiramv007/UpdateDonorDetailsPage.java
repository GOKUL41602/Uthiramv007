package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

public class UpdateDonorDetailsPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private TextView bloodGroup, phoneNo, deptNameText;

    private TextInputLayout name, rollNo, age, weight, address, pinCode;

    private Spinner district;

    private RelativeLayout relativeLayout;

    private String userName, nameText, ageText, rollNoText, deptName, weightText, addressText, pinCodeText, phoneNoText, districtText, bloodGroupText, phoneNoFromDB, districtFromDB;

    private DatabaseReference reference;

    private Button updateBtn;

    public String loginPath = "null";

    private String rollNoPath = "null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_donor_details_page);
        userName = getIntent().getStringExtra("userName");
        phoneNoFromDB = getIntent().getStringExtra("phoneNo");
        initializeViews();
        initializeSpinners();
        loadDonorDetails();


        drawerLayout = findViewById(R.id.updateDonorDetailsPage_design_navigation_view);

        Toolbar toolbar = findViewById(R.id.updateDonorDetailsPage_toolbar);

        NavigationView navigationView = findViewById(R.id.updateDonorDetailsPage_nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

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
                                        if (validateDistrict()) {
                                            showSnackBar();
                                        } else {
                                            validateDistrict();
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
                    validateDonorName();
                }
            }
        });

    }

    private void initializeSpinners() {
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
    }

    private void settingDistrictNameInSpinner() {
        ArrayList<String> districtName1 = new ArrayList<>();
        districtName1.add("District Name");
        districtName1.add("Ariyalur");
        districtName1.add("Chengalpet");
        districtName1.add("Chennai");
        districtName1.add("Coimbatore");
        districtName1.add("Cuddalore");
        districtName1.add("Dharmapuri");
        districtName1.add("Dindigul");
        districtName1.add("Erode");
        districtName1.add("Kallakurichi");
        districtName1.add("Kancheepuram");
        districtName1.add("Karur");
        districtName1.add("Krishnagiri");
        districtName1.add("Madurai");
        districtName1.add("Nagapattinam");
        districtName1.add("Kanyakumari");
        districtName1.add("Namakkal");
        districtName1.add("Perambalur");
        districtName1.add("Pudukottai");
        districtName1.add("Ramanathapuram");
        districtName1.add("Ranipet");
        districtName1.add("Salem");
        districtName1.add("Sivagangai");
        districtName1.add("Tenkasi");
        districtName1.add("Thanjavur");
        districtName1.add("Theni");
        districtName1.add("Thiruvallur");
        districtName1.add("Thiruvarur");
        districtName1.add("Tuticorin");
        districtName1.add("Trichirappalli");
        districtName1.add("Thirunelveli");
        districtName1.add("Tirupathur");
        districtName1.add("Tiruppur");
        districtName1.add("Thiruvannamalai");
        districtName1.add("The Nilgiris");
        districtName1.add("Vellore");
        districtName1.add("Viluppuram");
        districtName1.add("Virudhunagar");
        districtName1.add("Other...");

        for (int i = 0; i < districtName1.size(); i++) {
            if (districtFromDB.equals(districtName1.get(i))) {
                districtName1.remove(i);
                districtName1.add(0, districtFromDB);
            }
        }

        ArrayAdapter<String> districtNameAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, districtName1);
        district.setAdapter(districtNameAdapter);
        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districtText = districtName1.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void showSnackBar() {
        Snackbar.make(relativeLayout, "Verify Phone Number", Snackbar.LENGTH_INDEFINITE).setAction("VERIFY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateDonorDetailsPage.this, UpdatePhoneNoOtpPage.class);
                intent.putExtra("phoneNo", phoneNoText);
                intent.putExtra("userName", rollNoText);
                intent.putExtra("donorName", nameText);
                intent.putExtra("weight", weightText);
                intent.putExtra("age", ageText);
                intent.putExtra("pinCode", pinCodeText);
                intent.putExtra("district", districtText);
                intent.putExtra("address", addressText);
                startActivity(intent);
                UpdateDonorDetailsPage.this.finish();
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
                    deptName = snapshot.child(userName).child("deptName").getValue(String.class);
                    pinCodeText = snapshot.child(userName).child("pinCode").getValue(String.class);
                    phoneNoText = snapshot.child(userName).child("phoneNo").getValue(String.class);
                    bloodGroupText = snapshot.child(userName).child("bloodGroup").getValue(String.class);
                    districtFromDB = snapshot.child(userName).child("district").getValue(String.class);

                    name.getEditText().setText(nameText);
                    age.getEditText().setText(ageText);
                    rollNo.getEditText().setText(rollNoText);
                    weight.getEditText().setText(weightText);
                    address.getEditText().setText(addressText);
                    pinCode.getEditText().setText(pinCodeText);
                    phoneNo.setText(phoneNoText);
                    deptNameText.setText("Department Name : " + deptName);
                    bloodGroup.setText(bloodGroupText);
                    settingDistrictNameInSpinner();

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
        deptNameText = findViewById(R.id.updateDonorDetailsPage_deptName);
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent intent = new Intent(UpdateDonorDetailsPage.this, RequestBloodDonor.class);
        startActivity(intent);
        UpdateDonorDetailsPage.this.finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.donor_home:
                Intent intent0 = new Intent(UpdateDonorDetailsPage.this, RequestBloodDonor.class);
                intent0.putExtra("userName", userName);
                startActivity(intent0);
                UpdateDonorDetailsPage.this.finish();
                break;
            case R.id.view_requests:
                Intent intent = new Intent(UpdateDonorDetailsPage.this, ViewRequests.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
                UpdateDonorDetailsPage.this.finish();
                break;
            case R.id.edit_donor_profile:
                Intent intent1 = new Intent(UpdateDonorDetailsPage.this, UpdateDonorDetailsPage.class);
                intent1.putExtra("userName", userName);
                startActivity(intent1);
                break;
            case R.id.edit_donor_status:
                Intent intent2 = new Intent(UpdateDonorDetailsPage.this, UpdateDonorStatusPage.class);
                intent2.putExtra("userName", userName);
                startActivity(intent2);
                UpdateDonorDetailsPage.this.finish();
                break;
            case R.id.donor_logout:
                loginPath = getExternalFilesDir("text").getAbsolutePath() + "/loginCredentials.txt";
                try {
                    FileWriter fw = new FileWriter(loginPath);
                    fw.write("");
                    fw.flush();
                    fw.close();
                } catch (Exception e) {
                    System.out.println(e);
                }

                rollNoPath = getExternalFilesDir("text").getAbsolutePath() + "/rollNo.txt";

                File file = new File(rollNoPath);
                try {
                    FileWriter fw = new FileWriter(rollNoPath);
                    fw.write("");
                    fw.flush();
                    fw.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                Intent intent3 = new Intent(UpdateDonorDetailsPage.this, EmergencyRequests.class);
                startActivity(intent3);
                UpdateDonorDetailsPage.this.finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}