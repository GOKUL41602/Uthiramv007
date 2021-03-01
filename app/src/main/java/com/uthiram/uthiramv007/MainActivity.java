package com.uthiram.uthiramv007;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * MainActivity of the <b>UTHIRAM</b> Application<br>
 * <p>it contains the splash screen animation of the application.</p>
 */
public class MainActivity extends AppCompatActivity {

    // declaring all used widgets.

    private TextView nextActBtn;

    private RelativeLayout relativeLayout;

    /**
     * MainActivity is redirected to EmergencyRequests Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initializeViews method is called.

        initializeViews();

        // Handler class is initialized to delay this activity for 2 seconds and to be redirected to EmergencyRequests Activity.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent class is used to redirect to another activity.

                Intent intent = new Intent(MainActivity.this, EmergencyRequests.class);
                startActivity(intent);

                //finish() method is called to finish this activity one redirected to another activity.

                MainActivity.this.finish();
            }
        }, 2000);
    }

    /**
     * Initialization of  all widgets used in this activity are defined by using the <b>findViewById()</b> method.
     */
    private void initializeViews() {
        nextActBtn = findViewById(R.id.mainActivity_btn);
        relativeLayout = findViewById(R.id.main_relLayout);
    }
}