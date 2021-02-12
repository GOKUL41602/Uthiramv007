package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ViewRequests extends AppCompatActivity {


    private RecyclerView recyclerView;

    private RelativeLayout relativeLayout;

    private ViewRequestRecViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);
        initializeViews();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<RequestDonorDto> options
                = new FirebaseRecyclerOptions.Builder<RequestDonorDto>()
                .setQuery(FirebaseDatabase.getInstance().getReference("RequestDonorDto"), RequestDonorDto.class)
                .build();
        adapter = new ViewRequestRecViewAdapter(options);
        recyclerView.setAdapter(adapter);
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
}