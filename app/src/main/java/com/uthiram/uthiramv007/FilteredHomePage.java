package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

public class FilteredHomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private FilteredHomePageRecAdapter adapter;
    private TextView deptNameText, bloodGroupText,emptyText;
    private String deptName, bloodGroup;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_home_page);
        initializeViews();
        deptName = getIntent().getStringExtra("1");
        bloodGroup = getIntent().getStringExtra("2");
        deptNameText.setText(deptName);
        bloodGroupText.setText(bloodGroup);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<DonorsDto> options
                = new FirebaseRecyclerOptions.Builder<DonorsDto>()
                .setQuery(FirebaseDatabase.getInstance().getReference("DonorsDto"), DonorsDto.class)
                .build();
        adapter = new FilteredHomePageRecAdapter(options, bloodGroup, deptName,progressBar);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.filteredHomePage_recView);
        relativeLayout = findViewById(R.id.filteredHomePage_relLayout);
        deptNameText = findViewById(R.id.filteredHomePage_deptName);
        bloodGroupText = findViewById(R.id.filteredHomePage_bloodGroup);
        progressBar = findViewById(R.id.filteredHomePage_progressBar);
        emptyText=findViewById(R.id.filteredHomePage_emptyText);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        FilteredHomePage.this.finish();
    }
}