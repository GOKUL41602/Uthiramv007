package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

public class DonorHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private TextView title;

    private String userName;

    private Button viewProfileBtn, updateProfileBtn, updateStatusBtn, bacKBtn, logOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_home_page);
        initializeViews();

        userName = getIntent().getStringExtra("userName");

        title.setText("Welcome " + userName);

        drawerLayout = findViewById(R.id.donorHomePage_design_navigation_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();


        viewProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonorHomePage.this, ViewDonorProfile.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonorHomePage.this, UpdateDonorDetailsPage.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

        updateStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonorHomePage.this, UpdateDonorStatusPage.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
                Log.d("Demo", "Demo");
            }
        });

        bacKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DonorHomePage.this, HomePage.class);
                startActivity(intent);
                DonorHomePage.this.finish();
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonorHomePage.this, LoginPage.class);
                startActivity(intent);
                DonorHomePage.this.finish();
            }
        });
    }

    private void initializeViews() {
        title = findViewById(R.id.donorHomePage_title);
        viewProfileBtn = findViewById(R.id.donorHomePage_viewProfileBtn);
        updateProfileBtn = findViewById(R.id.donorHomePage_updateProfileBtn);
        updateStatusBtn = findViewById(R.id.donorHomePage_updateStatusBtn);
        bacKBtn = findViewById(R.id.donorHomePage_backBtn);
        logOutBtn = findViewById(R.id.donorHomePage_logOutBtn);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_donor_profile:Intent intent1 = new Intent(DonorHomePage.this, UpdateDonorDetailsPage.class);
                intent1.putExtra("userName", userName);
                startActivity(intent1);
                break;
            case R.id.edit_donor_status:
                Intent intent2 = new Intent(DonorHomePage.this, UpdateDonorStatusPage.class);
                intent2.putExtra("userName", userName);
                startActivity(intent2);
                break;
            case R.id.donor_logout:
                Intent intent3 = new Intent(DonorHomePage.this, LoginPage.class);
                startActivity(intent3);
                DonorHomePage.this.finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}