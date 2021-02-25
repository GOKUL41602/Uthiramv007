package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class FilteredBloodHomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private FilteredHomePageDeptAdapter adapter;
    private TextView deptNameText, bloodGroupText;
    private String bloodGroup, deptName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_blood_home_page);
        initializeViews();
        deptName = getIntent().getStringExtra("1");
        deptNameText.setText(deptName);
        bloodGroup = getIntent().getStringExtra("2");
        bloodGroupText.setText(bloodGroup);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<DonorsDto> options
                = new FirebaseRecyclerOptions.Builder<DonorsDto>()
                .setQuery(FirebaseDatabase.getInstance().getReference("DonorsDto"), DonorsDto.class)
                .build();
        adapter = new FilteredHomePageDeptAdapter(options, bloodGroup);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.filteredBloodHomePage_recView);
        relativeLayout = findViewById(R.id.filteredBloodHomePage_relLayout);
        deptNameText = findViewById(R.id.filteredBloodHomePage_deptName);
        bloodGroupText = findViewById(R.id.filteredBloodHomePage_bloodGroup);
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
        super.onBackPressed();
        Intent intent = new Intent(FilteredBloodHomePage.this, HomePage.class);
        startActivity(intent);
        FilteredBloodHomePage.this.finish();
    }
}