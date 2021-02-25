package com.uthiram.uthiramv007;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EmergencyRequestRecAdapter extends FirebaseRecyclerAdapter<RequestDonorDto, EmergencyRequestRecAdapter.ViewHolder> {


    public String currentDate, currentTime, timeFromDB, dateFromDB, date, keyFromDB;

    public boolean global = false;

    public EmergencyRequestRecAdapter(@NonNull FirebaseRecyclerOptions<RequestDonorDto> options, String currentDate, String currentTime) {
        super(options);
        this.currentDate = currentDate;
        this.currentTime = currentTime;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull RequestDonorDto model) {

        holder.date.setText(model.getNeededWithInDate());
        holder.time.setText(model.getNeededWithInTime());
        holder.key.setText(model.getKey());
        keyFromDB = holder.key.getText().toString();
        //
        timeFromDB = holder.time.getText().toString();
        dateFromDB = holder.date.getText().toString();
        //
        if (dateFromDB.length() == 10) {
            date = String.format("0%s-%s-%s", dateFromDB.substring(0, 1), dateFromDB.substring(2, 5), dateFromDB.substring(6, 10));
        } else if (dateFromDB.length() == 11) {
            date = String.format("%s-%s-%s", dateFromDB.substring(0, 2), dateFromDB.substring(3, 6), dateFromDB.substring(7, 11));
        }

        if (verifyDate()) {
            if (global) {
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
                        intent.putExtra("checkValue", "emergency");
                        intent.putExtra("userName", model.getPatientName());
                        intent.putExtra("phoneNo", model.getPatientPhoneNo());
                        holder.context.startActivity(intent);
                    }
                });
            } else {
                holder.cardView.setVisibility(View.GONE);
                holder.relativeLayout.setVisibility(View.GONE);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EmergencyRequests");
                Query query = reference.orderByChild("key").startAt(keyFromDB).endAt(keyFromDB + "\uf8ff");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            reference.child(keyFromDB).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } else {
            holder.cardView.setVisibility(View.GONE);
            holder.relativeLayout.setVisibility(View.GONE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EmergencyRequests");
            Query query = reference.orderByChild("key").startAt(keyFromDB).endAt(keyFromDB + "\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        reference.child(keyFromDB).removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    public boolean isTimeWith_in_Interval(String startTime, String endTime) {
        boolean isBetween = false;
        try {
            Date time1 = new SimpleDateFormat("h:mm a").parse(startTime);

            Date time2 = new SimpleDateFormat("h:mm a").parse(endTime);

            if (time2.after(time1)) {
                isBetween = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }


    public boolean isDate(String startDate, String endDate) {
        boolean isBetween = false;
        try {
            Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(startDate);

            Date date2 = new SimpleDateFormat("dd-MMM-yyyy").parse(endDate);

            if (date2.after(date1)) {
                isBetween = true;
                global = true;
            } else {
                if (date2.equals(date1)) {
                    isBetween = true;
                    if (verifyTime(isBetween)) {
                        global = true;
                    } else {
                        global = false;
                    }

                } else {
                    isBetween = false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }

    private boolean verifyTime(boolean isBetween) {
        boolean bool = false;
        if (isBetween) {
            if (isTimeWith_in_Interval(currentTime, timeFromDB)) {
                bool = true;
            } else {
                bool = false;
            }
        } else {
        }
        return bool;
    }

    private boolean verifyDate() {
        if (isDate(currentDate, date)) {

            return true;
        } else {

            return false;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_blood_requests_foramt, parent, false);
        return new EmergencyRequestRecAdapter.ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView patientName, bloodGroup, unitsNeeded, contactNo, hospitalName, date, time, key;
        private ImageView callBtn, msgBtn;
        private Context context = itemView.getContext();
        private CardView cardView;
        private RelativeLayout relativeLayout, relativeLayout1;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews();
        }

        private void initializeViews() {
            key = itemView.findViewById(R.id.emergencyRequestFormat_key);
            patientName = itemView.findViewById(R.id.emergencyRequestFormat_patientName);
            bloodGroup = itemView.findViewById(R.id.emergencyRequestFormat_bloodGroup);
            unitsNeeded = itemView.findViewById(R.id.emergencyRequestFormat_unitsNeeded);
            contactNo = itemView.findViewById(R.id.emergencyRequestFormat_contactNo);
            hospitalName = itemView.findViewById(R.id.emergencyRequestFormat_hospitalName);
            date = itemView.findViewById(R.id.emergencyRequestFormat_date);
            time = itemView.findViewById(R.id.emergencyRequestFormat_time);
            cardView = itemView.findViewById(R.id.emergencyRequestFormat_cardView);
            relativeLayout = itemView.findViewById(R.id.emergencyRequestFormat_dataPresent);
            relativeLayout1 = itemView.findViewById(R.id.emergencyRequestFormat_dataNull);

            callBtn = itemView.findViewById(R.id.emergencyRequestFormat_callBtn);
            msgBtn = itemView.findViewById(R.id.emergencyRequestFormat_msgBtn);
        }

    }

}
