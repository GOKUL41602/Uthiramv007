package com.uthiram.uthiramv007;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
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
import java.util.ArrayList;

import static com.uthiram.uthiramv007.R.string.navigation_draw_open;

/**
 * HomePage Activity displays the donor details registered in <b>UTHIRAM</b>  and this activity <br>
 * has options to call or send message to specified registered donor.
 */
public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // declaring all the used widgets and dataTypes in this activity.
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private Spinner deptNameSpinner, bloodGroupSpinner;
    private String deptName, bloodGroup;
    private RelativeLayout relativeLayout;
    private Button filterBtn;
    private HomePageRecAdapter adapter;
    private ProgressBar progressBar;
    String count1;
    int count;

    private String loginPath = "null";

    private String rollNoPath = "null";

    private String rollNo = "";

    private char[] j = new char[1];

    private char r;

    /**
     * This activity displays all the registered Donors in <b>UTHIRAM</b><br>
     * checks for loginCredentials when Login Button is clicked.<br>
     * Based on the Filter options this activity will be redirected to another Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //initializeViews() function is called
        initializeViews();
        //initializeSpinners() function is called
        initializeSpinners();

        //created two strings for retrieving both loginCredential text and rollNo path for sharedPreferences.
        loginPath = getExternalFilesDir("text").getAbsolutePath() + "/loginCredentials.txt";

        rollNoPath = getExternalFilesDir("text").getAbsolutePath() + "/rollNo.txt";
        //before loading the details from the firebase database progressbar visibility is set to visible.
        progressBar.setVisibility(View.VISIBLE);
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

        // Navigation Drawer is initialized and implemented.

        drawerLayout = findViewById(R.id.homePage_design_navigation_view);

        Toolbar toolbar = findViewById(R.id.homePage_toolbar);

        NavigationView navigationView = findViewById(R.id.homePage_nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        // using FirebaseRecyclerOptions class, requests in the EmergencyRequests Collection will be displayed in the recyclerView.
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //using addValueEventListener function, gets the total count in the EmergencyRequests Collection and stores in the local variable count.
        DatabaseReference databaseReference = firebaseDatabase.getReference("DonorsDto");
        //using addValueEventListener function, gets the total count in the EmergencyRequests Collection and stores in the local variable count.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long count = snapshot.getChildrenCount();
                if (count != 0) {
                    //if count is not zero, set the visibility of progressbar and relativeLayout1(emptyRelativeLayout)is gone and visibility of relativeLayout(if request is available) is set to visible.
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(HomePage.this, "No Records to Display !", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePage.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        //setLayoutManage() is used to display the list of data from database in the recycler view.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<DonorsDto> options
                = new FirebaseRecyclerOptions.Builder<DonorsDto>()
                .setQuery(FirebaseDatabase.getInstance().getReference("DonorsDto"), DonorsDto.class)
                .build();
        adapter = new HomePageRecAdapter(options, this);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);//to enable smooth scrolling.

        //filter button is used to filter the donors list based on bloodGroup and department Spinner.
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if either bloodGroup nor departmentName is selected
                if (bloodGroup.equals("Blood Group") && deptName.equals("Dept Name")) {
                    Snackbar.make(relativeLayout, "Please select Any Filter!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                } else {
                    //if  both bloodGroup and departmentName is selected
                    if (!bloodGroup.equals("Blood Group") && !deptName.equals("Dept Name")) {
                        Intent intent = new Intent(HomePage.this, FilteredHomePage.class);
                        intent.putExtra("1", deptName);
                        intent.putExtra("2", bloodGroup);
                        startActivity(intent);
                        //if  bloodGroup or departmentName is selected based on the selected spinner following else if statements.
                    } else if (!bloodGroup.equals("Blood Group")) {
                        //if only bloodGroup is selected.
                        Intent intent = new Intent(HomePage.this, FilteredBloodHomePage.class);
                        intent.putExtra("1", deptName);
                        intent.putExtra("2", bloodGroup);
                        startActivity(intent);
                    } else if (!deptName.equals("Dept Name")) {
                        //if only departmentName is selected.
                        Intent intent = new Intent(HomePage.this, FilteredDeptHomePage.class);
                        intent.putExtra("1", deptName);
                        intent.putExtra("2", bloodGroup);
                        startActivity(intent);
                    }
                }
            }

        });
    }

    /**
     * Initialization of spinners takes place here.
     */
    private void initializeSpinners() {
        // using ArrayList(bloodList) data for bloodGroupSpinner is set.
        ArrayList<String> bloodList = new ArrayList<String>();
        bloodList.add("Blood Group");
        bloodList.add("B+");
        bloodList.add("B-");
        bloodList.add("AB+");
        bloodList.add("AB-");
        bloodList.add("A+");
        bloodList.add("A-");
        bloodList.add("O+");
        bloodList.add("O-");

        //using ArrayAdapter(bloodAdapter) bloodList data's are set to bloodGroupSpinner.
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(HomePage.this, android.R.layout.simple_spinner_dropdown_item, bloodList);
        bloodGroupSpinner.setAdapter(bloodAdapter);
        //when data in spinner is changed it's value is stored in bloodGroup(String).
        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroup = bloodList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // using ArrayList(deptNameList) data for deptNameSpinner is set.

        ArrayList<String> deptNameList = new ArrayList<String>();

        deptNameList.add("Dept Name");
        deptNameList.add("IT");
        deptNameList.add("CSE");
        deptNameList.add("FT");
        deptNameList.add("EEE");
        deptNameList.add("ME");
        deptNameList.add("AU");
        deptNameList.add("AE");
        deptNameList.add("BT");
        deptNameList.add("CE");
        deptNameList.add("ECE");
        deptNameList.add("EIE");
        deptNameList.add("ISE");
        deptNameList.add("MC");
        deptNameList.add("TT");
        deptNameList.add("MCA");
        deptNameList.add("MBA");

        //using ArrayAdapter(deptAdapter) deptNameList data's are set to deptNameSpinner.
        ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(HomePage.this, android.R.layout.simple_spinner_dropdown_item, deptNameList);
        deptNameSpinner.setAdapter(deptAdapter);
        //when data in spinner is changed it's value is stored in deptName(String).
        deptNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deptName = deptNameList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Initialization of  all widgets used in this activity are defined by using the <b>findViewById()</b> method.
     */
    private void initializeViews() {
        recyclerView = findViewById(R.id.homePage_recView);
        deptNameSpinner = findViewById(R.id.homePage_deptSpinner);
        bloodGroupSpinner = findViewById(R.id.homePage_bloodGroupSpinner);
        relativeLayout = findViewById(R.id.homePage_relLayout);
        filterBtn = findViewById(R.id.homePage_filterBtn);
        progressBar = findViewById(R.id.homePage_progressBar);
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
     * onBackPressed() this activity will redirect to EmergencyRequest Activity.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Intent intent = new Intent(HomePage.this, EmergencyRequests.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // these three lines are used to check for finish previous activity.
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        HomePage.this.finish();
    }

    /**
     * onNavigationItemSelected() function is declared to redirect respective activities based on their id.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                Intent intent = new Intent(HomePage.this, EmergencyRequests.class);
                startActivity(intent);
                HomePage.this.finish();
                break;
            case R.id.filter:
                Intent intent1 = new Intent(HomePage.this, HomePage.class);
                startActivity(intent1);
                HomePage.this.finish();
                break;
            case R.id.donor_login:
                if (checkLoginCredential()) {
                    Intent intent2 = new Intent(HomePage.this, LoginPage.class);
                    intent2.putExtra("rollNo", rollNo);
                    startActivity(intent2);
                    HomePage.this.finish();
                } else {
                    Intent intent3 = new Intent(HomePage.this, LoginPage.class);
                    rollNo = "123456";
                    intent3.putExtra("rollNo", rollNo);
                    startActivity(intent3);
                    HomePage.this.finish();
                }
                break;
            case R.id.about_us:
                Intent intent3 = new Intent(HomePage.this, AboutUs.class);
                startActivity(intent3);
                HomePage.this.finish();
                break;
            case R.id.contact_us:
                Intent intent4 = new Intent(HomePage.this, ContactUs.class);
                startActivity(intent4);
                HomePage.this.finish();
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
        if (k == 0) {

            return true;
        } else {
            return false;
        }
    }
}