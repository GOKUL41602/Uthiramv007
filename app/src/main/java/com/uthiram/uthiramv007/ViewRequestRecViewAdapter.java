package com.uthiram.uthiramv007;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ViewRequestRecViewAdapter extends FirebaseRecyclerAdapter<RequestDonorDto, ViewRequestRecViewAdapter.ViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
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

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_request_donors_format, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView patientName, bloodGroup, unitsNeeded, contactNo, hospitalName, date, time;

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
        }
    }
}
