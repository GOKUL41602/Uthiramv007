package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView nextActBtn;

    private RelativeLayout relativeLayout;

    private ViewTreeObserver vto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        nextActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmergencyRequests.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        vto = relativeLayout.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    Thread.sleep(4000);
                    Intent intent = new Intent(MainActivity.this, EmergencyRequests.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initializeViews() {
        nextActBtn = findViewById(R.id.mainActivity_btn);
        relativeLayout = findViewById(R.id.main_relLayout);
    }
}