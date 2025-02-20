package com.uthiram.uthiramv007;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class FilteredHomePageDeptAdapter extends FirebaseRecyclerAdapter<DonorsDto, FilteredHomePageDeptAdapter.ViewHolder> {

    private String bloodGroup;
    ProgressBar progressBar;
    int count = 0;

    public FilteredHomePageDeptAdapter(@NonNull FirebaseRecyclerOptions<DonorsDto> options, String bloodGroup, ProgressBar progressBar) {
        super(options);
        this.bloodGroup = bloodGroup;
        this.progressBar = progressBar;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull DonorsDto model) {
        if (model.getBloodGroup().equals(bloodGroup)) {

            if (model.getStatus().equals("Available")) {
                count++;
                holder.emptyRelLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                holder.donorName.setText(model.getDonorName());
                holder.place.setText(model.getAddress());
                holder.phoneNo.setText(model.getPhoneNo());
                holder.deptName.setText(model.getDeptName());
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
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("checkValue", "blood");
                        intent.putExtra("deptName", "Dept Name");
                        intent.putExtra("bloodGroup", bloodGroup);
                        intent.putExtra("userName", model.getDonorName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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

//        if (count != 0) {
//            holder.emptyRelLayout.setVisibility(View.GONE);
//        } else {
//            holder.emptyRelLayout.setVisibility(View.VISIBLE);
//        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filtered_deptname_format, parent, false);
        return new FilteredHomePageDeptAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView donorName, phoneNo, place, deptName;
        private ImageView callBtn, msgBtn;
        private Context context = itemView.getContext();
        private RelativeLayout relativeLayout, emptyRelLayout;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews();
        }

        private void initializeViews() {
            donorName = itemView.findViewById(R.id.filteredDeptDonorDisplayFormat_donorName);
            phoneNo = itemView.findViewById(R.id.filteredDeptDonorDisplayFormat_phoneNo);
            place = itemView.findViewById(R.id.filteredDeptDonorDisplayFormat_place);
            deptName = itemView.findViewById(R.id.filteredDeptDonorDisplayFormat_dept);
            relativeLayout = itemView.findViewById(R.id.filteredDeptDonorDisplayFormat_relLayout);
            callBtn = itemView.findViewById(R.id.filteredDeptDonorDisplayFormat_callBtn);
            msgBtn = itemView.findViewById(R.id.filteredDeptDonorDisplayFormat_msgBtn);
            cardView = itemView.findViewById(R.id.filteredDeptDonorDisplayFormat_cardView);
            emptyRelLayout = itemView.findViewById(R.id.filteredDeptDonorDisplayFormat_emptyRelLayout);
        }
    }
}
