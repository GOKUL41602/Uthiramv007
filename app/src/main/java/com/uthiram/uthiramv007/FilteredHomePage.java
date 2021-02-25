package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

public class FilteredHomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private FilteredHomePageRecAdapter adapter;
    private TextView deptNameText, bloodGroupText;
    private String deptName, bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_home_page);
        initializeViews();
        deptName = getIntent().getStringExtra("1");
        bloodGroup = getIntent().getStringExtra("2");
        deptNameText.setText(deptName);
        bloodGroupText.setText(bloodGroup);
        Log.d("Demo", "Demo");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<DonorsDto> options
                = new FirebaseRecyclerOptions.Builder<DonorsDto>()
                .setQuery(FirebaseDatabase.getInstance().getReference("DonorsDto"), DonorsDto.class)
                .build();
        adapter = new FilteredHomePageRecAdapter(options, bloodGroup, deptName);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.filteredHomePage_recView);
        relativeLayout = findViewById(R.id.filteredHomePage_relLayout);
        deptNameText = findViewById(R.id.filteredHomePage_deptName);
        bloodGroupText = findViewById(R.id.filteredHomePage_bloodGroup);
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
        Intent intent = new Intent(FilteredHomePage.this, HomePage.class);
        startActivity(intent);
        FilteredHomePage.this.finish();
    }
}