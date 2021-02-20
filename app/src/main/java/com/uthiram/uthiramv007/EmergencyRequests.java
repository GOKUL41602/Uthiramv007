package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

public class EmergencyRequests extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private RelativeLayout relativeLayout, relativeLayout1;

    private RecyclerView recyclerView;

    private EmergencyRequestRecAdapter adapter;

    private FloatingActionButton filterBtn, createReqBtn, emptyCreateReqBtn;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_requests);

        isNetworkConnected();
        initializeViews();
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("EmergencyRequests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long count = snapshot.getChildrenCount();
                if (count != 0) {
                    progressBar.setVisibility(View.GONE);
                    relativeLayout1.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);

                } else {
                    progressBar.setVisibility(View.GONE);
                    relativeLayout1.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(EmergencyRequests.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }

        });


        if (!isNetworkConnected()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Check Internet Connection")
                    .setMessage("Please Check Internet Connection ! ")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        } else {
            drawerLayout = findViewById(R.id.emergencyRequest_design_navigation_view);

            Toolbar toolbar = findViewById(R.id.toolbar);

            NavigationView navigationView = findViewById(R.id.nav_view);

            navigationView.setNavigationItemSelectedListener(this);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

            drawerLayout.addDrawerListener(toggle);

            toggle.syncState();

        }


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EmergencyRequests");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<RequestDonorDto> options
                = new FirebaseRecyclerOptions.Builder<RequestDonorDto>()
                .setQuery(reference, RequestDonorDto.class)
                .build();
        adapter = new EmergencyRequestRecAdapter(options);
        recyclerView.setAdapter(adapter);


        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyRequests.this, HomePage.class);
                startActivity(intent);
                EmergencyRequests.this.finish();
            }
        });

        createReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EmergencyRequests.this, "Login to create Request", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EmergencyRequests.this, LoginPage.class);
                startActivity(intent);
                EmergencyRequests.this.finish();
            }
        });
        emptyCreateReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EmergencyRequests.this, "Login to create Request", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EmergencyRequests.this, LoginPage.class);
                startActivity(intent);
                EmergencyRequests.this.finish();
            }
        });
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.emergencyRequests_progressBar);
        relativeLayout = findViewById(R.id.emergencyRequests_relLayout);
        recyclerView = findViewById(R.id.emergencyRequests_recView);
        relativeLayout1 = findViewById(R.id.emergencyRequestFormat_dataNull);
        filterBtn = findViewById(R.id.emergencyRequestFormat_emptyFloatingFilterBtn);
        createReqBtn = findViewById(R.id.emergencyRequests_createReqBtn);
        emptyCreateReqBtn = findViewById(R.id.emergencyRequests_empty_createReqBtn);

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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                Intent intent = new Intent(EmergencyRequests.this, EmergencyRequests.class);
                startActivity(intent);
                EmergencyRequests.this.finish();
                break;
            case R.id.filter:
                Intent intent1 = new Intent(EmergencyRequests.this, HomePage.class);
                startActivity(intent1);
                EmergencyRequests.this.finish();
                break;
            case R.id.donor_login:
                Intent intent0 = new Intent(EmergencyRequests.this, LoginPage.class);
                startActivity(intent0);
                EmergencyRequests.this.finish();
                break;
            case R.id.about_us:
                Toast.makeText(this, "About Us Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact_us:
                Toast.makeText(this, "Contact Us Selected", Toast.LENGTH_SHORT).show();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}