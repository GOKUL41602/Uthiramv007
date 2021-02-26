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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class HomePageRecAdapter extends FirebaseRecyclerAdapter<DonorsDto, HomePageRecAdapter.ViewHolder> {


    private Context context;

    public HomePageRecAdapter(@NonNull FirebaseRecyclerOptions<DonorsDto> options, Context context) {

        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull DonorsDto donorsDto) {

        if (donorsDto.getStatus().equals("Available")) {
            holder.donorName.setText(donorsDto.getDonorName());
            holder.phoneNo.setText(donorsDto.getPhoneNo());
            holder.deptName.setText(donorsDto.getDeptName());
            holder.bloodGroup.setText(donorsDto.getBloodGroup());
            holder.place.setText(donorsDto.getAddress());

            holder.callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = holder.phoneNo.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
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
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("checkValue", "filter");
                    intent.putExtra("userName", donorsDto.getDonorName());
                    intent.putExtra("phoneNo", donorsDto.getPhoneNo());
                    holder.context.startActivity(intent);
                }
            });
        } else {
            holder.relativeLayout.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.GONE);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donor_display_format, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView donorName, place, deptName, bloodGroup, phoneNo;
        private ImageView callBtn, msgBtn;
        private Context context = itemView.getContext();
        private RelativeLayout relativeLayout;
        private CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews();
        }

        private void initializeViews() {
            donorName = itemView.findViewById(R.id.donorDisplayFormat_donorName);
            place = itemView.findViewById(R.id.donorDisplayFormat_place);
            deptName = itemView.findViewById(R.id.donorDisplayFormat_deptName);
            bloodGroup = itemView.findViewById(R.id.donorDisplayFormat_bloodGroup);
            phoneNo = itemView.findViewById(R.id.donorDisplayFormat_phoneNo);
            cardView = itemView.findViewById(R.id.donorDisplayFormat_card);
            relativeLayout = itemView.findViewById(R.id.donorDisplayFormat_relLayout);
            callBtn = itemView.findViewById(R.id.donorDisplayFormat_callBtn);
            msgBtn = itemView.findViewById(R.id.donorDisplayFormat_msgBtn);
        }


    }
}
