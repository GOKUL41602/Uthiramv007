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

public class FilteredHomePageRecAdapter extends FirebaseRecyclerAdapter<DonorsDto, FilteredHomePageRecAdapter.ViewHolder> {

    private String bloodGroup, deptName;

    public ProgressBar progressBar;


    public FilteredHomePageRecAdapter(@NonNull FirebaseRecyclerOptions<DonorsDto> options, String bloodGroup, String deptName, ProgressBar progressBar) {
        super(options);
        this.bloodGroup = bloodGroup;
        this.deptName = deptName;
        this.progressBar = progressBar;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull DonorsDto model) {
        if (model.getBloodGroup().equals(bloodGroup)) {
            if (model.getDeptName().equals(deptName)) {
                if (model.getStatus().equals("Available")) {
                    progressBar.setVisibility(View.GONE);
                    holder.donorName.setText(model.getDonorName());
                    holder.place.setText(model.getAddress());
                    holder.phoneNo.setText(model.getPhoneNo());
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
                            intent.putExtra("checkValue", "bloodDept");
                            intent.putExtra("bloodGroup", bloodGroup);
                            intent.putExtra("deptName", deptName);
                            intent.putExtra("userName", model.getDonorName());
                            intent.putExtra("phoneNo", model.getPhoneNo());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
        } else {
            progressBar.setVisibility(View.GONE);
            holder.relativeLayout.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filtered_donor_display_format, parent, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView donorName, phoneNo, place;
        private ImageView callBtn, msgBtn;
        private Context context = itemView.getContext();
        private RelativeLayout relativeLayout;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews();
        }

        private void initializeViews() {
            donorName = itemView.findViewById(R.id.filteredDonorDisplayFormat_donorName);
            phoneNo = itemView.findViewById(R.id.filteredDonorDisplayFormat_phoneNo);
            place = itemView.findViewById(R.id.filteredDonorDisplayFormat_place);
            relativeLayout = itemView.findViewById(R.id.filteredDonorDisplayFormat_relLayout);
            callBtn = itemView.findViewById(R.id.filteredDonorDisplayFormat_callBtn);
            msgBtn = itemView.findViewById(R.id.filteredDonorDisplayFormat_msgBtn);
            cardView = itemView.findViewById(R.id.filteredDonorDisplayFormat_cardView);
        }
    }
}
