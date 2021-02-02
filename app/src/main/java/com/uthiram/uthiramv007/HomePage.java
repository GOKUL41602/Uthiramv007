package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner deptNameSpinner, bloodGroupSpinner;
    private String deptName, bloodGroup;
    private RelativeLayout relativeLayout;
    private Button filterBtn;
    private FloatingActionButton loginBtn;
    private HomePageRecAdapter adapter;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initializeViews();
        initializeSpinners();
        sp = getSharedPreferences("login", MODE_PRIVATE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<DonorsDto> options
                = new FirebaseRecyclerOptions.Builder<DonorsDto>()
                .setQuery(FirebaseDatabase.getInstance().getReference("DonorsDto"), DonorsDto.class)
                .build();
        adapter = new HomePageRecAdapter(options);
        recyclerView.setAdapter(adapter);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(HomePage.this, LoginPage.class);
                    startActivity(intent);
            }
        });
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
        deptNameList.add("CS");
        deptNameList.add("FT");
        deptNameList.add("EEE");
        deptNameList.add("ME");

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
        loginBtn = findViewById(R.id.homePage_loginBtn);
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
}