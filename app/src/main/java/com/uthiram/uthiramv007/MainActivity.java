package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView nextActBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        nextActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }

    private void initializeViews() {
        nextActBtn = findViewById(R.id.mainActivity_btn);
    }
}