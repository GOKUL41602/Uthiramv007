package com.uthiram.uthiramv007;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewRequestRecViewAdapter extends FirebaseRecyclerAdapter<RequestDonorDto, ViewRequestRecViewAdapter.ViewHolder> {

    public String key;

    public ViewRequestRecViewAdapter(@NonNull FirebaseRecyclerOptions<RequestDonorDto> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull RequestDonorDto model) {
        holder.patientName.setText(model.getPatientName());
        holder.bloodGroup.setText(model.getBloodGroup());
        holder.unitsNeeded.setText(model.getUnitsNeeded());
        holder.hospitalName.setText(model.getHospitalName());
        holder.contactNo.setText(model.getPatientPhoneNo());
        holder.date.setText(model.getNeededWithInDate());
        holder.time.setText(model.getNeededWithInTime());
        holder.key.setText(model.getKey());
        key = model.getKey();

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context.getApplicationContext(), EditEmergencyRequest.class);
                intent.putExtra("key", holder.key.getText());
                intent.putExtra("userName", model.getUserName());
                intent.putExtra("patientName", holder.patientName.getText().toString());
                holder.context.startActivity(intent);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RequestDonorDto/" + model.getUserName());
                Query query = reference.orderByChild("key").startAt(key).endAt(key + "\uf8ff");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            reference.child(holder.key.getText().toString()).removeValue();
                            Toast.makeText(holder.context, "Request Removed Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(holder.context, "Data doesn't exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                removeDataFromDB(holder.context);
            }
        });

    }


    private void removeDataFromDB(Context context) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EmergencyRequests");
        Query query = reference.orderByChild("key").startAt(key).endAt(key + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    reference.child(key).removeValue();
                    Toast.makeText(context, "Request Removed Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Data doesn't exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_request_donors_format, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView patientName, bloodGroup, unitsNeeded, contactNo, hospitalName, date, time, key;

        private ImageView editBtn, deleteBtn;

        private Context context = itemView.getContext();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews();
        }

        private void initializeViews() {
            patientName = itemView.findViewById(R.id.viewRequestDonorFormat_patientName);
            bloodGroup = itemView.findViewById(R.id.viewRequestDonorFormat_bloodGroup);
            unitsNeeded = itemView.findViewById(R.id.viewRequestDonorFormat_unitsNeeded);
            contactNo = itemView.findViewById(R.id.viewRequestDonorFormat_contactNo);
            hospitalName = itemView.findViewById(R.id.viewRequestDonorFormat_hospitalName);
            date = itemView.findViewById(R.id.viewRequestDonorFormat_date);
            time = itemView.findViewById(R.id.viewRequestDonorFormat_time);
            editBtn = itemView.findViewById(R.id.viewRequestDonorFormat_editBtn);
            deleteBtn = itemView.findViewById(R.id.viewRequestDonorFormat_deleteBtn);
            key = itemView.findViewById(R.id.viewRequestDonorFormat_key);
        }
    }
}
