package com.uthiram.uthiramv007;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FilteredHomePageBloodGroupAdapter extends FirebaseRecyclerAdapter<DonorsDto, FilteredHomePageBloodGroupAdapter.ViewHolder> {

    String deptName;
    ProgressBar progressBar;

    public FilteredHomePageBloodGroupAdapter(@NonNull FirebaseRecyclerOptions<DonorsDto> options, String deptName,ProgressBar progressBar) {
        super(options);
        this.deptName = deptName;
        this.progressBar=progressBar;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull DonorsDto model) {

        if (model.getDeptName().equals(deptName)) {

            if (model.getStatus().equals("Available")) {
                progressBar.setVisibility(View.GONE);
                holder.donorName.setText(model.getDonorName());
                holder.place.setText(model.getAddress());
                holder.phoneNo.setText(model.getPhoneNo());
                holder.bloodGroup.setText(model.getBloodGroup());
                holder.callBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        String phone = holder.phoneNo.getText().toString();
                        intent.setData(Uri.parse("tel:" + phone));
                        holder.context.startActivity(intent);

                    }
                });
                holder.msgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(holder.context.getApplicationContext(), SendSmsPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("checkValue", "dept");
                        intent.putExtra("deptName", deptName);
                        intent.putExtra("bloodGroup", "Blood Group ");
                        intent.putExtra("userName", model.getDonorName());
                        intent.putExtra("phoneNo", model.getPhoneNo());
                        holder.context.startActivity(intent);
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                holder.relativeLayout.setVisibility(View.GONE);
                holder.cardView.setVisibility(View.GONE);
            }

        } else {
            progressBar.setVisibility(View.GONE);
            holder.relativeLayout.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filtered_bloodgroup_format, parent, false);
        return new FilteredHomePageBloodGroupAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView donorName, phoneNo, place, bloodGroup;
        private ImageView callBtn, msgBtn;
        private Context context = itemView.getContext();
        private RelativeLayout relativeLayout;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews();
        }

        private void initializeViews() {
            donorName = itemView.findViewById(R.id.filteredBloodDonorDisplayFormat_donorName);
            phoneNo = itemView.findViewById(R.id.filteredBloodDonorDisplayFormat_phoneNo);
            place = itemView.findViewById(R.id.filteredBloodDonorDisplayFormat_place);
            bloodGroup = itemView.findViewById(R.id.filteredBloodDonorDisplayFormat_blood);
            relativeLayout = itemView.findViewById(R.id.filteredBloodDonorDisplayFormat_relLayout);
            callBtn = itemView.findViewById(R.id.filteredBloodDonorDisplayFormat_callBtn);
            msgBtn = itemView.findViewById(R.id.filteredBloodDonorDisplayFormat_msgBtn);
            cardView = itemView.findViewById(R.id.filteredBloodDonorDisplayFormat_cardView);
        }
    }
}
