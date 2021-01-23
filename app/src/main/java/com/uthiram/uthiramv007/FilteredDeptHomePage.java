package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_dept_home_page);

        initializeViews();
        deptName = getIntent().getStringExtra("1");
        deptNameText.setText(deptName);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<DonorsDto> options
                = new FirebaseRecyclerOptions.Builder<DonorsDto>()
                .setQuery(FirebaseDatabase.getInstance().getReference("DonorsDto"), DonorsDto.class)
                .build();
        adapter = new FilteredHomePageBloodGroupAdapter(options, deptName);
        recyclerView.setAdapter(adapter);
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.filteredDeptHomePage_recView);
        relativeLayout = findViewById(R.id.filteredDeptHomePage_relLayout);
        deptNameText = findViewById(R.id.filteredDeptHomePage_deptName);
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
}