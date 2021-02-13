package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class EmergencyRequests extends AppCompatActivity {

    private RelativeLayout relativeLayout;

    private RecyclerView recyclerView;

    private EmergencyRequestRecAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_requests);

        initializeViews();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<RequestDonorDto> options
                = new FirebaseRecyclerOptions.Builder<RequestDonorDto>()
                .setQuery(FirebaseDatabase.getInstance().getReference("EmergencyRequests"), RequestDonorDto.class)
                .build();
        adapter = new EmergencyRequestRecAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    private void initializeViews()
    {
        relativeLayout=findViewById(R.id.emergencyRequests_relLayout);
        recyclerView=findViewById(R.id.emergencyRequests_recView);
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