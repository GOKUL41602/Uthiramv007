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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

public class EmergencyRequests extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private RelativeLayout relativeLayout;

    private RecyclerView recyclerView;

    private EmergencyRequestRecAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_requests);

        initializeViews();

        drawerLayout = findViewById(R.id.design_navigation_view);

        Toolbar toolbar = findViewById(R.id.toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<RequestDonorDto> options
                = new FirebaseRecyclerOptions.Builder<RequestDonorDto>()
                .setQuery(FirebaseDatabase.getInstance().getReference("EmergencyRequests"), RequestDonorDto.class)
                .build();
        adapter = new EmergencyRequestRecAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    private void initializeViews() {
        relativeLayout = findViewById(R.id.emergencyRequests_relLayout);
        recyclerView = findViewById(R.id.emergencyRequests_recView);
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
                break;
            case R.id.filter:
                Intent intent1 = new Intent(EmergencyRequests.this, HomePage.class);
                startActivity(intent1);
                break;
            case R.id.donor_login:
                Intent intent0 = new Intent(EmergencyRequests.this, LoginPage.class);
                startActivity(intent0);
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