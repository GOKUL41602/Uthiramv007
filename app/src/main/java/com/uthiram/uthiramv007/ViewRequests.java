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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

public class ViewRequests extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private RecyclerView recyclerView;

    private RelativeLayout relativeLayout;

    private ViewRequestRecViewAdapter adapter;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);
        userName = getIntent().getStringExtra("userName");
        initializeViews();

        drawerLayout = findViewById(R.id.viewRequest_design_navigation_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);

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
                Intent intent3 = new Intent(ViewRequests.this, LoginPage.class);
                startActivity(intent3);
                ViewRequests.this.finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}