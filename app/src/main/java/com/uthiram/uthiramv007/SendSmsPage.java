package com.uthiram.uthiramv007;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

public class SendSmsPage extends AppCompatActivity {

    private TextInputLayout message;

    private Button sendBtn;

    private String messageText, phoneNo, userName, checkValue = null, bloodGroup = "Blood Group", deptName = "Dept Name";

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms_page);
        initializeViews();
        bloodGroup = getIntent().getStringExtra("bloodGroup");
        deptName = getIntent().getStringExtra("deptName");
        checkValue = getIntent().getStringExtra("checkValue");
        userName = getIntent().getStringExtra("userName");
        phoneNo = getIntent().getStringExtra("phoneNo");


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeStrings();
                if (validateMessage()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                            sendSms();
                        } else {
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                            Log.d("demo", "demo");
                        }
                    }
                }
            }
        });
    }

    private void sendSms() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, messageText, null, null);
            Toast.makeText(this, "Message Sent to  " + userName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SendSmsPage.this, "Failed to send", Toast.LENGTH_SHORT).show();
        }


    }

    private void initializeStrings() {
        messageText = message.getEditText().getText().toString().trim();
    }

    private boolean validateMessage() {
        if (messageText.isEmpty()) {
            message.setError("Please Enter Text");
            message.requestFocus();
            return false;
        } else {
            message.setError(null);
            message.setErrorEnabled(false);
            return true;
        }
    }

    private void initializeViews() {
        message = findViewById(R.id.sendSmsPage_message);
        sendBtn = findViewById(R.id.sendSmsPage_sendBtn);

        relativeLayout = findViewById(R.id.sendSmsPage_relLayout);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (checkValue.equals("dept")) {
            Intent intent = new Intent(SendSmsPage.this, FilteredDeptHomePage.class);
            intent.putExtra("1", deptName);
            intent.putExtra("2", bloodGroup);
            startActivity(intent);
            SendSmsPage.this.finish();
        } else if (checkValue.equals("blood")) {
            Intent intent = new Intent(SendSmsPage.this, FilteredBloodHomePage.class);
            intent.putExtra("1", deptName);
            intent.putExtra("2", bloodGroup);
            startActivity(intent);
            SendSmsPage.this.finish();
        } else if (checkValue.equals("bloodDept")) {
            Intent intent = new Intent(SendSmsPage.this, FilteredHomePage.class);
            intent.putExtra("1", deptName);
            intent.putExtra("2", bloodGroup);
            startActivity(intent);
            SendSmsPage.this.finish();
        } else if (checkValue.equals("emergency")) {
            Intent intent = new Intent(SendSmsPage.this, EmergencyRequests.class);
            startActivity(intent);
            SendSmsPage.this.finish();
        } else if (checkValue.equals("filter")) {
            Intent intent = new Intent(SendSmsPage.this, HomePage.class);
            startActivity(intent);
            SendSmsPage.this.finish();
        }

    }
}