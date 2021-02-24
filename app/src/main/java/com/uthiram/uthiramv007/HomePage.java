package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private Spinner deptNameSpinner, bloodGroupSpinner;
    private String deptName, bloodGroup;
    private RelativeLayout relativeLayout;
    private Button filterBtn;
    private HomePageRecAdapter adapter;
    private ProgressBar progressBar;
    String count1;
    int count;

    private String loginPath = "null";

    private String rollNoPath = "null";

    private String rollNo = "";

    private char[] j = new char[1];

    private char r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initializeViews();
        initializeSpinners();
        loginPath = getExternalFilesDir("text").getAbsolutePath() + "/loginCredentials.txt";

        rollNoPath = getExternalFilesDir("text").getAbsolutePath() + "/rollNo.txt";

        progressBar.setVisibility(View.VISIBLE);

        FileReader fr = null;
        try {
            fr = new FileReader(loginPath);
            int i;
            while ((i = fr.read()) != -1)
                j[0] = (char) i;
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(rollNoPath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String st;
            while ((st = br.readLine()) != null)
                rollNo = st;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        drawerLayout = findViewById(R.id.homePage_design_navigation_view);

        Toolbar toolbar = findViewById(R.id.homePage_toolbar);

        NavigationView navigationView = findViewById(R.id.homePage_nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("DonorsDto");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long count = snapshot.getChildrenCount();
                if (count != 0) {
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(HomePage.this, "No Records to Display !", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePage.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<DonorsDto> options
                = new FirebaseRecyclerOptions.Builder<DonorsDto>()
                .setQuery(FirebaseDatabase.getInstance().getReference("DonorsDto"), DonorsDto.class)
                .build();
        adapter = new HomePageRecAdapter(options);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);



        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bloodGroup.equals("Blood Group") && deptName.equals("Dept Name")) {
                    Snackbar.make(relativeLayout, "Please select Any Filter!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                } else {
                    if (!bloodGroup.equals("Blood Group") && !deptName.equals("Dept Name")) {
                        Intent intent = new Intent(HomePage.this, FilteredHomePage.class);
                        intent.putExtra("1", deptName);
                        intent.putExtra("2", bloodGroup);
                        startActivity(intent);
                    } else if (!bloodGroup.equals("Blood Group")) {
                        Intent intent = new Intent(HomePage.this, FilteredBloodHomePage.class);
                        intent.putExtra("1", deptName);
                        intent.putExtra("2", bloodGroup);
                        startActivity(intent);
                    } else if (!deptName.equals("Dept Name")) {
                        Intent intent = new Intent(HomePage.this, FilteredDeptHomePage.class);
                        intent.putExtra("1", deptName);
                        intent.putExtra("2", bloodGroup);
                        startActivity(intent);
                    }
                }
            }

        });
    }


    private void initializeSpinners() {
        ArrayList<String> bloodList = new ArrayList<String>();
        bloodList.add("Blood Group");
        bloodList.add("B+");
        bloodList.add("B-");
        bloodList.add("AB+");
        bloodList.add("AB-");
        bloodList.add("A+");
        bloodList.add("A-");
        bloodList.add("O+");
        bloodList.add("O-");

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(HomePage.this, android.R.layout.simple_spinner_dropdown_item, bloodList);
        bloodGroupSpinner.setAdapter(bloodAdapter);
        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroup = bloodList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> deptNameList = new ArrayList<String>();

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

        ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(HomePage.this, android.R.layout.simple_spinner_dropdown_item, deptNameList);
        deptNameSpinner.setAdapter(deptAdapter);
        deptNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deptName = deptNameList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.homePage_recView);
        deptNameSpinner = findViewById(R.id.homePage_deptSpinner);
        bloodGroupSpinner = findViewById(R.id.homePage_bloodGroupSpinner);
        relativeLayout = findViewById(R.id.homePage_relLayout);
        filterBtn = findViewById(R.id.homePage_filterBtn);
        progressBar = findViewById(R.id.homePage_progressBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent intent = new Intent(HomePage.this, EmergencyRequests.class);
        startActivity(intent);
        HomePage.this.finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                Intent intent = new Intent(HomePage.this, EmergencyRequests.class);
                startActivity(intent);
                HomePage.this.finish();
                break;
            case R.id.filter:
                Intent intent1 = new Intent(HomePage.this, HomePage.class);
                startActivity(intent1);
                HomePage.this.finish();
                break;
            case R.id.donor_login:
                if (checkLoginCredential()) {
                    Intent intent2 = new Intent(HomePage.this, LoginPage.class);
                    intent2.putExtra("rollNo", rollNo);
                    startActivity(intent2);
                    HomePage.this.finish();
                } else {
                    Toast.makeText(HomePage.this, "Login to create Request", Toast.LENGTH_SHORT).show();
                    Intent intent3 = new Intent(HomePage.this, LoginPage.class);
                    rollNo = "123456";
                    intent3.putExtra("rollNo", rollNo);
                    startActivity(intent3);
                    HomePage.this.finish();
                }
                break;
            case R.id.about_us:
                Intent intent3 = new Intent(HomePage.this, AboutUs.class);
                startActivity(intent3);
                HomePage.this.finish();
                break;
            case R.id.contact_us:
                Intent intent4 = new Intent(HomePage.this, ContactUs.class);
                startActivity(intent4);
                HomePage.this.finish();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkLoginCredential() {
        int k = Character.compare(j[0], '1');
        if (k == 0) {

            return true;
        } else {
            return false;
        }
    }
}