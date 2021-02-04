package com.uthiram.uthiramv007;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
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

    private Button sendBtn, cancelBtn;

    private String messageText, phoneNo;

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms_page);
        initializeViews();
        phoneNo = getIntent().getStringExtra("phoneNo");


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeStrings();
                if (validateMessage()) {
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                    {
                        if(checkSelfPermission(Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED)
                        {

                            sendSms();
                        }
                        else
                        {
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                        }
                    }
                }
            }
        });
    }


    private void sendSms() {
        try {
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo,null,messageText,null,null);
            Toast.makeText(this, "Message Sent to "+phoneNo, Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(SendSmsPage.this,"Failed to send",Toast.LENGTH_SHORT).show();
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
        cancelBtn = findViewById(R.id.sendSmsPage_cancelBtn);
        relativeLayout = findViewById(R.id.sendSmsPage_relLayout);
    }


}