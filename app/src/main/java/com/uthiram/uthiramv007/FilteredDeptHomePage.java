package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class FilteredDeptHomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private FilteredHomePageBloodGroupAdapter adapter;
    private TextView deptNameText, bloodGroupText;
    private String deptName;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_dept_home_page);

        initializeViews();
        deptName = getIntent().getStringExtra("1");
        deptNameText.setText(deptName);

        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<DonorsDto> options
                = new FirebaseRecyclerOptions.Builder<DonorsDto>()
                .setQuery(FirebaseDatabase.getInstance().getReference("DonorsDto"), DonorsDto.class)
                .build();
        adapter = new FilteredHomePageBloodGroupAdapter(options, deptName,progressBar);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.filteredDeptHomePage_recView);
        relativeLayout = findViewById(R.id.filteredDeptHomePage_relLayout);
        deptNameText = findViewById(R.id.filteredDeptHomePage_deptName);
        progressBar=findViewById(R.id.filteredDeptHomePage_progressBar);
        bloodGroupText = findViewById(R.id.filteredDeptHomePage_bloodGroup);
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
        Intent intent = new Intent(FilteredDeptHomePage.this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        FilteredDeptHomePage.this.finish();
    }
}