package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DonorHomePage extends AppCompatActivity {

    private TextView title;

    private String userName;

    private Button viewProfileBtn, updateProfileBtn, updateStatusBtn, bacKBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_home_page);
        initializeViews();
        userName = getIntent().getStringExtra("userName");
        title.setText("Welcome " + userName);

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
            }
        });
        bacKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonorHomePage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initializeViews() {
        title = findViewById(R.id.donorHomePage_title);
        viewProfileBtn = findViewById(R.id.donorHomePage_viewProfileBtn);
        updateProfileBtn = findViewById(R.id.donorHomePage_updateProfileBtn);
        updateStatusBtn = findViewById(R.id.donorHomePage_updateStatusBtn);
        bacKBtn = findViewById(R.id.donorHomePage_backBtn);
    }

}