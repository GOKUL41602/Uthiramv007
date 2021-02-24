package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;

import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

public class ViewRequests extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private RecyclerView recyclerView;

    private RelativeLayout relativeLayout, emptyRelativeLayout, relativeLayout1;

    private ProgressBar progressBar;

    private ViewRequestRecViewAdapter adapter;

    private String userName;

    public String loginPath = "null";

    private String rollNoPath = "null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);
        userName = getIntent().getStringExtra("userName");
        initializeViews();

        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("RequestDonorDto/" + userName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long count = snapshot.getChildrenCount();
                if (count != 0) {

                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    emptyRelativeLayout.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);
                    emptyRelativeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewRequests.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        drawerLayout = findViewById(R.id.viewRequest_design_navigation_view);

        Toolbar toolbar = findViewById(R.id.viewRequest_toolbar);

        NavigationView navigationView = findViewById(R.id.viewRequest_nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<RequestDonorDto> options
                = new FirebaseRecyclerOptions.Builder<RequestDonorDto>()
                .setQuery(FirebaseDatabase.getInstance().getReference("RequestDonorDto/" + userName), RequestDonorDto.class)
                .build();
        adapter = new ViewRequestRecViewAdapter(options);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);


    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.viewRequests_recView);
        relativeLayout = findViewById(R.id.viewRequests_relLayout);
        emptyRelativeLayout = findViewById(R.id.viewRequestEmptyRelLayout);
        progressBar = findViewById(R.id.viewRequestProgressBar);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public void onStop() {
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
        Intent intent = new Intent(ViewRequests.this, EmergencyRequests.class);
        startActivity(intent);
        ViewRequests.this.finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.donor_home:
                Intent intent0 = new Intent(ViewRequests.this, RequestBloodDonor.class);
                intent0.putExtra("userName", userName);
                startActivity(intent0);
                ViewRequests.this.finish();
                break;
            case R.id.view_requests:
                Intent intent = new Intent(ViewRequests.this, ViewRequests.class);
                intent.putExtra("userName", userName);
                ViewRequests.this.finish();
                startActivity(intent);
                break;
            case R.id.edit_donor_profile:
                Intent intent1 = new Intent(ViewRequests.this, UpdateDonorDetailsPage.class);
                intent1.putExtra("userName", userName);
                startActivity(intent1);
                ViewRequests.this.finish();
                break;
            case R.id.edit_donor_status:
                Intent intent2 = new Intent(ViewRequests.this, UpdateDonorStatusPage.class);
                intent2.putExtra("userName", userName);
                startActivity(intent2);
                ViewRequests.this.finish();
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
                Intent intent3 = new Intent(ViewRequests.this, EmergencyRequests.class);
                startActivity(intent3);
                ViewRequests.this.finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}