package com.uthiram.uthiramv007;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class EmergencyRequestRecAdapter extends FirebaseRecyclerAdapter<RequestDonorDto, EmergencyRequestRecAdapter.ViewHolder> {

    public EmergencyRequestRecAdapter(@NonNull FirebaseRecyclerOptions<RequestDonorDto> options) {
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

        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phone = holder.contactNo.getText().toString();
                intent.setData(Uri.parse("tel:" + phone));
                holder.context.startActivity(intent);
            }
        });

        holder.msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context.getApplicationContext(), SendSmsPage.class);
                intent.putExtra("phoneNo", model.getPatientPhoneNo());
                holder.context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_blood_requests_foramt, parent, false);
        return new EmergencyRequestRecAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView patientName, bloodGroup, unitsNeeded, contactNo, hospitalName, date, time;
        private ImageView callBtn, msgBtn;
        private Context context = itemView.getContext();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews();
        }

        private void initializeViews() {
            patientName = itemView.findViewById(R.id.emergencyRequestFormat_patientName);
            bloodGroup = itemView.findViewById(R.id.emergencyRequestFormat_bloodGroup);
            unitsNeeded = itemView.findViewById(R.id.emergencyRequestFormat_unitsNeeded);
            contactNo = itemView.findViewById(R.id.emergencyRequestFormat_contactNo);
            hospitalName = itemView.findViewById(R.id.emergencyRequestFormat_hospitalName);
            date = itemView.findViewById(R.id.emergencyRequestFormat_date);
            time = itemView.findViewById(R.id.emergencyRequestFormat_time);

            callBtn = itemView.findViewById(R.id.emergencyRequestFormat_callBtn);
            msgBtn = itemView.findViewById(R.id.emergencyRequestFormat_msgBtn);
        }
    }
}
