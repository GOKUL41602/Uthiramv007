package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Process.myPid;
import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

/**
 * <ol>
 *     <li>Home page of <b>UTHIRAM</b> is EmergencyRequests Activity</li>
 *     <li>EmergencyRequest Activity displays all the emergency requests created by the user</li>
 *     <li>fetches the information from the emergencyRequestsRecAdapter Class.</li>
 *     <li>If no requests are created it displays <b>No Records to display.</b>
 *     </li>
 * </ol>
 */
public class EmergencyRequests extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // declaring all the used widgets and dataTypes in this activity.

    private DrawerLayout drawerLayout;

    private RelativeLayout relativeLayout, relativeLayout1;

    private RecyclerView recyclerView;

    private EmergencyRequestRecAdapter adapter;

    private FloatingActionButton filterBtn, createReqBtn, emptyCreateReqBtn;

    private ProgressBar progressBar;

    private String loginPath = "null", currentDate, currentTime;

    private String rollNoPath = "null";

    private String rollNo = "";

    private char[] j = new char[1];

    private char r;

    /**
     * This OnCreate method
     * <ol>
     *     <li>Checks for internet connectivity available for the user (client)</li>
     *     <li>Retrieves the created requests by the registered users from the firebase database using FirebaseRecyclerOptions Class</li>
     *     <li>Whenever <b>Login button</b> or <b>plus icon</b> is pressed loginCredentials are checked to enable sharedPreferences(Auto Login)</li>
     *     <li>Whenever this activity is opened,current date and time  will be get from the user's device and removes the expired request's in the firebase database<b>EmergencyRequests</b> collection </li>
     * </ol>
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_requests);

        // isNetworkConnected function is called
        isNetworkConnected();
        //initializeViews() function is called
        initializeViews();

        //created two strings for retrieving both loginCredential text and rollNo path for sharedPreferences.
        loginPath = getExternalFilesDir("text").getAbsolutePath() + "/loginCredentials.txt";

        rollNoPath = getExternalFilesDir("text").getAbsolutePath() + "/rollNo.txt";

        //created two String variables to store current date and time.
        currentDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

        //using FileReader class, reads the contents of loginCredential text file and stores it in an character array(j).
        FileReader fr = null;
        try {
            fr = new FileReader(loginPath);
            int i;
            while ((i = fr.read()) != -1)
                j[0] = (char) i;
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //using FileReader class, reads the contents of rollNo text file and stores it in an String(rollNo).
        File file = new File(rollNoPath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String st;
            while ((st = br.readLine()) != null)
                rollNo = st;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //before loading the details from the firebase database progressbar visibility is set to visible.
        progressBar.setVisibility(View.VISIBLE);
        //Using FirebaseDatabase class, EmergencyRequests collection instance is initialized.
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("EmergencyRequests");
        //using addValueEventListener function, gets the total count in the EmergencyRequests Collection and stores in the local variable count.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long count = snapshot.getChildrenCount();
                if (count != 0) {
                    //if count is not zero, set the visibility of progressbar and relativeLayout1(emptyRelativeLayout)is gone and visibility of relativeLayout(if request is available) is set to visible.
                    progressBar.setVisibility(View.GONE);
                    relativeLayout1.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    //vice-versa
                    progressBar.setVisibility(View.GONE);
                    relativeLayout1.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                }
            }

            //If there is problem while retrieving data from the database this method will be executed.
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmergencyRequests.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        //1.
        if (!isNetworkConnected()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Check Internet Connection")
                    .setMessage("Please Check Internet Connection ! ")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        } else {
            // Navigation Drawer is initialized and implemented.
            drawerLayout = findViewById(R.id.emergencyRequest_design_navigation_view);

            Toolbar toolbar = findViewById(R.id.emergencyRequest_toolbar);

            NavigationView navigationView = findViewById(R.id.emergencyRequest_nav_view);

            navigationView.setNavigationItemSelectedListener(this);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

            drawerLayout.addDrawerListener(toggle);

            toggle.syncState();

        }

        //2. using FirebaseRecyclerOptions class, requests in the EmergencyRequests Collection will be displayed in the recyclerView.
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EmergencyRequests");
        //setLayoutManage() is used to display the list of data from database in the recycler view.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<RequestDonorDto> options
                = new FirebaseRecyclerOptions.Builder<RequestDonorDto>()
                .setQuery(reference, RequestDonorDto.class)
                .build();
        //passing the options instance to EmergencyRequestRecAdapter Class.
        adapter = new EmergencyRequestRecAdapter(options, currentDate, currentTime);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);//to enable smooth scrolling.


        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyRequests.this, HomePage.class);
                startActivity(intent);
                EmergencyRequests.this.finish();
            }
        });

        //3.
        createReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loginCredential is checked for Auto-login
                if (checkLoginCredential()) {
                    Intent intent = new Intent(EmergencyRequests.this, LoginPage.class);
                    intent.putExtra("rollNo", rollNo);
                    startActivity(intent);
                    EmergencyRequests.this.finish();
                } else {

                    Toast.makeText(EmergencyRequests.this, "Login to create Request", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EmergencyRequests.this, LoginPage.class);
                    rollNo = "123456";
                    intent.putExtra("rollNo", rollNo);
                    startActivity(intent);
                    EmergencyRequests.this.finish();
                }
            }
        });
        //3.
        emptyCreateReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loginCredential is checked for Auto-login
                if (checkLoginCredential()) {
                    Intent intent = new Intent(EmergencyRequests.this, LoginPage.class);
                    intent.putExtra("rollNo", rollNo);
                    startActivity(intent);
                    EmergencyRequests.this.finish();
                } else {
                    Toast.makeText(EmergencyRequests.this, "Login to create Request", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EmergencyRequests.this, LoginPage.class);
                    rollNo = "123456";
                    intent.putExtra("rollNo", rollNo);
                    startActivity(intent);
                    EmergencyRequests.this.finish();
                }
            }
        });
    }

    /**
     * to check for internet availability.
     */
    private boolean isNetworkConnected() {
        //ConnectivityManager is used to get the network availability.
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    /**
     * Initialization of  all widgets used in this activity are defined by using the <b>findViewById()</b> method.
     */
    private void initializeViews() {
        progressBar = findViewById(R.id.emergencyRequests_progressBar);
        relativeLayout = findViewById(R.id.emergencyRequests_relLayout);
        recyclerView = findViewById(R.id.emergencyRequests_recView);
        relativeLayout1 = findViewById(R.id.emergencyRequestFormat_dataNull);
        filterBtn = findViewById(R.id.emergencyRequestFormat_emptyFloatingFilterBtn);
        createReqBtn = findViewById(R.id.emergencyRequests_createReqBtn);
        emptyCreateReqBtn = findViewById(R.id.emergencyRequests_empty_createReqBtn);

    }

    /**
     * onStart() method is used to set listener for adapter.
     */
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    /**
     * onStop() method is used to set listener for adapter.
     */
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /**
     * onBackPressed() this activity will terminate the app.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        moveTaskToBack(true);
        android.os.Process.killProcess(myPid());
        System.exit(1);
    }

    /**
     * onNavigationItemSelected() function is declared to redirect respective activities based on their id.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(EmergencyRequests.this, EmergencyRequests.class);
                startActivity(intent);
                EmergencyRequests.this.finish();
                break;
            case R.id.filter:
                Intent intent1 = new Intent(EmergencyRequests.this, HomePage.class);
                startActivity(intent1);
                EmergencyRequests.this.finish();
                break;
            case R.id.donor_login:
                if (checkLoginCredential()) {
                    Intent intent2 = new Intent(EmergencyRequests.this, LoginPage.class);
                    //rollNo is passed to check for availability for user in database for sharedPreference purpose.
                    intent2.putExtra("rollNo", rollNo);
                    startActivity(intent2);
                    EmergencyRequests.this.finish();
                } else {
                    Intent intent3 = new Intent(EmergencyRequests.this, LoginPage.class);
                    rollNo = "123456";
                    //rollNo is passed to check for availability for user in database for sharedPreference purpose.
                    intent3.putExtra("rollNo", rollNo);
                    startActivity(intent3);
                    EmergencyRequests.this.finish();
                }
                break;
            case R.id.about_us:
                Intent intent2 = new Intent(EmergencyRequests.this, AboutUs.class);
                startActivity(intent2);
                EmergencyRequests.this.finish();
                break;
            case R.id.contact_us:
                Intent intent3 = new Intent(EmergencyRequests.this, ContactUs.class);
                startActivity(intent3);
                EmergencyRequests.this.finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * checks for SharedPreferences(Auto-Login)
     */
    private boolean checkLoginCredential() {

        int k = Character.compare(j[0], '1');
        if (k == 0)//while comparision of equal char return '0'.
        {
            return true;
        } else //else it returns -1 or 1 in integer.
        {
            return false;
        }
    }

}